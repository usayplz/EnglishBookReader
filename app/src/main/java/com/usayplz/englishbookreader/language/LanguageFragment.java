package com.usayplz.englishbookreader.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.manager.DownloadManager;
import com.usayplz.englishbookreader.model.Dictionary;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.utils.DbUtils;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.view.ProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 24/03/16.
 * u.sayplz@gmail.com
 */
public class LanguageFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.list) ListView listView;
    @Bind(R.id.warning) TextView warningView;
    @Bind(R.id.next) Button nextView;

    private Lang lang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.language_list_item, R.id.lang, Lang.getTitles());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String title = (String) parent.getItemAtPosition(position);

            lang = Lang.getLangByTitle(title);
            
            if (lang != null) {
                Locale locale = new Locale(lang.name());
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

                warningView.setText(R.string.warning_download);
                warningView.setVisibility(View.VISIBLE);

                nextView.setText(R.string.button_continue);
                nextView.setVisibility(View.VISIBLE);
            }
        });

        nextView.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        if (lang == null) return;

        final ProgressDialog progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.progress_download_dictionary));

        File saveFile = FileUtils.concatToFile(getActivity().getCacheDir().getPath(), lang.zipFile);

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.download(saveFile, lang.url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(file -> {
                    if (file.exists()) {
                        try {
                            FileUtils.unzip(saveFile, getActivity().getCacheDir());
                        } catch (IOException e) {
                            Observable.error(new Exception());
                        }
                    }

                    File dbFile = FileUtils.concatToFile(getActivity().getCacheDir().getPath(), lang.dbFile);
                    if (dbFile.exists()) {
                        return dbFile;
                    } else {
                        Observable.error(new Exception("cannot unzip"));
                        return null;
                    }
                })
                .doOnNext(dbFile -> {
                    if (dbFile.exists()) {
                        try {
                            DbUtils.importTable(getContext(), dbFile.getPath(), Dictionary.TABLE);
                        } catch (Exception e) {
                            Observable.error(new Exception("cannot copy db"));
                        }
                    }
                })
                .subscribe(
                        file -> {
                            new UserData(getContext()).setFirstLaunch(false);
                            Intent intent = new Intent(getActivity(), LibraryActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        },
                        throwable -> {
                            Log.d(throwable.getMessage());
                            Toast.makeText(getContext(), R.string.error_download_dict, Toast.LENGTH_LONG).show();
                        },
                        progressDialog::dismiss
                );
    }
}

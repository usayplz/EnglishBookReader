package com.usayplz.englishbookreader.reading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseFragment;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesActivity;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.view.EBookView;
import com.usayplz.englishbookreader.view.MenuView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingFragment extends BaseFragment implements ReadingView, EBookView.EBookListener {
    @Bind(R.id.book) EBookView bookView;
    @Bind(R.id.left) View leftView;
    @Bind(R.id.right) View rightView;
    @Bind(R.id.top) View topView;
    @Bind(R.id.bottom) ImageView bottomView;
    @Bind(R.id.main) RelativeLayout mainView;

    private ReadingPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Views
        bookView.setListener(this);
        leftView.setOnClickListener(v -> onPrevious());
        rightView.setOnClickListener(v -> onNext());
        bottomView.setOnClickListener(v -> presenter.createMenu());

        // presenter
        presenter = new ReadingPresenter();
        presenter.attachView(this);
        presenter.getContent();
    }

    @Override
    public void showMenu(int page, int maxPage) {
        Log.d("page: " + page + ", max: " + maxPage);
        MenuView menuView = new MenuView(getActivity(), page, maxPage, new MenuView.IMenuView() {
            @Override
            public void onPageChanged(int page) {
                presenter.getContent(page);
            }

            @Override
            public void onMenuItemClicked(int id) {
                ReadingMenuItem readingMenuItem = ReadingMenuItem.byId(id);
                if (readingMenuItem == null) return;

                switch (readingMenuItem) {
                    case SETTINGS:
                        Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                        startActivityForResult(intent, PreferencesActivity.SETTINGS_CHANGED_REQUEST);
                        break;
                    case LIBRARY:
                        startActivity(new Intent(getActivity(), LibraryActivity.class));
                        break;
                    case NIGHTMODE:
                        break;
                    case EXIT:
                        getActivity().finish();
                        System.exit(0);
                        break;
                }
            }
        });

        menuView.show();
    }

    @Override
    public void showContent(File content, Settings settings, int page) {
        applySettings(settings);
        getActivity().runOnUiThread(() -> bookView.loadContent(content, settings, page));
    }

    private void applySettings(Settings settings) {
        mainView.setBackgroundColor(settings.getBackgroundColor());
        bookView.setBackgroundColor(settings.getBackgroundColor());

        rightView.getLayoutParams().width = settings.getMarginRight();
        bottomView.getLayoutParams().height = settings.getMarginBottom();
        leftView.getLayoutParams().width = settings.getMarginLeft();
        topView.getLayoutParams().height = settings.getMarginTop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.detachView();
    }

    @Override
    public void setPage(int page) {
        bookView.setPage(page);
    }

    @Override
    public void onTextSelected(String word) {
        Toast.makeText(getContext(), word, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext() {
        presenter.nextPage();
    }

    @Override
    public void onPrevious() {
        presenter.previousPage();
    }

    @Override
    public void onGetPageCount(int pagecount) {
        presenter.setPageCount(pagecount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PreferencesActivity.SETTINGS_CHANGED_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.getContent();
        }
    }
}

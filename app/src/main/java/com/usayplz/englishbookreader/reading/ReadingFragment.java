package com.usayplz.englishbookreader.reading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BaseFragment;
import com.usayplz.englishbookreader.libraly.LibraryActivity;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesActivity;
import com.usayplz.englishbookreader.view.BookView;
import com.usayplz.englishbookreader.view.MenuView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingFragment extends BaseFragment implements ReadingView, BookView.IBookListener {
    @Bind(R.id.book) BookView bookView;

    private ReadingPresenter presenter;
    private MenuView menuView;
    private boolean bugFixWebView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Views
        menuView = new MenuView();
        bookView.setListener(this);

        // presenter
        presenter = new ReadingPresenter();
        presenter.attachView(this);
        presenter.getContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.detachView();
    }

    @Override
    public void showMenu(int page, int maxPage) {
        if (menuView.isAdded()) {
            menuView.dismiss();
        }

        menuView.show(getActivity().getSupportFragmentManager(), page, maxPage, new MenuView.IMenuListener() {
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
                        startActivityForResult(intent, PreferencesActivity.REQUEST_SETTINGS_CHANGED);
                        break;
                    case LIBRARY:
                        startActivity(new Intent(getActivity(), LibraryActivity.class));
                        break;
                    case NIGHTMODE:
                        presenter.switchNightmode();
                        break;
                    case EXIT:
                        getActivity().finish();
                        System.exit(0);
                        break;
                }
                menuView.dismiss();
            }
        });
    }

    @Override
    public void showContent(File content, Settings settings, int page, int relativePage, int lastPage) {
        getActivity().runOnUiThread(() -> {
            int chapterPage = bugFixWebView ? ReadingPresenter.PAGE_BUG : page;
            bookView.loadContent(content, settings, chapterPage, relativePage, lastPage);
        });
    }

    @Override
    public void setPage(int page) {
        getActivity().runOnUiThread(() -> bookView.setPage(page));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PreferencesActivity.REQUEST_SETTINGS_CHANGED && resultCode == Activity.RESULT_OK) {
            presenter.getContent(true);
        }
    }

    // Implements BookView.IBookListener
    @Override
    public void onTextSelected(String word, String sentence) {
//        EnglishStemmer stemmer = new EnglishStemmer();
//        stemmer.setCurrent(word);
//        String result = stemmer.stem() ? stemmer.getCurrent() : word;
//
//        DictionaryDao dictionaryDao = new DictionaryDao(getContext());
//        Dictionary r = dictionaryDao.get((long) 10);
//        Log.d("w: " + r.getWord() + "t: " + r.getTerm());

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
    public void onSetPageCount(int pagecount) {
        if (bugFixWebView) {
            bugFixWebView = false;
            presenter.getContent();
        } else {
            presenter.setPageCount(pagecount);
        }
    }

    @Override
    public void onMenuClicked() {
        presenter.createMenu();
    }
}

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
import com.usayplz.englishbookreader.model.Chapter;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesActivity;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.view.BookView;
import com.usayplz.englishbookreader.view.ChapterView;
import com.usayplz.englishbookreader.view.MenuView;

import java.io.File;
import java.util.List;

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
        if (menuView == null) {
            menuView = new MenuView();
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
                    case CHAPTER:
                        presenter.createChapter();
                        break;
                    case SETTINGS:
                        Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                        startActivityForResult(intent, PreferencesActivity.SETTINGS_CHANGED_REQUEST);
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
    public void showChapters(int chapter, List<Chapter> chapters) {
        new ChapterView().show(getActivity().getSupportFragmentManager(), chapter, chapters, new ChapterView.IChapterListener() {
            @Override
            public void onChapterChanged(Chapter chapter1) {
                Log.d("chapter: " + chapter);
                presenter.getContent(chapter1);
            }
        });
    }

    @Override
    public void showContent(File content, Settings settings, int page) {
        getActivity().runOnUiThread(() -> bookView.loadContent(content, settings, page));

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
    public void onMenuClicked() {
        presenter.createMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PreferencesActivity.SETTINGS_CHANGED_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.getContent();
        }
    }
}

package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookSettings;
import com.usayplz.englishbookreader.preference.PreferencesManager;
import com.usayplz.englishbookreader.reading.manager.AbstractBookManager;
import com.usayplz.englishbookreader.utils.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingPresenter extends BasePresenter<ReadingView> {

    private Book book;
    private int page;
    private int pageCount;
    private int maxPageCount;
    private boolean isLoading = false;

    public ReadingPresenter(Book book) {
        this.book = book;
        this.page = book.getPage();
        this.pageCount = 0;
        this.maxPageCount = 0;
    }

    public void getContent() {
        if (isViewAttached()) {
            AbstractBookManager bookManager = AbstractBookManager.getBookManager(book.getType());
            PreferencesManager preferencesManager = new PreferencesManager();

            getView().showLoading(R.string.progress_message);
            isLoading = true;

            Observable.combineLatest(
                    bookManager.getContent(book),
                    preferencesManager.getPreferences(getView().getContext()),
                    (content, settings) -> {
                        page = book.getPage();

                        BookSettings bookSettings = new BookSettings();
                        bookSettings.setBook(book);
                        bookSettings.setContent(content);
                        bookSettings.setSettings(settings);
                        return bookSettings;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(bookSettings -> {
                        if (isViewAttached()) {
                            getView().showContent(bookSettings);
                        }
                    }, throwable -> {
                        Log.d(throwable.getMessage());
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showError(R.string.error_open_book);
                        }
                    });
        }
    }

    public void savePage(int page) {
        book.setPage(page);
        // TODO add save to db (update(book))
    }

    public void nextPage() {
        if (isLoading || !isViewAttached()) return;

        if (page >= pageCount) {
            if (book.getChapter() < book.getMaxChapter()) {
                book.setChapter(book.getChapter() + 1);
                book.setPage(0);
                getContent();
            }
        } else {
            page++;
            getView().setPage(page);
            savePage(page);
        }
    }

    public void previousPage() {
        if (isLoading || !isViewAttached()) return;

        if (page == 0) {
            if (book.getChapter() > 1) {
                book.setChapter(book.getChapter() - 1);
                book.setPage(-1);
                getContent();
            }
        } else {
            page--;
            getView().setPage(page);
            savePage(page);
        }
    }

    public void setPageCount(int pageCount) {
        isLoading = false;
        this.pageCount = pageCount;
        this.maxPageCount = maxPageCount + pageCount;
        if (this.page < 0) {
            this.page = pageCount;
            savePage(this.page);
        }

        if (isViewAttached()) {
            getView().hideLoading();
        }
    }
}

package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.Chapter;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesManager;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.manager.AbstractBookManager;
import com.usayplz.englishbookreader.utils.FileUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingPresenter extends BasePresenter<ReadingView> {
    private static final String BOOK_TEMPLATE = "html/template.html";

    private Settings settings;
    private AbstractBookManager bookManager;
    private Book book;
    private boolean isLoading = false;

    public ReadingPresenter() {
    }

    public void getContent() {
        if (getView() != null) {
            this.isLoading = true; // false in setPageCount

            // Init classes
            initialize();

            if (!bookManager.isReady(book)) {
                getView().showLoading(R.string.progress_loading_book);
            }

            String template = FileUtils.loadAsset(getView().getContext(), BOOK_TEMPLATE);
            bookManager
                    .getContent(book, template)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(file -> {
                                if (getView() != null) {
                                    getView().showContent(file, settings, book.getPage());
                                }
                            },
                            throwable -> {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().showError(R.string.error_open_book);
                                }
                            }
                    );
        }
    }

    private void initialize() {
        if (getView() != null) {
            if (book == null) {
                long id = new UserData(getView().getContext()).getBookId();
                book = new BookDao(getView().getContext()).get(id);
                bookManager = AbstractBookManager.getBookManager(book.getType());
            }

            if (settings == null) {
                PreferencesManager preferencesManager = new PreferencesManager();
                this.settings = preferencesManager.getPreferences(getView().getContext());
            }
        }
    }

    public void getContent(int page) {
        if (getView() != null) {
            book.setPage(page);
            getView().setPage(book.getPage());
        }
    }

    public void getContent(Chapter chapter) {
        book.setPage(Book.FIRST_PAGE);
        book.setChapter(chapter.getId());
        getContent();
    }

    @Override
    public void detachView() {
        saveBook();
        super.detachView();
    }

    public void saveBook() {
        if (getView() != null) {
            new BookDao(getView().getContext()).update(book);
        }
    }

    public void nextPage() {
        if (isLoading || getView() == null) return;

        if (book.getPage() < book.getLastPage()) {
            book.setPage(book.getPage() + 1);
            getView().setPage(book.getPage());
        } else if (book.getChapter() < book.getLastChapter()) {
            book.setChapter(book.getChapter() + 1);
            book.setPage(Book.FIRST_PAGE);
            getContent();
        }
    }

    public void previousPage() {
        if (isLoading || getView() == null) return;

        if (book.getPage() > Book.FIRST_PAGE) {
            book.setPage(book.getPage() - 1);
            getView().setPage(book.getPage());
        } else if (book.getChapter() > 0) {
            book.setChapter(book.getChapter() - 1);
            book.setPage(Book.LAST_PAGE);
            getContent();
        }
    }

    public void setPageCount(int lastPage) {
        this.isLoading = false;
        book.setLastPage(lastPage);
        if (book.getPage() == Book.LAST_PAGE) {
            book.setPage(lastPage);
        }

        if (getView() != null) {
            getView().hideLoading();
        }
    }

    public void createMenu() {
        if (getView() != null && book.getLastPage() >= 0) {
            getView().showMenu(book.getPage(), book.getLastPage());
        }
    }

    public void createMenuChapters() {
        if (getView() != null) {
            initialize();
            getView().showChapters(book.getChapter(), bookManager.getChapters(book.getFile()));
        }
    }

    public void switchNightmode() {
        if (getView() != null) {
            PreferencesManager preferencesManager = new PreferencesManager();
            preferencesManager.switchNightmode(getView().getContext());
            this.settings = preferencesManager.getPreferences(getView().getContext());
            getContent();
        }
    }
}

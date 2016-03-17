package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesManager;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.manager.AbstractBookManager;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.utils.Strings;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingPresenter extends BasePresenter<ReadingView> {
    private static final String BOOK_TEMPLATE = "html/template.html";
    public static final int PAGE_LAST = -1;
    public static final int PAGE_BUG = -2;
    public static final int PAGE_COUNTING = -3;

    private Settings settings;
    private AbstractBookManager bookManager;
    private Book book;
    private boolean isLoading = false;
    private int currentPage;
    private int currentLastPage;
    private int currentChapter;

    public ReadingPresenter() {
        currentLastPage = 0;
    }

    public void getContent() {
        if (getView() != null) {
            this.isLoading = true; // false in setPageCount

            // Init classes
            if (book == null) {
                long id = new UserData(getView().getContext()).getBookId();
                book = new BookDao(getView().getContext()).get(id);
                bookManager = AbstractBookManager.getBookManager(book.getType());
            }

            if (settings == null) {
                settings = new PreferencesManager().getPreferences(getView().getContext());
            }

            if (book.getLastPage() == 0) { // PAGE_COUNTING fucking cheat, see setLastPage
                getView().showLoading(R.string.progress_loading_book);
                currentPage = book.getPage() > 0 ? book.getPage() : Book.FIRST_PAGE;
                currentChapter = book.getChapter();
                book.setPage(PAGE_COUNTING);
                book.setChapter(0);
            }

            String template = FileUtils.loadAsset(getView().getContext(), BOOK_TEMPLATE);
            bookManager
                    .getContent(book, template)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(file -> {
                                if (getView() != null) {
                                    getView().showContent(file, settings, book.getPage(), getRelativePage(), book.getLastPage());
                                }
                            },
                            throwable -> {
                                Log.d(throwable.getMessage());
                                if (getView() != null) {
                                    this.isLoading = false;
                                    getView().hideLoading();
                                    getView().showError(R.string.error_open_book);
                                }
                            }
                    );
        }
    }

    // set relative page
    public void getContent(int page) {
        int chapter_page_count = 0;
        int chapter = 0;
        String[] chapters = book.getChaptersCount().split(",");
        for (int i = 0; i < chapters.length; i++) {
            int count = Integer.valueOf(chapters[i]);
            if (chapter_page_count + count >= page || i+1 == chapters.length) {
                chapter = i;
                break;
            }
            chapter_page_count += count;
        }

        book.setPage(page - chapter_page_count);
        book.setChapter(chapter);
        getContent();
    }

    // recount pages
    public void getContent(boolean recount) {
        if (recount && getView() != null) {
            settings = new PreferencesManager().getPreferences(getView().getContext());
            book.setLastPage(0);
            book.setChaptersCount("");
            getContent();
        }
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

        if (book.getPage() < currentLastPage) {
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
            book.setPage(PAGE_LAST);
            getContent();
        }
    }

    public void setPageCount(int lastPage) {
        if (book.getPage() == PAGE_COUNTING) {
            book.setLastPage(book.getLastPage() + lastPage);
            book.setChaptersCount(Strings.isEmpty(book.getChaptersCount()) ? String.valueOf(lastPage) : book.getChaptersCount() + "," + lastPage);

            if (book.getChapter() == book.getLastChapter()) {
                book.setPage(currentPage);
                book.setChapter(currentChapter);
            } else {
                book.setChapter(book.getChapter() + 1);
            }

            getContent();
            return;
        }

        this.isLoading = false;
        this.currentLastPage = lastPage;
        if (book.getPage() == PAGE_LAST) {
            book.setPage(lastPage);
        }

        if (getView() != null) {
            getView().hideLoading();
        }
    }

    public void createMenu() {
        if (getView() != null && book.getLastPage() >= 0) {
            getView().showMenu(getRelativePage() + book.getPage(), book.getLastPage());
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

    // sum all chapters before current
    private int getRelativePage() {
        int result = 0;
        String[] chapters = book.getChaptersCount().split(",");
        for (int i = 0; i < book.getChapter(); i++) {
            int count = Integer.valueOf(chapters[i]);
            result += count;
        }
        return result;
    }
}

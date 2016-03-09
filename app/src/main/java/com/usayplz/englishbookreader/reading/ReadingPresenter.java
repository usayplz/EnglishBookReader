package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.db.ChapterDao;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.Chapter;
import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.preference.PreferencesManager;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.manager.AbstractBookManager;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.utils.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class ReadingPresenter extends BasePresenter<ReadingView> {
    private static final String BOOK_TEMPLATE = "html/template.html";
    public static final int PAGE_COUNTING_FLAG = -1000;

    private Settings settings;
    private AbstractBookManager bookManager;
    private Book book;
    private int pageCount;
    private int maxPageCount;
    private boolean isLoading = false;
    private boolean isCounting = false;
    private int chapter_index;
    private List<File> files;
    private List<Chapter> chapters;

    public ReadingPresenter() {
        this.pageCount = 0;
        this.maxPageCount = 0;
    }

    public void getContent() {
        if (getView() != null) {
            this.isLoading = true; // false in setPageCount

            // Init objects
            if (book == null) {
                long id = new UserData(getView().getContext()).getBookId();
                book = new BookDao(getView().getContext()).get(id);
                bookManager = AbstractBookManager.getBookManager(book.getType());
            }

            if (settings == null) {
                PreferencesManager preferencesManager = new PreferencesManager();
                this.settings = preferencesManager.getPreferences(getView().getContext());
            }

            if (book.getMaxPageCount() == 0) {
                getView().showLoading(R.string.progress_loading_book);
                String template = FileUtils.loadAsset(getView().getContext(), BOOK_TEMPLATE);
                this.files = bookManager.process(book, template);
                this.chapter_index = files.size();
                this.isCounting = true;
                this.chapters = new ArrayList<>();
                nextFileCounting();
            } else {
                if (chapters == null || chapters.size() == 0) {
                    ChapterDao chapterDao = new ChapterDao(getView().getContext());
                    chapterDao.getByBook(book.getId())
                            .subscribe(chapters -> {
                                this.chapters = chapters;

                                File chapterFile = bookManager.getChapterFile(book.getDir(), book.getChapter());
                                getView().showContent(chapterFile, settings, getCurrentPage(), book.getMaxPageCount());
                            });
                } else {
                    File chapterFile = bookManager.getChapterFile(book.getDir(), book.getChapter());
                    getView().showContent(chapterFile, settings, getCurrentPage(), book.getMaxPageCount());
                }
            }
        }
    }

    private int getCurrentPage() {
        int count = 0;
        for (int i = 0; i < chapters.size(); i++) {
            count += chapters.get(i).getPageCount();
            if (count > book.getPage()) {
                return count - book.getPage();
            }
        }
        return 0;
    }

    private void nextFileCounting() {
        chapter_index--;
        if (chapter_index < 0) {
            isCounting = false;
            book.setMaxPageCount(maxPageCount);
            saveBook();
            saveChapters();
            getContent();
            return;
        }

        if (getView() != null) {
            getView().showContent(files.get(chapter_index), settings, PAGE_COUNTING_FLAG, 0);
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

    public void saveChapters() {
        if (getView() != null) {
            ChapterDao chapterDao = new ChapterDao(getView().getContext());
            chapterDao.removeByBook(book.getId());
            chapterDao.addAll(chapters);
        }
    }

    public void nextPage() {
        if (isLoading || getView() == null) return;

        if (book.getPage() >= pageCount) {
            if (book.getChapter() < book.getMaxChapter()) {
                book.setChapter(book.getChapter() + 1);
                book.setPage(0);
                getContent();
            }
        } else {
            book.setPage(book.getPage() + 1);
            getView().setPage(book.getPage());
        }
    }

    public void previousPage() {
        if (isLoading || getView() == null) return;

        if (book.getPage() == 0) {
            if (book.getChapter() > 0) {
                book.setChapter(book.getChapter() - 1);
                book.setPage(-1);
                getContent();
            }
        } else {
            book.setPage(book.getPage() - 1);
            getView().setPage(book.getPage());
        }
    }

    public void setPageCount(int pageCount) {
        if (isCounting) {
            chapters.add(new Chapter(book.getId(), chapter_index, pageCount));
            this.maxPageCount += pageCount;
            nextFileCounting();
            return;
        }

        this.isLoading = false;
        this.pageCount = pageCount;

        if (book.getPage() < 0) {
            book.setPage(pageCount);
        }

        if (getView() != null) {
            getView().hideLoading();
        }
    }

    public void createMenu() {
        if (getView() != null && book.getMaxPageCount() > 0) {
            getView().showMenu(book.getPage() + 1, book.getMaxPageCount() + 1);
        }
    }

    public void getContent(int page) {
        int count = 0;
        Log.d("size: " + chapters.size());
        for (int i = 0; i < chapters.size(); i++) {
            count += chapters.get(i).getPageCount();
            Log.d(String.format("chapter %s, page: %s", chapters.get(i).getPageCount(), chapters.get(i).getChapter()));
            if (count > page) {
                book.setPage(count - page);
                book.setChapter(chapters.get(i).getChapter());
                Log.d(String.format("chapter %s, page: %s", book.getPage(), book.getChapter()));
                break;
            }
        }

        this.getContent();
    }
}

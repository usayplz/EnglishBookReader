package com.usayplz.englishbookreader.libraly;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.manager.AbstractBookManager;
import com.usayplz.englishbookreader.manager.ScanDriveEngine;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.utils.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class LibraryPresenter extends BasePresenter<LibraryView> {

    public LibraryPresenter() {
    }

    public void getContent() {
        if (getView() != null) {
            UserData userData = new UserData(getView().getContext());
            if (userData.getScanned()) {
                getBooksDb();
            } else {
                scanDrives();
            }
        }
    }

    public void openBook(Book book) {
        if (getView() != null) {
            UserData userData = new UserData(getView().getContext());
            userData.setBookId(book.getId());
            getView().openBook();
        }
    }

    public void getBooksDb() {
        if (getView() != null) {
            getView().showLoading(R.string.progress_message);

            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.getAll()
                    .subscribe(
                            books -> {
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().showContent(books);
                                }
                            },
                            throwable -> {
                                Log.d(throwable.getMessage());
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().showError(R.string.error_load_books);
                                }
                            }
                    );
        }
    }

    public void scanDrives() {
        if (getView() != null) {
            getView().showLoading(R.string.progress_message);

            String filesDir = getView().getContext().getFilesDir().getPath();

            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.getAll()
                    .subscribe(
                            books -> {
                                if (getView() != null) {
                                    for (Book book : books) {
                                        File bookFile = new File(book.getFile());
                                        if (!bookFile.exists()) {
                                            bookDao.remove(book.getId());
                                        }
                                    }
                                }
                            },
                            throwable -> {
                                Log.d(throwable.getMessage());
                                if (getView() != null) {
                                    getView().hideLoading();
                                    getView().showError(R.string.error_load_books);
                                }
                            }
                    );

            ScanDriveEngine scanDriveEngine = new ScanDriveEngine();
            scanDriveEngine.find()
                    .map(file -> {
                        Book book = new Book();
                        book.setFile(file.getPath());
                        book.setType(BookType.byExtension(book.getFile()));

                        AbstractBookManager bookManager = AbstractBookManager.getBookManager(book.getType());
                        if (bookManager != null) {
                            book = bookManager.getBookInfo(book.getFile(), filesDir);
                        }

                        return book;
                    })
                    .filter(book -> book != null && !Strings.isEmpty(book.getTitle()))
                    .filter(book -> {
                        Book bookDb = bookDao.getByFile(book.getFile());
                        return bookDb == null; // don't add if exits
                    })
                    .doOnNext(bookDao::add)
                    .doOnCompleted(() -> {
                        if (getView() != null) {
                            new UserData(getView().getContext()).setScanned(true);
                            getBooksDb();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(); // TODO need to show errors?
        }
    }

    public void filterBooks(String query) {
        final String queryLowerCase = query.toLowerCase();

        if (getView() != null) {
            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.getAll()
                    .map(books -> {
                        List<Book> result = new ArrayList<>();
                        for (Book book : books) {
                            if (book.getTitle().toLowerCase().contains(queryLowerCase)) {
                                result.add(book);
                            }
                        }
                        return result;
                    })
                    .subscribe(
                            books -> {
                                if (getView() != null) {
                                    getView().showContent(books);
                                }
                            },
                            throwable -> {
                                Log.d(throwable.getMessage());
                            }
                    );
        }
    }
}

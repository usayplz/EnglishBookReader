package com.usayplz.englishbookreader.libraly;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.reading.manager.AbstractBookManager;
import com.usayplz.englishbookreader.utils.Log;

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
        if (isViewAttached()) {
            UserData userData = new UserData(getView().getContext());
            if (userData.getScanned()) {
                getBooksFromDb();
            } else {
                findBooks();
            }
        }
    }

    public void onOpenBook(Book book) {
        Log.d("onOpenBook: " + book.getTitle());
        if (isViewAttached()) {
            UserData userData = new UserData(getView().getContext());
            userData.setBookId(book.getId());
            getView().openBook(book.getId());
        }
    }

    public void getBooksFromDb() {
        if (isViewAttached()) {
            getView().showLoading(R.string.progress_message);

            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.getAll().subscribe(
                    books -> {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            if (books.size() > 0) {
                                getView().showContent(books);
                            } else {
                                getView().showEmpty();
                            }
                        }
                    },
                    throwable -> {
                        Log.d(throwable.getMessage());
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showError(R.string.error_load_books);
                        }
                    }

            );
        }
    }

    public void findBooks() {
        if (isViewAttached()) {
            getView().showLoading(R.string.progress_message);

            String filesDir = getView().getContext().getFilesDir().getPath();
            String default_authors = getView().getContext().getString(R.string.default_book_authors);
            String default_title = getView().getContext().getString(R.string.default_book_title);

            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.removeAll();

            BookFindEngine bookFindEngine = new BookFindEngine();
            bookFindEngine.find()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(book -> {
                        book.setType(AbstractBookManager.getBookType(book.getFile()));
                        AbstractBookManager bookManager = AbstractBookManager.getBookManager(book.getType());

                        book = bookManager.getBookInfo(book.getFile(), filesDir, default_authors, default_title);
                        return book;
                    })
                    .filter(book -> book != null)
                    .doOnNext(bookDao::add)
                    .doOnCompleted(this::getBooksFromDb)
                    .subscribe();
        }
    }
}

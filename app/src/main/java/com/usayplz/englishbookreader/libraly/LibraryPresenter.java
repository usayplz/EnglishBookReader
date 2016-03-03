package com.usayplz.englishbookreader.libraly;

import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.base.BasePresenter;
import com.usayplz.englishbookreader.db.BookDao;
import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.preference.UserData;
import com.usayplz.englishbookreader.utils.Log;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class LibraryPresenter extends BasePresenter<LibraryView> {

    public LibraryPresenter() {
    }

    public void getContent() {
        if (isViewAttached()) {
            getView().showLoading(R.string.progress_message);
            BookDao bookDao = new BookDao(getView().getContext());
            bookDao.getAll().subscribe(
                    books -> {
                        if (isViewAttached()) {
                            getView().showContent(books);
                            getView().hideLoading();
                        }
                    },
                    throwable -> {
                        Log.d(throwable.getMessage());
                        if (isViewAttached()) {
                            getView().showError(R.string.error_load_books);
                            getView().hideLoading();
                        }
                    }

            );
        }
    }

    public void loadBook(Book book) {
        if (isViewAttached()) {
            UserData userData = new UserData(getView().getContext());
            userData.setBookId(book.getId());
            getView().showBook(book.getId());
        }
    }
}

package com.usayplz.englishbookreader.db;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.usayplz.englishbookreader.model.Book;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class BookDao {
    private BriteDatabase db;

    public BookDao(Context context) {
        db = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.io());
    }

    public Observable<List<Book>> getAll() {
        return db.createQuery(Book.TABLE, "select * from " + Book.TABLE)
                .mapToList(Book.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Book> get(int id) {
        return db.createQuery(Book.TABLE, "select * from " + Book.TABLE)
                .mapToOne(Book.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void add(Book book) {
        db.insert(Book.TABLE, book.getContentValues());
    }

    public void addAll(List<Book> books) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        for (Book book : books) {
            db.insert(Book.TABLE, book.getContentValues());
        }
        transaction.markSuccessful();
        transaction.close();
    }

    public void remove(Long id) {
        db.delete(Book.TABLE, "id = ?", id.toString());
    }

    public void removeAll() {
        db.delete(Book.TABLE, "");
    }
}

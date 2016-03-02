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

    public Observable<Long> add(Book book) {
        return Observable.just(db.insert(Book.TABLE, book.getContentValues()));
    }

    public Observable<Integer> remove(Long id) {
        return Observable.just(db.delete(Book.TABLE, "id = ?", id.toString()));
    }

    public Observable<Integer> removeAll() {
        return Observable.just(db.delete(Book.TABLE, ""));
    }
}

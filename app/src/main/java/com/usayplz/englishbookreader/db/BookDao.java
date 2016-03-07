package com.usayplz.englishbookreader.db;

import android.content.Context;
import android.database.Cursor;

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
        return db.createQuery(Book.TABLE, "select * from " + Book.TABLE + " order by " + Book.COL_TITLE)
                .mapToList(Book.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Book get(Long id) {
        Cursor cursor = db.query("select * from " + Book.TABLE+" where " + Book.COL_ID + " = ?", id.toString());
        if (!cursor.moveToFirst()) return null;
        return Book.MAPPER.call(cursor);
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
        db.delete(Book.TABLE, Book.COL_ID + " = ?", id.toString());
    }

    public void removeAll() {
        db.delete(Book.TABLE, "");
    }

    public void update(Book book) {
        db.update(Book.TABLE, book.getContentValues(), Book.COL_ID + " = ?", String.valueOf(book.getId()));
    }
}

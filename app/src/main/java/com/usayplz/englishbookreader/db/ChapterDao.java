package com.usayplz.englishbookreader.db;

import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.usayplz.englishbookreader.model.Chapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 08/03/16.
 * u.sayplz@gmail.com
 */
public class ChapterDao {
    private BriteDatabase db;

    public ChapterDao(Context context) {
        db = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.io());
    }

    public Observable<List<Chapter>> getByBook(long bookId) {
        return db.createQuery(Chapter.TABLE, String.format("select * from %s where %s = %s order by %s", Chapter.TABLE, Chapter.COL_BOOK_ID, bookId, Chapter.COL_CHAPTER))
                .mapToList(Chapter.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Chapter get(Long bookId, Integer chapter) {
        Cursor cursor = db.query(String.format("select * from %s where %s = %s and %s = %s", Chapter.TABLE, Chapter.COL_BOOK_ID, bookId, Chapter.COL_CHAPTER, chapter));
        if (!cursor.moveToFirst()) return null;
        return Chapter.MAPPER.call(cursor);
    }

    public void add(Chapter chapter) {
        db.insert(Chapter.TABLE, chapter.getContentValues());
    }

    public void addAll(List<Chapter> chapters) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        for (Chapter chapter : chapters) {
            db.insert(Chapter.TABLE, chapter.getContentValues());
        }
        transaction.markSuccessful();
        transaction.close();
    }

    public void remove(Long id) {
        db.delete(Chapter.TABLE, Chapter.COL_ID + " = ?", id.toString());
    }

    public void removeAll() {
        db.delete(Chapter.TABLE, "");
    }

    public void update(Chapter chapter) {
        db.update(Chapter.TABLE, chapter.getContentValues(), Chapter.COL_ID + " = ?", String.valueOf(chapter.getId()));
    }
}

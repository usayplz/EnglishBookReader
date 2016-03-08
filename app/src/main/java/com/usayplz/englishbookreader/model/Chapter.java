package com.usayplz.englishbookreader.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.usayplz.englishbookreader.db.Db;

import rx.functions.Func1;

/**
 * Created by Sergei Kurikalov on 08/03/16.
 * u.sayplz@gmail.com
 */
public class Chapter {
    public static final String TABLE = "chapter";
    public static final String COL_ID = "_id";
    public static final String COL_BOOK_ID = "book_id";
    public static final String COL_CHAPTER = "chapter";
    public static final String COL_PAGECOUNT = "page_count";

    public static final String CREATE_TABLE = ""
            + "CREATE TABLE " + TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_BOOK_ID + " INTEGER,"
            + COL_CHAPTER + " INTEGER,"
            + COL_PAGECOUNT + " INTEGER"
            + ")";

    private long id;
    private long bookId;
    private int chapter;
    private int pageCount;

    public static final Func1<Cursor, Chapter> MAPPER = cursor -> {
        Chapter chapter = new Chapter();
        chapter.id = Db.getLong(cursor, COL_ID);
        chapter.bookId = Db.getLong(cursor, COL_BOOK_ID);
        chapter.chapter = Db.getInt(cursor, COL_CHAPTER);
        chapter.pageCount = Db.getInt(cursor, COL_PAGECOUNT);
        return chapter;
    };

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID, bookId);
        values.put(COL_CHAPTER, chapter);
        values.put(COL_PAGECOUNT, pageCount);

        return values;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}

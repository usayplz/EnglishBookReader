package com.usayplz.englishbookreader.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.usayplz.englishbookreader.db.Db;

import java.io.Serializable;

import rx.functions.Func1;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class Book implements Serializable {
    public static final int PAGE_FIRST = 1;

    public static final String TABLE = "book";
    public static final String COL_ID = "_id";
    public static final String COL_TYPE = "type_code";
    public static final String COL_FILE = "file";
    public static final String COL_DIR = "dir";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_COVERIMAGE = "cover_image";
    public static final String COL_PAGE = "page";
    public static final String COL_CHAPTER = "chapter";
    public static final String COL_LASTPAGE = "last_page";
    public static final String COL_LASTCHAPTER = "last_chapter";
    public static final String COL_CHAPTERSCOUNT = "chapters_count";

    public static final String CREATE_TABLE = ""
            + "CREATE TABLE " + TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_TYPE + " INTEGER,"
            + COL_FILE + " TEXT,"
            + COL_DIR + " TEXT,"
            + COL_TITLE + " TEXT,"
            + COL_AUTHOR + " TEXT,"
            + COL_COVERIMAGE + " TEXT,"
            + COL_PAGE + " INTEGER,"
            + COL_CHAPTER + " INTEGER,"
            + COL_LASTPAGE + " INTEGER,"
            + COL_LASTCHAPTER + " INTEGER,"
            + COL_CHAPTERSCOUNT + " TEXT"
            + ")";

    private long id;
    private BookType type;
    private int typeCode;
    private String file;
    private String dir;
    private String title;
    private String author;
    private String coverImage;
    private int page;
    private int chapter;
    private int lastPage;
    private int lastChapter;
    private String chaptersCount;

    public static final Func1<Cursor, Book> MAPPER = cursor -> {
        Book book = new Book();
        book.id = Db.getLong(cursor, COL_ID);
        book.typeCode = Db.getInt(cursor, COL_TYPE);
        book.type = BookType.byCode(book.typeCode);
        book.file = Db.getString(cursor, COL_FILE);
        book.dir = Db.getString(cursor, COL_DIR);
        book.title = Db.getString(cursor, COL_TITLE);
        book.author = Db.getString(cursor, COL_AUTHOR);
        book.coverImage = Db.getString(cursor, COL_COVERIMAGE);
        book.page = Db.getInt(cursor, COL_PAGE);
        book.chapter = Db.getInt(cursor, COL_CHAPTER);
        book.lastPage = Db.getInt(cursor, COL_LASTPAGE);
        book.lastChapter = Db.getInt(cursor, COL_LASTCHAPTER);
        book.chaptersCount = Db.getString(cursor, COL_CHAPTERSCOUNT);
        return book;
    };

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
//        values.put(COL_ID, id);
        values.put(COL_TYPE, typeCode);
        values.put(COL_FILE, file);
        values.put(COL_DIR, dir);
        values.put(COL_TITLE, title);
        values.put(COL_AUTHOR, author);
        values.put(COL_COVERIMAGE, coverImage);
        values.put(COL_PAGE, page);
        values.put(COL_CHAPTER, chapter);
        values.put(COL_LASTPAGE, lastPage);
        values.put(COL_LASTCHAPTER, lastChapter);
        values.put(COL_CHAPTERSCOUNT, chaptersCount);

        return values;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
        this.typeCode = type.getCode();
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
        this.type = BookType.byCode(typeCode);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(int lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(String chaptersCount) {
        this.chaptersCount = chaptersCount;
    }
}

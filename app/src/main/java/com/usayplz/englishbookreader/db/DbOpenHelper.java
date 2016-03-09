package com.usayplz.englishbookreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.usayplz.englishbookreader.model.Book;

/**
 * Created by Sergei Kurikalov on 05/12/15.
 * u.sayplz@gmail.com
 */
public final class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reader.db";
    private static final int DATABASE_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Book.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
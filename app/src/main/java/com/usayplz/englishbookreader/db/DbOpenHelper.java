package com.usayplz.englishbookreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.Dictionary;

/**
 * Created by Sergei Kurikalov on 05/12/15.
 * u.sayplz@gmail.com
 */
public final class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "reader.db";
    private static final int DB_VERSION = 1;

    private Context context;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Book.CREATE_TABLE);
        db.execSQL(Dictionary.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
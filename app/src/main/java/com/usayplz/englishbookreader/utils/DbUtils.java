package com.usayplz.englishbookreader.utils;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.usayplz.englishbookreader.db.DbOpenHelper;

import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 25/03/16.
 * u.sayplz@gmail.com
 */
public class DbUtils {
    public static void importTable(Context context, String dbFile, String table) {
        BriteDatabase db = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.io());
        db.execute("ATTACH DATABASE '" + dbFile + "' AS NEW_DB");
        db.execute("DROP TABLE IF EXISTS " + table);
        db.execute("CREATE TABLE " + table + " AS SELECT * FROM NEW_DB." + table);
        db.execute("DETACH NEW_DB");
        db.close();
    }
}

package com.usayplz.englishbookreader.db;

import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.usayplz.englishbookreader.model.Dictionary;

import rx.schedulers.Schedulers;

/**
 * Created by Sergei Kurikalov on 25/03/16.
 * u.sayplz@gmail.com
 */
public class DictionaryDao {
    private BriteDatabase db;

    public DictionaryDao(Context context) {
        db = SqlBrite.create().wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.io());
    }

    public void removeAll() {
        db.delete(Dictionary.TABLE, "");
    }

    public Dictionary get(Long id) {
        Cursor cursor = db.query("select * from " + Dictionary.TABLE + " where " + Dictionary.COL_ID + " = ?", id.toString());
        if (!cursor.moveToFirst()) return null;
        return Dictionary.MAPPER.call(cursor);
    }
}

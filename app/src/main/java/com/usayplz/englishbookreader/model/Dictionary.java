package com.usayplz.englishbookreader.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.usayplz.englishbookreader.db.Db;

import rx.functions.Func1;

/**
 * Created by Sergei Kurikalov on 25/03/16.
 * u.sayplz@gmail.com
 */
public class Dictionary {
    public static final String TABLE = "dictionary";
    public static final String COL_ID = "id";
    public static final String COL_WORD = "word";
    public static final String COL_TERM = "term";

    public static final String CREATE_TABLE = ""
            + "CREATE TABLE " + TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_WORD + " TEXT,"
            + COL_TERM + " TEXT"
            + ")";

    private long id;
    private String word;
    private String term;

    public static final Func1<Cursor, Dictionary> MAPPER = cursor -> {
        Dictionary dictionary = new Dictionary();
        dictionary.id = Db.getLong(cursor, COL_ID);
        dictionary.word = Db.getString(cursor, COL_WORD);
        dictionary.term = Db.getString(cursor, COL_TERM);
        return dictionary;
    };

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
//        values.put(COL_ID, id);
        values.put(COL_WORD, word);
        values.put(COL_TERM, term);

        return values;
    }

    public Dictionary() {}

    public Dictionary(String word, String term) {
        this.word = word;
        this.term = term;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}

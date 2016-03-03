package com.usayplz.englishbookreader.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public class UserData {
    private SharedPreferences userData;

    private static final String APP_PREF_USER_DATA = "user_data";
    public static final String APP_PREF_BOOK_ID = "book_id";

    public UserData(Context context) {
        userData = context.getSharedPreferences(APP_PREF_USER_DATA, Context.MODE_PRIVATE);
    }

    public void setBookId(Long id) {
        userData.edit().remove(APP_PREF_BOOK_ID).apply();
        if (id != null) userData.edit().putLong(APP_PREF_BOOK_ID, id).apply();
    }

    public Long getBookId() {
        return userData.getLong(APP_PREF_BOOK_ID, -1);
    }
}

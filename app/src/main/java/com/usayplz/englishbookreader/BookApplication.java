package com.usayplz.englishbookreader;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.usayplz.englishbookreader.db.DbOpenHelper;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public class BookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Preference
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);

        // DB
        new DbOpenHelper(this);
    }

    public static BookApplication get(Context context) {
        return (BookApplication) context.getApplicationContext();
    }

}

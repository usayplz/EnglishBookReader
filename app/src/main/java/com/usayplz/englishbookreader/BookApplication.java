package com.usayplz.englishbookreader;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public class BookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
    }

    public static BookApplication get(Context context) {
        return (BookApplication) context.getApplicationContext();
    }

}

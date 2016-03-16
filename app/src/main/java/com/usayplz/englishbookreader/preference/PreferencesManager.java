package com.usayplz.englishbookreader.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.usayplz.englishbookreader.BookApplication;
import com.usayplz.englishbookreader.R;
import com.usayplz.englishbookreader.model.Settings;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class PreferencesManager {

    public Settings getPreferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(BookApplication.get(context));
        Resources res = context.getResources();

        Settings settings = new Settings();
        settings.setMargin(Integer.valueOf(pref.getString(res.getString(R.string.pref_margin), res.getString(R.string.pref_margin_default))));
        settings.setFontFamily(pref.getString(res.getString(R.string.pref_font_family), res.getString(R.string.pref_font_family_default)));
        settings.setFontSize(Integer.valueOf(pref.getString(res.getString(R.string.pref_font_size), res.getString(R.string.pref_font_size_default))));
        settings.setFontColor(pref.getInt(res.getString(R.string.pref_font_color), res.getColor(R.color.day)));
        settings.setBackgroundColor(pref.getInt(res.getString(R.string.pref_background_color), res.getColor(R.color.night)));
        settings.setNightmode(pref.getBoolean(res.getString(R.string.pref_nightmode), false));

        return settings;
    }

    public void changeNightmode(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(BookApplication.get(context));
        Resources res = context.getResources();

        boolean nightmode = !pref.getBoolean(res.getString(R.string.pref_nightmode), false); // !nightmode
        int fontColor = res.getColor(R.color.day);
        int backgroundColor = res.getColor(R.color.night);
        if (nightmode) {
            fontColor = res.getColor(R.color.night);
            backgroundColor = res.getColor(R.color.day);
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(res.getString(R.string.pref_nightmode), nightmode);
        editor.apply();

        editor.putInt(res.getString(R.string.pref_font_color), fontColor).apply();
        editor.putInt(res.getString(R.string.pref_background_color), backgroundColor).apply();
    }
}

package com.usayplz.englishbookreader.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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
        settings.setMarginTop(Integer.valueOf(pref.getString(res.getString(R.string.pref_margin_top), res.getString(R.string.pref_margin_default))));
        settings.setMarginBottom(Integer.valueOf(pref.getString(res.getString(R.string.pref_margin_bottom), res.getString(R.string.pref_margin_default))));
        settings.setMarginLeft(Integer.valueOf(pref.getString(res.getString(R.string.pref_margin_left), res.getString(R.string.pref_margin_default))));
        settings.setMarginRight(Integer.valueOf(pref.getString(res.getString(R.string.pref_margin_right), res.getString(R.string.pref_margin_default))));
        settings.setFontFamily(pref.getString(res.getString(R.string.pref_font_family), res.getString(R.string.pref_font_family_default)));
        settings.setFontSize(Integer.valueOf(pref.getString(res.getString(R.string.pref_font_size), res.getString(R.string.pref_font_size_default))));
        settings.setFontColor(pref.getInt(res.getString(R.string.pref_font_color), res.getColor(R.color.font_day)));
        settings.setBackgroundColor(pref.getInt(res.getString(R.string.pref_background_color), Color.WHITE));
        settings.setNightMode(pref.getBoolean(res.getString(R.string.pref_nightmode), false));

        return settings;
    }
}

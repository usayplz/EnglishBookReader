package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public enum ReadingMenuItem {
    CHAPTER(0, R.drawable.ic_chapter, R.string.change_chapter),
    SETTINGS(10, R.drawable.ic_settings, R.string.settings),
    LIBRARY(20, R.drawable.ic_library, R.string.library),
     NIGHTMODE(30, R.drawable.ic_nightmode, R.string.nightmode),
    // BOOKMARKS(40, R.drawable.ic_more_horiz, R.string.bookmarks),
    EXIT(50, R.drawable.ic_exit, R.string.exit);

    public int id;
    public int icon;
    public int name;

    ReadingMenuItem(int id, int icon, int name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public static ReadingMenuItem byId(int id) {
        for (ReadingMenuItem item : ReadingMenuItem.values()) {
            if (item.id == id) return item;
        }
        return null;
    }
}

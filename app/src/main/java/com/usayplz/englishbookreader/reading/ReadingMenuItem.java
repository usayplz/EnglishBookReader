package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.R;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public enum ReadingMenuItem {
    SETTINGS(0, 0, 0, R.string.settings),
    LIBRARY(0, 10, 10, R.string.library),
    NIGHTMODE(0, 20, 20, R.string.nightmode),
    // ADDBOOKMARK(0, 30, 30, R.string.add_bookmark),
    // BOOKMARKS(0, 40, 40, R.string.bookmarks),
    EXIT(0, 50, 50, R.string.exit);

    public int group;
    public int id;
    public int name;
    public int order;

    ReadingMenuItem(int group, int id, int order, int name) {
        this.group = group;
        this.id = id;
        this.order = order;
        this.name = name;
    }

    public static ReadingMenuItem byId(int id) {
        for (ReadingMenuItem item : ReadingMenuItem.values()) {
            if (item.id == id) return item;
        }
        return null;
    }
}

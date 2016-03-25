package com.usayplz.englishbookreader.language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergei Kurikalov on 25/03/16.
 * u.sayplz@gmail.com
 */

public enum Lang {
    RU("Russian", "http://95.85.31.41/static/enru.zip", "enru.zip", "enru.db"),
    ZH("Chinese", "http://95.85.31.41/static/enzh.zip", "enzh.zip", "enzh.db"),;

    public String title;
    public String url;
    public String zipFile;
    public String dbFile;

    Lang(String title, String url, String zipFile, String dbFile) {
        this.title = title;
        this.url = url;
        this.zipFile = zipFile;
        this.dbFile = dbFile;
    }

    public static List<String> getTitles() {
        List<String> result = new ArrayList<>();
        for (Lang lang : Lang.values()) {
            result.add(lang.title);
        }

        return result;
    }

    public static Lang getLangByTitle(String title) {
        for (Lang lang : Lang.values()) {
            if (lang.title.equals(title)) {
                return lang;
            }
        }
        return null;
    }
}

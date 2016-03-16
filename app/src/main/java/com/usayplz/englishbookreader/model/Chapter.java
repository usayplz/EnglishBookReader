package com.usayplz.englishbookreader.model;

/**
 * Created by Sergei Kurikalov on 16/03/16.
 * u.sayplz@gmail.com
 */
public class Chapter {
    private int id;
    private String name;

    public Chapter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

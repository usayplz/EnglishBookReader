package com.usayplz.englishbookreader.model;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public enum BookType {
    EPUB(0), FB2(1);

    private int code;

    private BookType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static BookType byCode(int code) {
        for (BookType bookType : BookType.values()) {
            if (bookType.code == code) return bookType;
        }
        return null;
    }
}
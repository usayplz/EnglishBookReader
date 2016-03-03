package com.usayplz.englishbookreader.model;

/**
 * Created by Sergei Kurikalov on 02/03/16.
 * u.sayplz@gmail.com
 */
public enum BookType {
    EPUB(0, ".epub"), FB2(1, ".fb2");

    private int code;
    private String extension;

    private BookType(int code, String extension) {
        this.code = code;
        this.extension = extension;
    }

    public int getCode() {
        return code;
    }

    public String getExtension() {
        return extension;
    }

    public static BookType byCode(int code) {
        for (BookType bookType : BookType.values()) {
            if (bookType.code == code) {
                return bookType;
            }
        }
        return null;
    }

    public static BookType byExtension(String filePath) {
        filePath = filePath.toLowerCase();

        for (BookType bookType : BookType.values()) {
            if (filePath.endsWith(bookType.getExtension())) {
                return bookType;
            }
        }
        return null;
    }
}
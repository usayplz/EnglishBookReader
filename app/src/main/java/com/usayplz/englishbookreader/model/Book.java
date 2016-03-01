package com.usayplz.englishbookreader.model;

import android.content.Context;

import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */

public class Book implements Serializable {
    private long id;
    private BookType type;
    private String file;
    private String dir;
    private String title;
    private String author;
    private String coverImage;
    private Integer page;
    private Boolean active;
    private Integer chapter;
    private Integer maxChapter;

    public Book() {
    }

    // TODO remove example
    public Book(Context context) {
//        String filePath = "/mnt/sdcard/Download/johnny.epub";
//        String filePath = "/storage/sdcard/Download/Adamov_G_Izgnanie_VladiykiI.epub";
        String filePath = "/mnt/sdcard/Download/Adamov_G_Izgnanie_VladiykiI.epub";
        File fileBook = new File(filePath);
        File dirBook = FileUtils.concatToFile(context.getFilesDir().getPath(), fileBook.getName() + fileBook.length());

        this.type = Book.BookType.EPUB;
        this.file = fileBook.getPath();
        // TODO Define OPS dir
        this.dir = dirBook.getPath();// + File.separator + "OPS";
        this.title = "Johnny Mnemonic";
        this.author = "Unknown";
        this.chapter = 1;
        this.maxChapter = 58;
        this.page = 0;
        this.active = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Integer getMaxChapter() {
        return maxChapter;
    }

    public void setMaxChapter(Integer maxChapter) {
        this.maxChapter = maxChapter;
    }

    public enum BookType {
        EPUB, FB2;
    }
}

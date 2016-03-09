package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;

import java.io.File;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class FB2Manager extends AbstractBookManager {
    @Override
    public Book getBookInfo(String filePath, String filesPath) {
        return null;
    }

    @Override
    public Observable<File> getContent(Book book, String template) {
        return null;
    }
}

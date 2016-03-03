package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class FB2Manager extends AbstractBookManager {

    @Override
    public Book getBookInfo(String filePath, String filesPath, String default_authors, String default_title) {
        return new Book();
    }

    @Override
    public Observable<String> getContent(Book book) {
        return Observable.just("");
    }
}

package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class FB2Manager extends AbstractBookManager {
    @Override
    public Observable<Book> getBookInfo(String filePath) {
        return Observable.just(new Book());
    }

    @Override
    public Observable<String> getContent(Book book) {
        return Observable.just("");
    }
}

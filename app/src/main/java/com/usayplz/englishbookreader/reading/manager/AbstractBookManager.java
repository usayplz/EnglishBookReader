package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public abstract class AbstractBookManager {
    public abstract Observable<Book> getBookInfo(String filePath);
    public abstract Observable<String> getContent(Book book);
}

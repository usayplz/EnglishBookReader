package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.Chapter;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class FB2Manager extends AbstractBookManager {

    @Override
    public Book getBookInfo(String filePath, String dirPath) {
        return null;
    }

    @Override
    public Observable<File> getContent(Book book, String template) {
        return null;
    }

    @Override
    public List<Chapter> getChapters(String filePath) {
        return null;
    }
}

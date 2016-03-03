package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public abstract class AbstractBookManager {
    public abstract Book getBookInfo(String filePath, String filesPath, String default_authors, String default_title);
    public abstract Observable<String> getContent(Book book);

    public static AbstractBookManager getBookManager(BookType type) {
        switch (type) {
            case EPUB:
                return new EpubManager();
            case FB2:
                return new FB2Manager();
        }
        return null;
    }

    public static BookType getBookType(String filePath) {
        filePath = filePath.toUpperCase();

        if (filePath.endsWith("EPUB")) {
            return BookType.EPUB;
        }

        if (filePath.endsWith("FB2")) {
            return BookType.FB2;
        }

        return null;
    }
}

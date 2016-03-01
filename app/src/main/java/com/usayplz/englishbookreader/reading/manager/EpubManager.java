package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import nl.siegmann.epublib.epub.EpubReader;
import rx.Observable;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class EpubManager extends AbstractBookManager {
    private static final String BOOK_TEMPLATE = "html/template.html";

    @Override
    public Observable<Book> getBookInfo(String filePath) {
        return Observable.defer(() -> {
            Book book = newBook();
            book.setFile(filePath);

            try {
                nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(filePath));
                book.setAuthor(epubBook.getMetadata().getAuthors().toString());
                book.setTitle(epubBook.getTitle());
                book.setMaxChapter(epubBook.getContents().size()-1);
            } catch (IOException e) {
                return Observable.error(e);
            }

            return Observable.just(book);
        });
    }

    @Override
    public Observable<String> getContent(Book book) {
        return Observable.defer(() -> {
            try {
                File file = new File(book.getFile());
                File dir = new File(book.getDir());
                if (!dir.exists()) {
                    FileUtils.unzip(file, dir);
                }

                nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(book.getFile()));
                String content = new String(epubBook.getContents().get(book.getChapter()).getData());
                return Observable.just(content);
            } catch (IOException e) {
                return Observable.error(e);
            }
        });
    }

    private int getContentSize(nl.siegmann.epublib.domain.Book epubBook) throws IOException {
        int length = 0;
        for (int i=1; i<=epubBook.getContents().size()-1; i++) {
            length += new String(epubBook.getContents().get(i).getData()).length();
        }
        return length;
    }

    private Book newBook() {
        Book book = new Book();
        book.setType(Book.BookType.EPUB);
        book.setChapter(1);
        book.setPage(0);
        return book;
    }
}

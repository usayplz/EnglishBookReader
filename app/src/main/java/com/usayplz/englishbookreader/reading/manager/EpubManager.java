package com.usayplz.englishbookreader.reading.manager;

import android.text.TextUtils;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
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
    @Override
    public Book getBookInfo(String filePath, String filesPath) {
        File file = new File(filePath);
        File dir = FileUtils.concatToFile(filesPath, file.getName() + file.length());
        Book book = new Book();
        book.setType(BookType.EPUB);
        book.setPage(0);
        book.setFile(filePath);
        book.setDir(dir.getPath());

        try {
            nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(filePath));
            String authors = TextUtils.join(", ", epubBook.getMetadata().getAuthors());
            book.setAuthor(authors);
            book.setTitle(epubBook.getTitle());
        } catch (Exception e) {
            return null;
        }

        return book;
    }

    @Override
    public Observable<File> getContent(Book book, String templateHeader, String templateFooter) {
        return Observable.defer(() -> {
            try {
                File file = new File(book.getFile());
                File dir = new File(book.getDir());
                if (!dir.exists()) {
                    FileUtils.unzip(file, dir);
                }

                File bookFile = getBookFile(dir);
                if (bookFile.exists()) {
                    return Observable.just(bookFile);
                }

                nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(book.getFile()));
                FileUtils.appendFile(bookFile, templateHeader);
                for (int chapter = 0; chapter < epubBook.getContents().size(); chapter++) {
                    String content = getBody(new String(epubBook.getContents().get(chapter).getData()));
                    FileUtils.appendFile(bookFile, content);
                }
                FileUtils.appendFile(bookFile, templateFooter);

                if (bookFile.exists()) {
                    return Observable.just(bookFile);
                } else {
                    return error();
                }
            } catch (IOException e) {
                return error();
            }
        });
    }

    private Observable<File> error() {
        Observable.error(new Exception("Cannot create file"));
        return null;
    }
}

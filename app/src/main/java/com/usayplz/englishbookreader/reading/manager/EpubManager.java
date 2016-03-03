package com.usayplz.englishbookreader.reading.manager;

import android.text.TextUtils;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.utils.Strings;

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
    public Book getBookInfo(String filePath, String filesPath, String default_authors, String default_title) {
        File file = new File(filePath);
        File dir = FileUtils.concatToFile(filesPath, file.getName() + file.length());
        Book book = new Book();
        book.setType(BookType.EPUB);
        book.setChapter(1);
        book.setPage(0);
        book.setFile(filePath);
        book.setDir(dir.getPath());

        try {
            nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(filePath));
            book.setMaxChapter(epubBook.getContents().size() - 1);

            String authors = TextUtils.join(", ", epubBook.getMetadata().getAuthors());
            if (Strings.isEmpty(authors)) {
                authors = default_authors;
            }
            book.setAuthor(authors);

            String title = epubBook.getTitle();
            if (Strings.isEmpty(title)) {
                title = default_title;
            }
            book.setTitle(title);

        } catch (IOException ignored) {
            return null;
        }

        return book;
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
}

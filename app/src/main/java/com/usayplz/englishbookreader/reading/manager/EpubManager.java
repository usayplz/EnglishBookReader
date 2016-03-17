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
    public Book getBookInfo(String filePath, String dirPath) {
        File file = new File(filePath);
        File dir = FileUtils.concatToFile(dirPath, file.getName() + file.length());
        Book book = new Book();
        book.setType(BookType.EPUB);
        book.setPage(Book.FIRST_PAGE);
        book.setChapter(0);
        book.setLastPage(0);
        book.setFile(filePath);
        book.setDir(dir.getPath());
        book.setChaptersCount("");

        try {
            nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(filePath));
            String authors = TextUtils.join(", ", epubBook.getMetadata().getAuthors());
            book.setAuthor(authors);
            book.setTitle(epubBook.getTitle());
            book.setLastChapter(epubBook.getContents().size() - 1);
        } catch (Exception e) {
            return null;
        }

        return book;
    }

    @Override
    public Observable<File> getContent(Book book, String template) {
        return Observable.defer(() -> {
            try {
                File file = new File(book.getFile());
                File dir = new File(book.getDir());

                File bookFile = getBookFile(dir, book.getChapter());
                // TODO UNCOMMENT
//                if (bookFile.exists()) {
//                    return Observable.just(bookFile);
//                }

                if (!isReady(book)) {
                    FileUtils.unzip(file, dir);
                }

                nl.siegmann.epublib.domain.Book epubBook = (new EpubReader()).readEpub(new FileInputStream(book.getFile()));
                for (int chapter = 0; chapter < epubBook.getContents().size(); chapter++) {
                    String content = new String(epubBook.getContents().get(chapter).getData());
                    createContent(getBookFile(dir, chapter), content, template);
                }

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

    @Override
    public boolean isReady(Book book) {
        return new File(book.getDir()).exists();
    }

    private Observable<File> error() {
        Observable.error(new Exception("Cannot create file"));
        return null;
    }
}

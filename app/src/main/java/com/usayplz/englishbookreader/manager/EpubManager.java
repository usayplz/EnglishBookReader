package com.usayplz.englishbookreader.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.siegmann.epublib.epub.EpubReader;
import rx.Observable;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class EpubManager extends AbstractBookManager {
    private static final String DIR_COVER = "covers";

    @Override
    public Book getBookInfo(String filePath, String dirPath) {
        File file = new File(filePath);
        File dir = FileUtils.concatToFile(dirPath, file.getName() + file.length());

        Book book = new Book();
        book.setType(BookType.EPUB);
        book.setPage(Book.PAGE_FIRST);
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

            // save cover image
            if (epubBook.getCoverImage() != null) {
                File coverImage = saveCoverImage(dir.getPath(), epubBook.getCoverImage().getInputStream());
                if (coverImage != null && coverImage.exists()) {
                    book.setCoverImage(coverImage.getPath());
                }
            }
        } catch (Exception e) {
            return null;
        }

        return book;
    }

    private File saveCoverImage(String bookPath, InputStream coverImageStream) {
        try {
            int separator = bookPath.lastIndexOf("/");
            File coverImage = FileUtils.concatToFile(bookPath.substring(1, separator), DIR_COVER, bookPath.substring(separator + 1) + ".png");


            File coverDir = FileUtils.concatToFile(bookPath.substring(1, separator), DIR_COVER);
            if (!coverDir.exists()) coverDir.mkdir();

            OutputStream fos = new FileOutputStream(coverImage); // context.openFileOutput(picName, Context.MODE_PRIVATE);
            Bitmap b = BitmapFactory.decodeStream(coverImageStream);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return coverImage;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Observable<File> getContent(Book book, String template) {
        return Observable.defer(() -> {
            try {
                File file = new File(book.getFile());
                File dir = new File(book.getDir());

                File bookFile = getBookFile(dir, book.getChapter());
                if (bookFile.exists()) {
                    return Observable.just(bookFile);
                }

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

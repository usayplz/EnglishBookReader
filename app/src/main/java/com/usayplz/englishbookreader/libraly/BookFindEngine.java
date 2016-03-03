package com.usayplz.englishbookreader.libraly;

import android.os.Environment;

import com.usayplz.englishbookreader.model.Book;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 03/03/16.
 * u.sayplz@gmail.com
 */
public class BookFindEngine {
    public BookFindEngine() {
    }

    public Observable<Book> find() {
        return Observable.defer(() -> {
            List<File> files = new ArrayList<>();
            searchFiles(Environment.getExternalStorageDirectory(), files, new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".epub"); //TODO: add other formats
                }
            });

            List<Book> books = new ArrayList<>();
            for (File f : files) {
                Book book = new Book();
                book.setFile(f.getPath());
                books.add(book);
            }

            return Observable.from(books);
        });
    }

    private void searchFiles(File f, List<File> list, FileFilter filter) {
        if (f == null) {
            return;
        }

        File[] files = f.listFiles(filter);

        for (File file : files) {
            if (file.isDirectory())
                searchFiles(file, list, filter);
            else
                list.add(file);
        }
    }
}

package com.usayplz.englishbookreader.libraly;

import android.os.Environment;

import com.usayplz.englishbookreader.model.BookType;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 03/03/16.
 * u.sayplz@gmail.com
 */
public class ScanDriveEngine {
    public ScanDriveEngine() {
    }

    public Observable<File> find() {
        return Observable.defer(() -> {
            List<File> files = new ArrayList<>();
            searchFiles(Environment.getExternalStorageDirectory(), files, f -> {
                return f.isDirectory() || BookType.byExtension(f.getName()) != null; //TODO: add other formats
            });

            return Observable.from(files);
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

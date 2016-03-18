package com.usayplz.englishbookreader.manager;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.usayplz.englishbookreader.model.BookType;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 03/03/16.
 * u.sayplz@gmail.com
 */
public class ScanDriveEngine {
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    public ScanDriveEngine() {
    }

    public Observable<File> find() {
        return Observable.defer(() -> {
            List<File> files = new ArrayList<>();
            for (String dirPath : getStorageDirectories()) {
                File dir = new File(dirPath);
                if (!dir.exists()) continue;
                searchFiles(dir, files, f -> {
                    return f.isDirectory() || BookType.byExtension(f.getName()) != null;
                });
            }

            return Observable.from(files);
        });
    }

    private void searchFiles(File f, List<File> list, FileFilter filter) {
        if (f == null) {
            return;
        }

        File[] files = f.listFiles(filter);

        for (File file : files) {
            if (file.isDirectory()) {
                searchFiles(file, list, filter);
            } else {
                list.add(file);
            }
        }
    }

    public static String[] getStorageDirectories() {
        final Set<String> result = new HashSet<>();
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");

        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            if (TextUtils.isEmpty(rawExternalStorage)) {
                result.add("/storage/sdcard0");
            } else {
                result.add(rawExternalStorage);
            }
        } else {
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                result.add(rawEmulatedStorageTarget);
            } else {
                result.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }

        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(result, rawSecondaryStorages);
        }
        return result.toArray(new String[result.size()]);
    }
}

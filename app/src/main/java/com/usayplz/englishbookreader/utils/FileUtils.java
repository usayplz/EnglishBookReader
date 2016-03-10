package com.usayplz.englishbookreader.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public class FileUtils {

    public static File concatToFile(String... args) {
        return new File(TextUtils.join(File.separator, args));
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (ze.isDirectory()) {
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

    public static String loadAsset(Context context, String assetFile) {
        try {
            InputStream is = context.getAssets().open(assetFile);
            final int size = is.available();
            final byte[] buffer = new byte[size];
            StringBuilder sb = new StringBuilder();

            while (is.read(buffer) != -1) {
                sb.append(new String(buffer));
            }
            is.close();

            return sb.toString();
        } catch (IOException e) {
            Log.d(e.getMessage());
            return null;
        }
    }

    public static void appendFile(File file, String content) throws IOException {
        boolean append = file.exists();

        FileWriter writer = new FileWriter(file, append);
        writer.write(content);
        writer.flush();
        writer.close();
    }
}

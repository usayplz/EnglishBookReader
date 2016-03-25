package com.usayplz.englishbookreader.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by Sergei Kurikalov on 24/03/16.
 * u.sayplz@gmail.com
 */
public class DownloadManager {

    public Observable<File> download(File target, String url) {
        return Observable.defer(() -> {
            if (target.exists()) {
                return Observable.just(target);
            }

            InputStream input = null;
            OutputStream output = null;
            try {
                Request request = new Request.Builder().url(url).build();
                Response response = new OkHttpClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    input = response.body().byteStream();
                    output = new FileOutputStream(target.getPath());
                    byte data[] = new byte[1024];

                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    return Observable.just(target);
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                Observable.error(e);
                return Observable.just(target);
            }
        });
    }
}

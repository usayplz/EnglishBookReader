package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public abstract class AbstractBookManager {
    private static final String BOOK_FILE_NAME = "book_%s.html";

    public abstract Book getBookInfo(String filePath, String dirPath);
    public abstract Observable<File> getContent(Book book, String template);
    public abstract boolean isReady(Book book);

    public static AbstractBookManager getBookManager(BookType type) {
        switch (type) {
            case EPUB:
                return new EpubManager();
            case FB2:
                return new FB2Manager();
        }
        return null;
    }

    public File getBookFile(File dir, int chapter) {
        return FileUtils.concatToFile(dir.getPath(), String.format(BOOK_FILE_NAME, chapter));
    }

    public String getBody(String content) {
        // body
        int start = content.indexOf("<body") + 6;
        for (int i = start; i <= content.length(); i++) {
            if (">".equals(content.substring(i - 1, i))) {
                start = i;
                break;
            }
        }
        return content.substring(start, content.indexOf("</body>"));
    }

    public void createContent(File bookFile, String content, String template) throws IOException {
        content = getBody(content);
        content = template.replace("${content}", content);
        FileUtils.write(bookFile, content);
    }
}

package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import rx.Observable;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public abstract class AbstractBookManager {
    private static final String BOOK_FILE_NAME = "book.html";

    public abstract Book getBookInfo(String filePath, String filesPath);
    public abstract Observable<File> getContent(Book book, String template);

    public static AbstractBookManager getBookManager(BookType type) {
        switch (type) {
            case EPUB:
                return new EpubManager();
            case FB2:
                return new FB2Manager();
        }
        return null;
    }

    public File getBookFile(File dir) {
        return FileUtils.concatToFile(dir.getPath(), BOOK_FILE_NAME);
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

    public File createContent(String content, String template, File bookFile) {
        template = template.replace("${content}", content);

        try {
            FileWriter writer = new FileWriter(bookFile);
            writer.write(template);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookFile;
    }
}

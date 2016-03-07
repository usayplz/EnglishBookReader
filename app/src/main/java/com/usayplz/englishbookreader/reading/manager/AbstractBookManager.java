package com.usayplz.englishbookreader.reading.manager;

import com.usayplz.englishbookreader.model.Book;
import com.usayplz.englishbookreader.model.BookType;
import com.usayplz.englishbookreader.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public abstract class AbstractBookManager {
    private static final String CHAPTER_FILE_NAME = "chapter_%s.html";

    public abstract Book getBookInfo(String filePath, String filesPath, String default_authors, String default_title);
    public abstract List<File> process(Book book, String template);

    public File getChapterFile(String dir, int chapter) {
        return FileUtils.concatToFile(dir, String.format(CHAPTER_FILE_NAME, chapter));
    }

    public static AbstractBookManager getBookManager(BookType type) {
        switch (type) {
            case EPUB:
                return new EpubManager();
            case FB2:
                return new FB2Manager();
        }
        return null;
    }

    public File createContent(String content, String template, String dir, int chapter) {
        File file = getChapterFile(dir, chapter);
        if (file.exists()) return file;

        // body
        int start = content.indexOf("<body") + 6;
        for (int i = start; i <= content.length(); i++) {
            if (">".equals(content.substring(i - 1, i))) {
                start = i;
                break;
            }
        }

        // replace
        content = content.substring(start, content.indexOf("</body>"));
        template = template.replace("${content}", content);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(template);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}

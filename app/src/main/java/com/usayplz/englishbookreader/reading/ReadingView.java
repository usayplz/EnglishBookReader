package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.base.BaseView;
import com.usayplz.englishbookreader.model.Settings;

import java.io.File;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public interface ReadingView extends BaseView {
    void showContent(File content, Settings settings, int page, int relativePage, int lastPage);

    void setPage(int page);

    void showMenu(int page, int maxPageCount);
}

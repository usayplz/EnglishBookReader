package com.usayplz.englishbookreader.reading;

import com.usayplz.englishbookreader.base.BaseView;
import com.usayplz.englishbookreader.model.BookSettings;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public interface ReadingView extends BaseView {
    void showContent(BookSettings bookSettings);

    void showError(int error);

    void showLoading(int message);
    void hideLoading();

    void setPage(int page);
}

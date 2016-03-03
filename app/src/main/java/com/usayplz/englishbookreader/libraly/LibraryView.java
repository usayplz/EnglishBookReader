package com.usayplz.englishbookreader.libraly;

import com.usayplz.englishbookreader.base.BaseView;
import com.usayplz.englishbookreader.model.Book;

import java.util.List;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public interface LibraryView extends BaseView {
    void showContent(List<Book> books);

    void openBook(long id);

    void showEmpty();
}

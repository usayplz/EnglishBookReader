package com.usayplz.englishbookreader.libraly;

import com.usayplz.englishbookreader.base.BasePresenter;

/**
 * Created by Sergei Kurikalov on 01/03/16.
 * u.sayplz@gmail.com
 */
public class LibralyPresenter extends BasePresenter<LibralyView> {

    public LibralyPresenter() {
    }

    public void getContent() {
        if (isViewAttached()) {
            getView().showContent();
        }
    }
}

package com.usayplz.englishbookreader.base;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public interface BaseInterfacePresenter<V extends BaseView> {
    public void attachView(V view);
    public void detachView();
}

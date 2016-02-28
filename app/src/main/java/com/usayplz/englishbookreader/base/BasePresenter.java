package com.usayplz.englishbookreader.base;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class BasePresenter<V extends BaseView> implements BaseInterfacePresenter<V> {

    private WeakReference<V> viewRef;

    @Override public void attachView(V view) {
        viewRef = new WeakReference<V>(view);
    }

    @Nullable
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}

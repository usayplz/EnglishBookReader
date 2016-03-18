package com.usayplz.englishbookreader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.usayplz.englishbookreader.model.Settings;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.utils.Strings;

import java.io.File;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class BookView extends WebView {
    private static final float SCROLL_THRESHOLD = 0.1f;

    private float posX;
    private float posY;
    private boolean isSwiping;
    private IBookListener listener;
    private Settings settings;
    private int page;
    private int relativePage;
    private int lastPage;

    public BookView(Context context) {
        super(context);
        init();
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // settings
        WebSettings webSettings = this.getSettings();

        this.addJavascriptInterface(this, "android");
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);

        // speed?
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.requestFocus(View.FOCUS_DOWN);

        // disable long tap
        this.setOnLongClickListener(v -> true);
        this.setLongClickable(false);

        // loading content
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (settings != null) {
                    loadUrl(String.format("javascript: init('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                            settings.getFontFamily(),
                            settings.getFontSize(),
                            Strings.colorToRGB(settings.getFontColor()),
                            Strings.colorToRGB(settings.getBackgroundColor()),
                            settings.getMargin(),
                            page,
                            relativePage,
                            lastPage));
                }


                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // logs
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                Log.d("console.log: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    public void loadContent(File content, Settings settings, int page, int relativePage, int lastPage) {
        this.page = page;
        this.relativePage = relativePage;
        this.lastPage = lastPage;
        this.settings = settings;
        this.loadUrl(Uri.fromFile(content).toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX() / getWidth();
                posY = event.getY() / getHeight();
                isSwiping = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!isSwiping) {
                    float upX = event.getX() / getWidth();
                    if (upX - posX > SCROLL_THRESHOLD) {
                        if (listener != null) listener.onPrevious();
                    } else if (upX - posX < -SCROLL_THRESHOLD) {
                        if (listener != null) listener.onNext();
                    }
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSwiping && (Math.abs(posX - event.getX() / getWidth()) > SCROLL_THRESHOLD ||
                        Math.abs(posY - event.getY() / getHeight()) > SCROLL_THRESHOLD)) {
                    isSwiping = false;
                }
                return false;
        }

        return super.onTouchEvent(event);
    }

    public void setPage(int page) {
        loadUrl("javascript:setPage(" + page + ")");
    }

    @JavascriptInterface
    public void onTextSelected(String word) {
        if (listener != null) listener.onTextSelected(word);
    }

    @JavascriptInterface
    public void onSetPageCount(String pageCount) {
        if (listener != null) listener.onSetPageCount(Integer.parseInt(pageCount));
    }

    @JavascriptInterface
    public void onMenuClicked() {
        if (listener != null) listener.onMenuClicked();
    }

    @JavascriptInterface
    public void onNext() {
        if (listener != null) listener.onNext();
    }

    @JavascriptInterface
    public void onPrevious() {
        if (listener != null) listener.onPrevious();
    }

    public void setListener(IBookListener listener) {
        this.listener = listener;
    }

    public interface IBookListener {
        void onTextSelected(String word);

        void onNext();

        void onPrevious();

        void onSetPageCount(int pagecount);

        void onMenuClicked();
    }
}

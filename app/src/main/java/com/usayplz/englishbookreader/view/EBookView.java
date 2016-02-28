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

import com.usayplz.englishbookreader.model.BookSettings;
import com.usayplz.englishbookreader.utils.FileUtils;
import com.usayplz.englishbookreader.utils.Log;
import com.usayplz.englishbookreader.utils.Strings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 */
public class EBookView extends WebView {
    private static final float SCROLL_THRESHOLD = 0.2f;
    private static final String BOOK_TEMPLATE = "html/template.html";

    private float posX;
    private float posY;
    private boolean isSwiping;
    private EBookListener listener;

    public EBookView(Context context) {
        super(context);
        init();
    }

    public EBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // settings
        WebSettings settings = this.getSettings();

        this.addJavascriptInterface(this, "android");
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);

        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        // speed?
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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
                super.onPageFinished(view, url);

                loadUrl("javascript:" + FileUtils.loadAsset(getContext(), "js/jquery.min.js"));
                loadUrl("javascript: init();");
            }
        });

        // logs
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                Log.d(consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    public void loadContent(BookSettings bookSettings) {
        this.loadUrl(Uri.fromFile(modifyContent(bookSettings)).toString());
    }

    private File modifyContent(BookSettings bookSettings) {
        Log.d(new Exception("fucking exception"));
        String template = FileUtils.loadAsset(getContext(), BOOK_TEMPLATE);
        if (Strings.isEmpty(template)) {
            Log.d("Cannot load template");
            return null;
        }

        // main
        String content = bookSettings.getContent();

        // body
        int start = content.indexOf("<body") + 6;
        for (int i = start; i <= content.length(); i++) {
            if (">".equals(content.substring(i - 1, i))) {
                start = i;
                break;
            }
        }
        content = content.substring(start, content.indexOf("</body>"));

        // replace
        template = template.replace("${content}", content);
        template = template.replace("${page}", bookSettings.getBook().getPage().toString());
        template = template.replace("${font-family}", bookSettings.getSettings().getFontFamily());
        template = template.replace("${font-size}", bookSettings.getSettings().getFontSize().toString());
        template = template.replace("${font-color}", Strings.colorToRGB(bookSettings.getSettings().getFontColor()));
        template = template.replace("${background-color}", Strings.colorToRGB(bookSettings.getSettings().getBackgroundColor()));
        template = template.replace("${content-size}", bookSettings.getBook().getContentSize().toString());

        template = template.replace("${padding-top}", bookSettings.getSettings().getMarginTop().toString());
        template = template.replace("${padding-bottom}", bookSettings.getSettings().getMarginBottom().toString());
        template = template.replace("${padding-left}", bookSettings.getSettings().getMarginLeft().toString());
        template = template.replace("${padding-right}", bookSettings.getSettings().getMarginRight().toString());

        File file = FileUtils.concatToFile(bookSettings.getBook().getDir(), "template.html");
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
    public void getPageCounts(String pageCount, String maxPageCount) {
        if (listener != null) listener.onGetPageCounts(Integer.parseInt(pageCount), Integer.parseInt(maxPageCount));
    }

    public void setListener(EBookListener listener) {
        this.listener = listener;
    }

    public interface EBookListener {
        void onTextSelected(String word);
        void onNext();
        void onPrevious();
        void onGetPageCounts(int pagecount, int maxPageCount);
    }
}

package com.usayplz.englishbookreader.utils;

import com.usayplz.englishbookreader.BuildConfig;

/**
 * Created by Sergei Kurikalov on 03/02/16.
 * u.sayplz@gmail.com
 *
 * Futures:
 *  - the usual syntax
 *  - auto defining a calling class
 *  - show when debug version only
 *  - easy to filter logs by "LOG_PREFIX" or the calling class
 *
 */

public class Log {
    private static final String LOG_PREFIX = "LOG/";
    private static final boolean LOGGING_ENABLED = BuildConfig.DEBUG;
    private static final int TRACE_DEPTH = 2;


    private static String getTag() {
        StackTraceElement[] stackTraceElement = new Throwable().getStackTrace();
        String name = stackTraceElement[TRACE_DEPTH].getClassName();

        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            name = name.substring(dot + 1);
        }

        return LOG_PREFIX + name;
    }

    public static void d(String message) {
        if (LOGGING_ENABLED) {
            android.util.Log.d(getTag(), message);
        }
    }

    public static void v(String message) {
        if (LOGGING_ENABLED) {
            android.util.Log.v(getTag(), message);
        }
    }

    public static void i(String message) {
        if (LOGGING_ENABLED) {
            android.util.Log.i(getTag(), message);
        }
    }

    public static void w(String message) {
        if (LOGGING_ENABLED) {
            android.util.Log.w(getTag(), message);
        }
    }

    public static void e(String message) {
        if (LOGGING_ENABLED) {
            android.util.Log.e(getTag(), message);
        }
    }

    private Log() {
    }
}

package com.usayplz.englishbookreader.utils;

/**
 * Created by Sergei Kurikalov on 06/02/16.
 * u.sayplz@gmail.com
 */
public class Strings {

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String colorToRGB(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}

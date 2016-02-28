package com.usayplz.englishbookreader.model;

import java.io.Serializable;

/**
 * Created by Sergei Kurikalov on 08/02/16.
 * u.sayplz@gmail.com
 */
public class Settings implements Serializable {
    private String fontFamily;
    private Integer fontSize;
    private String language;
    private Integer MarginTop;
    private Integer MarginBottom;
    private Integer MarginLeft;
    private Integer MarginRight;
    private Integer backgroundColor;
    private Integer fontColor;
    private Boolean nightMode;

//    private Boolean preventSleeping;
//    private Integer textLineSpacing;
//    private Integer textAlignment;


    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getMarginTop() {
        return MarginTop;
    }

    public void setMarginTop(Integer marginTop) {
        MarginTop = marginTop;
    }

    public Integer getMarginBottom() {
        return MarginBottom;
    }

    public void setMarginBottom(Integer marginBottom) {
        MarginBottom = marginBottom;
    }

    public Integer getMarginLeft() {
        return MarginLeft;
    }

    public void setMarginLeft(Integer marginLeft) {
        MarginLeft = marginLeft;
    }

    public Integer getMarginRight() {
        return MarginRight;
    }

    public void setMarginRight(Integer marginRight) {
        MarginRight = marginRight;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getFontColor() {
        return fontColor;
    }

    public void setFontColor(Integer fontColor) {
        this.fontColor = fontColor;
    }

    public Boolean getNightMode() {
        return nightMode;
    }

    public void setNightMode(Boolean nightMode) {
        this.nightMode = nightMode;
    }
}

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
    private Integer margin;
    private Integer backgroundColor;
    private Integer fontColor;
    private Boolean nightmode;

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

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
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

    public Boolean getNightmode() {
        return nightmode;
    }

    public void setNightmode(Boolean nightmode) {
        this.nightmode = nightmode;
    }
}

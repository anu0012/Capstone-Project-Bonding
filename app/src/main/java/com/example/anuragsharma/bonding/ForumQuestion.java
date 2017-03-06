package com.example.anuragsharma.bonding;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 * Created by anuragsharma on 13/2/17.
 */

public class ForumQuestion {
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mKey;
    private TextDrawable mTextDrawable;

    public ForumQuestion(String Title, String Description, String url, String key) {
        mTitle = Title;
        mDescription = Description;
        mUrl = url;
        mKey = key;
    }

    public ForumQuestion(String title, String description, String key) {
        mTitle = title;
        mDescription = description;
        mKey = key;
    }

    public ForumQuestion(String title, String description, String key, TextDrawable textDrawable) {
        mTitle = title;
        mDescription = description;
        mKey = key;
        mTextDrawable = textDrawable;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getKey() {
        return mKey;
    }

    public TextDrawable getTextDrawable() {
        return mTextDrawable;
    }

}

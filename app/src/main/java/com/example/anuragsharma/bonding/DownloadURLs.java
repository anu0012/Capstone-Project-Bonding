package com.example.anuragsharma.bonding;

/**
 * Created by anuragsharma on 11/2/17.
 */

public class DownloadURLs {
    private String mTitle;
    private String mSection;
    private String mUrl;
    private String mAuthor;

    public DownloadURLs(String Title, String Section, String url, String Author) {
        mTitle = Title;
        mSection = Section;
        mUrl = url;
        mAuthor = Author;
    }

    public DownloadURLs(String url, String section) {
        mUrl = url;
        mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }
}

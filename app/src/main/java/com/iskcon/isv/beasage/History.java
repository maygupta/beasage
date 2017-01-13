package com.iskcon.isv.beasage;

/**
 * Created by mayank on 1/13/17.
 */
public class History {
    public String mDate;
    public String mBook;
    public int mCount;
    public boolean mIsPage;
    public String mUrl;

    History(String date ,String book, int count, boolean isPage, String url) {
        mDate = date;
        mCount = count;
        mBook = book;
        mIsPage = isPage;
        mUrl = url;
    }
}

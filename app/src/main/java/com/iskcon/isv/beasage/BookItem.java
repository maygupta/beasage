package com.iskcon.isv.beasage;

/**
 * Created by mayank on 3/19/17.
 */
public class BookItem {
    String name;
    int pages;
    int slokas;
    String url;

    public BookItem(String n, int p, int s, String u) {
        name = n;
        pages = p;
        slokas = s;
        url = u;
    }
}

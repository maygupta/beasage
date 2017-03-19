package com.iskcon.isv.beasage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 3/19/17.
 */
public class Books {

    HashMap<Integer, BookItem> booksMap;

    public Books() {
        booksMap = new HashMap<>();
    }

    public void setBooks(List<BookItem> bookitems) {
        for(BookItem item: bookitems) {
            booksMap.put(bookitems.indexOf(item), item);
        }
    }

    public BookItem getBookById(int id) {
        return booksMap.get(id);
    }

    public int getBookByName(String s) {
        for(int i = 0; i < booksMap.size(); i++) {
            if(s.equals(booksMap.get(i).name)) {
                return i;
            }
        }
        return 0;
    }

}



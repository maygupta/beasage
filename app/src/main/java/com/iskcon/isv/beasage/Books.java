package com.iskcon.isv.beasage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 3/19/17.
 */
public class Books {

    static HashMap<Integer, BookItem> booksMap;
    static Books instance = null;

    public void setBooks(List<BookItem> bookitems) {
        for(BookItem item: bookitems) {
            booksMap.put(bookitems.indexOf(item), item);
        }
    }

    Books(){
        booksMap = new HashMap<>();
        ArrayList<BookItem> items = new ArrayList<>();
        items.add(new BookItem("Bhagavad Gita", 717, 700,"http://bbtacademic.com/wp-content/uploads/2016/08/en-bg-388x600.jpg"));
        items.add(new BookItem("Srimad Bhagavatam", 14625, 14094, "http://bbtacademic.com/wp-content/uploads/2016/08/bhagavatam_big.jpg"));
        items.add(new BookItem("Caitanya Caritamrta", 6624, 11555, "http://bbtacademic.com/wp-content/uploads/2016/01/Caitanya-caritamrta.jpg"));
        items.add(new BookItem("Krsna Book", 796, 0,"http://www.prabhupada.org.uk/images/spb05.jpg"));
        items.add(new BookItem("Sri Isopanishad", 138, 19, "http://bbtacademic.com/wp-content/uploads/2016/07/cover_iso_pb_0.png"));
        items.add(new BookItem("Nectar of Devotion", 399, 0, "http://bbtacademic.com/wp-content/uploads/2016/07/the-nectar-of-devotion.jpg"));
        items.add(new BookItem("Nectar of Instruction", 91, 11, "http://bbtacademic.com/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-9.53.51-PM.png"));
        items.add(new BookItem("TLC", 350, 0, "http://krishna.org/images/Misc/tlc_cover.jpg?x64805"));
        setBooks(items);
    }

    public static Books getBooksInstance() {
        if(instance == null){
            instance = new Books();
        }
        return instance;
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



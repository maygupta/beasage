package com.iskcon.isv.beasage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 3/19/17.
 */
public class Books {

    static ArrayList<BookItem> booksMap;
    static Books instance = null;

    Books(){
        booksMap = new ArrayList<>();
        booksMap.add(new BookItem(0,"Bhagavad Gita", 717, 700,"https://media.licdn.com/mpr/mpr/p/5/005/096/3d1/242b35e.jpg"));
        booksMap.add(new BookItem(1,"Srimad Bhagavatam", 14625, 14094, "http://bbtacademic.com/wp-content/uploads/2016/08/bhagavatam_big.jpg"));
        booksMap.add(new BookItem(2,"Caitanya Caritamrta", 6624, 11555, "http://bbtacademic.com/wp-content/uploads/2016/01/Caitanya-caritamrta.jpg"));
        booksMap.add(new BookItem(3,"Krsna Book", 796, 0,"http://www.prabhupada.org.uk/images/spb05.jpg"));
        booksMap.add(new BookItem(4,"Sri Isopanishad", 138, 19, "http://bbtacademic.com/wp-content/uploads/2016/07/cover_iso_pb_0.png"));
        booksMap.add(new BookItem(5,"Nectar of Devotion", 399, 0, "http://bbtacademic.com/wp-content/uploads/2016/07/the-nectar-of-devotion.jpg"));
        booksMap.add(new BookItem(6,"Nectar of Instruction", 91, 11, "http://bbtacademic.com/wp-content/uploads/2016/07/Screen-Shot-2016-07-26-at-9.53.51-PM.png"));
        booksMap.add(new BookItem(7,"TLC", 350, 0, "http://krishna.org/images/Misc/tlc_cover.jpg?x64805"));
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



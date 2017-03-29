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
        booksMap.add(new BookItem(1,"Srimad Bhagavatam", 14625, 14094, "http://www.goldenagemedia.org/itemimages/ProductImage1/srimad-bhagavatam---complete-18-volumes-set-(eng).jpg"));
        booksMap.add(new BookItem(2,"Caitanya Caritamrta", 6624, 11555, "http://korg.cdn.krishna.org/wp-content/uploads/2013/02/KA1_192.jpg?x64805"));
        booksMap.add(new BookItem(3,"Krsna Book", 796, 0,"http://korg.cdn.krishna.org/wp-content/uploads/2011/11/Krishna-enjoying-with-His-friends.jpg?x64805"));
        booksMap.add(new BookItem(4,"Sri Isopanishad", 138, 19, "https://i.ytimg.com/vi/AkU0WX-aCB0/hqdefault.jpg"));
        booksMap.add(new BookItem(5,"Nectar of Devotion", 399, 0, "http://covers.globalbbt.org/sites/default/files/covers/EN_NOD_1975.jpg"));
        booksMap.add(new BookItem(6,"Nectar of Instruction", 91, 11, "https://s-media-cache-ak0.pinimg.com/564x/b2/71/de/b271de222cfad5956f22f444852e7c29.jpg"));
        booksMap.add(new BookItem(7,"TLC", 350, 0, "http://www.utahkrishnas.org/wp-content/uploads/2010/11/caitanya-nityananda.jpg"));
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

    public ArrayList<BookItem> getBooksMap(){
        return booksMap;
    }

}



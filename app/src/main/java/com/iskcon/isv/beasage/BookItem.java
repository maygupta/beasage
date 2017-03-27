package com.iskcon.isv.beasage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayank on 3/19/17.
 */
public class BookItem implements Parcelable{
    int id;
    String name;
    int pages;
    int slokas;
    String url;
    int pagesRead;
    int slokasRead;

    public BookItem(int id,String n, int p, int s, String u) {
        this.id=id;
        name = n;
        pages = p;
        slokas = s;
        url = u;
    }

    public BookItem(int id,String n, int p, int s, String u,int pagesRead,int slokasRead) {
        this.id=id;
        name = n;
        pages = p;
        slokas = s;
        url = u;
        this.pagesRead=pagesRead;
        this.slokasRead=slokasRead;
    }

    protected BookItem(Parcel in) {
        id=in.readInt();
        name = in.readString();
        pages = in.readInt();
        slokas = in.readInt();
        url = in.readString();
        pagesRead = in.readInt();
        slokasRead = in.readInt();
    }

    public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
        @Override
        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(pages);
        parcel.writeInt(slokas);
        parcel.writeString(url);
        parcel.writeInt(pagesRead);
        parcel.writeInt(slokasRead);
    }
}

package com.iskcon.isv.beasage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by sumitgarg on 19/03/17.
 */

public class BeasageDbHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "beasage.db";

  public static final String TABLE_BEASAGE = "table_beasage";
  public static final String COLUMN_ID = "book_id";
  public static final String COLUMN_BOOK_NAME = "book_name";
  public static final String COLUMN_TOTAL_PAGES = "total_pages";
  public static final String COLUMN_TOTAL_SLOKAS = "total_slokas";
  public static final String COLUMN_PAGES_READ = "pages_read";
  public static final String COLUMN_SLOKAS_READ = "slokas_read";
  public static final String COLUMN_BOOK_URL = "book_url";

  private static final String DATABASE_CREATE = "create table "
      + TABLE_BEASAGE + "( " + COLUMN_ID
      + " integer primary key , " + COLUMN_BOOK_NAME
      + " text not null , "+
      COLUMN_TOTAL_PAGES +" integer not null ,"+
      COLUMN_TOTAL_SLOKAS +" integer not null ,"+
      COLUMN_PAGES_READ +" integer ,"+
      COLUMN_SLOKAS_READ +" integer,"+
      COLUMN_BOOK_URL+" text );";
  private SQLiteDatabase database;

  public void open() throws SQLException {
    database= getWritableDatabase();
  }

  public void close() {
    getWritableDatabase().close();
  }

  public BeasageDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    database = getWritableDatabase();
    database.close();
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(DATABASE_CREATE);
  }


  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    Log.w(BeasageDbHelper.class.getName(),
        "Upgrading database from version " + i + " to "
            + i1 + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEASAGE);
    onCreate(db);
  }

  public long insertNewBook(int id,String bookName,int totalPages,int totalSlokas,String url){
    ContentValues values = new ContentValues();
    values.put(COLUMN_ID,id);
    values.put(COLUMN_BOOK_NAME,bookName);
    values.put(COLUMN_TOTAL_PAGES,totalPages);
    values.put(COLUMN_TOTAL_SLOKAS,totalSlokas);
    values.put(COLUMN_BOOK_URL,url);

    long insertId = database.insert(TABLE_BEASAGE, null,
        values);

    return insertId;

  }

  public int removeBookFromTable(int id){
    int result=database.delete(TABLE_BEASAGE, COLUMN_ID
        + " = " + id, null);
    return result;
  }

  public long addBookData(int id,int pagesRead,int slokasRead){
    ContentValues values = new ContentValues();
    values.put(COLUMN_PAGES_READ,pagesRead);
    values.put(COLUMN_SLOKAS_READ,slokasRead);
    long updateId=database.update(TABLE_BEASAGE,values,COLUMN_ID+"="+id,null);
    return updateId;
  }

  public HashMap<Integer,BookItem> getAllBooks(){
    HashMap<Integer,BookItem> bookMap=new HashMap<>();
    Cursor cursor = database.query(TABLE_BEASAGE,
        new String[]{COLUMN_ID,COLUMN_BOOK_NAME,COLUMN_TOTAL_PAGES,COLUMN_TOTAL_SLOKAS,COLUMN_PAGES_READ,COLUMN_SLOKAS_READ,COLUMN_BOOK_URL}, null, null, null, null, null);
    if(cursor.moveToFirst()){
      while (cursor.moveToNext()){
        bookMap.put(cursor.getInt(0),new BookItem(cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getString(6),cursor.getInt(4),cursor.getInt(5)));
      }
    }
    return bookMap;
  }


}

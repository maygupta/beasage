package com.iskcon.isv.beasage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;

public class TrackerActivity extends AppCompatActivity {

    Books books;
    HashMap<Integer, BookItem> selectedBooks;
    private BeasageDbHelper beasageDbHelper;
    private HashMap<Integer,BookItem> prevoiusBooks;
    private LinearLayout linearLayout;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        books = Books.getBooksInstance();

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle("Book Tracker");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inflater = getLayoutInflater();
        linearLayout = (LinearLayout) findViewById(R.id.selectedBooksLL);

        beasageDbHelper=new BeasageDbHelper(this);
        try {
            beasageDbHelper.open();
            prevoiusBooks=beasageDbHelper.getAllBooks();
            beasageDbHelper.close();
            showPreviousBooks();
        }catch (SQLException e){
        }


        final Spinner spinner =(Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.books_array,
                R.layout.support_simple_spinner_dropdown_item );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        selectedBooks = new HashMap<>();

        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String item = (String) spinner.getSelectedItem();
                final Integer id = books.getBookByName(item);
                final BookItem bookItem = books.getBookById(id);

                try {
                    beasageDbHelper.open();
                    if(beasageDbHelper.isBookExists(id)){
                        Toast.makeText(getApplicationContext(), "Book already being tracked", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    beasageDbHelper.close();
                }catch (SQLException e){

                }
                final View view = inflater.inflate(R.layout.tracked_book_item, null);
                TextView tvBookName = (TextView) view.findViewById(R.id.tvBookName);
                tvBookName.setText(bookItem.name);

                Button btnRemove = (Button) view.findViewById(R.id.removeBtn);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      try {
                        beasageDbHelper.open();
                        int result=beasageDbHelper.removeBookFromTable(id);
                        if(result>0){
                          selectedBooks.remove(id);
                          linearLayout.removeView(view);
                        }else{
                          Toast.makeText(TrackerActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
                        }
                        beasageDbHelper.close();
                      }catch (SQLException e){
                        Toast.makeText(TrackerActivity.this,"Couldn't aremovedd data",Toast.LENGTH_LONG).show();
                      }

                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), BookTrackingActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });

                ImageView ivBook = (ImageView) view.findViewById(R.id.ivBook);
                Picasso.with(getApplicationContext()).load(bookItem.url).into(ivBook);

                try {
                    beasageDbHelper.open();
                    long result= beasageDbHelper.insertNewBook(id,bookItem.name,bookItem.pages,bookItem.slokas,bookItem.url);
                    if(result>=0){
                        linearLayout.addView(view);
                    }else{
                        Toast.makeText(TrackerActivity.this,"Couldn't add data",Toast.LENGTH_LONG).show();
                    }
                    beasageDbHelper.close();

                } catch (SQLException e) {
                    Toast.makeText(TrackerActivity.this,"Couldn't add data",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void showPreviousBooks(){
        if(prevoiusBooks!=null){
            for(final int key:prevoiusBooks.keySet()){
                final View view = inflater.inflate(R.layout.tracked_book_item, null);
                TextView tvBookName = (TextView) view.findViewById(R.id.tvBookName);
                tvBookName.setText(prevoiusBooks.get(key).name);
                ImageView ivBook = (ImageView) view.findViewById(R.id.ivBook);
                Picasso.with(getApplicationContext()).load(prevoiusBooks.get(key).url).into(ivBook);
                view.setTag(key);
                linearLayout.addView(view);

                Button btnRemove = (Button) view.findViewById(R.id.removeBtn);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            beasageDbHelper.open();
                            int result=beasageDbHelper.removeBookFromTable(key);
                            if(result>0){
                                selectedBooks.remove(key);
                                linearLayout.removeView(view);
                            }else{
                                Toast.makeText(TrackerActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
                            }
                            beasageDbHelper.close();
                        }catch (SQLException e){
                            Toast.makeText(TrackerActivity.this,"Couldn't aremovedd data",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), BookTrackingActivity.class);
                        intent.putExtra("id", key);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        beasageDbHelper.close();
    }
}

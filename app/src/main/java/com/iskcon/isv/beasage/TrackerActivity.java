package com.iskcon.isv.beasage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackerActivity extends AppCompatActivity {

    Books books;
    HashMap<Integer, BookItem> selectedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        books = Books.getBooksInstance();
        Picasso.with(this).setLoggingEnabled(true);

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

        final Spinner spinner =(Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.books_array,
                R.layout.support_simple_spinner_dropdown_item );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        selectedBooks = new HashMap<>();

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.selectedBooksLL);

        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String item = (String) spinner.getSelectedItem();
                final Integer id = books.getBookByName(item);
                BookItem bookItem = books.getBookById(id);

                if(selectedBooks.containsKey(id)) {
                    Toast.makeText(getApplicationContext(), "Book already being tracked", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedBooks.put(id, bookItem);

                final LayoutInflater inflater = getLayoutInflater();

                final View view = inflater.inflate(R.layout.tracked_book_item, null);
                TextView tvBookName = (TextView) view.findViewById(R.id.tvBookName);
                tvBookName.setText(bookItem.name);

                Button btnRemove = (Button) view.findViewById(R.id.removeBtn);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedBooks.remove(id);
                        linearLayout.removeView(view);
                    }
                });

                ImageView ivBook = (ImageView) view.findViewById(R.id.ivBook);

                Picasso.with(getApplicationContext()).load(bookItem.url).into(ivBook);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), BookTrackingActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });

                linearLayout.addView(view);
            }
        });
    }
}

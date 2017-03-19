package com.iskcon.isv.beasage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackerActivity extends AppCompatActivity {

    List<BookItem> items;
    Books books;
    HashMap<Integer, BookItem> selectedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        items = new ArrayList<>();
        items.add(new BookItem("Bhagavad Gita", 717, 700));
        items.add(new BookItem("Srimad Bhagavatam", 14625, 14094));
        items.add(new BookItem("Caitanya Caritamrta", 6624, 11555));
        items.add(new BookItem("Krsna Book", 796, 0));
        items.add(new BookItem("Sri Isopanishad", 138, 19));
        items.add(new BookItem("Nectar of Devotion", 399, 0));
        items.add(new BookItem("Nectar of Instruction", 91, 11));
        items.add(new BookItem("TLC", 350, 0));

        books = new Books();
        books.setBooks(items);

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

                LayoutInflater inflater = getLayoutInflater();

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

                linearLayout.addView(view);
            }
        });
    }
}

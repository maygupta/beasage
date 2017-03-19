package com.iskcon.isv.beasage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = (String) spinner.getSelectedItem();
                Integer id = books.getBookByName(item);
                selectedBooks.put(id, books.getBookById(id));
                View selectedBookView = new View(getApplicationContext());

                // TODO: Create Book view and add it to Dynamic Scroll View
                // Tracked Book Item view is add as tracked_book_item.xml
            }
        });
    }
}

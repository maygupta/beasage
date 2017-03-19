package com.iskcon.isv.beasage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookTrackingActivity extends AppCompatActivity {

    int pagesRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tracking);

        Intent i = getIntent();
        Books books = Books.getBooksInstance();
        final BookItem item = books.getBookById(i.getIntExtra("id", 0));

        final TextView tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        pagesRead = 0;
        tvPageCount.setText(pagesRead + "/" + item.pages + " pages");

        TextView tvBookName = (TextView) findViewById(R.id.tvBookName);
        tvBookName.setText(" of " + item.name);

        Button btn = (Button) findViewById(R.id.addPages);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) findViewById(R.id.editText);
                pagesRead += Integer.parseInt(tv.getText().toString());
                tvPageCount.setText(pagesRead + "/" + item.pages + " pages");
            }
        });

    }
}

package com.iskcon.isv.beasage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class BookTrackingActivity extends AppCompatActivity {

    int pagesRead;
    private BeasageDbHelper beasageDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tracking);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        beasageDbHelper=new BeasageDbHelper(this);

        Intent i = getIntent();
        Books books = Books.getBooksInstance();
        final int id=i.getIntExtra("id", 0);
        final BookItem item = books.getBookById(id);

        getSupportActionBar().setTitle(item.name);

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
                try{
                    beasageDbHelper.open();
                    long result = beasageDbHelper.addBookData(id,pagesRead,0);
                    if(result>0){
                        Toast.makeText(BookTrackingActivity.this,"Pages Added",Toast.LENGTH_LONG).show();
                    }
                    beasageDbHelper.close();
                }catch (SQLException e){

                }
            }
        });

    }
}

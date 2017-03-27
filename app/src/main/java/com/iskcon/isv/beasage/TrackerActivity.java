package com.iskcon.isv.beasage;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;

public class TrackerActivity extends AppCompatActivity {

    private Books books;
    private HashMap<Integer, BookItem> selectedBooks;
    private BeasageDbHelper beasageDbHelper;

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
               Intent intent=new Intent(TrackerActivity.this,TrackedBooksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        beasageDbHelper=new BeasageDbHelper(this);

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
                        Toast.makeText(getApplicationContext(), "Book already being tracked. Choose different Book", Toast.LENGTH_SHORT).show();
                        spinner.performClick();
                        return;
                    }
                    beasageDbHelper.close();
                }catch (SQLException e){

                }

                try {
                    beasageDbHelper.open();
                    long result= beasageDbHelper.insertNewBook(id,bookItem.name,bookItem.pages,bookItem.slokas,bookItem.url);
                    if(result>=0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrackerActivity.this);
                        builder.setMessage("Book Tracked Successfully")
                            .setTitle("Success");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(TrackerActivity.this,TrackedBooksActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        beasageDbHelper.close();
    }
}

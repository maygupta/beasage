package com.iskcon.isv.beasage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sumitgarg on 27/03/17.
 */

public class TrackedBooksActivity extends AppCompatActivity{

  private BeasageDbHelper beasageDbHelper;
  private ArrayList<BookItem> previousBooks;
  private RecyclerView bookRecyclerView;
  private TrackedBookAdapter trackedBookAdapter;
  private TrackedBookListener trackedBookListener;
  private FloatingActionButton addBook;
  private TextView noBooksTxt;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_books);
    beasageDbHelper=new BeasageDbHelper(this);

    Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
    getSupportActionBar().setTitle("Books Being Tracked");
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    bookRecyclerView = (RecyclerView) findViewById(R.id.book_result_view);
    noBooksTxt=(TextView)findViewById(R.id.no_books_txt);
    addBook=(FloatingActionButton)findViewById(R.id.add_book);
    bookRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(this));
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    bookRecyclerView.setLayoutManager(layoutManager);

    getBooksFromDB();

    if(previousBooks!=null && previousBooks.size()>0){
      noBooksTxt.setVisibility(View.GONE);
      bookRecyclerView.setVisibility(View.VISIBLE);
      trackedBookListener = new TrackedBookListener() {
        @Override
        public void deleteRecord(final int id) {

          AlertDialog.Builder builder = new AlertDialog.Builder(TrackedBooksActivity.this);
          builder.setMessage("Do you want to remove current book?")
              .setTitle("Alert");
          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              try {
                beasageDbHelper.open();
                int result=beasageDbHelper.removeBookFromTable(id);
                if(result>0){
                  AlertDialog.Builder builderInner = new AlertDialog.Builder(TrackedBooksActivity.this);
                  builderInner.setMessage("Book Deleted Successfully")
                      .setTitle("Success");
                  builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      getBooksFromDB();
                    }
                  });

                  AlertDialog dialogInner = builderInner.create();
                  dialogInner.show();
                }else{
                  Toast.makeText(TrackedBooksActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
                }
                beasageDbHelper.close();
              }catch (SQLException e){
                Toast.makeText(TrackedBooksActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
              }
            }
          });

          builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
          });

          AlertDialog dialog = builder.create();
          dialog.show();

        }

        @Override
        public void openBookDetails(int id, BookItem bookItem) {
          Intent intent = new Intent(getApplicationContext(), BookTrackingActivity.class);
          intent.putExtra("id", id);
          intent.putExtra("bookitem",bookItem);
          startActivity(intent);
        }
      };

      trackedBookAdapter = new TrackedBookAdapter(this,previousBooks,trackedBookListener);
      bookRecyclerView.setAdapter(trackedBookAdapter);
    }else{
      bookRecyclerView.setVisibility(View.GONE);
      noBooksTxt.setVisibility(View.VISIBLE);
    }

    addBook.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(TrackedBooksActivity.this, TrackerActivity.class);
        startActivity(i);
      }
    });
  }


  private void getBooksFromDB(){
    try {
      beasageDbHelper.open();
      previousBooks =beasageDbHelper.getAllBooks();
      beasageDbHelper.close();
    }catch (SQLException e){
    }

    if(previousBooks!=null && previousBooks.size()>0 && trackedBookAdapter!=null){
      trackedBookAdapter.updateList(previousBooks);
      trackedBookAdapter.notifyDataSetChanged();
      bookRecyclerView.setVisibility(View.VISIBLE);
      noBooksTxt.setVisibility(View.GONE);
    }else{
      bookRecyclerView.setVisibility(View.GONE);
      noBooksTxt.setVisibility(View.VISIBLE);
    }

  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    getBooksFromDB();
  }
}

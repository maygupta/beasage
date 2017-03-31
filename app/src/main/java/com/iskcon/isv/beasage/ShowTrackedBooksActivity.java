package com.iskcon.isv.beasage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sumitgarg on 27/03/17.
 */

public class ShowTrackedBooksActivity extends AppCompatActivity{

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
         deleteBookRecord(id,false);
        }

        @Override
        public void openBookDetails(int id, BookItem bookItem) {
          Intent intent = new Intent(getApplicationContext(), ShowBookDetailsActivity.class);
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
        SelectBookDialogFragment showBookDetailDialog = SelectBookDialogFragment.getInstance();
        showBookDetailDialog.show(getSupportFragmentManager(), "BookTracker");
      }
    });

    initSwipe();
  }


  private void initSwipe(){
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
       int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
          final BookItem item=previousBooks.get(position);
          deleteBookRecord(item.id,true);
        }
      }

      @Override
      public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Bitmap icon= BitmapFactory.decodeResource(getResources(), R.drawable.delete2);
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
          View itemView = viewHolder.itemView;
          float height = (float) itemView.getBottom() - (float) itemView.getTop();
          float width = height / 3;
          Paint p=new Paint();
          p.setColor(Color.parseColor("#D32F2F"));
          RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
          c.drawRect(background,p);
          RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
          c.drawBitmap(icon,null,icon_dest,p);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
      }
    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(bookRecyclerView);

  }

  private void deleteBookRecord(final int id, final boolean fromSwipe){
    AlertDialog.Builder builder = new AlertDialog.Builder(ShowTrackedBooksActivity.this);
    builder.setMessage("Do you want to remove current book?")
        .setTitle("Alert");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        try {
          beasageDbHelper.open();
          int result=beasageDbHelper.removeBookFromTable(id);
          if(result>0){
            AlertDialog.Builder builderInner = new AlertDialog.Builder(ShowTrackedBooksActivity.this);
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
            Toast.makeText(ShowTrackedBooksActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
          }
          beasageDbHelper.close();
        }catch (SQLException e){
          Toast.makeText(ShowTrackedBooksActivity.this,"Couldn't remove data",Toast.LENGTH_LONG).show();
        }
      }
    });

    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(fromSwipe)
        getBooksFromDB();
      }
    });

    AlertDialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
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

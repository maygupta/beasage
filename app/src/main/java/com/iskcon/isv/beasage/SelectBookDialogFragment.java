package com.iskcon.isv.beasage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sumitgarg on 28/03/17.
 */

public class SelectBookDialogFragment extends DialogFragment {

  private ShowTrackedBooksActivity context;
  private RelativeLayout mainLayout;
  private RadioGroup radioGroup;
  private Books books;
  private ArrayList<BookItem> booksMap;
  private Button selectBook;
  private BeasageDbHelper beasageDbHelper;


  public static SelectBookDialogFragment getInstance() {
    return new SelectBookDialogFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ShowTrackedBooksActivity) {
      this.context = (ShowTrackedBooksActivity) context;
    } else {
      throw new ClassCastException();
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View cvvDialog = inflater.inflate(R.layout.fragment_select_book, null);
    return cvvDialog;
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mainLayout = (RelativeLayout) view.findViewById(R.id.book_add_layout);
    radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
    selectBook = (Button) view.findViewById(R.id.select_btn);

  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    beasageDbHelper = new BeasageDbHelper(context);
    books = Books.getBooksInstance();
    booksMap = books.getBooksMap();
    BookItem bookItem;
    for (int i = 0; i < booksMap.size(); i++) {
      bookItem = booksMap.get(i);
      RadioButton rdbtn = new RadioButton(context);
      rdbtn.setId(i);
      rdbtn.setTag(bookItem);
      rdbtn.setText(bookItem.name);
      rdbtn.setSingleLine(true);
      rdbtn.setPadding(10, 10, 10, 20);
      rdbtn.setTextSize(18);
      radioGroup.addView(rdbtn);
      if (i == 0) {
        rdbtn.setChecked(true);
      }

      rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean b) {
          if (b) {
            BookItem item = (BookItem) view.getTag();
            View view1 = context.getLayoutInflater().inflate(R.layout.book_tracked_item, null);
            TextView bookName = (TextView) view1.findViewById(R.id.book_name);
            TextView bookProgress = (TextView) view1.findViewById(R.id.book_progress);
            Button btnDelete = (Button) view1.findViewById(R.id.removeBtn);
            ImageView bookImageView = (ImageView) view1.findViewById(R.id.book_image);

            bookName.setText(item.name);
            bookProgress.setText("Total Pages: " + item.pages + " / Total Slokas: " + item.slokas);
            btnDelete.setVisibility(View.GONE);
            Picasso.with(context).load(item.url).placeholder(R.drawable.img_books_d).into(bookImageView);

            new AlertDialog.Builder(context)
                .setView(view1)
                .setTitle("")
                .create().show();
          }
        }
      });
    }


    selectBook.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int id = radioGroup.getCheckedRadioButtonId();
        final BookItem selectedBook = booksMap.get(id);
        try {
          beasageDbHelper.open();
          if (beasageDbHelper.isBookExists(id)) {
            Toast.makeText(context, selectedBook.name + " is already tracked. Choose different Book", Toast.LENGTH_SHORT).show();
            return;
          }
          beasageDbHelper.close();
        } catch (SQLException e) {

        }

        try {
          beasageDbHelper.open();
          long result = beasageDbHelper.insertNewBook(selectedBook.id, selectedBook.name, selectedBook.pages, selectedBook.slokas, selectedBook.url);
          if (result >= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(selectedBook.name + " Tracked Successfully")
                .setTitle("Success");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, ShowTrackedBooksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
              }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

          } else {
            Toast.makeText(context, "Couldn't add data", Toast.LENGTH_LONG).show();
          }
          beasageDbHelper.close();

        } catch (SQLException e) {
          Toast.makeText(context, "Couldn't add data", Toast.LENGTH_LONG).show();
        }

      }
    });


  }
}

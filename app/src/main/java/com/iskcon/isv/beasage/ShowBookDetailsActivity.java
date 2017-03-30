package com.iskcon.isv.beasage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;

public class ShowBookDetailsActivity extends AppCompatActivity {

    private int pagesRead;
    private BeasageDbHelper beasageDbHelper;
    private BookItem bookItem;
    private TextView toggleDayButton;
    private TextView toggleWeekButton;
    private TextView toggleMonthButton;
    private TextView toggleYearButton;
    private TextInputLayout inputLayoutPages;
    private TextView tv;

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
        final int id=i.getIntExtra("id", 0);
         bookItem = i.getParcelableExtra("bookitem");

        getSupportActionBar().setTitle(bookItem.name);

        toggleDayButton=(TextView)findViewById(R.id.day);
        toggleWeekButton=(TextView)findViewById(R.id.week);
        toggleMonthButton=(TextView)findViewById(R.id.month);
        toggleYearButton=(TextView)findViewById(R.id.year);
        inputLayoutPages=(TextInputLayout)findViewById(R.id.input_layout_pages);
        tv = (TextView) findViewById(R.id.editText);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final TextView tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        pagesRead = bookItem.pagesRead;
        tvPageCount.setText(pagesRead + "/" + bookItem.pages + " pages");


        if(savedInstanceState==null) {
          toggleDayButton.setTextColor(getResources().getColor(R.color.orange));
          final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
          BarGraphFragment barGraphFragment = BarGraphFragment.getInstance("day");
          fragmentTransaction.add(R.id.graphFrame, barGraphFragment);
          fragmentTransaction.addToBackStack(null);
          fragmentTransaction.commit();
        }


        toggleDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDayButton.setTextColor(getResources().getColor(R.color.orange));
                toggleWeekButton.setTextColor(getResources().getColor(R.color.grey));
                toggleMonthButton.setTextColor(getResources().getColor(R.color.grey));
                toggleYearButton.setTextColor(getResources().getColor(R.color.grey));
                final FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                BarGraphFragment barGraphFragment =  BarGraphFragment.getInstance("day");
                fragmentTransaction.replace(R.id.graphFrame,barGraphFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        toggleWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleWeekButton.setTextColor(getResources().getColor(R.color.orange));
                toggleDayButton.setTextColor(getResources().getColor(R.color.grey));
                toggleMonthButton.setTextColor(getResources().getColor(R.color.grey));
                toggleYearButton.setTextColor(getResources().getColor(R.color.grey));

                final FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                BarGraphFragment barGraphFragment =  BarGraphFragment.getInstance("week");
                fragmentTransaction.replace(R.id.graphFrame,barGraphFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        toggleMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMonthButton.setTextColor(getResources().getColor(R.color.orange));
                toggleDayButton.setTextColor(getResources().getColor(R.color.grey));
                toggleWeekButton.setTextColor(getResources().getColor(R.color.grey));
                toggleYearButton.setTextColor(getResources().getColor(R.color.grey));

                final FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                BarGraphFragment barGraphFragment =  BarGraphFragment.getInstance("month");
                fragmentTransaction.replace(R.id.graphFrame,barGraphFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        toggleYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYearButton.setTextColor(getResources().getColor(R.color.orange));
                toggleDayButton.setTextColor(getResources().getColor(R.color.grey));
                toggleWeekButton.setTextColor(getResources().getColor(R.color.grey));
                toggleMonthButton.setTextColor(getResources().getColor(R.color.grey));
            }
        });


        Button addBtn = (Button) findViewById(R.id.addPages);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tv.getText().toString())){
                  inputLayoutPages.setError("Please enter no of pages");
                    pagesRead=bookItem.pagesRead;
                    return;
                }

                if(Integer.valueOf(tv.getText().toString())==0){
                    inputLayoutPages.setError("Please enter atleast 1 page");
                    pagesRead=bookItem.pagesRead;
                    return;
                }
                pagesRead += Integer.parseInt(tv.getText().toString());
                if(pagesRead>bookItem.pages){
                    inputLayoutPages.setError("You Cannot read more than "+bookItem.pages +" pages");
                    pagesRead=bookItem.pagesRead;
                  return;
                }

                inputLayoutPages.setError(null);

                tvPageCount.setText(pagesRead + "/" + bookItem.pages + " pages");

                updatePageDb(id,pagesRead,0);
            }
        });


        Button delBtn = (Button) findViewById(R.id.delPages);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tv.getText().toString())){
                    inputLayoutPages.setError("Please enter no of pages");
                    pagesRead=bookItem.pagesRead;
                    return;
                }

                if(Integer.valueOf(tv.getText().toString())==0){
                    inputLayoutPages.setError("Please enter atleast 1 page");
                    pagesRead=bookItem.pagesRead;
                    return;
                }

                pagesRead -= Integer.parseInt(tv.getText().toString());
                if(pagesRead<0){
                    inputLayoutPages.setError("You Cannot delete more than "+bookItem.pagesRead +" pages");
                    pagesRead=bookItem.pagesRead;
                    return;
                }

                inputLayoutPages.setError(null);
                tvPageCount.setText(pagesRead + "/" + bookItem.pages + " pages");

                updatePageDb(id,pagesRead,0);
            }
        });

    }

    private void updatePageDb(int id,int pagesRead,int slokasRead){
        try{
            beasageDbHelper.open();
            long result = beasageDbHelper.addBookData(id,pagesRead,slokasRead);
            if(result>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowBookDetailsActivity.this);
                builder.setMessage("Pages Deleted Successfully")
                    .setTitle("Success");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ShowBookDetailsActivity.this,ShowTrackedBooksActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
            beasageDbHelper.close();
        }catch (SQLException e){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

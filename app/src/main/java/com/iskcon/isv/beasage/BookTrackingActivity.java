package com.iskcon.isv.beasage;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.SQLException;

public class BookTrackingActivity extends AppCompatActivity {

    int pagesRead;
    private BeasageDbHelper beasageDbHelper;
    GraphView graph;

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

        ImageView ivBook = (ImageView) findViewById(R.id.ivBookBig);
        Picasso.with(getApplicationContext()).load(item.url).into(ivBook);

        graph = (GraphView) findViewById(R.id.graph);
        Date d1 = new Date(2017, 03, 21);
        Date d2 = new Date(2017, 03, 22);
        Date d3 = new Date(2017, 03, 23);
        Date d4 = new Date(2017, 03, 24);
        Date d5 = new Date(2017, 03, 25);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 21),
                new DataPoint(d2, 24),
                new DataPoint(d3, 13),
                new DataPoint(d4, 21),
                new DataPoint(d5, 21),
        });
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if(data.getY() >= 21) {
                    return Color.GREEN;
                } else {
                    return Color.RED;
                }
            }
        });
        series.setSpacing(30);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));

        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d5.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

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

//                        Date d6 = new Date(2017, 03, 26);
//                        BarGraphSeries<DataPoint>mSeries2 = new BarGraphSeries<>(new DataPoint[]{
//                                new DataPoint(d6, pagesRead),
//                        });
//                        graph.addSeries(mSeries2);
                    }
                    beasageDbHelper.close();
                }catch (SQLException e){

                }
            }
        });

    }
}

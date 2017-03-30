package com.iskcon.isv.beasage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sumitgarg on 29/03/17.
 */

public class BarGraphFragment extends Fragment {

  private String query="day";

  public BarGraphFragment(){
    super();
  }

  public static BarGraphFragment getInstance(String query){
    Bundle bundle= new Bundle();
    bundle.putString("query",query);
    BarGraphFragment barGraphFragment = new BarGraphFragment();
    barGraphFragment.setArguments(bundle);
    return barGraphFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(getArguments()!=null && getArguments().getString("query")!=null){
      query=getArguments().getString("query");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.graph_fragment,null);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    BarChart chart = (BarChart) view.findViewById(R.id.chart);
    chart.setMaxVisibleValueCount(60);
    chart.setPinchZoom(true);
    chart.setDrawGridBackground(false);
    BarData data = new BarData(getXAxisValues(), getDataSet());
    chart.setData(data);
    chart.animateXY(2000, 2000);
    chart.invalidate();

  }

  private ArrayList<BarDataSet> getDataSet() {
    ArrayList<BarDataSet> dataSets = null;

    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
    valueSet1.add(v1e1);


    ArrayList<BarEntry> valueSet2 = new ArrayList<>();
    BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
    valueSet2.add(v2e1);

    ArrayList<BarEntry> valueSet3 = new ArrayList<>();
    BarEntry v2e2 = new BarEntry(150.000f, 0); // Jan
    valueSet3.add(v2e2);

    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Read");
    barDataSet1.setColor(Color.rgb(0, 155, 0));
    BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Deleted");
    barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

    dataSets = new ArrayList<>();
    dataSets.add(barDataSet1);
    dataSets.add(barDataSet2);
    return dataSets;
  }

  private ArrayList<String> getXAxisValues() {
    ArrayList<String> xAxis = new ArrayList<>();
    if(query.equals("day")){
      Calendar calendar = Calendar.getInstance();
      SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
      xAxis.add(mdformat.format(calendar.getTime()));
    }else if(query.equals("week")){
      xAxis.add("Mon");
      xAxis.add("Tue");
      xAxis.add("Wed");
      xAxis.add("Thu");
      xAxis.add("Fri");
      xAxis.add("Sat");
      xAxis.add("sun");
    }else if(query.equals("month")){
      xAxis.add("Jan");
      xAxis.add("Feb");
      xAxis.add("Mar");
      xAxis.add("Apr");
      xAxis.add("May");
      xAxis.add("Jun");
      xAxis.add("Jul");
      xAxis.add("Aug");
      xAxis.add("Sep");
      xAxis.add("Oct");
      xAxis.add("Nov");
      xAxis.add("Dec");

    }else if(query.equals("year")){

    }

    return xAxis;
  }


}

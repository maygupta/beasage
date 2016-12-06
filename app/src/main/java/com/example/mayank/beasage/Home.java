package com.example.mayank.beasage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements WheelPicker.OnItemSelectedListener {

    List<String> books;
    int currentDurationPos = 2;
    int currentCountPos = 2;
    int curBookPos = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        books = new ArrayList<>();
        books.add("Bhagavad Gita");
        books.add("Srimad Bhagavatam");
        books.add("Caitanya Caritamrta");
        books.add("Krsna Book");
        books.add("Sri Isopanishad");
        books.add("Nectar of Devotion");
        books.add("Nectar of Instruction");
        books.add("TLC");

        WheelPicker wheelPicker1 = (WheelPicker) findViewById(R.id.main_wheel_left);
        wheelPicker1.setData(books);

        wheelPicker1.setSelectedItemPosition(2);

        List<String> data2 = new ArrayList<>();
        data2.add("  Day");
        data2.add("  Week");
        data2.add("  Month");
        data2.add("  Year");

        WheelPicker wheelPicker2 = (WheelPicker) findViewById(R.id.main_wheel_center);
        wheelPicker2.setData(data2);
        wheelPicker2.setSelectedItemPosition(2);

        List<String> data3 = new ArrayList<>();

        for (int i = 1; i < 30; i++)
            data3.add(String.format("  %d",i));

        WheelPicker wheelPicker3 = (WheelPicker) findViewById(R.id.main_wheel_right);
        wheelPicker3.setData(data3);
        wheelPicker3.setSelectedItemPosition(2);

        wheelPicker1.setOnItemSelectedListener(this);
        wheelPicker2.setOnItemSelectedListener(this);
        wheelPicker3.setOnItemSelectedListener(this);

        setResultView();

//        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams
//                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        flContainer.addView(wheelPicker, flParams);

    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.main_wheel_left:
                curBookPos = position;
                break;
            case R.id.main_wheel_center:
                currentDurationPos = position;
                break;
            case R.id.main_wheel_right:
                currentCountPos = position;
                break;
        }
        setResultView();
    }

    private void setResultView() {
        TextView tvBook = (TextView) findViewById(R.id.tvBook);
        tvBook.setText(books.get(curBookPos));
        TextView tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvDuration.setText(String.format(" %d %s", currentCountPos + 1, getDurationByPos()));

        int sbPages = 15119;
        int bgPages = 868;
        int ccPages = 6621;
        int krPages = 706;
        int ndPages = 407;
        int tlPages = 347;
        int isPages = 158;
        int niPages = 130;
        int sbSlokas = 14094;
        int bgSlokas = 700;
        int ccSlokas = 11555;
        int krSlokas = 0;
        int ndSlokas = 0;
        int niSlokas = 11;
        int tlSlokas = 0;
        int isSlokas = 19;

        int selScripturePages = 0;
        switch(curBookPos) {
            case 0:
                selScripturePages = bgPages;
                break;
            case 1:
                selScripturePages = sbPages;
                break;
            case 2:
                selScripturePages = ccPages;
                break;
            case 3:
                selScripturePages = krPages;
                break;
            case 4:
                selScripturePages = isPages;
                break;
            case 5:
                selScripturePages = ndPages;
                break;
            case 6:
                selScripturePages = tlPages;
                break;
            case 7:
                selScripturePages = niPages;
                break;
            default: break;
        }

        int numPagesDay = 0;
        switch(currentDurationPos) {
            case 0:
                numPagesDay = selScripturePages / (currentCountPos + 1);
                break;
            case 1:
                numPagesDay = selScripturePages / (7 * (currentCountPos + 1));
                break;
            case 2:
                numPagesDay = selScripturePages / (30 * (currentCountPos + 1));
                break;
            case 3:
                numPagesDay = selScripturePages / (365 * (currentCountPos + 1));
                break;
        }

        TextView tvResult = (TextView) findViewById(R.id.tvResult);
        tvResult.setText(String.format("%d", numPagesDay));
    }

    private String getDurationByPos() {
        String duration = "";
        switch (currentDurationPos) {
            case 0:
                duration = "day";
                break;
            case 1:
                duration = "week";
                break;
            case 2:
                duration = "month";
                break;
            case 3:
                duration = "year";
                break;
        }
        if ( currentCountPos > 0 ) {
            duration = String.format(duration + "s");
        }
        return duration;
    }
}

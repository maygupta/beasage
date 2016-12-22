package com.iskcon.isv.beasage;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.michael.easydialog.EasyDialog;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements WheelPicker.OnItemSelectedListener {

    List<String> books;
    int currentDurationPos = 2;
    int currentCountPos = 2;
    int curBookPos = 2;
    boolean isPage = true;
    WheelPicker wheelPicker1;
    WheelPicker wheelPicker2;
    WheelPicker wheelPicker3;
    SwitchCompat pageSlokaSwitch;
    EasyDialog bookDialog;
    EasyDialog switchDialog;
    EasyDialog resultsDialog;

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

        wheelPicker1 = (WheelPicker) findViewById(R.id.main_wheel_left);
        wheelPicker1.setData(books);

        wheelPicker1.setSelectedItemPosition(2);

        List<String> data2 = new ArrayList<>();
        data2.add("  Day");
        data2.add("  Week");
        data2.add("  Month");
        data2.add("  Year");

        wheelPicker2 = (WheelPicker) findViewById(R.id.main_wheel_center);
        wheelPicker2.setData(data2);
        wheelPicker2.setSelectedItemPosition(2);

        List<String> data3 = new ArrayList<>();

        for (int i = 1; i < 30; i++)
            data3.add(String.format("  %d",i));

        wheelPicker3 = (WheelPicker) findViewById(R.id.main_wheel_right);
        wheelPicker3.setData(data3);
        wheelPicker3.setSelectedItemPosition(2);

        wheelPicker1.setOnItemSelectedListener(this);
        wheelPicker2.setOnItemSelectedListener(this);
        wheelPicker3.setOnItemSelectedListener(this);

        setResultView();

        pageSlokaSwitch = (SwitchCompat) findViewById(R.id.switch1);
        pageSlokaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPage = !isChecked;
                setResultView();
            }
        });

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

    public void handleReminder(View tvReminder) {

    }

    public void handleOpenBbta(View tvBbta) {
        String url = "http://www.bbtacademic.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void handleViewDemo(View tvDemo) {

        View view = this.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
        bookDialog = new EasyDialog(Home.this)
                .setLayout(view)
                .setBackgroundColor(Home.this.getResources().getColor(R.color.background_color_black))
                // .setLocation(new location[])//point in screen
                .setLocationByAttachedView(findViewById(R.id.main_wheel_left))
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24)
                .show();

        View view2 = this.getLayoutInflater().inflate(R.layout.layout_switch_tip, null);
        switchDialog = new EasyDialog(Home.this)
                .setLayout(view2)
                .setBackgroundColor(Home.this.getResources().getColor(R.color.background_color_black))
                // .setLocation(new location[])//point in screen
                .setLocationByAttachedView(findViewById(R.id.switch1))
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24);

        View view3 = this.getLayoutInflater().inflate(R.layout.layout_results_tip, null);
        resultsDialog = new EasyDialog(Home.this)
                .setLayout(view3)
                .setBackgroundColor(Home.this.getResources().getColor(R.color.background_color_black))
                // .setLocation(new location[])//point in screen
                .setLocationByAttachedView(findViewById(R.id.ivCircle))
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24);
    }

    public void startDemo(View v) {
        bookDialog.dismiss();

        curBookPos = 0;
        final Handler handler = new Handler();

//        Toast.makeText(this, "Starting Demo!", Toast.LENGTH_SHORT).show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker1.setSelectedItemPosition(4);
            }
        }, 500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker1.setSelectedItemPosition(5);
            }
        }, 1000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker1.setSelectedItemPosition(6);
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker1.setSelectedItemPosition(7);
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker1.setSelectedItemPosition(0);
            }
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker2.setSelectedItemPosition(1);
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker2.setSelectedItemPosition(0);
            }
        }, 3500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker3.setSelectedItemPosition(1);
            }
        }, 4000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker3.setSelectedItemPosition(0);
                currentDurationPos = 0;
                currentCountPos = 0;
                setResultView();
                resultsDialog.show();
            }
        }, 4500);

    }

    public void startSwitchDemo(View v) {
        resultsDialog.dismiss();
        switchDialog.show();
    }

    public void flipSwitch(View v) {
        switchDialog.dismiss();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                pageSlokaSwitch.setChecked(true);
                setResultView();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                wheelPicker3.setSelectedItemPosition(0);
                pageSlokaSwitch.setChecked(false);
                setResultView();
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Toast.makeText(getApplicationContext(), "Demo Complete!", Toast.LENGTH_LONG).show();
            }
        }, 3000);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private void setResultView() {

        TextView tvBook = (TextView) findViewById(R.id.tvBook);

        String complete = getColoredSpanned("To complete reading", "#FFCC33");
        String book = getColoredSpanned(String.format(" %s", books.get(curBookPos)), "#303F9F");
        tvBook.setText(Html.fromHtml(book));

        TextView tvDuration = (TextView) findViewById(R.id.tvDuration);
        String in = getColoredSpanned("in", "#FFCC33");
        String duration = getColoredSpanned(String.format(" %d %s", currentCountPos + 1, getDurationByPos()), "#fd951b1b");
        String pleaseRead = getColoredSpanned("please read", "#FFCC33");
        tvDuration.setText(Html.fromHtml(in + " " + duration + " " + pleaseRead));

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

        TextView tvResult = (TextView) findViewById(R.id.tvResult);

        if(isPage) {
            int selScripturePages = 0;
            switch (curBookPos) {
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
                    selScripturePages = niPages;
                    break;
                case 7:
                    selScripturePages = tlPages;
                    break;
                default:
                    break;
            }

            double numPagesDay = 0;
            switch (currentDurationPos) {
                case 0:
                    numPagesDay = selScripturePages  / (currentCountPos + 1);
                    break;
                case 1:
                    numPagesDay = selScripturePages  / (7 * (currentCountPos + 1));
                    break;
                case 2:
                    numPagesDay = selScripturePages / (30 * (currentCountPos + 1));
                    break;
                case 3:
                    numPagesDay = selScripturePages / (365 * (currentCountPos + 1));
                    break;
            }

            if(numPagesDay < 1) {
                numPagesDay = 1;
            }
            numPagesDay = Math.ceil(numPagesDay);
            tvResult.setText(String.format("%d", Math.round(numPagesDay)));
            TextView pageOrSloka = (TextView) findViewById(R.id.pageOrSloka);
            pageOrSloka.setText("pages per day");
        } else {
            int selScriptureSlokas = 0;
            switch (curBookPos) {
                case 0:
                    selScriptureSlokas = bgSlokas;
                    break;
                case 1:
                    selScriptureSlokas = sbSlokas;
                    break;
                case 2:
                    selScriptureSlokas = ccSlokas;
                    break;
                case 3:
                    selScriptureSlokas = krSlokas;
                    break;
                case 4:
                    selScriptureSlokas = isSlokas;
                    break;
                case 5:
                    selScriptureSlokas = ndSlokas;
                    break;
                case 6:
                    selScriptureSlokas = niSlokas;
                    break;
                case 7:
                    selScriptureSlokas = tlSlokas;
                    break;
                default:
                    break;
            }

            double numSlokasDay = 0.0;
            switch (currentDurationPos) {
                case 0:
                    numSlokasDay = selScriptureSlokas / (currentCountPos + 1);
                    break;
                case 1:
                    numSlokasDay = selScriptureSlokas / (7 * (currentCountPos + 1));
                    break;
                case 2:
                    numSlokasDay = selScriptureSlokas / (30 * (currentCountPos + 1));
                    break;
                case 3:
                    numSlokasDay = selScriptureSlokas / (365 * (currentCountPos + 1));
                    break;
            }

            if(selScriptureSlokas == 0) {
                tvResult.setText("N/A");
            } else {
                if(numSlokasDay < 1) {
                    numSlokasDay = 1;
                }
                tvResult.setText(String.format("%d", Math.round(numSlokasDay)));
            }
            TextView pageOrSloka = (TextView) findViewById(R.id.pageOrSloka);
            pageOrSloka.setText("slokas per day");
        }
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

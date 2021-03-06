package com.iskcon.isv.beasage;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.michael.easydialog.EasyDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    TextView tvBook;
    TextView tvDuration;
    TextView tvResult;
    TextView pageOrSloka;
    boolean isDemoStarted = false;
    TextView tvReminder;

    public static final String EXTRA_CURR_BOOK_POS="extra_curBookPos";
    public static final String EXTRA_CURRENT_DURATION_POS="extra_currentDurationPos";
    public static final String EXTRA_CURRENT_COUNT_POS="extra_currentCountPos";
    public static final String EXTRA_PAGES_SLOKA="extra_pages_sloka";
    public static final String MyPREFERENCES = "besage_Prefs" ;
    public static final String PREF_IS_REMINDER_SET = "nameKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvBook = (TextView) findViewById(R.id.tvBook);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvResult= (TextView) findViewById(R.id.tvResult);
        pageOrSloka = (TextView) findViewById(R.id.pageOrSloka);
        tvReminder=(TextView)findViewById(R.id.tvReminder);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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


        List<String> data2 = new ArrayList<>();
        data2.add("  Day");
        data2.add("  Week");
        data2.add("  Month");
        data2.add("  Year");

        wheelPicker2 = (WheelPicker) findViewById(R.id.main_wheel_center);
        wheelPicker2.setData(data2);

        List<String> data3 = new ArrayList<>();

        for (int i = 1; i < 30; i++)
            data3.add(String.format("  %d",i));

        wheelPicker3 = (WheelPicker) findViewById(R.id.main_wheel_right);
        wheelPicker3.setData(data3);

        wheelPicker1.setOnItemSelectedListener(this);
        wheelPicker2.setOnItemSelectedListener(this);
        wheelPicker3.setOnItemSelectedListener(this);

        pageSlokaSwitch = (SwitchCompat) findViewById(R.id.switch1);
        pageSlokaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPage = !isChecked;
                setResultView();
            }
        });

        readDataFromIntent(getIntent());

        if(sharedpreferences!=null){
            if(sharedpreferences.getBoolean(PREF_IS_REMINDER_SET,false)){
                tvReminder.setText("Cancel Reminder");
                tvReminder.setTag(true);
            }else{
                tvReminder.setTag(false);
            }
        }

        tvReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((boolean)view.getTag()==true){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setMessage("Do you want to remove current reminder?")
                            .setTitle("Cancel Reminder");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelReminder();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    handleReminder();
                }
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

    public void handleReminder() {
        showTimePickerDialog();
    }

    //This functions shows timepicker
    public void showTimePickerDialog(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Home.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                cancelReminder(); // To cancel any previous Reminder
                Calendar cal=Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                cal.set(Calendar.MINUTE,selectedMinute);
                Toast.makeText(Home.this,"Reminder set at "+selectedHour+":"+selectedMinute +" to read "+tvBook.getText().toString() + " daily",Toast.LENGTH_LONG).show();
                setReminder(cal);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Reminder Time");
        mTimePicker.show();
    }

    public void setReminder(Calendar cal){
        String contextText="To complete reading "+tvBook.getText().toString()+" "+tvDuration.getText().toString()+" please read " +tvResult.getText().toString()+" "+pageOrSloka.getText().toString();
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_text)
                .setContentTitle("Reminder")
                .setContentText(contextText)
                .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.app))
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(contextText));

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(EXTRA_CURR_BOOK_POS, curBookPos);
        notificationIntent.putExtra(EXTRA_CURRENT_COUNT_POS, currentCountPos);
        notificationIntent.putExtra(EXTRA_CURRENT_DURATION_POS, currentDurationPos);
        notificationIntent.putExtra(EXTRA_PAGES_SLOKA,pageSlokaSwitch.isChecked());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY ,
                pendingIntent);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_IS_REMINDER_SET,true);
        editor.commit();

        tvReminder.setText("Cancel Reminder");
        tvReminder.setTag(true);

    }

    public void cancelReminder(){
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_IS_REMINDER_SET,false);
        editor.commit();

        tvReminder.setText("Set Reminder");
        tvReminder.setTag(false);
    }

    public void readDataFromIntent(Intent intent){
        curBookPos = intent.getIntExtra(EXTRA_CURR_BOOK_POS,2);
        currentDurationPos = intent.getIntExtra(EXTRA_CURRENT_DURATION_POS,2);
        currentCountPos = intent.getIntExtra(EXTRA_CURRENT_COUNT_POS,2);

        if(intent.getBooleanExtra(EXTRA_PAGES_SLOKA,false)){
            pageSlokaSwitch.setChecked(true);
        }

        wheelPicker1.setSelectedItemPosition(curBookPos);
        wheelPicker2.setSelectedItemPosition(currentDurationPos);
        wheelPicker3.setSelectedItemPosition(currentCountPos);

        setResultView();
    }

    public void handleOpenBbta(View tvBbta) {
        String url = "http://bbtacademic.com/product-tag/beasage_app/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readDataFromIntent(intent);
    }

    public void handleViewDemo(View tvDemo) {
        if(isDemoStarted) {
            return;
        }
        isDemoStarted = true;

        View view = this.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
        bookDialog = new EasyDialog(Home.this)
            .setLayout(view)
            .setBackgroundColor(Home.this.getResources().getColor(R.color.background_color_black))
            // .setLocation(new location[])//point in screen
            .setLocationByAttachedView(findViewById(R.id.spinner))
            .setGravity(EasyDialog.GRAVITY_BOTTOM)
            .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
            .setAnimationAlphaShow(1000, 0.3f, 1.0f)
            .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
            .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
            .setTouchOutsideDismiss(true)
            .setMatchParent(false)
            .setMarginLeftAndRight(24, 24)
            .show();
        startDemo();

        View view2 = this.getLayoutInflater().inflate(R.layout.layout_switch_tip, null);
        switchDialog = new EasyDialog(Home.this)
            .setLayout(view2)
            .setBackgroundColor(Home.this.getResources().getColor(R.color.background_color_black))
            // .setLocation(new location[])//point in screen
            .setLocationByAttachedView(findViewById(R.id.switch1))
            .setGravity(EasyDialog.GRAVITY_TOP)
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
            .setGravity(EasyDialog.GRAVITY_TOP)
            .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
            .setAnimationAlphaShow(1000, 0.3f, 1.0f)
            .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
            .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
            .setTouchOutsideDismiss(true)
            .setMatchParent(false)
            .setMarginLeftAndRight(24, 24);
    }

    public void startDemo() {
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
                setResultView();
                bookDialog.dismiss();
                resultsDialog.show();
            }
        }, 3500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                resultsDialog.dismiss();
                switchDialog.show();
                flipSwitch();
            }
        }, 6000);

    }

    public void flipSwitch() {
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
                pageSlokaSwitch.setChecked(false);
                setResultView();
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                switchDialog.dismiss();
                isDemoStarted = false;
                Toast.makeText(getApplicationContext(), "Demo Complete!", Toast.LENGTH_LONG).show();
            }
        }, 3000);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    private void setResultView() {

        String complete = getColoredSpanned("To complete reading", "#FFCC33");
        List<String> vedabaseIds = Arrays.asList("bg", "sb", "cc", "kb", "iso", "nod", "noi", "tlc");
        // 303F9F
//        String book = String.format("<a href=\"http://vedabase.com/%s\">%s</a>",
//                vedabaseIds.get(curBookPos), books.get(curBookPos));
        tvBook.setText(Html.fromHtml("<a href=\""+ "http://vedabase.com/" + vedabaseIds.get(curBookPos) + "\">" + books.get(curBookPos) + "</a>"));

//        tvBook.setText(Html.fromHtml(book));
        tvBook.setClickable(true);
        tvBook.setMovementMethod (LinkMovementMethod.getInstance());

        String in = getColoredSpanned("in", "#FFFFFF");
        String duration = getColoredSpanned(String.format(" %d %s", currentCountPos + 1, getDurationByPos()), "#a10707");
        tvDuration.setText(Html.fromHtml(in + " " + duration));

        int sbPages = 14625;
        int bgPages = 717;
        int ccPages = 6624;
        int krPages = 796;
        int ndPages = 399;
        int tlPages = 350;
        int isPages = 138;
        int niPages = 91;
        int sbSlokas = 14094;
        int bgSlokas = 700;
        int ccSlokas = 11555;
        int krSlokas = 0;
        int ndSlokas = 0;
        int niSlokas = 11;
        int tlSlokas = 0;
        int isSlokas = 19;



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
                    numPagesDay = selScripturePages  / (1.0 * (currentCountPos + 1));
                    break;
                case 1:
                    numPagesDay = selScripturePages  / (7.0 * (currentCountPos + 1));
                    break;
                case 2:
                    numPagesDay = selScripturePages / (30.0 * (currentCountPos + 1));
                    break;
                case 3:
                    numPagesDay = selScripturePages / (365.0 * (currentCountPos + 1));
                    break;
            }

            if(numPagesDay < 1) {
                numPagesDay = 1;
            }
            numPagesDay = Math.ceil(numPagesDay);
            tvResult.setText(String.format("%d", (int) numPagesDay));
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
                    numSlokasDay = selScriptureSlokas / (1.0 * (currentCountPos + 1));
                    break;
                case 1:
                    numSlokasDay = selScriptureSlokas / (7.0 * (currentCountPos + 1));
                    break;
                case 2:
                    numSlokasDay = selScriptureSlokas / (30.0 * (currentCountPos + 1));
                    break;
                case 3:
                    numSlokasDay = selScriptureSlokas / (365.0 * (currentCountPos + 1));
                    break;
            }

            if(selScriptureSlokas == 0) {
                tvResult.setText("N/A");
            } else {
                if(numSlokasDay < 1) {
                    numSlokasDay = 1;
                }
                numSlokasDay = Math.ceil(numSlokasDay);
                tvResult.setText(String.format("%d", (int) numSlokasDay));
            }
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

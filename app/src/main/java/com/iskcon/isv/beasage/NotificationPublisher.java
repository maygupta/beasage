package com.iskcon.isv.beasage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.iskcon.isv.beasage.Home.EXTRA_CURRENT_COUNT_POS;
import static com.iskcon.isv.beasage.Home.EXTRA_CURRENT_DURATION_POS;
import static com.iskcon.isv.beasage.Home.EXTRA_CURR_BOOK_POS;
import static com.iskcon.isv.beasage.Home.EXTRA_PAGES_SLOKA;

/**
 * Created by sumitgarg on 21/12/16.
 */

public class NotificationPublisher extends BroadcastReceiver {

  public static String NOTIFICATION_ID = "notification-id";
  public static String NOTIFICATION = "notification";

  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = intent.getParcelableExtra(NOTIFICATION);
    int id = intent.getIntExtra(NOTIFICATION_ID, 0);

    Intent targetIntent = new Intent(context, Home.class);
    targetIntent.putExtra(EXTRA_CURR_BOOK_POS,intent.getIntExtra(EXTRA_CURR_BOOK_POS,0));
    targetIntent.putExtra(EXTRA_CURRENT_DURATION_POS,intent.getIntExtra(EXTRA_CURRENT_DURATION_POS,0));
    targetIntent.putExtra(EXTRA_CURRENT_COUNT_POS,intent.getIntExtra(EXTRA_CURRENT_COUNT_POS,0));
    targetIntent.putExtra(EXTRA_PAGES_SLOKA,intent.getBooleanExtra(EXTRA_PAGES_SLOKA,false));

    int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
    targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pIntent = PendingIntent.getActivity(context, iUniqueId,
        targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    notification.contentIntent=pIntent;
    notificationManager.notify(id, notification);
  }


}

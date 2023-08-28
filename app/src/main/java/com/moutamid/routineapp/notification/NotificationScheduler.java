package com.moutamid.routineapp.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.routineapp.utils.Constants;

import java.util.Calendar;
import java.util.Random;

public class NotificationScheduler {

    public static void scheduleNotification(Context context, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an intent to trigger the notification
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                new Random().nextInt(100), intent,//NOTIFICATION_ID
                PendingIntent.FLAG_IMMUTABLE);//FLAG_UPDATE_CURRENT

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

//        Stash.put(Constants.LAST_TIME, calendar.getTimeInMillis());
    }
}


package com.inventrax_pepsi.listeners;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by android on 5/3/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    //private static long TIME_INTERVAL = 60 * 60 * 1000 ; // For Production 1 Hour

    private static long TIME_INTERVAL = 60 * 1000 ; // For Production 1 Minute

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DeviceBootReceiver.TIME_INTERVAL, pendingIntent);

           // Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }



}

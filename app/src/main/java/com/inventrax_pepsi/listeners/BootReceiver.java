package com.inventrax_pepsi.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inventrax_pepsi.activities.SplashScreenActivity;

/**
 * Created by android on 4/21/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, SplashScreenActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
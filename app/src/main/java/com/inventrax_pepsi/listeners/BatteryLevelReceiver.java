package com.inventrax_pepsi.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by android on 2/29/2016.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Battery Low",Toast.LENGTH_LONG).show();

    }



}

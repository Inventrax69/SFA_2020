package com.inventrax_pepsi.kioskmode.receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by android on 4/23/2016.
 */
public class DeviceAdministrator  extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {

    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Do you want to disable the administrator?";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {

    }

}

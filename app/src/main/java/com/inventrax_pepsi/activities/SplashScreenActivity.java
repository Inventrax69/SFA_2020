package com.inventrax_pepsi.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.listeners.AlarmReceiver;
import com.inventrax_pepsi.services.appupdate.UpdateServiceUtils;
import com.inventrax_pepsi.services.gcm.GCMRegisterUtils;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Author   : Naresh P.
 * Date		: 26/04/2016 11:03 AM
 * Purpose	: Splash Screen Activity
 */

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private Gson gson;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private static int JOB_ID = 1010;

    private PendingIntent pendingIntent;

    //private static long TIME_INTERVAL = 60 * 60 * 1000 ; // For Production 1 Hour

    private static long TIME_INTERVAL = 60 * 1000 ; // For Production 1 Minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        try {

            sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", getApplicationContext());

            gson = new GsonBuilder().create();

            new GCMRegisterUtils();

            // Check for new updates
            new UpdateServiceUtils().checkUpdate();


            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setResource(R.raw.tab_wallpaper);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                   prepareNextIntent();
              //locking the remaining apps it should be enabled
                    //Intent intent = new Intent(SplashScreenActivity.this, AdminActivity.class);
                    //startActivity(intent);
                  // finish();

                }

            }, SPLASH_TIME_OUT);


        }catch (Exception ex){
            Logger.Log(SplashScreenActivity.class.getName(),ex);
        }


    }

    public void prepareNextIntent() {

        Intent intent = null;
        try {
            if (sharedPreferencesUtils.loadPreferenceAsBoolean("login_status", false)) {

                if (sharedPreferencesUtils.loadPreference("loginUserObject", "") != "") {
                    User user = gson.fromJson(sharedPreferencesUtils.loadPreference("loginUserObject", ""), User.class);
                    if (user != null)
                        AppController.setUser(user);
                }

                if (sharedPreferencesUtils.loadPreference("userLoginInfo", "") != "") {
                    LoginInfo loginInfo = gson.fromJson(sharedPreferencesUtils.loadPreference("userLoginInfo", ""), LoginInfo.class);
                    if (loginInfo != null)
                        AppController.setLoginInfo(loginInfo);
                }

                if (AppController.getUser() != null && AppController.getLoginInfo() != null) {

                    if (AppController.mapUserRoutes == null)
                        AppController.mapUserRoutes = new HashMap<>();

                    if (AppController.getUser().getRouteList()!=null) {
                        for (RouteList routeList : AppController.getUser().getRouteList()) {
                            if (!AppController.mapUserRoutes.containsKey(routeList.getRouteCode()))
                                AppController.mapUserRoutes.put(routeList.getRouteCode(), routeList.getRouteName());
                        }
                    }

                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);

                } else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }

            } else {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }

            startActivity(intent);

            // close this activity
            finish();

        } catch (Exception ex) {

            DialogUtils.showAlertDialog(this, "Error while loading user information");
            return;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void setAlarm(){
        try
        {
            /* Retrieve a PendingIntent that will perform a broadcast */
            Intent alarmIntent = new Intent(SplashScreenActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(SplashScreenActivity.this, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), SplashScreenActivity.TIME_INTERVAL, pendingIntent);

            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Logger.Log(SplashScreenActivity.class.getName(),ex);
            return;
        }
    }


    private void cancel() {
        try {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Logger.Log(SplashScreenActivity.class.getName(),ex);
            return;
        }
    }

}

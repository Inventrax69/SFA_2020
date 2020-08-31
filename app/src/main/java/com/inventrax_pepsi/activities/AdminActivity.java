package com.inventrax_pepsi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.kioskmode.KioskUtils;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;

import java.util.HashMap;


/**
 * Author   : Naresh P.
 * Date		: 26/04/2016 11:03 AM
 * Purpose	: Kiosk Mode Activity
 */

public class AdminActivity extends AppCompatActivity {

    private Button btnEnableAdmin;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private Gson gson;
    private KioskUtils kioskUtils;
    private Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_admin);

        sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", getApplicationContext());

        gson = new GsonBuilder().create();

        activity=this;

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


                    intent = new Intent(AdminActivity.this, MainActivity.class);

                } else {
                    intent = new Intent(AdminActivity.this, LoginActivity.class);
                }

            } else {
                intent = new Intent(AdminActivity.this, LoginActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_CANCELED) {

                    Toast.makeText(getApplicationContext(),"Device Admin Cancelled",Toast.LENGTH_LONG).show();

                } else if (resultCode == Activity.RESULT_OK) {

                    if (kioskUtils.isAdminActivated()) {

                        Toast.makeText(getApplicationContext(),"Device Admin Activated",Toast.LENGTH_LONG).show();

                        if (!kioskUtils.isKioskModeEnabled()) {

                            kioskUtils.activateELM(getString(R.string.elm_key));

                            kioskUtils.enableKioskMode(true);
                        }
                    }

                    if (kioskUtils.isKioskModeEnabled())
                    {
                        prepareNextIntent();
                    }


                }
            }
        } catch (Exception e) {
            Log.e(AdminActivity.this.getLocalClassName(), e.getMessage());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadFormControls();

    }


    private void loadFormControls(){

        if (kioskUtils == null)
            kioskUtils=new KioskUtils(this);

        btnEnableAdmin=(Button)findViewById(R.id.btnEnableAdmin);
        btnEnableAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    if (!kioskUtils.isAdminActivated()) {
                        kioskUtils.activateAdmin();
                    }


                }catch (Exception ex){
                    Logger.Log(AdminActivity.class.getName(),ex);
                    Toast.makeText(getApplicationContext(),"Error while enabling kiosk mode",Toast.LENGTH_LONG).show();
                }

            }
        });

        try {

            if (kioskUtils.isKioskModeEnabled()) {

                prepareNextIntent();

            }else {
                /*DialogUtils.showAlertDialog(this,"Please click enable admin button");
                return;*/

                if (!kioskUtils.isAdminActivated()) {
                    kioskUtils.activateAdmin();
                }

            }

        }catch (Exception ex){
            Logger.Log(AdminActivity.class.getName(),ex);
            Toast.makeText(getApplicationContext(),"Error while enabling kiosk mode",Toast.LENGTH_LONG).show();

        }

    }

}

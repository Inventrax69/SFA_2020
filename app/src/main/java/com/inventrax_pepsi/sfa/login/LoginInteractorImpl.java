package com.inventrax_pepsi.sfa.login;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerReturn;
import com.inventrax_pepsi.database.TableUser;
import com.inventrax_pepsi.database.TableVehicleLoad;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.User;
import com.inventrax_pepsi.sfa.pojos.DeviceInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoginInteractorImpl implements LoginInteractor {

    private SharedPreferencesUtils sharedPreferencesUtils;
    private OnLoginFinishedListener listener;
    private Gson gson;
    private boolean isRememberEnabled;
    String userName="",password="";
    private DatabaseHelper databaseHelper;
    private TableUser tableUser;
    private TableVehicleLoad tableVehicleLoad;
    private TableVehicleStock tableVehicleStock;
    private TableCustomerReturn tableCustomerReturn;


    public LoginInteractorImpl(){

        gson=new GsonBuilder().create();
        sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", AbstractApplication.get());
        databaseHelper=DatabaseHelper.getInstance();
        tableUser=databaseHelper.getTableUser();

        tableVehicleLoad=databaseHelper.getTableVehicleLoad();
        tableVehicleStock=databaseHelper.getTableVehicleStock();
        tableCustomerReturn=databaseHelper.getTableCustomerReturn();


    }

    @Override
    public void login(final String username, final String password,boolean isRememberEnabled, final OnLoginFinishedListener listener) {

        try {



            this.listener = listener;
            this.isRememberEnabled = isRememberEnabled;
            this.userName = username;
            this.password = password;

            checkLoadandDelete();

            RootObject rootObject = new RootObject();

            rootObject.setServiceCode(ServiceCode.LOGIN);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setDeviceInfo(DeviceInfo.getInstance());
            loginInfo.setMode(1);
            loginInfo.setLoginTime(DateUtils.getTimeStamp());
            loginInfo.setUserLoginId(username);
            loginInfo.setPwd(password);
            loginInfo.setgCM(AppController.DEVICE_GCM_REGISTER_ID);

            rootObject.setLoginInfo(loginInfo);

            new LoginAsyncTask().execute(gson.toJson(rootObject));

        }catch (Exception ex){
            Logger.Log(LoginInteractorImpl.class.getName());
            listener.onLoginError("Error while login");
            return;
        }

    }

    private class LoginAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {


            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(params[0]);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            return SoapUtils.getJSONResponse(propertyInfoList,ServiceURLConstants.METHOD_LOGIN_REQUEST);
        }

        @Override
        protected void onPostExecute(String response) {

            if ( response!=null && !TextUtils.isEmpty(response) ) {

                try
                {

                    JSONObject responseJsonObject = new JSONObject(response.toString());

                    JSONObject doLoginResult = responseJsonObject.getJSONObject("RootObject");

                    RootObject rootObject = gson.fromJson(doLoginResult.toString(), RootObject.class);

                    ExecutionResponse executionResponse = rootObject.getExecutionResponse();

                    if (executionResponse.getSuccess() == 1) {

                        String loginInfoJSON="",userJSON="";
                        com.inventrax_pepsi.sfa.pojos.User sfaUser=null;

                        if (rootObject.getUsers()==null)
                            return;

                        sfaUser=rootObject.getUsers().get(0);

                        loginInfoJSON=gson.toJson(rootObject.getLoginInfo());

                        userJSON=gson.toJson(sfaUser);

                        if (sfaUser != null) {

                            AppController.setUser(sfaUser);

                            if (AppController.mapUserRoutes == null)
                                AppController.mapUserRoutes = new HashMap<>();

                            if (sfaUser.getRouteList()!=null) {
                                for (RouteList routeList : sfaUser.getRouteList()) {
                                    if (!AppController.mapUserRoutes.containsKey(routeList.getRouteCode()))
                                        AppController.mapUserRoutes.put(routeList.getRouteCode(), routeList.getRouteName());
                                }
                            }

                            sharedPreferencesUtils.savePreference("loginUserObject",userJSON );

                        }

                        if (rootObject.getLoginInfo() != null) {

                            AppController.setLoginInfo(rootObject.getLoginInfo());
                            sharedPreferencesUtils.savePreference("userLoginInfo", loginInfoJSON);

                        }

                        if (isRememberEnabled){

                            sharedPreferencesUtils.savePreference("isRememberPasswordChecked",true);
                            sharedPreferencesUtils.savePreference("userId",userName);
                            sharedPreferencesUtils.savePreference("password",password);

                        }else {
                            sharedPreferencesUtils.savePreference("isRememberPasswordChecked",false);
                        }

                        tableUser.deleteAllUsers();

                        User user=new User();
                        user.setUserJSON(userJSON);
                        user.setLoginInfoJSON(loginInfoJSON);
                        user.setUserLogInId(userName);
                        user.setUserId(sfaUser.getUserId());

                        tableUser.createUser(user);

                        listener.onSuccess();

                    }
                    else {
                        listener.onLoginError("Error while login");
                    }

                }catch(Exception ex){
                    Logger.Log(LoginInteractorImpl.class.getName(),ex);
                    listener.onLoginError("Error while login");
                }
            }
            else {
                listener.onLoginError("Error while login");
            }
        }
    }


    private void checkLoadandDelete(){

        try {

            LoginInfo loginInfo = null;

            String strLoginInfo = sharedPreferencesUtils.loadPreference("userLoginInfo", "");

            if (!TextUtils.isEmpty(strLoginInfo)) {

                loginInfo = gson.fromJson(strLoginInfo, LoginInfo.class);

                if (loginInfo != null) {

                    if (!loginInfo.getUserLoginId().equalsIgnoreCase(userName)) {
                        deleteLoadInfo();
                        return;
                    }

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    //Date loginDate = format.parse("2016-05-21 18:36:41.330");
                    Date loginDate = format.parse(loginInfo.getLoginTime());
                    Date currentDate = format.parse(DateUtils.getTimeStamp());
                    if (currentDate.compareTo(loginDate) != 0) {
                        deleteLoadInfo();
                        return;
                    }
                }

            } else {
                deleteLoadInfo();
                return;
            }
        }catch (Exception ex){
            deleteLoadInfo();
            return;
        }

    }

    private void deleteLoadInfo(){
        try
        {

            tableVehicleLoad.deleteAllVehicleLoads();
            tableVehicleStock.deleteAllVehicleStocks();
            tableCustomerReturn.deleteAllCustomerReturns();


        }catch (Exception ex){
            Logger.Log(LoginInteractorImpl.class.getName(),ex);
            return;
        }
    }


}



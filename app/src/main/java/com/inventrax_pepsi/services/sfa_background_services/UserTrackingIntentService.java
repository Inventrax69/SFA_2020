package com.inventrax_pepsi.services.sfa_background_services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.database.pojos.UserTracking;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.sfa.pojos.UserTrackHistory;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 21-Mar-16.
 */
public class UserTrackingIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = UserTrackingIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver userTrackingResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableUserTracking tableUserTracking;
    private TableJSONMessage tableJSONMessage;

    public UserTrackingIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableUserTracking=databaseHelper.getTableUserTracking();
        tableJSONMessage=databaseHelper.getTableJSONMessage();

        gson=new GsonBuilder().create();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();
            userTrackingResultReceiver=intent.getParcelableExtra("UserTrackingResultReceiver");

            int customerId= intent.getExtras().getInt("CustomerId");
            boolean isCheckIn=intent.getExtras().getBoolean("IsCheckIn");

            if (customerId!=0){

                processRequestFromUser(customerId, isCheckIn);

            }else{

                processRequestFromListener();

            }

        }catch (Exception ex){

            Logger.Log(TAG, ex);
            userTrackingResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }

    private void processRequestFromUser(int customerId,boolean isCheckIn){

        try {

            UserTracking userTracking = null;

            if (isCheckIn)
            {

                userTracking = tableUserTracking.getSingleUserTracking(customerId);

            }else{

                userTracking=tableUserTracking.getSingleUserTrackingForCheckOut(customerId);

            }

            if (userTracking!=null){

                userTrackingResultReceiver.send(STATUS_RUNNING, bundle);

                sendUserTrackInfo(userTracking);

            }else {
                userTrackingResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            userTrackingResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 3 -> User Tracking Notifications
            // NotificationId -> CustomerId

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, 3);

            for (JSONMessage jsonMessage:jsonMessageList){

                List<UserTracking> userTrackings= tableUserTracking.getAllUserTrackingDetails(jsonMessage.getNotificationId());

                for (UserTracking userTracking:userTrackings) {

                    if (userTracking != null) {

                        userTrackingResultReceiver.send(STATUS_RUNNING, bundle);

                        sendUserTrackInfo(userTracking);

                    } else {

                        userTrackingResultReceiver.send(STATUS_ERROR, bundle);
                    }
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendUserTrackInfo(UserTracking userTracking){

        try
        {
            List<UserTrackHistory> userTrackHistoryList=new ArrayList<>();

            UserTrackHistory userTrackHistory=new UserTrackHistory();

            userTrackHistory.setUserId(AppController.getUser().getUserId());
            userTrackHistory.setCustomerCode(userTracking.getCustomerCode());
            userTrackHistory.setCustomerId(userTracking.getCustomerId());
            userTrackHistory.setIsCheckOut((TextUtils.isEmpty(userTracking.getCheckOutTimestamp()) ? false : true));
            userTrackHistory.setTimestamp((userTrackHistory.isCheckOut() == true ? userTracking.getCheckOutTimestamp() : userTracking.getCheckInTimestamp()));
            userTrackHistory.setLatitude(userTracking.getLatitude());
            userTrackHistory.setLongitude(userTracking.getLongitude());

            userTrackHistoryList.add(userTrackHistory);



            if (userTrackHistoryList!=null && userTrackHistoryList.size() >0 ) {

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.DO_CHECK_IN_OUT);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setUserTrackings(userTrackHistoryList);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);


                JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(userTracking.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }

                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CHECK_IN_OUT),userTracking)){

                    userTrackingResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(userTracking.getJsonMessageAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5 ){

                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);

                    }

                    userTrackingResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            userTrackingResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean processOrderResponse(String jsonResponse,UserTracking userTracking){

        boolean status=false;

        try
        {
            if (!TextUtils.isEmpty(jsonResponse)){

                JSONObject jsonObject = new JSONObject(jsonResponse);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse=null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse!=null) {

                    if (executionResponse.getSuccess() == 1) {

                        JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(userTracking.getJsonMessageAutoIncId());

                        if (jsonMessage!=null) {
                            jsonMessage.setSyncStatus(1);
                            tableJSONMessage.updateJSONMessage(jsonMessage);
                        }

                        status=true;

                    }
                }


            }else {

                userTrackingResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            userTrackingResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

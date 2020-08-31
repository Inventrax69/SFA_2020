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
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetPullout;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetPullout;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.CustomerAssetPullout;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 21-Mar-16.
 */
public class AssetPulloutIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = AssetPulloutIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver assetPulloutResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableAssetPullout tableAssetPullout;
    private TableJSONMessage tableJSONMessage;

    public AssetPulloutIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableAssetPullout = databaseHelper.getTableAssetPullout();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();
        
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();

            assetPulloutResultReceiver=intent.getParcelableExtra("AssetPulloutResultReceiver");

            int autoIncId= intent.getExtras().getInt("AutoIncId");

            if ( autoIncId != 0 )
            {

                processRequestFromUser(autoIncId);

            }else{

                processRequestFromListener();

            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetPulloutResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(int autoIncId){

        try{

            AssetPullout assetPullout = tableAssetPullout.getAssetPullout(autoIncId);

            if (assetPullout!=null){

                assetPulloutResultReceiver.send(STATUS_RUNNING, bundle);

                //sendAssetPullout(assetPullout);
                processRequestFromListener();

            }else {

                assetPulloutResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetPulloutResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 8 -> Asset Pullout Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.AssetPullout.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                //AssetPullout assetPullout = tableAssetPullout.getAssetPullout(jsonMessage.getNotificationId());

                if ( jsonMessage != null ){

                    assetPulloutResultReceiver.send(STATUS_RUNNING, bundle);

                    sendAssetPullout(jsonMessage);

                }else {

                    assetPulloutResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendAssetPullout(JSONMessage jsonMessage){

        try
        {
            List<CustomerAssetPullout> customerAssetPullouts=new ArrayList<>();

            CustomerAssetPullout customerAssetPullout=gson.fromJson(jsonMessage.getJsonMessage(),CustomerAssetPullout.class);

            customerAssetPullouts.add(customerAssetPullout);

            if (customerAssetPullouts!=null && customerAssetPullouts.size()>0 ) {

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_ASSET_PULLOUT);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setCustomerAssetPullouts(customerAssetPullouts);

                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(assetPullout.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }

                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CREATE_ASSET_PULLOUT),jsonMessage)){

                    assetPulloutResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(jsonMessage.getAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    assetPulloutResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetPulloutResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean processOrderResponse(String jsonResponse,JSONMessage jsonMessage){

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

                        List<CustomerAssetPullout> customerAssetPullouts= rootObject.getCustomerAssetPullouts();

                        if ( customerAssetPullouts != null && customerAssetPullouts.size()>0 ){

                            CustomerAssetPullout customerAssetPullout=customerAssetPullouts.get(0);

                            if ( customerAssetPullout != null ) {

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mAssetPullout.getJsonMessageAutoIncId());

                                AssetPullout assetPullout=new AssetPullout();

                                assetPullout.setJson(gson.toJson(customerAssetPullout));
                                assetPullout.setQRCode(customerAssetPullout.getQRCode());
                                assetPullout.setCustomerId(customerAssetPullout.getCustomerId());
                                assetPullout.setAutoIncId(jsonMessage.getNotificationId());
                                assetPullout.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableAssetPullout.updateAssetPullout(assetPullout);

                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                            }

                        }

                        status = true;

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                assetPulloutResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetPulloutResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

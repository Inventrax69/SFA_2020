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
import com.inventrax_pepsi.database.TableAssetCapture;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetCapture;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.CustomerAuditInfo;
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
public class AssetCaptureIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = AssetCaptureIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver assetCaptureResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableAssetCapture tableAssetCapture;
    private TableJSONMessage tableJSONMessage;

    public AssetCaptureIntentService() {
        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableAssetCapture=databaseHelper.getTableAssetCapture();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();
        
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();

            assetCaptureResultReceiver=intent.getParcelableExtra("AssetCaptureResultReceiver");

            String QR_CODE= intent.getExtras().getString("QR_CODE");

            if (!TextUtils.isEmpty(QR_CODE)){
                processRequestFromUser(QR_CODE);
            }else{
                processRequestFromListener();
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetCaptureResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(String QR_Code){

        try{

            AssetCapture assetCapture=tableAssetCapture.getAssetCapture(QR_Code);

            if (assetCapture!=null){

                assetCaptureResultReceiver.send(STATUS_RUNNING, bundle);

                //sendAssetCapture(assetCapture);
                processRequestFromListener();

            }else {

                assetCaptureResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetCaptureResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 6 -> Asset Capture Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.AssetCapture.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                //AssetCapture assetCapture= tableAssetCapture.getAssetCapture(jsonMessage.getNotificationId());

                if (jsonMessage!=null){

                    assetCaptureResultReceiver.send(STATUS_RUNNING, bundle);

                    sendAssetCapture(jsonMessage);

                }else {

                    assetCaptureResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendAssetCapture(JSONMessage jsonMessage){

        try
        {
            CustomerAuditInfo customerAuditInfo= gson.fromJson(jsonMessage.getJsonMessage(), CustomerAuditInfo.class);

            if (customerAuditInfo!=null) {

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_ASSET_CAPTURE);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setCustomerAuditInfo(customerAuditInfo);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(assetCapture.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }

                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CREATE_ASSET_CAPTURE_HISTORY),jsonMessage)){

                    assetCaptureResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(jsonMessage.getAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    assetCaptureResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetCaptureResultReceiver.send(STATUS_ERROR,bundle);
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

                        CustomerAuditInfo customerAuditInfo=rootObject.getCustomerAuditInfo();

                        if ( customerAuditInfo != null  ){

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mAssetCapture.getJsonMessageAutoIncId());

                                AssetCapture assetCapture=new AssetCapture();

                                assetCapture.setJson(gson.toJson(customerAuditInfo));
                                assetCapture.setCustomerId(customerAuditInfo.getCustomerId());
                                assetCapture.setAutoIncId(jsonMessage.getNotificationId());
                                assetCapture.setQRCode(customerAuditInfo.getAssetCaptureHistories()!=null && customerAuditInfo.getAssetCaptureHistories().size()>0 ? customerAuditInfo.getAssetCaptureHistories().get(0).getqRCode():"" );
                                assetCapture.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableAssetCapture.updateAssetCapture(assetCapture);

                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                status = true;

                                Intent counterBroadcastIntent=new Intent();
                                counterBroadcastIntent.setAction("com.inventrax.broadcast.assetcapture");
                                sendBroadcast(counterBroadcastIntent);

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                assetCaptureResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetCaptureResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

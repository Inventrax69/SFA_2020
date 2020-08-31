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
import com.inventrax_pepsi.database.TableAssetRequest;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetRequest;
import com.inventrax_pepsi.database.pojos.JSONMessage;
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
public class AssetRequestIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = AssetRequestIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver assetRequestResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableAssetRequest tableAssetRequest;
    private TableJSONMessage tableJSONMessage;

    public AssetRequestIntentService() {
        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableAssetRequest=databaseHelper.getTableAssetRequest();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();
        
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();

            assetRequestResultReceiver=intent.getParcelableExtra("AssetRequestResultReceiver");

            int autoIncId= intent.getExtras().getInt("AutoIncId");

            if ( autoIncId != 0 )
            {
                processRequestFromUser(autoIncId);

            }else{

                processRequestFromListener();

            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetRequestResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(int autoIncId){

        try{

            AssetRequest assetRequest=tableAssetRequest.getAssetRequest(autoIncId);

            if (assetRequest!=null){

                assetRequestResultReceiver.send(STATUS_RUNNING, bundle);

                //sendAssetRequest(assetRequest);

                processRequestFromListener();


            }else {

                assetRequestResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetRequestResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 9 -> Asset Request Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.AssetRequest.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                //AssetRequest assetRequest= tableAssetRequest.getAssetRequest(jsonMessage.getNotificationId());

                if ( jsonMessage != null ){

                    assetRequestResultReceiver.send(STATUS_RUNNING, bundle);

                    sendAssetRequest(jsonMessage);

                }else {

                    assetRequestResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendAssetRequest(JSONMessage jsonMessage){

        try
        {

            List<com.inventrax_pepsi.sfa.pojos.AssetRequest> mAssetRequestList=new ArrayList<>();
            com.inventrax_pepsi.sfa.pojos.AssetRequest mAssetRequest=gson.fromJson(jsonMessage.getJsonMessage(), com.inventrax_pepsi.sfa.pojos.AssetRequest.class);

            if (mAssetRequest!=null) {

                mAssetRequestList.add(mAssetRequest);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_ASSET_REQUEST);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setAssetRequests(mAssetRequestList);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(assetRequest.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }


                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CREATE_ASSET_REQUEST),jsonMessage)){

                    assetRequestResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(jsonMessage.getAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    assetRequestResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetRequestResultReceiver.send(STATUS_ERROR,bundle);
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

                        List<com.inventrax_pepsi.sfa.pojos.AssetRequest> assetRequests=rootObject.getAssetRequests();

                        if (assetRequests!=null && assetRequests.size()>0 ){

                            com.inventrax_pepsi.sfa.pojos.AssetRequest assetRequest=assetRequests.get(0);

                            if ( assetRequest != null ) {

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mAssetRequest.getJsonMessageAutoIncId());
                                AssetRequest mAssetRequest=new AssetRequest();

                                mAssetRequest.setJson(gson.toJson(assetRequest));
                                mAssetRequest.setCustomerId(mAssetRequest.getCustomerId());
                                mAssetRequest.setAutoIncId(jsonMessage.getNotificationId());
                                mAssetRequest.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableAssetRequest.updateAssetRequest(mAssetRequest);

                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                status = true;

                            }

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                assetRequestResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetRequestResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

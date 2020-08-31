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
import com.inventrax_pepsi.database.TableAssetComplaint;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetComplaint;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.OutletComplaint;
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
public class AssetComplaintIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = AssetComplaintIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver assetComplaintResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableAssetComplaint tableAssetComplaint;
    private TableJSONMessage tableJSONMessage;

    public AssetComplaintIntentService() {
        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableAssetComplaint=databaseHelper.getTableAssetComplaint();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();
        
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();

            assetComplaintResultReceiver=intent.getParcelableExtra("AssetComplaintResultReceiver");

            int autoIncId= intent.getExtras().getInt("AutoIncId");

            if (autoIncId!=0){
                processRequestFromUser(autoIncId);
            }else{
                processRequestFromListener();
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetComplaintResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(int auto_inc_id){

        try{

            AssetComplaint assetComplaint=tableAssetComplaint.getAssetComplaint(auto_inc_id);

            if (assetComplaint!=null){

                assetComplaintResultReceiver.send(STATUS_RUNNING, bundle);

                //sendAssetComplaint(assetComplaint);

                processRequestFromListener();

            }else {

                assetComplaintResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetComplaintResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 5 -> Asset Complaint Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.AssetComplaint.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

               // AssetComplaint assetComplaint = tableAssetComplaint.getAssetComplaint(jsonMessage.getNotificationId());

                if (jsonMessage!=null){

                    assetComplaintResultReceiver.send(STATUS_RUNNING, bundle);

                    sendAssetComplaint(jsonMessage);

                }else {

                    assetComplaintResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendAssetComplaint(JSONMessage jsonMessage){

        try
        {
            List<OutletComplaint> outletComplaintList=new ArrayList<>();

            OutletComplaint outletComplaint= gson.fromJson(jsonMessage.getJsonMessage(), OutletComplaint.class);

            if (outletComplaint!=null) {

                outletComplaintList.add(outletComplaint);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_ASSET_COMPLAINTS);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setOutletComplaints(outletComplaintList);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(assetComplaint.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }


                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_ASSET_COMPLAINT),jsonMessage)){

                    assetComplaintResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(jsonMessage.getAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    assetComplaintResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetComplaintResultReceiver.send(STATUS_ERROR,bundle);
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

                        List<OutletComplaint> outletComplaintList=rootObject.getOutletComplaints();

                        if (outletComplaintList!=null && outletComplaintList.size()>0){

                            OutletComplaint outletComplaint=outletComplaintList.get(0);

                            if ( outletComplaint != null ) {

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mAssetComplaint.getJsonMessageAutoIncId());

                                AssetComplaint assetComplaint=new AssetComplaint();

                                assetComplaint.setComplaintJSON(gson.toJson(outletComplaint));
                                assetComplaint.setCustomerId(outletComplaint.getCustomerId());
                                assetComplaint.setAutoIncId(jsonMessage.getNotificationId());
                                assetComplaint.setComplaintId(outletComplaint.getOutletComplaintId());
                                assetComplaint.setComplaintStatus(outletComplaint.getComplaintStatusId());
                                assetComplaint.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableAssetComplaint.updateAssetComplaint(assetComplaint);

                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                status = true;

                                Intent counterBroadcastIntent=new Intent();
                                counterBroadcastIntent.setAction("com.inventrax.broadcast.assetcomplaint");
                                sendBroadcast(counterBroadcastIntent);


                            }

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                assetComplaintResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            assetComplaintResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

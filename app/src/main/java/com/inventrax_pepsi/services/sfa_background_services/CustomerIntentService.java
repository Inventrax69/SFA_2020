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
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.Customer;
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
public class CustomerIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = CustomerIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver customerResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private TableJSONMessage tableJSONMessage;

    public CustomerIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableCustomer = databaseHelper.getTableCustomer();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();
        
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();

            customerResultReceiver=intent.getParcelableExtra("CustomerResultReceiver");

            int auto_inc_id = intent.getExtras().getInt("AutoIncId");

            if (auto_inc_id!=0){
                processRequestFromUser(auto_inc_id);
            }else{
                processRequestFromListener();
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(int auto_inc_id){

        try{

            Customer customer=tableCustomer.getCustomerByAutoIncId(auto_inc_id);

            if (customer!=null){

                customerResultReceiver.send(STATUS_RUNNING, bundle);

                //sendCustomer(customer);

                processRequestFromListener();

            }else {

                customerResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 10 -> Customer Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.Customer.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                //Customer customer= tableCustomer.getCustomerByAutoIncId(jsonMessage.getNotificationId());

                if (jsonMessage!=null){

                    customerResultReceiver.send(STATUS_RUNNING, bundle);

                    sendCustomer(jsonMessage);

                }else {

                    customerResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendCustomer(JSONMessage jsonMessage){

        try
        {
            List<com.inventrax_pepsi.sfa.pojos.Customer> customers=new ArrayList<>();

            com.inventrax_pepsi.sfa.pojos.Customer mCustomer= gson.fromJson(jsonMessage.getJsonMessage(), com.inventrax_pepsi.sfa.pojos.Customer.class);

            if (mCustomer!=null) {

                customers.add(mCustomer);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_CUSTOMER);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setCustomers(customers);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(customer.getJsonMessageAutoIncId());

                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }


                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CREATE_CUSTOMER),jsonMessage)){

                    customerResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    jsonMessage = tableJSONMessage.getJSONMessage(jsonMessage.getAutoIncId());

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    customerResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerResultReceiver.send(STATUS_ERROR,bundle);
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

                        List<com.inventrax_pepsi.sfa.pojos.Customer> customers=rootObject.getCustomers();

                        if ( customers != null && customers.size() > 0 ){

                            com.inventrax_pepsi.sfa.pojos.Customer customer=customers.get(0);

                            if ( customer != null ) {

                               // JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mCustomer.getJsonMessageAutoIncId());

                                Customer mCustomer=tableCustomer.getCustomerByAutoIncId(jsonMessage.getAutoIncId());

                                customer.setAutoIncId(mCustomer.getAutoIncId());
                                mCustomer.setCustomerId(customer.getCustomerId());
                                mCustomer.setCompleteJSON(gson.toJson(customer));

                                tableCustomer.updateCustomer(mCustomer);

                                if (jsonMessage!=null){

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                Intent outletSyncBroadcastIntent=new Intent();
                                outletSyncBroadcastIntent.setAction("com.inventrax.broadcast.outletlistsyncreceiver");
                                sendBroadcast(outletSyncBroadcastIntent);

                                status=true;

                            }

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                customerResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

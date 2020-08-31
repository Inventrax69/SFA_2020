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
import com.inventrax_pepsi.database.TableCustomerTrans;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.CustomerTrans;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.CustomerTransaction;
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
public class CustomerTransIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = CustomerTransIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver customerTransResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableCustomerTrans tableCustomerTrans;
    private TableJSONMessage tableJSONMessage;

    public CustomerTransIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableCustomerTrans = databaseHelper.getTableCustomerTrans();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();
            customerTransResultReceiver=intent.getParcelableExtra("CustomerTransResultReceiver");

            int customerId= intent.getExtras().getInt("CustomerId");

            if (customerId!=0){
                processRequestFromUser(customerId);
            }else{
                processRequestFromListener();
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerTransResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }


    private void processRequestFromUser(int customerId){

        try{

            CustomerTrans customerTrans=tableCustomerTrans.getCustomerTrans(customerId);


            if (customerTrans!=null){

                customerTransResultReceiver.send(STATUS_RUNNING, bundle);

                //sendCustomerReturn(customerReturn);
                processRequestFromListener();

            }else {

                customerTransResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerTransResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 11 -> Customer Trans Notifications
            // NotificationId -> CustomerId

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.CustomerTrans.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                CustomerTrans customerTrans = tableCustomerTrans.getCustomerTrans(jsonMessage.getNotificationId());

                if (customerTrans!=null){


                    customerTransResultReceiver.send(STATUS_RUNNING, bundle);

                    sendCustomerTrans(customerTrans,jsonMessage);

                }else {
                    bundle.putString(MESSAGE,"Invalid Invoice Number");
                    customerTransResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendCustomerTrans(CustomerTrans mCustomerTrans,JSONMessage jsonMessage){

        try
        {
            List<CustomerTransaction> customerTransactions=new ArrayList<>();

            CustomerTransaction customerTrans= gson.fromJson(jsonMessage.getJsonMessage(), CustomerTransaction.class);

            if (customerTrans!=null) {

                customerTransactions.add(customerTrans);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.SAVE_CUSTOMER_TRANSACTION);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setCustomerTransactions(customerTransactions);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);


                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }


                if(processCustomerTransResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_SAVE_CUSTOMER_TRANSACTION), mCustomerTrans,jsonMessage)){

                    customerTransResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    customerTransResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerTransResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean processCustomerTransResponse(String jsonResponse,CustomerTrans mCustomerTrans,JSONMessage jsonMessage){

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

                        List<com.inventrax_pepsi.sfa.pojos.CustomerTransaction> customerTransactions=rootObject.getCustomerTransactions();

                        if (customerTransactions!=null && customerTransactions.size()>0){

                            com.inventrax_pepsi.sfa.pojos.CustomerTransaction customerTransaction=customerTransactions.get(0);

                            if (customerTransaction!=null) {

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mCustomerReturn.getJsonMessageAutoIncId());

                                CustomerTrans localCustomerTrans=new CustomerTrans();

                                localCustomerTrans.setJson(gson.toJson(customerTransaction));
                                localCustomerTrans.setCustomerId(customerTransaction.getCustomerId());
                                localCustomerTrans.setAmount(customerTransaction.getAmount());
                                localCustomerTrans.setAutoIncId(mCustomerTrans.getAutoIncId());
                                localCustomerTrans.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableCustomerTrans.updateCustomerTrans(localCustomerTrans);

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
                customerTransResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerTransResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

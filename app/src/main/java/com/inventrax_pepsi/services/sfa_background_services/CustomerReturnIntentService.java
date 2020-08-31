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
import com.inventrax_pepsi.database.TableCustomerReturn;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.CustomerReturn;
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
public class CustomerReturnIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = CustomerReturnIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver customerReturnResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableCustomerReturn tableCustomerReturn;
    private TableJSONMessage tableJSONMessage;

    public CustomerReturnIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableCustomerReturn=databaseHelper.getTableCustomerReturn();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();
            customerReturnResultReceiver=intent.getParcelableExtra("CustomerReturnResultReceiver");

            int customerId= intent.getExtras().getInt("CustomerId");

            if (customerId!=0){
                processRequestFromUser(customerId);
            }else{
                processRequestFromListener();
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerReturnResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }




    private void processRequestFromUser(int customerId){

        try{

            CustomerReturn customerReturn=tableCustomerReturn.getCustomerReturn(customerId);


            if (customerReturn!=null){

                customerReturnResultReceiver.send(STATUS_RUNNING, bundle);

                //sendCustomerReturn(customerReturn);
                processRequestFromListener();

            }else {

                customerReturnResultReceiver.send(STATUS_ERROR, bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerReturnResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 4 -> Customer Return Notifications
            // NotificationId -> CustomerId

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, JsonMessageNotificationType.CustomerReturn.getNotificationType());

            for (JSONMessage jsonMessage:jsonMessageList){

                CustomerReturn customerReturn = tableCustomerReturn.getCustomerReturn(jsonMessage.getNotificationId());

                if (customerReturn!=null){


                    customerReturnResultReceiver.send(STATUS_RUNNING, bundle);

                    sendCustomerReturn(customerReturn,jsonMessage);

                }else {
                    bundle.putString(MESSAGE,"Invalid Invoice Number");
                    customerReturnResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendCustomerReturn(CustomerReturn mCustomerReturn,JSONMessage jsonMessage){

        try
        {
            List<com.inventrax_pepsi.sfa.pojos.CustomerReturn> customerReturns=new ArrayList<>();

            com.inventrax_pepsi.sfa.pojos.CustomerReturn customerReturn= gson.fromJson(jsonMessage.getJsonMessage(), com.inventrax_pepsi.sfa.pojos.CustomerReturn.class);

            if (customerReturn!=null) {

                customerReturns.add(customerReturn);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.CREATE_CUSTOMER_RETURNS);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setCustomerReturns(customerReturns);


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


                if(processCustomerReturnResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CUSTOMER_RETURNS), mCustomerReturn,jsonMessage)){

                    customerReturnResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {

                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    customerReturnResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerReturnResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean processCustomerReturnResponse(String jsonResponse,CustomerReturn mCustomerReturn,JSONMessage jsonMessage){

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

                        List<com.inventrax_pepsi.sfa.pojos.CustomerReturn> customerReturns=rootObject.getCustomerReturns();

                        if (customerReturns!=null && customerReturns.size()>0){

                            com.inventrax_pepsi.sfa.pojos.CustomerReturn customerReturn=customerReturns.get(0);

                            if (customerReturn!=null) {

                                //JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(mCustomerReturn.getJsonMessageAutoIncId());

                                CustomerReturn localCustomerReturn=new CustomerReturn();

                                localCustomerReturn.setJson(gson.toJson(customerReturn));
                                localCustomerReturn.setCustomerId(customerReturn.getCustomerId());
                                localCustomerReturn.setRouteId(customerReturn.getRouteId());
                                localCustomerReturn.setAutoIncId(mCustomerReturn.getAutoIncId());
                                localCustomerReturn.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableCustomerReturn.updateCustomerReturn(localCustomerReturn);

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
                customerReturnResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerReturnResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

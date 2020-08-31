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
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Invoice;
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
public class InvoiceIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = InvoiceIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver invoiceResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableInvoice tableInvoice;
    private TableJSONMessage tableJSONMessage;

    public InvoiceIntentService() {
        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableInvoice=databaseHelper.getTableInvoice();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();
            invoiceResultReceiver=intent.getParcelableExtra("InvoiceResultReceiver");

            processRequestFromListener();

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            invoiceResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 2 -> Invoice Type Notifications
            // NotificationId -> InvoiceId

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, 2);

            for (JSONMessage jsonMessage:jsonMessageList){

                if (jsonMessage!=null){

                    bundle.putString("InvoiceNumber","");
                    invoiceResultReceiver.send(STATUS_RUNNING, bundle);

                    sendInvoice(jsonMessage);

                }else {
                    bundle.putString(MESSAGE,"Invalid Invoice Number");
                    invoiceResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendInvoice(JSONMessage jsonMessage){

        try
        {
            List<Invoice> invoiceList=new ArrayList<>();
            Invoice invoice= gson.fromJson(jsonMessage.getJsonMessage(), Invoice.class);

            if (invoice!=null) {

                invoiceList.add(invoice);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.UPDATE_BILL_STATUS);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setInvoices(invoiceList);


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


                if(processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_UPDATE_INVOICE),jsonMessage)){

                    bundle.putString("InvoiceNumber",invoice.getInvoiceNo());
                    invoiceResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    if (jsonMessage!=null && jsonMessage.getNoOfRequests()>=5)
                    {
                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    bundle.putString("InvoiceNumber",invoice.getInvoiceNo());
                    invoiceResultReceiver.send(STATUS_ERROR,bundle);
                }


            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            bundle.putString("InvoiceNumber","");
            invoiceResultReceiver.send(STATUS_ERROR,bundle);
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

                        List<Invoice> invoiceList=rootObject.getInvoices();

                        if (invoiceList!=null && invoiceList.size()>0){

                            Invoice invoice=invoiceList.get(0);

                            if (invoice!=null) {

                                DeliveryInvoice deliveryInvoice=new DeliveryInvoice();

                                deliveryInvoice.setInvoiceJSON(gson.toJson(invoice));
                                deliveryInvoice.setInvoiceId(invoice.getInvoiceId());
                                deliveryInvoice.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());

                                tableInvoice.updateInvoiceJSON(deliveryInvoice);

                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                status = true;

                                Intent orderSyncBroadcastIntent=new Intent();
                                orderSyncBroadcastIntent.setAction("com.inventrax.broadcast.deliverylistsyncreceiver");
                                sendBroadcast(orderSyncBroadcastIntent);


                            }

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                invoiceResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            invoiceResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

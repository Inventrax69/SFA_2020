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
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Order;
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
public class OrderIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = OrderIntentService.class.getName();
    private static final String MESSAGE="message";
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver orderResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableJSONMessage tableJSONMessage;


    public OrderIntentService() {
        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableOrder=databaseHelper.getTableOrder();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            bundle = new Bundle();
            orderResultReceiver=intent.getParcelableExtra("OrderResultReceiver");

            processRequestFromListener();


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            orderResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }


    private void processRequestFromListener(){

        try
        {
            // 0 -> Un Synced records
            // 1 -> Order Type Notifications
            // NotificationId -> Auto_Inc_Id

            List<JSONMessage> jsonMessageList= tableJSONMessage.getAllJSONMessages(0, 1);

            for (JSONMessage jsonMessage:jsonMessageList){

                if (jsonMessage!=null){

                    bundle.putString("OrderNumber","");
                    orderResultReceiver.send(STATUS_RUNNING, bundle);

                    sendOrder(jsonMessage);

                }else {
                    bundle.putString(MESSAGE,"Invalid Order Number");
                    orderResultReceiver.send(STATUS_ERROR, bundle);
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            return;
        }

    }

    private void sendOrder(JSONMessage jsonMessage){

        try
        {
            List<Order> orderList=new ArrayList<>();
            Order order= gson.fromJson(jsonMessage.getJsonMessage(), Order.class);

            if (order!=null) {

                orderList.add(order);

                List<User> userList = new ArrayList<>();
                User user = AppController.getUser();
                userList.add(user);

                RootObject rootObject = new RootObject();

                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
                rootObject.setOrders(orderList);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setType(String.class);

                boolean status=false;


                if (jsonMessage!=null){

                    jsonMessage.setNoOfRequests(jsonMessage.getNoOfRequests()+1);

                    tableJSONMessage.updateJSONMessage(jsonMessage);

                }

                // For Pre-Sale
                if(order.getOrderStatusId() >= OrderStatus.InTransit.getStatus()){

                    rootObject.setServiceCode(ServiceCode.UPDATE_ORDER_STATUS);

                    propertyInfo.setValue(gson.toJson(rootObject));
                    propertyInfoList.add(propertyInfo);

                    status=processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_UPDATE_ORDER),jsonMessage);

                }else {

                    // For Ready-Sale

                    rootObject.setServiceCode(ServiceCode.CREATE_ORDER);

                    propertyInfo.setValue(gson.toJson(rootObject));
                    propertyInfoList.add(propertyInfo);

                    status=processOrderResponse(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CREATE_ORDER),jsonMessage);
                }

                if(status){

                    bundle.putString("OrderNumber",order.getOrderCode());
                    orderResultReceiver.send(STATUS_FINISHED,bundle);

                }else
                {
                    if (jsonMessage != null && jsonMessage.getNoOfRequests() >= 5  ) {

                        jsonMessage.setSyncStatus(1);

                        tableJSONMessage.updateJSONMessage(jsonMessage);
                    }

                    bundle.putString("OrderNumber",order.getOrderCode());
                    orderResultReceiver.send(STATUS_ERROR,bundle);
                }


            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            bundle.putString("OrderNumber","");
            orderResultReceiver.send(STATUS_ERROR,bundle);
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

                        List<Order> orderList=rootObject.getOrders();

                        if (orderList!=null && orderList.size()>0){

                            Order order=orderList.get(0);

                            if (order!=null) {

                                com.inventrax_pepsi.database.pojos.Order localDBOrder=new com.inventrax_pepsi.database.pojos.Order();

                                localDBOrder.setOrderJSON(gson.toJson(order));
                                localDBOrder.setCustomerId(order.getCustomerId());
                                localDBOrder.setOrderCode(order.getOrderCode());
                                localDBOrder.setJsonMessageAutoIncId(jsonMessage.getAutoIncId());
                                localDBOrder.setCustomerCode(order.getCustomerCode());
                                localDBOrder.setOrderPrice(order.getOrderPrice());
                                localDBOrder.setDerivedPrice(order.getDerivedPrice());
                                localDBOrder.setOrderType(order.getOrderTypeId());
                                localDBOrder.setOrderStatus(order.getOrderStatusId());
                                localDBOrder.setOrderQuantity(order.getOrderQuantity());
                                localDBOrder.setRouteId(order.getRouteId());
                                localDBOrder.setAutoIncId(jsonMessage.getNotificationId());
                                localDBOrder.setOrderId(order.getOrderId());
                                localDBOrder.setRouteCode(tableOrder.getOrder(order.getOrderCode()).getRouteCode());

                                tableOrder.updateOrder(localDBOrder);


                                if (jsonMessage != null) {

                                    jsonMessage.setSyncStatus(1);

                                    tableJSONMessage.updateJSONMessage(jsonMessage);
                                }

                                status = true;

                                Intent counterBroadcastIntent=new Intent();
                                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                                sendBroadcast(counterBroadcastIntent);


                                Intent orderSyncBroadcastIntent=new Intent();
                                orderSyncBroadcastIntent.setAction("com.inventrax.broadcast.orderhistorysyncreceiver");
                                sendBroadcast(orderSyncBroadcastIntent);


                            }

                        }

                    }
                }


            }else {
                bundle.putString(MESSAGE,"Invalid Response");
                orderResultReceiver.send(STATUS_ERROR,bundle);
            }



        }catch (Exception ex){
            Logger.Log(TAG, ex);
            orderResultReceiver.send(STATUS_ERROR,bundle);
            return  status;
        }

        return  status;
    }



}

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
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.Route;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/5/2016.
 */
public class TodayOrdersIntentService extends IntentService  {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = TodayOrdersIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver todayOrdersResultReceiver;

    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableJSONMessage tableJSONMessage;

    public TodayOrdersIntentService(){

        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableOrder = databaseHelper.getTableOrder();
        tableJSONMessage=databaseHelper.getTableJSONMessage();
        gson=new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            todayOrdersResultReceiver = intent.getParcelableExtra("TodayOrdersResultReceiver");

            bundle = new Bundle();

            todayOrdersResultReceiver.send(STATUS_RUNNING, bundle);

            User user=AppController.getUser();
            Route route=null;
            List<User> userList=new ArrayList<>();
            userList.add(user);

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_ORDERS_BY_USERS);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);

                getTodayOrdersList(gson.toJson(rootObject));


            }else {
                todayOrdersResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){

            Logger.Log(TAG, ex);
            todayOrdersResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }

    private void getTodayOrdersList(String inputJSON){

        try
        {
            todayOrdersResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveTodayOrdersList(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_ORDERS_BY_USERS))){

                todayOrdersResultReceiver.send(STATUS_FINISHED,bundle);

            }else
            {
                todayOrdersResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){

            Logger.Log(TAG, ex);
            todayOrdersResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }

    private boolean saveTodayOrdersList(String responseJSON){

        boolean status=false;

        try
        {
            if (!TextUtils.isEmpty(responseJSON)){

                todayOrdersResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse = null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse != null) {

                    if (executionResponse.getSuccess() == 1) {

                        List<Order> orderList = rootObject.getOrders();

                        OrderUtil orderUtil=new OrderUtil();

                        if (orderList != null && orderList.size() > 0) {

                            tableOrder.deleteAllOrders();

                            com.inventrax_pepsi.database.pojos.Order localOrder = null;

                            for (Order order : orderList) {

                                orderUtil.getOrderWithFrees(order);

                                localOrder=new com.inventrax_pepsi.database.pojos.Order();

                                localOrder.setCustomerCode(order.getCustomerCode());
                                localOrder.setRouteCode(order.getRouteCode());
                                localOrder.setRouteId(order.getRouteId());
                                localOrder.setDerivedPrice(order.getDerivedPrice());
                                localOrder.setCustomerId(order.getCustomerId());
                                localOrder.setOrderCode(order.getOrderCode());
                                localOrder.setOrderId(order.getOrderId());
                                localOrder.setOrderJSON(gson.toJson(order));
                                localOrder.setOrderPrice(order.getOrderPrice());
                                localOrder.setOrderStatus(order.getOrderStatusId());
                                localOrder.setOrderType(order.getOrderTypeId());
                                localOrder.setOrderQuantity(order.getOrderQuantity());
                                localOrder.setPaymentMode(order.getPaymentInfo()==null?0: order.getPaymentInfo().getPaymentTypeId());


                                long order_auto_inc_id = tableOrder.createOrder(localOrder);


                                if (order_auto_inc_id != 0) {

                                    JSONMessage jsonMessage = new JSONMessage();
                                    jsonMessage.setJsonMessage(localOrder.getOrderJSON());
                                    jsonMessage.setNoOfRequests(0);
                                    jsonMessage.setSyncStatus(1);
                                    jsonMessage.setNotificationId((int) order_auto_inc_id);
                                    jsonMessage.setNotificationTypeId(1); // For Order Notification Type Id is 1

                                    long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                                    if (json_message_auto_inc_id != 0) {

                                        localOrder.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                                        localOrder.setAutoIncId((int) order_auto_inc_id);

                                        tableOrder.updateOrder(localOrder);

                                    }

                                }



                            }

                            Intent counterBroadcastIntent=new Intent();
                            counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                            sendBroadcast(counterBroadcastIntent);


                        }

                        status = true;
                    }
                }
            }

        }catch (Exception ex){
            Logger.Log(TAG,ex);
            todayOrdersResultReceiver.send(STATUS_ERROR,bundle);
            return false;

        }

        return status;
    }



}

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
import com.inventrax_pepsi.database.TableCustomerOrderHistory;
import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;
import com.inventrax_pepsi.sfa.pojos.CustSKUOrder;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.Route;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/5/2016.
 */
public class CustomerOrderHistoryIntentService extends IntentService {


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = CustomerOrderHistoryIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver customerOrderHistoryResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableCustomerOrderHistory tableCustomerOrderHistory;

    public CustomerOrderHistoryIntentService(){

        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableCustomerOrderHistory=databaseHelper.getTableCustomerOrderHistory();

        gson=new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            customerOrderHistoryResultReceiver=intent.getParcelableExtra("CustomerOrderHistoryResultReceiver");

            bundle = new Bundle();

            customerOrderHistoryResultReceiver.send(STATUS_RUNNING, bundle);

            User user=AppController.getUser();
            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_CUSTOMER_ORDER_HISTORY);
                rootObject.setLoginInfo(AppController.getLoginInfo());

                if (user.getRouteList()!=null)
                for (RouteList routeList : user.getRouteList()) {

                    route = new Route();

                    route.setRouteCode(routeList.getRouteCode());
                    route.setRouteId(routeList.getRouteId());
                    route.setAuditInfo(user.getAuditInfo());

                    userRouteList.add(route);
                }

                rootObject.setRoutes(userRouteList);

                getCustomerOrderHistoryList(gson.toJson(rootObject));


            }else {
                customerOrderHistoryResultReceiver.send(STATUS_ERROR,bundle);
            }


        }catch (Exception ex){

            Logger.Log(TAG, ex);
            customerOrderHistoryResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }


    private void getCustomerOrderHistoryList(String inputJSON){

        try
        {

            customerOrderHistoryResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveCustomerOrderHistory(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CUSTOMER_ORDER_HISTORY))){

                customerOrderHistoryResultReceiver.send(STATUS_FINISHED,bundle);

            }else
            {
                customerOrderHistoryResultReceiver.send(STATUS_ERROR,bundle);
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerOrderHistoryResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean saveCustomerOrderHistory(String responseJSON){

        boolean status=false;

        try
        {

            if (!TextUtils.isEmpty(responseJSON)){

                customerOrderHistoryResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse=null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse!=null) {

                    if (executionResponse.getSuccess() == 1) {

                        tableCustomerOrderHistory.deleteAllCustomerHistories();

                        List<CustSKUOrder> custSKUOrderList = rootObject.getCustSKUOrders();

                        if (custSKUOrderList != null && custSKUOrderList.size() > 0) {

                            CustomerOrderHistory customerOrderHistory=null;

                            for (CustSKUOrder custSKUOrder:custSKUOrderList){

                                customerOrderHistory=new CustomerOrderHistory();
                                customerOrderHistory.setCustomerId(custSKUOrder.getCustomerId());
                                customerOrderHistory.setCustomerCode(custSKUOrder.getCustomerCode());
                                customerOrderHistory.setCustomerName(custSKUOrder.getCustomerName());
                                customerOrderHistory.setRouteCode(custSKUOrder.getRouteCode());
                                customerOrderHistory.setRouteId(custSKUOrder.getRouteId());
                                customerOrderHistory.setBrandPackJSON(gson.toJson(custSKUOrder));
                                customerOrderHistory.setBrandJSON(gson.toJson(custSKUOrder));

                                tableCustomerOrderHistory.createCustomerOrderHistory(customerOrderHistory);

                            }


                        }

                        status = true;

                    }
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            customerOrderHistoryResultReceiver.send(STATUS_ERROR,bundle);
            return status;
        }

        return status;
    }


}

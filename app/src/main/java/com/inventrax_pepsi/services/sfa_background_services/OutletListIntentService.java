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
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
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
public class OutletListIntentService extends IntentService {


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = OutletListIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver outletListResultReceiver;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;

    public OutletListIntentService(){

        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableCustomer=databaseHelper.getTableCustomer();
        gson=new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {

            outletListResultReceiver=intent.getParcelableExtra("OutletListResultReceiver");

            bundle = new Bundle();

            outletListResultReceiver.send(STATUS_RUNNING, bundle);

            User user=AppController.getUser();
            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_CUSTOMERS_BY_ROUTE);
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

                getOutletList(gson.toJson(rootObject));


            }else {
                outletListResultReceiver.send(STATUS_ERROR,bundle);
            }


        }catch (Exception ex){

            Logger.Log(TAG, ex);
            outletListResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }


    private void getOutletList(String inputJSON){

        try
        {

            outletListResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveOutletList(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_CUSTOMER_LIST))){

                outletListResultReceiver.send(STATUS_FINISHED,bundle);

                Intent counterBroadcastIntent=new Intent();
                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                sendBroadcast(counterBroadcastIntent);


            }else
            {
                outletListResultReceiver.send(STATUS_ERROR,bundle);
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            outletListResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean saveOutletList(String responseJSON){

        boolean status=false;

        try
        {

            if (!TextUtils.isEmpty(responseJSON)){

                outletListResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse=null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse!=null) {

                    if (executionResponse.getSuccess() == 1) {

                        List<Customer> customerList = rootObject.getCustomers();

                        if (customerList != null && customerList.size() > 0) {

                            tableCustomer.deleteAllCustomers();

                            com.inventrax_pepsi.database.pojos.Customer localDBCustomer = null;
                            OutletProfile outletProfile = null;

                            for (Customer customer : customerList) {

                                localDBCustomer = new com.inventrax_pepsi.database.pojos.Customer();

                                outletProfile = customer.getOutletProfile();

                                localDBCustomer.setCustomerId(customer.getCustomerId());
                                localDBCustomer.setCustomerCode(customer.getCustomerCode());

                                if (outletProfile != null) {
                                    localDBCustomer.setRouteId(outletProfile.getRouteId());
                                    localDBCustomer.setRouteNo(outletProfile.getRouteCode());
                                    localDBCustomer.setIsScheduledOutlet(( outletProfile.isScheduledOutlet()==true) ? 1 : 0 );
                                }

                                localDBCustomer.setCustomerName(customer.getCustomerName());
                                localDBCustomer.setCustomerType(customer.getCustomerGroup());
                                localDBCustomer.setCustomerTypeId(customer.getCustomerGroupId());
                                localDBCustomer.setOrderCap(customer.getOrderCap());
                                localDBCustomer.setSyncStatus(0);

                                localDBCustomer.setCompleteJSON(gson.toJson(customer));
                                //localDBCustomer.setDiscountJSON(gson.toJson(customer.getDiscounts()));
                                localDBCustomer.setCouponJSON(gson.toJson(customer.getCoupons()));
                                //localDBCustomer.setPriceJSON(gson.toJson(customer.getItemPrices()));


                                tableCustomer.createCustomer(localDBCustomer);

                            }

                            status = true;

                        }

                    }
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            outletListResultReceiver.send(STATUS_ERROR,bundle);
            return status;
        }

        return status;
    }


}

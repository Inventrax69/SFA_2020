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
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Invoice;
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
public class DeliveryListIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = DeliveryListIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver deliveryListResultReceiver;


    private DatabaseHelper databaseHelper;
    private TableInvoice tableInvoice;

    public DeliveryListIntentService() {

        super(TAG);

        databaseHelper = DatabaseHelper.getInstance();
        tableInvoice=databaseHelper.getTableInvoice();

        gson = new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            deliveryListResultReceiver = intent.getParcelableExtra("DeliveryListResultReceiver");
            bundle = new Bundle();
            deliveryListResultReceiver.send(STATUS_RUNNING, bundle);


            User user= AppController.getUser();
            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_DELIVERY_ORDERS_BY_ROUTES);
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

                getDeliveryList(gson.toJson(rootObject));

            }else {

                deliveryListResultReceiver.send(STATUS_ERROR,bundle);

            }

        } catch (Exception ex) {
            Logger.Log(TAG, ex);
            deliveryListResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }
    }

    private void getDeliveryList(String inputJSON) {

        try
        {

            deliveryListResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveDeliveryList(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_DELIVERY_LIST))){

                deliveryListResultReceiver.send(STATUS_FINISHED, bundle);

                Intent counterBroadcastIntent=new Intent();
                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                sendBroadcast(counterBroadcastIntent);

            }else
            {
                deliveryListResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            deliveryListResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }


    }

    private boolean saveDeliveryList(String responseJSON) {

        boolean status=false;

        try
        {

            if (!TextUtils.isEmpty(responseJSON)){

                deliveryListResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse=null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse!=null) {

                    if (executionResponse.getSuccess() == 1) {

                        tableInvoice.deleteAllInvoices();

                        List<Invoice> invoices= rootObject.getInvoices();

                        DeliveryInvoice deliveryInvoice=null;

                        if (invoices!=null && invoices.size()>0){

                            for (Invoice invoice:invoices){

                                deliveryInvoice=new DeliveryInvoice();

                                deliveryInvoice.setInvoiceJSON(gson.toJson(invoice));
                                deliveryInvoice.setInvoiceId(invoice.getInvoiceId());
                                deliveryInvoice.setCustomerId(invoice.getCustomerId());
                                deliveryInvoice.setRouteId(invoice.getRouteId());
                                deliveryInvoice.setRouteCode(invoice.getRouteCode());
                                deliveryInvoice.setInvoiceNumber(invoice.getInvoiceNo());
                                deliveryInvoice.setOrderId(invoice.getInvoiceOrders().get(0).getOrderId());
                                deliveryInvoice.setOrderNumber(invoice.getInvoiceOrders().get(0).getOrderCode());
                                deliveryInvoice.setCustomerCode(invoice.getCustomerCode());
                                deliveryInvoice.setCustomerName(invoice.getCustomerName());
                                deliveryInvoice.setInvoiceAmount(invoice.getNetAmount());

                                tableInvoice.createInvoice(deliveryInvoice);

                            }

                        }

                        status = true;

                    }

                }

            }

        }
        catch (Exception ex){

            Logger.Log(TAG, ex);
            deliveryListResultReceiver.send(STATUS_ERROR,bundle);
            return status;

        }

        return status;

    }


}

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
import com.inventrax_pepsi.database.TableCustomerDiscount;
import com.inventrax_pepsi.database.TableCustomerPrice;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.database.pojos.CustomerPrice;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.Route;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeTargetItem;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/5/2016.
 */
public class DiscountListIntentService extends IntentService {


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = DiscountListIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver discountListResultReceiver;


    private DatabaseHelper databaseHelper;
    private TableScheme tableScheme;
    private TableCustomerDiscount tableCustomerDiscount;
    private TableCustomerPrice tableCustomerPrice;

    public DiscountListIntentService() {

        super(TAG);
        databaseHelper = DatabaseHelper.getInstance();

        tableScheme=databaseHelper.getTableScheme();
        tableCustomerDiscount=databaseHelper.getTableCustomerDiscount();
        tableCustomerPrice=databaseHelper.getTableCustomerPrice();

        gson = new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            discountListResultReceiver = intent.getParcelableExtra("DiscountListResultReceiver");
            bundle = new Bundle();
            discountListResultReceiver.send(STATUS_RUNNING, bundle);


            User user= AppController.getUser();
            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_DISCOUNTS_BY_ROUTES);
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

                getDiscountList(gson.toJson(rootObject));

            }else {

                discountListResultReceiver.send(STATUS_ERROR,bundle);

            }


        } catch (Exception ex) {
            Logger.Log(TAG, ex);
            discountListResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }
    }

    private void getDiscountList(String inputJSON) {

        try
        {

            discountListResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveDiscountList(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_DISCOUNT_LIST))){

                discountListResultReceiver.send(STATUS_FINISHED, bundle);

                Intent counterBroadcastIntent=new Intent();
                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                sendBroadcast(counterBroadcastIntent);

            }else
            {
                discountListResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){
            Logger.Log(TAG, ex);
            discountListResultReceiver.send(STATUS_ERROR,bundle);
            return;
        }

    }

    private boolean saveDiscountList(String responseJSON) {

        boolean status=false;

        try
        {

            if (!TextUtils.isEmpty(responseJSON)){

                discountListResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse=null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse!=null) {

                    if (executionResponse.getSuccess() == 1) {


                        tableScheme.deleteAllSchemes();
                        tableCustomerDiscount.deleteAllCustomerDiscounts();
                        tableCustomerPrice.deleteAllCustomerPrices();

                        // Scheme Master

                        List<Scheme> schemeList=rootObject.getSchemes();

                        com.inventrax_pepsi.database.pojos.Scheme localDBScheme=null;

                        for (Scheme scheme:schemeList){

                            for (SchemeTargetItem schemeTargetItem:scheme.getSchemeTargetItems()){

                                localDBScheme=new com.inventrax_pepsi.database.pojos.Scheme();

                                localDBScheme.setSchemeName(scheme.getSchemeName());
                                localDBScheme.setSchemeCode(scheme.getSchemeCode());
                                localDBScheme.setSchemeId(scheme.getSchemeId());

                                localDBScheme.setUomCode(schemeTargetItem.getUoMCode());
                                localDBScheme.setUomId(schemeTargetItem.getUoMId());
                                localDBScheme.setItemCode(schemeTargetItem.getItemCode());
                                localDBScheme.setItemId(schemeTargetItem.getItemId());
                                localDBScheme.setQuantity(schemeTargetItem.getQuantity());

                                localDBScheme.setSchemeJSON(gson.toJson(scheme));

                                tableScheme.createScheme(localDBScheme);
                            }

                        }

                        // Customer Discounts

                        List<CustomerDiscount> customerDiscountList=rootObject.getCustomerDiscounts();

                        com.inventrax_pepsi.database.pojos.CustomerDiscount localCustomerDiscount=null;

                        for (CustomerDiscount customerDiscount:customerDiscountList){

                            localCustomerDiscount=new com.inventrax_pepsi.database.pojos.CustomerDiscount();

                            localCustomerDiscount.setDiscountJSON(gson.toJson(customerDiscount));
                            localCustomerDiscount.setUomCode(customerDiscount.getTargetUoMCode());
                            localCustomerDiscount.setUomId(customerDiscount.getTargetUoMId());
                            localCustomerDiscount.setItemCode(customerDiscount.getTargetItemCode());
                            localCustomerDiscount.setItemId(customerDiscount.getTargetItemId());
                            localCustomerDiscount.setCustomerId(customerDiscount.getCustomerId());
                            localCustomerDiscount.setQuantity(customerDiscount.getTargetItemQty());
                            localCustomerDiscount.setDiscountId(customerDiscount.getDiscountId());


                            tableCustomerDiscount.createCustomerDiscount(localCustomerDiscount);

                        }

                        // Customer Prices

                        List<Item> itemList=rootObject.getItems();

                        if (itemList != null && itemList.size() > 0) {

                            CustomerPrice customerPrice = null;

                            for (Item item : itemList) {

                                List<ItemUoM> itemUoMList = item.getItemUoMs();

                                for (ItemUoM itemUoM : itemUoMList) {

                                    List<ItemPrice> itemPriceList = item.getItemPrices();

                                    for (ItemPrice itemPrice : itemPriceList) {

                                        if (itemUoM.getItemId() == itemPrice.getItemId() && itemUoM.getUoMId() == itemPrice.getUoMId()) {

                                            customerPrice = new CustomerPrice();

                                            customerPrice.setMRP(itemPrice.getmRP());
                                            customerPrice.setCustomerId(itemPrice.getCustomerId());
                                            customerPrice.setUomCode(itemUoM.getUomCode());
                                            customerPrice.setUomId(itemUoM.getUoMId());
                                            customerPrice.setItemId(itemPrice.getItemId());
                                            customerPrice.setItemCode(itemPrice.getItemCode());

                                            if (itemPrice.getTradePrice() != 0) {
                                                customerPrice.setTradePrice(itemPrice.getTradePrice());
                                                customerPrice.setIsTrade(1);
                                            }

                                            customerPrice.setJSON(gson.toJson(itemPrice));

                                            tableCustomerPrice.createCustomerPrice(customerPrice);

                                        }
                                    }
                                }

                            }

                        }

                        status = true;

                    }

                }
            }
        }
        catch (Exception ex){
            Logger.Log(TAG, ex);
            discountListResultReceiver.send(STATUS_ERROR,bundle);
            return status;
        }

        return status;
    }

}

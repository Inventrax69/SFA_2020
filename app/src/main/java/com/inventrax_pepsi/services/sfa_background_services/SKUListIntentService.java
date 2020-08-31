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
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
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
public class SKUListIntentService extends IntentService  {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = SKUListIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver skuListResultReceiver;

    private DatabaseHelper databaseHelper;
    private TableItem tableItem;

    public SKUListIntentService(){

        super(TAG);
        databaseHelper=DatabaseHelper.getInstance();
        tableItem=databaseHelper.getTableItem();
        gson=new GsonBuilder().create();

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            skuListResultReceiver = intent.getParcelableExtra("SKUListResultReceiver");

            bundle = new Bundle();

            skuListResultReceiver.send(STATUS_RUNNING, bundle);

            User user=AppController.getUser();
            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            if (user!=null) {

                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_ITEMS_WITH_PRICE_BY_TERRITORY);
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

                getSKUList(gson.toJson(rootObject));


            }else {
                skuListResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){

            Logger.Log(TAG, ex);
            skuListResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }

    private void getSKUList(String inputJSON){

        try
        {
            skuListResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveSKUList(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_SKU_LIST))){

                skuListResultReceiver.send(STATUS_FINISHED,bundle);

            }else
            {
                skuListResultReceiver.send(STATUS_ERROR,bundle);
            }

        }catch (Exception ex){

            Logger.Log(TAG, ex);
            skuListResultReceiver.send(STATUS_ERROR,bundle);
            return;

        }

    }

    private boolean saveSKUList(String responseJSON){

        boolean status=false;

        try
        {
            if (!TextUtils.isEmpty(responseJSON)){

                skuListResultReceiver.send(STATUS_RUNNING,bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse = null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse != null) {

                    if (executionResponse.getSuccess() == 1) {

                        List<Item> itemList = rootObject.getItems();

                        if (itemList != null && itemList.size() > 0) {

                            tableItem.deleteAllItems();

                            com.inventrax_pepsi.database.pojos.Item localDBItem = null;

                            for (Item item : itemList) {

                                localDBItem = new com.inventrax_pepsi.database.pojos.Item();

                                ItemUoM itemUoM=item.getItemUoMs().get(0);

                                List<ItemPrice> itemPriceList=item.getItemPrices();

                                for (ItemPrice itemPrice:itemPriceList){

                                    if (itemUoM.getItemId() == itemPrice.getItemId() && itemUoM.getUoMId() == itemPrice.getUoMId() )
                                    {
                                        localDBItem.setItemUoM(itemUoM.getUoM());
                                        localDBItem.setItemUoMId(itemUoM.getUoMId());
                                        localDBItem.setItemUoMQuantity(itemUoM.getUnits());

                                        if (itemPrice.getTradePrice()!=0) {
                                            localDBItem.setTrade_retail(itemPrice.getTradePrice());
                                            localDBItem.setIsTrade(1);
                                        }

                                        localDBItem.setItemMRP(itemPrice.getmRP());


                                        break;
                                    }
                                }

                                localDBItem.setItemId(item.getItemId());
                                localDBItem.setBrandPackName(item.getItemPack());
                                localDBItem.setBrandName(item.getItemBrand());
                                localDBItem.setItemCode(item.getItemCode());
                                localDBItem.setItemJSON(gson.toJson(item));
                                localDBItem.setPriceJSON(gson.toJson(item.getItemPrices()));
                                localDBItem.setUomJSON(gson.toJson(item.getItemUoMs()));
                                localDBItem.setItemType(""+item.getItemTypeId());
                                localDBItem.setIsFMO((item.isFMO()==true?1:0));
                                localDBItem.setPackDisplaySeq(item.getPackDisplaySeq());
                                localDBItem.setBrandDisplaySeq(item.getBrandDisplaySeq());

                                tableItem.createItem(localDBItem);

                            }

                            status = true;
                        }
                    }
                }
            }

        }catch (Exception ex){

            Logger.Log(TAG,ex);
            skuListResultReceiver.send(STATUS_ERROR,bundle);
            return false;

        }

        return status;
    }



}

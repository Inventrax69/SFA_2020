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
import com.inventrax_pepsi.database.TableVehicleLoad;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.VehicleLoad;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Load;
import com.inventrax_pepsi.sfa.pojos.LoadItem;
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
public class LoadOutInfoIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = LoadOutInfoIntentService.class.getName();
    private Gson gson;
    private Bundle bundle;
    private ResultReceiver loadOutInfoResultReceiver;


    private DatabaseHelper databaseHelper;
    private TableVehicleLoad tableVehicleLoad;
    private TableVehicleStock tableVehicleStock;


    public LoadOutInfoIntentService(){

        super(TAG);

        databaseHelper=DatabaseHelper.getInstance();
        tableVehicleLoad=databaseHelper.getTableVehicleLoad();
        tableVehicleStock=databaseHelper.getTableVehicleStock();

        gson=new GsonBuilder().create();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {
            loadOutInfoResultReceiver=intent.getParcelableExtra("LoadOutInfoResultReceiver");

            bundle = new Bundle();

            loadOutInfoResultReceiver.send(STATUS_RUNNING, bundle);

            List<User> userList=new ArrayList<>();

            User user=AppController.getUser();
            userList.add(user);

            List<Route> userRouteList=new ArrayList<>();
            Route route=null;

            RootObject rootObject=new RootObject();

            if (user!=null) {

                if (user.getRouteList()!=null)
                for (RouteList routeList : user.getRouteList()) {

                    route = new Route();

                    route.setRouteCode(routeList.getRouteCode());
                    route.setRouteId(routeList.getRouteId());
                    route.setAuditInfo(user.getAuditInfo());

                    userRouteList.add(route);
                }

                rootObject.setRoutes(userRouteList);

                rootObject.setServiceCode(ServiceCode.GET_LOAD_BY_ROUTES);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setUsers(userList);
            }

            getVanInventory(gson.toJson(rootObject));


        }catch (Exception ex){
            Logger.Log(TAG,ex);
            loadOutInfoResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }
    }

    private void getVanInventory(String inputJSON){

        try
        {
            loadOutInfoResultReceiver.send(STATUS_RUNNING,bundle);

            List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("jsonStr");
            propertyInfo.setValue(inputJSON);
            propertyInfo.setType(String.class);
            propertyInfoList.add(propertyInfo);

            if(saveVanInventory(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_LOAD_OUT_INFO))){

                loadOutInfoResultReceiver.send(STATUS_FINISHED,bundle);

                Intent counterBroadcastIntent=new Intent();
                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                sendBroadcast(counterBroadcastIntent);

            }else
            {
                loadOutInfoResultReceiver.send(STATUS_ERROR,bundle);
            }


        }catch (Exception ex){
            Logger.Log(TAG, ex);
            loadOutInfoResultReceiver.send(STATUS_ERROR, bundle);
            return;
        }

    }

    private boolean saveVanInventory(String responseJSON){

        boolean status=false;

        try
        {

            if (!TextUtils.isEmpty(responseJSON)) {

                loadOutInfoResultReceiver.send(STATUS_RUNNING, bundle);

                JSONObject jsonObject = new JSONObject(responseJSON);
                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse = null;

                if (rootObject!=null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse != null) {

                    if (executionResponse.getSuccess() == 1) {

                        List<Load> loadList = rootObject.getLoads();

                        if (loadList != null && loadList.size() > 0) {

                            //tableVehicleLoad.deleteAllVehicleLoads();
                            //tableVehicleStock.deleteAllVehicleStocks();

                            VehicleLoad vehicleLoad=null;
                            VehicleStock vehicleStock=null;

                            for (Load load : loadList) {

                                if(tableVehicleLoad.getVehicleLoad(load.getLoadId()) ==  null) {

                                    vehicleLoad = new VehicleLoad();

                                    Route route = load.getRoute();
                                    if (route != null) {
                                        vehicleLoad.setRouteCode(route.getRouteCode());
                                        vehicleLoad.setRouteId(route.getRouteId());
                                    }

                                    vehicleLoad.setSettlementNo(load.getSettlementNo());
                                    vehicleLoad.setLoadAmount(load.getLoadAmount());
                                    vehicleLoad.setLoadId(load.getLoadId());
                                    vehicleLoad.setLoadJSON(gson.toJson(load));


                                    tableVehicleLoad.createVehicleLoad(vehicleLoad);

                                    for (LoadItem loadItem : load.getLoadItems()) {

                                        vehicleStock = new VehicleStock();

                                        vehicleStock.setItemId(loadItem.getItemId());
                                        vehicleStock.setItemCode(loadItem.getItemCode());
                                        vehicleStock.setItemName(loadItem.getItemName());
                                        vehicleStock.setItemTypeId(loadItem.getItemTypeId());
                                        vehicleStock.setBottleQuantity((loadItem.getUoMId() == 1) ? loadItem.getQuantity() : 0);
                                        vehicleStock.setCaseQuantity((loadItem.getUoMId() != 1) ? loadItem.getQuantity() : 0);
                                        vehicleStock.setTransactionType(1);
                                        vehicleStock.setImageName(loadItem.getImageName());
                                        vehicleStock.setLineMRP(loadItem.getLineMRP());

                                        //VehicleStock localVehicleStock = tableVehicleStock.getVehicleStock(loadItem.getItemId());
                                        VehicleStock localVehicleStock = tableVehicleStock.getVehicleStock(loadItem.getItemId(),loadItem.getLineMRP());

                                        if (localVehicleStock != null) {
                                            double caseQty = localVehicleStock.getCaseQuantity();
                                            double bottleQty = localVehicleStock.getBottleQuantity();
                                            if (loadItem.getUoMId() != 1)
                                                caseQty += loadItem.getQuantity();
                                            else
                                                bottleQty += loadItem.getQuantity();

                                            //tableVehicleStock.updateVanInventory(loadItem.getItemId(), caseQty, bottleQty);

                                            tableVehicleStock.updateVanInventory(loadItem.getItemId(),loadItem.getLineMRP() , caseQty, bottleQty);

                                        } else {
                                            tableVehicleStock.createVehicleStock(vehicleStock);
                                        }


                                    }
                                }

                            }

                            status = true;

                        }

                        status = true;

                    }
                }
            }


        }catch (Exception ex){
            Logger.Log(TAG,ex);
            loadOutInfoResultReceiver.send(STATUS_ERROR, bundle);
            return status;
        }

        return  status;
    }

}

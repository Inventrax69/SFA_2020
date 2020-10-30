package com.inventrax_pepsi.sfa.logout;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.activities.LoginActivity;
import com.inventrax_pepsi.activities.MainActivity;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetCapture;
import com.inventrax_pepsi.database.TableAssetComplaint;
import com.inventrax_pepsi.database.TableAssetPullout;
import com.inventrax_pepsi.database.TableAssetRequest;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableCustomerDiscount;
import com.inventrax_pepsi.database.TableCustomerPrice;
import com.inventrax_pepsi.database.TableCustomerTrans;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.database.TableUser;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.interfaces.MainActivityView;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.LogoutInfo;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 18-Apr-16.
 */
public class LogoutUtil {

    private FragmentActivity fragmentActivity;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private Activity activity;
    private MainActivityView mainActivityView;

    private DatabaseHelper databaseHelper;
    private TableUser tableUser;
    private TableOrder tableOrder;
    private TableJSONMessage tableJSONMessage;
    private TableCustomer tableCustomer;
    private TableScheme tableScheme;
    private TableCustomerDiscount tableCustomerDiscount;
    private TableCustomerPrice tableCustomerPrice;
    private TableUserTracking tableUserTracking;
   // private TableVehicleStock tableVehicleStock;
    private TableItem tableItem;
    private TableInvoice tableInvoice;
    private TableAssetCapture tableAssetCapture;
    private TableAssetComplaint tableAssetComplaint;
    private TableAssetPullout tableAssetPullout;
    private TableAssetRequest tableAssetRequest;
    //private TableCustomerReturn tableCustomerReturn;
    private TableCustomerTrans tableCustomerTrans;

    private Gson gson;
    private BackgroundServiceFactory backgroundServiceFactory;

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setSharedPreferencesUtils(SharedPreferencesUtils sharedPreferencesUtils) {
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        new ProgressDialogUtils(activity);
    }

    public void setBackgroundServiceFactory(BackgroundServiceFactory backgroundServiceFactory) {
        this.backgroundServiceFactory = backgroundServiceFactory;
    }

    public void setMainActivityView(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
    }

    public LogoutUtil(){

        gson=new GsonBuilder().create();

        databaseHelper = DatabaseHelper.getInstance();

        tableUser = databaseHelper.getTableUser();
        tableOrder = databaseHelper.getTableOrder();
        tableJSONMessage = databaseHelper.getTableJSONMessage();
        tableCustomer = databaseHelper.getTableCustomer();
        tableScheme = databaseHelper.getTableScheme();
        tableCustomerDiscount = databaseHelper.getTableCustomerDiscount();
        tableCustomerPrice = databaseHelper.getTableCustomerPrice();
        tableUserTracking=databaseHelper.getTableUserTracking();
        //tableVehicleStock=databaseHelper.getTableVehicleStock();
        tableItem=databaseHelper.getTableItem();
        tableInvoice=databaseHelper.getTableInvoice();
        tableAssetCapture=databaseHelper.getTableAssetCapture();
        tableAssetComplaint=databaseHelper.getTableAssetComplaint();
        tableAssetPullout=databaseHelper.getTableAssetPullout();
        tableAssetRequest=databaseHelper.getTableAssetRequest();
        //tableCustomerReturn=databaseHelper.getTableCustomerReturn();
        tableCustomerTrans=databaseHelper.getTableCustomerTrans();

    }



    public void performLogoutOperation() {

        try {

            if (!NetworkUtils.isInternetAvailable()){

                DialogUtils.showAlertDialog(fragmentActivity,"No internet connection available");
                return;

            }

            backgroundServiceFactory.setActivity(activity);
            backgroundServiceFactory.setContext(AbstractApplication.get());
            backgroundServiceFactory.setMainActivityView(mainActivityView);

            checkSyncStatus();

           /* List<JSONMessage> jsonMessageList=tableJSONMessage.getAllJSONMessages(0);

            if (jsonMessageList!=null)
            {
                DialogUtils.showAlertDialog(this,"Please wait, Syncing updated information");
                return;
            }*/

            new LogOutAsyncTask().execute();


        } catch (Exception ex) {

            Logger.Log(MainActivity.class.getName(), ex);
            DialogUtils.showAlertDialog(activity, "Error while logout");
            return;

        }

    }

    private void checkSyncStatus() {

        try
        {
            List<JSONMessage> jsonMessageList=tableJSONMessage.getAllJSONMessages(0);

            for (JSONMessage jsonMessage:jsonMessageList){

                switch (jsonMessage.getNotificationTypeId()){

                    // Order Type
                    case 1:{

                        backgroundServiceFactory.initiateOrderService();

                    }break;

                    // Invoice Type
                    case 2:{

                        backgroundServiceFactory.initiateInvoiceUpdateService();

                    }break;

                    // User Tracking Type
                    case 3:{

                        backgroundServiceFactory.initiateUserTrackService();

                    }break;

                    // Customer Return
                    case 4:{

                        backgroundServiceFactory.initiateCustomerReturnService();

                    }break;

                    // Asset Complaints
                    case 5:{

                        backgroundServiceFactory.initiateAssetComplaintService();

                    }break;

                    // Asset Capture
                    case 6:{

                        backgroundServiceFactory.initiateAssetCaptureService();

                    }break;

                    // Ready Sale Invoice
                    case 7:{

                        backgroundServiceFactory.initiateReadySaleInvoiceService();

                    }break;

                    // Asset Pullout
                    case 8:{

                        backgroundServiceFactory.initiateAssetPulloutService();

                    }break;

                    // Asset Request
                    case 9:{

                        backgroundServiceFactory.initiateAssetRequestService();

                    }break;

                    // Customer
                    case 10:{

                        backgroundServiceFactory.initiateCustomerService();

                    }break;
                    // Customer Trans
                    case 11:{

                        backgroundServiceFactory.initiateCustomerTransactionService();

                    }break;


                }

            }

        } catch (Exception ex) {
            Logger.Log(MainActivity.class.getName(), ex);
            return;
        }

    }

    private class LogOutAsyncTask extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {

            ProgressDialogUtils.showProgressDialog();


        }

        @Override
        protected String doInBackground(Void... params) {

            try
            {

                LoginInfo loginInfo= AppController.getLoginInfo();

                LogoutInfo logoutInfo=new LogoutInfo();

                logoutInfo.setDeviceInfo(loginInfo.getDeviceInfo());
                logoutInfo.setLogoutTime(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));
                logoutInfo.setMode(1);
                logoutInfo.setSysUserId(loginInfo.getSysUserId());
                logoutInfo.setServiceSessionId(loginInfo.getServiceSessionId());

                RootObject rootObject=new RootObject();
                rootObject.setServiceCode(ServiceCode.LOGOUT);
                rootObject.setLogoutInfo(logoutInfo);

                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);


                return SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_DO_LOGOUT);


            } catch (Exception ex){
                Logger.Log(LogOutAsyncTask.class.getName(),ex);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            ProgressDialogUtils.closeProgressDialog();

            try {

                if (!TextUtils.isEmpty(result)) {

                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                    RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                    ExecutionResponse executionResponse = null;

                    if (rootObject != null)
                        executionResponse = rootObject.getExecutionResponse();

                    if (executionResponse != null) {
                        if (executionResponse.getSuccess() == 1) {
                            doLogout();
                        }else {
                            doLogout();
                            DialogUtils.showAlertDialog(fragmentActivity,"Error while logout");
                            return;
                        }

                    }else {
                        doLogout();
                    }

                }else {
                    doLogout();
                }

            }catch (Exception ex){
                Logger.Log("", ex);
                ProgressDialogUtils.closeProgressDialog();
                DialogUtils.showAlertDialog(fragmentActivity,"Error while logout");
                return;
            }

        }
    }


    public void doLogout(){

        try
        {
            sharedPreferencesUtils.savePreference("login_status", false);
            sharedPreferencesUtils.savePreference("loginUserObject", "");
           // sharedPreferencesUtils.savePreference("userLoginInfo", "");

            tableUserTracking.deleteAllUserTrackingInfo();
            tableCustomerDiscount.deleteAllCustomerDiscounts();
            tableCustomer.deleteAllCustomers();
            tableCustomerPrice.deleteAllCustomerPrices();
            tableScheme.deleteAllSchemes();
            tableAssetCapture.deleteAllAssetCaptures();
            tableAssetComplaint.deleteAllAssetComplaints();
            //tableVehicleStock.deleteAllVehicleStocks();
            tableUser.deleteAllUsers();
            tableInvoice.deleteAllInvoices();
            tableOrder.deleteAllOrders();
            tableItem.deleteAllItems();
            tableAssetPullout.deleteAllAssetPullouts();
            tableAssetRequest.deleteAllAssetRequests();
            tableJSONMessage.deleteAllJSONMessages();
            //tableCustomerReturn.deleteAllCustomerReturns();
            tableCustomerTrans.deleteAllCustomerTrans();


            Intent loginIntent = new Intent(activity, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(loginIntent);



            Toast.makeText(activity, "You have successfully logged out", Toast.LENGTH_LONG).show();
            activity.finish();

        }catch (Exception ex){

        }

    }


}

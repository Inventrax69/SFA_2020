package com.inventrax_pepsi.services.sfa_background_services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.interfaces.MainActivityView;
import com.inventrax_pepsi.interfaces.OrderView;

/**
 * Created by android on 3/15/2016.
 */
public class BackgroundServiceFactory {

    private  Activity activity;
    private static MainActivityView mainActivityView;
    private static BackgroundServiceFactory backgroundServiceFactory;
    private static OrderView orderView;
    private Context context;


    private BackgroundServiceFactory(){

    }

    public  static synchronized BackgroundServiceFactory getInstance(){

        if (backgroundServiceFactory!=null){

            return backgroundServiceFactory;

        }
        else {

            return new BackgroundServiceFactory();

        }

    }

    public void setMainActivityView(MainActivityView mainActivityView) {
        BackgroundServiceFactory.mainActivityView = mainActivityView;
    }

    public static void setOrderView(OrderView orderView) {
        BackgroundServiceFactory.orderView = orderView;
    }

    public  void setContext(Context context) {
       this.context=context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void initiateSKUListService(){

        try
        {

            SKUListResultReceiver skuListResultReceiver=new SKUListResultReceiver(new Handler());
            skuListResultReceiver.setReceiver(new SKUListListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, SKUListIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("SKUListResultReceiver", skuListResultReceiver);
            activity.startService(intent);

        }catch (Exception ex){

            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (mainActivityView!=null){
                mainActivityView.showSKUListStatus("Error While Syncing SKU List", 2);
            }

            return;
        }

    }

    private class SKUListListener implements SKUListResultReceiver.Receiver {

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (mainActivityView!=null) {

                switch (resultCode) {

                    case SKUListIntentService.STATUS_FINISHED: {
                        mainActivityView.showSKUListStatus("SKU List Successfully Synced", SKUListIntentService.STATUS_FINISHED);
                    }
                    break;

                    case SKUListIntentService.STATUS_RUNNING: {
                        mainActivityView.showSKUListStatus("Syncing SKU List ", SKUListIntentService.STATUS_RUNNING);
                    }
                    break;

                    case SKUListIntentService.STATUS_ERROR: {
                        mainActivityView.showSKUListStatus("Error While Syncing SKU List", SKUListIntentService.STATUS_ERROR);
                    }
                    break;
                }
            }
        }
    }


    public void initiateOutletListService(){

        try
        {
            OutletListResultReceiver outletListResultReceiver = new OutletListResultReceiver(new Handler());
            outletListResultReceiver.setReceiver(new OutletListListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, OutletListIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("OutletListResultReceiver", outletListResultReceiver);
            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            if (mainActivityView!=null){
                mainActivityView.showOutletListStatus("Error While Syncing Outlet List", 2);
            }

            return;
        }

    }

    private class OutletListListener implements OutletListResultReceiver.Receiver  {

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (mainActivityView!=null) {

                switch (resultCode) {

                    case OutletListIntentService.STATUS_FINISHED: {
                        mainActivityView.showOutletListStatus("Outlet List Successfully Synced", OutletListIntentService.STATUS_FINISHED);
                    }
                    break;

                    case OutletListIntentService.STATUS_RUNNING: {
                        mainActivityView.showOutletListStatus("Syncing Outlet List", OutletListIntentService.STATUS_RUNNING);
                    }
                    break;

                    case OutletListIntentService.STATUS_ERROR: {
                        mainActivityView.showOutletListStatus("Error While Syncing Outlet List", OutletListIntentService.STATUS_ERROR);
                    }
                    break;
                }
            }

        }
    }


    public void initiateLoadOutService(){

        try
        {

            LoadOutInfoResultReceiver loadOutInfoResultReceiver=new LoadOutInfoResultReceiver(new Handler());
            loadOutInfoResultReceiver.setReceiver(new LoadOutListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, LoadOutInfoIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("LoadOutInfoResultReceiver", loadOutInfoResultReceiver);
            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (mainActivityView!=null){
                mainActivityView.showLoadOutListStatus("Error While Syncing Load Out Information", 2);
            }

            return;
        }

    }

    private class LoadOutListener implements LoadOutInfoResultReceiver.Receiver  {

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (mainActivityView!=null) {

                switch (resultCode) {

                    case LoadOutInfoIntentService.STATUS_FINISHED: {
                        mainActivityView.showLoadOutListStatus("Load Out Information Successfully Synced", LoadOutInfoIntentService.STATUS_FINISHED);
                    }
                    break;

                    case LoadOutInfoIntentService.STATUS_RUNNING: {
                        mainActivityView.showLoadOutListStatus("Syncing Load Out Information", LoadOutInfoIntentService.STATUS_RUNNING);
                    }
                    break;

                    case LoadOutInfoIntentService.STATUS_ERROR: {
                        mainActivityView.showLoadOutListStatus("Error While Syncing Load Out Information", LoadOutInfoIntentService.STATUS_ERROR);
                    }
                    break;
                }
            }

        }
    }


    public void initiateDeliveryListService(){

        try
        {
            DeliveryListResultReceiver deliveryListResultReceiver=new DeliveryListResultReceiver(new Handler());
            deliveryListResultReceiver.setReceiver(new DeliveryListListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, DeliveryListIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("DeliveryListResultReceiver", deliveryListResultReceiver);
            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (mainActivityView!=null){
                mainActivityView.showDeliveryListStatus("Error While Syncing Delivery List", 2);
            }

            return;
        }

    }

    private class DeliveryListListener implements DeliveryListResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (mainActivityView!=null) {

                switch (resultCode) {

                    case DeliveryListIntentService.STATUS_FINISHED: {
                        mainActivityView.showDeliveryListStatus(" Delivery List Successfully Synced", DeliveryListIntentService.STATUS_FINISHED);
                    }
                    break;

                    case DeliveryListIntentService.STATUS_RUNNING: {
                        mainActivityView.showDeliveryListStatus("Syncing Delivery List", DeliveryListIntentService.STATUS_RUNNING);
                    }
                    break;

                    case DeliveryListIntentService.STATUS_ERROR: {
                        mainActivityView.showDeliveryListStatus("Error While Syncing Delivery List", DeliveryListIntentService.STATUS_ERROR);
                    }
                    break;
                }
            }

        }
    }


    public void initiateDiscountService(){

        try
        {
            DiscountListResultReceiver discountListResultReceiver=new DiscountListResultReceiver(new Handler());
            discountListResultReceiver.setReceiver(new DiscountListListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, DiscountListIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("DiscountListResultReceiver", discountListResultReceiver);
            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (mainActivityView!=null){
                mainActivityView.showDiscountListStatus("Error While Syncing Discount Information", 2);
            }

            return;
        }

    }

    private class DiscountListListener implements DiscountListResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (mainActivityView!=null) {

                switch (resultCode) {

                    case DiscountListIntentService.STATUS_FINISHED: {
                        mainActivityView.showDiscountListStatus("Discount Information Successfully Synced", DiscountListIntentService.STATUS_FINISHED);
                    }
                    break;

                    case DiscountListIntentService.STATUS_RUNNING: {
                        mainActivityView.showDiscountListStatus("Syncing Discount Information", DiscountListIntentService.STATUS_RUNNING);
                    }
                    break;
                    case DiscountListIntentService.STATUS_ERROR: {
                        mainActivityView.showDiscountListStatus("Error While Syncing Discount Information", DiscountListIntentService.STATUS_ERROR);
                    }
                    break;

                }
            }

        }
    }

    public void initiateOrderService(String orderNumber){

        try
        {
            OrderResultReceiver orderResultReceiver=new OrderResultReceiver(new Handler());
            orderResultReceiver.setReceiver(new OrderListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, OrderIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("OrderResultReceiver", orderResultReceiver);
            intent.putExtra("OrderNumber",orderNumber);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (orderView!=null){
                orderView.showOrderSyncStatus("Error While Syncing Order Information", 2);
            }

            return;
        }

    }


    private class OrderListener implements OrderResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (orderView!=null) {

                switch (resultCode) {

                    case OrderIntentService.STATUS_FINISHED: {
                        orderView.showOrderSyncStatus("Order Information Successfully Synced", OrderIntentService.STATUS_FINISHED);
                    }
                    break;

                    case OrderIntentService.STATUS_RUNNING: {
                        orderView.showOrderSyncStatus("Syncing Order Information", OrderIntentService.STATUS_RUNNING);
                    }
                    break;
                    case OrderIntentService.STATUS_ERROR: {
                        orderView.showOrderSyncStatus("Error While Syncing Order Information", OrderIntentService.STATUS_ERROR);
                    }
                    break;

                }
            }

        }
    }



    public void initiateInvoiceUpdateService(String invoiceNumber){

        try
        {
            InvoiceResultReceiver invoiceResultReceiver=new InvoiceResultReceiver(new Handler());
            invoiceResultReceiver.setReceiver(new UpdateInvoiceListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, InvoiceIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("InvoiceResultReceiver", invoiceResultReceiver);
            intent.putExtra("InvoiceNumber",invoiceNumber);
            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            if (orderView!=null){
                orderView.showOrderSyncStatus("Error While Syncing Delivery Info.", 2);
            }

            return;
        }

    }

    private class UpdateInvoiceListener implements InvoiceResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            if (orderView!=null) {

                switch (resultCode) {

                    case InvoiceIntentService.STATUS_FINISHED: {
                        orderView.showOrderSyncStatus(" Delivery Updated Successfully Synced", DeliveryListIntentService.STATUS_FINISHED);
                    }
                    break;

                    case InvoiceIntentService.STATUS_RUNNING: {
                        orderView.showOrderSyncStatus("Syncing Updated Delivery ", DeliveryListIntentService.STATUS_RUNNING);
                    }
                    break;

                    case InvoiceIntentService.STATUS_ERROR: {
                        orderView.showOrderSyncStatus("Error While Syncing Delivery Info.", DeliveryListIntentService.STATUS_ERROR);
                    }
                    break;
                }
            }

        }
    }


    public void initiateUserTrackService(int customerId,boolean isCheckIn){

        try
        {
            UserTrackingResultReceiver userTrackingResultReceiver=new UserTrackingResultReceiver(new Handler());
            userTrackingResultReceiver.setReceiver(new UserTrackListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, UserTrackingIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("UserTrackingResultReceiver", userTrackingResultReceiver);
            intent.putExtra("CustomerId",customerId);
            intent.putExtra("IsCheckIn",isCheckIn);

            activity.startService(intent);


        }catch (Exception ex){

            Logger.Log(BackgroundServiceFactory.class.getName(),ex);

            return;
        }

    }

    private class UserTrackListener implements UserTrackingResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case UserTrackingIntentService.STATUS_FINISHED: {

                    }
                    break;

                    case UserTrackingIntentService.STATUS_RUNNING: {

                    }
                    break;

                    case UserTrackingIntentService.STATUS_ERROR: {

                    }
                    break;
                }

        }
    }



    public void initiateOrderService(){

        try
        {
            OrderResultReceiver orderResultReceiver=new OrderResultReceiver(new Handler());
            orderResultReceiver.setReceiver(new OrderListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, OrderIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("OrderResultReceiver", orderResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(), ex);
            return;
        }

    }

    public void initiateInvoiceUpdateService(){

        try
        {
            InvoiceResultReceiver invoiceResultReceiver=new InvoiceResultReceiver(new Handler());
            invoiceResultReceiver.setReceiver(new UpdateInvoiceListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, InvoiceIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("InvoiceResultReceiver", invoiceResultReceiver);
            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    public void initiateUserTrackService(){

        try
        {
            UserTrackingResultReceiver userTrackingResultReceiver=new UserTrackingResultReceiver(new Handler());
            userTrackingResultReceiver.setReceiver(new UserTrackListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, UserTrackingIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("UserTrackingResultReceiver", userTrackingResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    public void initiateCustomerOrderHistoryService(){

        try
        {
            CustomerOrderHistoryResultReceiver customerOrderHistoryResultReceiver=new CustomerOrderHistoryResultReceiver(new Handler());
            customerOrderHistoryResultReceiver.setReceiver(new CustomerOrderHistoryListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, CustomerOrderHistoryIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("CustomerOrderHistoryResultReceiver", customerOrderHistoryResultReceiver);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class CustomerOrderHistoryListener implements  CustomerOrderHistoryResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            switch (resultCode) {

                case CustomerOrderHistoryIntentService.STATUS_FINISHED: {

                }
                break;

                case CustomerOrderHistoryIntentService.STATUS_RUNNING: {

                }
                break;

                case CustomerOrderHistoryIntentService.STATUS_ERROR: {

                }
                break;
            }
        }
    }


    public void initiateCustomerReturnService(int customerId){

        try
        {
            CustomerReturnResultReceiver customerReturnResultReceiver=new CustomerReturnResultReceiver(new Handler());
            customerReturnResultReceiver.setReceiver(new CustomerReturnListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, CustomerReturnIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("CustomerReturnResultReceiver", customerReturnResultReceiver);
            intent.putExtra("CustomerId",customerId);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    public void initiateCustomerReturnService(){

        try
        {
            CustomerReturnResultReceiver customerReturnResultReceiver=new CustomerReturnResultReceiver(new Handler());
            customerReturnResultReceiver.setReceiver(new CustomerReturnListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, CustomerReturnIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("CustomerReturnResultReceiver", customerReturnResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    private class CustomerReturnListener implements CustomerReturnResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }


    public void initiateAssetComplaintService(int auto_inc_id){

        try
        {
            AssetComplaintResultReceiver assetComplaintResultReceiver=new AssetComplaintResultReceiver(new Handler());
            assetComplaintResultReceiver.setReceiver(new AssetComplaintListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, AssetComplaintIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetComplaintResultReceiver", assetComplaintResultReceiver);
            intent.putExtra("AutoIncId",auto_inc_id);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    public void initiateAssetComplaintService(){

        try
        {
            AssetComplaintResultReceiver assetComplaintResultReceiver=new AssetComplaintResultReceiver(new Handler());
            assetComplaintResultReceiver.setReceiver(new AssetComplaintListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AssetComplaintIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetComplaintResultReceiver", assetComplaintResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class AssetComplaintListener implements AssetComplaintResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }



    public void initiateAssetCaptureService(String QR_CODE){

        try
        {
            AssetCaptureResultReceiver assetCaptureResultReceiver=new AssetCaptureResultReceiver(new Handler());
            assetCaptureResultReceiver.setReceiver(new AssetCaptureListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, AssetCaptureIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetCaptureResultReceiver", assetCaptureResultReceiver);
            intent.putExtra("QR_CODE",QR_CODE);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    public void initiateAssetCaptureService(){

        try
        {
            AssetCaptureResultReceiver assetCaptureResultReceiver=new AssetCaptureResultReceiver(new Handler());
            assetCaptureResultReceiver.setReceiver(new AssetCaptureListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AssetCaptureIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetCaptureResultReceiver", assetCaptureResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class AssetCaptureListener implements AssetCaptureResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }


    public void initiateReadySaleInvoiceService(int auto_inc_id){

        try
        {
            ReadySaleInvoiceResultReceiver readySaleInvoiceResultReceiver=new ReadySaleInvoiceResultReceiver(new Handler());
            readySaleInvoiceResultReceiver.setReceiver(new ReadySaleInvoiceListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ReadySaleInvoiceIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("ReadySaleInvoiceResultReceiver", readySaleInvoiceResultReceiver);
            intent.putExtra("AutoIncId",auto_inc_id);

            activity.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    public void initiateReadySaleInvoiceService(){

        try
        {
            ReadySaleInvoiceResultReceiver readySaleInvoiceResultReceiver=new ReadySaleInvoiceResultReceiver(new Handler());
            readySaleInvoiceResultReceiver.setReceiver(new ReadySaleInvoiceListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ReadySaleInvoiceIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("ReadySaleInvoiceResultReceiver", readySaleInvoiceResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class ReadySaleInvoiceListener implements ReadySaleInvoiceResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }


    public void initiateTodayOrderService(){

        try
        {
            TodayOrdersResultReceiver todayOrdersResultReceiver=new TodayOrdersResultReceiver(new Handler());
            todayOrdersResultReceiver.setReceiver(new TodayOrdersListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, TodayOrdersIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("TodayOrdersResultReceiver", todayOrdersResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(), ex);
            return;
        }

    }

    private class TodayOrdersListener implements TodayOrdersResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {


            switch (resultCode) {

                case TodayOrdersIntentService.STATUS_FINISHED: {

                    Toast.makeText(context,"Today Orders Successfully Synced",Toast.LENGTH_LONG).show();

                }
                break;

                case TodayOrdersIntentService.STATUS_RUNNING: {

                    Toast.makeText(context,"Syncing Order Info ..."   ,Toast.LENGTH_LONG).show();

                }
                break;
                case TodayOrdersIntentService.STATUS_ERROR: {

                    Toast.makeText(context,"Error While Syncing Orders " ,Toast.LENGTH_LONG).show();

                }
                break;

            }

        }
    }


    public void initiateAssetRequestService(){

        try
        {
            AssetRequestResultReceiver assetRequestResultReceiver=new AssetRequestResultReceiver(new Handler());
            assetRequestResultReceiver.setReceiver(new AssetRequestListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AssetRequestIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetRequestResultReceiver", assetRequestResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class AssetRequestListener implements AssetRequestResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }

    public void initiateAssetPulloutService(){

        try
        {
            AssetPulloutResultReceiver assetPulloutResultReceiver=new AssetPulloutResultReceiver(new Handler());
            assetPulloutResultReceiver.setReceiver(new AssetPulloutListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AssetPulloutIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("AssetPulloutResultReceiver", assetPulloutResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class AssetPulloutListener implements AssetPulloutResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            switch (resultCode){

                case AssetPulloutIntentService.STATUS_RUNNING:{

                }break;

                case AssetPulloutIntentService.STATUS_FINISHED:{

                }
                case AssetPulloutIntentService.STATUS_ERROR:{

                }break;

            }


        }
    }

    public void initiateCustomerService(){

        try
        {
            CustomerResultReceiver customerResultReceiver=new CustomerResultReceiver(new Handler());
            customerResultReceiver.setReceiver(new CustomerListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, CustomerIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("CustomerResultReceiver", customerResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }

    private class CustomerListener implements CustomerResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {


            switch (resultCode){

                case CustomerIntentService.STATUS_RUNNING:{

                }break;

                case CustomerIntentService.STATUS_FINISHED:{

                }break;

                case CustomerIntentService.STATUS_ERROR:{

                }break;

            }

        }

    }


    public void initiateCustomerTransactionService(){

        try
        {
            CustomerTransResultReceiver customerTransResultReceiver=new CustomerTransResultReceiver(new Handler());
            customerTransResultReceiver.setReceiver(new CustomerTransListener());

            Intent intent = new Intent(Intent.ACTION_SYNC, null, context, CustomerTransIntentService.class);

            // Send optional extras to IntentService
            intent.putExtra("CustomerTransResultReceiver", customerTransResultReceiver);

            context.startService(intent);


        }catch (Exception ex){
            Logger.Log(BackgroundServiceFactory.class.getName(),ex);
            return;
        }

    }


    private class CustomerTransListener implements CustomerTransResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }






}

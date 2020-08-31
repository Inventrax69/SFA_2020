package com.inventrax_pepsi.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.services.sfa_background_services.AssetCaptureIntentService;
import com.inventrax_pepsi.services.sfa_background_services.AssetCaptureResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.AssetComplaintIntentService;
import com.inventrax_pepsi.services.sfa_background_services.AssetComplaintResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.AssetPulloutIntentService;
import com.inventrax_pepsi.services.sfa_background_services.AssetPulloutResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.AssetRequestIntentService;
import com.inventrax_pepsi.services.sfa_background_services.AssetRequestResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.CustomerIntentService;
import com.inventrax_pepsi.services.sfa_background_services.CustomerResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.CustomerReturnIntentService;
import com.inventrax_pepsi.services.sfa_background_services.CustomerReturnResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.CustomerTransIntentService;
import com.inventrax_pepsi.services.sfa_background_services.CustomerTransResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.InvoiceIntentService;
import com.inventrax_pepsi.services.sfa_background_services.InvoiceResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.OrderIntentService;
import com.inventrax_pepsi.services.sfa_background_services.OrderResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.ReadySaleInvoiceIntentService;
import com.inventrax_pepsi.services.sfa_background_services.ReadySaleInvoiceResultReceiver;
import com.inventrax_pepsi.services.sfa_background_services.UserTrackingIntentService;
import com.inventrax_pepsi.services.sfa_background_services.UserTrackingResultReceiver;
import com.inventrax_pepsi.util.NetworkUtils;

/**
 * Created by Naresh on 05-Jan-16.
 */
public class NetworkChangeListener extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtils.getConnectivityStatusAsString(context);

        this.context=context;

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        if (NetworkUtils.isInternetAvailable()){

            initiateOrderService();
            initiateInvoiceUpdateService();
            initiateUserTrackService();
            initiateCustomerReturnService();
            initiateAssetComplaintService();
            initiateAssetCaptureService();
            initiateReadySaleInvoiceService();
            initiateAssetRequestService();
            initiateCustomerService();
            initiateAssetPulloutService();
            initiateCustomerTransactionService();

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
            Logger.Log(NetworkChangeListener.class.getName(), ex);
            return;
        }

    }

    private class OrderListener implements OrderResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {


            switch (resultCode) {

                case OrderIntentService.STATUS_FINISHED: {

                    Toast.makeText(context,"Order  " + resultData.getString("OrderNumber") + " Successfully Synced",Toast.LENGTH_LONG).show();

                }
                break;

                case OrderIntentService.STATUS_RUNNING: {

                    Toast.makeText(context,"Syncing Order " + resultData.getString("OrderNumber")   ,Toast.LENGTH_LONG).show();

                }
                break;
                case OrderIntentService.STATUS_ERROR: {

                    Toast.makeText(context,"Error While Syncing Order " + resultData.getString("OrderNumber"),Toast.LENGTH_LONG).show();

                }
                break;

            }


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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    private class UpdateInvoiceListener implements InvoiceResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case InvoiceIntentService.STATUS_FINISHED: {
                        Toast.makeText(context," Delivery Updated Successfully Synced", Toast.LENGTH_LONG).show();
                    }
                    break;

                    case InvoiceIntentService.STATUS_RUNNING: {
                        Toast.makeText(context,"Syncing Updated Delivery ", Toast.LENGTH_LONG).show();
                    }
                    break;

                    case InvoiceIntentService.STATUS_ERROR: {
                        Toast.makeText(context,"Error While Syncing Delivery Info.", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
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

            Logger.Log(NetworkChangeListener.class.getName(),ex);

            return;
        }

    }

    private class UserTrackListener implements UserTrackingResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

            switch (resultCode) {

                case UserTrackingIntentService.STATUS_FINISHED: {
                    Toast.makeText(context," User Tracking Details Successfully Synced", Toast.LENGTH_LONG).show();
                }
                break;

                case UserTrackingIntentService.STATUS_RUNNING: {
                    Toast.makeText(context," Syncing User Tracking Details", Toast.LENGTH_LONG).show();
                }
                break;

                case UserTrackingIntentService.STATUS_ERROR: {
                    Toast.makeText(context,"Error While Syncing User Tracking Details", Toast.LENGTH_LONG).show();
                }
                break;
            }

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    
    private class CustomerReturnListener implements CustomerReturnResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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

            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;

        }

    }

    private class AssetComplaintListener implements AssetComplaintResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    private class AssetCaptureListener implements AssetCaptureResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    private class ReadySaleInvoiceListener implements ReadySaleInvoiceResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    private class AssetPulloutListener implements AssetPulloutResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }

    private class CustomerListener implements CustomerResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

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
            Logger.Log(NetworkChangeListener.class.getName(),ex);
            return;
        }

    }


    private class CustomerTransListener implements CustomerTransResultReceiver.Receiver{

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {

        }
    }
    
    
}
package com.inventrax_pepsi.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerReturn;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.TableVehicleLoad;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.CustomerReturn;
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.InvoiceItem;
import com.inventrax_pepsi.sfa.pojos.Load;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentGUI;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 5/26/2016.
 */
public class DaySummaryFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private SFACommon sfaCommon;
    private TextView txtTotalOrderValue,txtTotalCashCollected,txtNumberOfCrates,txtNumberOfEmpties,txtCrownValue,txtFulls,txtTotalOrderQuantity;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableInvoice tableInvoice;
    private TableVehicleLoad tableVehicleLoad;
    private TableVehicleStock tableVehicleStock;
    private TableCustomerReturn tableCustomerReturn;
    private double totalOrderValue=0,totalCashCollected=0,numberOfEmpties=0,crownValue=0,fullsInCases=0,fullsInBottles=0,numberOfEmptyBottles=0,emptyCrates=0,orderQuantity=0;
    private Gson gson;
    private Button btnSettlementRequest;
    private BackgroundServiceFactory backgroundServiceFactory;
    private TableJSONMessage tableJSONMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_day_summary,container,false);

        sfaCommon=SFACommon.getInstance();

        FragmentGUI.setView(rootView);

        loadFormControls();

        return rootView;
    }

    private void loadFormControls(){
        try
        {
            gson = new GsonBuilder().create();
            new ProgressDialogUtils(getActivity());

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());

            txtTotalOrderValue = FragmentGUI.getTextView(R.id.txtTotalOrderValue);
            txtTotalCashCollected = FragmentGUI.getTextView(R.id.txtTotalCashCollected);
            txtNumberOfCrates = FragmentGUI.getTextView(R.id.txtNumberOfCrates);
            txtNumberOfEmpties = FragmentGUI.getTextView(R.id.txtNumberOfEmpties);
            txtCrownValue = FragmentGUI.getTextView(R.id.txtCrownValue);
            txtFulls = FragmentGUI.getTextView(R.id.txtFulls);
            txtTotalOrderQuantity=FragmentGUI.getTextView(R.id.txtTotalOrderQuantity);
            btnSettlementRequest = FragmentGUI.getButton(R.id.btnSettlementRequest);
            btnSettlementRequest.setOnClickListener(this);

            databaseHelper=DatabaseHelper.getInstance();
            tableOrder=databaseHelper.getTableOrder();
            tableInvoice=databaseHelper.getTableInvoice();
            tableVehicleStock=databaseHelper.getTableVehicleStock();
            tableCustomerReturn=databaseHelper.getTableCustomerReturn();
            tableJSONMessage=databaseHelper.getTableJSONMessage();
            tableVehicleLoad=databaseHelper.getTableVehicleLoad();

            if (tableVehicleLoad.getLoadStatus()==0)
            {
                btnSettlementRequest.setVisibility(Button.VISIBLE);
            }else {
                btnSettlementRequest.setVisibility(Button.GONE);
            }

            getOrderInfo();
            getDeliveryInfo();
            getCustomerReturn();
            getStockInfo();

            txtTotalOrderValue.setText(":  " + getString(R.string.Rs)+ NumberUtils.formatValue(totalOrderValue));
            txtTotalOrderQuantity.setText(":  " + NumberUtils.formatValueUptoThreeDecimals(orderQuantity));
            txtTotalCashCollected.setText(":  " + getString(R.string.Rs)+ NumberUtils.formatValue(totalCashCollected));
            txtNumberOfCrates.setText(":  " + emptyCrates);
            txtNumberOfEmpties.setText(":  " + numberOfEmpties+" CS" + "    " + numberOfEmptyBottles + " FB"   );
            txtCrownValue.setText(":  " + crownValue );
            txtFulls.setText(":  " + fullsInCases+" CS" + "   " + fullsInBottles+" FB" );

        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_day_summary));
        sfaCommon.displayDate(getActivity());
    }

    private void getDeliveryInfo(){

        try
        {

            Invoice invoice = null;

            List<DeliveryInvoice> deliveryInvoiceList = tableInvoice.getAllInvoices();

            for (DeliveryInvoice deliveryInvoice : deliveryInvoiceList) {

                invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);

                if (invoice.getOrderStatusId() <= OrderStatus.Partial.getStatus() || invoice.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {
                    totalOrderValue += invoice.getNetAmount();

                    if (invoice.getInvoiceItems()!=null && invoice.getInvoiceItems().size()>0 ) {

                        for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                            if (invoiceItem.getUoMCode().equalsIgnoreCase("FC") || invoiceItem.getUoMCode().equalsIgnoreCase("PACK")) {
                                orderQuantity += invoiceItem.getQuantity();

                            } else {

                                orderQuantity += (invoiceItem.getQuantity() / 1000);

                            }

                        }
                    }

                }

                if ( invoice.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {
                    if (invoice.getPaymentInfo() != null)
                        totalCashCollected += invoice.getPaymentInfo().getAmount();
                }

            }


        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }

    }

    private void getOrderInfo(){

        try
        {
            List<com.inventrax_pepsi.database.pojos.Order> localDBOrderHistoryList =  tableOrder.getAllOrders();

            Order order = null;

            for (com.inventrax_pepsi.database.pojos.Order localDBOrder : localDBOrderHistoryList) {

                order = gson.fromJson(localDBOrder.getOrderJSON(), Order.class);

                if (order.getOrderStatusId() <= OrderStatus.Partial.getStatus() || order.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {
                    totalOrderValue += order.getDerivedPrice();

                    if (order.getOrderItems()!=null && order.getOrderItems().size()>0) {
                        for (OrderItem orderItem : order.getOrderItems()) {

                            if (orderItem.getUoMCode().equalsIgnoreCase("FC") || orderItem.getUoMCode().equalsIgnoreCase("PACK")) {
                                orderQuantity += orderItem.getQuantity();

                            } else {

                                orderQuantity += (orderItem.getQuantity() / 1000);

                            }

                        }
                    }

                    //orderQuantity += order.getOrderQuantity();
                }


                if ( order.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {

                    if (order.getPaymentInfo() != null) {
                        totalCashCollected += order.getPaymentInfo().getAmount();
                    }

                }

            }


        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }

    }

    private void getStockInfo(){

        try
        {
            List<VehicleStock> vehicleStocks=tableVehicleStock.getAllVehicleStocks();

            if (vehicleStocks==null || vehicleStocks.size()==0 )
                return;

            for (VehicleStock vehicleStock:vehicleStocks)
            {
                fullsInCases+=vehicleStock.getCaseQuantity();
                fullsInBottles+=vehicleStock.getBottleQuantity();
            }

        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }

    }

    private void getCustomerReturn(){
        try
        {

            List<CustomerReturn> customerReturns=tableCustomerReturn.getAllCustomerReturns();

            if (customerReturns==null || customerReturns.size()==0 )
                return;


            for (CustomerReturn customerReturn:customerReturns)
            {
                com.inventrax_pepsi.sfa.pojos.CustomerReturn customerReturn1= gson.fromJson(customerReturn.getJson(), com.inventrax_pepsi.sfa.pojos.CustomerReturn.class);

                numberOfEmpties+=customerReturn1.getNoOfCases();
                crownValue+=customerReturn1.getCrownValue();
                numberOfEmptyBottles+=customerReturn1.getNoOfBottles();
                emptyCrates+=customerReturn1.getNoOfShells();

            }

        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

            final MenuItem menuItemSyncOrderInfo = menu.findItem(R.id.action_sync_order_info);
            menuItemSyncOrderInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemSyncOrderInfo.setVisible(true);

            menuItemSyncOrderInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    try {

                        DialogUtils.showAlertDialog(getActivity(), "Syncing started, Please Wait ...");

                        syncToServer();



                    }catch (Exception ex){
                        Logger.Log(DashboardFragment.class.getName(),ex);
                    }

                    return false;
                }
            });


        }

        super.onCreateOptionsMenu(menu, inflater);

    }


    private void syncToServer(){
        try
        {


            if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.internetenablemessage));
                return;
            }

            if (backgroundServiceFactory != null) {

                backgroundServiceFactory.initiateOrderService();
                backgroundServiceFactory.initiateInvoiceUpdateService();
                backgroundServiceFactory.initiateUserTrackService();
                backgroundServiceFactory.initiateCustomerReturnService();
                backgroundServiceFactory.initiateAssetComplaintService();
                backgroundServiceFactory.initiateAssetCaptureService();
                backgroundServiceFactory.initiateReadySaleInvoiceService();
                backgroundServiceFactory.initiateAssetPulloutService();
                backgroundServiceFactory.initiateCustomerService();
                backgroundServiceFactory.initiateAssetRequestService();

            }

        }catch (Exception ex){
            DaySummaryFragment.class.getName();
            return;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSettlementRequest:{

                DialogUtils.showConfirmDialog(getActivity(),"",AbstractApplication.get().getString(R.string.Salemessage), new SettlementRequestClickListener());

            }break;

        }

    }


    private class SettlementRequestClickListener implements DialogInterface.OnClickListener{


        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {

                    processSettlementRequest();

                    dialog.dismiss();

                }
                break;
                case DialogInterface.BUTTON_NEGATIVE: {

                    dialog.dismiss();

                }
                break;
            }

        }
    }


    private void processSettlementRequest(){

        try
        {
            List<JSONMessage> jsonMessages=tableJSONMessage.getAllJSONMessages(0);

            if ( jsonMessages != null && jsonMessages.size() > 0 )
            {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Ordersmessage));
                syncToServer();
                return;
            }

            if(!NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
            {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.internetenablemessage));
                return;
            }

            if(!NetworkUtils.isInternetAvailable())
            {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleasecheckinternetconnectivity));
                return;
            }

            new DaySummaryAsyncTask().execute();


        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            return;
        }

    }


    class DaySummaryAsyncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            ProgressDialogUtils.showProgressDialog("Please wait ...");
        }

        @Override
        protected String doInBackground(Void... params) {

            try
            {

                RootObject rootObject=new RootObject();

                rootObject.setServiceCode(ServiceCode.UPDATE_LOAD_DELIVERY_STATUS);

                rootObject.setLoads(new ArrayList<Load>());

                Load load=new Load();
                load.setRouteAgentId(AppController.getUser().getUserId());
                if (AppController.getUser().getRouteList()!=null && AppController.getUser().getRouteList().size()>0 )
                load.setLoadToId(AppController.getUser().getRouteList().get(0).getRouteId());

                rootObject.getLoads().add(load);

                List<PropertyInfo> propertyInfoList=new ArrayList<>();

                PropertyInfo propertyInfo=new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                return SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_SETTLEMENT_REQUEST);

            }catch (Exception ex){
                Logger.Log(DaySummaryFragment.class.getName(),ex);
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            ProgressDialogUtils.closeProgressDialog();

            if (TextUtils.isEmpty(s))
            {
                DialogUtils.showAlertDialog(getActivity(),"Error while processing settlement request");
                return;

            }else {

                processLoadoutDeliveryStatusResponse(s);

            }

        }
    }


    private void processLoadoutDeliveryStatusResponse(String responseJSON){

        try
        {
            JSONObject jsonObject = new JSONObject(responseJSON);

            JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

            RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

            ExecutionResponse executionResponse = null;

            if (rootObject!=null)
                executionResponse = rootObject.getExecutionResponse();

            if (executionResponse != null) {

                if (executionResponse.getSuccess() == 1) {

                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.SuccessfullyUpdated));

                    tableVehicleLoad.updateVehicleLoadStatus();

                    btnSettlementRequest.setVisibility(Button.GONE);

                }
            }

        }catch (Exception ex){
            Logger.Log(DaySummaryFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while processing settlement request");
            return;
        }

    }

}

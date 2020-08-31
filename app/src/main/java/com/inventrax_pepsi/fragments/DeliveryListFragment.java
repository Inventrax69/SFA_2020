package com.inventrax_pepsi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.DeliveryListAdapter;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.interfaces.DeliveryListView;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class DeliveryListFragment extends Fragment implements DeliveryListView,OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener,View.OnClickListener {

    private TextView txtEmptyView,txtRouteName,txtTotalOrderValue,txtTotalCashCollected;
    private RecyclerView orderListRecyclerView;
    private DeliveryListAdapter orderHistoryListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Invoice> invoiceList;
    private View rootView;
    private SearchView searchView;
    private AppCompatActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableInvoice tableInvoice;
    private TableCustomer tableCustomer;
    private Gson gson;
    private Customer customer;
    private SFACommon sfaCommon;
    private String selectedRoute = "";
    private Spinner spinnerRouteList;
    private CharSequence[] userRouteCharSequences;
    private ArrayList<String> userRouteStringList;
    private RelativeLayout layoutRouteSelection;
    private FloatingActionButton fabCheckout;
    private GPSLocationService gpsLocationService;

    private IntentFilter mIntentFilter;
    private DeliveryListSyncReceiver deliveryListSyncReceiver;
    private BackgroundServiceFactory backgroundServiceFactory;

    private double totalOrderValue=0,totalCashCollected=0;

    public DeliveryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_delivery_list, container, false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });

        activity = (AppCompatActivity) getActivity();

        gpsLocationService = new GPSLocationService(getContext());

        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.deliverylistsyncreceiver");

            deliveryListSyncReceiver=new DeliveryListSyncReceiver();


            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableInvoice = databaseHelper.getTableInvoice();
            tableCustomer = databaseHelper.getTableCustomer();

            if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("customerJSON"))) {
                this.customer = gson.fromJson(getArguments().getString("customerJSON"), Customer.class);
            }


            layoutRouteSelection = (RelativeLayout) rootView.findViewById(R.id.layoutRouteSelection);

            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }

            fabCheckout=(FloatingActionButton)rootView.findViewById(R.id.fabCheckout);
            fabCheckout.setOnClickListener(this);

            fabCheckout.setVisibility(FloatingActionButton.GONE);

            SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            if (getArguments() != null && getArguments().getInt("CustomerId") != 0) {
                layoutRouteSelection.setVisibility(RelativeLayout.GONE);
                fabCheckout.setVisibility(FloatingActionButton.VISIBLE);
            }

            txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);

            txtTotalOrderValue = (TextView)rootView.findViewById(R.id.txtTotalOrderValue);
            txtTotalCashCollected = (TextView)rootView.findViewById(R.id.txtTotalCashCollected);

            orderListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_delivery_list);

            orderListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            orderListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_delivery_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            invoiceList = new ArrayList<Invoice>();

            loadDeliveryList();

            ProgressDialogUtils.closeProgressDialog();

            // check if GPS enabled
            if (!gpsLocationService.canGetLocation()) {
                // Ask user to enable GPS/network in settings
                gpsLocationService.showSettingsAlert();
                return;
            }



        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing delivery list");
            return;

        }
    }


    private void displayDeliveryList() {
        try {

            if ( invoiceList != null && invoiceList.size()>0 ) {

                txtTotalOrderValue.setText("Total Order's Value : " + getString(R.string.Rs) + " " + NumberUtils.formatValue(totalOrderValue));

                txtTotalCashCollected.setText("");

                if (AppController.getUser().getUserTypeId() == 7) {
                    txtTotalCashCollected.setText(" , Total Cash Collected : " + getString(R.string.Rs) + " " + NumberUtils.formatValue(totalCashCollected));
                }

            }else {

                txtTotalOrderValue.setText("");
                txtTotalCashCollected.setText("");
            }

            // create an Object for Adapter
            orderHistoryListAdapter = new DeliveryListAdapter(getActivity(), invoiceList, orderListRecyclerView,this);

            // set the adapter object to the Recyclerview
            orderListRecyclerView.setAdapter(orderHistoryListAdapter);
            //  mAdapter.notifyDataSetChanged();

            if (invoiceList.isEmpty()) {
                orderListRecyclerView.setVisibility(View.GONE);
                orderListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                orderListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

            orderHistoryListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {
            Logger.Log(DeliveryListFragment.class.getName(), ex);
            return;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

            final MenuItem item = menu.findItem(R.id.cust_action_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setVisible(true);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);

            final MenuItem menuItemSyncOrderInfo = menu.findItem(R.id.action_sync_order_info);
            menuItemSyncOrderInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemSyncOrderInfo.setVisible(true);

            menuItemSyncOrderInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(!NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                        DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.internetenablemessage));
                        return false;
                    }

                    if(backgroundServiceFactory!=null)
                        backgroundServiceFactory.initiateInvoiceUpdateService();

                    DialogUtils.showAlertDialog(getActivity(),"Syncing Order Info. Please Wait ...");

                    refreshContent();

                    return false;
                }
            });


        }

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {

        final List<Invoice> filteredModelList = filter(invoiceList, searchText);

        orderHistoryListAdapter.setDeliveryListAdapter(getActivity(), filteredModelList, orderListRecyclerView,this);
        orderHistoryListAdapter.notifyDataSetChanged();  // data set changed

        if (TextUtils.isEmpty(searchText)) {
            orderHistoryListAdapter.setDeliveryListAdapter(getActivity(), invoiceList, orderListRecyclerView,this);
            orderHistoryListAdapter.notifyDataSetChanged();
        }

        return true;
    }

    private List<Invoice> filter(List<Invoice> models, String query) {

        final List<Invoice> filteredModelList = new ArrayList<>();

        try {
            query = query.toLowerCase();


            for (Invoice model : models) {
                if (model != null) {
                    //if (( model.getCustomerName().toLowerCase().contains(query.trim()) ||  model.getCustomerCode().toLowerCase().contains(query.trim()) || (model.getInvoiceOrders() != null && model.getInvoiceOrders().size() > 0) ? model.getInvoiceOrders().get(0).getOrderCode().toLowerCase().contains(query.trim()) : false)) {
                    if(model.getCustomerName().toLowerCase().contains(query.trim()) || model.getCustomerCode().toLowerCase().contains(query.trim()))
                    {
                        filteredModelList.add(model);
                    }

                }
            }
        } catch (Exception ex) {

        }

        return filteredModelList;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(deliveryListSyncReceiver, mIntentFilter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_delivery_list));

        if (getArguments() != null && getArguments().getInt("CustomerId") != 0) {
            sfaCommon.displayUserInfo(getActivity(), customer, "");
        } else {
            sfaCommon.hideUserInfo(getActivity());
        }


    }

    @Override
    public void onRefresh() {

        refreshContent();
        swipeRefreshLayout.setRefreshing(false);

    }

    private void refreshContent() {
        try {

            ProgressDialogUtils.showProgressDialog();

            loadDeliveryList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
        }
    }

    @Override
    public void onLoadMore() {

    }


    private void loadDeliveryList() {

        try {

            totalCashCollected=0;
            totalOrderValue=0;

            invoiceList.clear();

            Invoice invoice = null;

            List<DeliveryInvoice> deliveryInvoiceList = tableInvoice.getAllInvoicesByRoute(selectedRoute, getArguments() == null ? 0 : getArguments().getInt("CustomerId"));

            for (DeliveryInvoice deliveryInvoice : deliveryInvoiceList) {

                invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);

                invoiceList.add(invoice);

                if (invoice.getOrderStatusId() <= OrderStatus.Partial.getStatus() || invoice.getOrderStatusId() == OrderStatus.Completed.getStatus() )
                    totalOrderValue+=invoice.getNetAmount();

                if ( invoice.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {
                    if (invoice.getPaymentInfo() != null)
                        totalCashCollected += invoice.getPaymentInfo().getAmount();
                }
            }

            displayDeliveryList();

        } catch (Exception ex) {
            Logger.Log(DeliveryListFragment.class.getName(), ex);
            return;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinnerRouteList: {

                selectedRoute = spinnerRouteList.getSelectedItem().toString();

                if (AppController.mapUserRoutes!=null)
                    txtRouteName.setText(AppController.mapUserRoutes.get(selectedRoute));

                try {
                    ProgressDialogUtils.showProgressDialog();

                    loadDeliveryList();

                    ProgressDialogUtils.closeProgressDialog();

                } catch (Exception ex) {
                    ProgressDialogUtils.closeProgressDialog();
                }


            }
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(deliveryListSyncReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fabCheckout:{


                DialogUtils.showConfirmDialog(getActivity(), "", AbstractApplication.get().getString(R.string.checkoutmessage), AbstractApplication.get().getString(R.string.Yes), AbstractApplication.get().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE ){

                            doCheckout();

                            dialog.dismiss();
                        }else
                        {
                            dialog.dismiss();
                        }


                    }
                });



            }break;

        }

    }

    private void doCheckout(){
        try
        {

            if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.locationinformation));
                return;
            }

            if (!sfaCommon.checkInOut(customer, 2, gpsLocationService)) {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.checkoutmessage));
            } else {

                Toast.makeText(getActivity(), AbstractApplication.get().getString(R.string.checkoutmessage), Toast.LENGTH_LONG).show();

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                    backgroundServiceFactory.initiateUserTrackService(customer.getCustomerId(), false);

                FragmentUtils.replaceFragment(getActivity(),R.id.container_body,new DeliveryListFragment());

            }


        }catch (Exception ex){
            Logger.Log(DeliveryListFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void doCheckIn(int customerId) {
        try
        {

            customer = gson.fromJson(tableCustomer.getCustomer(customerId).getCompleteJSON(),Customer.class);

            if (customer == null)
                return;

            if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                DialogUtils.showAlertDialog(getActivity(),  AbstractApplication.get().getString(R.string.locationinformation));
                return;
            }

            if (!sfaCommon.checkInOut(customer, 1, gpsLocationService)) {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Errorwhilecheckin));
            } else {

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                    backgroundServiceFactory.initiateUserTrackService(customerId, true);

                Toast.makeText(getActivity(),AbstractApplication.get().getString(R.string.Youhavesuccessfullycheckedin), Toast.LENGTH_LONG).show();

            }


        }catch (Exception ex){
            Logger.Log(DeliveryListFragment.class.getName(),ex);
            return;
        }
    }

    public class DeliveryListSyncReceiver extends BroadcastReceiver {

        public DeliveryListSyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {

            refreshContent();

        }
    }


}
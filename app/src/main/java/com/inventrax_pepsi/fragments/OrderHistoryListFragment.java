package com.inventrax_pepsi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.OrderHistoryListAdapter;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Order;
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
public class OrderHistoryListFragment extends Fragment implements OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener,View.OnClickListener {

    private TextView txtEmptyView,txtRouteName,txtTotalOrderValue,txtTotalCashCollected;
    private RecyclerView orderListRecyclerView;
    private OrderHistoryListAdapter orderHistoryListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Order> orderList;
    private View rootView;
    private SearchView searchView;
    private AppCompatActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private Gson gson;
    private Customer customer;
    private SFACommon sfaCommon;
    private Spinner spinnerRouteList;
    private String selectedRouteCode = "";
    private CharSequence[] userRouteCharSequences;
    private ArrayList<String> userRouteStringList;
    private RelativeLayout layoutRouteSelection;
    private FloatingActionButton fabReturn;

    private IntentFilter mIntentFilter;
    private OrderHistorySyncReceiver orderHistorySyncReceiver;
    private BackgroundServiceFactory backgroundServiceFactory;

    private double totalOrderValue=0,totalCashCollected=0;
    private Resources resources;

    public OrderHistoryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_history_list, container, false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        doReturn();

                        return true;
                    }
                }
                return false;
            }
        });

        activity = (AppCompatActivity) getActivity();

        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.orderhistorysyncreceiver");

            orderHistorySyncReceiver =new OrderHistorySyncReceiver();

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());


            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableOrder = databaseHelper.getTableOrder();

            if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("customerJSON"))) {
                this.customer = gson.fromJson(getArguments().getString("customerJSON"), Customer.class);
            }

            layoutRouteSelection = (RelativeLayout) rootView.findViewById(R.id.layoutRouteSelection);
            layoutRouteSelection.setVisibility(RelativeLayout.GONE);

            fabReturn=(FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            fabReturn.setVisibility(FloatingActionButton.GONE);

            if (getArguments() != null && getArguments().getInt("customerId") == 0) {

                spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
                spinnerRouteList.setOnItemSelectedListener(this);

                userRouteStringList = new ArrayList<>();

                for (RouteList userRoute : AppController.getUser().getRouteList()) {
                    userRouteStringList.add(userRoute.getRouteCode());
                }

                SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);
            }else {

                // Except RA visible to all
                /*if (AppController.getUser().getUserTypeId()!=7) {
                    fabReturn.setVisibility(FloatingActionButton.VISIBLE);
                }*/

                fabReturn.setVisibility(FloatingActionButton.VISIBLE);
            }

            txtRouteName= (TextView) rootView.findViewById(R.id.txtRouteName);
            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);

            txtTotalOrderValue = (TextView)rootView.findViewById(R.id.txtTotalOrderValue);
            txtTotalCashCollected = (TextView)rootView.findViewById(R.id.txtTotalCashCollected);

            orderListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order_history_list);

            orderListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            orderListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_order_history_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            orderList = new ArrayList<Order>();

            loadOrderHistoryList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(OrderHistoryListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing order list");
            return;

        }
    }

    private void displayOrderList() {
        try {

            if (orderList!=null && orderList.size()>0) {

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
            orderHistoryListAdapter = new OrderHistoryListAdapter(getActivity(), orderList, orderListRecyclerView);

            // set the adapter object to the Recyclerview
            orderListRecyclerView.setAdapter(orderHistoryListAdapter);
            //  mAdapter.notifyDataSetChanged();

            if (orderList.isEmpty()) {
                orderListRecyclerView.setVisibility(View.GONE);
                orderListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                orderListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

            orderHistoryListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {
            Logger.Log(OrderHistoryListFragment.class.getName(), ex);
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
                        backgroundServiceFactory.initiateOrderService();

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

        final List<Order> filteredModelList = filter(orderList, searchText);

        orderHistoryListAdapter.setOrderHistoryListAdapter(getActivity(), filteredModelList, orderListRecyclerView);
        orderHistoryListAdapter.notifyDataSetChanged();  // data set changed

        if (TextUtils.isEmpty(searchText)) {
            orderHistoryListAdapter.setOrderHistoryListAdapter(getActivity(), orderList, orderListRecyclerView);
            orderHistoryListAdapter.notifyDataSetChanged();
        }

        return true;
    }

    private List<Order> filter(List<Order> models, String query) {

        query = query.toLowerCase();

        final List<Order> filteredModelList = new ArrayList<>();

        for (Order model : models) {
            if (model != null) {

                if (model.getCustomerName().toLowerCase().contains(query.trim()) || model.getCustomerCode().toLowerCase().contains(query.trim()) || model.getOrderCode().toLowerCase().contains(query.trim())) {

                    filteredModelList.add(model);

                }
            }
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

        getActivity().registerReceiver(orderHistorySyncReceiver, mIntentFilter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_order_history));

        if (customer != null)
            sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_order_history));
        else {
            sfaCommon.hideUserInfo(getActivity());
            layoutRouteSelection.setVisibility(RelativeLayout.VISIBLE);
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

            loadOrderHistoryList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
        }
    }

    @Override
    public void onLoadMore() {

    }


    private void loadOrderHistoryList() {

        try {
            resources=getResources();

            totalOrderValue=0;
            totalCashCollected=0;

            List<com.inventrax_pepsi.database.pojos.Order> localDBOrderHistoryList = null;

            if (getArguments() != null && getArguments().getInt("customerId") != 0)
                localDBOrderHistoryList = tableOrder.getAllOrdersByCustomerId(getArguments().getInt("customerId"));
            else
                localDBOrderHistoryList = tableOrder.getAllOrdersByRouteCode(selectedRouteCode);

            Order order = null;

            orderList.clear();

            for (com.inventrax_pepsi.database.pojos.Order localDBOrder : localDBOrderHistoryList) {

                order = new Order();

                order = gson.fromJson(localDBOrder.getOrderJSON(), Order.class);
                order.setAutoSyncId(localDBOrder.getAutoIncId());

                /*AuditInfo auditInfo = new AuditInfo();
                auditInfo.setCreatedDate(localDBOrder.getCreatedOn());
                order.setAuditInfo(auditInfo);*/

                if (order.getOrderStatusId() <= OrderStatus.Partial.getStatus() || order.getOrderStatusId() == OrderStatus.Completed.getStatus() )
                totalOrderValue += order.getDerivedPrice();

                if ( order.getOrderStatusId() == OrderStatus.Completed.getStatus() ) {

                    if (order.getPaymentInfo() != null) {
                        totalCashCollected += order.getPaymentInfo().getAmount();
                    }

                }
                orderList.add(order);

            }

            displayOrderList();

        } catch (Exception ex) {
            Logger.Log(OrderHistoryListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading order history");
            return;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinnerRouteList: {

                selectedRouteCode = spinnerRouteList.getSelectedItem().toString();

                if (AppController.mapUserRoutes!=null)
                    txtRouteName.setText(AppController.mapUserRoutes.get(selectedRouteCode));

                try {
                    ProgressDialogUtils.showProgressDialog();

                    loadOrderHistoryList();

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
        getActivity().unregisterReceiver(orderHistorySyncReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fabReturn:{

                doReturn();

            }break;

        }

    }

    private void doReturn(){
        try
        {
            if (getArguments() != null && getArguments().getInt("customerId") != 0 ){

                Bundle bundle = new Bundle();

                OutletDashboardNewFragment outletDashboardFragment = new OutletDashboardNewFragment();
                bundle.putString("customerJSON",getArguments().getString("customerJSON"));
                outletDashboardFragment.setArguments(bundle);

                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletDashboardFragment);


            }

        }catch (Exception ex){
            Logger.Log(OrderHistoryListFragment.class.getName(),ex);
            return;
        }
    }


    public class OrderHistorySyncReceiver extends BroadcastReceiver {

        public OrderHistorySyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {

            refreshContent();

        }
    }



}
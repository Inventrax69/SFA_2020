package com.inventrax_pepsi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.NewOutletListAdapter;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class NewOutletListFragment extends Fragment implements OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private TextView txtEmptyView,txtRouteName;
    private RecyclerView outletListRecyclerView;
    private NewOutletListAdapter outletListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View rootView;
    private SearchView searchView;
    private AppCompatActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private List<Customer> customerList;
    private Gson gson;
    private String selectedRouteCode;
    private TextView txtBottomBar;
    private SFACommon sfaCommon;
    private Spinner spinnerRouteList;
    private CharSequence[] userRouteCharSequences;
    private ArrayList<String> userRouteStringList;
    private FloatingActionButton fabAddNewCustomer;
    private BackgroundServiceFactory backgroundServiceFactory;

    private OutletListSyncReceiver outletListSyncReceiver;
    private IntentFilter mIntentFilter;

    public NewOutletListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_new_outlet_list, container, false);

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

        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.outletlistsyncreceiver");

            outletListSyncReceiver=new OutletListSyncReceiver();

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setContext(getContext());
            backgroundServiceFactory.setActivity(getActivity());

            customerList = new ArrayList<>();


            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }

            SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);
            outletListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_outlet_list_new);

            fabAddNewCustomer = (FloatingActionButton)rootView.findViewById(R.id.fabAddNewCustomer);
            fabAddNewCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(),R.id.container_body,new OutletRegistrationFragment());

                }
            });

            outletListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            outletListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_outlet_list_new_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            loadOutletList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(NewOutletListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Errorwhileinitializingoutletlist));
            return;

        }
    }


    private void displayOutletList() {
        try {
            // create an Object for Adapter
            outletListAdapter = new NewOutletListAdapter(getActivity(), customerList,getActivity());

            // set the adapter object to the Recyclerview
            outletListRecyclerView.setAdapter(outletListAdapter);
            //  mAdapter.notifyDataSetChanged();

            if (customerList.isEmpty()) {
                outletListRecyclerView.setVisibility(View.GONE);
                outletListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                outletListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Logger.Log(NewOutletListFragment.class.getName(),ex);
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


            final MenuItem menuItemSyncOutletInfo = menu.findItem(R.id.action_sync_order_info);
            menuItemSyncOutletInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemSyncOutletInfo.setVisible(true);

            menuItemSyncOutletInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(!NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                        DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.internetenablemessage));
                        return false;
                    }

                    if(backgroundServiceFactory!=null)
                        backgroundServiceFactory.initiateCustomerService();

                    DialogUtils.showAlertDialog(getActivity(),"Syncing Outlet Info. Please Wait ...");

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

        final List<Customer> filteredModelList = filter(customerList, searchText);

        try {
            outletListAdapter.setNewOutletListAdapter(getActivity(), filteredModelList,getActivity());
            outletListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                outletListAdapter.setNewOutletListAdapter(getActivity(), customerList,getActivity());
                outletListAdapter.notifyDataSetChanged();
            }

            return true;
        }catch (Exception ex){
            Logger.Log(NewOutletListFragment.class.getName(),ex);
            return false;
        }
    }

    private List<Customer> filter(List<Customer> models, String query) {

        query = query.toLowerCase();

        final List<Customer> filteredModelList = new ArrayList<>();
        try {

            for (Customer model : models) {
                if (model != null) {
                    if (model.getCustomerCode().toLowerCase().contains(query.trim()) || (query.trim().length() < model.getCustomerName().length() ? model.getCustomerName().toLowerCase().contains(query.trim()) : false) || model.getMobileNo1().toLowerCase().contains(query.trim())) {
                        filteredModelList.add(model);
                    }
                }
            }

            return filteredModelList;

        }catch (Exception ex){
            Logger.Log(NewOutletListFragment.class.getName(),ex);
            return filteredModelList;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(outletListSyncReceiver, mIntentFilter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_new_outlet_list));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        sfaCommon.hideUserInfo(getActivity());

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(outletListSyncReceiver);
        super.onPause();
    }

    @Override
    public void onRefresh() {

        refreshContent();
        swipeRefreshLayout.setRefreshing(false);

    }

    private void refreshContent() {

        loadOutletList();

    }

    @Override
    public void onLoadMore() {

    }

    private void loadOutletList() {

        try {

            List<com.inventrax_pepsi.database.pojos.Customer> localDBCustomerList = tableCustomer.getAllNewCustomersByRoute(selectedRouteCode);

            customerList.clear();

            for (com.inventrax_pepsi.database.pojos.Customer localCustomer : localDBCustomerList) {

                Customer customer=gson.fromJson(localCustomer.getCompleteJSON(), Customer.class);
                customer.setAutoIncId(customer.getAutoIncId());

                customerList.add(customer);
            }

            displayOutletList();


        } catch (Exception ex) {
            Logger.Log(NewOutletListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading outlet list");
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

                    loadOutletList();

                    ProgressDialogUtils.closeProgressDialog();

                } catch (Exception ex) {
                    Logger.Log(NewOutletListFragment.class.getName(),ex);
                    ProgressDialogUtils.closeProgressDialog();
                }


            }
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class OutletListSyncReceiver extends BroadcastReceiver {

        public OutletListSyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {

            refreshContent();

        }
    }


}
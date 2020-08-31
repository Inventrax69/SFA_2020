package com.inventrax_pepsi.fragments;

import android.content.res.Resources;
import android.os.Bundle;
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
import com.inventrax_pepsi.adapters.OutletListNewAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class OutletListNewFragment extends Fragment implements OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private TextView txtEmptyView,txtRouteName,txtTotalOutlets,txtPCIOutlets,txtCCXOutlets,txtMIXOutlets;
    private RecyclerView outletListRecyclerView;
    private OutletListNewAdapter outletListAdapter;
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
    private int totalOutlets=0,pciOutlets=0,ccxOutlets=0,mixOutlets=0;
    private Resources resources;

    public OutletListNewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_outlet_list_new, container, false);

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

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();

            customerList = new ArrayList<>();


            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }
            resources=getResources();

            SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);

            txtTotalOutlets = (TextView) rootView.findViewById(R.id.txtTotalOutlets);
            txtPCIOutlets = (TextView) rootView.findViewById(R.id.txtPCIOutlets);
            txtCCXOutlets = (TextView) rootView.findViewById(R.id.txtCCXOutlets);
            txtMIXOutlets = (TextView) rootView.findViewById(R.id.txtMIXOutlets);

            outletListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_outlet_list_new);

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
            Logger.Log(OutletListNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing outlet list");
            return;

        }
    }


    private void displayOutletList() {
        try {

            txtTotalOutlets.setText(resources.getString(R.string.Total)+ totalOutlets );
            txtPCIOutlets.setText(resources.getString(R.string.PCI)+ pciOutlets);
            txtCCXOutlets.setText(resources.getString(R.string.CCX)+ ccxOutlets);
            txtMIXOutlets.setText(resources.getString(R.string.MIX) + mixOutlets );

            // create an Object for Adapter
            outletListAdapter = new OutletListNewAdapter(getActivity(), customerList, outletListRecyclerView);

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

            outletListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {
            Logger.Log(OutletListNewFragment.class.getName(),ex);
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
            outletListAdapter.setOutletListNewAdapter(getActivity(), filteredModelList, outletListRecyclerView);
            outletListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                outletListAdapter.setOutletListNewAdapter(getActivity(), customerList, outletListRecyclerView);
                outletListAdapter.notifyDataSetChanged();
            }

            return true;
        }catch (Exception ex){
            Logger.Log(OutletListNewFragment.class.getName(),ex);
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
            Logger.Log(OutletListNewFragment.class.getName(),ex);
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_outlet_list));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        sfaCommon.hideUserInfo(getActivity());

    }

    @Override
    public void onRefresh() {

        refreshContent();
        swipeRefreshLayout.setRefreshing(false);

    }

    private void refreshContent() {

    }

    @Override
    public void onLoadMore() {

        /*try
        {

            customerList.add(null);
            outletListAdapter.notifyItemInserted(customerList.size() - 1);

            //Load more data for reyclerview
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //Remove loading item
                    customerList.remove(customerList.size() - 1);
                    outletListAdapter.notifyItemRemoved(customerList.size());

                    //Load data
                    int index = customerList.size();
                    int end = index + 20;
                    for (int i = index; i < end; i++) {


                    }
                    outletListAdapter.notifyDataSetChanged();
                    outletListAdapter.setLoaded();
                }
            }, 5000);


        }catch (Exception ex){

        }*/




    }

    private void loadOutletList() {

        try {

            List<com.inventrax_pepsi.database.pojos.Customer> localDBCustomerList = tableCustomer.getAllCustomersByRoute(selectedRouteCode);

            customerList.clear();

            pciOutlets=0;ccxOutlets=0;mixOutlets=0;totalOutlets=0;

            for (com.inventrax_pepsi.database.pojos.Customer customer : localDBCustomerList) {

                Customer mCustomer=gson.fromJson(customer.getCompleteJSON(), Customer.class);

                if (mCustomer.getOutletProfile()!=null){

                    switch (mCustomer.getOutletProfile().getAccountType().toLowerCase())
                    {
                        case "pci":{pciOutlets++;}break;
                        case "ccx":{ccxOutlets++;}break;
                        case "mix":{mixOutlets++;}break;
                    }
                }

                customerList.add(mCustomer);
            }

            totalOutlets = customerList.size();

            displayOutletList();


        } catch (Exception ex) {
            Logger.Log(OutletListNewFragment.class.getName(), ex);
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
                    Logger.Log(OutletListNewFragment.class.getName(),ex);
                    ProgressDialogUtils.closeProgressDialog();
                }


            }
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
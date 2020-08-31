package com.inventrax_pepsi.fragments;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.DiscountListAdapter;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerDiscount;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class DiscountListFragment extends Fragment implements OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private View rootView;
    private List<CustomerDiscount> customerDiscountList;
    private TextView txtEmptyView;
    private RecyclerView discountListRecyclerView;
    private DiscountListAdapter discountListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableCustomerDiscount tableCustomerDiscount;
    private Gson gson;
    private Customer customer;
    private SFACommon sfaCommon;
    private FloatingActionButton fabReturn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_discount_list, container, false);


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



        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            gson = new GsonBuilder().create();

            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomerDiscount = databaseHelper.getTableCustomerDiscount();

            customerDiscountList = new ArrayList<>();

            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);
            discountListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_discount_list);

            discountListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            discountListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_discount_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            fabReturn = (FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            loadDiscountList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DiscountListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing discount list");
            return;

        }
    }


    private void displayDiscountList() {
        try {
            // create an Object for Adapter
            discountListAdapter = new DiscountListAdapter(getActivity(), customerDiscountList, discountListRecyclerView);

            // set the adapter object to the Recyclerview
            discountListRecyclerView.setAdapter(discountListAdapter);
            //  mAdapter.notifyDataSetChanged();

            if (customerDiscountList.isEmpty()) {
                discountListRecyclerView.setVisibility(View.GONE);
                discountListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                discountListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

            discountListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {

            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_discount_list));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_discount_list));
        //sfaCommon.displayDate(getActivity());
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
    public void onLoadMore() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        final List<CustomerDiscount> filteredModelList = filter(customerDiscountList, searchText);

        discountListAdapter.setOutletListAdapter(getActivity(), filteredModelList, discountListRecyclerView);
        discountListAdapter.notifyDataSetChanged();  // data set changed

        if (TextUtils.isEmpty(searchText)) {
            discountListAdapter.setOutletListAdapter(getActivity(), customerDiscountList, discountListRecyclerView);
            discountListAdapter.notifyDataSetChanged();
        }

        return true;
    }


    private List<CustomerDiscount> filter(List<CustomerDiscount> models, String query) {

        query = query.toLowerCase();

        final List<CustomerDiscount> filteredModelList = new ArrayList<>();

        for (CustomerDiscount model : models) {
            if (model != null) {
                if (model.getDiscountCode().toLowerCase().contains(query.trim()) || model.getDiscountType().toLowerCase().contains(query.trim()) || model.getDiscountName().toLowerCase().contains(query.trim()) || model.getTargetItemName().toLowerCase().contains(query.trim())) {
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
    public void onRefresh() {

        refreshContent();
        swipeRefreshLayout.setRefreshing(false);

    }

    private void refreshContent() {
       /* try {

            ProgressDialogUtils.showProgressDialog();

            loadDiscountList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
        }*/
    }

    private void loadDiscountList() {

        try {

            CustomerDiscount customerDiscount = null;

            customerDiscountList.clear();

            List<com.inventrax_pepsi.database.pojos.CustomerDiscount> localCustomerDiscountList = tableCustomerDiscount.getAllCustomerDiscountsByCustomerId(getArguments().getInt("CustomerId"));


            for (com.inventrax_pepsi.database.pojos.CustomerDiscount localDBCustomerDiscount : localCustomerDiscountList) {

                customerDiscount = gson.fromJson(localDBCustomerDiscount.getDiscountJSON(), CustomerDiscount.class);

                customerDiscountList.add(customerDiscount);
            }

            displayDiscountList();

        } catch (Exception ex) {
            Logger.Log(DiscountListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading discount list");
            return;
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabReturn: {

                doReturn();

            }
            break;
        }

    }

    private void doReturn(){
        try
        {
            Bundle bundle=new Bundle();
            bundle.putString("customerJSON",getArguments().getString("customerJSON"));
            OutletDashboardNewFragment outletDashboardNewFragment=new OutletDashboardNewFragment();
            outletDashboardNewFragment.setArguments(bundle);

            FragmentUtils.replaceFragmentWithBackStack(getActivity(),R.id.container_body,outletDashboardNewFragment);

        }catch (Exception ex){
            Logger.Log(AssetComplaintFragment.class.getName(),ex);
            return;
        }
    }

}

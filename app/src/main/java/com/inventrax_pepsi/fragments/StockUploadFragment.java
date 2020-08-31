package com.inventrax_pepsi.fragments;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.OutletListNewAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerAuditInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class StockUploadFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private LinearLayoutManager linearLayoutManager;
    private View rootView;

    private TextView txtSelectedDepot, txtGo;

    private AppCompatActivity activity;
    private FragmentUtils fragmentUtils;
    private DatabaseHelper databaseHelper;

    private Gson gson;
    private SFACommon sfaCommon;
    private Spinner spinnerRouteList;

    private Resources resources;
    Customer customerDto;
    private String customerIdSelected = "";
    ArrayList<String> customerNames;
    ArrayList<String> customerIds;

    public StockUploadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_stock_upload, container, false);

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
        //this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);
        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            gson = new GsonBuilder().create();
            fragmentUtils = new FragmentUtils();

            databaseHelper = DatabaseHelper.getInstance();

            spinnerRouteList = (Spinner) rootView.findViewById(R.id.spinnerRouteList);
            spinnerRouteList.setOnItemSelectedListener(this);

            txtSelectedDepot = (TextView) rootView.findViewById(R.id.txtSelectedDepot);
            txtGo = (TextView) rootView.findViewById(R.id.txtGo);

            txtGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customerIdSelected != "") {
                        Bundle bundleDepotId = new Bundle();
                        bundleDepotId.putInt("customerIdSelected", Integer.parseInt(customerIdSelected));
                        Fragment fragment = null;
                        fragment = new StockUploadListFragment();
                        fragment.setArguments(bundleDepotId);
                        fragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, fragment);
                    } else {
                        DialogUtils.showAlertDialog(getActivity(), "Please select customer");
                    }

                }
            });

            resources = getResources();
            getUserAssginedDepots();
            //SpinnerUtils.getSpinner(getActivity(), "Select Route", spinnerRouteList, userRouteStringList);

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(StockUploadFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing outlet list");
            return;

        }
    }

    public void getUserAssginedDepots() {


        customerDto = new Customer();


        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String s = GetUserAssginedDepots();
                return s;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                depotList(response);
                //ProgressDialogUtils.closeProgressDialog();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //ProgressDialogUtils.showProgressDialog();
            }
        }.execute();


    }


    private String GetUserAssginedDepots() {
        String result = null;
        try {

            User user = AppController.getUser();
            Customer customer = new Customer();
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setUserId(user.getUserId());
            customer.setAuditInfo(auditInfo);

            List<Customer> cust = new ArrayList<>();
            cust.add(customer);

            if (cust.size() > 0) {


                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_USER_ASSIGNED_DEPOTS);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setCustomers(cust);
                //rootObject.setAsset(customerAuditInfo);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                result = String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.GET_USER_ASSIGNED_DEPOTS));

                //Log.d("JsonRes", String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.GET_USER_ASSIGNED_DEPOTS)));

            }

        } catch (Exception ex) {
            Logger.Log("Exception", ex);
        }

        return result;
    }


    private void depotList(String responseJSON) {


        try {

            if (!TextUtils.isEmpty(responseJSON)) {

                JSONObject jsonObject = new JSONObject(responseJSON);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse = null;

                if (rootObject != null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse != null) {

                    if (executionResponse.getSuccess() == 1) {

                        List<Customer> customers = new ArrayList<>();
                        customerNames = new ArrayList();
                        customerIds = new ArrayList();


                        for (int i = 0; i <= rootObject.getCustomers().size() - 1; i++) {

                            Customer customer = rootObject.getCustomers().get(i);
                            customers.add(customer);
                            customerNames.add(customer.getCustomerName());
                            customerIds.add(String.valueOf(customer.getCustomerId()));
                        }

                        SpinnerUtils.getSpinner(getActivity(), "Select Depot", spinnerRouteList, customerNames);

                    }

                }

            }

        } catch (Exception ex) {
            Logger.Log(AssetAuditFragment.class.getName(), ex);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

            /*final MenuItem item = menu.findItem(R.id.cust_action_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setVisible(true);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);*/
        }

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_item_stock_upload));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        sfaCommon.hideUserInfo(getActivity());

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int customerName = spinnerRouteList.getSelectedItemPosition();

        customerIdSelected = customerIds.get(customerName);
        txtGo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
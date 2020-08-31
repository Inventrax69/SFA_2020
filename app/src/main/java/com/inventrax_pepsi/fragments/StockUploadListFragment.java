package com.inventrax_pepsi.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.SKUListAdapterNew;
import com.inventrax_pepsi.adapters.StockListAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.sfa.pojos.ActiveStock;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.Load;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Padmaja.b on 13/8/2020.
 */
public class StockUploadListFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {


    private RecyclerView skuListRecyclerView;
    private StockListAdapter stockListAdapter;

    private LinearLayoutManager linearLayoutManager;

    private View rootView;
    private SearchView searchView;
    private AppCompatActivity activity;
    private DatabaseHelper databaseHelper;

    private Gson gson;
    private Button btnUpdate;
    private RelativeLayout skuLayoutBottomBar;
    private TextView txtSKUsModified;


    private MaterialDialog materialSchemeOfferItemsDialog;

    private Resources resources;
    private Spinner brand_spinner;

    private SFACommon sfaCommon;
    private ArrayList<String> brandList;

    List<ActiveStock> activeStockList;

    private int selectedCustomerID = 0;


    public StockUploadListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_stock_upload_list, container, false);

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
            resources = getResources();

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();

            skuListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_sku_list);

            btnUpdate = (Button) rootView.findViewById(R.id.btnUpdate);

            txtSKUsModified = (TextView) rootView.findViewById(R.id.txtSKUsModified);

            skuLayoutBottomBar = (RelativeLayout) rootView.findViewById(R.id.skuLayoutBottomBar);

            skuListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            skuListRecyclerView.setLayoutManager(linearLayoutManager);

            if (getArguments() != null && getArguments().getInt("customerIdSelected") != 0) {
                selectedCustomerID= getArguments().getInt("customerIdSelected",0);
            }

            brand_spinner = (Spinner) rootView.findViewById(R.id.brand_spinner);
            brand_spinner.setOnItemSelectedListener(this);

            getActiveStockDepotWiseAsync();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(StockUploadListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing sku list");
            return;
        }
    }

    private void displaySKUList(List<ActiveStock> activeStockList) {

        try {
            // create an Object for Adapter
            stockListAdapter = new StockListAdapter(getActivity(),activeStockList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,txtSKUsModified,selectedCustomerID);

            // set the adapter object to the Recyclerview
            skuListRecyclerView.setAdapter(stockListAdapter);
            //  mAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            Logger.Log(StockUploadListFragment.class.getName(), ex);
            return;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getActiveStockDepotWiseAsync() {


        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String s = GetActiveStockDepotWise();
                return s;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                activeStock(response);
                //ProgressDialogUtils.closeProgressDialog();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //ProgressDialogUtils.showProgressDialog();
            }
        }.execute();


    }

    private String GetActiveStockDepotWise() {

        String result = null;
        try {

            User user = AppController.getUser();

            Load load = new Load();
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setUserId(user.getUserId());
            load.setStoreId(selectedCustomerID);
            load.setAuditInfo(auditInfo);

            List<Load> loadList = new ArrayList<>();
            loadList.add(load);

            if (loadList.size() > 0) {


                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.GET_ACTIVE_STOCK_DEPOTWISE);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setLoads(loadList);
                //rootObject.setAsset(customerAuditInfo);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                result = String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.GET_ACTIVE_STOCK_DEPOTWISE));

                //Log.d("JsonRes", String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.GET_ACTIVE_STOCK_DEPOTWISE)));

            }

        } catch (Exception ex) {
            Logger.Log("Exception", ex);
        }

        return result;

    }

    private void activeStock(String responseJSON) {

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

                        activeStockList = new ArrayList<>();
                        brandList = new ArrayList<>();

                        brandList.add("All");

                        stockListAdapter = new StockListAdapter();
                        //stockListAdapter = new StockListAdapter(getActivity(),activeStockList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,selectedCustomerID);
                        skuListRecyclerView.setAdapter(stockListAdapter);

                        for (int i = 0; i < rootObject.getActiveStockList().size() ; i++) {

                            ActiveStock activeStock = rootObject.getActiveStockList().get(i);
                            activeStockList.add(activeStock);
                            brandList.add(activeStock.getItemCode());

                        }

                        brand_spinner = SpinnerUtils.getSpinner(getActivity(), "", brand_spinner, brandList);
                        displaySKUList(activeStockList);

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

            final MenuItem item = menu.findItem(R.id.cust_action_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setVisible(true);

            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);
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

      /*  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.BookOrder));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.BookOrder));*/
        // sfaCommon.displayDate(getActivity());
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        try {

            final List<ActiveStock> filteredModelList = filter(activeStockList, searchText);

            stockListAdapter.setStockSKUListAdapter(getActivity(),filteredModelList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,txtSKUsModified,selectedCustomerID);
            stockListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                stockListAdapter.setStockSKUListAdapter(getActivity(),activeStockList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,txtSKUsModified,selectedCustomerID);
                stockListAdapter.notifyDataSetChanged();
            }

            return true;

        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return false;
        }
    }

    private List<ActiveStock> filter(List<ActiveStock> models, String query) {
        final List<ActiveStock> filteredModelList = new ArrayList<>();
        try {

            query = query.toLowerCase();



            for (ActiveStock model : models) {
                if (model != null) {

                    if (model.getItemCode().toLowerCase().contains(query.trim()) || model.getItemCode().toLowerCase().contains(query.trim())) {
                        filteredModelList.add(model);
                    }
                }
            }

            return filteredModelList;

        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return filteredModelList;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        searchBrandList(brand_spinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void searchBrandList(String brandSelected) {
        try {
            final List<ActiveStock> filteredModelList = filterBrandPackList(activeStockList, brandSelected);

            stockListAdapter.setStockSKUListAdapter(getActivity(),filteredModelList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,txtSKUsModified,selectedCustomerID);
            stockListAdapter.notifyDataSetChanged();  // data set changed

            if (brandSelected.equalsIgnoreCase("All") && brandSelected.equalsIgnoreCase("All")) {
                stockListAdapter.setStockSKUListAdapter(getActivity(),activeStockList,skuListRecyclerView,skuLayoutBottomBar,btnUpdate,txtSKUsModified,selectedCustomerID);
                stockListAdapter.notifyDataSetChanged();
            }


        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return;
        }
    }

    private List<ActiveStock> filterBrandPackList(List<ActiveStock> models, String brandName) {

        try {

            final List<ActiveStock> filteredModelList = new ArrayList<>();

            for (ActiveStock model : models) {
                if (model != null) {

                    if ((brandName.equalsIgnoreCase("All") || brandName.equalsIgnoreCase(model.getItemCode())) ) {
                        filteredModelList.add(model);
                    }

                }
            }

            return filteredModelList;
        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return null;
        }

    }

}

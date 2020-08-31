package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.inventrax_pepsi.adapters.VehicleStockListAdapter;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableVehicleLoad;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 13-Mar-16.
 */
public class VehicleStockFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private View rootView;
    private LinearLayoutManager linearLayoutManager;
    private VehicleStockListAdapter vehicleStockListAdapter;
    private RecyclerView recyclerView;
    private List<VehicleStock> vehicleStockList = null;
    private SFACommon sfaCommon;
    private DatabaseHelper databaseHelper;
    private TableVehicleStock tableVehicleStock;
    private TableVehicleLoad tableVehicleLoad;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtStockInfo;
    private int numberOfCases = 0, numberOfBottles = 0;
    private Spinner spinnerBrandNames, spinnerPack;
    private ArrayList<String> brandList;
    private ArrayList<String> packList;
    private TableItem tableItem;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vehicle_stock_list, container, false);

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
            tableVehicleStock = databaseHelper.getTableVehicleStock();
            tableVehicleLoad = databaseHelper.getTableVehicleLoad();
            tableItem = databaseHelper.getTableItem();

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_vehicle_stock_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);


            txtStockInfo = (TextView) rootView.findViewById(R.id.txtStockInfo);

            vehicleStockList = new ArrayList<>();

            vehicleStockList = tableVehicleStock.getAllVehicleStocks();

            txtStockInfo.setText((tableVehicleStock.getCaseQuantity() != 0 ? " CS/" + (int) tableVehicleStock.getCaseQuantity() : "") + "  " + (tableVehicleStock.getBottleQuantity() != 0 ? "  FB/" + (int) tableVehicleStock.getBottleQuantity() + "  " : "") + " ");

            loadStockItems();

            loadSKUList();

            spinnerBrandNames = (Spinner) rootView.findViewById(R.id.action_brand_spinner);
            spinnerBrandNames = SpinnerUtils.getSpinner(getActivity(), "", spinnerBrandNames, brandList);
            spinnerBrandNames.setOnItemSelectedListener(this);

            spinnerPack = (Spinner) rootView.findViewById(R.id.action_pack_spinner);
            spinnerPack = SpinnerUtils.getSpinner(getActivity(), "", spinnerPack, packList);
            spinnerPack.setOnItemSelectedListener(this);


            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(VehicleStockFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }


    private void loadStockItems() {

        try {

            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_vehicle_stock_list);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            vehicleStockListAdapter = new VehicleStockListAdapter(getContext(), vehicleStockList);
            recyclerView.setAdapter(vehicleStockListAdapter);

        } catch (Exception ex) {
            Logger.Log(VehicleStockFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.StockDetails));

        sfaCommon.displayDate(getActivity());

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {

        try {

            final List<VehicleStock> filteredModelList = filter(vehicleStockList, searchText, "All");

            vehicleStockListAdapter.setVehicleStockListAdapter(getActivity(), filteredModelList);
            vehicleStockListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                vehicleStockListAdapter.setVehicleStockListAdapter(getActivity(), vehicleStockList);
                vehicleStockListAdapter.notifyDataSetChanged();
            }

            return true;
        }catch (Exception ex){
            Logger.Log(VehicleStockFragment.class.getName(),ex);
            return false;
        }
    }

    private List<VehicleStock> filter(List<VehicleStock> models, String brandName, String packName) {

        try {

            brandName = brandName.toLowerCase();
            packName = packName.toLowerCase();

            final List<VehicleStock> filteredModelList = new ArrayList<>();

            for (VehicleStock model : models) {
                if (model != null) {

                    if ((brandName.equalsIgnoreCase("All") || model.getItemName().toLowerCase().contains(brandName.trim())) && (packName.equalsIgnoreCase("All") || model.getItemName().toLowerCase().contains(packName.trim()))) {
                        // && model.getItemName().toLowerCase().contains(brandName.trim())
                        filteredModelList.add(model);

                    }
                }
            }

            return filteredModelList;

        }catch (Exception ex){

            Logger.Log(VehicleStockFragment.class.getName(),ex);
            return null;

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onRefresh() {

        try {

            if (vehicleStockList != null) {
                vehicleStockList.clear();
                vehicleStockList = tableVehicleStock.getAllVehicleStocks();
                vehicleStockListAdapter.setVehicleStockListAdapter(getActivity(), vehicleStockList);
                vehicleStockListAdapter.notifyDataSetChanged();  // data set changed

                searchBrandPackList();
            }

            swipeRefreshLayout.setRefreshing(false);

        }catch (Exception ex){
            Logger.Log(VehicleStockFragment.class.getName(),ex);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.action_brand_spinner: {


                if (spinnerBrandNames != null && spinnerPack != null) {

                    searchBrandPackList();

                }

            }
            break;

            case R.id.action_pack_spinner: {

                if (spinnerBrandNames != null && spinnerPack != null) {

                    searchBrandPackList();

                }

            }
            break;

        }

    }

    private void searchBrandPackList() {
        try {

            final List<VehicleStock> filteredModelList = filter(vehicleStockList, spinnerBrandNames.getSelectedItem().toString(), spinnerPack.getSelectedItem().toString());

            vehicleStockListAdapter.setVehicleStockListAdapter(getActivity(), filteredModelList);
            vehicleStockListAdapter.notifyDataSetChanged();  // data set changed

            if (spinnerBrandNames.getSelectedItem().toString().equalsIgnoreCase("All") && spinnerPack.getSelectedItem().toString().equalsIgnoreCase("All")) {
                vehicleStockListAdapter.setVehicleStockListAdapter(getActivity(), vehicleStockList);
                vehicleStockListAdapter.notifyDataSetChanged();
            }

        } catch (Exception ex) {
            Logger.Log(VehicleStockFragment.class.getName(),ex);
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void loadSKUList() {

        try {

            brandList = new ArrayList<>();
            brandList.add("All");
            brandList.addAll(tableItem.getAllItemBrands());
            packList = new ArrayList<>();
            packList.add("All");
            packList.addAll(tableItem.getAllItemPacks());

        } catch (Exception ex) {

            Logger.Log(VehicleStockFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading list");
            return;

        }

    }
}

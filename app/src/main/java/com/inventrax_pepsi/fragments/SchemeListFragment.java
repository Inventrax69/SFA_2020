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
import com.inventrax_pepsi.adapters.SchemeListAdapter;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */
public class SchemeListFragment extends Fragment implements OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener , AdapterView.OnItemSelectedListener {

    private View rootView;
    private List<Scheme> schemeList;
    private TextView txtEmptyView;
    private RecyclerView schemeListRecyclerView;
    private SchemeListAdapter schemeListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableScheme tableScheme;
    private Gson gson;
    private SFACommon sfaCommon;
    private Spinner spinnerBrandNames, spinnerPack;
    private ArrayList<String> brandList;
    private ArrayList<String> packList;
    private TableItem tableItem;

   // private BackgroundServiceFactory backgroundServiceFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_scheme_list, container, false);

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
            tableScheme = databaseHelper.getTableScheme();
            tableItem = databaseHelper.getTableItem();

            /*backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());*/

            schemeList = new ArrayList<>();

            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);
            schemeListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_scheme_list);

            schemeListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            schemeListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_scheme_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            loadSKUList();

            spinnerBrandNames = (Spinner) rootView.findViewById(R.id.action_brand_spinner);
            spinnerBrandNames = SpinnerUtils.getSpinner(getActivity(), "", spinnerBrandNames, brandList);
            spinnerBrandNames.setOnItemSelectedListener(this);

            spinnerPack = (Spinner) rootView.findViewById(R.id.action_pack_spinner);
            spinnerPack = SpinnerUtils.getSpinner(getActivity(), "", spinnerPack, packList);
            spinnerPack.setOnItemSelectedListener(this);


            loadSchemeList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(SchemeListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing scheme list");
            return;

        }
    }


    private void displaySchemeList() {
        try {
            // create an Object for Adapter
            schemeListAdapter = new SchemeListAdapter(getActivity(), schemeList, schemeListRecyclerView);

            // set the adapter object to the Recyclerview
            schemeListRecyclerView.setAdapter(schemeListAdapter);
            //  mAdapter.notifyDataSetChanged();

            if (schemeList.isEmpty()) {
                schemeListRecyclerView.setVisibility(View.GONE);
                schemeListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                schemeListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

            schemeListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {
            Logger.Log(SchemeListFragment.class.getName(),ex);
            return;        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_scheme_list));
        //sfaCommon.displayUserInfo(getActivity(), getString(R.string.title_scheme_list), null);
        sfaCommon.displayDate(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

            final MenuItem item = menu.findItem(R.id.cust_action_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setVisible(true);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);


           /* final MenuItem menuItemSyncSchemeInfo = menu.findItem(R.id.action_sync_order_info);
            menuItemSyncSchemeInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemSyncSchemeInfo.setVisible(true);

            menuItemSyncSchemeInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(!NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                        DialogUtils.showAlertDialog(getActivity(), "Please enable internet");
                        return false;
                    }

                    if(backgroundServiceFactory!=null)
                        backgroundServiceFactory.initiateDiscountService();

                    DialogUtils.showAlertDialog(getActivity(),"Syncing Scheme Info. Please Wait ...");

                    refreshContent();

                    return false;
                }
            });*/


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
        final List<Scheme> filteredModelList = filter(schemeList, searchText);

        try {

            schemeListAdapter.setOutletListAdapter(getActivity(), filteredModelList, schemeListRecyclerView);
            schemeListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                schemeListAdapter.setOutletListAdapter(getActivity(), schemeList, schemeListRecyclerView);
                schemeListAdapter.notifyDataSetChanged();
            }

            return true;
        }catch (Exception ex){
            Logger.Log(SchemeListFragment.class.getName(),ex);
            return false;
        }
    }


    private List<Scheme> filter(List<Scheme> models, String query) {

        final List<Scheme> filteredModelList = new ArrayList<>();

        try {

            query = query.toLowerCase();

            for (Scheme model : models) {
                if (model != null) {
                    if (model.getSchemeCode().toLowerCase().contains(query.trim()) || model.getSchemeName().toLowerCase().contains(query.trim()) || model.getDescription().toLowerCase().contains(query.trim())) {
                        filteredModelList.add(model);
                    }
                }
            }

            return filteredModelList;

        }catch (Exception ex){
            Logger.Log(SchemeListFragment.class.getName(), ex);
            return filteredModelList;
        }
    }

    private List<Scheme> filter(List<Scheme> models, String brandName, String packName) {

        final List<Scheme> filteredModelList = new ArrayList<>();

        try {

            brandName = brandName.toLowerCase();
            packName = packName.toLowerCase();


            for (Scheme model : models) {
                if (model != null) {
                    if ((brandName.equalsIgnoreCase("All") || model.getSchemeTargetItems().get(0).getItemBrand().toLowerCase().contains(brandName.trim())) && (packName.equalsIgnoreCase("All") || model.getSchemeTargetItems().get(0).getItemPack().toLowerCase().contains(packName.trim()))) {
                        filteredModelList.add(model);
                    }
                }
            }

            return filteredModelList;

        }catch (Exception ex){
            Logger.Log(SchemeListFragment.class.getName(), ex);
            return filteredModelList;
        }
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

    }

    private void loadSchemeList() {

        try {

            Scheme scheme = null;

            schemeList.clear();

            List<com.inventrax_pepsi.database.pojos.Scheme> localSchemeList = tableScheme.getAllFilteredSchemes() ; //tableScheme.getAllSchemes();


            for (com.inventrax_pepsi.database.pojos.Scheme localDBScheme : localSchemeList) {

                scheme = gson.fromJson(localDBScheme.getSchemeJSON(), Scheme.class);

                schemeList.add(scheme);
            }

            displaySchemeList();

        } catch (Exception ex) {
            Logger.Log(SchemeListFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading scheme list");
            return;
        }

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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void searchBrandPackList() {
        try {

            final List<Scheme> filteredModelList = filter(schemeList, spinnerBrandNames.getSelectedItem().toString(), spinnerPack.getSelectedItem().toString());

            schemeListAdapter.setOutletListAdapter(getActivity(), filteredModelList, schemeListRecyclerView);
            schemeListAdapter.notifyDataSetChanged();  // data set changed

            if (spinnerBrandNames.getSelectedItem().toString().equalsIgnoreCase("All") && spinnerPack.getSelectedItem().toString().equalsIgnoreCase("All")) {
                schemeListAdapter.setOutletListAdapter(getActivity(), schemeList,schemeListRecyclerView);
                schemeListAdapter.notifyDataSetChanged();
            }

        } catch (Exception ex) {
            Logger.Log(VehicleStockFragment.class.getName(),ex);
            return;
        }
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

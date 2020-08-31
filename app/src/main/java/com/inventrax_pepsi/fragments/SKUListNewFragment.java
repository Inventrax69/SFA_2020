package com.inventrax_pepsi.fragments;

import android.content.DialogInterface;
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
import com.inventrax_pepsi.adapters.SchemeOfferItemListAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerDiscount;
import com.inventrax_pepsi.database.TableCustomerOrderHistory;
import com.inventrax_pepsi.database.TableCustomerPrice;
import com.inventrax_pepsi.database.TableItem;
import com.inventrax_pepsi.database.TableScheme;
import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;
import com.inventrax_pepsi.database.pojos.CustomerPrice;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.interfaces.SKUListView;
import com.inventrax_pepsi.interfaces.SchemeOfferItemView;
import com.inventrax_pepsi.sfa.cart.DerivedCart;
import com.inventrax_pepsi.sfa.cart.RebateCalculator;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.CustSKUOrder;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderHistory;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.SKUHistory;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;
import com.inventrax_pepsi.sfa.scheme.SchemeUtil;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SpinnerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 3/4/2016.
 */
public class SKUListNewFragment extends Fragment implements SchemeOfferItemView,SKUListView, OnLoadMoreListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView txtEmptyView, txtBottomBar, txtCartTotal, txtTotalPrice;
    private RecyclerView skuListRecyclerView,schemeOfferItemListRecyclerView;
    private SKUListAdapterNew skuListAdapter;
    private SchemeOfferItemListAdapter schemeOfferItemListAdapter;
    private LinearLayoutManager linearLayoutManager,linearLayoutManagerSchemeOfferItemList;
    private List<Item> itemList;
    private List<SchemeOfferItem> schemeOfferItemList;
    private View rootView;
    private SearchView searchView;
    private AppCompatActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHelper databaseHelper;
    private TableItem tableItem;
    private TableScheme tableScheme;
    private TableCustomerPrice tableCustomerPrice;
    private TableCustomerDiscount tableCustomerDiscount;
    private TableCustomerOrderHistory tableCustomerOrderHistory;
    private Gson gson;
    private Button btnCheckout;
    private RelativeLayout skuLayoutBottomBar;
    private Customer customer;
    private ArrayList<String> brandList;
    private ArrayList<String> packList;
    private DerivedCart derivedCart;
    private Spinner spinnerBrandNames, spinnerPack;
    private SFACommon sfaCommon;
    private Map<Integer, List<SKUHistory>> skuHistoryMap;

    private MaterialDialog materialSchemeOfferItemsDialog;
    private OrderUtil orderUtil;
    private Resources resources;




    public SKUListNewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sku_list_new, container, false);

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
            resources=getResources();

            gson = new GsonBuilder().create();
            customer = gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            buildSchemeOfferItemDialog();

            orderUtil=new OrderUtil();

            databaseHelper = DatabaseHelper.getInstance();
            tableItem = databaseHelper.getTableItem();
            tableScheme = databaseHelper.getTableScheme();
            tableCustomerDiscount = databaseHelper.getTableCustomerDiscount();
            tableCustomerPrice = databaseHelper.getTableCustomerPrice();
            tableCustomerOrderHistory = databaseHelper.getTableCustomerOrderHistory();

            txtEmptyView = (TextView) rootView.findViewById(R.id.txtEmptyView);
            skuListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_sku_list);

            btnCheckout = (Button) rootView.findViewById(R.id.btnCheckout);
            btnCheckout.setOnClickListener(this);

            txtCartTotal = (TextView) rootView.findViewById(R.id.txtCartTotal);
            txtTotalPrice = (TextView) rootView.findViewById(R.id.txtTotalPrice);

            skuLayoutBottomBar = (RelativeLayout) rootView.findViewById(R.id.skuLayoutBottomBar);
            skuLayoutBottomBar.setOnClickListener(this);

            skuListRecyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());

            // use a linear layout manager
            skuListRecyclerView.setLayoutManager(linearLayoutManager);

            swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_sku_list_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(this);

            itemList = new ArrayList<Item>();

            if (derivedCart == null) {
                derivedCart = new DerivedCart(customer);
            } else {
                if (derivedCart.getDerivedPrice() != 0) {

                    displayCartDetails();

                    showBottomBar();

                    setRefreshLayoutMargin();

                }
            }

            derivedCart.setSkuListView(this);


            loadCustomerSKUHistory();

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
            Logger.Log(SKUListNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing sku list");
            return;

        }
    }

    private void displaySKUList() {
        try {
            // create an Object for Adapter
            skuListAdapter = new SKUListAdapterNew(getActivity(), itemList, skuListRecyclerView, this, skuHistoryMap);
            skuListAdapter.setBillingPriceId(customer.getBillingPriceTypeId());

            // set the adapter object to the Recyclerview
            skuListRecyclerView.setAdapter(skuListAdapter);
            //  mAdapter.notifyDataSetChanged();



            if (itemList.isEmpty()) {
                skuListRecyclerView.setVisibility(View.GONE);
                skuListRecyclerView.setVisibility(View.VISIBLE);

            } else {
                skuListRecyclerView.setVisibility(View.VISIBLE);
                txtEmptyView.setVisibility(View.GONE);
            }

            skuListAdapter.setOnLoadMoreListener(this);

        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(),ex);
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

        try {

            final List<Item> filteredModelList = filter(itemList, searchText);

            skuListAdapter.setSKUListAdapter(getActivity(), filteredModelList, skuListRecyclerView, this, skuHistoryMap);
            skuListAdapter.setBillingPriceId(customer.getBillingPriceTypeId());
            skuListAdapter.notifyDataSetChanged();  // data set changed

            if (TextUtils.isEmpty(searchText)) {
                skuListAdapter.setSKUListAdapter(getActivity(), itemList, skuListRecyclerView, this, skuHistoryMap);
                skuListAdapter.setBillingPriceId(customer.getBillingPriceTypeId());
                skuListAdapter.notifyDataSetChanged();
            }

            return true;

        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return false;
        }
    }

    private List<Item> filter(List<Item> models, String query) {
        final List<Item> filteredModelList = new ArrayList<>();
        try {

            query = query.toLowerCase();

            for (Item model : models) {
                if (model != null) {

                    if (model.getItemBrand().toLowerCase().contains(query.trim()) || model.getItemPack().toLowerCase().contains(query.trim())) {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.BookOrder));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.BookOrder));
        // sfaCommon.displayDate(getActivity());
    }

    @Override
    public void onRefresh() {

        refreshContent();
        swipeRefreshLayout.setRefreshing(false);

    }

    private void refreshContent() {
        try {

            ProgressDialogUtils.showProgressDialog();

            loadSKUList();

            searchBrandPackList();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(SKUListNewFragment.class.getName(), ex);
            return;
        }
    }

    @Override
    public void onLoadMore() {

    }

    private void sortItemPrices(Item item)
    {
        try
        {

          if(item !=null && item.getItemPrices() !=null && item.getItemPrices().size() > 0)
          {
              Collections.sort((ArrayList)item.getItemPrices(), new Comparator<ItemPrice>() {

                  @Override
                  public int compare(ItemPrice lhs, ItemPrice rhs) {

                      return  (lhs.getItemPriceId() >= rhs.getItemPriceId()) ? -1 : 1;

                  }
              });
          }

        }catch (Exception e){

        }

    }

    private void loadSKUList() {

        try {

            itemList.clear();

            List<com.inventrax_pepsi.database.pojos.Item> localDBItemList  = null;

            if (AppController.getUser().getUserTypeId()!=7)
                localDBItemList = tableItem.getAllItems();
            else
                localDBItemList = tableItem.getAllActiveStockItems();

            if (localDBItemList==null)
                return;

            brandList = new ArrayList<>();
            brandList.add("All");
            brandList.addAll(tableItem.getAllItemBrands());
            packList = new ArrayList<>();
            packList.add("All");
            packList.addAll(tableItem.getAllItemPacks());

            Map<Integer, Object> itemRebateMap = null;

            for (com.inventrax_pepsi.database.pojos.Item localDBItem : localDBItemList) {


                Item item = gson.fromJson(localDBItem.getItemJSON(), Item.class);

                sortItemPrices(item);

                switch (this.customer.getCustomerGroupId()) {

                    // General Outlet
                    case 4: {

                        itemRebateMap = new HashMap<>();

                        // Get Item Prices
                        itemRebateMap.put(1, item.getItemPrices());

                        // Customer Discount
                        itemRebateMap.put(2, new ArrayList<CustomerDiscount>());

                        // Get Schemes
                        List<com.inventrax_pepsi.database.pojos.Scheme> localDBSchemeList = tableScheme.getAllSchemesByItemId(item.getItemId());

                        List<com.inventrax_pepsi.sfa.pojos.Scheme> schemeList = new ArrayList<>();

                        for (com.inventrax_pepsi.database.pojos.Scheme scheme : localDBSchemeList) {
                            schemeList.add(gson.fromJson(scheme.getSchemeJSON(), com.inventrax_pepsi.sfa.pojos.Scheme.class));
                        }

                        SchemeUtil.getSortedSchemes(schemeList);

                        itemRebateMap.put(3, schemeList);


                        item.setItemRebateMap(itemRebateMap);

                    }
                    break;

                    // Key Outlet(6) & National Key Outlet(5) & Display Outlet(7)
                    case 5:
                    case 6:
                    case 7:
                    {

                        itemRebateMap = new HashMap<>();

                        List<CustomerPrice> customerPriceList = tableCustomerPrice.getAllCustomerPricesByCustomerId(this.customer.getCustomerId(), item.getItemId());

                        List<ItemPrice> itemPriceList = new ArrayList<>();

                        if (customerPriceList != null && customerPriceList.size() > 0) {

                            // Get Item Prices
                            for (CustomerPrice customerPrice : customerPriceList) {

                                itemPriceList.add(gson.fromJson(customerPrice.getJSON(), ItemPrice.class));

                            }

                            Collections.sort(itemPriceList, new Comparator<ItemPrice>() {
                                @Override
                                public int compare(ItemPrice lhs, ItemPrice rhs) {
                                    return lhs.getItemPriceId() >= rhs.getItemPriceId() ? -1 : 1;
                                }
                            });

                            itemRebateMap.put(1, itemPriceList);

                            if (itemPriceList.size()>0)
                                item.setItemPrices(itemPriceList);

                        } else {

                            // Get Item Prices
                            itemRebateMap.put(1, item.getItemPrices());

                        }

                        // Get All Customer Discounts
                        List<com.inventrax_pepsi.database.pojos.CustomerDiscount> customerDiscountList = tableCustomerDiscount.getAllCustomerDiscountsByCustomerId(this.customer.getCustomerId(), item.getItemId());
                        List<CustomerDiscount> customerDiscounts = new ArrayList<>();

                        for (com.inventrax_pepsi.database.pojos.CustomerDiscount customerDiscount : customerDiscountList) {

                            customerDiscounts.add(gson.fromJson(customerDiscount.getDiscountJSON(), CustomerDiscount.class));

                        }

                        itemRebateMap.put(2, customerDiscounts);


                        // Get Schemes
                        List<com.inventrax_pepsi.database.pojos.Scheme> localDBSchemeList = tableScheme.getAllSchemesByItemId(item.getItemId());

                        List<com.inventrax_pepsi.sfa.pojos.Scheme> schemeList = new ArrayList<>();

                        for (com.inventrax_pepsi.database.pojos.Scheme scheme : localDBSchemeList) {
                            schemeList.add(gson.fromJson(scheme.getSchemeJSON(), com.inventrax_pepsi.sfa.pojos.Scheme.class));
                        }

                        SchemeUtil.getSortedSchemes(schemeList);

                        itemRebateMap.put(3, schemeList);

                        item.setItemRebateMap(itemRebateMap);

                    }
                    break;


                }

                itemList.add(item);

            }

            displaySKUList();

        } catch (Exception ex) {

            Logger.Log(SKUListNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading SKU list");
            return;

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCheckout:
            case R.id.skuLayoutBottomBar: {


                DialogUtils.showConfirmDialog(getActivity(), "", resources.getString(R.string.dialoguemessage), resources.getString(R.string.Yes),resources.getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {


                                try {



                                    /*if(customer.getCustomerGroupId()==4)
                                    {*/
                                        // Mixed Scheme Start
                                        RebateCalculator rebateCalculator = new RebateCalculator(customer);

                                        List<OrderItem> orderItemList = new ArrayList<OrderItem>();

                                        for (Map.Entry<Integer, List<OrderItem>> entry : derivedCart.getCartItems().entrySet()) {

                                            List<OrderItem> orderItems = entry.getValue();

                                            if (orderItems != null)
                                                for (OrderItem orderItem : orderItems) {
                                                    if (orderItem.getItemPrice() != 0)
                                                        orderItemList.add(orderItem);
                                                }
                                        }
                                        Order order = new Order();
                                        order.setOrderItems(orderItemList);
                                        rebateCalculator.applyCustomizedSchemeOnOrder(order, rebateCalculator.getTerritoryPrices(), rebateCalculator.getTerritorySchemes());
                                        derivedCart.getRecalculatedCart(derivedCart);
                                   // }

                                    // Mixed Scheme End
                                }catch (Exception ex){
                                    Logger.Log(ex);
                                    return;
                                }

                                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();


                                Bundle bundle = new Bundle();
                                bundle.putString("customer", getArguments().getString("customerJSON"));
                                bundle.putString("ItemList", gson.toJson(itemList));
                                bundle.putString("SelectedRoute", getArguments().getString("SelectedRoute"));
                                orderSummaryFragment.setArguments(bundle);
                                orderSummaryFragment.setDerivedCart(derivedCart);

                                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderSummaryFragment);


                                dialog.dismiss();
                            }
                            break;

                            case DialogInterface.BUTTON_NEGATIVE: {

                                dialog.dismiss();

                            }
                            break;
                        }


                    }
                });

            }
            break;
        }
    }

    @Override
    public void updateCart(Item item, int caseQuantity, int bottleQuantity) {

        try {

            schemeOfferItemListAdapter.setItem(null);
            schemeOfferItemListAdapter.setCaseQuantity(0);
            schemeOfferItemListAdapter.setBottleQuantity(0);
            schemeOfferItemListAdapter.notifyDataSetChanged();


            schemeOfferItemList = orderUtil.getSchemeOfferItem(item,caseQuantity,bottleQuantity);


            if (schemeOfferItemList!=null && schemeOfferItemList.size() > 1 && caseQuantity >= 1 ) {
                // Get & Display Scheme Offer Items from order util if more than one offer available
                displaySchemeOfferItemList(item,caseQuantity,bottleQuantity);

            }else {
                processCartItem(item,caseQuantity,bottleQuantity,null);
            }


        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while adding item to the cart");

            return;

        }

    }

    private void processCartItem(Item item, int caseQuantity, int bottleQuantity,SchemeOfferItem schemeOfferItem){

        try
        {

            if ( customer!=null && customer.getOutletProfile() != null &&  customer.getOutletProfile().getOrderCap()>0) {
                orderUtil = new OrderUtil();
                if (orderUtil.checkOrderCap(customer.getOutletProfile().getOrderCap(), customer.getCustomerId(), derivedCart.getNoOfCases()+caseQuantity))
                {
                    DialogUtils.showAlertDialog(getActivity(),"Order limit [ "+ customer.getOutletProfile().getOrderCap() +" ] exceeded to this customer.");
                    return;
                }
            }

            ProgressDialogUtils.showProgressDialog();

            derivedCart.AddItemToCart(item, caseQuantity, bottleQuantity, this.customer.getCustomerGroupId(),schemeOfferItem);

            if (derivedCart.getDerivedPrice() > 0) {

                displayCartDetails();

               // Toast.makeText(getActivity(), item.getItemBrand() + " " + item.getItemPack()   + " added to the cart", Toast.LENGTH_SHORT).show();

                showBottomBar();

                setRefreshLayoutMargin();

            }else {

                hideBottomBar();

            }

            ProgressDialogUtils.closeProgressDialog();


        }catch (Exception ex){
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(SKUListNewFragment.class.getName());
            DialogUtils.showAlertDialog(getActivity(), "Error while adding item to the cart");
            return;
        }

    }



    private void displayCartDetails() {
        try {

            txtCartTotal.setText(derivedCart.getNoOfSku() + "" + (derivedCart.getNoOfSku() > 1 ? resources.getString(R.string.Items):resources.getString(R.string.Item)));
            txtTotalPrice.setText(resources.getString(R.string.Total) + getString(R.string.Rs) + NumberUtils.formatValue(derivedCart.getDerivedPrice()));


        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(), ex);
            return;
        }
    }

    @Override
    public void showBottomBar() {
        skuLayoutBottomBar.setVisibility(RelativeLayout.VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        skuLayoutBottomBar.setVisibility(RelativeLayout.GONE);
    }

    @Override
    public void setRefreshLayoutMargin() {

    }

    @Override
    public void showMessage(String message,boolean isDialog) {
        if (isDialog) {
            DialogUtils.showAlertDialog(getActivity(), message);
        }else {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

        return;
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
            final List<Item> filteredModelList = filterBrandPackList(itemList, spinnerBrandNames.getSelectedItem().toString(), spinnerPack.getSelectedItem().toString());

            skuListAdapter.setSKUListAdapter(getActivity(), filteredModelList, skuListRecyclerView, this, skuHistoryMap);
            skuListAdapter.setBillingPriceId(customer.getBillingPriceTypeId());
            skuListAdapter.notifyDataSetChanged();  // data set changed

            if (spinnerBrandNames.getSelectedItem().toString().equalsIgnoreCase("All") && spinnerPack.getSelectedItem().toString().equalsIgnoreCase("All")) {
                skuListAdapter.setSKUListAdapter(getActivity(), itemList, skuListRecyclerView, this, skuHistoryMap);
                skuListAdapter.setBillingPriceId(customer.getBillingPriceTypeId());
                skuListAdapter.notifyDataSetChanged();
            }


        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return;
        }
    }

    private List<Item> filterBrandPackList(List<Item> models, String brandName, String packName) {

        try {

            final List<Item> filteredModelList = new ArrayList<>();

            for (Item model : models) {
                if (model != null) {

                    if ((brandName.equalsIgnoreCase("All") || brandName.equalsIgnoreCase(model.getItemBrand())) && (packName.equalsIgnoreCase("All") || packName.equalsIgnoreCase(model.getItemPack()))) {
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadCustomerSKUHistory() {

        try {

            CustomerOrderHistory customerOrderHistory = tableCustomerOrderHistory.getCustomerOrderHistory(customer.getCustomerId());

            if (customerOrderHistory != null) {

                CustSKUOrder custSKUOrder = gson.fromJson(customerOrderHistory.getBrandPackJSON(), CustSKUOrder.class);

                skuHistoryMap = new HashMap<>();

                int tempItemId = 0;

                if (custSKUOrder.getOrderHistory() != null)
                    for (OrderHistory orderHistory : custSKUOrder.getOrderHistory()) {
                        if (tempItemId != orderHistory.getItemId()) {
                            tempItemId = orderHistory.getItemId();
                            skuHistoryMap.put(orderHistory.getItemId(), new ArrayList<SKUHistory>());
                        }

                        skuHistoryMap.get(tempItemId).add(new SKUHistory(orderHistory.getDate(), (int) orderHistory.getqTY()));
                    }

            }

        } catch (Exception ex) {
            Logger.Log(SKUListNewFragment.class.getName(), ex);
            return;
        }

    }


    public void setDerivedCart(DerivedCart derivedCart) {
        this.derivedCart = derivedCart;
    }



    private void buildSchemeOfferItemDialog(){

        try
        {


            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title("Select Offer Item")
                    .autoDismiss(false)
                    .customView(R.layout.dialog_scheme_offer_item_list, true);
                    //.positiveText("Ok");

            materialSchemeOfferItemsDialog = builder.build();

            schemeOfferItemList=new ArrayList<>();

            schemeOfferItemListRecyclerView = (RecyclerView) materialSchemeOfferItemsDialog.findViewById(R.id.recycler_view_scheme_offer_item_list);

            schemeOfferItemListRecyclerView.setHasFixedSize(true);

            linearLayoutManagerSchemeOfferItemList = new LinearLayoutManager(getContext());

            // use a linear layout manager
            schemeOfferItemListRecyclerView .setLayoutManager(linearLayoutManagerSchemeOfferItemList);


            schemeOfferItemListAdapter = new SchemeOfferItemListAdapter(getActivity(),schemeOfferItemList,this);

            // set the adapter object to the Recyclerview
            schemeOfferItemListRecyclerView.setAdapter(schemeOfferItemListAdapter);


        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return;
        }

    }


    private void displaySchemeOfferItemList(Item item,int caseQuantity,int bottleQuantity){
        try
        {
            schemeOfferItemListAdapter.setSchemeOfferItemListAdapter(getContext(),schemeOfferItemList,this);
            schemeOfferItemListAdapter.setItem(item);
            schemeOfferItemListAdapter.setCaseQuantity(caseQuantity);
            schemeOfferItemListAdapter.setBottleQuantity(bottleQuantity);

            schemeOfferItemListAdapter.notifyDataSetChanged();

            materialSchemeOfferItemsDialog.show();

        }catch (Exception ex){
            Logger.Log(SKUListNewFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void OnSchemeOfferItemSelected(SchemeOfferItem schemeOfferItem,Item item,int caseQuantity,int bottleQuantity) {

        processCartItem(item,caseQuantity,bottleQuantity,schemeOfferItem);

        materialSchemeOfferItemsDialog.dismiss();
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
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }
    }



}

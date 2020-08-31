package com.inventrax_pepsi.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.OrderSummaryListAdapter;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.interfaces.OrderSummaryView;
import com.inventrax_pepsi.interfaces.OrderView;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.cart.DerivedCart;
import com.inventrax_pepsi.sfa.cart.RebateCalculator;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Naresh on 13-Mar-16.
 */
public class OrderSummaryFragment extends Fragment implements OrderView, OrderSummaryView, View.OnClickListener {

    String customerJSON = "", itemListJSON = "";
    private View rootView;
    private LinearLayoutManager linearLayoutManager;
    private OrderSummaryListAdapter orderSummaryListAdapter;
    private RecyclerView recyclerView;
    private Gson gson;
    private Customer customer;
    private Button btnConfirmOrder;
    private CoordinatorLayout coordinatorLayout;
    private DerivedCart derivedCart = null;
    private Map<Integer, List<OrderItem>> derivedCartOrderItems = null;
    private List<OrderItem> orderItemList = null;
    private TextView txtTotalAmount, txtCases, txtBottles, txtFrees;
    private RelativeLayout layoutOrderSummaryFooter, layoutAddMore;
    private SFACommon sfaCommon;
    private OrderUtil orderUtil;
    private BackgroundServiceFactory backgroundServiceFactory;
    private FloatingActionButton fabAddMoreItems;
    private Resources resources;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_summary, container, false);

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

            orderItemList = new ArrayList<>();

            customerJSON = getArguments().getString("customer");

            if (!TextUtils.isEmpty(customerJSON))
                customer = gson.fromJson(customerJSON, Customer.class);

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setOrderView(this);


            buildOrderSummaryHeader();

            buildOrderSummaryItems();

            buildOrderSummaryFooter();

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryHeader() {

        try {

            fabAddMoreItems = (FloatingActionButton) rootView.findViewById(R.id.fabAddMoreItems);
            fabAddMoreItems.setOnClickListener(this);

            layoutAddMore = (RelativeLayout) rootView.findViewById(R.id.layoutAddMore);

        } catch (Exception ex) {
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryFooter() {
        try {

            coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.snack_bar_action_layout);
            layoutOrderSummaryFooter = (RelativeLayout) rootView.findViewById(R.id.layoutOrderSummaryFooter);
            layoutOrderSummaryFooter.setOnClickListener(this);
            txtTotalAmount = (TextView) rootView.findViewById(R.id.txtTotalAmount);
            txtBottles = (TextView) rootView.findViewById(R.id.txtBottles);
            txtCases = (TextView) rootView.findViewById(R.id.txtCases);
            txtFrees = (TextView) rootView.findViewById(R.id.txtFrees);
            btnConfirmOrder = (Button) rootView.findViewById(R.id.btnConfirmOrder);
            btnConfirmOrder.setText(resources.getString(R.string.Confirm_Order));
            btnConfirmOrder.setOnClickListener(this);

            displayOrderSummaryFooter();

        } catch (Exception ex) {
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }


    private void displayOrderSummaryFooter() {
        try {

            txtTotalAmount.setText(getString(R.string.Rs) + " " + NumberUtils.formatValue(derivedCart.getDerivedPrice()));

            txtBottles.setText((derivedCart.getNoOfBottles() > 0 ? resources.getString(R.string.Bottles) + ((int) derivedCart.getNoOfBottles()) : " "));

            txtCases.setText((derivedCart.getNoOfCases() > 0 ?resources.getString(R.string.Cases)+ ((int) derivedCart.getNoOfCases()) : " "));
            txtCases.setText((derivedCart.getNoOfCases() > 0 ?resources.getString(R.string.Cases)+ ((int) derivedCart.getNoOfCases()) : " "));

            txtFrees.setText(((derivedCart.getNoOfFreesInCase() > 0 || derivedCart.getNoOfFreesInBottles() > 0) ? " Frees : " : " ") + (derivedCart.getNoOfFreesInCase() > 0 ? "FC/" + ((int) derivedCart.getNoOfFreesInCase()) : "") + (derivedCart.getNoOfFreesInBottles() > 0 ? "  FB/" + ((int) derivedCart.getNoOfFreesInBottles()) : " "));

        } catch (Exception ex) {
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryItems() {

        try {

            getOrderItemList();

            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order_summary);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            orderSummaryListAdapter = new OrderSummaryListAdapter(getContext(), orderItemList, this);
            recyclerView.setAdapter(orderSummaryListAdapter);

        } catch (Exception ex) {
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void getOrderItemList() {
        try {

            derivedCartOrderItems = derivedCart.getCartItems();

            orderItemList.clear();

            for (Map.Entry<Integer, List<OrderItem>> entry : derivedCartOrderItems.entrySet()) {

                List<OrderItem> orderItems = entry.getValue();

                if (orderItems != null)
                    for (OrderItem orderItem : orderItems) {

                        if (orderItem.getItemPrice() != 0)
                            orderItemList.add(orderItem);


                   /* OrderItem schemeOrderItem=null;

                    if (orderItem.getOrderItemSchemes()!=null)
                        for (OrderItemScheme orderItemScheme: orderItem.getOrderItemSchemes()){

                            schemeOrderItem=new OrderItem();

                            schemeOrderItem.setItemBrand(orderItemScheme.getItemBrand());
                            schemeOrderItem.setItemPack(orderItemScheme.getItemPack());
                            schemeOrderItem.setItemPrice(orderItemScheme.getPrice());
                            schemeOrderItem.setDerivedPrice(0);
                            schemeOrderItem.setQuantity(orderItemScheme.getValue());
                            schemeOrderItem.setUoMCode(orderItemScheme.getUoM());
                            schemeOrderItem.setItemId(orderItemScheme.getItemId());
                            schemeOrderItem.setImageName(orderItemScheme.getImageName());
                            schemeOrderItem.setHideDeleteButton(true);
                            schemeOrderItem.setItemType(orderItemScheme.getItemType());

                            orderItemList.add(schemeOrderItem);
                        }*/


                    }
            }

        } catch (Exception ex) {
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            return;
        }
    }

    @Override
    public void onItemDeleted(OrderItem orderItem, int position) {

        try {

            if (orderItemList != null) {

                derivedCart.deleteCartItem(orderItem.getItemId(), orderItem.getUoMCode());

                eraseMixedSchemes(derivedCart);

                try {

                    // Removed based on customer requirement 16/06/2016 3:12 PM
                    /*if(customer.getCustomerGroupId()==4)
                    {*/
                        // Mixed Scheme Start
                        RebateCalculator rebateCalculator = new RebateCalculator(customer);

                        List<OrderItem> orderItemList = new ArrayList<OrderItem>();

                        for (Map.Entry<Integer, List<OrderItem>> entry : derivedCart.getCartItems().entrySet()) {

                            List<OrderItem> orderItems = entry.getValue();

                            if (orderItems != null)
                                for (OrderItem orderItem1 : orderItems) {
                                    if (orderItem1.getItemPrice() != 0)
                                        orderItemList.add(orderItem1);
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


                getOrderItemList();

                orderSummaryListAdapter.setOrderSummaryListAdapter(getContext(), orderItemList, this);

                orderSummaryListAdapter.notifyDataSetChanged();

                SnackbarUtils.showSnackbarLengthLong(coordinatorLayout, "Selected line item removed successfully");



                if (orderItemList != null && orderItemList.size() == 0) {

                    layoutOrderSummaryFooter.setVisibility(RelativeLayout.GONE);

                    SKUListNewFragment skuListFragment = new SKUListNewFragment();

                    Bundle skuBundle = new Bundle();
                    skuBundle.putString("customerJSON", customerJSON);
                    skuBundle.putString("SelectedRoute", customer.getOutletProfile().getRouteCode());

                    skuListFragment.setArguments(skuBundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, skuListFragment);

                }

                displayOrderSummaryFooter();


            }
        }catch (Exception ex){
            Logger.Log(OrderSummaryFragment.class.getName(),ex);
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_order_summary));

        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_order_summary));
        // sfaCommon.hideUserInfo(getActivity());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.layoutOrderSummaryFooter:
            case R.id.btnConfirmOrder: {

                if ( customer!=null && customer.getOutletProfile() != null &&  customer.getOutletProfile().getOrderCap()>0) {
                    orderUtil = new OrderUtil();
                    if (orderUtil.checkOrderCap(customer.getOutletProfile().getOrderCap(), customer.getCustomerId(), derivedCart.getNoOfCases()))
                    {
                        DialogUtils.showAlertDialog(getActivity(),"Order limit [ "+ customer.getOutletProfile().getOrderCap() +" ] exceeded to this customer.");
                        return;
                    }
                }

                DialogUtils.showConfirmDialog(getActivity(), "",resources.getString(R.string.confirmordermessage) ,resources.getString(R.string.Yes),resources.getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {


                                generateOrder();

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

            case R.id.fabAddMoreItems: {

                SKUListNewFragment skuListFragment = new SKUListNewFragment();

                Bundle skuBundle = new Bundle();
                skuBundle.putString("customerJSON", customerJSON);
                skuBundle.putString("SelectedRoute", customer.getOutletProfile().getRouteCode());
                skuListFragment.setArguments(skuBundle);
                eraseMixedSchemes(derivedCart);
                skuListFragment.setDerivedCart(derivedCart);
                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, skuListFragment);

            }
            break;
        }
    }


    private void eraseMixedSchemes(DerivedCart derivedCart)
    {
        try
        {
            if(derivedCart.getCartItems() !=null && derivedCart.getCartItems().size() > 0)
            {
                for(Map.Entry<Integer,List<OrderItem>> cartEntry : derivedCart.getCartItems().entrySet())
                {
                    for(OrderItem orderItem:cartEntry.getValue())
                    {
                        if(orderItem.getOrderItemSchemes() !=null && orderItem.getOrderItemSchemes().get(0).isMixedScheme())
                        {
                            derivedCart.setNoOfFreesInBottles(derivedCart.getNoOfFreesInBottles() - orderItem.getOrderItemSchemes().get(0).getValue());
                            orderItem.setOrderItemSchemes(null);
                        }
                    }
                }
            }
        }catch (Exception e)
        {
            //logger
        }
    }

    public DerivedCart getDerivedCart() {
        return derivedCart;
    }

    public void setDerivedCart(DerivedCart derivedCart) {
        this.derivedCart = derivedCart;
    }


    private void generateOrder() {
        try {

            orderUtil = new OrderUtil();

            ProgressDialogUtils.showProgressDialog();

            Order order = orderUtil.generateOrder(customer, derivedCart, orderItemList);


            if (order != null && orderUtil.createOrderInLocalDB(order, customer)) {

                DialogUtils.showAlertDialog(getActivity(),resources.getString(R.string.OrderCreatedSuccessfully));

                Intent counterBroadcastIntent = new Intent();
                counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
                getActivity().sendBroadcast(counterBroadcastIntent);

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateOrderService(order.getOrderCode());
                }

                OrderViewTabFragment orderViewTabFragment = new OrderViewTabFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromDelivery", false);
                bundle.putString("OrderNumber", order.getOrderCode());
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", customerJSON);
                orderViewTabFragment.setArguments(bundle);

                ProgressDialogUtils.closeProgressDialog();


                /*OrderHistoryListFragment orderHistoryListFragment = new OrderHistoryListFragment();
                Bundle orderHistoryBundle = new Bundle();
                orderHistoryBundle.putString("customerJSON", customerJSON);
                orderHistoryBundle.putInt("customerId", customer.getCustomerId());
                orderHistoryListFragment.setArguments(orderHistoryBundle);

                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderHistoryListFragment);*/


                if (order.getOrderTypeId()==1){

                    // Pre - Sale

                    OrderHistoryListFragment orderHistoryListFragment = new OrderHistoryListFragment();
                    Bundle orderHistoryBundle = new Bundle();
                    orderHistoryBundle.putString("customerJSON", customerJSON);
                    orderHistoryBundle.putInt("customerId", customer.getCustomerId());
                    orderHistoryListFragment.setArguments(orderHistoryBundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderHistoryListFragment);

                }else {

                    // Ready Sale

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderViewTabFragment);

                }

            } else {
                ProgressDialogUtils.closeProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), "Error while generating order ");
                return;
            }


        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(OrderSummaryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while generating order ");
            return;
        }
    }


    @Override
    public void showOrderSyncStatus(String message, int status) {


        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


    }
}

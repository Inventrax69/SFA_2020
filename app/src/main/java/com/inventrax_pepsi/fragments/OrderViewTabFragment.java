package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.ViewPagerAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.util.FragmentUtils;

/**
 * Created by android on 3/15/2016.
 */
public class OrderViewTabFragment extends Fragment {

    private View rootView;
    private TabLayout orderTabLayout;
    private ViewPager orderViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean isFromDelivery = false;
    private String invoiceNumber = "", orderNumber = "";
    private SFACommon sfaCommon;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private Customer customer;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_view_tabs, container, false);

        sfaCommon = SFACommon.getInstance();

        try {

            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {


                            if (isFromDelivery) {

                                DeliveryListFragment deliveryListFragment = new DeliveryListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("customerId", 0);
                                deliveryListFragment.setArguments(bundle);

                                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, deliveryListFragment);


                            } else {

                                OrderHistoryListFragment orderHistoryListFragment = new OrderHistoryListFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("customerId", 0);
                                orderHistoryListFragment.setArguments(bundle);

                                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderHistoryListFragment);
                            }

                        }
                    }
                    return false;
                }
            });

            loadFormControls();

        }catch (Exception ex){
            Logger.Log(OrderViewTabFragment.class.getName(), ex);
            return rootView;
        }

        return rootView;
    }

    private void loadFormControls() {
        try {

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();

            com.inventrax_pepsi.database.pojos.Customer localCustomer = tableCustomer.getCustomer(getArguments().getInt("CustomerId"));

            customer = gson.fromJson(localCustomer.getCompleteJSON(), Customer.class);


            orderTabLayout = (TabLayout) rootView.findViewById(R.id.orderViewTabs);
            orderViewPager = (ViewPager) rootView.findViewById(R.id.orderViewPager);


            isFromDelivery = (getArguments() != null && getArguments().getBoolean("isFromDelivery") == true) ? true : false;

            if (isFromDelivery) {

                if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("InvoiceNo")))
                    invoiceNumber = getArguments().getString("InvoiceNo");

            }

            orderNumber = getArguments() != null ? getArguments().getString("OrderNumber") : "";




            orderTabLayout.addTab(orderTabLayout.newTab().setText("Summary"));

            if (AppController.getUser().getUserTypeId() == 7) {
                orderTabLayout.addTab(orderTabLayout.newTab().setText("Delivery"));
                orderTabLayout.addTab(orderTabLayout.newTab().setText("Returns"));

            }

            final ViewPagerAdapter adapter = new ViewPagerAdapter
                    (getActivity().getSupportFragmentManager(), orderTabLayout.getTabCount(), invoiceNumber, orderNumber, isFromDelivery);
            orderViewPager.setAdapter(adapter);
            orderViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(orderTabLayout));

            if (AppController.getUser().getUserTypeId() == 7) {
                orderViewPager.setCurrentItem(1);
            }

            orderTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    orderViewPager.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (Exception ex) {
            Logger.Log(OrderViewTabFragment.class.getName(), ex);
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        sfaCommon.displayUserInfo(getActivity(), customer, "");

    }

}

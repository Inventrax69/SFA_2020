package com.inventrax_pepsi.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.OutletMenuAdapterNew;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.application.OutletMenuFactory;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerOrderHistory;
import com.inventrax_pepsi.database.TableCustomerTrans;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;
import com.inventrax_pepsi.interfaces.OutletDashboardView;
import com.inventrax_pepsi.model.OutletMenuItem;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.AddressBook;
import com.inventrax_pepsi.sfa.pojos.BrandHistory;
import com.inventrax_pepsi.sfa.pojos.CustSKUOrder;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.LoginInfo;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FontUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by android on 3/12/2016.
 */
public class OutletDashboardNewFragment extends Fragment implements OutletDashboardView, View.OnClickListener {

    private List<OutletMenuItem> outletMenuItemList;
    private View rootView;
    private Context context;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private OutletMenuAdapterNew outletMenuAdapter;
    private Customer customer;
    private Gson gson;
    private TextView txtOwnerName, txtPhoneNumber, txtOutletType, txtChannelCode, txtLandMark, txtRouteName, txtRouteCode, txtAccountType, txtOutletName,txtCustomerAssets,txtCreditLimit,txtCreditDueDays;
    private TextView txtBottomBar;
    private SFACommon sfaCommon;
    private ImageButton btnCheckIn;
    private LinearLayout layoutCustomerCreditInfo;
    private RelativeLayout layoutOutletDashboard, layoutCheckIn;
    private DatabaseHelper databaseHelper;
    private TableUserTracking tableUserTracking;
    private TableCustomerOrderHistory tableCustomerOrderHistory;
    private TableCustomerTrans tableCustomerTrans;
    private PieChart pieChart;
    private GPSLocationService gpsLocationService;
    private BackgroundServiceFactory backgroundServiceFactory;

    private SharedPreferencesUtils sharedPreferencesUtils;
    private Resources resources;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_outlet_dashboard_new, container, false);

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

        context = getContext();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {
        try {

            ProgressDialogUtils.showProgressDialog();

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableUserTracking = databaseHelper.getTableUserTracking();
            tableCustomerOrderHistory = databaseHelper.getTableCustomerOrderHistory();
            tableCustomerTrans = databaseHelper.getTableCustomerTrans();


            btnCheckIn = (ImageButton) rootView.findViewById(R.id.btnCheckIn);
            btnCheckIn.setOnClickListener(this);

            layoutOutletDashboard = (RelativeLayout) rootView.findViewById(R.id.layoutOutletDashboard);
            layoutCheckIn = (RelativeLayout) rootView.findViewById(R.id.layoutCheckIn);

            gpsLocationService = new GPSLocationService(getContext());

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());

            sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", getContext());


            buildOutletInfo();
            buildOutletMenu();

            ProgressDialogUtils.closeProgressDialog();


            // check if GPS enabled
            if (!gpsLocationService.canGetLocation()) {
                // Ask user to enable GPS/network in settings
                gpsLocationService.showSettingsAlert();
                return;
            }


        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOutletInfo() {
        try {
            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            if (customer != null) {

                OutletProfile outletProfile = customer.getOutletProfile();


                if (tableUserTracking.getSingleUserTracking(customer.getCustomerId()) != null) {
                    layoutOutletDashboard.setVisibility(RelativeLayout.VISIBLE);
                    layoutCheckIn.setVisibility(RelativeLayout.GONE);
                } else {
                    layoutOutletDashboard.setVisibility(RelativeLayout.GONE);
                    layoutCheckIn.setVisibility(RelativeLayout.VISIBLE);
                }


                AddressBook addressBook = null;
                if (customer.getAddressBook() != null)
                    addressBook = customer.getAddressBook().get(0);

                txtOutletName = (TextView) rootView.findViewById(R.id.txtOutletName);
                txtOutletName.setText(customer.getCustomerName() + " - [ " + customer.getCustomerCode() + " ]");
                FontUtils.setFont(txtOutletName);
                txtOwnerName = (TextView) rootView.findViewById(R.id.txtOwnerName);
                txtOwnerName.setText(customer.getContactPersonName());

                txtPhoneNumber = (TextView) rootView.findViewById(R.id.txtPhoneNumber);
                txtPhoneNumber.setText("" + (TextUtils.isEmpty(customer.getMobileNo1())?customer.getMobileNo2():customer.getMobileNo1()));

                txtOutletType = (TextView) rootView.findViewById(R.id.txtOutletType);
                txtOutletType.setText(customer.getCustomerGroup());
                 resources=getResources();
                layoutCustomerCreditInfo = (LinearLayout)rootView.findViewById(R.id.layoutCustomerCreditInfo);

                if (customer.isCreditAccount()) {

                    layoutCustomerCreditInfo.setVisibility(LinearLayout.VISIBLE);

                    txtCreditLimit = (TextView) rootView.findViewById(R.id.txtCreditLimit);
                    txtCreditDueDays = (TextView) rootView.findViewById(R.id.txtCreditDueDays);

                    double customerCreditAmount=(customer.getCustomerTransaction() == null ? 0 : customer.getCustomerTransaction().getAmount()) - tableCustomerTrans.getCustomerPaidAmount(customer.getCustomerId());

                    txtCreditLimit.setText("Credit Limit : [ " +  getString(R.string.Rs) + NumberUtils.formatValue( ( customerCreditAmount > 0 ? customerCreditAmount : 0 ) )   + " / " +  getString(R.string.Rs) + NumberUtils.formatValue(customer.getCreditLimit()) + " ]" );

                    txtCreditDueDays.setText("Credit Due Days : " + customer.getCreditDueDays() );

                }else {
                    layoutCustomerCreditInfo.setVisibility(LinearLayout.GONE);
                }

                txtCustomerAssets = (TextView) rootView.findViewById(R.id.txtCustomerAssets);

                if (customer!=null && customer.getCustomerAssets()!=null && customer.getCustomerAssets().size()>0 )
                    txtCustomerAssets.setText("Assets [ "+customer.getCustomerAssets().size()+" ] ");

                txtCustomerAssets.setOnClickListener(this);


                if (outletProfile != null) {

                    txtRouteCode = (TextView) rootView.findViewById(R.id.txtRouteCode);
                    txtRouteCode.setText(outletProfile.getRouteCode());

                    txtAccountType = (TextView) rootView.findViewById(R.id.txtAccountType);
                    txtAccountType.setText(outletProfile.getAccountType());

                    txtChannelCode = (TextView) rootView.findViewById(R.id.txtChannelCode);
                    txtChannelCode.setText(outletProfile.getChannelCode());

                    txtRouteName = (TextView) rootView.findViewById(R.id.txtRouteName);
                    txtRouteName.setText(outletProfile.getRouteName());
                }

                if (addressBook != null) {
                    txtLandMark = (TextView) rootView.findViewById(R.id.txtLandMark);
                    txtLandMark.setText(addressBook.getLandmark());
                }
            }

            pieChart = (PieChart) rootView.findViewById(R.id.pieChartOutlet);
            pieChart.setNoDataText("");

            loadCustomerBrandHistory();

        } catch (Exception ex) {
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading outlet info");
            return;
        }
    }

    private void loadCustomerBrandHistory() {

        try {

            CustomerOrderHistory customerOrderHistory = tableCustomerOrderHistory.getCustomerOrderHistory(customer.getCustomerId());

            if (customerOrderHistory != null) {

                CustSKUOrder custSKUOrder = gson.fromJson(customerOrderHistory.getBrandJSON(), CustSKUOrder.class);

                List<BrandHistory> brandHistories = custSKUOrder.getBrandHistory();

                if (brandHistories != null && brandHistories.size() > 0) {

                    displayPieChart(brandHistories);

                }


            }


        } catch (Exception ex) {
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            return;
        }

    }


    private void displayPieChart(List<BrandHistory> brandHistories) {

        try {

            pieChart.setUsePercentValues(false);
            pieChart.setDescription("Brand Sale History");

            // enable hole and configure
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);
            pieChart.setHoleRadius(7);
            pieChart.setTransparentCircleRadius(10);


            // enable rotation of the chart by touch
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);

            // creating data values
            ArrayList<Entry> entries = new ArrayList<>();

            // creating labels
            final ArrayList<String> labels = new ArrayList<String>();

            // Brand Color Codes
            List<Integer> colorArray = new ArrayList<>();

            int index = 0;

            for (BrandHistory brandHistory : brandHistories) {

                entries.add(new Entry((float) brandHistory.getQuantity(), index));
                //labels.add(index,brandHistory.getBrandName());
                labels.add(index, "");
                colorArray.add(SFACommon.getColorByBrand(brandHistory.getBrandName()));

                index++;
            }


            PieDataSet dataset = new PieDataSet(entries, "Brand Sale History");
            dataset.setSliceSpace(3);
            dataset.setSelectionShift(5);
            dataset.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf((int) Math.floor(value));
                }
            });

            PieData data = new PieData(labels, dataset); // initialize Piedata
            //data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            pieChart.setData(data); // set data into chart

            pieChart.setDescription("");  // set the description

            int colors[] = NumberUtils.convertIntegers(colorArray);

            dataset.setColors(colors); // set the color

            // undo all highlights
            pieChart.highlightValues(null);

            // customize legends
            Legend l = pieChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);

            pieChart.setDescription("");    // Hide the description
            pieChart.getLegend().setEnabled(false);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {


                }

                @Override
                public void onNothingSelected() {

                }
            });

            // update pie chart
            pieChart.invalidate();

            pieChart.animateY(3000);


        } catch (Exception ex) {
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            return;
        }

    }

    private void buildOutletMenu() {

        try {



            outletMenuItemList = OutletMenuFactory.getOutletMenuItemsByUserType(AppController.getUser().getUserTypeId(),((customer.getCustomerAssets()!=null && customer.getCustomerAssets().size()>0) ? true : false ));
            //outletMenuItemList = sfaCommon.getOutletMenuItemList();

            gridLayoutManager = new GridLayoutManager(getContext(), 3);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_outlet_menu);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);

            outletMenuAdapter = new OutletMenuAdapterNew(getContext(), outletMenuItemList, this);
            recyclerView.setAdapter(outletMenuAdapter);

        } catch (Exception ex) {
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading menu items");
            return;
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.CustomerOperations));

        sfaCommon.displayDate(getActivity());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }
    private int GetmenuIndex(String menuTitle) {

        String[] resourcevalues={resources.getString(R.string.title_book_order),resources.getString(R.string.title_delivery_list),resources.getString(R.string.title_customer_returns),resources.getString(R.string.title_asset_info),resources.getString(R.string.title_order_history),resources.getString(R.string.title_outlet_registration),resources.getString(R.string.title_asset_request),resources.getString(R.string.title_asset_complaint),resources.getString(R.string.title_asset_audit),resources.getString(R.string.title_pullout_form),resources.getString(R.string.title_discount_list),resources.getString(R.string.sku_list_btn_checkout_text)};
        for (int i = 0; i < resourcevalues.length; i++) {
            if (resourcevalues[i].equals(menuTitle)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onMenuItemSelected(OutletMenuItem outletMenuItem) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        resources=getResources();
        switch (GetmenuIndex(outletMenuItem.getMenuText())) {

            case 0: {

                try {



                    LoginInfo loginInfo = null;

                    String strLoginInfo = sharedPreferencesUtils.loadPreference("userLoginInfo", "");

                    if (!TextUtils.isEmpty(strLoginInfo)) {

                        loginInfo = gson.fromJson(strLoginInfo, LoginInfo.class);

                        if (loginInfo != null) {

                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            //Date loginDate = format.parse("2016-05-21 18:36:41.330");
                            Date loginDate = format.parse(loginInfo.getLoginTime());
                            Date currentDate = format.parse(DateUtils.getTimeStamp());
                            if (currentDate.compareTo(loginDate) != 0) {
                                showPastLoginSessionErrorMessage();
                                return;
                            }
                        }

                    } else {
                        showPastLoginSessionErrorMessage();
                        return;
                    }
                }catch (Exception ex){
                    showPastLoginSessionErrorMessage();
                    return;
                }

                if (!checkAssetAudit())
                    break;

                fragment = new SKUListNewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                bundle.putString("SelectedRoute", customer.getOutletProfile().getRouteCode());
                fragment.setArguments(bundle);

                title = getString(R.string.title_book_order);

            }
            break;
            case 1: {
                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));

                fragment = new DeliveryListFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_delivery_list);
            }
            break;

            case 2:{

                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));

                fragment = new CustomerReturnsFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_customer_returns);

            }break;

            case 3:{

                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));

                fragment = new AssetInfoFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_asset_info);

            }break;

            case 4: {

                Bundle bundle = new Bundle();
                bundle.putInt("customerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment = new OrderHistoryListFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_order_history);
            }
            break;

            case 5: {

                Bundle bundle = new Bundle();
                bundle.putInt("customerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment = new OutletRegistrationFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_outlet_registration);

            }
            break;
            case 6: {

                Bundle bundle = new Bundle();
                bundle.putInt("customerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment = new AssetRequestFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_asset_request);

            }
            break;
            case 7: {
                fragment = new AssetComplaintFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment.setArguments(bundle);
                title = getString(R.string.title_asset_complaint);

            }
            break;
            case 8: {

                fragment = new AssetAuditFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment.setArguments(bundle);
                title = getString(R.string.title_asset_audit);

            }
            break;

            case 9:{

                fragment = new PulloutFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment.setArguments(bundle);
                title = getString(R.string.title_pullout_form);

            }break;

            case 10: {

                fragment = new DiscountListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("CustomerId", customer.getCustomerId());
                bundle.putString("customerJSON", getArguments().getString("customerJSON"));
                fragment.setArguments(bundle);

            }
            break;

            case 11: {

                try {

                    /*if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                        DialogUtils.showAlertDialog(getActivity(), "Location Information Is Not Available");
                        return;
                    }*/

                    if (!sfaCommon.checkInOut(customer, 2, gpsLocationService)) {
                        DialogUtils.showAlertDialog(getActivity(), "Error while checking out");
                    } else {

                        Toast.makeText(getActivity(), AbstractApplication.get().getString(R.string.checkoutmessage), Toast.LENGTH_LONG).show();

                        if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                            backgroundServiceFactory.initiateUserTrackService(customer.getCustomerId(), false);

                        doReturn();
                    }

                }catch (Exception ex){
                    Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
                    return;
                }
            }
            break;

        }

        if (fragment != null) {

            FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, fragment);

        }
    }

    private boolean checkAssetAudit() {

        try
        {
            OrderUtil orderUtil=new OrderUtil();

            if (customer.isAuditRequired()) {
                if (!orderUtil.checkIsAssetAuditCompleted(customer)) {
                    DialogUtils.showAlertDialog(getActivity(), "Do Asset audit before order booking");
                    return false;
                }
            }

        }catch (Exception ex){
            return false;
        }

        return true;

    }

    private void doReturn(){
        try
        {

            OutletListNewFragment outletListFragment = new OutletListNewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("RouteCode", customer.getOutletProfile().getRouteCode());
            outletListFragment.setArguments(bundle);

            FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletListFragment);

        }catch (Exception ex){

        }
    }

    private void showPastLoginSessionErrorMessage(){
        DialogUtils.showAlertDialog(getActivity(), "You're continuing with past login session, Please logout and login again");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCheckIn: {


                try {

                    /*if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                        DialogUtils.showAlertDialog(getActivity(), "Location Information Is Not Available");
                        return;
                    }*/

                    if (!sfaCommon.checkInOut(customer, 1, gpsLocationService)) {
                        DialogUtils.showAlertDialog(getActivity(), "Error while check-in");
                    } else {

                        if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                            backgroundServiceFactory.initiateUserTrackService(customer.getCustomerId(), true);

                        Toast.makeText(getActivity(), "You have successfully checked in", Toast.LENGTH_LONG).show();

                        layoutOutletDashboard.setVisibility(RelativeLayout.VISIBLE);
                        layoutCheckIn.setVisibility(RelativeLayout.GONE);
                    }

                } catch (Exception ex) {
                    Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
                    DialogUtils.showAlertDialog(getActivity(), "Error while check-in");
                    return;
                }


            }
            break;

            case R.id.txtCustomerAssets:{

                try {

                    Bundle bundle = new Bundle();
                    bundle.putInt("CustomerId", customer.getCustomerId());
                    bundle.putString("customerJSON", getArguments().getString("customerJSON"));

                    AssetInfoFragment assetInfoFragment = new AssetInfoFragment();
                    assetInfoFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, assetInfoFragment);

                }catch (Exception ex){
                    Logger.Log(OutletDashboardNewFragment.class.getName(),ex);
                }

            }break;

        }

    }


}

package com.inventrax_pepsi.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.ImageAdapter;
import com.inventrax_pepsi.adapters.ImageViewPagerAdapter;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.pojos.Customer;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.CustomizedUserTarget;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.Route;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by android on 3/4/2016.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static int PAGE_SWITCH_TIME = 3000;
    private TextView txtScheduledOutlets, txtCoveredOutlets, txtOutletPercentageOfCoverage;
    private TextView txtTotalCallsMade, txtStrikeCalls, txtStrikeRate;
    private TextView txtSalesCases, txtDropSize, txtTotalLinesSold, txtLinesalesCall;
    private TextView txtCurrentOutlet, txtNextOutlet,txtCurrentOutletId, txtNextOutletId;
    private TextView txtMTDCurrentRunRateVolume, txtMTDCurrentRunRateSales, txtMTDRequiredRunRateVolume, txtMTDRequiredRunRateSales;
    private TextView txtYTDCurrentRunRateVolume, txtYTDCurrentRunRateSales, txtYTDRequiredRunRateVolume, txtYTDRequiredRunRateSales;
    private TextView txtAOP, txtAchieved, txtPlusMinus, txtRR, txtCR;
    private TextView txtTotalOrderValue,txtTotalCashCollected;
    private SFACommon sfaCommon;
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private Timer timer;
    private int page = 0;
    private View rootView;
    private Gson gson;
    private ImageButton btnRefresh;

    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;


    private BackgroundServiceFactory backgroundServiceFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_psr_dashboard_new, container, false);

        sfaCommon = SFACommon.getInstance();

        try
        {
            loadFormControls();

        }catch (Exception ex){
            Logger.Log(DashboardFragment.class.getName(),ex);
        }


        return rootView;
    }

    private void loadFormControls() {

        try {

            new ProgressDialogUtils(getActivity(), ProgressDialog.STYLE_SPINNER);

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());

            btnRefresh=(ImageButton)rootView.findViewById(R.id.btnRefresh);
            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {

                        new UserRunRateTask().execute();

                    } else {
                        DialogUtils.showAlertDialog(getActivity(), "Please enable internet");
                        return;
                    }

                }
            });

            txtScheduledOutlets = getTextView(R.id.txtScheduledOutlets);
            txtCoveredOutlets = getTextView(R.id.txtCoveredOutlets);
            txtOutletPercentageOfCoverage = getTextView(R.id.txtOutletPercentageOfCoverage);

            txtTotalCallsMade = getTextView(R.id.txtTotalCallsMade);
            txtStrikeCalls = getTextView(R.id.txtStrikeCalls);
            txtStrikeRate = getTextView(R.id.txtStrikeRate);

            txtSalesCases = getTextView(R.id.txtSalesCases);
            txtDropSize = getTextView(R.id.txtDropSize);
            txtTotalLinesSold = getTextView(R.id.txtTotalLinesSold);
            txtLinesalesCall = getTextView(R.id.txtLinesalesCall);

            txtCurrentOutlet = getTextView(R.id.txtCurrentOutlet);
            txtCurrentOutlet.setOnClickListener(this);
            txtNextOutlet = getTextView(R.id.txtNextOutlet);
            txtNextOutlet.setOnClickListener(this);
            txtCurrentOutletId = getTextView(R.id.txtCurrentOutletId);
            txtNextOutletId = getTextView(R.id.txtNextOutletId);

            /*txtMTDCurrentRunRateVolume = getTextView(R.id.txtMTDCurrentRunRateVolume);
            txtMTDCurrentRunRateSales = getTextView(R.id.txtMTDCurrentRunRateSales);
            txtMTDRequiredRunRateVolume = getTextView(R.id.txtMTDRequiredRunRateVolume);
            txtMTDRequiredRunRateSales = getTextView(R.id.txtMTDRequiredRunRateSales);


            txtYTDCurrentRunRateVolume = getTextView(R.id.txtYTDCurrentRunRateVolume);
            txtYTDCurrentRunRateSales = getTextView(R.id.txtYTDCurrentRunRateSales);
            txtYTDRequiredRunRateVolume = getTextView(R.id.txtYTDRequiredRunRateVolume);
            txtYTDRequiredRunRateSales = getTextView(R.id.txtYTDRequiredRunRateSales);*/

            txtAOP = getTextView(R.id.txtAOP);
            txtAchieved = getTextView(R.id.txtAchieved);
            txtPlusMinus = getTextView(R.id.txtPlusMinus);
            txtRR = getTextView(R.id.txtRR);
            txtCR = getTextView(R.id.txtCR);

            txtTotalOrderValue = getTextView(R.id.txtTotalOrderValue);
            txtTotalCashCollected = getTextView(R.id.txtTotalCashCollected);

           /* if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {

                new UserRunRateTask().execute();

            } else {
                DialogUtils.showAlertDialog(getActivity(), "Please enable internet");
                return;
            }*/


            /*setUpView();
            setTab();
            pageSwitcher(DashboardFragment.PAGE_SWITCH_TIME);*/


            if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                setupViewPager();

        } catch (Exception ex) {
            Logger.Log(DashboardFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }

    private void setupViewPager(){
        try
        {
            _mViewPager = (ViewPager)rootView.findViewById(R.id.viewPagerImageSlider);
            ArrayList<String> images = new ArrayList<>();
            images.add("1.png");
            images.add("2.png");
            images.add("3.png");
            images.add("4.png");
            images.add("5.png");
            images.add("6.png");
            images.add("7.png");
            _mViewPager.setAdapter(new ImageAdapter(images));
            /*InkPageIndicator inkPageIndicator = (InkPageIndicator)rootView.findViewById(R.id.ink_pager_indicator);
            inkPageIndicator.setViewPager(_mViewPager);*/
            _mViewPager.setCurrentItem(0);
            _mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            pageSwitcher(DashboardFragment.PAGE_SWITCH_TIME);

        }catch (Exception ex){
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }
    }

    private TextView getTextView(int id) {

        return (TextView) rootView.findViewById(id);
    }

    private void processResponse(String responseJSON) {

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


                        displayDashboardData(rootObject.getCustomizedUserTarget().get(0));


                    } else {
                        clearDashboardData();
                    }
                }

            } else {
                clearDashboardData();
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.InvalidResponse));
                return;

            }

        } catch (Exception ex) {
            clearDashboardData();
            Logger.Log(DashboardFragment.class.getName(), ex);
            return;

        }

    }

    private void displayDashboardData(CustomizedUserTarget customizedUserTarget) {

        try {
            txtOutletPercentageOfCoverage.setText("0");

            if (customizedUserTarget != null) {


                txtScheduledOutlets.setText("" + customizedUserTarget.getScheduledCalls());
                txtCoveredOutlets.setText("" + customizedUserTarget.getCompletedCalls());
                txtTotalCallsMade.setText("" + customizedUserTarget.getTotalCalls());
                //txtOutletPercentageOfCoverage.setText("" + customizedUserTarget.get);
                txtStrikeCalls.setText("" + customizedUserTarget.getStrikeCalls());
                txtStrikeRate.setText("" + customizedUserTarget.getStrikeRate());


                txtSalesCases.setText("" + customizedUserTarget.getmTDSale());
                txtDropSize.setText("" + customizedUserTarget.getDropSize());
                txtTotalLinesSold.setText("" + customizedUserTarget.getTotalLinesSold());
                txtLinesalesCall.setText("" + customizedUserTarget.getLineSalesCall());


              /* txtMTDCurrentRunRateVolume.setText("" + customizedUserTarget.getCurrVol());
               txtMTDCurrentRunRateSales.setText("" + customizedUserTarget.getmTDSale());

               txtMTDRequiredRunRateVolume.setText("" + customizedUserTarget.getbOMRequiredRunRate());
               txtMTDRequiredRunRateSales.setText("" + customizedUserTarget.getbOMSales());*/

                // Either booked order or lrb orders
                txtAOP.setText("" + customizedUserTarget.getaOPTarget());

                txtAchieved.setText("" + customizedUserTarget.getBookedOrders());
                txtPlusMinus.setText("" + (customizedUserTarget.getlRBVolumeOrders() - customizedUserTarget.getlRBVolumePlan()));
                txtRR.setText("" + customizedUserTarget.getbOMRequiredRunRate());
                txtCR.setText("" + customizedUserTarget.getCurrentRunRate());

                txtCurrentOutlet.setText("  " + (TextUtils.isEmpty(customizedUserTarget.getVisitedOutlet()) ? "" : customizedUserTarget.getVisitedOutlet()));
                txtNextOutlet.setText("  " + (TextUtils.isEmpty(customizedUserTarget.getNextOutlet()) ? "" : customizedUserTarget.getNextOutlet()));

                txtCurrentOutletId.setText(""+customizedUserTarget.getVisitedOutletId());
                txtNextOutletId.setText(""+customizedUserTarget.getNextOutletId());

            } else {

                clearDashboardData();
            }


        } catch (Exception ex) {

            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }


    }

    private void clearDashboardData() {
        try {
            txtScheduledOutlets.setText("0");
            txtCoveredOutlets.setText("0");
            txtTotalCallsMade.setText("0");
            txtOutletPercentageOfCoverage.setText("0");

            txtStrikeCalls.setText("0");
            txtDropSize.setText("0");
            txtTotalLinesSold.setText("0");
            txtLinesalesCall.setText("0");

            txtStrikeRate.setText("0");
            txtSalesCases.setText("0");

            txtAOP.setText("0");
            txtAchieved.setText("0");
            txtPlusMinus.setText("0");
            txtRR.setText("0");
            txtCR.setText("0");



            /*txtMTDCurrentRunRateVolume.setText("0");
            txtMTDCurrentRunRateSales.setText("0");
            txtMTDRequiredRunRateVolume.setText("0");
            txtMTDRequiredRunRateSales.setText("0");*/

            txtCurrentOutlet.setText("");
            txtNextOutlet.setText("");

            txtCurrentOutletId.setText("");
            txtNextOutletId.setText("");


        } catch (Exception ex) {
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_home));
        sfaCommon.displayDate(getActivity());

    }

    private void setUpView() {

        try {

            _mViewPager = (ViewPager) rootView.findViewById(R.id.viewPagerImageSlider);
            _adapter = new ImageViewPagerAdapter(getActivity(), getFragmentManager());
            _mViewPager.setAdapter(_adapter);
            _mViewPager.setCurrentItem(0);
            _mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        }catch (Exception ex){
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }

    }

    private void setTab() {

        try {

            _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int position) {

                    //  page=position;

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageSelected(int position) {
                    // TODO Auto-generated method stub
                    // page=position;

                }

            });

        }catch (Exception ex){
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }

    }

    private void pageSwitcher(int seconds) {
        try {
            timer = new Timer(); // At this line a new Thread will be created
            timer.scheduleAtFixedRate(new RemindTask(), 0, seconds); // delay
            // in
            // milliseconds
        } catch (Exception ex) {
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }
    }

    @Override
    public void onClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.txtCurrentOutlet: {

                    if (Integer.parseInt(TextUtils.isEmpty(txtCurrentOutletId.getText().toString()) ? "0" : txtCurrentOutletId.getText().toString()) == 0)
                        return;

                    Customer customer = tableCustomer.getCustomer(Integer.parseInt(TextUtils.isEmpty(txtCurrentOutletId.getText().toString()) ? "0" : txtCurrentOutletId.getText().toString()));

                    if (customer == null)
                        return;

                    Bundle bundle = new Bundle();

                    OutletDashboardNewFragment outletDashboardFragment = new OutletDashboardNewFragment();
                    bundle.putString("customerJSON", customer.getCompleteJSON());
                    outletDashboardFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletDashboardFragment);

                }
                break;

                case R.id.txtNextOutlet: {


                    if (Integer.parseInt(TextUtils.isEmpty(txtNextOutletId.getText().toString()) ? "0" : txtNextOutletId.getText().toString()) == 0)
                        return;

                    Customer customer = tableCustomer.getCustomer(Integer.parseInt(TextUtils.isEmpty(txtNextOutletId.getText().toString()) ? "0" : txtNextOutletId.getText().toString()));

                    if (customer == null)
                        return;

                    Bundle bundle = new Bundle();

                    OutletDashboardNewFragment outletDashboardFragment = new OutletDashboardNewFragment();
                    bundle.putString("customerJSON", customer.getCompleteJSON());
                    outletDashboardFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletDashboardFragment);


                }
                break;

            }
        }catch (Exception ex){
            Logger.Log(DashboardFragment.class.getName(),ex);
            return;
        }

    }

    private class UserRunRateTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {

            ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_SPINNER);

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                User user = AppController.getUser();
                List<Route> userRouteList = new ArrayList<>();
                Route route = null;

                if (user != null) {

                    RootObject rootObject = new RootObject();

                    rootObject.setServiceCode(ServiceCode.GET_USER_RUN_RATE);
                    rootObject.setLoginInfo(AppController.getLoginInfo());

                    if (user.getRouteList() != null)
                        for (RouteList routeList : user.getRouteList()) {

                            route = new Route();

                            route.setRouteCode(routeList.getRouteCode());
                            route.setRouteId(routeList.getRouteId());
                            route.setAuditInfo(user.getAuditInfo());

                            userRouteList.add(route);
                        }

                    rootObject.setRoutes(userRouteList);


                    List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                    PropertyInfo propertyInfo = new PropertyInfo();
                    propertyInfo.setName("jsonStr");
                    propertyInfo.setValue(gson.toJson(rootObject));
                    propertyInfo.setType(String.class);
                    propertyInfoList.add(propertyInfo);


                    return SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_USER_RUN_RATE);

                }


            } catch (Exception ex) {
                Logger.Log(DashboardFragment.class.getName(),ex);
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String responseJSON) {

            try {

                ProgressDialogUtils.closeProgressDialog();

                processResponse(responseJSON);

            }catch (Exception ex){
                Logger.Log(DashboardFragment.class.getName(),ex);
                return;
            }

        }
    }

    private class RemindTask extends TimerTask {


        @Override
        public void run() {

            try {

                // As the TimerTask run on a seprate thread from UI thread we have
                // to call runOnUiThread to do work on UI thread.
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        if (page > 6) { // Total 7 Slides
                            timer.cancel();
                            page = 0;
                            pageSwitcher(DashboardFragment.PAGE_SWITCH_TIME);
                        } else {
                            _mViewPager.setCurrentItem(page++);
                        }
                    }
                });

            } catch (Exception ex) {
                Logger.Log(DashboardFragment.class.getName(),ex);
                return;
            }

        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {

            try {

                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            } catch (Exception ex) {
                Logger.Log(DashboardFragment.class.getName(),ex);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

            final MenuItem menuItemSyncOrderInfo = menu.findItem(R.id.action_sync_order_info);
            menuItemSyncOrderInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItemSyncOrderInfo.setVisible(true);

            menuItemSyncOrderInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    try {

                        if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                            DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.internetenablemessage));
                            return false;
                        }

                        if (backgroundServiceFactory != null) {

                            backgroundServiceFactory.initiateOrderService();
                            backgroundServiceFactory.initiateInvoiceUpdateService();
                            backgroundServiceFactory.initiateUserTrackService();
                            backgroundServiceFactory.initiateCustomerReturnService();
                            backgroundServiceFactory.initiateAssetComplaintService();
                            backgroundServiceFactory.initiateAssetCaptureService();
                            backgroundServiceFactory.initiateReadySaleInvoiceService();
                            backgroundServiceFactory.initiateAssetPulloutService();
                            backgroundServiceFactory.initiateCustomerService();
                            backgroundServiceFactory.initiateAssetRequestService();
                            backgroundServiceFactory.initiateCustomerTransactionService();

                        }

                        DialogUtils.showAlertDialog(getActivity(), "Syncing started, Please Wait ...");

                    }catch (Exception ex){
                        Logger.Log(DashboardFragment.class.getName(),ex);
                    }

                    return false;
                }
            });


        }

        super.onCreateOptionsMenu(menu, inflater);

    }

}


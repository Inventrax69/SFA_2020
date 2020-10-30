package com.inventrax_pepsi.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.fragments.AboutFragment;
import com.inventrax_pepsi.fragments.CheckAssetFragment;
import com.inventrax_pepsi.fragments.DashboardFragment;
import com.inventrax_pepsi.fragments.DaySummaryFragment;
import com.inventrax_pepsi.fragments.DeliveryListFragment;
import com.inventrax_pepsi.fragments.DrawerFragment;
import com.inventrax_pepsi.fragments.HomeFragment;
import com.inventrax_pepsi.fragments.NewOutletListFragment;
import com.inventrax_pepsi.fragments.OrderHistoryListFragment;
import com.inventrax_pepsi.fragments.OutletListNewFragment;
import com.inventrax_pepsi.fragments.OutletRegistrationFragment;
import com.inventrax_pepsi.fragments.PSRDaySummaryFragment;
import com.inventrax_pepsi.fragments.PlanogramFragment;
import com.inventrax_pepsi.fragments.SchemeListFragment;
import com.inventrax_pepsi.fragments.StockUploadFragment;
import com.inventrax_pepsi.fragments.TechnicianOutletListFragment;
import com.inventrax_pepsi.fragments.VehicleStockFragment;
import com.inventrax_pepsi.interfaces.MainActivityView;
import com.inventrax_pepsi.model.NavDrawerItem;
import com.inventrax_pepsi.model.Selectedlanguage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.logout.LogoutUtil;
import com.inventrax_pepsi.sfa.pojos.RouteList;
import com.inventrax_pepsi.util.AndroidUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SharedPreferencesUtils;
import com.prowesspride.api.Setup;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;


/**
 * Author   : Naresh P.
 * Date		: 28/03/2016 08:49 AM
 * Purpose	: Main Activity
 */

public class MainActivity extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener, MainActivityView {

    private Toolbar mToolbar;
    private DrawerFragment drawerFragment;
    private FragmentUtils fragmentUtils;
    private CharSequence[] userRouteCharSequences;
    private List<String> userRouteStringList;
    private String selectedRouteCode;
    private FragmentActivity fragmentActivity;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private BackgroundServiceFactory backgroundServiceFactory;
    private LogoutUtil logoutUtil;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            // For Crash Report Custom Data
            ACRA.getErrorReporter().putCustomData(AppController.getUser().getLoginUserId() + "["+AppController.getUser().getUserId()+"]" , ( ( AppController.getUser().getRouteList()!=null && AppController.getUser().getRouteList().size() > 0 ) ? AppController.getUser().getRouteList().get(0).getRouteName() : "" ) );

            try {
                Setup setup = new Setup();

                boolean result = setup.blActivateLibrary(this, R.raw.licencefull_pride_gen);

                AppController.setup = setup;

                Log.v("Bl Result : ", "" + (result == true ? "Yes" : "No"));

            } catch (Exception ex) {
                Logger.Log(AppController.class.getName(), ex);
            }

            setContentView(R.layout.activity_main);

            loadFormControls();
             resources=getResources();
            // display the first navigation drawer view on app launch
            if(Selectedlanguage.getLanguage()==1)
            {
                displayView(0, new NavDrawerItem(false,resources.getString(R.string.nav_item_home)));
            }
            else{
                displayView(0, new NavDrawerItem(false, "Home"));
            }

        } catch (Exception ex) {
            Logger.Log(MainActivity.class.getName(), ex);
        }
    }

    public void loadFormControls() {
        try {

            logoutUtil = new LogoutUtil();

            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            fragmentUtils = new FragmentUtils();

            fragmentActivity = this;

            new ProgressDialogUtils(this);

            AbstractApplication.FRAGMENT_ACTIVITY = this;

            setSupportActionBar(mToolbar);

           /* if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }*/

            View logoView = AndroidUtils.getToolbarLogoIcon(mToolbar);

            if (logoView != null)
                logoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, new HomeFragment());
                    }
                });

            drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(this);

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(this);
            backgroundServiceFactory.setMainActivityView(this);
            backgroundServiceFactory.setContext(this);

            sharedPreferencesUtils = new SharedPreferencesUtils("LoginActivity", getApplicationContext());

            userRouteStringList = new ArrayList<>();

            for (RouteList userRoute : AppController.getUser().getRouteList()) {
                userRouteStringList.add(userRoute.getRouteCode());
            }

            userRouteCharSequences = userRouteStringList.toArray(new CharSequence[userRouteStringList.size()]);

            logoutUtil.setActivity(this);
            logoutUtil.setBackgroundServiceFactory(backgroundServiceFactory);
            logoutUtil.setFragmentActivity(fragmentActivity);
            logoutUtil.setMainActivityView(this);
            logoutUtil.setSharedPreferencesUtils(sharedPreferencesUtils);


        } catch (Exception ex) {
            DialogUtils.showAlertDialog(this, "Error while loading form controls");
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_logout: {
                logoutUtil.performLogoutOperation();
            }
            break;

            case R.id.action_settings: {
                return true;
            }

            case R.id.action_about: {
                FragmentUtils.replaceFragmentWithBackStack(this, R.id.container_body, new AboutFragment());
            }
            break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDrawerItemSelected(View view, int position, NavDrawerItem menuItem) {

        displayView(position, menuItem);
    }

    private int GetmenuIndex(String menuTitle) {
        String[] resourcevalues={resources.getString(R.string.nav_item_home),resources.getString(R.string.nav_item_outlet_list),resources.getString(R.string.nav_item_delivery_list),resources.getString(R.string.nav_item_order_history),resources.getString(R.string.nav_item_outlet_registration),resources.getString(R.string.nav_item_schemes_list),resources.getString(R.string.StockDetails),resources.getString(R.string.nav_item_planogram),resources.getString(R.string.nav_item_check_asset),resources.getString(R.string.nav_item_new_outlet_list),resources.getString(R.string.nav_item_day_summary),resources.getString(R.string.nav_item_psr_day_summary),resources.getString(R.string.nav_item_stock_upload),resources.getString(R.string.nav_item_technician)};
        for (int i = 0; i < resourcevalues.length; i++) {
            if (resourcevalues[i].equals(menuTitle)) {
                return i;
            }
        }
        return -1;
    }

    private void displayView(int position, NavDrawerItem menuItem) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        resources=getResources();
        //String[] resourcevalues={resources.getString(R.string.nav_item_home),resources.getString(R.string.nav_item_outlet_list)};
        Bundle bundle = new Bundle();

        if (userRouteStringList != null && userRouteStringList.size() > 0) {
            if (userRouteStringList.size() == 1) {
                bundle.putString("RouteCode", userRouteStringList.get(0));
            }
        }

        System.out.println(resources.getString(R.string.nav_item_home));
        switch (GetmenuIndex(menuItem.getTitle())) {
            case 0:
                fragment = new DashboardFragment();

                title = getString(R.string.title_home);
                break;
            case 1: {

                fragment = new OutletListNewFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_outlet_list);
            }
            break;
            case 2: {

                fragment = new DeliveryListFragment();
                fragment.setArguments(bundle);

                title = getString(R.string.title_delivery_list);

            }

            break;
            case 3:
                Bundle bundleOrder = new Bundle();
                bundleOrder.putInt("customerId", 0);
                fragment = new OrderHistoryListFragment();
                fragment.setArguments(bundleOrder);
                title = getString(R.string.title_order_history);
                break;
            case 4: {

                fragment = new OutletRegistrationFragment();
                fragment.setArguments(bundle);
                title = getString(R.string.title_outlet_registration);
            }

                break;
            case 5: {
                fragment = new SchemeListFragment();
                title = getString(R.string.title_scheme_list);
            }
            break;
            case 6: {
                fragment = new VehicleStockFragment();
                title = getString(R.string.StockDetails);
            }
            break;
            case 7: {
                fragment = new PlanogramFragment();
                title = getString(R.string.title_planogram);
            }
            break;

            case 8:{

                fragment = new CheckAssetFragment();
                title = getString(R.string.title_check_asset);

            }break;

            case 9:

                fragment = new NewOutletListFragment();
                title = getString(R.string.title_new_outlet_list);
                break;
            case 10:{

                fragment = new DaySummaryFragment();
                title = getString(R.string.title_day_summary);

            }break;

            case 11:{

                fragment = new PSRDaySummaryFragment();
                title = getString(R.string.title_psr_day_summary);

            }break;

            case 12:{
                Bundle bundleCust = new Bundle();
                bundleCust.putInt("customerId", 0);
                fragment = new StockUploadFragment();
                fragment.setArguments(bundleCust);
                title = getString(R.string.nav_item_stock_upload);

            }break;

            case 13:{

                fragment = new TechnicianOutletListFragment();
                title = getString(R.string.title_new_outlet_list);

            }break;

            default:

        }

        if (fragment != null) {

            fragmentUtils.replaceFragmentWithBackStack(this, R.id.container_body, fragment);

            // set the toolbar title
            getSupportActionBar().setTitle(title);

        }
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

       /* try {

            EnterpriseDeviceManager enterpriseDeviceManager = (EnterpriseDeviceManager)getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);

            RestrictionPolicy restrictionPolicy = enterpriseDeviceManager.getRestrictionPolicy();

            restrictionPolicy.allowSettingsChanges(false);

        }catch (Exception ex){

        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initiateBackgroundServices() {
        try {

            if (AppController.getUser().getUserTypeId() == 7) {

                backgroundServiceFactory.initiateCustomerOrderHistoryService();
                backgroundServiceFactory.initiateSKUListService();
                backgroundServiceFactory.initiateTodayOrderService();
                backgroundServiceFactory.initiateDiscountService();
                backgroundServiceFactory.initiateDeliveryListService();
                backgroundServiceFactory.initiateLoadOutService();


            } else {


                backgroundServiceFactory.initiateCustomerOrderHistoryService();
                backgroundServiceFactory.initiateSKUListService();
                backgroundServiceFactory.initiateTodayOrderService();
                backgroundServiceFactory.initiateDiscountService();

            }

        } catch (Exception ex) {
            Logger.Log(MainActivity.class.getName(), ex);
            return;
        }
    }

    @Override
    public void showSKUListStatus(String message, int status) {

        switch (status) {
            case 0: {
                ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_HORIZONTAL, message);
            }
            break;
            case 1: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            }
            break;
            case 2: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void showOutletListStatus(String message, int status) {

        switch (status) {
            case 0: {
                ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_HORIZONTAL, message);

            }
            break;
            case 1: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                initiateBackgroundServices();

            }
            break;
            case 2: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            break;
        }

    }

    @Override
    public void showDiscountListStatus(String message, int status) {
        switch (status) {
            case 0: {

                ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_HORIZONTAL, message);

            }
            break;
            case 1: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


            }
            break;
            case 2: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void showDeliveryListStatus(String message, int status) {
        switch (status) {
            case 0: {

                ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_HORIZONTAL, message);

            }
            break;
            case 1: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


            }
            break;
            case 2: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void showLoadOutListStatus(String message, int status) {
        switch (status) {
            case 0: {

                ProgressDialogUtils.showProgressDialog(ProgressDialog.STYLE_HORIZONTAL, message);

            }
            break;
            case 1: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            }
            break;
            case 2: {
                ProgressDialogUtils.closeProgressDialog();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }


    public class UserRouteSingleChoiceDialogClickListener implements DialogInterface.OnClickListener {

        private Fragment fragment;
        private String title;
        private Bundle bundle;

        public UserRouteSingleChoiceDialogClickListener(Fragment fragment, String title) {

            this.fragment = fragment;
            this.title = title;
            bundle = new Bundle();

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    dialog.dismiss();
                }
                break;
                case DialogInterface.BUTTON_NEGATIVE: {
                    dialog.dismiss();
                }
                break;

                default: {
                    selectedRouteCode = (String) userRouteCharSequences[which];
                    dialog.dismiss();

                    if (fragment != null) {

                        bundle.putString("RouteCode", selectedRouteCode);
                        fragment.setArguments(bundle);

                        fragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, fragment);

                        // set the toolbar title
                        getSupportActionBar().setTitle(title);

                    }
                }
                break;
            }
        }
    }





}
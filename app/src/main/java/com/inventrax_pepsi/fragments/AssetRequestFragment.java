package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetRequest;
import com.inventrax_pepsi.database.TableCustomerOrderHistory;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AssetRequest;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.BrandHistory;
import com.inventrax_pepsi.sfa.pojos.CustSKUOrder;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentGUI;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

import java.util.List;

/**
 * Created by android on 5/7/2016.
 */
public class AssetRequestFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private SFACommon sfaCommon;
    private Gson gson;
    private Customer customer;
    private FloatingActionButton fabReturn;
    private InputFilter inputFilter;
    private String blockCharacterSet = "'~#^|$%&*!\"()?=+_";


    private EditText input_pre_pi_volume,input_pre_ko_volume,input_pre_mix_volume,input_pro_pi_volume,input_pro_ko_volume,input_pro_mix_volume;
    private EditText input_pre_pi_cooler_size,input_pre_ko_cooler_size,input_pre_mix_cooler_size,input_pro_pi_cooler_size,input_pro_ko_cooler_size,input_pro_mix_cooler_size;
    private CheckBox chk_pre_pi_volume,chk_pre_ko_volume,chk_pre_mix_volume,chk_pro_pi_volume,chk_pro_ko_volume,chk_pro_mix_volume;
    private EditText input_pre_pi_glasses,input_pre_ko_glasses,input_pre_mix_glasses,input_pro_pi_glasses,input_pro_ko_glasses,input_pro_mix_glasses;
    private RadioGroup rbgCoolerType;
    private RadioButton rbVISI,rbEBC;

    private EditText inputRemarks;
    private Button btnCancel,btnSubmit;

    private DatabaseHelper databaseHelper;
    private TableAssetRequest tableAssetRequest;
    private TableCustomerOrderHistory tableCustomerOrderHistory;
    private TableJSONMessage tableJSONMessage;
    private BackgroundServiceFactory backgroundServiceFactory;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView= inflater.inflate(R.layout.fragment_asset_request,container,false);

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

        FragmentGUI.setView(rootView);


        loadFormControls();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_asset_request));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_asset_request));

    }

    private void loadFormControls(){

        try
        {

            inputFilter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source != null && blockCharacterSet.contains(("" + source))) {
                        return "";
                    }
                    return null;
                }
            };

            fabReturn = (FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            gson = new GsonBuilder().create();

            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);


            input_pre_pi_volume = FragmentGUI.getEditText(R.id.input_pre_pi_volume);
            input_pre_ko_volume = FragmentGUI.getEditText(R.id.input_pre_ko_volume);
            input_pre_mix_volume = FragmentGUI.getEditText(R.id.input_pre_mix_volume);
            input_pro_pi_volume = FragmentGUI.getEditText(R.id.input_pro_pi_volume);
            input_pro_ko_volume = FragmentGUI.getEditText(R.id.input_pro_ko_volume);
            input_pro_mix_volume = FragmentGUI.getEditText(R.id.input_pro_mix_volume);

            input_pre_pi_cooler_size = FragmentGUI.getEditText(R.id.input_pre_pi_cooler_size);
            input_pre_ko_cooler_size = FragmentGUI.getEditText(R.id.input_pre_ko_cooler_size);
            input_pre_mix_cooler_size = FragmentGUI.getEditText(R.id.input_pre_mix_cooler_size);
            input_pro_pi_cooler_size = FragmentGUI.getEditText(R.id.input_pro_pi_cooler_size);
            input_pro_ko_cooler_size = FragmentGUI.getEditText(R.id.input_pro_ko_cooler_size);
            input_pro_mix_cooler_size = FragmentGUI.getEditText(R.id.input_pro_mix_cooler_size);

            chk_pre_pi_volume = FragmentGUI.getCheckBox(R.id.chk_pre_pi_volume);
            chk_pre_ko_volume = FragmentGUI.getCheckBox(R.id.chk_pre_ko_volume);
            chk_pre_mix_volume = FragmentGUI.getCheckBox(R.id.chk_pre_mix_volume);
            chk_pro_pi_volume = FragmentGUI.getCheckBox(R.id.chk_pro_pi_volume);
            chk_pro_ko_volume = FragmentGUI.getCheckBox(R.id.chk_pro_ko_volume);
            chk_pro_mix_volume = FragmentGUI.getCheckBox(R.id.chk_pro_mix_volume);

            input_pre_pi_glasses = FragmentGUI.getEditText(R.id.input_pre_pi_glasses);
            input_pre_ko_glasses = FragmentGUI.getEditText(R.id.input_pre_ko_glasses);
            input_pre_mix_glasses = FragmentGUI.getEditText(R.id.input_pre_mix_glasses);
            input_pro_pi_glasses = FragmentGUI.getEditText(R.id.input_pro_pi_glasses);
            input_pro_ko_glasses = FragmentGUI.getEditText(R.id.input_pro_ko_glasses);
            input_pro_mix_glasses = FragmentGUI.getEditText(R.id.input_pro_mix_glasses);

            rbgCoolerType = (RadioGroup) rootView.findViewById(R.id.rbgCoolerType);
            rbVISI = (RadioButton)rootView.findViewById(R.id.rbVISI);
            rbEBC = (RadioButton)rootView.findViewById(R.id.rbEBC);;

            inputRemarks = FragmentGUI.getEditText(R.id.inputRemarks);
            inputRemarks.setFilters(new InputFilter[] { inputFilter });

            btnCancel=FragmentGUI.getButton(R.id.btnCancel);
            btnCancel.setOnClickListener(this);

            btnSubmit=FragmentGUI.getButton(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);

            databaseHelper = DatabaseHelper.getInstance();
            tableAssetRequest = databaseHelper.getTableAssetRequest();
            tableCustomerOrderHistory=databaseHelper.getTableCustomerOrderHistory();
            tableJSONMessage=databaseHelper.getTableJSONMessage();

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());

            displayPresentStatus();

        }catch (Exception ex){
            Logger.Log(AssetRequestFragment.class.getName(),ex);
            return;
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fabReturn: {

                doReturn();

            }
            break;

            case R.id.btnCancel:{

                Bundle bundle=new Bundle();
                bundle.putString("customerJSON",getArguments().getString("customerJSON"));
                AssetRequestFragment assetRequestFragment=new AssetRequestFragment();
                assetRequestFragment.setArguments(bundle);

                FragmentUtils.replaceFragmentWithBackStack(getActivity(),R.id.container_body,assetRequestFragment);

            }break;

            case R.id.btnSubmit:{

                submitRequestForm();

            }break;


        }

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
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }
    }

    private void submitRequestForm(){

        try
        {

            if (TextUtils.isEmpty(input_pro_pi_cooler_size.getText().toString()))
            {
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Proposedcoolersizecannotbeempty));
                return;
            }

            ProgressDialogUtils.showProgressDialog("Please wait ...");


            AssetRequest assetRequest=new AssetRequest();

            assetRequest.setCustomerId(customer.getCustomerId());
            assetRequest.setAssetType(((RadioButton) rootView.findViewById(rbgCoolerType.getCheckedRadioButtonId())).getText().toString().trim());
            assetRequest.setAssetTypeId(((RadioButton) rootView.findViewById(rbgCoolerType.getCheckedRadioButtonId())).getText().toString().trim().equalsIgnoreCase("VISI")?1:2);
            assetRequest.setCustomerName(customer.getCustomerName());
            assetRequest.setProposedCustomerSales(Double.parseDouble(TextUtils.isEmpty(input_pro_pi_volume.getText().toString().trim()) == true ?"0":input_pro_pi_volume.getText().toString().trim()));
            assetRequest.setProposedVolume(Double.parseDouble(TextUtils.isEmpty(input_pro_pi_volume.getText().toString().trim()) == true ?"0":input_pro_pi_volume.getText().toString().trim()));
            assetRequest.setRemarks(inputRemarks.getText().toString().trim());
            assetRequest.setRouteCode(customer.getOutletProfile().getRouteCode());
            assetRequest.setCoolerVolume(input_pro_pi_cooler_size.getText().toString());
            assetRequest.setRequestedVolume(input_pro_pi_cooler_size.getText().toString());
            assetRequest.setHasNightGaurd(chk_pro_pi_volume.isChecked()?1:0);
            assetRequest.setPriorityId(3);


            Double.parseDouble(TextUtils.isEmpty(input_pre_pi_volume.getText().toString().trim()) == true ?"0":input_pre_pi_volume.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pre_ko_volume.getText().toString().trim()) == true ?"0":input_pre_ko_volume.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pre_mix_volume.getText().toString().trim()) == true ?"0":input_pre_mix_volume.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pro_ko_volume.getText().toString().trim()) == true ?"0":input_pro_ko_volume.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pro_mix_volume.getText().toString().trim()) == true ?"0":input_pro_mix_volume.getText().toString().trim());


            input_pre_ko_cooler_size.getText().toString();
            input_pre_mix_cooler_size.getText().toString();
            input_pro_pi_cooler_size.getText().toString();
            input_pro_ko_cooler_size.getText().toString();
            input_pro_mix_cooler_size.getText().toString();

            chk_pre_pi_volume.isChecked();
            chk_pre_ko_volume.isChecked();
            chk_pre_mix_volume.isChecked();
            chk_pro_ko_volume.isChecked();
            chk_pro_mix_volume.isChecked();

            Double.parseDouble(TextUtils.isEmpty(input_pre_pi_glasses.getText().toString().trim()) == true ?"0":input_pre_pi_glasses.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pre_ko_glasses.getText().toString().trim()) == true ?"0":input_pre_ko_glasses.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pre_mix_glasses.getText().toString().trim()) == true ?"0":input_pre_mix_glasses.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pro_pi_glasses.getText().toString().trim()) == true ?"0":input_pro_pi_glasses.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pro_ko_glasses.getText().toString().trim()) == true ?"0":input_pro_ko_glasses.getText().toString().trim());
            Double.parseDouble(TextUtils.isEmpty(input_pro_mix_glasses.getText().toString().trim()) == true ?"0":input_pro_mix_glasses.getText().toString().trim());


            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));

            assetRequest.setAuditInfo(auditInfo);


            com.inventrax_pepsi.database.pojos.AssetRequest localAssetRequest=new com.inventrax_pepsi.database.pojos.AssetRequest();

            localAssetRequest.setCustomerId(customer.getCustomerId());
            localAssetRequest.setJson(gson.toJson(assetRequest));

            long auto_inc_id = tableAssetRequest.createAssetRequest(localAssetRequest);

            if ( auto_inc_id>0 ){

                JSONMessage jsonMessage=new JSONMessage();

                jsonMessage.setJsonMessage(localAssetRequest.getJson());
                jsonMessage.setNotificationTypeId(JsonMessageNotificationType.AssetRequest.getNotificationType());
                jsonMessage.setNotificationId((int)auto_inc_id);
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);

                long json_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if ( json_auto_inc_id > 0 ){

                    localAssetRequest.setAutoIncId((int)auto_inc_id);
                    localAssetRequest.setJsonMessageAutoIncId((int)json_auto_inc_id);

                    tableAssetRequest.updateAssetRequest(localAssetRequest);

                    if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                    {
                        backgroundServiceFactory.initiateAssetRequestService();
                    }

                    ProgressDialogUtils.closeProgressDialog();

                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Assetrequestupdatedsuccessfully));

                }else {
                    ProgressDialogUtils.closeProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Errorwhileupdatingassetrequest));
                    return;
                }

            }else {
                ProgressDialogUtils.closeProgressDialog();
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Errorwhileupdatingassetrequest));
                return;
            }


        }catch (Exception ex){

            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(AssetRequestFragment.class.getName(),ex);
            return;
        }

    }

    private void displayPresentStatus(){
        try
        {
            if (customer==null)
                return;

            CustomerOrderHistory customerOrderHistory=tableCustomerOrderHistory.getCustomerOrderHistory(customer.getCustomerId());

            double piVolume=0;

            if (customerOrderHistory != null) {

                CustSKUOrder custSKUOrder = gson.fromJson(customerOrderHistory.getBrandJSON(), CustSKUOrder.class);

                List<BrandHistory> brandHistories = custSKUOrder.getBrandHistory();

                if (brandHistories != null && brandHistories.size() > 0) {

                    for (BrandHistory brandHistory:brandHistories){

                        piVolume+=brandHistory.getQuantity();

                    }

                    input_pre_pi_volume.setText(""+piVolume);

                }
            }

        }catch (Exception ex){
            Logger.Log(AssetRequestFragment.class.getName(),ex);
            return;
        }
    }
}

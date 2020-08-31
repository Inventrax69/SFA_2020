package com.inventrax_pepsi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetCapture;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetCapture;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AssetCaptureHistory;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerAsset;
import com.inventrax_pepsi.sfa.pojos.CustomerAuditInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentGUI;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.QRScannerUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by android on 4/12/2016.
 */
public class AssetAuditFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Gson gson;
    private Customer customer;
    private SFACommon sfaCommon;

    private EditText inputAssetSerialNo,inputRemarks;
    private ImageView btnScan;
    private Button btnSubmit;
    private TextView Lastauditdate;

    private EditText input_pre_pi_volume,input_pre_ko_volume,input_pre_mix_volume;
    private EditText input_pre_pi_cooler_size,input_pre_ko_cooler_size,input_pre_mix_cooler_size;
    private CheckBox chk_pre_pi_volume,chk_pre_ko_volume,chk_pre_mix_volume;
    private EditText input_pre_pi_glasses,input_pre_ko_glasses,input_pre_mix_glasses;

    private FrameLayout frmlAudittHistoryList,frmlLastAuditDate;
    private TableLayout tblAuditHistory,tbleLastAuditDate;
    private DatabaseHelper databaseHelper;
    private TableJSONMessage tableJSONMessage;
    private BackgroundServiceFactory backgroundServiceFactory;
    private TableAssetCapture tableAssetCapture;
    private FloatingActionButton fabReturn;
    private  TableLayout.LayoutParams tableLayoutParams;
    private TableRow.LayoutParams tableRowParams;
    private Resources resources;


    private GPSLocationService gpsLocationService;

    private InputFilter inputFilter;
    private String blockCharacterSet = "'~#^|$%&*!\"()?=+_";

    private IntentFilter mIntentFilter;
    private AssetCaptureBroadcastReceiver assetCaptureBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_asset_audit,container,false);

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

        FragmentGUI.setView(rootView);

        sfaCommon=SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();


        return rootView;
    }

    private void loadFormControls(){
        try {

            inputFilter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source != null && blockCharacterSet.contains(("" + source))) {
                        return "";
                    }
                    return null;
                }
            };

            gson = new GsonBuilder().create();

            databaseHelper=DatabaseHelper.getInstance();
            tableJSONMessage=databaseHelper.getTableJSONMessage();
            tableAssetCapture=databaseHelper.getTableAssetCapture();

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.assetcapture");

            assetCaptureBroadcastReceiver = new AssetCaptureBroadcastReceiver();

            this.customer=(Customer)gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            gpsLocationService=new GPSLocationService(getContext());

            inputAssetSerialNo=(EditText)rootView.findViewById(R.id.inputAssetSerialNo);
            inputRemarks=(EditText)rootView.findViewById(R.id.inputRemarks);
            inputRemarks.setFilters(new InputFilter[] { inputFilter });

            btnScan=(ImageView)rootView.findViewById(R.id.btnScan);
            btnScan.setOnClickListener(this);
            btnSubmit=(Button)rootView.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);

            fabReturn = (FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            frmlAudittHistoryList=(FrameLayout)rootView.findViewById(R.id.frmlAudittHistoryList);
            tblAuditHistory=(TableLayout)rootView.findViewById(R.id.tblAuditHistory);
            Lastauditdate=(TextView) rootView.findViewById(R.id.txtLastauditDate);

            frmlLastAuditDate= (FrameLayout) rootView.findViewById(R.id.frmlLastAuditDate);
            tbleLastAuditDate = (TableLayout) rootView.findViewById(R.id.tblLastAuditdate);
            input_pre_pi_volume = FragmentGUI.getEditText(R.id.input_pre_pi_volume);
            input_pre_pi_volume.setFilters(new InputFilter[] { inputFilter });
            input_pre_ko_volume = FragmentGUI.getEditText(R.id.input_pre_ko_volume);
            input_pre_ko_volume.setFilters(new InputFilter[] { inputFilter });
            input_pre_mix_volume = FragmentGUI.getEditText(R.id.input_pre_mix_volume);
            input_pre_mix_volume.setFilters(new InputFilter[] { inputFilter });

            input_pre_pi_cooler_size = FragmentGUI.getEditText(R.id.input_pre_pi_cooler_size);
            input_pre_pi_cooler_size.setFilters(new InputFilter[] { inputFilter });
            input_pre_ko_cooler_size = FragmentGUI.getEditText(R.id.input_pre_ko_cooler_size);
            input_pre_ko_cooler_size.setFilters(new InputFilter[] { inputFilter });
            input_pre_mix_cooler_size = FragmentGUI.getEditText(R.id.input_pre_mix_cooler_size);
            input_pre_mix_cooler_size.setFilters(new InputFilter[] { inputFilter });

            chk_pre_pi_volume = FragmentGUI.getCheckBox(R.id.chk_pre_pi_volume);
            chk_pre_ko_volume = FragmentGUI.getCheckBox(R.id.chk_pre_ko_volume);
            chk_pre_mix_volume = FragmentGUI.getCheckBox(R.id.chk_pre_mix_volume);

            input_pre_pi_glasses = FragmentGUI.getEditText(R.id.input_pre_pi_glasses);
            input_pre_pi_glasses.setFilters(new InputFilter[] { inputFilter });
            input_pre_ko_glasses = FragmentGUI.getEditText(R.id.input_pre_ko_glasses);
            input_pre_ko_glasses.setFilters(new InputFilter[] { inputFilter });
            input_pre_mix_glasses = FragmentGUI.getEditText(R.id.input_pre_mix_glasses);
            input_pre_mix_glasses.setFilters(new InputFilter[] { inputFilter });
            resources=getResources();
            // check if GPS enabled
            if (!gpsLocationService.canGetLocation()) {
                // Ask user to enable GPS/network in settings
                gpsLocationService.showSettingsAlert();
                return;
            }
            Lastauditdate();
            displayAuditHistory();

        }catch (Exception ex){
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(assetCaptureBroadcastReceiver, mIntentFilter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_asset_audit));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_asset_audit));

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(assetCaptureBroadcastReceiver);
        super.onPause();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnScan:{

                QRScannerUtils.scanQRCode(this);

            }break;

            case R.id.btnSubmit:{

                /*if (TextUtils.isEmpty(inputAssetSerialNo.getText().toString())){
                    DialogUtils.showAlertDialog(getActivity(), "Please scan asset serial number");
                    return;
                }*/

                processAuditRequest();

            }break;

            case R.id.fabReturn:{

                doReturn();

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
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try
        {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents())) {

                if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents()) && scanResult.getContents().trim().length()> 10 )
                {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleasescanvalidserialnumber));
                    return;
                }

                if (customer!=null && customer.getCustomerAssets()!=null && customer.getCustomerAssets().size()>0 )
                {
                    if (!checkCustomerAsset(scanResult.getContents().toString().trim()))
                    {
                        showAssetNotAvailableMessage();
                        inputAssetSerialNo.setText("");
                        return;
                    }

                }else {
                    showAssetNotAvailableMessage();
                    inputAssetSerialNo.setText("");
                    return;
                }


                inputAssetSerialNo.setText(scanResult.getContents().toString().trim()+"");
                inputAssetSerialNo.setEnabled(false);

            }

        }catch (Exception ex){
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }

    }


    private void processAuditRequest() {

        try
        {

            if (customer!=null && customer.getCustomerAssets()!=null && customer.getCustomerAssets().size()>0 )
            {
                if (!checkCustomerAsset(inputAssetSerialNo.getText().toString().trim()))
                {
                    showAssetNotAvailableMessage();
                    inputAssetSerialNo.setText("");
                    return;
                }

            }else {
                showAssetNotAvailableMessage();
                inputAssetSerialNo.setText("");
                return;
            }


            ProgressDialogUtils.showProgressDialog(AbstractApplication.get().getString(R.string.Loadingmessage));

            CustomerAuditInfo customerAuditInfo=new CustomerAuditInfo();

            customerAuditInfo.setCustomerCode(customer.getCustomerCode());
            customerAuditInfo.setCustomerId(customer.getCustomerId());
            customerAuditInfo.setAccountType(customer.getOutletProfile().getAccountTypeId());
            customerAuditInfo.setCcxAssetVolume(input_pre_ko_cooler_size.getText().toString().trim());
            customerAuditInfo.setPciAssetVolume(input_pre_pi_cooler_size.getText().toString().trim());
            customerAuditInfo.setNoOfGlasses(Integer.parseInt(TextUtils.isEmpty(input_pre_pi_glasses.getText().toString().trim()) == true ?"0":input_pre_pi_glasses.getText().toString().trim()));
            customerAuditInfo.setCcxVolPerYearInBottles(Double.parseDouble(TextUtils.isEmpty(input_pre_ko_volume.getText().toString().trim()) == true ?"0":input_pre_ko_volume.getText().toString().trim()));
            customerAuditInfo.setVolPerDayInBottles(Double.parseDouble(TextUtils.isEmpty(input_pre_pi_volume.getText().toString().trim()) == true ?"0":input_pre_pi_volume.getText().toString().trim()));
            customerAuditInfo.setVolPerYearInBottles(Double.parseDouble(TextUtils.isEmpty(input_pre_pi_volume.getText().toString().trim()) == true ?"0":input_pre_pi_volume.getText().toString().trim()));
            customerAuditInfo.setAssetCaptureHistories(new ArrayList<AssetCaptureHistory>());

            AssetCaptureHistory assetCaptureHistory=new AssetCaptureHistory();

            assetCaptureHistory.setCustomerId(customer.getCustomerId());
            assetCaptureHistory.setqRCode(inputAssetSerialNo.getText().toString());
            assetCaptureHistory.setRemarks(inputRemarks.getText().toString());

            assetCaptureHistory.setLatitude(gpsLocationService.getLatitude()+"");
            assetCaptureHistory.setLongitude(gpsLocationService.getLongitude()+"");





            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));

            assetCaptureHistory.setAuditInfo(auditInfo);
            customerAuditInfo.setAuditInfo(auditInfo);
            customerAuditInfo.getAssetCaptureHistories().add(assetCaptureHistory);


            AssetCapture assetCapture=new AssetCapture();
            assetCapture.setJson(gson.toJson(customerAuditInfo));
            assetCapture.setCustomerId(customer.getCustomerId());
            assetCapture.setQRCode(inputAssetSerialNo.getText().toString());

            long auto_inc_id = tableAssetCapture.createAssetCapture(assetCapture);

            if (auto_inc_id!=0){

                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(assetCapture.getJson());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId((int) auto_inc_id);
                jsonMessage.setNotificationTypeId(JsonMessageNotificationType.AssetCapture.getNotificationType()); // For Asset Capture Notification Type Id is 6

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    assetCapture.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    assetCapture.setAutoIncId((int) auto_inc_id);

                    tableAssetCapture.updateAssetCapture(assetCapture);
                }

            }

            ProgressDialogUtils.closeProgressDialog();

            DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.AuditInfoUpdatedSuccessfully));

            if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                backgroundServiceFactory.initiateAssetCaptureService(inputAssetSerialNo.getText().toString());
            }

            /*if (NetworkUtils.getConnectivityStatusAsBoolean(getActivity()))
                displayAuditHistory();*/
            displayAuditHistory();


        }catch (Exception ex){
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;

        }
    }


    private void displayAuditHistory(){

        try
        {

            //new AuditHistoryAsyncTask().execute();

            loadAssetCapturesFromLocalDB();

        }catch (Exception ex){
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }
    }


    private void loadAssetCapturesFromLocalDB(){

        try
        {
            List<AssetCapture> assetCaptures=tableAssetCapture.getAllAssetCapturesByCustomerId(customer.getCustomerId());

            if (assetCaptures==null || assetCaptures.size() ==0 )
                return;

            List<CustomerAuditInfo> customerAuditInfos=new ArrayList<>();

            for (AssetCapture assetCapture:assetCaptures){

                CustomerAuditInfo customerAuditInfo=gson.fromJson(assetCapture.getJson(),CustomerAuditInfo.class);

                customerAuditInfos.add(customerAuditInfo);
            }

            buildAuditHistoryList(customerAuditInfos, tblAuditHistory);


        }catch (Exception ex){
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }


    }


    private void processAuditCaptureHistory(String responseJSON) {


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

                        List<CustomerAuditInfo> customerAuditInfos=new ArrayList<>();
                        customerAuditInfos.add(rootObject.getCustomerAuditInfo());
                        buildAuditHistoryList(customerAuditInfos, tblAuditHistory);

                    }

                }

            }

        } catch (Exception ex) {
            Logger.Log(AssetAuditFragment.class.getName(), ex);
        }

    }


    private void buildAuditHistoryList(List<CustomerAuditInfo> customerAuditInfos, TableLayout tableLayout) {

        try {


            if (customerAuditInfos != null && customerAuditInfos.size() > 0) {

                TableLayout.LayoutParams tableLayoutParams;
                TableRow.LayoutParams tableRowParams;

                tableLayoutParams = new TableLayout.LayoutParams();
                tableRowParams = new TableRow.LayoutParams();
                tableRowParams.setMargins(1, 1, 1, 1);

                tableLayout.removeAllViews();

                TableRow rowHeader = new TableRow(getActivity());
                rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

                rowHeader.addView(getTexView("Customer Code", AbstractApplication.get().getString(R.string.Customercode), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Serial No.",AbstractApplication.get().getString(R.string.serialno), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                //rowHeader.addView(getTexView("Complaint Type", "Complaint Type", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Remarks",AbstractApplication.get().getString(R.string.Remarks), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                //rowHeader.addView(getTexView("Status", "Status", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Audit Date",AbstractApplication.get().getString(R.string.AuditDate), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

                tableLayout.addView(rowHeader, tableLayoutParams);

                for (CustomerAuditInfo customerAuditInfo : customerAuditInfos) {

                    TableRow rowSchemeDetails = new TableRow(getActivity());
                    rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                    rowSchemeDetails.addView(getTexView("Customer Code", customerAuditInfo.getCustomerCode(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Serial No.", ((customerAuditInfo.getAssetCaptureHistories()!=null && customerAuditInfo.getAssetCaptureHistories().size()>0 ) ? customerAuditInfo.getAssetCaptureHistories().get(0).getqRCode() : "" )  , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    //rowSchemeDetails.addView(getTexView("Complaint Type", outletComplaint.getComplaintType(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Remarks", ((customerAuditInfo.getAssetCaptureHistories()!=null && customerAuditInfo.getAssetCaptureHistories().size()>0 ) ? customerAuditInfo.getAssetCaptureHistories().get(0).getRemarks() : "" ) , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    //rowSchemeDetails.addView(getTexView("Status", outletComplaint.getComplaintStatus(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Audit Date", ((customerAuditInfo.getAssetCaptureHistories()!=null && customerAuditInfo.getAssetCaptureHistories().size()>0 ) ? ( customerAuditInfo.getAssetCaptureHistories().get(0).getAuditInfo() != null ? customerAuditInfo.getAssetCaptureHistories().get(0).getAuditInfo().getCreatedDate() : " ") : "" )   , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                    tableLayout.addView(rowSchemeDetails, tableLayoutParams);
                }

            }


        } catch (Exception ex) {
            Logger.Log(AssetAuditFragment.class.getName(), ex);
            return;
        }
    }


    private TextView getTexView(String ID, String TextViewValue, float textSize, int mColor, boolean isBold, boolean IsBackGroundColor, int BackGroundColor) {


        try {
            TextView textView = new TextView(getActivity());

            textView.setText(TextViewValue);
            textView.setTextSize(textSize);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(mColor);
            textView.setGravity(Gravity.LEFT);
            if (isBold)
                textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
            if (IsBackGroundColor)
                textView.setBackgroundColor(BackGroundColor);
            return textView;

        } catch (Exception ex) {
            Logger.Log(AssetAuditFragment.class.getName(), ex);
            return null;
        }
    }


    public class AssetCaptureBroadcastReceiver extends BroadcastReceiver {

        public AssetCaptureBroadcastReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            displayAuditHistory();

        }

    }

    private class AuditHistoryAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            ProgressDialogUtils.showProgressDialog("Please Wait ... ");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                RootObject rootObject = new RootObject();

                List<AssetCaptureHistory> assetCaptureHistories = new ArrayList<>();

                AssetCaptureHistory assetCaptureHistory = new AssetCaptureHistory();
                assetCaptureHistory.setCustomerId(customer.getCustomerId());

                assetCaptureHistories.add(assetCaptureHistory);

                rootObject.setServiceCode(ServiceCode.GET_ASSET_CAPTURE_HISTORY);

                rootObject.setAssetCaptureHistories(assetCaptureHistories);

                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                return SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_ASSET_CAPTURE_HISTORY);

            } catch (Exception ex) {
                Logger.Log(AssetAuditFragment.class.getName(), ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            ProgressDialogUtils.closeProgressDialog();

            processAuditCaptureHistory(response);
        }
    }


    private void showAssetNotAvailableMessage(){
        DialogUtils.showAlertDialog(getActivity(),"Scanned QR code not mapped to this customer");
    }

    private boolean checkCustomerAsset(String QRCode){

        try
        {
            for (CustomerAsset customerAsset:customer.getCustomerAssets()){
                if (customerAsset.getQrCode().equalsIgnoreCase(QRCode))
                {
                    return true;
                }
            }

            return false;

        }catch (Exception ex){
            return false;
        }
    }
    //by Prasanna
    private void Lastauditdate() {

        if (customer == null || customer.getCustomerAssets() == null || customer.getCustomerAssets().size() == 0 )
            return;
        Lastauditdate.setText(resources.getString(R.string.LastAuditDetails));
        Lastauditdate.setEnabled(true);


        tableLayoutParams = new TableLayout.LayoutParams();
        tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);


        tbleLastAuditDate.removeAllViews();

        TableRow rowHeader = new TableRow(getActivity());
        rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

        rowHeader.addView(getTexView("QR Code", AbstractApplication.get().getString(R.string.QRCode), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
        rowHeader.addView(getTexView("Last Audit Date", AbstractApplication.get().getString(R.string.LastAuditDate), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

        tbleLastAuditDate.addView(rowHeader, tableLayoutParams);


        for (CustomerAsset customerAssets : customer.getCustomerAssets()) {

            //if(customerAssets.getLastAuditDate()!=null && customerAssets.getLastAuditDate()!="") {

                TableRow rowLastAuditDetails = new TableRow(getActivity());
                rowLastAuditDetails.setBackgroundColor(Color.parseColor("#000000"));

                rowLastAuditDetails.addView(getTexView("QR Code", customerAssets.getQrCode(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowLastAuditDetails.addView(getTexView("Last Audit Date", ( customerAssets.getLastAuditDate()==null || customerAssets.getLastAuditDate()=="" )?"":customerAssets.getLastAuditDate(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                tbleLastAuditDate.addView(rowLastAuditDetails, tableLayoutParams);
                //System.out.println("data"+customerAssets.getLastAuditDate());
            //}
        }
    }
//prasanna

}






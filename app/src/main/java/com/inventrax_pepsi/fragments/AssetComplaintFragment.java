package com.inventrax_pepsi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetComplaint;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetComplaint;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerAsset;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.OutletComplaint;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
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
 * Created by Naresh on 05-Apr-16.
 */
public class AssetComplaintFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private Gson gson;
    private Customer customer;
    private SFACommon sfaCommon;

    private EditText inputAssetSerialNo, inputRemarks;
    private ImageView btnScan;
    private Spinner spinnerComplaintType;
    private Button btnSubmit, btnCancel;
    private FloatingActionButton fabReturn;

    private DatabaseHelper databaseHelper;
    private TableAssetComplaint tableAssetComplaint;
    private TableJSONMessage tableJSONMessage;
    private BackgroundServiceFactory backgroundServiceFactory;

    private FrameLayout frmlComplaintHistoryList;
    private TableLayout tblComplaintHistory;

    private IntentFilter mIntentFilter;
    private AssetComplaintBroadcastReceiver assetComplaintBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_complaint_reg, container, false);

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

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }


    private void loadFormControls() {
        try {

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableAssetComplaint = databaseHelper.getTableAssetComplaint();
            tableJSONMessage = databaseHelper.getTableJSONMessage();

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());

            fabReturn = (FloatingActionButton) rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.assetcomplaint");

            assetComplaintBroadcastReceiver = new AssetComplaintBroadcastReceiver();


            inputAssetSerialNo = (EditText) rootView.findViewById(R.id.inputAssetSerialNo);
            inputRemarks = (EditText) rootView.findViewById(R.id.inputRemarks);
            spinnerComplaintType = (Spinner) rootView.findViewById(R.id.spinnerComplaintType);
            btnScan = (ImageView) rootView.findViewById(R.id.btnScan);
            btnScan.setOnClickListener(this);
            btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);
            btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(this);

            frmlComplaintHistoryList = (FrameLayout) rootView.findViewById(R.id.frmlComplaintHistoryList);
            tblComplaintHistory = (TableLayout) rootView.findViewById(R.id.tblComplaintHistory);

            tblComplaintHistory.removeAllViews();

            if (NetworkUtils.getConnectivityStatusAsBoolean(getActivity()))
                displayComplaintHistory();

        } catch (Exception ex) {
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(assetComplaintBroadcastReceiver, mIntentFilter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_asset_complaint));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_asset_complaint));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnScan: {

                QRScannerUtils.scanQRCode(this);

            }
            break;

            case R.id.btnCancel: {

                inputAssetSerialNo.setText("");
                inputRemarks.setText("");

            }
            break;

            case R.id.btnSubmit: {

                if (spinnerComplaintType.getSelectedItem().toString().equalsIgnoreCase("Select Complaint Type")) {
                    DialogUtils.showAlertDialog(getActivity(), "Please select complaint type");
                    return;
                }

                if (TextUtils.isEmpty(inputAssetSerialNo.getText().toString())) {
                    DialogUtils.showAlertDialog(getActivity(), "Please scan asset serial number");
                    return;
                }

                processComplaintRequest();

            }
            break;

            case R.id.fabReturn: {

                doReturn();

            }
            break;


        }


    }

    private void doReturn() {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("customerJSON", getArguments().getString("customerJSON"));
            OutletDashboardNewFragment outletDashboardNewFragment = new OutletDashboardNewFragment();
            outletDashboardNewFragment.setArguments(bundle);

            FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletDashboardNewFragment);

        } catch (Exception ex) {
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents()) && scanResult.getContents().trim().length() > 10) {
            DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Pleasescanvalidserialnumber));
            return;
        }


        if (customer != null && customer.getCustomerAssets() != null && customer.getCustomerAssets().size() > 0) {
            if (!checkCustomerAsset(scanResult.getContents().toString().trim())) {
                showAssetNotAvailableMessage();
                inputAssetSerialNo.setText("");
                return;
            }

        } else {
            showAssetNotAvailableMessage();
            inputAssetSerialNo.setText("");
            return;
        }


        inputAssetSerialNo.setText(scanResult.getContents().toString().trim()+"");
        inputAssetSerialNo.setEnabled(false);


    }


    private void processComplaintRequest() {

        try {


            if (customer != null && customer.getCustomerAssets() != null && customer.getCustomerAssets().size() > 0) {
                if (!checkCustomerAsset(inputAssetSerialNo.getText().toString().trim())) {
                    showAssetNotAvailableMessage();
                    inputAssetSerialNo.setText("");
                    return;
                }

            } else {
                showAssetNotAvailableMessage();
                inputAssetSerialNo.setText("");
                return;
            }


            ProgressDialogUtils.showProgressDialog(AbstractApplication.get().getString(R.string.Loadingmessage));

            OutletComplaint outletComplaint = new OutletComplaint();

            outletComplaint.setCustomerId(customer.getCustomerId());
            outletComplaint.setqRCode(inputAssetSerialNo.getText().toString().trim());
            outletComplaint.setDescription(inputRemarks.getText().toString());
            outletComplaint.setComplaintType(spinnerComplaintType.getSelectedItem().toString());

            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));

            outletComplaint.setAuditInfo(auditInfo);


            AssetComplaint assetComplaint = new AssetComplaint();

            assetComplaint.setCustomerId(outletComplaint.getCustomerId());
            assetComplaint.setComplaintJSON(gson.toJson(outletComplaint));
            assetComplaint.setComplaintStatus(1);


            long auto_inc_id = tableAssetComplaint.createAssetComplaint(assetComplaint);

            if (auto_inc_id != 0) {

                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(assetComplaint.getComplaintJSON());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId((int) auto_inc_id);
                jsonMessage.setNotificationTypeId(5); // For Asset Complaint Notification Type Id is 5

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    assetComplaint.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    assetComplaint.setAutoIncId((int) auto_inc_id);

                    tableAssetComplaint.updateAssetComplaint(assetComplaint);
                }

            }

            ProgressDialogUtils.closeProgressDialog();

            DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.ComplaintInitiatedSuccessfully));

            if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                backgroundServiceFactory.initiateAssetComplaintService((int) auto_inc_id);
            }

            if (NetworkUtils.getConnectivityStatusAsBoolean(getActivity()))
                displayComplaintHistory();


        } catch (Exception ex) {

            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;

        }

    }

    private void displayComplaintHistory() {

        try {

            new ComplaintHistoryAsyncTask().execute();

        } catch (Exception ex) {
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return;
        }
    }

    private void processComplaintHistory(String responseJSON) {


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

                        buildComplaintHistoryList(rootObject.getOutletComplaints(), tblComplaintHistory);

                    }

                }

            }

        } catch (Exception ex) {
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
        }

    }

    private void buildComplaintHistoryList(List<OutletComplaint> outletComplaintList, TableLayout tableLayout) {

        try {


            if (outletComplaintList != null && outletComplaintList.size() > 0) {

                TableLayout.LayoutParams tableLayoutParams;
                TableRow.LayoutParams tableRowParams;

                tableLayoutParams = new TableLayout.LayoutParams();
                tableRowParams = new TableRow.LayoutParams();
                tableRowParams.setMargins(1, 1, 1, 1);

                tableLayout.removeAllViews();

                TableRow rowHeader = new TableRow(getActivity());
                rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

                rowHeader.addView(getTexView(AbstractApplication.get().getString(R.string.serialno),AbstractApplication.get().getString(R.string.serialno), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Complaint Type", AbstractApplication.get().getString(R.string.ComplaintType), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Remarks", AbstractApplication.get().getString(R.string.Remarks), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Status", AbstractApplication.get().getString(R.string.status), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Complain Date", AbstractApplication.get().getString(R.string.ComplainDate), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

                tableLayout.addView(rowHeader, tableLayoutParams);

                for (OutletComplaint outletComplaint : outletComplaintList) {

                    TableRow rowSchemeDetails = new TableRow(getActivity());
                    rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                    rowSchemeDetails.addView(getTexView("Serial No.", outletComplaint.getqRCode(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Complaint Type", outletComplaint.getComplaintType(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Remarks", outletComplaint.getDescription(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Status", outletComplaint.getComplaintStatus(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Complain Date", (outletComplaint.getAuditInfo() != null ? outletComplaint.getAuditInfo().getCreatedDate() : " "), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                    tableLayout.addView(rowSchemeDetails, tableLayoutParams);
                }

            }


        } catch (Exception ex) {
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
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
            Logger.Log(AssetComplaintFragment.class.getName(), ex);
            return null;
        }
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(assetComplaintBroadcastReceiver);
        super.onPause();

    }

    private void showAssetNotAvailableMessage() {
        DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.ScannedQRcodenotmappedtothiscustomer));
    }

    private boolean checkCustomerAsset(String QRCode) {

        try {
            for (CustomerAsset customerAsset : customer.getCustomerAssets()) {
                if (customerAsset.getQrCode().equalsIgnoreCase(QRCode)) {
                    return true;
                }
            }

            return false;

        } catch (Exception ex) {
            return false;
        }
    }

    private class ComplaintHistoryAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            ProgressDialogUtils.showProgressDialog("Please Wait ... ");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                RootObject rootObject = new RootObject();

                List<OutletComplaint> outletComplaintList = new ArrayList<>();

                OutletComplaint outletComplaint = new OutletComplaint();
                outletComplaint.setCustomerId(customer.getCustomerId());
                outletComplaint.setCustomerCode(customer.getCustomerCode());

                outletComplaintList.add(outletComplaint);

                rootObject.setServiceCode(ServiceCode.GET_COMPLAINT_HISTORY);
                rootObject.setOutletComplaints(outletComplaintList);

                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                return SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_GET_COMPLAINT_HISTORY);

            } catch (Exception ex) {
                Logger.Log(AssetComplaintFragment.class.getName(), ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            ProgressDialogUtils.closeProgressDialog();

            processComplaintHistory(response);
        }
    }

    public class AssetComplaintBroadcastReceiver extends BroadcastReceiver {

        public AssetComplaintBroadcastReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            displayComplaintHistory();

        }

    }


}

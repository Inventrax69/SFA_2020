package com.inventrax_pepsi.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.AssetPulloutReason;
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableAssetPullout;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.AssetPullout;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerAsset;
import com.inventrax_pepsi.sfa.pojos.CustomerAssetPullout;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentGUI;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.QRScannerUtils;

import java.util.List;

/**
 * Created by android on 5/6/2016.
 */
public class PulloutFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private Button btnSubmit,btnCancel;
    private EditText inputReasonForPullout,inputAssetSerialNo;
    private TextInputLayout input_layout_pullout_reason;
    private RadioGroup rbgScrap,rbgMachineStatus;
    private RadioButton rbScrapYes,rbScrapNo,rbWorking,rbNotWorking;
    private Spinner spinnerPulloutReasons;
    private TableLayout tblAssetPullout;
    private TextView txtPulloutDetails;
    private ImageView btnScan;
    private SFACommon sfaCommon;
    private Gson gson;
    private Customer customer;
    private FloatingActionButton fabReturn;
    private DatabaseHelper databaseHelper;
    private TableAssetPullout tableAssetPullout;
    private TableJSONMessage tableJSONMessage;
    private BackgroundServiceFactory backgroundServiceFactory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pullout_form,container,false);

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_pullout_form));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_pullout_form));

    }

    private void loadFormControls(){

        try
        {
            btnSubmit = FragmentGUI.getButton(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);
            btnCancel = FragmentGUI.getButton(R.id.btnCancel);
            btnCancel.setOnClickListener(this);

            inputReasonForPullout=FragmentGUI.getEditText(R.id.inputReasonForPullout);
            inputAssetSerialNo=FragmentGUI.getEditText(R.id.inputAssetSerialNo);

            input_layout_pullout_reason=FragmentGUI.getTextInputLayout(R.id.input_layout_pullout_reason);

            rbgScrap = FragmentGUI.getRadioGroup(R.id.rbgScrap);

            rbgMachineStatus=FragmentGUI.getRadioGroup(R.id.rbgMachineStatus);

            rbScrapYes = FragmentGUI.getRadioButton(R.id.rbScrapYes);
            rbScrapNo = FragmentGUI.getRadioButton(R.id.rbScrapNo);
            rbWorking = FragmentGUI.getRadioButton(R.id.rbWorking);
            rbNotWorking = FragmentGUI.getRadioButton(R.id.rbNotWorking);

            spinnerPulloutReasons = FragmentGUI.getSpinner(R.id.spinnerPulloutReasons);

            tblAssetPullout = FragmentGUI.getTableLayout(R.id.tblAssetPullout);

            txtPulloutDetails = FragmentGUI.getTextView(R.id.txtPulloutDetails);

            btnScan = FragmentGUI.getImageView(R.id.btnScan);
            btnScan.setOnClickListener(this);

            fabReturn = (FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            gson = new GsonBuilder().create();

            this.customer = (Customer) gson.fromJson(getArguments().getString("customerJSON"), Customer.class);

            databaseHelper = DatabaseHelper.getInstance();
            tableAssetPullout=databaseHelper.getTableAssetPullout();
            tableJSONMessage = databaseHelper.getTableJSONMessage();

            backgroundServiceFactory=BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());

            displayPulloutDetails();


        }catch (Exception ex){
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }

    }

    private void displayPulloutDetails() {

        try
        {
           List<AssetPullout> assetPullouts=tableAssetPullout.getAllAssetPulloutsByCustomerId(customer.getCustomerId());

            if (assetPullouts == null || assetPullouts.size() ==0)
                return;

            txtPulloutDetails.setVisibility(TextView.VISIBLE);

            TableLayout.LayoutParams tableLayoutParams;
            TableRow.LayoutParams tableRowParams;

            tableLayoutParams = new TableLayout.LayoutParams();
            tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(1, 1, 1, 1);

            tblAssetPullout.removeAllViews();

            TableRow rowHeader = new TableRow(getActivity());
            rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

            rowHeader.addView(getTexView("Serial No.", "Serial No.", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Machine Status", "Machine Status", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Reason", "Reason", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Is Scrapped", "Is Scrapped", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Remarks", "Remarks", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

            tblAssetPullout.addView(rowHeader, tableLayoutParams);

            for (AssetPullout assetPullout:assetPullouts){

                CustomerAssetPullout customerAssetPullout = gson.fromJson(assetPullout.getJson(),CustomerAssetPullout.class);

                if (customerAssetPullout==null)
                    return;


                TableRow rowSchemeDetails = new TableRow(getActivity());
                rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                rowSchemeDetails.addView(getTexView("Serial No.", customerAssetPullout.getQRCode(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowSchemeDetails.addView(getTexView("Machine Status", customerAssetPullout.getIsWorking()==1?"Working":"Not Working", 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowSchemeDetails.addView(getTexView("Reason", customerAssetPullout.getAssetPulloutReason(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowSchemeDetails.addView(getTexView("Is Scrapped", ( customerAssetPullout.getIsScrap()==1?"Yes":"No" ) , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                rowSchemeDetails.addView(getTexView("Remarks", customerAssetPullout.getRemarks() , 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                tblAssetPullout.addView(rowSchemeDetails, tableLayoutParams);

            }

        }catch (Exception ex){
            Logger.Log(PulloutFragment.class.getName(),ex);
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
            Logger.Log(AssetComplaintFragment.class.getName(),ex);
            return null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents())) {

                if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents()) && scanResult.getContents().trim().length() > 10) {
                    DialogUtils.showAlertDialog(getActivity(), "Please scan valid serial number");
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
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }

    }

    private void showAssetNotAvailableMessage(){
        DialogUtils.showAlertDialog(getActivity(),"Scanned QR code not mapped to this customer");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnScan:{

                QRScannerUtils.scanQRCode(this);

            }break;

            case R.id.btnSubmit:{



                validateForm();

            }break;

            case R.id.btnCancel:{

                clearFormDetails();

            }break;

            case R.id.fabReturn: {

                doReturn();

            }
            break;
        }

    }

    private void clearFormDetails(){

        try
        {
            inputReasonForPullout.setText("");
            inputAssetSerialNo.setText("");

        }catch (Exception ex){
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }

    }

    private void submitForm(){

        try
        {

            if (spinnerPulloutReasons.getSelectedItem().toString().trim().equalsIgnoreCase("Select Reason")) {
                DialogUtils.showAlertDialog(getActivity(),"Please select pullout reason");
                return;
            }

            ProgressDialogUtils.showProgressDialog("Please Wait ...");

            CustomerAssetPullout customerAssetPullout=new CustomerAssetPullout();

            customerAssetPullout.setQRCode(inputAssetSerialNo.getText().toString().trim());
            customerAssetPullout.setAssetPulloutReasonId(AssetPulloutReason.getAssetPulloutReasons().get(spinnerPulloutReasons.getSelectedItem().toString()));
            customerAssetPullout.setRemarks(inputReasonForPullout.getText().toString());

            customerAssetPullout.setIsScrap( ((RadioButton) rootView.findViewById(rbgScrap.getCheckedRadioButtonId())).getText().toString().trim().toLowerCase().equals("yes")?1:0);
            customerAssetPullout.setIsWorking(((RadioButton) rootView.findViewById(rbgMachineStatus.getCheckedRadioButtonId())).getText().toString().trim().toLowerCase().equals("working")?1:0);

            customerAssetPullout.setCustomerId(customer.getCustomerId());
            customerAssetPullout.setPulledOutOn(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));
            customerAssetPullout.setAssetPulloutReason(spinnerPulloutReasons.getSelectedItem().toString());

            AuditInfo auditInfo=new AuditInfo();
            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSSSSS_DATE_FORMAT));
            customerAssetPullout.setAuditInfo(auditInfo);

            AssetPullout assetPullout=new AssetPullout();

            assetPullout.setQRCode(customerAssetPullout.getQRCode());
            assetPullout.setCustomerId(customer.getCustomerId());
            assetPullout.setJson(gson.toJson(customerAssetPullout));


            long auto_inc_id = tableAssetPullout.createAssetPullout(assetPullout);

            if ( auto_inc_id > 0 ){

                JSONMessage jsonMessage=new JSONMessage();

                jsonMessage.setSyncStatus(0);
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setJsonMessage(assetPullout.getJson());
                // Notification Type Id 8 for pullout
                jsonMessage.setNotificationTypeId(JsonMessageNotificationType.AssetPullout.getNotificationType());
                jsonMessage.setNotificationId((int)auto_inc_id);

                long json_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_auto_inc_id>0){

                    assetPullout.setAutoIncId((int)auto_inc_id);
                    assetPullout.setJsonMessageAutoIncId((int)json_auto_inc_id);

                    if (tableAssetPullout.updateAssetPullout(assetPullout)<0)
                    {
                        ProgressDialogUtils.closeProgressDialog();
                        DialogUtils.showAlertDialog(getActivity(),"Error while updating pullout details");
                        return;
                    }

                    displayPulloutDetails();

                    DialogUtils.showAlertDialog(getActivity(),"Pullout details updated successfully");

                    if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                    {
                        backgroundServiceFactory.initiateAssetPulloutService();
                    }

                }

            }else {
                ProgressDialogUtils.closeProgressDialog();
                DialogUtils.showAlertDialog(getActivity(),"Error while updating pullout details");
                return;
            }

            ProgressDialogUtils.closeProgressDialog();

        }catch (Exception ex){
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }

    }

    private void validateForm(){

        try
        {
            if (TextUtils.isEmpty(inputAssetSerialNo.getText().toString().trim()))
            {
                DialogUtils.showAlertDialog(getActivity(),"Please scan asset serial number");
                return;
            }


            if (customer!=null && customer.getCustomerAssets()!=null && customer.getCustomerAssets().size()>0 )
            {
                if (!checkCustomerAsset(inputAssetSerialNo.getText().toString().trim()))
                {
                    showAssetNotAvailableMessage();
                    return;
                }

            }else {
                showAssetNotAvailableMessage();
                return;
            }

            submitForm();

        }catch (Exception ex){
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
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




}

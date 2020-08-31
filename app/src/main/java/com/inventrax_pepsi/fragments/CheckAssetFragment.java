package com.inventrax_pepsi.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.inventrax_pepsi.sfa.pojos.CustomizedAssetInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.User;
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
 * Created by android on 4/26/2016.
 */
public class CheckAssetFragment  extends Fragment implements View.OnClickListener {

    private View rootView;

    private EditText inputAssetSerialNo;
    private ImageView btnScan;
    private Button btnSubmit;
    private Gson gson;
    private LinearLayout layoutAssetDetails;
    private SFACommon sfaCommon;

    private TextView txtOutletCode,txtOutletName,txtRouteCode,txtRouteName,txtTaggedBy,txtTaggedDate,txtAssetType,txtAssetVolume,txtPhoneNo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_check_asset,container,false);

        sfaCommon=SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }


    private void loadFormControls(){

        try
        {
            gson=new GsonBuilder().create();

            inputAssetSerialNo=(EditText)rootView.findViewById(R.id.inputAssetSerialNo);

            btnScan=(ImageView)rootView.findViewById(R.id.btnScan);
            btnScan.setOnClickListener(this);
            btnSubmit=(Button)rootView.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(this);

            layoutAssetDetails=(LinearLayout)rootView.findViewById(R.id.layoutAssetDetails);

            txtOutletCode=(TextView)rootView.findViewById(R.id.txtOutletCode);
            txtOutletName=(TextView)rootView.findViewById(R.id.txtOutletName);
            txtRouteCode=(TextView)rootView.findViewById(R.id.txtRouteCode);
            txtRouteName=(TextView)rootView.findViewById(R.id.txtRouteName);
            txtTaggedBy=(TextView)rootView.findViewById(R.id.txtTaggedBy);
            txtTaggedDate=(TextView)rootView.findViewById(R.id.txtTaggedDate);
            txtAssetVolume=(TextView)rootView.findViewById(R.id.txtAssetVolume);
            txtAssetType=(TextView)rootView.findViewById(R.id.txtAssetType);
            txtPhoneNo=(TextView)rootView.findViewById(R.id.txtPhoneNo);


            layoutAssetDetails.setVisibility(LinearLayout.GONE);


            if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
            {
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.internetenablemessage));

                FragmentUtils.replaceFragment(getActivity(),R.id.container_body,new DashboardFragment());

                return;
            }


        }catch (Exception ex){
            DialogUtils.showAlertDialog(getActivity(),"Error while initializing");
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnScan:{

                QRScannerUtils.scanQRCode(this);

            }break;

            case R.id.btnSubmit:{

                if (TextUtils.isEmpty(inputAssetSerialNo.getText().toString().trim())){
                    DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Pleasescanvalidserialnumber));
                    return;
                }

                if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.internetenablemessage));
                    return;
                }

                layoutAssetDetails.setVisibility(LinearLayout.GONE);

                processRequest();

            }break;


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents())) {

                clearAssetDetails();

                if (scanResult != null && !TextUtils.isEmpty(scanResult.getContents()) && scanResult.getContents().trim().length() > 10) {
                    DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Pleasescanvalidserialnumber));
                    return;
                }

                if (!TextUtils.isEmpty(scanResult.getContents())) {
                    inputAssetSerialNo.setText(scanResult.getContents().toString().trim());
                    inputAssetSerialNo.setEnabled(false);
                } else {
                    inputAssetSerialNo.setText("");
                }

            }
        }catch (Exception ex){
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;
        }

    }


    private void processRequest(){
        try
        {

            RootObject rootObject = new RootObject();

            rootObject.setServiceCode(ServiceCode.ASSET_AVAIL_CHECK);
            rootObject.setLoginInfo(AppController.getLoginInfo());

            ArrayList<User> users=new ArrayList<>();
            users.add(AppController.getUser());

            rootObject.setUsers(users);

            /*AssetAvailInfo assetAvailInfo=new AssetAvailInfo();
            assetAvailInfo.setqRCode(inputAssetSerialNo.getText().toString());
            List<AssetAvailInfo> assetAvailInfos=new ArrayList<>();
            assetAvailInfos.add(assetAvailInfo);
            rootObject.setAssetAvailInfos(assetAvailInfos);*/

            rootObject.setCustomizedAssetInfos(new ArrayList<CustomizedAssetInfo>());

            CustomizedAssetInfo customizedAssetInfo=new CustomizedAssetInfo();
            customizedAssetInfo.setQRCode(inputAssetSerialNo.getText().toString().trim());
            rootObject.getCustomizedAssetInfos().add(customizedAssetInfo);

            new CheckAssetAsyncTask().execute(rootObject);

        }catch (Exception ex){
            DialogUtils.showAlertDialog(getActivity(),"Error while checking");
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;
        }
    }


    private class CheckAssetAsyncTask extends AsyncTask<RootObject,Void,String> {

        @Override
        protected void onPreExecute() {
            ProgressDialogUtils.showProgressDialog("Please Wait ...");
        }

        @Override
        protected String doInBackground(RootObject... params) {

            try
            {

                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(params[0]));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);


                return  SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.METHOD_CHECK_ASSET);

            }catch (Exception ex){
                Logger.Log(CheckAssetFragment.class.getName(),ex);
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {

            ProgressDialogUtils.closeProgressDialog();

            processJSONResponse(s);

        }
    }


    private void processJSONResponse(String jsonResponse){

        try
        {

            if (!TextUtils.isEmpty(jsonResponse)) {

                JSONObject jsonObject = new JSONObject(jsonResponse);

                JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                ExecutionResponse executionResponse = null;

                if (rootObject != null)
                    executionResponse = rootObject.getExecutionResponse();

                if (executionResponse != null) {

                    if (executionResponse.getSuccess() == 1) {

                        clearAssetDetails();

                        if (rootObject.getCustomizedAssetInfos() !=null && rootObject.getCustomizedAssetInfos().size()>0)

                            displayAssetDetails(rootObject.getCustomizedAssetInfos().get(0));

                        else {
                            clearAssetDetails();
                            DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.AssetNotTagged));
                            return;
                        }

                    }else {
                        clearAssetDetails();
                        DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.AssetNotTagged));
                        return;
                    }
                }else {

                    DialogUtils.showAlertDialog(getActivity(),"Error while processing");
                    return;

                }

            }else {

                DialogUtils.showAlertDialog(getActivity(),"Error while processing");
                return;

            }


        }catch (Exception ex){
            DialogUtils.showAlertDialog(getActivity(),"Invalid Response");
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;
        }

    }


    private void displayAssetDetails(CustomizedAssetInfo assetAvailInfo){

        try
        {
            layoutAssetDetails.setVisibility(LinearLayout.VISIBLE);

            txtOutletCode.setText( "" + assetAvailInfo.getOutletCode() );
            txtOutletName.setText(assetAvailInfo.getOutletName());
            txtRouteCode.setText(assetAvailInfo.getRouteCode());
            txtRouteName.setText(assetAvailInfo.getRouteName());
            txtTaggedBy.setText(assetAvailInfo.getInstalledUsr());
            txtTaggedDate.setText(assetAvailInfo.getInstalledOn());
            txtAssetType.setText(assetAvailInfo.getAssetType());
            txtAssetVolume.setText(assetAvailInfo.getAssetVolume());
            txtPhoneNo.setText(assetAvailInfo.getMobileNo1());

        }catch (Exception ex){
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;

        }

    }


    private void clearAssetDetails(){
        try
        {
            layoutAssetDetails.setVisibility(LinearLayout.GONE);

            txtOutletCode.setText("");
            txtOutletName.setText("");
            txtRouteCode.setText("");
            txtRouteName.setText("");
            txtTaggedBy.setText("");
            txtTaggedDate.setText("");
            txtAssetType.setText("");
            txtAssetVolume.setText("");
            txtPhoneNo.setText("");


        }catch (Exception ex){
            Logger.Log(CheckAssetFragment.class.getName(),ex);
            return;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        sfaCommon.displayDate(getActivity());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_check_asset));

    }
}

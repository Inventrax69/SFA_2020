package com.inventrax_pepsi.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerAsset;
import com.inventrax_pepsi.util.FragmentUtils;

/**
 * Created by android on 5/19/2016.
 */
public class AssetInfoFragment extends Fragment {

    private View rootView;
    private TableLayout tblAssetDetails;
    private Customer customer;
    private SFACommon sfaCommon;
    private Gson gson;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_asset_info,container,false);

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

        sfaCommon=SFACommon.getInstance();

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try
        {
            gson =new  GsonBuilder().create();

            this.customer=(Customer)gson.fromJson(getArguments().getString("customerJSON"),Customer.class);

            tblAssetDetails=(TableLayout)rootView.findViewById(R.id.tblAssetDetails);

            buildAssetInfoList();

        }catch (Exception ex){
            Logger.Log(AssetInfoFragment.class.getName(),ex);
            return;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_asset_info));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_asset_info));

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


    private void buildAssetInfoList() {

        try {


            if (customer == null || customer.getCustomerAssets() == null || customer.getCustomerAssets().size() == 0 )
                return;

                TableLayout.LayoutParams tableLayoutParams;
                TableRow.LayoutParams tableRowParams;

                tableLayoutParams = new TableLayout.LayoutParams();
                tableRowParams = new TableRow.LayoutParams();
                tableRowParams.setMargins(1, 1, 1, 1);

                tblAssetDetails.removeAllViews();

                TableRow rowHeader = new TableRow(getActivity());
                rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

                rowHeader.addView(getTexView("QR Code", AbstractApplication.get().getString(R.string.QRCode), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Type", AbstractApplication.get().getString(R.string.AssetType), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Model",AbstractApplication.get().getString(R.string.Model), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Make", AbstractApplication.get().getString(R.string.Make), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Volume", AbstractApplication.get().getString(R.string.Volume), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Pepsi Serial No.",AbstractApplication.get().getString(R.string.pepsiserialno) , 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("OEM No.", AbstractApplication.get().getString(R.string.OEMNo), 18, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

                tblAssetDetails.addView(rowHeader, tableLayoutParams);

                for (CustomerAsset customerAsset : customer.getCustomerAssets()) {

                    TableRow rowSchemeDetails = new TableRow(getActivity());
                    rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                    rowSchemeDetails.addView(getTexView("QR Code", customerAsset.getQrCode(), 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Type",  customerAsset.getAssetType() , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Model", customerAsset.getModel() , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Make",  customerAsset.getMake() , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Volume", customerAsset.getVolume() , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Pepsi Serial No.",  customerAsset.getSerialNo()  , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("OEM No.", customerAsset.getoEMNo() , 18, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                    tblAssetDetails.addView(rowSchemeDetails, tableLayoutParams);
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


}

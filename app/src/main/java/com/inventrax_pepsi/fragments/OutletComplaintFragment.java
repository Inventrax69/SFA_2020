package com.inventrax_pepsi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.model.TechnicianOutletList;
import com.inventrax_pepsi.util.DialogUtils;

/**
 * Created by android on 3/4/2016.
 */
public class OutletComplaintFragment extends Fragment {

    View rootView;
    Activity activity;
    SFACommon sfaCommon;
    Gson gson;

    TextInputLayout input_layout_qr_code;
    EditText inputQRCode,inputPepsiSerialNumber,etPartsAdded;
    Spinner spinnerComplaintType,spinnerCoolerVolume;
    Button btnSubmit,btnClear,btnCancel;

    MaterialDialog materialAssetDialog;

    public OutletComplaintFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_complaint_new, container, false);
        setHasOptionsMenu(true);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });


        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {
            //Retrieve the value
            String json = getArguments().getString("json");

            TechnicianOutletList technicianOutletList=new Gson().fromJson(json,TechnicianOutletList.class);

           TextView txtOutletName = (TextView) rootView.findViewById(R.id.txtOutletName);
            TextView txtOutletCode = (TextView) rootView.findViewById(R.id.txtOutletCode);
            TextView txtCustomerName = (TextView) rootView.findViewById(R.id.txtCustomerName);
            TextView txtAssetsCount = (TextView) rootView.findViewById(R.id.txtAssetsCount);

            Button btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
            Button btnClear = (Button) rootView.findViewById(R.id.btnClear);
            Button btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadComplaintDetailsDialog();
                }
            });

            txtOutletName.setText(technicianOutletList.getOutletName());
            txtOutletCode.setText(technicianOutletList.getCustomerCode());
            txtCustomerName.setText(technicianOutletList.getCustomerName());
            txtAssetsCount.setText(String.valueOf(technicianOutletList.getAssetCount()));

            Spinner spinnerjoborder = (Spinner) rootView.findViewById(R.id.spinnerjoborder);


            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,technicianOutletList.getJobOrders());
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinnerjoborder.setAdapter(aa);

        } catch (Exception ex) {

            Logger.Log(OutletComplaintFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing outlet list");
            return;

        }
}

    private void loadComplaintDetailsDialog() {

        try {
            MaterialDialog.Builder builderAssetDialog = new MaterialDialog.Builder(getActivity())
                    .title("Asset Details")
                    .customView(R.layout.dialog_technician_complaint, true)
                    .cancelable(false);

            materialAssetDialog = builderAssetDialog.build();

            input_layout_qr_code = (TextInputLayout) materialAssetDialog.findViewById(R.id.input_layout_qr_code);
            inputQRCode = (EditText) materialAssetDialog.findViewById(R.id.inputQRCode);
            inputPepsiSerialNumber = (EditText) materialAssetDialog.findViewById(R.id.inputPepsiSerialNumber);
            etPartsAdded = (EditText) materialAssetDialog.findViewById(R.id.etPartsAdded);

            spinnerComplaintType = (Spinner) materialAssetDialog.findViewById(R.id.spinnerComplaintType);
            spinnerCoolerVolume = (Spinner) materialAssetDialog.findViewById(R.id.spinnerCoolerVolume);

            btnSubmit = (Button) materialAssetDialog.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialAssetDialog.dismiss();
                }
            });

            btnClear = (Button) materialAssetDialog.findViewById(R.id.btnClear);
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialAssetDialog.dismiss();
                }
            });

            btnCancel = (Button) materialAssetDialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialAssetDialog.dismiss();
                }
            });


            materialAssetDialog.show();

        } catch (Exception ex) {
            Logger.Log(OutletComplaintFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {

        }

        super.onCreateOptionsMenu(menu, inflater);


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Job order list");

    }

}
package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by Naresh on 05-Apr-16.
 */
public class PlanogramFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private View rootView;
    private SFACommon sfaCommon;
    private Button btnViewPlan;
    private ImageView imgPlanogram;
    private Spinner spinnerAssetMake,spinnerAssetCapacity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_planogram, container, false);

        try {

            sfaCommon = SFACommon.getInstance();

            btnViewPlan = (Button) rootView.findViewById(R.id.btnViewPlan);
            btnViewPlan.setOnClickListener(this);

            imgPlanogram = (ImageView) rootView.findViewById(R.id.imgPlanogram);

            spinnerAssetMake = (Spinner) rootView.findViewById(R.id.spinnerAssetMake);
            spinnerAssetCapacity = (Spinner) rootView.findViewById(R.id.spinnerAssetCapacity);
            spinnerAssetCapacity.setOnItemSelectedListener(this);

        }catch (Exception ex){
            Logger.Log(PlanogramFragment.class.getName(),ex);
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_planogram));
        sfaCommon.displayDate(getActivity());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnViewPlan:{

                try {

                    if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                        DialogUtils.showAlertDialog(getActivity(),"Please enable internet");

                    Picasso.with(getContext())
                            .load(ServiceURLConstants.VISI_PLANOGRAM_IMAGE_URL + spinnerAssetMake.getSelectedItem().toString() + "_" + spinnerAssetCapacity.getSelectedItem().toString() + ".jpg")
                            .placeholder(R.drawable.pepsi_logo)
                            .into(imgPlanogram);


                }catch (Exception ex){
                    Logger.Log(PlanogramFragment.class.getName(),ex);
                    return;
                }

            }break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {

            if (!NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                DialogUtils.showAlertDialog(getActivity(),"Please enable internet");

            if (!spinnerAssetCapacity.getSelectedItem().toString().equalsIgnoreCase("Select Volume")) {

                Picasso.with(getContext())
                        .load(ServiceURLConstants.VISI_PLANOGRAM_IMAGE_URL + spinnerAssetCapacity.getSelectedItem().toString() + ".png")
                        .placeholder(R.drawable.pepsi_logo)
                        .into(imgPlanogram);

            } else {

                imgPlanogram.setImageResource(R.drawable.pepsi_logo);

            }
        }catch (Exception ex){
            Logger.Log(PlanogramFragment.class.getName(),ex);
            return;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

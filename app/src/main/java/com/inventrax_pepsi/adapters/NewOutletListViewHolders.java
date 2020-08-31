package com.inventrax_pepsi.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.fragments.OutletRegistrationFragment;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.util.FragmentUtils;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: New Outlet List View Holders
 */


public class NewOutletListViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView  txtOutletCode,txtOutletName,txtOwnerName,txtChannelCode,txtLandmark,txtPhoneNo;
    public Customer customer;
    private Gson gson;
    private FragmentActivity fragmentActivity;



    public NewOutletListViewHolders(View itemView,FragmentActivity fragmentActivity) {

        super(itemView);

        this.fragmentActivity=fragmentActivity;

        if (gson == null){

            gson = new  GsonBuilder().create();
        }

        txtOutletCode = (TextView) itemView.findViewById(R.id.txtOutletCode);
        txtOutletName = (TextView) itemView.findViewById(R.id.txtOutletName);
        txtOwnerName = (TextView) itemView.findViewById(R.id.txtOwnerName);
        txtChannelCode = (TextView) itemView.findViewById(R.id.txtChannelCode);
        txtLandmark = (TextView) itemView.findViewById(R.id.txtLandmark);
        txtPhoneNo = (TextView) itemView.findViewById(R.id.txtPhoneNo);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();

        OutletRegistrationFragment outletRegistrationFragment= new OutletRegistrationFragment();
        bundle.putString("customerJSON", gson.toJson(customer));
        outletRegistrationFragment.setArguments(bundle);

        FragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, outletRegistrationFragment);

    }
}

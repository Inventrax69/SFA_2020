package com.inventrax_pepsi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.interfaces.OutletDashboardView;
import com.inventrax_pepsi.model.OutletMenuItem;

/**
 * Author   : Naresh P.
 * Date		: 12/03/2016 11:03 AM
 * Purpose	: Outlet Menu View Holders
 */


public class OutletMenuViewHoldersNew extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuText;
    public ImageView imgMenuIcon;
    public OutletMenuItem outletMenuItem;

    private OutletDashboardView outletDashboardView;

    public OutletMenuViewHoldersNew(View itemView, OutletDashboardView outletDashboardView) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.outletDashboardView = outletDashboardView;
        txtMenuText = (TextView) itemView.findViewById(R.id.txtMenuText);
        imgMenuIcon = (ImageView) itemView.findViewById(R.id.imgMenuIcon);
    }

    @Override
    public void onClick(View v) {
        outletDashboardView.onMenuItemSelected(outletMenuItem);
    }
}

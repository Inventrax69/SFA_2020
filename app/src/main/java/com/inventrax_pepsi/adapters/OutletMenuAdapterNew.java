package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.interfaces.OutletDashboardView;
import com.inventrax_pepsi.model.OutletMenuItem;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 12/03/2016 11:03 AM
 * Purpose	: Outlet Menu Adapter
 */


public class OutletMenuAdapterNew extends RecyclerView.Adapter<OutletMenuViewHoldersNew> {

    private List<OutletMenuItem> outletMenuItemList;
    private Context context;
    private OutletDashboardView outletDashboardView;

    public OutletMenuAdapterNew(Context context, List<OutletMenuItem> outletMenuItemList, OutletDashboardView outletDashboardView) {
        this.outletMenuItemList = outletMenuItemList;
        this.context = context;
        this.outletDashboardView = outletDashboardView;
    }

    @Override
    public OutletMenuViewHoldersNew onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_menu_row_new, parent, false);
        OutletMenuViewHoldersNew outletMenuViewHolders = new OutletMenuViewHoldersNew(layoutView, outletDashboardView);

        return outletMenuViewHolders;

    }

    @Override
    public void onBindViewHolder(OutletMenuViewHoldersNew holder, int position) {

        try {

            OutletMenuItem outletMenuItem = outletMenuItemList.get(position);
            holder.imgMenuIcon.setImageResource(outletMenuItem.getMenuItemIcon());
            holder.txtMenuText.setText(outletMenuItem.getMenuText());
            holder.outletMenuItem = outletMenuItem;
        } catch (Exception ex) {
            Logger.Log(OutletMenuAdapterNew.class.getName(), ex);
        }

    }

    @Override
    public int getItemCount() {
        return this.outletMenuItemList.size();
    }
}
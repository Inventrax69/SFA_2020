package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.sfa.sku.ItemUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Naresh on 13-Mar-16.
 */

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: Vehicle Stock List Adapter
 */


public class VehicleStockListAdapter extends RecyclerView.Adapter<VehicleStockViewHolders> {

    private List<VehicleStock> vehicleStockList;
    private Context context;
    private Resources resources;

    public VehicleStockListAdapter(Context context, List<VehicleStock> vehicleStockList) {
        setVehicleStockListAdapter(context, vehicleStockList);
    }

    public void setVehicleStockListAdapter(Context context, List<VehicleStock> vehicleStockList) {
        this.vehicleStockList = vehicleStockList;
        this.context = context;
        resources = context.getResources();
    }

    @Override
    public VehicleStockViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_stock_row, parent, false);

        VehicleStockViewHolders orderSummaryViewHolders = new VehicleStockViewHolders(layoutView);

        return orderSummaryViewHolders;
    }

    @Override
    public void onBindViewHolder(VehicleStockViewHolders holder, int position) {

        try {

            VehicleStock vehicleStock = vehicleStockList.get(position);

           /* final int resourceId = resources.getIdentifier("p" + vehicleStock.getItemId() + "", "drawable", context.getPackageName());

            if (resourceId != 0)
                holder.imgItemIcon.setImageResource(resourceId);
            else
                holder.imgItemIcon.setImageResource(R.drawable.mtndew);*/

            Picasso.with(context)
                    .load(ServiceURLConstants.SKU_IMAGE_URL + vehicleStock.getImageName())
                    .placeholder(R.drawable.pepsi_mock_image)
                    .into(holder.imgItemIcon);


            holder.txtItemName.setText(vehicleStock.getItemName());

            holder.txtItemType.setText(ItemUtil.getItemType(vehicleStock.getItemTypeId()));

            holder.txtAvailableQuantity.setText((vehicleStock.getCaseQuantity() != 0 ? ((int) vehicleStock.getCaseQuantity()) + " CS  " : "") + (vehicleStock.getBottleQuantity() != 0 ? (int) vehicleStock.getBottleQuantity() + "  FB  " : ""));

            holder.txtItemMRP.setText(AbstractApplication.get().getString(R.string.withsemicolumnmrp)+ vehicleStock.getLineMRP() );

            holder.vehicleStock = vehicleStock;


        } catch (Exception ex) {
            Logger.Log(VehicleStockListAdapter.class.getName(), ex);
        }

    }

    @Override
    public int getItemCount() {
        return vehicleStockList.size();
    }
}

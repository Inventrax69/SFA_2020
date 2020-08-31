package com.inventrax_pepsi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.database.pojos.VehicleStock;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: Vehicle Stock View Holders
 */



public class VehicleStockViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtItemName, txtAvailableQuantity, txtItemType,txtItemMRP;
    public ImageView imgItemIcon;
    public VehicleStock vehicleStock;


    public VehicleStockViewHolders(View itemView) {
        super(itemView);

        txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
        txtAvailableQuantity = (TextView) itemView.findViewById(R.id.txtAvailableQuantity);
        txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);
        imgItemIcon = (ImageView) itemView.findViewById(R.id.imgItemIcon);
        txtItemMRP = (TextView) itemView.findViewById(R.id.txtItemMRP);

    }

    @Override
    public void onClick(View v) {


    }

}

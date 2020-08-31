package com.inventrax_pepsi.adapters;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.interfaces.OrderSummaryView;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.util.DialogUtils;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: Order View Holders
 */


public class OrderViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductUnitPrice, txtProductDiscount, txtProductQuantity, txtPayableAmount, txtProductScheme, txtDiscountText, txtItemType;
    //public TableRow tableRowProductDiscount,tableRowProductScheme;
    public LinearLayout tableRowProductDiscount, tableRowProductScheme;
    public ImageView imgSKUThumbnail;
    public ImageButton imgButtonDelete;
    public OrderItem item;
    private OrderSummaryView orderSummaryView;


    public OrderViewHolders(View itemView, OrderSummaryView orderSummaryView) {

        super(itemView);

        this.orderSummaryView = orderSummaryView;

        txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
        txtProductUnitPrice = (TextView) itemView.findViewById(R.id.txtProductUnitPrice);
        txtProductDiscount = (TextView) itemView.findViewById(R.id.txtProductDiscount);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.txtProductQuantity);
        txtPayableAmount = (TextView) itemView.findViewById(R.id.txtPayableAmount);
        txtDiscountText = (TextView) itemView.findViewById(R.id.txtDiscountText);
        txtItemType = (TextView) itemView.findViewById(R.id.txtItemType);

        txtProductScheme = (TextView) itemView.findViewById(R.id.txtProductScheme);

        imgSKUThumbnail = (ImageView) itemView.findViewById(R.id.imgSKUThumbnail);

        imgButtonDelete = (ImageButton) itemView.findViewById(R.id.imgButtonDelete);
        imgButtonDelete.setOnClickListener(this);

        /*tableRowProductDiscount=(TableRow)itemView.findViewById(R.id.tableRowProductDiscount);
        tableRowProductScheme=(TableRow)itemView.findViewById(R.id.tableRowProductScheme);*/

        tableRowProductDiscount = (LinearLayout) itemView.findViewById(R.id.tableRowProductDiscount);
        tableRowProductScheme = (LinearLayout) itemView.findViewById(R.id.tableRowProductScheme);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgButtonDelete: {

                DialogUtils.showConfirmDialog(AbstractApplication.getFragmentActivity(), "", AbstractApplication.get().getString(R.string.orderdialogmessage),AbstractApplication.get().getString(R.string.Yes),AbstractApplication.get().getString(R.string.NO), new ConfirmDialogClickListener());

            }
            break;

        }

    }

    private class ConfirmDialogClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {

                    orderSummaryView.onItemDeleted(item, getAdapterPosition());
                    dialog.dismiss();

                }
                break;
                case DialogInterface.BUTTON_NEGATIVE: {

                    dialog.dismiss();

                }
                break;
            }
        }
    }
}

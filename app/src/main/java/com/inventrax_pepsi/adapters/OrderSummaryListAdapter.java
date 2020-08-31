package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.interfaces.OrderSummaryView;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.sfa.pojos.OrderItemScheme;
import com.inventrax_pepsi.sfa.sku.ItemUtil;
import com.inventrax_pepsi.util.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: Order Summary List Adapter
 */


public class OrderSummaryListAdapter extends RecyclerView.Adapter<OrderSummaryViewHolders> {

    private List<OrderItem> itemList;
    private Context context;
    private OrderSummaryView orderSummaryView;
    private Resources resources;


    public OrderSummaryListAdapter(Context context, List<OrderItem> itemList, OrderSummaryView orderSummaryView) {
        setOrderSummaryListAdapter(context, itemList, orderSummaryView);
    }

    public void setOrderSummaryListAdapter(Context context, List<OrderItem> itemList, OrderSummaryView orderSummaryView) {
        this.itemList = itemList;
        this.context = context;
        resources = context.getResources();

        this.orderSummaryView = orderSummaryView;
    }

    @Override
    public OrderSummaryViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_items_row_new, parent, false);

        OrderSummaryViewHolders orderSummaryViewHolders = new OrderSummaryViewHolders(layoutView, orderSummaryView);

        return orderSummaryViewHolders;
    }

    @Override
    public void onBindViewHolder(OrderSummaryViewHolders holder, int position) {

        try {

            OrderItem item = itemList.get(position);

           /* final int resourceId = resources.getIdentifier("p" + item.getItemId() + "", "drawable", context.getPackageName());

            if (resourceId != 0)
                holder.imgSKUThumbnail.setImageResource(resourceId);
            else
                holder.imgSKUThumbnail.setImageResource(R.drawable.pepsi_mock_image);*/

            Picasso.with(context)
                    .load(ServiceURLConstants.SKU_IMAGE_URL + item.getImageName())
                    .placeholder(R.drawable.pepsi_mock_image)
                    .into(holder.imgSKUThumbnail);

            holder.imgButtonDelete.setVisibility(ImageButton.VISIBLE);

            if (item.isHideDeleteButton())
                holder.imgButtonDelete.setVisibility(ImageButton.INVISIBLE);

            holder.txtProductName.setText(item.getItemBrand() + " " + item.getItemPack());

            holder.txtItemType.setText("" + ItemUtil.getItemType(item.getItemTypeId()));

            holder.txtProductUnitPrice.setText("" + item.getItemPrice());
            holder.txtProductQuantity.setText(" " + item.getUoMCode() + "/" + item.getQuantity());
            holder.txtPayableAmount.setText(context.getString(R.string.Rs) + " " + NumberUtils.formatValue(item.getDerivedPrice()));

            holder.txtProductScheme.setText("");
            //holder.tableRowProductScheme.setVisibility(TableRow.GONE);
            holder.txtProductDiscount.setText("");
            holder.tableRowProductScheme.setVisibility(LinearLayout.GONE);


            if (item.getOrderItemSchemes() != null && item.getOrderItemSchemes().size() > 0) {

                holder.txtProductScheme.setText(AbstractApplication.get().getString(R.string.Schemes));

                for (OrderItemScheme orderItemScheme : item.getOrderItemSchemes()) {
                    // holder.txtProductScheme.append(orderItemScheme.getItemCode() + "|" + orderItemScheme.getUoM() + "/" + orderItemScheme.getValue());
                    holder.txtProductScheme.append(orderItemScheme.getItemBrand() + " " + orderItemScheme.getItemPack() + " | " + orderItemScheme.getUoM() + "/" + orderItemScheme.getValue());
                    //holder.tableRowProductScheme.setVisibility(TableRow.VISIBLE);
                    holder.tableRowProductScheme.setVisibility(LinearLayout.VISIBLE);
                }
            }

            holder.txtDiscountText.setText("");
            holder.txtProductDiscount.setText("");
            //holder.tableRowProductDiscount.setVisibility(TableRow.GONE);
            holder.tableRowProductDiscount.setVisibility(LinearLayout.GONE);


            if (item.getOrderItemDiscount() != null) {

                switch (item.getOrderItemDiscount().getDiscountTypeId()) {

                    // Bottle Discount
                    case 1: {

                        holder.txtDiscountText.setText((item.getOrderItemDiscount().isSpot() ? "Spot" : "Monthly") + " Bottle Discount  " + item.getOrderItemDiscount().getItemCode() + " | " + item.getOrderItemDiscount().getUoM() + "/" + item.getOrderItemDiscount().getValue() + " ");

                    }
                    break;

                    // Percentage Discount
                    case 2: {

                        holder.txtDiscountText.setText((item.getOrderItemDiscount().isSpot() ? "Spot" : "Monthly") + " Discount " +  NumberUtils.formatValue(item.getOrderItemDiscount().getDiscountValue()) + "% ");

                    }
                    break;

                    // Fixed Discount
                    case 3: {

                        holder.txtDiscountText.setText((item.getOrderItemDiscount().isSpot() ? "Spot" : "Monthly") + " Discount " + context.getString(R.string.Rs) + NumberUtils.formatValue(item.getOrderItemDiscount().getValue()) + " ");

                    }
                    break;
                }
            }

            /*if (item.getDiscountPrice() != 0 && item.getOrderItemDiscount().isSpot()) {
                holder.txtProductDiscount.setText(NumberUtils.formatDoubleValue(item.getDiscountPrice()));
                //  holder.tableRowProductDiscount.setVisibility(TableRow.VISIBLE);
                holder.tableRowProductDiscount.setVisibility(LinearLayout.VISIBLE);
            } else if (item.getDiscountPrice() != 0) {
                holder.txtProductDiscount.setText("" + item.getPrice());
                //holder.tableRowProductDiscount.setVisibility(TableRow.VISIBLE);
                holder.tableRowProductDiscount.setVisibility(LinearLayout.VISIBLE);
            } else if ( item.getOrderItemDiscount()!=null && item.getOrderItemDiscount().getDiscountTypeId()==1) // Bottle Discount
            {
                holder.tableRowProductDiscount.setVisibility(LinearLayout.VISIBLE);
            }else
            {
                //holder.tableRowProductDiscount.setVisibility(TableRow.GONE);
                holder.tableRowProductDiscount.setVisibility(LinearLayout.GONE);
            }*/

            if (item.getDiscountPrice() != 0 && item.getOrderItemDiscount().getDiscountTypeId()!=1 )
            {
                holder.txtProductDiscount.setText(NumberUtils.formatValue(item.getDiscountPrice()));
                //holder.txtProductDiscount.setText("" + item.getPrice());
                //holder.tableRowProductDiscount.setVisibility(TableRow.VISIBLE);
                holder.tableRowProductDiscount.setVisibility(LinearLayout.VISIBLE);
            }
            else if ( item.getOrderItemDiscount()!=null && item.getOrderItemDiscount().getDiscountTypeId()==1) // Bottle Discount
            {
                holder.tableRowProductDiscount.setVisibility(LinearLayout.VISIBLE);
            }else
            {
                //holder.tableRowProductDiscount.setVisibility(TableRow.GONE);
                holder.tableRowProductDiscount.setVisibility(LinearLayout.GONE);
            }

            holder.item = item;


        } catch (Exception ex) {
            Logger.Log(OrderSummaryListAdapter.class.getName(), ex);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

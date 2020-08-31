package com.inventrax_pepsi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.interfaces.SchemeOfferItemView;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;

import java.util.List;

/**
 * Created by Naresh on 13-Mar-16.
 */

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: Scheme OfferItem List Adapter
 */


public class SchemeOfferItemListAdapter extends RecyclerView.Adapter<SchemeOfferItemListViewHolders> {

    private List<SchemeOfferItem> schemeOfferItems;
    private Context context;
    private SchemeOfferItemView schemeOfferItemView;
    private Item item;
    private int caseQuantity, bottleQuantity;

    public SchemeOfferItemListAdapter(Context context, List<SchemeOfferItem> schemeOfferItems,SchemeOfferItemView schemeOfferItemView) {
        setSchemeOfferItemListAdapter(context, schemeOfferItems,schemeOfferItemView);
    }

    public void setSchemeOfferItemListAdapter(Context context, List<SchemeOfferItem> schemeOfferItems,SchemeOfferItemView schemeOfferItemView) {
        this.schemeOfferItems = schemeOfferItems;
        this.context = context;
        this.schemeOfferItemView=schemeOfferItemView;
    }

    @Override
    public SchemeOfferItemListViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheme_offer_item_list_row, parent, false);

        SchemeOfferItemListViewHolders schemeOfferItemListViewHolders = new SchemeOfferItemListViewHolders(layoutView,schemeOfferItemView);
        schemeOfferItemListViewHolders.setBottleQuantity(bottleQuantity);
        schemeOfferItemListViewHolders.setItem(item);
        schemeOfferItemListViewHolders.setCaseQuantity(caseQuantity);

        return schemeOfferItemListViewHolders;
    }

    @Override
    public void onBindViewHolder(SchemeOfferItemListViewHolders holder, int position) {

        try {

            SchemeOfferItem schemeOfferItem = schemeOfferItems.get(position);

            holder.txtOfferItem.setText(schemeOfferItem.getItemBrand() + " " + schemeOfferItem.getItemPack()  );

            holder.txtOfferItemQty.setText( schemeOfferItem.getUoMCode() +"/"+schemeOfferItem.getQuantity());

            holder.schemeOfferItem = schemeOfferItem;

            holder.setBottleQuantity(bottleQuantity);
            holder.setCaseQuantity(caseQuantity);
            holder.setItem(item);


        } catch (Exception ex) {

            Logger.Log(SchemeOfferItemListAdapter.class.getName(), ex);

        }

    }

    @Override
    public int getItemCount() {
        return schemeOfferItems.size();
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCaseQuantity() {
        return caseQuantity;
    }

    public void setCaseQuantity(int caseQuantity) {
        this.caseQuantity = caseQuantity;
    }

    public int getBottleQuantity() {
        return bottleQuantity;
    }

    public void setBottleQuantity(int bottleQuantity) {
        this.bottleQuantity = bottleQuantity;
    }

}

package com.inventrax_pepsi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.interfaces.SchemeOfferItemView;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;

/**
 * Author   : Naresh P.
 * Date		: 13/03/2016 11:03 AM
 * Purpose	: View Holders
 */



public class SchemeOfferItemListViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOfferItem, txtOfferItemQty;
    public SchemeOfferItem schemeOfferItem;
    private SchemeOfferItemView schemeOfferItemView;
    private Item item;
    private int caseQuantity, bottleQuantity;



    public SchemeOfferItemListViewHolders(View itemView,SchemeOfferItemView schemeOfferItemView) {

        super(itemView);

        txtOfferItem = (TextView) itemView.findViewById(R.id.txtOfferItem);
        txtOfferItemQty = (TextView) itemView.findViewById(R.id.txtOfferItemQty);
        this.schemeOfferItemView=schemeOfferItemView;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (schemeOfferItemView!=null)
            schemeOfferItemView.OnSchemeOfferItemSelected(schemeOfferItem,item,caseQuantity,bottleQuantity);

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

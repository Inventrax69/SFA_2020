package com.inventrax_pepsi.interfaces;

import com.inventrax_pepsi.sfa.pojos.Item;

/**
 * Created by android on 3/11/2016.
 */
public interface SKUListView {

    void updateCart(Item item,int caseQuantity,int bottleQuantity);
    void showBottomBar();
    void hideBottomBar();
    void setRefreshLayoutMargin();
    void showMessage(String message,boolean isDialog);

}

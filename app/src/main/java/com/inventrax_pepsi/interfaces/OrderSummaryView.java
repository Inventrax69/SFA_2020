package com.inventrax_pepsi.interfaces;

import com.inventrax_pepsi.sfa.pojos.OrderItem;

/**
 * Created by Naresh on 13-Mar-16.
 */
public interface OrderSummaryView {

    void onItemDeleted(OrderItem item,int position);
}

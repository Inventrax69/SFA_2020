package com.inventrax_pepsi.interfaces;

import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;

/**
 * Created by android on 5/18/2016.
 */
public interface SchemeOfferItemView {

    void OnSchemeOfferItemSelected(SchemeOfferItem schemeOfferItem, Item item, int caseQuantity, int bottleQuantity);

}

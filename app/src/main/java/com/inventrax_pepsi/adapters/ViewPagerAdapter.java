package com.inventrax_pepsi.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.inventrax_pepsi.fragments.DeliveryFragment;
import com.inventrax_pepsi.fragments.OrderViewFragment;
import com.inventrax_pepsi.fragments.ReturnsFragment;

/**
 * Author   : Naresh P.
 * Date		: 15/03/2016 11:03 AM
 * Purpose	: View Pager Adapter
 */


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private String invoiceNumber;
    private String orderNumber;
    private boolean isFromDelivery;
    private Bundle bundle;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String invoiceNumber, String orderNumber, boolean isFromDelivery) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.invoiceNumber = invoiceNumber;
        this.orderNumber = orderNumber;
        this.isFromDelivery = isFromDelivery;

        bundle = new Bundle();

        bundle = new Bundle();
        bundle.putString("OrderNumber", this.orderNumber);
        bundle.putBoolean("isFromDelivery", this.isFromDelivery);
        bundle.putString("InvoiceNo", this.invoiceNumber);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                OrderViewFragment orderViewFragment = new OrderViewFragment();
                orderViewFragment.setArguments(bundle);

                return orderViewFragment;
            case 1:

                DeliveryFragment deliveryFragment = new DeliveryFragment();
                deliveryFragment.setArguments(bundle);

                return deliveryFragment;
            case 2:

                ReturnsFragment returnsFragment = new ReturnsFragment();
                returnsFragment.setArguments(bundle);

                return returnsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}




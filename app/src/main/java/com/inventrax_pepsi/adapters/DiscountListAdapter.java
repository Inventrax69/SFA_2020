package com.inventrax_pepsi.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Discount List Adapter
 */


public class DiscountListAdapter extends RecyclerView.Adapter {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<CustomerDiscount> customerDiscountList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity fragmentActivity;
    private Gson gson;


    public DiscountListAdapter(FragmentActivity fragmentActivity, List<CustomerDiscount> customerDiscountList, RecyclerView recyclerView) {
        setOutletListAdapter(fragmentActivity, customerDiscountList, recyclerView);
    }

    public void setOutletListAdapter(FragmentActivity fragmentActivity, List<CustomerDiscount> customerDiscountList, RecyclerView recyclerView) {

        this.customerDiscountList = customerDiscountList;
        this.fragmentActivity = fragmentActivity;

        gson = new GsonBuilder().create();

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return customerDiscountList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.discount_list_row, parent, false);

            viewHolder = new DiscountViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {

            if (holder instanceof DiscountViewHolder) {

                CustomerDiscount customerDiscount = (CustomerDiscount) customerDiscountList.get(position);

                ((DiscountViewHolder) holder).txtDiscountCode.setText(customerDiscount.getDiscountCode());
                ((DiscountViewHolder) holder).txtDiscountName.setText(customerDiscount.getDiscountName());
                ((DiscountViewHolder) holder).txtDiscountType.setText(customerDiscount.getDiscountType());


                // Percentage Discount
                if (customerDiscount.getDiscountTypeId() == 2) {

                    ((DiscountViewHolder) holder).txtDiscountValue.setText("" + customerDiscount.getDiscountValue() + "%");

                }

                // Fixed Discount
                if (customerDiscount.getDiscountTypeId() == 3) {

                    ((DiscountViewHolder) holder).txtDiscountValue.setText("Rs. " + customerDiscount.getDiscountValue());

                }

                String stringTargetItem = AbstractApplication.get().getString(R.string.OrderSKU)+ customerDiscount.getTargetItemCode() + " | " + customerDiscount.getTargetItemName() + " | " + customerDiscount.getTargetUoMCode() + " / " + customerDiscount.getTargetItemQty() + " ] ";

                ((DiscountViewHolder) holder).txtTargetItem.setText(stringTargetItem);

                // Bottle Discount
                if (customerDiscount.getDiscountTypeId() == 1) {

                    String stringOfferItem =AbstractApplication.get().getString(R.string.OfferSKU)+ customerDiscount.getOfferItemCode() + " | " + customerDiscount.getOfferItemName() + " | " + customerDiscount.getOfferUoMCode() + " / " + customerDiscount.getOfferQty() + " ] ";

                    ((DiscountViewHolder) holder).txtOfferItem.setText(stringOfferItem);

                } else {

                    ((DiscountViewHolder) holder).txtOfferItem.setVisibility(TextView.GONE);

                }


            } else {

                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

            }
        } catch (Exception ex) {
            Logger.Log(DiscountListAdapter.class.getName(), ex);
        }

    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return customerDiscountList.size();
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarItem);
        }
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDiscountCode, txtDiscountName, txtDiscountType, txtDiscountValue, txtTargetItem, txtOfferItem;
        public CustomerDiscount customerDiscount;

        public DiscountViewHolder(View v) {
            super(v);

            txtDiscountCode = (TextView) v.findViewById(R.id.txtDiscountCode);
            txtDiscountName = (TextView) v.findViewById(R.id.txtDiscountName);
            txtDiscountType = (TextView) v.findViewById(R.id.txtDiscountType);
            txtDiscountValue = (TextView) v.findViewById(R.id.txtDiscountValue);
            txtTargetItem = (TextView) v.findViewById(R.id.txtTargetItem);
            txtOfferItem = (TextView) v.findViewById(R.id.txtOfferItem);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("customerDiscountJSON", gson.toJson(customerDiscount));


                }
            });
        }

    }

}

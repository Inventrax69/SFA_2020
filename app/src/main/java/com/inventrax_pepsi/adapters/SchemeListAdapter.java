package com.inventrax_pepsi.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;
import com.inventrax_pepsi.sfa.pojos.SchemeTargetItem;
import com.inventrax_pepsi.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Scheme List Adapter
 */


public class SchemeListAdapter extends RecyclerView.Adapter {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Scheme> schemeList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity fragmentActivity;
    private Gson gson;


    public SchemeListAdapter(FragmentActivity fragmentActivity, List<Scheme> schemeList, RecyclerView recyclerView) {
        setOutletListAdapter(fragmentActivity, schemeList, recyclerView);
    }

    public void setOutletListAdapter(FragmentActivity fragmentActivity, List<Scheme> schemeList, RecyclerView recyclerView) {

        this.schemeList = schemeList;
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
        return schemeList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.scheme_list_row, parent, false);

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

                Scheme scheme = (Scheme) schemeList.get(position);


                Picasso.with(fragmentActivity)
                        .load(ServiceURLConstants.SKU_IMAGE_URL + (scheme.getSchemeTargetItems() != null ? scheme.getSchemeTargetItems().get(0).getImageName() : ""))
                        .placeholder(R.drawable.pepsi_mock_image)
                        .into(((DiscountViewHolder) holder).skuImageThumbnail);


                ((DiscountViewHolder) holder).txtSchemeCode.setText(scheme.getSchemeCode());
                ((DiscountViewHolder) holder).txtSchemeName.setText(scheme.getSchemeName());
                ((DiscountViewHolder) holder).txtSchemeDescription.setText(scheme.getDescription());
                ((DiscountViewHolder) holder).txtValidity.setText(AbstractApplication.get().getString(R.string.ValidFrom)+ DateUtils.formatDate(scheme.getValidity().getValidFrom()) + AbstractApplication.get().getString(R.string.to) + DateUtils.formatDate(scheme.getValidity().getValidityTo()));


                buildSchemeList(scheme, ((DiscountViewHolder) holder).tableLayout);


            } else {

                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

            }

        } catch (Exception ex) {
            Logger.Log(SchemeListAdapter.class.getName(), ex);

        }

    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return schemeList.size();
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void buildSchemeList(Scheme scheme, TableLayout tableLayout) {

        try {


            if (scheme != null) {

                TableLayout.LayoutParams tableLayoutParams;
                TableRow.LayoutParams tableRowParams;

                tableLayoutParams = new TableLayout.LayoutParams();
                tableRowParams = new TableRow.LayoutParams();
                tableRowParams.setMargins(1, 1, 1, 1);


                tableLayout.removeAllViews();


                TableRow rowHeader = new TableRow(fragmentActivity);
                rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

                rowHeader.addView(getTexView("Order SKU", AbstractApplication.get().getString(R.string.OrderSKUs), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Order Qty.",AbstractApplication.get().getString(R.string.OrderQty), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Offer SKU", AbstractApplication.get().getString(R.string.OfferSKUss), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
                rowHeader.addView(getTexView("Offer Qty.",AbstractApplication.get().getString(R.string.OfferQty), 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

                tableLayout.addView(rowHeader, tableLayoutParams);


                SchemeTargetItem schemeTargetItem = scheme.getSchemeTargetItems().get(0);

                for (SchemeOfferItem schemeOfferItem : scheme.getSchemeOfferItems()) {



                    TableRow rowSchemeDetails = new TableRow(fragmentActivity);
                    rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                    rowSchemeDetails.addView(getTexView("Order SKU", schemeTargetItem.getItemBrand() + " " + schemeTargetItem.getItemPack(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Order Qty.", schemeTargetItem.getUoMCode() + "/" + (int) schemeTargetItem.getQuantity(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Offer SKU", schemeOfferItem.getItemBrand() + " " + schemeOfferItem.getItemPack(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Offer Qty.", schemeOfferItem.getUoMCode() + "/" + (int) schemeOfferItem.getQuantity(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                    tableLayout.addView(rowSchemeDetails, tableLayoutParams);
                }

            }


        } catch (Exception ex) {
            Logger.Log(SKUListAdapterNew.class.getName(), ex);
            return;
        }
    }

    private TextView getTexView(String ID, String TextViewValue, float textSize, int mColor, boolean isBold, boolean IsBackGroundColor, int BackGroundColor) {


        try {
            TextView textView = new TextView(fragmentActivity);

            textView.setText(TextViewValue);
            textView.setTextSize(textSize);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(mColor);
            textView.setGravity(Gravity.LEFT);
            if (isBold)
                textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
            if (IsBackGroundColor)
                textView.setBackgroundColor(BackGroundColor);
            return textView;

        } catch (Exception ex) {

            return null;
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarItem);
        }
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSchemeCode, txtSchemeName, txtSchemeDescription, txtValidity, txtTargetItem, txtOfferItem;
        public CustomerDiscount customerDiscount;
        public ImageView skuImageThumbnail;
        TableLayout tableLayout;

        public DiscountViewHolder(View v) {
            super(v);

            txtSchemeCode = (TextView) v.findViewById(R.id.txtSchemeCode);
            txtSchemeName = (TextView) v.findViewById(R.id.txtSchemeName);
            txtSchemeDescription = (TextView) v.findViewById(R.id.txtSchemeDescription);
            txtValidity = (TextView) v.findViewById(R.id.txtValidity);
            skuImageThumbnail = (ImageView) v.findViewById(R.id.skuImageThumbnail);
            tableLayout = (TableLayout) v.findViewById(R.id.tblSchemeDetails);

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

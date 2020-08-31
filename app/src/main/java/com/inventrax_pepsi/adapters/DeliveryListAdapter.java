package com.inventrax_pepsi.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.fragments.OrderViewTabFragment;
import com.inventrax_pepsi.interfaces.DeliveryListView;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NumberUtils;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Delivery List Adapter
 */

public class DeliveryListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Invoice> invoiceList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity fragmentActivity;
    private Gson gson;
    private DatabaseHelper databaseHelper;
    private TableJSONMessage tableJSONMessage;
    private DeliveryListView deliveryListView;



    public DeliveryListAdapter(FragmentActivity fragmentActivity, List<Invoice> invoiceList, RecyclerView recyclerView,DeliveryListView deliveryListView) {
        setDeliveryListAdapter(fragmentActivity, invoiceList, recyclerView,deliveryListView);
    }

    public void setDeliveryListAdapter(FragmentActivity fragmentActivity, List<Invoice> invoiceList, RecyclerView recyclerView,DeliveryListView deliveryListView) {

        this.deliveryListView=deliveryListView;
        this.invoiceList = invoiceList;
        this.fragmentActivity = fragmentActivity;
        databaseHelper = DatabaseHelper.getInstance();
        tableJSONMessage = databaseHelper.getTableJSONMessage();


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
        return invoiceList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.delivery_list_row, parent, false);

            viewHolder = new OrderViewHolder(view);
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

            if (holder instanceof OrderViewHolder) {

                Invoice invoice = invoiceList.get(position);

                ((OrderViewHolder) holder).txtOutletCode.setText(invoice.getCustomerCode());
                ((OrderViewHolder) holder).txtInvoiceNumber.setText(invoice.getInvoiceNo());
                ((OrderViewHolder) holder).txtOutletName.setText(invoice.getCustomerName());
                ((OrderViewHolder) holder).txtOrderValue.setText(fragmentActivity.getString(R.string.Rs) + "" + NumberUtils.formatValue(invoice.getNetAmount()));
                ((OrderViewHolder) holder).txtOrderStatus.setText(invoice.getOrderStatus());

                ((OrderViewHolder) holder).txtOrderStatus.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_order_status));

                if (invoice.getOrderStatusId() == OrderStatus.Cancelled.getStatus()){
                    ((OrderViewHolder) holder).txtOrderStatus.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_red));
                }


                if (invoice.getInvoiceOrders() != null && invoice.getInvoiceOrders().size() > 0)
                    ((OrderViewHolder) holder).txtOrderNumber.setText(invoice.getInvoiceOrders().get(0).getOrderCode());

                ((OrderViewHolder) holder).txtOrderNumber.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_in_process));

                if (tableJSONMessage.getInvoiceSyncStatus(invoice.getInvoiceId()) == 1) {
                    ((OrderViewHolder) holder).txtOrderNumber.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_success));
                }

                if (invoice.getAuditInfo() != null)
                    ((OrderViewHolder) holder).txtOrderDate.setText(invoice.getAuditInfo().getCreatedDate());

                ((OrderViewHolder) holder).invoice = invoice;

            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception ex) {
            Logger.Log(DeliveryListAdapter.class.getName(), ex);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
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

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOutletCode, txtOrderNumber, txtOrderDate, txtOutletName, txtOrderValue, txtInvoiceNumber, txtOrderStatus;
        public Invoice invoice;

        public OrderViewHolder(View v) {
            super(v);

            txtOutletCode = (TextView) v.findViewById(R.id.txtOutletCode);
            txtOutletName = (TextView) v.findViewById(R.id.txtOutletName);
            txtOrderNumber = (TextView) v.findViewById(R.id.txtOrderNumber);
            txtOrderDate = (TextView) v.findViewById(R.id.txtOrderDate);
            txtOrderValue = (TextView) v.findViewById(R.id.txtOrderValue);
            txtInvoiceNumber = (TextView) v.findViewById(R.id.txtInvoiceNumber);
            txtOrderStatus = (TextView) v.findViewById(R.id.txtOrderStatus);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (invoice.getOrderStatusId() != 6) {

                        deliveryListView.doCheckIn(invoiceList.get(getAdapterPosition()).getCustomerId());

                        Bundle bundle = new Bundle();
                        bundle.putString("InvoiceNo", invoiceList.get(getAdapterPosition()).getInvoiceNo());
                        bundle.putBoolean("isFromDelivery", true);
                        bundle.putInt("CustomerId", invoiceList.get(getAdapterPosition()).getCustomerId());

                        OrderViewTabFragment orderViewTabFragment = new OrderViewTabFragment();
                        orderViewTabFragment.setArguments(bundle);

                        FragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, orderViewTabFragment);
                    }

                }
            });
        }
    }
}


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
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NumberUtils;

import java.util.List;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Order History List Adapter
 */


public class OrderHistoryListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Order> orderList;

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

    public OrderHistoryListAdapter(FragmentActivity fragmentActivity, List<Order> orders, RecyclerView recyclerView) {
        setOrderHistoryListAdapter(fragmentActivity, orders, recyclerView);
    }

    public void setOrderHistoryListAdapter(FragmentActivity fragmentActivity, List<Order> orders, RecyclerView recyclerView) {

        orderList = orders;
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
        return orderList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.order_history_list_row, parent, false);

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

                Order order = orderList.get(position);

                AuditInfo auditInfo = order.getAuditInfo();

                ((OrderViewHolder) holder).txtOrderNumber.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_in_process));

                if (tableJSONMessage.getOrderSyncStatus(order.getAutoSyncId()) == 1)
                    ((OrderViewHolder) holder).txtOrderNumber.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_success));




                ((OrderViewHolder) holder).txtOutletCode.setText(order.getCustomerCode());
                ((OrderViewHolder) holder).txtOutletName.setText(order.getCustomerName());
                ((OrderViewHolder) holder).txtOrderNumber.setText(order.getOrderCode());
                ((OrderViewHolder) holder).txtOrderType.setText(order.getOrderType());

                ((OrderViewHolder) holder).txtOrderStatus.setText(order.getOrderStatus());

                ((OrderViewHolder) holder).txtOrderStatus.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_order_status));

                if (order.getOrderStatusId() == OrderStatus.Cancelled.getStatus()){
                    ((OrderViewHolder) holder).txtOrderStatus.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_red));
                }

                if (auditInfo != null)
                    ((OrderViewHolder) holder).txtOrderDate.setText(auditInfo.getCreatedDate());
                else
                    ((OrderViewHolder) holder).txtOrderDate.setText("");

                ((OrderViewHolder) holder).txtOrderValue.setText(fragmentActivity.getString(R.string.Rs) + "" + NumberUtils.formatValue(order.getDerivedPrice()));

                ((OrderViewHolder) holder).order = order;

            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception ex) {
            Logger.Log(OrderHistoryListAdapter.class.getName(), ex);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
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
        public TextView txtOutletCode, txtOrderNumber, txtOrderDate, txtOutletName, txtOrderValue, txtOrderType, txtOrderStatus;
        public Order order;

        public OrderViewHolder(View v) {
            super(v);

            txtOutletCode = (TextView) v.findViewById(R.id.txtOutletCode);
            txtOutletName = (TextView) v.findViewById(R.id.txtOutletName);
            txtOrderNumber = (TextView) v.findViewById(R.id.txtOrderNumber);
            txtOrderDate = (TextView) v.findViewById(R.id.txtOrderDate);
            txtOrderValue = (TextView) v.findViewById(R.id.txtOrderValue);
            txtOrderType = (TextView) v.findViewById(R.id.txtOrderType);
            txtOrderStatus = (TextView) v.findViewById(R.id.txtOrderStatus);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (order.getOrderStatusId() != 6) {

                        Bundle bundle = new Bundle();
                        bundle.putString("OrderNumber", order.getOrderCode());
                        bundle.putBoolean("isFromDelivery", false);
                        bundle.putInt("CustomerId", order.getCustomerId());

                        OrderViewTabFragment orderViewTabFragment = new OrderViewTabFragment();
                        orderViewTabFragment.setArguments(bundle);

                        FragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, orderViewTabFragment);

                    }

                }
            });
        }
    }
}


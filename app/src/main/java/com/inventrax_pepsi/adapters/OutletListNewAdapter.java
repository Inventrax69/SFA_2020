package com.inventrax_pepsi.adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomerOrderHistory;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;
import com.inventrax_pepsi.fragments.OutletDashboardNewFragment;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.sfa.pojos.AddressBook;
import com.inventrax_pepsi.sfa.pojos.BrandHistory;
import com.inventrax_pepsi.sfa.pojos.CustSKUOrder;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.OutletProfile;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/4/2016.
 */

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: Outlet List Adapter
 */


public class OutletListNewAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Customer> outletList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity fragmentActivity;
    private Gson gson;
    private DatabaseHelper databaseHelper;
    private TableUserTracking tableUserTracking;
    private TableCustomerOrderHistory tableCustomerOrderHistory;



    public OutletListNewAdapter(FragmentActivity fragmentActivity, List<Customer> outlets, RecyclerView recyclerView) {
        setOutletListNewAdapter(fragmentActivity, outlets, recyclerView);
    }

    public void setOutletListNewAdapter(FragmentActivity fragmentActivity, List<Customer> outlets, RecyclerView recyclerView) {

        outletList = outlets;
        this.fragmentActivity = fragmentActivity;
        gson = new GsonBuilder().create();


        databaseHelper = DatabaseHelper.getInstance();
        tableUserTracking = databaseHelper.getTableUserTracking();
        tableCustomerOrderHistory = databaseHelper.getTableCustomerOrderHistory();

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
        return outletList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;


        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.outlet_list_row_new, parent, false);

            viewHolder = new OutletViewHolder(view);
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

            if (holder instanceof OutletViewHolder) {

                Customer singleOutlet = (Customer) outletList.get(position);
                OutletProfile outletProfile = null;
                AddressBook customerAddress = null;


                ((OutletViewHolder) holder).outletVisitStatus.setBackgroundColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_unvisited));

                switch (tableUserTracking.getOutletVisitStatus(singleOutlet.getCustomerId())) {

                    case -1:
                        ((OutletViewHolder) holder).outletVisitStatus.setBackgroundColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_unvisited));
                        break;
                    case 0:
                        ((OutletViewHolder) holder).outletVisitStatus.setBackgroundColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_visited_check_in));
                        break;
                    case 1:
                        ((OutletViewHolder) holder).outletVisitStatus.setBackgroundColor(ContextCompat.getColor(AbstractApplication.get(), R.color.color_visited_check_out));
                        break;

                }


                if (singleOutlet != null) {
                    outletProfile = singleOutlet.getOutletProfile();
                    if (singleOutlet.getAddressBook() != null)
                        customerAddress = singleOutlet.getAddressBook().get(0);
                }

                ((OutletViewHolder) holder).txtOwnerName.setText(singleOutlet.getContactPersonName());
                ((OutletViewHolder) holder).txtOutletName.setText(StringUtils.toCamelCase(singleOutlet.getCustomerName()) + " - [ " + singleOutlet.getCustomerCode() + " ] ");
                ((OutletViewHolder) holder).txtPhoneNumber.setText(singleOutlet.getMobileNo1());
                ((OutletViewHolder) holder).txtOutletType.setText(singleOutlet.getCustomerGroup());

                ((OutletViewHolder) holder).txtCustomerAssets.setText("");

                if (singleOutlet.getCustomerAssets()!=null && singleOutlet.getCustomerAssets().size()>0 )
                    ((OutletViewHolder) holder).txtCustomerAssets.setText(AbstractApplication.get().getString(R.string.Assets)+ singleOutlet.getCustomerAssets().size()+" ] ");


           /* if (customerAddress!=null)
            ((OutletViewHolder) holder).txtLandMark.setText(customerAddress.getLandmark());*/

                ((OutletViewHolder) holder).imgScheduledOutlet.setVisibility(ImageView.GONE);

                if (outletProfile != null) {

                    ((OutletViewHolder) holder).txtAccountType.setText(outletProfile.getAccountType());

                    switch (outletProfile.getAccountType().toLowerCase()){

                        case "pci":{

                            ((OutletViewHolder) holder).txtAccountType.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.pci));

                        }break;

                        case "ccx":{

                            ((OutletViewHolder) holder).txtAccountType.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.ccx));

                        }break;

                        case "mix":{

                            ((OutletViewHolder) holder).txtAccountType.setTextColor(ContextCompat.getColor(AbstractApplication.get(), R.color.mix));

                        }break;
                    }

                    ((OutletViewHolder) holder).txtChannelCode.setText(outletProfile.getChannelCode());

                    if (outletProfile.isScheduledOutlet()) {
                        ((OutletViewHolder) holder).imgScheduledOutlet.setVisibility(ImageView.VISIBLE);
                    }

                }

                ((OutletViewHolder) holder).outlet = singleOutlet;


                ((OutletViewHolder) holder).pieChartOutlet.setNoDataText("No History Available");
                clearPieChartData(((OutletViewHolder) holder).pieChartOutlet);

                loadCustomerBrandHistory(singleOutlet.getCustomerId(), ((OutletViewHolder) holder).pieChartOutlet);


            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        } catch (Exception ex) {
            Logger.Log(OutletListNewAdapter.class.getName(), ex);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return  (outletList == null?0:outletList.size());
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void loadCustomerBrandHistory(int customerId, PieChart pieChart) {

        try {

            CustomerOrderHistory customerOrderHistory = tableCustomerOrderHistory.getCustomerOrderHistory(customerId);

            if (customerOrderHistory != null) {

                CustSKUOrder custSKUOrder = gson.fromJson(customerOrderHistory.getBrandJSON(), CustSKUOrder.class);

                List<BrandHistory> brandHistories = custSKUOrder.getBrandHistory();

                if (brandHistories != null && brandHistories.size() > 0) {

                    displayPieChart(brandHistories, pieChart);

                }


            }


        } catch (Exception ex) {
            Logger.Log(OutletDashboardNewFragment.class.getName(), ex);
            return;
        }

    }

    private void clearPieChartData(PieChart pieChart) {
        try {
            pieChart.setUsePercentValues(false);
            pieChart.setDescription("Brand Sale History");

            // enable hole and configure
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);
            pieChart.setHoleRadius(7);
            pieChart.setTransparentCircleRadius(10);


            // enable rotation of the chart by touch
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);

            // creating data values
            ArrayList<Entry> entries = new ArrayList<>();

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();

            // Brand Color Codes
            List<Integer> colorArray = new ArrayList<>();


            PieDataSet dataset = new PieDataSet(entries, "Brand Sale History");
            dataset.setSliceSpace(3);
            dataset.setSelectionShift(5);

            PieData data = new PieData(labels, dataset); // initialize Piedata
            //data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            pieChart.setData(data); // set data into chart

            pieChart.setDescription("");  // set the description

            int colors[] = NumberUtils.convertIntegers(colorArray);

            dataset.setColors(colors); // set the color

            // undo all highlights
            pieChart.highlightValues(null);

            // customize legends
            Legend l = pieChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);

            pieChart.setDescription("");    // Hide the description
            pieChart.getLegend().setEnabled(false);

            // update pie chart
            pieChart.invalidate();

            pieChart.animateY(5000);

        } catch (Exception ex) {
            Logger.Log(OutletListNewAdapter.class.getName(), ex);
            return;
        }
    }

    private void displayPieChart(List<BrandHistory> brandHistories, PieChart pieChart) {

        try {

            pieChart.setUsePercentValues(false);
            pieChart.setDescription("Brand Sale History");

            // enable hole and configure
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);
            pieChart.setHoleRadius(7);
            pieChart.setTransparentCircleRadius(10);


            // enable rotation of the chart by touch
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);

            // creating data values
            ArrayList<Entry> entries = new ArrayList<>();

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();

            // Brand Color Codes
            List<Integer> colorArray = new ArrayList<>();

            int index = 0;

            if (brandHistories != null) {

                for (BrandHistory brandHistory : brandHistories) {

                    entries.add(new Entry((float) brandHistory.getQuantity(), index));
                    //labels.add(index,brandHistory.getBrandName());
                    labels.add(index, "");
                    colorArray.add(SFACommon.getColorByBrand(brandHistory.getBrandName()));

                    index++;
                }


                PieDataSet dataset = new PieDataSet(entries, "Brand Sale History");
                dataset.setSliceSpace(3);
                dataset.setSelectionShift(5);
                dataset.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return String.valueOf((int) Math.floor(value));
                    }
                });

                PieData data = new PieData(labels, dataset); // initialize Piedata
                //data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                pieChart.setData(data); // set data into chart

                pieChart.setDescription("");  // set the description

                int colors[] = NumberUtils.convertIntegers(colorArray);

                dataset.setColors(colors); // set the color

                // undo all highlights
                pieChart.highlightValues(null);

                // customize legends
                Legend l = pieChart.getLegend();
                l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);

                pieChart.setDescription("");    // Hide the description
                pieChart.getLegend().setEnabled(false);

                // update pie chart
                pieChart.invalidate();
            }

            //pieChart.animateY(3000);


        } catch (Exception ex) {
            Logger.Log(OutletListNewAdapter.class.getName(), ex);
            return;
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarItem);
        }
    }

    public class OutletViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOwnerName, txtOutletName, txtPhoneNumber, txtAccountType, txtLandMark, txtChannelCode, txtOutletType,txtCustomerAssets;
        public Customer outlet;
        public PieChart pieChartOutlet;
        public ImageView imgScheduledOutlet;
        public View outletVisitStatus;


        public OutletViewHolder(View v) {
            super(v);

            txtOwnerName = (TextView) v.findViewById(R.id.txtOwnerName);
            outletVisitStatus = (View) v.findViewById(R.id.outletVisitStatus);
            txtOutletName = (TextView) v.findViewById(R.id.txtOutletName);
            txtPhoneNumber = (TextView) v.findViewById(R.id.txtPhoneNumber);
            txtAccountType = (TextView) v.findViewById(R.id.txtAccountType);
            //  txtLandMark = (TextView) v.findViewById(R.id.txtLandMark);
            pieChartOutlet = (PieChart) v.findViewById(R.id.pieChartOutlet);
            txtChannelCode = (TextView) v.findViewById(R.id.txtChannelCode);
            txtOutletType = (TextView) v.findViewById(R.id.txtOutletType);
            imgScheduledOutlet = (ImageView) v.findViewById(R.id.imgScheduledOutlet);
            txtCustomerAssets = (TextView) v.findViewById(R.id.txtCustomerAssets);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();

                    OutletDashboardNewFragment outletDashboardFragment = new OutletDashboardNewFragment();
                    bundle.putString("customerJSON", gson.toJson(outlet));
                    outletDashboardFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(fragmentActivity, R.id.container_body, outletDashboardFragment);

                }
            });
        }

    }

}

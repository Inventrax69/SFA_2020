package com.inventrax_pepsi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.interfaces.SKUListView;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.SKUHistory;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;
import com.inventrax_pepsi.sfa.pojos.SchemeTargetItem;
import com.inventrax_pepsi.sfa.scheme.SchemeUtil;
import com.inventrax_pepsi.sfa.sku.ItemUtil;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author   : Naresh P.
 * Date		: 04/03/2016 11:03 AM
 * Purpose	: SKU List Adapter
 */


public class SKUListAdapterNew extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Item> itemList;

    private Resources resources;
    private Context context;
    private Context dialogContext;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity fragmentActivity;
    private Gson gson;
    private SKUListView skuListView;
    private MaterialDialog materialSchemeDialog, materialDiscountDialog;
    private Map<Integer, List<SKUHistory>> skuHistoryMap;
    private DatabaseHelper databaseHelper;
    private TableVehicleStock tableVehicleStock;
    private int billingPriceId;

    public void setBillingPriceId(int billingPriceId) {
        this.billingPriceId = billingPriceId;
    }

    public SKUListAdapterNew(FragmentActivity fragmentActivity, List<Item> items, RecyclerView recyclerView, SKUListView skuListView, Map<Integer, List<SKUHistory>> skuHistoryMap) {
        setSKUListAdapter(fragmentActivity, items, recyclerView, skuListView, skuHistoryMap);
    }


    public void setSKUListAdapter(FragmentActivity fragmentActivity, List<Item> items, RecyclerView recyclerView, SKUListView skuListView, Map<Integer, List<SKUHistory>> skuHistoryMap) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .title("Scheme List")
                .customView(R.layout.dialog_discount_scheme, true)
                .positiveText("Ok");

        materialSchemeDialog = builder.build();

        MaterialDialog.Builder builderDiscount = new MaterialDialog.Builder(fragmentActivity)
                .title("Discounts")
                .customView(R.layout.dialog_discount, true)
                .positiveText("Ok");

        materialDiscountDialog = builderDiscount.build();

        this.skuHistoryMap = skuHistoryMap;

        context = AbstractApplication.get();
        resources = context.getResources();
        itemList = items;
        this.fragmentActivity = fragmentActivity;
        gson = new GsonBuilder().create();
        this.skuListView = skuListView;

        // Route Agent
        if (AppController.getUser().getUserTypeId() == 7) {
            databaseHelper = DatabaseHelper.getInstance();
            tableVehicleStock = databaseHelper.getTableVehicleStock();
        }

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
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.sku_list_row_new, parent, false);

            viewHolder = new ItemViewHolder(view);

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

            if (holder instanceof ItemViewHolder) {

                final Item item = itemList.get(position);

                ((ItemViewHolder) holder).txtAvailableStock.setText("");

                if (tableVehicleStock != null) {

                    VehicleStock vehicleStock = tableVehicleStock.getVehicleStock(item.getItemId());

                    if (vehicleStock != null) {

                        ((ItemViewHolder) holder).txtAvailableStock.setText(((vehicleStock.getCaseQuantity() != 0 || vehicleStock.getBottleQuantity() != 0) ? "Stock : " : "") + (vehicleStock.getCaseQuantity() != 0 ? "FC/" + (int) vehicleStock.getCaseQuantity() : "") + "  " + (vehicleStock.getBottleQuantity() != 0 ? " FB/" + (int) vehicleStock.getBottleQuantity() : ""));

                    }

                }

                Picasso.with(context)
                        .load(ServiceURLConstants.SKU_IMAGE_URL + item.getImageName())
                        .placeholder(R.drawable.pepsi_mock_image)
                        .into(((ItemViewHolder) holder).itemIcon);


                ItemPrice itemPrice = item.getItemPrices().get(0);

                ((ItemViewHolder) holder).txtBrandPackName.setText(item.getItemBrand() + " " + item.getItemPack());
                ((ItemViewHolder) holder).txtItemType.setText(ItemUtil.getItemType(item.getItemTypeId()));

                ((ItemViewHolder) holder).txtMRP.setText("");

                if (itemPrice != null) {

                    //((ItemViewHolder) holder).txtMRP.setText("MRP  " + context.getString(R.string.Rs) + " " + itemPrice.getmRP());
                    ((ItemViewHolder) holder).txtTradePrice.setText(context.getString(R.string.Rs) + " " + NumberUtils.formatValue(itemPrice.getTradePrice()));

                    /*((ItemViewHolder) holder).txtMRP.setPaintFlags(((ItemViewHolder) holder).txtMRP.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    ((ItemViewHolder) holder).txtTradePrice.setPaintFlags(((ItemViewHolder) holder).txtTradePrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    if (itemPrice.isTrade()) {
                        ((ItemViewHolder) holder).txtMRP.setPaintFlags(((ItemViewHolder) holder).txtMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    } else {
                        ((ItemViewHolder) holder).txtTradePrice.setPaintFlags(((ItemViewHolder) holder).txtTradePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }*/

                }

                ((ItemViewHolder) holder).txtSKUFCPrice.setText("");
                ((ItemViewHolder) holder).txtSKUFBPrice.setText("");

                if (item.getItemPrices() != null && item.getItemPrices().size() > 0) {

                    boolean fbPriceFound=false,fcPriceFound=false;

                    ((ItemViewHolder) holder).rbgItemPrices.removeAllViews();

                    /*((ItemViewHolder) holder).rbgItemPrices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            changePriceDetails(item,group.getCheckedRadioButtonId(),((ItemViewHolder) holder).txtSKUFCPrice,((ItemViewHolder) holder).txtSKUFBPrice);

                            notifyDataSetChanged();

                        }
                    });*/


                    generateItemPriceRadioButtons(((ItemViewHolder) holder).rbgItemPrices, item.getItemPrices());

                    // Route Agent
                    /*if (AppController.getUser().getUserTypeId() == 7) {

                        generateItemPriceRadioButtons(((ItemViewHolder) holder).rbgItemPrices, item.getItemPrices());

                    }else {

                        ((ItemViewHolder) holder).rbgItemPrices.setVisibility(RadioGroup.GONE);

                    }*/

                    ((ItemViewHolder) holder).txtMRP.setVisibility(TextView.GONE);

                    for (ItemPrice itemPrice1 : item.getItemPrices()) {

                        if ( fcPriceFound == true && fbPriceFound == true )
                            break;

                        for (ItemUoM itemUoM : item.getItemUoMs()) {

                            if ( fcPriceFound ==false && itemPrice1.getUoMCode().equalsIgnoreCase(itemUoM.getUomCode()) && (itemPrice1.getUoMCode().equalsIgnoreCase("FC") || itemPrice1.getUoMCode().equalsIgnoreCase("PACK"))) {

                                //((ItemViewHolder) holder).txtSKUFCPrice.setText("" + (itemPrice1.isTrade() == true ? context.getString(R.string.Rs) + itemPrice1.getTradePrice() : context.getString(R.string.Rs) + itemPrice1.getmRP()));
                                ((ItemViewHolder) holder).txtSKUFCPrice.setText("" + (billingPriceId == 1 ? context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice1.getTradePrice()) : context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice1.getmRP())));

                                fcPriceFound = true;


                            } else if ( fbPriceFound == false && itemPrice1.getUoMCode().equalsIgnoreCase(itemUoM.getUomCode()) && (itemPrice1.getUoMCode().equalsIgnoreCase("FB"))) {

                                ((ItemViewHolder) holder).txtMRP.setText(context.getString(R.string.MRP) + context.getString(R.string.Rs) + " " + NumberUtils.formatValue(itemPrice1.getmRP()));

                                //((ItemViewHolder) holder).txtSKUFBPrice.setText("" + (itemPrice1.isTrade() == true ? context.getString(R.string.Rs) + itemPrice1.getTradePrice() : context.getString(R.string.Rs) + itemPrice1.getmRP()));

                                ((ItemViewHolder) holder).txtSKUFBPrice.setText("" + ( billingPriceId == 1 ? context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice1.getTradePrice()) : context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice1.getmRP())));

                                fbPriceFound = true ;
                            }

                        }

                    }

                }

                ((ItemViewHolder) holder).txtDiscount.setVisibility(TextView.INVISIBLE);
                ((ItemViewHolder) holder).imgScheme.setVisibility(ImageView.INVISIBLE);

                if (item.getItemRebateMap() != null)
                    for (Map.Entry<Integer, Object> entry : item.getItemRebateMap().entrySet()) {

                        switch (entry.getKey()) {

                            // Item Prices Price
                            case 1: {


                            }
                            break;

                            // Customer Discount
                            case 2: {

                                for (CustomerDiscount customerDiscount : (ArrayList<CustomerDiscount>) entry.getValue()) {

                                    switch (customerDiscount.getDiscountTypeId()) {

                                        // Bottle to Bottle
                                        case 1: {

                                            ((ItemViewHolder) holder).txtDiscount.setText("B");
                                            ((ItemViewHolder) holder).txtDiscount.setVisibility(TextView.VISIBLE);
                                        }
                                        break;

                                        // Percentage
                                        case 2: {

                                    /*((ItemViewHolder) holder).txtDiscount.append( customerDiscount.getDiscountValue() + "% on " + customerDiscount.getTargetUoMCode()+"/"+customerDiscount.getTargetItemQty()  );*/

                                            ((ItemViewHolder) holder).txtDiscount.setText((customerDiscount.getDiscountValue()) + "%");
                                            ((ItemViewHolder) holder).txtDiscount.setVisibility(TextView.VISIBLE);

                                        }
                                        break;

                                        // Fixed Price
                                        case 3: {
                                            ((ItemViewHolder) holder).txtDiscount.setText("F");
                                            ((ItemViewHolder) holder).txtDiscount.setVisibility(TextView.VISIBLE);

                                        }
                                        break;
                                    }

                                }

                            }
                            break;

                            // Schemes
                            case 3: {

                                if ((ArrayList<Scheme>) entry.getValue() != null && ((ArrayList<Scheme>) entry.getValue()).size() > 0) {
                                    ((ItemViewHolder) holder).imgScheme.setVisibility(ImageView.VISIBLE);
                                }

                            }
                            break;


                        }

                    }

             /*
            final int resourceId = resources.getIdentifier("p"+item.getItemId()+"", "drawable",context.getPackageName());

            if(resourceId!=0)
                ((ItemViewHolder) holder).itemIcon.setImageResource(resourceId);
            else
                ((ItemViewHolder) holder).itemIcon.setImageResource(R.drawable.pepsi_mock_image);*/

                //((ItemViewHolder) holder).txtAvailableStock.setI(item.getStock());

                ((ItemViewHolder) holder).item = item;

                ((ItemViewHolder) holder).imgFMO.setVisibility(ImageView.INVISIBLE);

                if (item.isFMO()) {
                    ((ItemViewHolder) holder).imgFMO.setVisibility(ImageView.VISIBLE);
                }

                clearSKUOrderHistoryViews(((ItemViewHolder) holder));

                if (skuHistoryMap == null)
                    skuHistoryMap = new HashMap<>();

                if (skuHistoryMap != null) {

                    List<SKUHistory> skuHistories = skuHistoryMap.get(item.getItemId());

                    if (skuHistories == null) {
                        skuHistories = new ArrayList<>();
                    }

                    SKUHistory skuHistory = null;

                    for (; skuHistories.size() < 7; ) {
                        skuHistories.add(new SKUHistory("-", 0));
                    }

                    if (skuHistories != null && skuHistories.size() > 0)

                        for (int index = 0; index < skuHistories.size(); index++) {

                            skuHistory = skuHistories.get(index);

                            switch (index) {

                                case 0: {

                                    ((ItemViewHolder) holder).txtSunday.setText(skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtSundayQty.setText("" + skuHistory.getQuantity());

                                }
                                break;

                                case 1: {

                                    ((ItemViewHolder) holder).txtMonday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtMondayQty.setText("" + skuHistory.getQuantity());

                                }
                                break;

                                case 2: {

                                    ((ItemViewHolder) holder).txtTuesday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtTuesdayQty.setText("" + skuHistory.getQuantity());

                                }
                                break;

                                case 3: {

                                    ((ItemViewHolder) holder).txtWednesday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtWednesdayQty.setText("" + skuHistory.getQuantity());

                                }
                                break;

                                case 4: {

                                    ((ItemViewHolder) holder).txtThursday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtThursdayQty.setText("" + skuHistory.getQuantity());

                                }
                                break;

                                case 5: {
                                    ((ItemViewHolder) holder).txtFriday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtFridayQty.setText("" + skuHistory.getQuantity());
                                }
                                break;

                                case 6: {
                                    ((ItemViewHolder) holder).txtSaturday.setText("" + skuHistory.getDate());
                                    ((ItemViewHolder) holder).txtSaturdayQty.setText("" + skuHistory.getQuantity());
                                }
                                break;

                            }
                        }


                }


            } else {

                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

            }
        } catch (Exception ex) {
            Logger.Log(SKUListAdapterNew.class.getName(), ex);
        }
    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void displaySchemes(Item item) {
        try {
            if (item.getItemRebateMap() != null)
                for (Map.Entry<Integer, Object> entry : item.getItemRebateMap().entrySet()) {

                    switch (entry.getKey()) {

                        // Item Prices Price
                        case 1: {


                        }
                        break;

                        // Customer Discount
                        case 2: {

                            for (CustomerDiscount customerDiscount : (ArrayList<CustomerDiscount>) entry.getValue()) {

                                switch (customerDiscount.getDiscountTypeId()) {

                                    // Bottle to Bottle
                                    case 1: {
                                    }
                                    break;

                                    // Percentage
                                    case 2: {

                                    /*((ItemViewHolder) holder).txtDiscount.append( customerDiscount.getDiscountValue() + "% on " + customerDiscount.getTargetUoMCode()+"/"+customerDiscount.getTargetItemQty()  );*/

                                    }
                                    break;

                                    // Fixed Price
                                    case 3: {
                                    }
                                    break;
                                }

                            }

                        }
                        break;

                        // Schemes
                        case 3: {

                            if ((ArrayList<Scheme>) entry.getValue() != null && ((ArrayList<Scheme>) entry.getValue()).size() > 0) {

                                buildSchemeList((ArrayList<Scheme>) entry.getValue(), item.getItemBrand() + " " + item.getItemPack());

                            }

                        }
                        break;


                    }

                }


        } catch (Exception ex) {
            Logger.Log(SKUListAdapterNew.class.getName(), ex);
            return;
        }
    }

    private void buildSchemeList(ArrayList<Scheme> schemes, String skuName) {

        try {
            TableLayout.LayoutParams tableLayoutParams;
            TableRow.LayoutParams tableRowParams;

            View view = materialSchemeDialog.getCustomView();

            dialogContext = view.getContext();

            TextView txtSKUName = (TextView) view.findViewById(R.id.txtSKUName);
            txtSKUName.setText(skuName);

            TableLayout tableLayout = (TableLayout) view.findViewById(R.id.tblSchemeDetails);

            tableLayoutParams = new TableLayout.LayoutParams();
            tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(1, 1, 1, 1);


            tableLayout.removeAllViews();


            TableRow rowHeader = new TableRow(context);
            rowHeader.setBackgroundColor(Color.parseColor("#075ba1"));

            rowHeader.addView(getTexView("Code", "Scheme Code", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Order Qty.", "Order Qty.", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Offer SKU", "Offer SKU", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);
            rowHeader.addView(getTexView("Offer Qty.", "Offer Qty.", 16, Color.WHITE, false, true, Color.parseColor("#075ba1")), tableRowParams);

            tableLayout.addView(rowHeader, tableLayoutParams);

            //for (Scheme scheme : schemes) {

                SchemeUtil.getSortedSchemes(schemes);

                Scheme scheme = (schemes!=null && schemes.size()>0)? schemes.get(0):null;

                if (scheme==null)
                    return;

                SchemeTargetItem schemeTargetItem = scheme.getSchemeTargetItems().get(0);

                for (SchemeOfferItem schemeOfferItem : scheme.getSchemeOfferItems()) {

                    TableRow rowSchemeDetails = new TableRow(dialogContext);
                    rowSchemeDetails.setBackgroundColor(Color.parseColor("#000000"));

                    rowSchemeDetails.addView(getTexView("Code", scheme.getSchemeCode(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Order Qty.", schemeTargetItem.getUoMCode() + "/" + schemeTargetItem.getQuantity(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Offer SKU", schemeOfferItem.getItemBrand() + " " + schemeOfferItem.getItemPack(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);
                    rowSchemeDetails.addView(getTexView("Offer Qty.", schemeOfferItem.getUoMCode() + "/" + schemeOfferItem.getQuantity(), 16, Color.BLACK, false, true, Color.WHITE), tableRowParams);

                    tableLayout.addView(rowSchemeDetails, tableLayoutParams);
                }

            //}


        } catch (Exception ex) {
            Logger.Log(SKUListAdapterNew.class.getName(), ex);
            return;
        }
    }

    private TextView getTexView(String ID, String TextViewValue, float textSize, int mColor, boolean isBold, boolean IsBackGroundColor, int BackGroundColor) {


        try {
            TextView textView = new TextView(dialogContext);

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

    private void prepareDiscountDialog(Item item) {

        try {

            View view = materialDiscountDialog.getCustomView();

            TextView txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            txtProductName.setText(item.getItemBrand() + " " + item.getItemPack());

            TextView txtDiscount = (TextView) view.findViewById(R.id.txtDiscountValue);

            txtDiscount.setText("");

            if (item.getItemRebateMap() != null)
                for (Map.Entry<Integer, Object> entry : item.getItemRebateMap().entrySet()) {

                    switch (entry.getKey()) {

                        // Customer Discount
                        case 2: {

                            for (CustomerDiscount customerDiscount : (ArrayList<CustomerDiscount>) entry.getValue()) {

                                switch (customerDiscount.getDiscountTypeId()) {

                                    // Bottle to Bottle
                                    case 1: {

                                        txtDiscount.append((customerDiscount.isSpot() ? "Spot" : "Monthly") + " Bottle Discount : ");
                                        txtDiscount.append("\n Order SKU : " + customerDiscount.getTargetItemName() + " | " + customerDiscount.getTargetUoMCode() + "/" + customerDiscount.getTargetItemQty());
                                        txtDiscount.append("\n Offer SKU : " + customerDiscount.getOfferItemName() + " | " + customerDiscount.getOfferUoMCode() + "/" + customerDiscount.getOfferQty());

                                    }
                                    break;

                                    // Percentage
                                    case 2: {

                                        txtDiscount.append((customerDiscount.isSpot() ? "Spot" : "Monthly") + " Percentage Discount : " + " Get " + customerDiscount.getDiscountValue() + "% on " + customerDiscount.getTargetUoMCode() + "/" + customerDiscount.getTargetItemQty() + " \n");

                                    }
                                    break;

                                    // Fixed Price
                                    case 3: {

                                        txtDiscount.append((customerDiscount.isSpot() ? "Spot" : "Monthly") + " Fixed Discount : " + context.getString(R.string.Rs) + customerDiscount.getDiscountValue() + " on " + customerDiscount.getTargetUoMCode() + "/" + customerDiscount.getTargetItemQty() + " \n");

                                    }
                                    break;
                                }

                            }

                        }
                        break;

                    }

                }

        } catch (Exception ex) {

        }

    }

    private void clearSKUOrderHistoryViews(ItemViewHolder holder) {

        holder.txtSunday.setText("");
        holder.txtSundayQty.setText("");
        holder.txtMonday.setText("");
        holder.txtMondayQty.setText("");
        holder.txtTuesday.setText("");
        holder.txtTuesdayQty.setText("");
        holder.txtWednesdayQty.setText("");
        holder.txtWednesday.setText("");
        holder.txtThursdayQty.setText("");
        holder.txtThursday.setText("");
        holder.txtFridayQty.setText("");
        holder.txtFriday.setText("");
        holder.txtSaturday.setText("");
        holder.txtSaturdayQty.setText("");

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarItem);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txtBrandPackName, txtMRP, txtTradePrice, txtDiscount, txtItemType, txtAvailableStock;
        public TextView txtSundayQty, txtMondayQty, txtTuesdayQty, txtWednesdayQty, txtThursdayQty, txtFridayQty, txtSaturdayQty;
        public TextView txtSunday, txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday;
        public TextView txtSKUFCPrice, txtSKUFBPrice;
        public ImageView itemIcon, imgFMO, imgScheme;
        public Item item;
        public RelativeLayout btnAddToCart;

        public EditText txtCaseQuantity, txtBottleQuantity;

        private RadioGroup rbgItemPrices;

        public ItemViewHolder(View v) {
            super(v);

            rbgItemPrices = (RadioGroup)  v.findViewById(R.id.rbgItemPrices);

            rbgItemPrices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {


                    txtSKUFCPrice.setText("");
                    txtSKUFBPrice.setText("");

                    double selectedMRP=changePriceDetails(itemList.get(getAdapterPosition()),group.getCheckedRadioButtonId(),txtSKUFCPrice,txtSKUFBPrice);

                    if (tableVehicleStock != null && selectedMRP > 0 ) {

                        VehicleStock vehicleStock = tableVehicleStock.getVehicleStock(itemList.get(getAdapterPosition()).getItemId(),selectedMRP);

                        txtAvailableStock.setText("");

                        if (vehicleStock != null) {

                            txtAvailableStock.setText(((vehicleStock.getCaseQuantity() != 0 || vehicleStock.getBottleQuantity() != 0) ? "Stock : " : "") + (vehicleStock.getCaseQuantity() != 0 ? "FC/" + (int) vehicleStock.getCaseQuantity() : "") + "  " + (vehicleStock.getBottleQuantity() != 0 ? " FB/" + (int) vehicleStock.getBottleQuantity() : ""));

                        }

                    }

                    //notifyItemChanged(getAdapterPosition());
                }
            });



            txtBrandPackName = (TextView) v.findViewById(R.id.txtBrandPackName);
            txtMRP = (TextView) v.findViewById(R.id.txtMRP);
            txtDiscount = (TextView) v.findViewById(R.id.txtDiscount);
            txtItemType = (TextView) v.findViewById(R.id.txtItemType);
            itemIcon = (ImageView) v.findViewById(R.id.imgSKUThumbnail);
            txtAvailableStock = (TextView) v.findViewById(R.id.txtAvailableStock);

            txtSKUFCPrice = (TextView) v.findViewById(R.id.txtSKUFCPrice);
            txtSKUFBPrice = (TextView) v.findViewById(R.id.txtSKUFBPrice);

            btnAddToCart = (RelativeLayout) v.findViewById(R.id.btnAddToCart);

            txtCaseQuantity = (EditText) v.findViewById(R.id.txtCaseQuantity);
            txtBottleQuantity = (EditText) v.findViewById(R.id.txtBottleQuantity);
            txtTradePrice = (TextView) v.findViewById(R.id.txtTradePrice);
            imgFMO = (ImageView) v.findViewById(R.id.imgFMO);
            imgScheme = (ImageView) v.findViewById(R.id.imgScheme);

            txtSundayQty = (TextView) v.findViewById(R.id.txtSundayQty);
            txtMondayQty = (TextView) v.findViewById(R.id.txtMondayQty);
            txtTuesdayQty = (TextView) v.findViewById(R.id.txtTuesdayQty);
            txtWednesdayQty = (TextView) v.findViewById(R.id.txtWednesdayQty);
            txtThursdayQty = (TextView) v.findViewById(R.id.txtThursdayQty);
            txtFridayQty = (TextView) v.findViewById(R.id.txtFridayQty);
            txtSaturdayQty = (TextView) v.findViewById(R.id.txtSaturdayQty);

            txtSunday = (TextView) v.findViewById(R.id.txtSunday);
            txtMonday = (TextView) v.findViewById(R.id.txtMonday);
            txtTuesday = (TextView) v.findViewById(R.id.txtTuesday);
            txtWednesday = (TextView) v.findViewById(R.id.txtWednesday);
            txtThursday = (TextView) v.findViewById(R.id.txtThursday);
            txtFriday = (TextView) v.findViewById(R.id.txtFriday);
            txtSaturday = (TextView) v.findViewById(R.id.txtSaturday);


            txtDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    prepareDiscountDialog(itemList.get(getAdapterPosition()));
                    materialDiscountDialog.show();

                }
            });

            imgScheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    displaySchemes(itemList.get(getAdapterPosition()));

                    materialSchemeDialog.show();
                }
            });




            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {

                    String strCaseQuantity = txtCaseQuantity.getText().toString(), strBottleQuantity = txtBottleQuantity.getText().toString();

                    if (TextUtils.isEmpty(strCaseQuantity) && TextUtils.isEmpty(strBottleQuantity)) {

                        //Toast.makeText(fragmentActivity, "Please enter quantity", Toast.LENGTH_LONG).show();
                        DialogUtils.showAlertDialog(fragmentActivity, "");

                    } else {

                        try {

                            if (Integer.parseInt(TextUtils.isEmpty(strCaseQuantity) ? "0" : strCaseQuantity) == 0 && Integer.parseInt(TextUtils.isEmpty(strBottleQuantity) ? "0" : strBottleQuantity) == 0) {
                                DialogUtils.showAlertDialog(fragmentActivity, "Quantity cannot be 0");
                                return;
                            }


                        } catch (Exception ex) {
                            DialogUtils.showAlertDialog(fragmentActivity, "Please enter a valid quantity");
                            return;
                        }


                        /*// Route Agent
                        if (AppController.getUser().getUserTypeId() == 7) {*/

                        if (rbgItemPrices.getCheckedRadioButtonId() < 0) {

                            DialogUtils.showAlertDialog(fragmentActivity, "Please select item price");
                            return;
                        }

                            setItemPriceId(item,rbgItemPrices.getCheckedRadioButtonId());

                        skuListView.updateCart(item, Integer.parseInt(TextUtils.isEmpty(strCaseQuantity) ? "0" : strCaseQuantity), Integer.parseInt(TextUtils.isEmpty(strBottleQuantity) ? "0" : strBottleQuantity));

                        txtCaseQuantity.setText("");
                        txtBottleQuantity.setText("");

                        hideKeyBoard(v,txtCaseQuantity);
                        hideKeyBoard(v,txtBottleQuantity);

                        notifyDataSetChanged();
                    }



                }
            });

        }
    }

    private void hideKeyBoard(View v, EditText editText){
        try {

            InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);


        }catch (Exception ex){
            Logger.Log(SKUListAdapterNew.class.getName(),ex);
            return;
        }
    }


    private void generateItemPriceRadioButtons(RadioGroup radioGroup,List<ItemPrice> itemPrices){

        try
        {
            if (itemPrices == null || itemPrices.size() == 0 )
                return;

            boolean isChecked=false;

            for (ItemPrice itemPrice:itemPrices){

                if ( itemPrice.getUoMCode()!=null && itemPrice.getUoMCode().equalsIgnoreCase("FB") )
                //if ( itemPrice.getUoMCode()!=null && ( itemPrice.getUoMCode().equalsIgnoreCase("FC") || itemPrice.getUoMCode().equalsIgnoreCase("PACK") ) )
                {
                    RadioButton rdbtn = new RadioButton(fragmentActivity);

                    rdbtn.setId(itemPrice.getItemPriceId());
                    //  (itemPrice.isTrade() == true ? context.getString(R.string.Rs) + itemPrice.getTradePrice() : context.getString(R.string.Rs) + itemPrice.getmRP())
                    rdbtn.setText("" + context.getString(R.string.Rs) + NumberUtils.getIntValue(itemPrice.getmRP())  );
                    rdbtn.setPadding(0,0,5,0);

                   //  rdbtn.setTextColor(Color.BLACK);

                    if ( itemPrices.size() == 1 ) {
                        rdbtn.setChecked(true);
                        isChecked=true;
                    }

                    if (!isChecked)
                    {
                        rdbtn.setChecked(true);
                        isChecked=true;
                    }

                    radioGroup.addView(rdbtn);

                }

            }



        }catch (Exception ex){
            return;
        }

    }

    private void setItemPriceId(Item item,int itemPriceId){
        try
        {
            if(item.getItemPrices()==null || item.getItemPrices().size()==0)
                return;

            double selectedMRP=0,fcUnits=0;

            for (ItemUoM itemUoM:item.getItemUoMs() )
            {
                if (itemUoM.getUomCode()!=null && ( itemUoM.getUomCode().equalsIgnoreCase("FC") || itemUoM.getUomCode().equalsIgnoreCase("PACK") ))
                {
                    fcUnits=itemUoM.getUnits();
                    break;
                }
            }

            for (ItemPrice itemPrice:item.getItemPrices()) {
                if (itemPrice.isUserSelected())
                    itemPrice.setUserSelected(false);
            }

            for (ItemPrice itemPrice:item.getItemPrices()) {

                //if ( itemPrice.getUoMCode()!=null && ( itemPrice.getUoMCode().equalsIgnoreCase("FC") || itemPrice.getUoMCode().equalsIgnoreCase("PACK") ) && itemPriceId == itemPrice.getItemPriceId() )
                if ( itemPrice.getUoMCode()!=null && itemPrice.getUoMCode().equalsIgnoreCase("FB")  && itemPriceId == itemPrice.getItemPriceId() )
                {
                    itemPrice.setUserSelected(true);
                    selectedMRP=itemPrice.getmRP();
                    break;
                }
            }



            for (ItemPrice itemPrice:item.getItemPrices())
            {
                //if ( itemPrice.getUoMCode() != null && itemPrice.getUoMCode().equalsIgnoreCase("FB") && itemPrice.getmRP() == ( selectedMRP/fcUnits ) )
                if ( itemPrice.getUoMCode() != null && ( itemPrice.getUoMCode().equalsIgnoreCase("FC") || itemPrice.getUoMCode().equalsIgnoreCase("PACK") ) && itemPrice.getmRP() == ( selectedMRP*fcUnits ) )
                {
                    itemPrice.setUserSelected(true);

                    break;
                }
            }


        }catch (Exception ex){
            Logger.Log(SKUListAdapterNew.class.getName(),ex);
            return;
        }
    }


    private double changePriceDetails(Item item,int itemPriceId,TextView txtFCPrice,TextView txtFBPrice){
        double selectedMRP=0,fcUnits=0;

        try
        {
            if(item.getItemPrices()==null || item.getItemPrices().size()==0)
                return 0;



            for (ItemUoM itemUoM:item.getItemUoMs() )
            {
                if (itemUoM.getUomCode()!=null && ( itemUoM.getUomCode().equalsIgnoreCase("FC") || itemUoM.getUomCode().equalsIgnoreCase("PACK") ))
                {
                    fcUnits=itemUoM.getUnits();
                    break;
                }
            }

            for (ItemPrice itemPrice:item.getItemPrices()) {

                //if ( itemPrice.getUoMCode()!=null && ( itemPrice.getUoMCode().equalsIgnoreCase("FC") || itemPrice.getUoMCode().equalsIgnoreCase("PACK") ) && itemPriceId == itemPrice.getItemPriceId() )
                if ( itemPrice.getUoMCode()!=null && itemPrice.getUoMCode().equalsIgnoreCase("FB")  && itemPriceId == itemPrice.getItemPriceId() )
                {
                    selectedMRP=itemPrice.getmRP();
                    txtFBPrice.setText("" + ( billingPriceId == 1 ? context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice.getTradePrice()) : context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice.getmRP())));
                    break;
                }
            }



            for (ItemPrice itemPrice:item.getItemPrices())
            {
                //if ( itemPrice.getUoMCode() != null && itemPrice.getUoMCode().equalsIgnoreCase("FB") && itemPrice.getmRP() == ( selectedMRP/fcUnits ) )
                if ( itemPrice.getUoMCode() != null && ( itemPrice.getUoMCode().equalsIgnoreCase("FC") || itemPrice.getUoMCode().equalsIgnoreCase("PACK") ) && itemPrice.getmRP() == ( selectedMRP*fcUnits ) )
                {
                    txtFCPrice.setText("" + (billingPriceId == 1 ? context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice.getTradePrice()) : context.getString(R.string.Rs) + NumberUtils.formatValue(itemPrice.getmRP())));

                    break;
                }
            }


        }catch (Exception ex){
            Logger.Log(SKUListAdapterNew.class.getName(),ex);
            return 0;
        }

        return selectedMRP;
    }


}


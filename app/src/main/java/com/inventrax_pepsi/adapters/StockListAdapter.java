package com.inventrax_pepsi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.constants.ServiceCode;
import com.inventrax_pepsi.common.constants.ServiceURLConstants;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableVehicleStock;
import com.inventrax_pepsi.database.pojos.VehicleStock;
import com.inventrax_pepsi.fragments.OrderSummaryFragment;
import com.inventrax_pepsi.fragments.StockUploadListFragment;
import com.inventrax_pepsi.interfaces.OnLoadMoreListener;
import com.inventrax_pepsi.interfaces.SKUListView;
import com.inventrax_pepsi.sfa.pojos.ActiveStock;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.CustomerDiscount;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Item;
import com.inventrax_pepsi.sfa.pojos.ItemPrice;
import com.inventrax_pepsi.sfa.pojos.ItemUoM;
import com.inventrax_pepsi.sfa.pojos.Load;
import com.inventrax_pepsi.sfa.pojos.LoadItem;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.SKUHistory;
import com.inventrax_pepsi.sfa.pojos.Scheme;
import com.inventrax_pepsi.sfa.pojos.SchemeOfferItem;
import com.inventrax_pepsi.sfa.pojos.SchemeTargetItem;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.sfa.scheme.SchemeUtil;
import com.inventrax_pepsi.sfa.sku.ItemUtil;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author   : Padmaja.b
 * Date		: 13/08/2020 11:03 AM
 * Purpose	: Stock List Adapter
 */


public class StockListAdapter extends RecyclerView.Adapter {

    private List<ActiveStock> itemList;

    private Resources resources;
    private Context context;

    private FragmentActivity fragmentActivity;
    private Gson gson;
    private SKUListView skuListView;
    private List<ActiveStock> activeStockList;
    private RelativeLayout relativeLayoutUpdate;
    private Button btnUpdate;
    private TextView txtSKUsModified;
    private int selectedCustomerID;

    public StockListAdapter(FragmentActivity fragmentActivity, List<ActiveStock> items, RecyclerView recyclerView, RelativeLayout relativeLayout, Button btnUpdate, TextView txtSKUsModified, int selectedCustomerID) {
        setStockSKUListAdapter(fragmentActivity, items, recyclerView, relativeLayout, btnUpdate, txtSKUsModified, selectedCustomerID);
    }

    public StockListAdapter() {

    }

    public void setStockSKUListAdapter(FragmentActivity fragmentActivity, List<ActiveStock> items, RecyclerView recyclerView, RelativeLayout relativeLayout, Button btnUpdate, TextView txtSKUsModified, int selectedCustomerID) {


        context = AbstractApplication.get();
        resources = context.getResources();
        itemList = items;
        this.fragmentActivity = fragmentActivity;
        gson = new GsonBuilder().create();
        this.skuListView = skuListView;
        relativeLayoutUpdate = relativeLayout;
        this.btnUpdate = btnUpdate;
        this.selectedCustomerID = selectedCustomerID;
        this.txtSKUsModified = txtSKUsModified;

        activeStockList = new ArrayList<>();

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stock_upload_list_row, parent, false);

        viewHolder = new ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        try {

            final ActiveStock item = itemList.get(position);
            Log.v("ABCDE", "" + position);

            ((ItemViewHolder) holder).txtBrandPackName.setText(item.getItemName());

            ((ItemViewHolder) holder).txtSKUPrice.setText("MRP" + ":" + " "+(int) item.getMRP());
            String qty = String.valueOf(item.getQuantity());
            ((ItemViewHolder) holder).txtCaseQuantity.setText(qty.split("[.]")[0]);
            ((ItemViewHolder) holder).txtBottleQuantity.setText(qty.split("[.]")[1]);

            ((ItemViewHolder) holder).btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    relativeLayoutUpdate.setVisibility(View.VISIBLE);

                    ActiveStock activeStock = itemList.get(position);

                    String strCaseQuantity = ((ItemViewHolder) holder).txtCaseQuantity.getText().toString(), strBottleQuantity = ((ItemViewHolder) holder).txtBottleQuantity.getText().toString();

                    if (TextUtils.isEmpty(strCaseQuantity) && TextUtils.isEmpty(strBottleQuantity)) {

                        //Toast.makeText(fragmentActivity, "Please enter quantity", Toast.LENGTH_LONG).show();
                        DialogUtils.showAlertDialog(fragmentActivity, "");

                    } else {

                        try {

                           /* if (Integer.parseInt(TextUtils.isEmpty(strCaseQuantity) ? "0" : strCaseQuantity) == 0 && Integer.parseInt(TextUtils.isEmpty(strBottleQuantity) ? "0" : strBottleQuantity) == 0) {
                                DialogUtils.showAlertDialog(fragmentActivity, "Quantity cannot be 0");
                                return;
                            }*/


                        } catch (Exception ex) {
                            DialogUtils.showAlertDialog(fragmentActivity, "Please enter a valid quantity");
                            return;
                        }


                        activeStock.setQuantity(Double.parseDouble(((ItemViewHolder) holder).txtCaseQuantity.getText().toString()));
                        activeStock.setFullBottle(Double.parseDouble(((ItemViewHolder) holder).txtBottleQuantity.getText().toString()));
                        activeStockList.add(activeStock);

                        txtSKUsModified.setText(String.valueOf(activeStockList.size()));

                        /*((ItemViewHolder) holder).txtCaseQuantity.setText("");
                        ((ItemViewHolder) holder).txtBottleQuantity.setText("");*/

                        hideKeyBoard(view, ((ItemViewHolder) holder).txtCaseQuantity);
                        hideKeyBoard(view, ((ItemViewHolder) holder).txtBottleQuantity);

                        notifyDataSetChanged();
                    }
                }
            });


            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activeStockList.size() > 0) {
                        updateListAsync();
                    }

                }
            });

        } catch (Exception ex) {
            Logger.Log(StockListAdapter.class.getName(), ex);
        }

    }


    @SuppressLint("StaticFieldLeak")
    public void updateListAsync() {


        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String s = updateList();
                return s;
            }

            @Override
            protected void onPostExecute(String responseJSON) {
                super.onPostExecute(responseJSON);

                if (!TextUtils.isEmpty(responseJSON)) {

                    try {
                        JSONObject jsonObject = null;

                        jsonObject = new JSONObject(responseJSON);
                        JSONObject resultJsonObject = jsonObject.getJSONObject("RootObject");

                        RootObject rootObject = gson.fromJson(resultJsonObject.toString(), RootObject.class);

                        ExecutionResponse executionResponse = null;

                        if (rootObject != null)
                            executionResponse = rootObject.getExecutionResponse();

                        if (executionResponse != null) {

                            if (executionResponse.getSuccess() == 1) {
                                Log.d("x", responseJSON);

                                StockUploadListFragment stockUploadListFragment = new StockUploadListFragment();

                                Bundle bundle = new Bundle();
                                bundle.putInt("customerIdSelected", selectedCustomerID);
                                stockUploadListFragment.setArguments(bundle);

                                FragmentUtils.replaceFragment(fragmentActivity, R.id.container_body, stockUploadListFragment);

                            } else {
                                Toast.makeText(fragmentActivity, "Failed to update stock. Please try again or contact support team", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ProgressDialogUtils.closeProgressDialog();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //ProgressDialogUtils.showProgressDialog();
            }
        }.execute();


    }


    private String updateList() {

        String result = null;
        try {

            User user = AppController.getUser();

            Load load = new Load();
            List<LoadItem> loadItemList = new ArrayList<>();
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setUserId(user.getUserId());
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            auditInfo.setCreatedDate(date);
            load.setAuditInfo(auditInfo);

            for (int i = 0; i < activeStockList.size(); i++) {

                ActiveStock activeStock = activeStockList.get(i);
                LoadItem loadItem = new LoadItem();

                loadItem.setItemName(activeStock.getItemName());
                loadItem.setItemCode(activeStock.getItemCode());
                loadItem.setQuantity(activeStock.getQuantity());
                loadItem.setItemId(activeStock.getItemId());
                loadItem.setItemUoMId(activeStock.getItemUoMId());
                loadItem.setUoMId(activeStock.getUomId());
                loadItem.setUom(activeStock.getUomCode());
                loadItem.setLineMRP(activeStock.getMRP());
                loadItem.setFBquantitiy(activeStock.getFullBottle());

                load.setStoreId(activeStock.getStoreId());

                loadItemList.add(loadItem);

            }

            List<Load> loadList = new ArrayList<>();
            load.setLoadItems(loadItemList);
            loadList.add(load);

            if (loadList.size() > 0) {


                RootObject rootObject = new RootObject();

                rootObject.setServiceCode(ServiceCode.UPDATE_WEEKLY_STOCK_AT_DEPOT);
                rootObject.setLoginInfo(AppController.getLoginInfo());
                rootObject.setLoads(loadList);
                //rootObject.setAsset(customerAuditInfo);


                List<PropertyInfo> propertyInfoList = new ArrayList<PropertyInfo>();

                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("jsonStr");
                propertyInfo.setValue(gson.toJson(rootObject));
                propertyInfo.setType(String.class);
                propertyInfoList.add(propertyInfo);

                result = String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.UPDATE_WEEKLY_STOCK_AT_DEPOT));

                //Log.d("JsonRes", String.valueOf(SoapUtils.getJSONResponse(propertyInfoList, ServiceURLConstants.GET_ACTIVE_STOCK_DEPOTWISE)));

            }

        } catch (Exception ex) {
            Logger.Log("Exception", ex);
        }

        return result;

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txtBrandPackName, txtSKUPrice;

        public RelativeLayout btnAdd;

        public EditText txtCaseQuantity, txtBottleQuantity;


        public ItemViewHolder(View v) {
            super(v);

            txtBrandPackName = (TextView) v.findViewById(R.id.txtBrandPackName);

            txtSKUPrice = (TextView) v.findViewById(R.id.txtSKUPrice);

            btnAdd = (RelativeLayout) v.findViewById(R.id.btnAdd);

            txtCaseQuantity = (EditText) v.findViewById(R.id.txtCaseQuantity);
            txtBottleQuantity = (EditText) v.findViewById(R.id.txtBottleQuantity);



           /* btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Load load = itemList.get();


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


                        txtCaseQuantity.setText("");
                        txtBottleQuantity.setText("");

                        hideKeyBoard(v, txtCaseQuantity);
                        hideKeyBoard(v, txtBottleQuantity);

                        notifyDataSetChanged();
                    }


                }
            });*/

        }
    }

    private void hideKeyBoard(View v, EditText editText) {
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);


        } catch (Exception ex) {
            Logger.Log(StockListAdapter.class.getName(), ex);
            return;
        }
    }
}


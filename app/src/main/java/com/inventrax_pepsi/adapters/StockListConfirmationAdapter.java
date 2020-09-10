package com.inventrax_pepsi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.inventrax_pepsi.fragments.OutletRegistrationFragment;
import com.inventrax_pepsi.fragments.StockUploadListFragment;
import com.inventrax_pepsi.interfaces.SKUListView;
import com.inventrax_pepsi.sfa.pojos.ActiveStock;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.ExecutionResponse;
import com.inventrax_pepsi.sfa.pojos.Load;
import com.inventrax_pepsi.sfa.pojos.LoadItem;
import com.inventrax_pepsi.sfa.pojos.RootObject;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;
import com.inventrax_pepsi.util.SoapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Author   : Padmaja.b
 * Date		: 13/08/2020 11:03 AM
 * Purpose	: Stock List Adapter
 */


public class StockListConfirmationAdapter extends RecyclerView.Adapter {

    private List<ActiveStock> itemList;

    private Resources resources;
    private Context context;

    private FragmentActivity fragmentActivity;
    private Gson gson;

    private List<ActiveStock> activeStockList;

    private int selectedCustomerID;



    public StockListConfirmationAdapter(FragmentActivity fragmentActivity, List<ActiveStock> items,int selectedCustomerID) {
        setStockSKUListAdapter(fragmentActivity, items, selectedCustomerID);
    }

    public StockListConfirmationAdapter() {

    }

    public void setStockSKUListAdapter(FragmentActivity fragmentActivity,List<ActiveStock> items, int selectedCustomerID) {


        context = AbstractApplication.get();
        resources = context.getResources();
        itemList = items;
        this.fragmentActivity = fragmentActivity;
        gson = new GsonBuilder().create();
        this.selectedCustomerID = selectedCustomerID;

        activeStockList = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stock_upload_confirmation_dialog_row, parent, false);

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
            ((ItemViewHolder) holder).txtCaseQuantity.setText(String.valueOf((int) item.getQuantity()));
            ((ItemViewHolder) holder).txtBottleQuantity.setText(String.valueOf((int) item.getFullBottle()));


        } catch (Exception ex) {
            Logger.Log(StockListConfirmationAdapter.class.getName(), ex);
        }



    }


    private class handleUserConfirmation implements DialogInterface.OnClickListener{


        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {

                    if (activeStockList.size() > 0) {
                        updateListAsync();
                    }else {
                        Toast.makeText(context, "Add at least one sku to proceed", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
                break;
                case DialogInterface.BUTTON_NEGATIVE: {

                    dialog.dismiss();

                }
                break;
            }

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

        public EditText txtCaseQuantity, txtBottleQuantity;



        public ItemViewHolder(View v) {
            super(v);

            txtBrandPackName = (TextView) v.findViewById(R.id.txtBrandPackName);

            txtSKUPrice = (TextView) v.findViewById(R.id.txtSKUPrice);

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
            Logger.Log(StockListConfirmationAdapter.class.getName(), ex);
            return;
        }
    }
}


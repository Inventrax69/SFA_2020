package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableCustomerReturn;
import com.inventrax_pepsi.database.TableCustomerTrans;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.pojos.CustomerReturn;
import com.inventrax_pepsi.database.pojos.CustomerTrans;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.CustomerTransaction;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

/**
 * Created by android on 3/15/2016.
 */
public class CustomerReturnsFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private TextView txtOutletCode, txtOutletName,  txtCases, txtBottles;
    private EditText inputCases, inputBottles, inputCrateQuantity,inputCrownsValue,inputCreditAmount;
    private TableRow tableRowCreditAmount;
    private Button btnReceive;
    private DatabaseHelper databaseHelper;
    private TableCustomer tableCustomer;
    private Gson gson;
    private Customer customer;
    private TableCustomerReturn tableCustomerReturn;
    private TableCustomerTrans tableCustomerTrans;
    private BackgroundServiceFactory backgroundServiceFactory;
    private TableJSONMessage tableJSONMessage;
    private FloatingActionButton fabReturn;
    private SFACommon sfaCommon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer_returns, container, false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        doReturn();

                        return true;
                    }
                }
                return false;
            }
        });

        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableCustomer = databaseHelper.getTableCustomer();
            tableCustomerReturn = databaseHelper.getTableCustomerReturn();
            tableJSONMessage = databaseHelper.getTableJSONMessage();
            tableCustomerTrans = databaseHelper.getTableCustomerTrans();

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getActivity());

            this.customer=(Customer)gson.fromJson(getArguments().getString("customerJSON"),Customer.class);

            txtOutletCode = (TextView) rootView.findViewById(R.id.txtOutletCode);
            txtOutletName = (TextView) rootView.findViewById(R.id.txtOutletName);
            txtCases = (TextView) rootView.findViewById(R.id.txtNoOfCases);
            txtBottles = (TextView) rootView.findViewById(R.id.txtNoOfBottles);
            inputCases = (EditText) rootView.findViewById(R.id.inputCaseQuantity);
            inputBottles = (EditText) rootView.findViewById(R.id.inputBottleQuantity);
            inputCrateQuantity = (EditText) rootView.findViewById(R.id.inputCrateQuantity);
            inputCrownsValue = (EditText) rootView.findViewById(R.id.inputCrownsValue);
            inputCreditAmount= (EditText) rootView.findViewById(R.id.inputCreditAmount);

            tableRowCreditAmount = (TableRow) rootView.findViewById(R.id.tableRowCreditAmount);



            btnReceive = (Button) rootView.findViewById(R.id.btnReceive);
            btnReceive.setOnClickListener(this);

            fabReturn = (FloatingActionButton)rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);

            if (customer!=null) {

                txtOutletCode.setText("" + customer.getCustomerCode());
                txtOutletName.setText("" + customer.getCustomerName());

                hideCreditAmount();

            }

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }


    private void hideCreditAmount(){

        try
        {
            if (customer.isCreditAccount())
            {
                if ( ( (customer.getCustomerTransaction()==null?0:customer.getCustomerTransaction().getAmount()) - tableCustomerTrans.getCustomerPaidAmount(customer.getCustomerId()) ) > 0) {

                    tableRowCreditAmount.setVisibility(TableRow.VISIBLE);

                    inputCreditAmount.setText("" + NumberUtils.formatValue((customer.getCustomerTransaction() == null ? 0 : customer.getCustomerTransaction().getAmount()) - tableCustomerTrans.getCustomerPaidAmount(customer.getCustomerId())));

                }else {
                    tableRowCreditAmount.setVisibility(TableRow.GONE);
                }

            }else {
                tableRowCreditAmount.setVisibility(TableRow.GONE);
            }

        }catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnReceive: {

                ProgressDialogUtils.showProgressDialog("Please Wait ...");


                processCustomerReturns();

                if (Double.parseDouble(TextUtils.isEmpty(inputCreditAmount.getText().toString()) ? "0" : inputCreditAmount.getText().toString()) > 0 )
                {
                    processCustomerCreditInfo();
                }

                ProgressDialogUtils.closeProgressDialog();


            }
            break;

            case R.id.fabReturn:{

                doReturn();

            }break;
        }

    }


    private void processCustomerReturns() {

        try {

            com.inventrax_pepsi.sfa.pojos.CustomerReturn customerReturn = new com.inventrax_pepsi.sfa.pojos.CustomerReturn();

            customerReturn.setRouteId(customer.getOutletProfile().getRouteId());
            customerReturn.setCustomerId(customer.getCustomerId());
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            customerReturn.setAuditInfo(auditInfo);
            customerReturn.setCustomerCode(customer.getCustomerCode());
            customerReturn.setIsEmpties(true);
            customerReturn.setCustomerName(customer.getCustomerName());
            customerReturn.setNoOfBottles(Integer.parseInt(TextUtils.isEmpty(inputBottles.getText().toString()) ? "0" : inputBottles.getText().toString()));
            customerReturn.setNoOfCases(Integer.parseInt(TextUtils.isEmpty(inputCases.getText().toString()) ? "0" : inputCases.getText().toString()));
            customerReturn.setNoOfShells(Integer.parseInt(TextUtils.isEmpty(inputCrateQuantity.getText().toString()) ? "0" : inputCrateQuantity.getText().toString()));
            customerReturn.setRouteCode(customer.getOutletProfile().getRouteCode());
            customerReturn.setCrownValue( Double.parseDouble( TextUtils.isEmpty(inputCrownsValue.getText().toString())?"0":inputCrownsValue.getText().toString()));


            CustomerReturn localCustomerReturn = new CustomerReturn();
            localCustomerReturn.setRouteId(customer.getOutletProfile().getRouteId());
            localCustomerReturn.setCustomerId(customer.getCustomerId());
            localCustomerReturn.setJson(gson.toJson(customerReturn));


            long cust_ret_auto_inc_id = tableCustomerReturn.createCustomerReturn(localCustomerReturn);

            if (cust_ret_auto_inc_id != 0) {

                localCustomerReturn = tableCustomerReturn.getCustomerReturn(customer.getCustomerId());


                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(localCustomerReturn.getJson());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId(localCustomerReturn.getCustomerId());
                jsonMessage.setNotificationTypeId(4); // For Returns Notification Type Id is 4

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    localCustomerReturn.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    localCustomerReturn.setAutoIncId(localCustomerReturn.getAutoIncId());

                    tableCustomerReturn.updateCustomerReturn(localCustomerReturn);

                }

                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.SuccessfullyUpdated));

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateCustomerReturnService(customer.getCustomerId());
                }

            }

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(CustomerReturnsFragment.class.getName(), ex);
            return;
        }

    }


    private void processCustomerCreditInfo(){
        try
        {
            CustomerTransaction customerTransaction=new CustomerTransaction();

            customerTransaction.setCustomerId(customer.getCustomerId());
            customerTransaction.setAmount(Double.parseDouble(TextUtils.isEmpty(inputCreditAmount.getText().toString()) ? "0" : inputCreditAmount.getText().toString()));
            customerTransaction.setCustomerCode(customer.getCustomerCode());
            customerTransaction.setCustomerName(customer.getCustomerName());
            customerTransaction.setTransactionOn(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));

            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
            auditInfo.setUserId(AppController.getUser().getUserId());
            auditInfo.setUserName(AppController.getUser().getLoginUserId());
            customerTransaction.setAuditInfo(auditInfo);

            CustomerTrans customerTrans=new CustomerTrans();

            customerTrans.setAmount(customerTransaction.getAmount());
            customerTrans.setCustomerId(customer.getCustomerId());
            customerTrans.setJson(gson.toJson(customerTransaction));

            long cust_trans_auto_inc_id = tableCustomerTrans.createCustomerTrans(customerTrans);

            if (cust_trans_auto_inc_id > 0) {

                customerTrans = tableCustomerTrans.getCustomerTrans(customer.getCustomerId());

                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(customerTrans.getJson());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId(customerTrans.getCustomerId());
                jsonMessage.setNotificationTypeId(JsonMessageNotificationType.CustomerTrans.getNotificationType());

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    customerTrans.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    customerTrans.setAutoIncId(customerTrans.getAutoIncId());

                    tableCustomerTrans.updateCustomerTransByAutoIncId(customerTrans);

                }

                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.SuccessfullyUpdated));

                hideCreditAmount();

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateCustomerTransactionService();
                }
            }



        }catch (Exception ex){
            Logger.Log(CustomerReturnsFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(),"Error while processing credit information");
            return;
        }

    }


    private void doReturn(){
        try
        {
            Bundle bundle=new Bundle();
            bundle.putString("customerJSON",getArguments().getString("customerJSON"));
            OutletDashboardNewFragment outletDashboardNewFragment=new OutletDashboardNewFragment();
            outletDashboardNewFragment.setArguments(bundle);

            FragmentUtils.replaceFragmentWithBackStack(getActivity(),R.id.container_body,outletDashboardNewFragment);

        }catch (Exception ex){
            Logger.Log(AssetAuditFragment.class.getName(),ex);
            return;
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_customer_returns));
        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.title_customer_returns));

    }

}

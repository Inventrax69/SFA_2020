package com.inventrax_pepsi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableCustomerReturn;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.pojos.CustomerReturn;
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

/**
 * Created by android on 3/15/2016.
 */
public class ReturnsFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private TextView txtOrderNumber, txtOutletCode, txtOutletName, txtOrderValue, txtCases, txtBottles;
    private EditText inputCases, inputBottles, inputCrateQuantity,inputCrownsValue;
    private Button btnReceive;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableCustomer tableCustomer;
    private Gson gson;
    private Customer customer;
    private Order order;
    private TableInvoice tableInvoice;
    private boolean isFromDelivery = false;
    private String invoiceNumber = "";
    private TableCustomerReturn tableCustomerReturn;
    private BackgroundServiceFactory backgroundServiceFactory;
    private TableJSONMessage tableJSONMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_returns, container, false);

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;
    }

    private void loadFormControls() {

        try {

            ProgressDialogUtils.showProgressDialog();

            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableOrder = databaseHelper.getTableOrder();
            tableCustomer = databaseHelper.getTableCustomer();
            tableInvoice = databaseHelper.getTableInvoice();
            tableCustomerReturn = databaseHelper.getTableCustomerReturn();
            tableJSONMessage = databaseHelper.getTableJSONMessage();

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());


            txtOrderNumber = (TextView) rootView.findViewById(R.id.txtOrderNumber);
            txtOutletCode = (TextView) rootView.findViewById(R.id.txtOutletCode);
            txtOutletName = (TextView) rootView.findViewById(R.id.txtOutletName);
            txtOrderValue = (TextView) rootView.findViewById(R.id.txtOrderValue);
            txtCases = (TextView) rootView.findViewById(R.id.txtNoOfCases);
            txtBottles = (TextView) rootView.findViewById(R.id.txtNoOfBottles);
            inputCases = (EditText) rootView.findViewById(R.id.inputCaseQuantity);
            inputBottles = (EditText) rootView.findViewById(R.id.inputBottleQuantity);
            inputCrateQuantity = (EditText) rootView.findViewById(R.id.inputCrateQuantity);
            inputCrownsValue = (EditText) rootView.findViewById(R.id.inputCrownsValue);

            btnReceive = (Button) rootView.findViewById(R.id.btnReceive);
            btnReceive.setOnClickListener(this);

            isFromDelivery = (getArguments() != null && getArguments().getBoolean("isFromDelivery") == true) ? true : false;

            if (isFromDelivery) {

                if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("InvoiceNo"))) {

                    invoiceNumber = getArguments().getString("InvoiceNo");

                    displayInvoiceInformation();
                }
            } else {
                displayOrderInformation();
            }


            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }

    private void displayInvoiceInformation() {

        try {

            DeliveryInvoice deliveryInvoice = tableInvoice.getInvoiceByNumber(invoiceNumber);
            Invoice invoice = null;


            if (deliveryInvoice != null) {

                invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);
                customer = gson.fromJson(tableCustomer.getCustomer(invoice.getCustomerId()).getCompleteJSON(), Customer.class);
            }

            if (invoice != null) {
                txtOutletCode.setText(invoice.getCustomerCode() + "");
                txtOutletName.setText(invoice.getCustomerName());
                txtOrderNumber.setText(invoice.getInvoiceOrders().get(0).getOrderCode() + "");
                txtOrderValue.setText(getString(R.string.Rs) + NumberUtils.formatValue(invoice.getNetAmount()) + "");

            } else {
                txtOrderNumber.setText("");
                txtOrderValue.setText("");
                txtOutletName.setText("");
                txtOutletCode.setText("");
                txtCases.setText("");
                txtBottles.setText("");
                inputBottles.setText("");
                inputCases.setText("");
            }


        } catch (Exception ex) {
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading order information");
            return;
        }

    }

    private void displayOrderInformation() {
        try {

            com.inventrax_pepsi.database.pojos.Order localOrder = tableOrder.getOrder(getArguments().getString("OrderNumber"));

            if (localOrder != null) {

                order = gson.fromJson(localOrder.getOrderJSON(), Order.class);
                customer = gson.fromJson(tableCustomer.getCustomer(order.getCustomerId()).getCompleteJSON(), Customer.class);
            }

            if (order != null) {

                txtOrderNumber.setText(order.getOrderCode() + "");
                txtOutletCode.setText(order.getCustomerCode() + "");
                txtOutletName.setText(order.getCustomerName());
                txtOrderValue.setText(getString(R.string.Rs) + "" + NumberUtils.formatValue(order.getDerivedPrice()));
                txtBottles.setText("" + order.getNoOfBottles());
                txtCases.setText(order.getNoOfCases() + "");
                //  inputBottles.setText("" + order.getNoOfBottles());
                //  inputCases.setText(order.getNoOfCases() + "");
            } else {
                txtOrderNumber.setText("");
                txtOutletCode.setText("");
                txtOutletName.setText("");
                txtOrderValue.setText("");
                txtBottles.setText("");
                txtCases.setText("");
                inputBottles.setText("");
                inputCases.setText("");
            }

        } catch (Exception ex) {
            Logger.Log(ReturnsFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading order information");
            return;
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnReceive: {

                ProgressDialogUtils.showProgressDialog("Please Wait ...");


                processCustomerReturns();


                ProgressDialogUtils.closeProgressDialog();


            }
            break;
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

                DialogUtils.showAlertDialog(getActivity(), "Successfully Updated");


                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateCustomerReturnService(customer.getCustomerId());
                }

                doReturn();

            }

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(ReturnsFragment.class.getName(), ex);
            return;
        }

    }


    private void doReturn(){
        try
        {
            Bundle bundle=new Bundle();
            bundle.putString("customerJSON",gson.toJson(customer));
            OutletDashboardNewFragment outletDashboardNewFragment=new OutletDashboardNewFragment();
            outletDashboardNewFragment.setArguments(bundle);

            FragmentUtils.replaceFragmentWithBackStack(getActivity(),R.id.container_body,outletDashboardNewFragment);

        }catch (Exception ex){
            Logger.Log(PulloutFragment.class.getName(),ex);
            return;
        }
    }

}

package com.inventrax_pepsi.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.application.AbstractApplication;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.common.constants.OrderStatus;
import com.inventrax_pepsi.database.DatabaseHelper;
import com.inventrax_pepsi.database.TableCustomer;
import com.inventrax_pepsi.database.TableInvoice;
import com.inventrax_pepsi.database.TableJSONMessage;
import com.inventrax_pepsi.database.TableOrder;
import com.inventrax_pepsi.database.TableReadySaleInvoice;
import com.inventrax_pepsi.database.TableUserTracking;
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.database.pojos.ReadySaleInvoice;
import com.inventrax_pepsi.evolute_pride.bluetooth.EvolutePrintUtil;
import com.inventrax_pepsi.interfaces.DeliveryView;
import com.inventrax_pepsi.interfaces.OrderView;
import com.inventrax_pepsi.services.gps.GPSLocationService;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.services.sfa_background_services.OrderIntentService;
import com.inventrax_pepsi.sfa.cart.BillingManager;
import com.inventrax_pepsi.sfa.invoice_print.PrintUtil;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.PaymentInfo;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by android on 3/15/2016.
 */
public class DeliveryFragment extends Fragment implements View.OnClickListener, OrderView, DeliveryView {


    private View rootView;
    private TextView txtOrderNumber, txtOutletCode, txtOutletName, txtOrderValue, txtOrderStatus;
    private EditText inputCashCheque;

    private RadioGroup rbgPaymentMode;
    private RadioButton rbCash, rbCheque;
    private Button btnReceive;
    private TableRow tableRowCashCheque, tableRowBankList;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableInvoice tableInvoice;
    private TableReadySaleInvoice tableReadySaleInvoice;
    private TableJSONMessage tableJSONMessage;
    private Gson gson;
    private Order order;
    private com.inventrax_pepsi.database.pojos.Order localOrder;
    private Invoice invoice;
    private boolean isFromDelivery = false;
    private String invoiceNumber = "";
    private Spinner spinnerBankList;
    private int paymentMode = 0;
    private OrderUtil orderUtil;
    private List<String> bankList;
    private TableCustomer tableCustomer;
    private TableUserTracking tableUserTracking;
    private ImageView imgBtnCheckout;
    private FloatingActionButton fabPrintInvoice,fabCheckout,fabReturn;
    private GPSLocationService gpsLocationService;
    private BackgroundServiceFactory backgroundServiceFactory;
    private SFACommon sfaCommon;
    private Customer customer=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_delivery, container, false);

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
            tableInvoice = databaseHelper.getTableInvoice();
            tableJSONMessage = databaseHelper.getTableJSONMessage();
            tableCustomer = databaseHelper.getTableCustomer();
            tableReadySaleInvoice = databaseHelper.getTableReadySaleInvoice();
            tableUserTracking = databaseHelper.getTableUserTracking();

            orderUtil = new OrderUtil();

            bankList = Arrays.asList(getResources().getStringArray(R.array.array_bank_list));

            sfaCommon = SFACommon.getInstance();

            gpsLocationService = new GPSLocationService(getContext());

            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setOrderView(this);

            fabPrintInvoice = (FloatingActionButton) rootView.findViewById(R.id.fabPrintInvoice);
            fabPrintInvoice.setOnClickListener(this);
            fabCheckout= (FloatingActionButton) rootView.findViewById(R.id.fabCheckout);
            fabCheckout.setOnClickListener(this);

            fabReturn= (FloatingActionButton) rootView.findViewById(R.id.fabReturn);
            fabReturn.setOnClickListener(this);



            imgBtnCheckout = (ImageView) rootView.findViewById(R.id.imgBtnCheckout);
            imgBtnCheckout.setOnClickListener(this);

            spinnerBankList = (Spinner) rootView.findViewById(R.id.spinnerBankList);
            txtOrderNumber = (TextView) rootView.findViewById(R.id.txtOrderNumber);
            txtOutletName = (TextView) rootView.findViewById(R.id.txtOutletName);
            txtOutletCode = (TextView) rootView.findViewById(R.id.txtOutletCode);
            txtOrderValue = (TextView) rootView.findViewById(R.id.txtOrderValue);
            txtOrderStatus = (TextView) rootView.findViewById(R.id.txtOrderStatus);
            tableRowCashCheque = (TableRow) rootView.findViewById(R.id.tableRowCashCheque);
            tableRowBankList = (TableRow) rootView.findViewById(R.id.tableRowBankList);
            inputCashCheque = (EditText) rootView.findViewById(R.id.inputCashCheque);



            rbCheque = (RadioButton) rootView.findViewById(R.id.rbCheque);
            rbCash = (RadioButton) rootView.findViewById(R.id.rbCash);

            btnReceive = (Button) rootView.findViewById(R.id.btnReceive);
            btnReceive.setOnClickListener(this);
            btnReceive.setText(AbstractApplication.get().getString(R.string.Receives));

            rbgPaymentMode = (RadioGroup) rootView.findViewById(R.id.rbgPaymentMode);


            rbgPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    /*if (rbCheque.isChecked()) {

                        tableRowCashCheque.setVisibility(TableRow.VISIBLE);
                        tableRowBankList.setVisibility(TableRow.VISIBLE);
                        btnReceive.setText("Receive " + getString(R.string.Cheque));
                        inputCashCheque.setHint(getString(R.string.ChequeNumber));
                        inputCashCheque.setInputType(InputType.TYPE_CLASS_TEXT);
                        paymentMode = 2; // For Cheque

                        if (isFromDelivery) {
                            if (invoice.getPaymentInfo() == null)
                                inputCashCheque.setText("");
                        } else {
                            if (order.getPaymentInfo() == null)
                                inputCashCheque.setText("");
                        }

                    } else {

                        tableRowCashCheque.setVisibility(TableRow.VISIBLE);
                        tableRowBankList.setVisibility(TableRow.GONE);
                        btnReceive.setText("Receive " + getString(R.string.Cash));
                        inputCashCheque.setHint("Cash");
                        inputCashCheque.setInputType(InputType.TYPE_CLASS_NUMBER);
                        paymentMode = 1; // For Cash

                        if (isFromDelivery) {

                            if (invoice.getPaymentInfo() == null) {
                                inputCashCheque.setText("" + invoice.getNetAmount());
                            }
                        } else {
                            if (order.getPaymentInfo() == null) {
                                inputCashCheque.setText("" + order.getDerivedPrice());
                            }
                        }

                    }*/

                    changePaymentInfoOnSelection();

                }
            });

            isFromDelivery = (getArguments() != null && getArguments().getBoolean("isFromDelivery") == true) ? true : false;

            if (isFromDelivery) {

                if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("InvoiceNo"))) {

                    invoiceNumber = getArguments().getString("InvoiceNo");
                    displayInvoiceInformation();
                }

                customer = gson.fromJson(tableCustomer.getCustomer(invoice.getCustomerId()).getCompleteJSON(), Customer.class);

                if (customer!=null)
                    inputCashCheque.setEnabled(customer.isCreditAccount()?true:false);

            } else {

                displayOrderInformation();

                customer = gson.fromJson(tableCustomer.getCustomer(order.getCustomerId()).getCompleteJSON(), Customer.class);

                if (customer!=null)
                    inputCashCheque.setEnabled(customer.isCreditAccount()?true:false);

            }


            rbCash.setChecked(true);
            changePaymentInfoOnSelection();

            if (invoice != null) {

                fabPrintInvoice.setVisibility(FloatingActionButton.GONE);

            }

            hideCheckoutButton();

            // check if GPS enabled
            if (!gpsLocationService.canGetLocation()) {
                // Ask user to enable GPS/network in settings
                gpsLocationService.showSettingsAlert();
                return;
            }

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }

    }


    private void changePaymentInfoOnSelection(){

        try
        {
            if (rbCheque.isChecked()) {

                tableRowCashCheque.setVisibility(TableRow.VISIBLE);
                tableRowBankList.setVisibility(TableRow.VISIBLE);
                btnReceive.setText(AbstractApplication.get().getString(R.string.Receivecheque));
                inputCashCheque.setHint(getString(R.string.ChequeNumber));
                inputCashCheque.setInputType(InputType.TYPE_CLASS_TEXT);
                paymentMode = 2; // For Cheque

                if (isFromDelivery) {
                    if (invoice.getPaymentInfo() == null)
                        inputCashCheque.setText("");
                } else {
                    if (order.getPaymentInfo() == null)
                        inputCashCheque.setText("");
                }

            } else {

                tableRowCashCheque.setVisibility(TableRow.VISIBLE);
                tableRowBankList.setVisibility(TableRow.GONE);
                btnReceive.setText(AbstractApplication.get().getString(R.string.Receives));
                inputCashCheque.setHint(getString(R.string.Cash));
                inputCashCheque.setInputType(InputType.TYPE_CLASS_NUMBER);
                paymentMode = 1; // For Cash

                if (isFromDelivery) {

                    if (invoice.getPaymentInfo() == null) {
                        inputCashCheque.setText("" +NumberUtils.formatValue(invoice.getNetAmount()));
                    }
                } else {
                    if (order.getPaymentInfo() == null) {
                        inputCashCheque.setText("" + NumberUtils.formatValue(order.getDerivedPrice()));
                    }
                }

            }


        }catch (Exception ex){
            Logger.Log(DeliveryFragment.class.getName(), ex);
            return;
        }

    }


    private void displayOrderInformation() {
        try {

            localOrder = tableOrder.getOrder(getArguments().getString("OrderNumber"));

            if (localOrder != null) {

                order = gson.fromJson(localOrder.getOrderJSON(), Order.class);
            }

            if (order != null) {
                txtOutletCode.setText(order.getCustomerCode() + "");
                txtOutletName.setText(order.getCustomerName());
                txtOrderNumber.setText(order.getOrderCode() + "");
                txtOrderValue.setText(getString(R.string.Rs) + NumberUtils.formatValue(order.getDerivedPrice()) + "");


                if (order.getPaymentInfo() != null) {

                    txtOrderStatus.setText(order.getOrderStatus());
                    btnReceive.setVisibility(Button.INVISIBLE);

                    if (order.getPaymentInfo().getMode() == 1) {
                        rbCash.setChecked(true);
                        rbCash.setEnabled(false);
                        rbCheque.setEnabled(false);
                        inputCashCheque.setText("" + NumberUtils.formatValue(order.getPaymentInfo().getAmount()));
                    } else {

                        if (bankList != null && bankList.contains(order.getPaymentInfo().getBankName())) {

                            spinnerBankList.setSelection(bankList.indexOf(order.getPaymentInfo().getBankName()));

                        }

                        inputCashCheque.setText(order.getPaymentInfo().getChequeNo());
                        rbCheque.setChecked(true);
                        rbCash.setEnabled(false);
                        rbCheque.setEnabled(false);
                    }

                } else {
                    txtOrderStatus.setText(order.getOrderStatus());
                    btnReceive.setVisibility(Button.VISIBLE);

                }


            } else {
                txtOrderNumber.setText("");
                txtOrderValue.setText("");
                txtOutletName.setText("");
                txtOutletCode.setText("");
                txtOrderStatus.setText("");
            }


        } catch (Exception ex) {
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Errorwhileloadingorderinformation));
            return;
        }
    }


    private void displayInvoiceInformation() {
        try {

            DeliveryInvoice deliveryInvoice = tableInvoice.getInvoiceByNumber(invoiceNumber);

            if (deliveryInvoice != null) {

                invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);
            }

            if (invoice != null) {
                txtOutletCode.setText(invoice.getCustomerCode() + "");
                txtOutletName.setText(invoice.getCustomerName());
                txtOrderNumber.setText(invoice.getInvoiceOrders().get(0).getOrderCode() + "");
                txtOrderValue.setText(getString(R.string.Rs) + NumberUtils.formatValue(invoice.getNetAmount()) + "");



                if (invoice.getPaymentInfo() != null) {

                    txtOrderStatus.setText(invoice.getOrderStatus());
                    btnReceive.setVisibility(Button.INVISIBLE);

                    if (invoice.getPaymentInfo().getMode() == 1) {

                        rbCash.setChecked(true);
                        rbCash.setEnabled(false);
                        rbCheque.setEnabled(false);
                        inputCashCheque.setText("" + NumberUtils.formatValue(invoice.getPaymentInfo().getAmount()));

                    } else {

                        if (bankList != null && bankList.contains(invoice.getPaymentInfo().getBankName())) {

                            spinnerBankList.setSelection(bankList.indexOf(invoice.getPaymentInfo().getBankName()));

                        }

                        inputCashCheque.setText(invoice.getPaymentInfo().getChequeNo());

                        rbCheque.setChecked(true);
                        rbCheque.setEnabled(false);
                        rbCash.setEnabled(false);

                    }

                } else {
                    txtOrderStatus.setText(invoice.getOrderStatus());
                    btnReceive.setVisibility(Button.VISIBLE);
                }


            } else {
                txtOrderNumber.setText("");
                txtOrderValue.setText("");
                txtOutletName.setText("");
                txtOutletCode.setText("");
                txtOrderStatus.setText("");
            }


        } catch (Exception ex) {
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Errorwhileloadingorderinformation));
            return;
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnReceive: {

                if (paymentMode == 0) {
                    DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.Pleaseselectpaymentmode));
                    return;
                }

                if (paymentMode == 2 && (TextUtils.isEmpty(inputCashCheque.getText().toString()) || spinnerBankList.getSelectedItem().toString().equalsIgnoreCase("Select Bank"))) {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.bankandchequeno));
                    return;
                }

                if (paymentMode == 1 && (TextUtils.isEmpty(inputCashCheque.getText().toString()))) {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleaseentercash) );
                    return;
                }

                if (isFromDelivery) {
                    // For Pre - Sale Invoice
                    processPreSaleInvoice();

                    sendNotification();

                } else {
                    // For Ready Sale
                    processReadySaleOrder();

                    sendNotification();
                }

                hideCheckoutButton();

            }
            break;

            case R.id.fabCheckout: {

                DialogUtils.showConfirmDialog(getActivity(), "", AbstractApplication.get().getString(R.string.checkoutmessage),AbstractApplication.get().getString(R.string.Yes), AbstractApplication.get().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (DialogInterface.BUTTON_POSITIVE == which) {
                            doCheckout();
                            dialog.dismiss();
                        }else {
                            dialog.dismiss();
                        }

                    }
                });

            }
            break;

            case R.id.fabPrintInvoice: {

                DialogUtils.showConfirmDialog(getActivity(), "", "Are you sure you want to print?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (DialogInterface.BUTTON_POSITIVE == which) {

                            printInvoice();

                            dialog.dismiss();

                        } else {

                            dialog.dismiss();

                        }

                    }
                });


            }
            break;

            case R.id.fabReturn:{

                doReturn();

            }break;
        }

    }

    private void sendNotification(){
        try
        {
            Intent counterBroadcastIntent=new Intent();
            counterBroadcastIntent.setAction("com.inventrax.broadcast.counter");
            getActivity().sendBroadcast(counterBroadcastIntent);

        }catch (Exception ex){
            Logger.Log(DeliveryFragment.class.getName(),ex);
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
            Logger.Log(AssetComplaintFragment.class.getName(),ex);
            return;
        }
    }


    private void processPreSaleInvoice() {

        try {


            ProgressDialogUtils.showProgressDialog();

            if (invoice != null) {

                if (!orderUtil.issueStock(invoice)) {
                    ProgressDialogUtils.closeProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.insufficientquantity));
                    return;
                }

                invoice.setOrderStatus(OrderStatus.Completed.name());
                invoice.setOrderStatusId(OrderStatus.Completed.getStatus());
                invoice.setPaymentStatus(true);


                PaymentInfo paymentInfo = new PaymentInfo();

                paymentInfo.setCustomerCode(invoice.getCustomerCode());
                paymentInfo.setCustomerId(invoice.getCustomerId());
                paymentInfo.setOrderCode(invoice.getInvoiceOrders().get(0).getOrderCode());
                paymentInfo.setAmount(invoice.getNetAmount());
                paymentInfo.setOrderId(invoice.getInvoiceOrders().get(0).getOrderId());
                paymentInfo.setMode(paymentMode);
                paymentInfo.setPaymentTypeId(paymentMode);

                if (paymentMode == 2) {

                    paymentInfo.setBankName(spinnerBankList.getSelectedItem().toString().equalsIgnoreCase("Select Bank") ? "" : spinnerBankList.getSelectedItem().toString());
                    paymentInfo.setChequeNo(inputCashCheque.getText().toString());

                } else {

                    paymentInfo.setAmount(Double.parseDouble(inputCashCheque.getText().toString()));

                }


                invoice.setPaymentInfo(paymentInfo);

                AuditInfo auditInfo = invoice.getAuditInfo();

                if (auditInfo == null) {

                    auditInfo = new AuditInfo();
                    auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    auditInfo.setUserId(AppController.getUser().getUserId());

                    invoice.setAuditInfo(auditInfo);
                } else {
                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    invoice.setAuditInfo(auditInfo);
                }


                DeliveryInvoice deliveryInvoice = new DeliveryInvoice();

                deliveryInvoice.setInvoiceId(invoice.getInvoiceId());
                deliveryInvoice.setInvoiceJSON(gson.toJson(invoice));


                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(deliveryInvoice.getInvoiceJSON());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId((int) invoice.getInvoiceId());
                jsonMessage.setNotificationTypeId(2); // For Invoice Notification Type Id is 2

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    deliveryInvoice.setJsonMessageAutoIncId((int) json_message_auto_inc_id);

                    tableInvoice.updateInvoiceJSON(deliveryInvoice);
                }

                ProgressDialogUtils.closeProgressDialog();

                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.orderupdated));

                txtOrderStatus.setText(OrderStatus.Completed.name());
                btnReceive.setVisibility(Button.INVISIBLE);


                DeliveryListFragment deliveryListFragment = new DeliveryListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("customerJSON",tableCustomer.getCustomer(invoice.getCustomerId()).getCompleteJSON());
                bundle.putInt("CustomerId", invoice.getCustomerId());
                deliveryListFragment.setArguments(bundle);

                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, deliveryListFragment);


                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateInvoiceUpdateService(invoice.getInvoiceNo());
                }

            }


        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while processing invoice");
            return;
        }
    }

    private void processReadySaleOrder() {

        try {
            ProgressDialogUtils.showProgressDialog();

            if (order != null) {

                if (!orderUtil.issueStock(order)) {
                    ProgressDialogUtils.closeProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(),  AbstractApplication.get().getString(R.string.insufficientquantity));
                    return;
                }

                order.setOrderStatus(OrderStatus.Completed.name());
                order.setOrderStatusId(OrderStatus.Completed.getStatus());
                order.setPaymentStatus(true);


                PaymentInfo paymentInfo = new PaymentInfo();

                paymentInfo.setCustomerCode(order.getCustomerCode());
                paymentInfo.setCustomerId(order.getCustomerId());
                paymentInfo.setAmount(order.getDerivedPrice());
                paymentInfo.setOrderCode(order.getOrderCode());
                paymentInfo.setMode(paymentMode);
                paymentInfo.setPaymentTypeId(paymentMode);

                if (paymentMode == 2) {

                    paymentInfo.setBankName(spinnerBankList.getSelectedItem().toString());
                    paymentInfo.setChequeNo(inputCashCheque.getText().toString());

                } else {

                    paymentInfo.setAmount(Double.parseDouble(inputCashCheque.getText().toString()));

                }

                order.setPaymentInfo(paymentInfo);

                AuditInfo auditInfo = order.getAuditInfo();

                if (auditInfo == null) {

                    auditInfo = new AuditInfo();
                    auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    auditInfo.setUserId(AppController.getUser().getUserId());

                    order.setAuditInfo(auditInfo);
                } else {
                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    order.setAuditInfo(auditInfo);
                }


                com.inventrax_pepsi.database.pojos.Order mLocalOrder = new com.inventrax_pepsi.database.pojos.Order();

                mLocalOrder.setOrderJSON(gson.toJson(order));
                mLocalOrder.setPaymentMode(paymentMode);
                mLocalOrder.setOrderStatus(OrderStatus.Completed.getStatus());
                mLocalOrder.setPaymentStatus(1);


                JSONMessage jsonMessage = new JSONMessage();
                jsonMessage.setJsonMessage(mLocalOrder.getOrderJSON());
                jsonMessage.setNoOfRequests(0);
                jsonMessage.setSyncStatus(0);
                jsonMessage.setNotificationId(localOrder.getAutoIncId());
                jsonMessage.setNotificationTypeId(1); // For Order Notification Type Id is 1

                long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                if (json_message_auto_inc_id != 0) {

                    mLocalOrder.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                    mLocalOrder.setAutoIncId(localOrder.getAutoIncId());

                    tableOrder.updateOrderJSON(mLocalOrder);

                }

                ProgressDialogUtils.closeProgressDialog();

                DialogUtils.showAlertDialog(getActivity(),  AbstractApplication.get().getString(R.string.orderupdated));

                txtOrderStatus.setText(OrderStatus.Completed.name());
                btnReceive.setVisibility(Button.INVISIBLE);


                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                    backgroundServiceFactory.initiateOrderService(order.getOrderCode());
                }

                processReadySaleInvoice();

                /*OrderHistoryListFragment orderHistoryListFragment = new OrderHistoryListFragment();
                Bundle orderHistoryBundle = new Bundle();
                orderHistoryBundle.putString("customerJSON", tableCustomer.getCustomer(order.getCustomerId()).getCompleteJSON());
                orderHistoryBundle.putInt("customerId", order.getCustomerId());
                orderHistoryListFragment.setArguments(orderHistoryBundle);

                FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderHistoryListFragment);*/


            }


        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while processing order");
            return;
        }
    }


    @Override
    public void showOrderSyncStatus(String message, int status) {


        switch (status) {

            case OrderIntentService.STATUS_FINISHED: {

              /*  txtOrderStatus.setText(OrderStatus.Completed.name());
                btnReceive.setVisibility(Button.INVISIBLE);*/

            }
            break;

        }

    }

    private void printInvoice() {

        try {

            if (AppController.setup == null) {
                DialogUtils.showAlertDialog(getActivity(), "Printer Not Initialized");
                return;
            }

            if (order == null) {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.InvalidOrder));
                return;
            }

            PrintUtil printUtil = new PrintUtil(getContext(), getActivity());

            printUtil.setDeliveryView(this);
            printUtil.setSetup(AppController.setup);

            BillingManager billingManager=new BillingManager();
            Invoice localInvoice = billingManager.prepareInvoice(order);

            printUtil.setPrintInvoice(printUtil.generatePrintInvoice(localInvoice));

            printUtil.startPrint();

           /* PrintUtil printUtil=new PrintUtil();


            EvolutePrintUtil evolutePrintUtil=new EvolutePrintUtil(getActivity(),this,printUtil.generatePrintInvoice(order));
            evolutePrintUtil.setDeliveryView(this);*/


        } catch (Exception ex) {
            Logger.Log(DeliveryFragment.class.getName(), ex);
            return;
        }

    }

    @Override
    public void showPrintStatus(int status,String message) {

        DialogUtils.showAlertDialog(getActivity(), message);

    }

    @Override
    public void showProgress() {
        ProgressDialogUtils.showProgressDialog();
    }

    @Override
    public void closeProgress() {
        ProgressDialogUtils.closeProgressDialog();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();

        if (requestCode == EvolutePrintUtil.REQUEST_DISCOVERY) {
            if (Activity.RESULT_OK == resultCode) {
                // Device is selected

                mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                mhtDeviceInfo.put("COD", data.getStringExtra("COD"));
                mhtDeviceInfo.put("RSSI", data.getStringExtra("RSSI"));
                mhtDeviceInfo.put("DEVICE_TYPE", data.getStringExtra("DEVICE_TYPE"));
                mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));

                EvolutePrintUtil.mhtDeviceInfo = mhtDeviceInfo;

                if (mhtDeviceInfo.get("BOND").equals("Nothing")) {

                } else {
                    EvolutePrintUtil.mBDevice = EvolutePrintUtil.mBT.getRemoteDevice(mhtDeviceInfo.get("MAC"));

                }
            } else if (Activity.RESULT_CANCELED == resultCode) {

            }
        } else if (requestCode == EvolutePrintUtil.EXIT_ON_RETURN) {

        }
    }

    private void processReadySaleInvoice(){

        try {

            if (order != null) {

                ProgressDialogUtils.showProgressDialog("Please wait ...");

                BillingManager billingManager = new BillingManager();

                Invoice localInvoice = billingManager.prepareInvoice(order);

                AuditInfo auditInfo = localInvoice.getAuditInfo();

                if (auditInfo == null) {

                    auditInfo = new AuditInfo();
                    auditInfo.setCreatedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    auditInfo.setUserId(AppController.getUser().getUserId());

                    localInvoice.setAuditInfo(auditInfo);

                } else {

                    auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                    auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());

                    localInvoice.setAuditInfo(auditInfo);
                }


                if (localInvoice != null) {


                    ReadySaleInvoice readySaleInvoice = tableReadySaleInvoice.getReadySaleInvoice(localInvoice.getInvoiceNo());

                    if (readySaleInvoice == null)
                        readySaleInvoice = new ReadySaleInvoice();

                    readySaleInvoice.setJson(gson.toJson(localInvoice));
                    readySaleInvoice.setInvoiceNumber(localInvoice.getInvoiceNo());


                    long auto_inc_id = (  readySaleInvoice == null ? 0 : readySaleInvoice.getAutoIncId() );

                    if (auto_inc_id == 0)
                        auto_inc_id = tableReadySaleInvoice.createReadySaleInvoice(readySaleInvoice);

                    if (auto_inc_id != 0) {

                        JSONMessage jsonMessage = new JSONMessage();
                        jsonMessage.setJsonMessage(readySaleInvoice.getJson());
                        jsonMessage.setNoOfRequests(0);
                        jsonMessage.setSyncStatus(0);
                        jsonMessage.setNotificationId((int) auto_inc_id);
                        jsonMessage.setNotificationTypeId(7); // For Ready Sale Invoice Notification Type Id is 7

                        long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                        if (json_message_auto_inc_id != 0) {

                            readySaleInvoice.setJsonMessageAutoIncId((int) json_message_auto_inc_id);
                            readySaleInvoice.setAutoIncId((int) auto_inc_id);

                            tableReadySaleInvoice.updateReadySaleInvoice(readySaleInvoice);

                            if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                                backgroundServiceFactory.initiateReadySaleInvoiceService((int) auto_inc_id);
                            }

                        }

                    }
                }


                ProgressDialogUtils.closeProgressDialog();
            }



        }catch (Exception ex){
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(),ex);
            return;
        }

    }


    private void doCheckout(){

        try
        {
            ProgressDialogUtils.showProgressDialog("Please wait ...");

            if (isFromDelivery)
            {
                if (OrderStatus.Completed.getStatus()!= invoice.getOrderStatusId())
                {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleaseupdatedeliverystatus));
                    ProgressDialogUtils.closeProgressDialog();
                    return;
                }

            }else {
                if (OrderStatus.Completed.getStatus()!= order.getOrderStatusId())
                {
                    DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleaseupdatedeliverystatus));
                    ProgressDialogUtils.closeProgressDialog();
                    return;
                }
            }

            if (customer == null) {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Pleaseupdatedeliverystatus));
                ProgressDialogUtils.closeProgressDialog();
                return;
            }

            if (gpsLocationService.getLatitude() == 0 || gpsLocationService.getLongitude() == 0) {
                DialogUtils.showAlertDialog(getActivity(), AbstractApplication.get().getString(R.string.locationinformation));
                ProgressDialogUtils.closeProgressDialog();
                return;
            }

            if (!sfaCommon.checkInOut(customer, 2, gpsLocationService)) {
                DialogUtils.showAlertDialog(getActivity(),AbstractApplication.get().getString(R.string.Errorwhilecheckingout));
                ProgressDialogUtils.closeProgressDialog();
            } else {

                Toast.makeText(getActivity(), AbstractApplication.get().getString(R.string.checkoutmessage), Toast.LENGTH_LONG).show();

                if (NetworkUtils.getConnectivityStatusAsBoolean(getContext()))
                    backgroundServiceFactory.initiateUserTrackService(customer.getCustomerId(), false);

                if (isFromDelivery){

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, new DeliveryListFragment());

                }else {

                    OutletListNewFragment outletListFragment = new OutletListNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("RouteCode", customer.getOutletProfile().getRouteCode());
                    outletListFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, outletListFragment);
                }

            }

            ProgressDialogUtils.closeProgressDialog();

        }catch (Exception ex){
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(DeliveryFragment.class.getName(),ex);
            return;
        }

    }

    private void hideCheckoutButton(){

        try
        {
            fabCheckout.setVisibility(FloatingActionButton.GONE);



            if (isFromDelivery)
            {
                if (OrderStatus.Completed.getStatus()==invoice.getOrderStatusId())
                    fabCheckout.setVisibility(FloatingActionButton.VISIBLE);

            }else {
                if (OrderStatus.Completed.getStatus()==order.getOrderStatusId())
                    fabCheckout.setVisibility(FloatingActionButton.VISIBLE);
            }

            if (tableUserTracking.getSingleUserTracking(customer.getCustomerId()) != null) {
                fabCheckout.setVisibility(FloatingActionButton.VISIBLE);
            } else {
                fabCheckout.setVisibility(FloatingActionButton.GONE);
            }

        }catch (Exception ex){
            Logger.Log(DeliveryFragment.class.getName(),ex);
            return;
        }


    }




}

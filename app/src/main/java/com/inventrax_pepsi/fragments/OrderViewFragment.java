package com.inventrax_pepsi.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.OrderViewListAdapter;
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
import com.inventrax_pepsi.database.pojos.DeliveryInvoice;
import com.inventrax_pepsi.database.pojos.JSONMessage;
import com.inventrax_pepsi.interfaces.OrderSummaryView;
import com.inventrax_pepsi.interfaces.OrderView;
import com.inventrax_pepsi.services.sfa_background_services.BackgroundServiceFactory;
import com.inventrax_pepsi.sfa.order.OrderUtil;
import com.inventrax_pepsi.sfa.pojos.AuditInfo;
import com.inventrax_pepsi.sfa.pojos.Customer;
import com.inventrax_pepsi.sfa.pojos.Invoice;
import com.inventrax_pepsi.sfa.pojos.InvoiceItem;
import com.inventrax_pepsi.sfa.pojos.InvoiceOrder;
import com.inventrax_pepsi.sfa.pojos.Order;
import com.inventrax_pepsi.sfa.pojos.OrderItem;
import com.inventrax_pepsi.util.DateUtils;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.FragmentUtils;
import com.inventrax_pepsi.util.NetworkUtils;
import com.inventrax_pepsi.util.NumberUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Naresh on 13-Mar-16.
 */
public class OrderViewFragment extends Fragment implements OrderSummaryView, View.OnClickListener, OrderView {

    private View rootView;
    private LinearLayoutManager linearLayoutManager;
    private OrderViewListAdapter orderSummaryListAdapter;
    private RecyclerView recyclerView;
    private Gson gson;
    private Customer customer;
    private Button btnConfirmOrder;
    private CoordinatorLayout coordinatorLayout;
    private Map<Integer, List<OrderItem>> derivedCartOrderItems = null;
    private List<OrderItem> orderItemList = null;
    private TextView txtTotalAmount, txtCases, txtBottles, txtFrees, txtOrderNumber;
    private RelativeLayout layoutOrderSummaryFooter, layoutOrderCancel;
    private Order order;
    private SFACommon sfaCommon;
    private DatabaseHelper databaseHelper;
    private TableOrder tableOrder;
    private TableInvoice tableInvoice;
    private TableCustomer tableCustomer;
    private TableJSONMessage tableJSONMessage;
    private boolean isFromDelivery = false;
    private String invoiceNumber = "";
    private FloatingActionButton fabOrderModification;
    private BackgroundServiceFactory backgroundServiceFactory;
    private OrderUtil orderUtil;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_view_summary, container, false);

        sfaCommon = SFACommon.getInstance();

        new ProgressDialogUtils(getActivity());

        loadFormControls();

        return rootView;

    }

    private void loadFormControls() {
        try {

            ProgressDialogUtils.showProgressDialog();


            backgroundServiceFactory = BackgroundServiceFactory.getInstance();
            backgroundServiceFactory.setActivity(getActivity());
            backgroundServiceFactory.setContext(getContext());
            backgroundServiceFactory.setOrderView(this);


            gson = new GsonBuilder().create();

            databaseHelper = DatabaseHelper.getInstance();
            tableOrder = databaseHelper.getTableOrder();
            tableCustomer = databaseHelper.getTableCustomer();
            tableInvoice = databaseHelper.getTableInvoice();
            tableJSONMessage = databaseHelper.getTableJSONMessage();

            fabOrderModification = (FloatingActionButton) rootView.findViewById(R.id.fabOrderModification);
            fabOrderModification.setOnClickListener(this);

            layoutOrderCancel = (RelativeLayout) rootView.findViewById(R.id.layoutOrderCancel);

            orderItemList = new ArrayList<>();

            orderUtil = new OrderUtil();

            isFromDelivery = (getArguments() != null && getArguments().getBoolean("isFromDelivery") == true) ? true : false;

            if (isFromDelivery) {

                if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("InvoiceNo"))) {

                    invoiceNumber = getArguments().getString("InvoiceNo");

                    loadInvoiceDetails();

                    customer = gson.fromJson(tableCustomer.getCustomer(order.getCustomerId()).getCompleteJSON(), Customer.class);

                }

            }

            com.inventrax_pepsi.database.pojos.Order localOrder = tableOrder.getOrder(getArguments().getString("OrderNumber"));

            if (localOrder != null) {

                order = gson.fromJson(localOrder.getOrderJSON(), Order.class);

                customer = gson.fromJson(tableCustomer.getCustomer(order.getCustomerId()).getCompleteJSON(), Customer.class);

                orderItemList.clear();

                orderItemList = order.getOrderItems();

            }

            buildOrderSummaryHeader();

            buildOrderSummaryItems();

            buildOrderSummaryFooter();

            if (order != null) {

                if (order.getOrderStatusId() >= 6) {

                    layoutOrderCancel.setVisibility(RelativeLayout.GONE);

                }
            }

            ProgressDialogUtils.closeProgressDialog();

        } catch (Exception ex) {
            ProgressDialogUtils.closeProgressDialog();
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryHeader() {
        try {

            txtOrderNumber = (TextView) rootView.findViewById(R.id.txtOrderNumber);
            txtOrderNumber.setText("" + order.getOrderCode());

        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryFooter() {
        try {
            coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.snack_bar_action_layout);
            layoutOrderSummaryFooter = (RelativeLayout) rootView.findViewById(R.id.layoutOrderSummaryFooter);
            txtTotalAmount = (TextView) rootView.findViewById(R.id.txtTotalAmount);
            txtBottles = (TextView) rootView.findViewById(R.id.txtBottles);
            txtCases = (TextView) rootView.findViewById(R.id.txtCases);
            txtFrees = (TextView) rootView.findViewById(R.id.txtFrees);
            btnConfirmOrder = (Button) rootView.findViewById(R.id.btnConfirmOrder);
            btnConfirmOrder.setOnClickListener(this);

            displayOrderSummaryFooter();

        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }


    private void displayOrderSummaryFooter() {
        try {
            txtTotalAmount.setText(getString(R.string.Rs) + "" + NumberUtils.formatValue(order.getDerivedPrice()));
            txtBottles.setText((order.getNoOfBottles() > 0 ? " Bottles : " + order.getNoOfBottles() : " "));
            txtCases.setText((order.getNoOfCases() > 0 ? " Cases : " + order.getNoOfCases() : " "));
            txtFrees.setText(((order.getNoOfFreesInCase() > 0 || order.getNoOfFreesInBottles() > 0) ? " Frees : " : "") + (order.getNoOfFreesInCase() > 0 ? "FC/" + order.getNoOfFreesInCase() : "") + (order.getNoOfFreesInBottles() > 0 ? "  FB/" + order.getNoOfFreesInBottles() : ""));

        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }

    private void buildOrderSummaryItems() {

        try {

            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_order_summary);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            orderSummaryListAdapter = new OrderViewListAdapter(getContext(), orderItemList, this);
            recyclerView.setAdapter(orderSummaryListAdapter);

        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while initializing");
            return;
        }
    }


    @Override
    public void onItemDeleted(OrderItem orderItem, int position) {


    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.OrderDetails));

        sfaCommon.displayUserInfo(getActivity(), customer, getString(R.string.OrderDetails));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fabOrderModification: {

                try

                {

                    DialogUtils.showConfirmDialog(getActivity(), "", AbstractApplication.get().getString(R.string.orderdialogmessage), AbstractApplication.get().getString(R.string.Yes) ,  AbstractApplication.get().getString(R.string.NO), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (DialogInterface.BUTTON_POSITIVE == which) {

                                updateOrderDetails();

                                dialog.dismiss();

                            } else {
                                dialog.dismiss();
                            }

                        }
                    });


                } catch (Exception ex) {
                    Logger.Log(OrderViewFragment.class.getName(), ex);
                    return;
                }


            }
        }


    }


    private void updateOrderDetails() {

        try {

            if (isFromDelivery) {

                if (order.getOrderStatusId() != 7) {

                    DeliveryInvoice deliveryInvoice = tableInvoice.getInvoiceByNumber(invoiceNumber);

                    if (deliveryInvoice != null) {

                        Invoice invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);

                        if (invoice != null) {

                            invoice.setOrderStatus(OrderStatus.Cancelled.name());
                            invoice.setOrderStatusId(OrderStatus.Cancelled.getStatus());

                            AuditInfo auditInfo = invoice.getAuditInfo();

                            if (auditInfo != null) {

                                auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                                auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                            } else {
                                auditInfo = new AuditInfo();

                                auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                                auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                            }

                            invoice.setAuditInfo(auditInfo);

                            deliveryInvoice.setInvoiceJSON(gson.toJson(invoice));


                            /*if (deliveryInvoice.getJsonMessageAutoIncId() != 0) {

                                JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(deliveryInvoice.getJsonMessageAutoIncId());

                                jsonMessage.setSyncStatus(0);
                                jsonMessage.setJsonMessage(deliveryInvoice.getInvoiceJSON());
                                jsonMessage.setNotificationId(invoice.getInvoiceId());

                                tableJSONMessage.updateJSONMessage(jsonMessage);

                                tableInvoice.updateInvoiceStatus(deliveryInvoice);
                                tableInvoice.updateInvoiceJSON(deliveryInvoice);

                            } else {*/

                                JSONMessage jsonMessage = new JSONMessage();

                                jsonMessage.setJsonMessage(deliveryInvoice.getInvoiceJSON());
                                jsonMessage.setNotificationId(invoice.getInvoiceId());
                                jsonMessage.setSyncStatus(0);
                                jsonMessage.setNotificationTypeId(2); // For Invoice
                                jsonMessage.setNoOfRequests(0);

                                long json_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                                if (json_auto_inc_id != 0) {

                                    deliveryInvoice.setJsonMessageAutoIncId((int) json_auto_inc_id);

                                    tableInvoice.updateInvoiceStatus(deliveryInvoice);
                                    tableInvoice.updateInvoiceJSON(deliveryInvoice);

                                }

                            //}

                            /*if (AppController.getUser().getUserTypeId() == 7) {

                                deliveryInvoice = tableInvoice.getInvoiceByNumber(invoiceNumber);

                                if (deliveryInvoice != null) {
                                    invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);
                                }

                                if (invoice != null && invoice.getInvoiceItems() != null && invoice.getInvoiceItems().size() > 0) {
                                    orderUtil.postVanInventory(orderUtil.getLoadItemsFromInvoice(invoice.getInvoiceItems()));
                                }
                            }*/


                            DialogUtils.showAlertDialog(getActivity(), "Delivery Cancelled Successfully");

                            if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {

                                backgroundServiceFactory.initiateInvoiceUpdateService(invoice.getInvoiceNo());

                            }

                            DeliveryListFragment deliveryListFragment = new DeliveryListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("customerId", 0);
                            deliveryListFragment.setArguments(bundle);

                            FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, deliveryListFragment);

                        }
                    }
                }

            } else {

                if (order.getOrderStatusId() != 7) {

                    com.inventrax_pepsi.database.pojos.Order localOrder = tableOrder.getOrder(order.getOrderCode());

                    order.setOrderStatus(OrderStatus.Cancelled.name());
                    order.setOrderStatusId(OrderStatus.Cancelled.getStatus());

                    AuditInfo auditInfo = order.getAuditInfo();

                    if (auditInfo != null) {

                        auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                        auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    } else {
                        auditInfo = new AuditInfo();

                        auditInfo.setLastModifiedDate(DateUtils.getDate(DateUtils.YYYYMMDDHHMMSS_DATE_FORMAT));
                        auditInfo.setLastModifiedUserId(AppController.getUser().getUserId());
                    }

                    order.setAuditInfo(auditInfo);

                    localOrder.setOrderJSON(gson.toJson(order));
                    localOrder.setOrderStatus(order.getOrderStatusId());


                    JSONMessage jsonMessage = new JSONMessage();
                    jsonMessage.setJsonMessage(localOrder.getOrderJSON());
                    jsonMessage.setNoOfRequests(0);
                    jsonMessage.setSyncStatus(0);
                    jsonMessage.setNotificationId((int) localOrder.getAutoIncId());
                    jsonMessage.setNotificationTypeId(1); // For Order Notification Type Id is 1

                    long json_message_auto_inc_id = tableJSONMessage.createJSONMessage(jsonMessage);

                    if (json_message_auto_inc_id != 0) {

                        localOrder.setJsonMessageAutoIncId((int)json_message_auto_inc_id);

                        tableOrder.updateOrderStatus(localOrder);

                        tableOrder.updateOrderJSON(localOrder);
                    }


                    /*if (localOrder.getJsonMessageAutoIncId() != 0) {

                        JSONMessage jsonMessage = tableJSONMessage.getJSONMessage(localOrder.getJsonMessageAutoIncId());

                        jsonMessage.setSyncStatus(0);
                        jsonMessage.setJsonMessage(localOrder.getOrderJSON());
                        jsonMessage.setNotificationId(localOrder.getAutoIncId());

                        tableJSONMessage.updateJSONMessage(jsonMessage);

                        tableOrder.updateOrderStatus(localOrder);

                        tableOrder.updateOrderJSON(localOrder);

                    }*/

                    /*if (AppController.getUser().getUserTypeId() == 7) {

                        localOrder = tableOrder.getOrder(order.getOrderCode());

                        if (localOrder != null && order != null && order.getOrderItems() != null && order.getOrderItems().size() > 0) {

                            orderUtil.postVanInventory(orderUtil.getLoadItemsFromOrder(order.getOrderItems()));
                        }

                    }*/

                    DialogUtils.showAlertDialog(getActivity(), "Order Cancelled Successfully");

                    if (NetworkUtils.getConnectivityStatusAsBoolean(getContext())) {
                        backgroundServiceFactory.initiateOrderService(order.getOrderCode());
                    }

                    OrderHistoryListFragment orderHistoryListFragment = new OrderHistoryListFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("customerId", 0);
                    orderHistoryListFragment.setArguments(bundle);

                    FragmentUtils.replaceFragmentWithBackStack(getActivity(), R.id.container_body, orderHistoryListFragment);

                }
            }


        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            return;

        }

    }


    private void loadInvoiceDetails() {

        try

        {

            orderItemList.clear();
            order = new Order();

            OrderItem orderItem = null;

            DeliveryInvoice deliveryInvoice = tableInvoice.getInvoiceByNumber(invoiceNumber);

            if (deliveryInvoice != null) {

                Invoice invoice = gson.fromJson(deliveryInvoice.getInvoiceJSON(), Invoice.class);

                if (invoice != null) {

                    List<InvoiceOrder> invoiceOrderList = invoice.getInvoiceOrders();

                    if (invoiceOrderList != null && invoiceOrderList.size() > 0) {
                        InvoiceOrder invoiceOrder = invoiceOrderList.get(0);

                        order.setOrderCode(invoiceOrder.getOrderCode());
                        order.setOrderId(invoiceOrder.getOrderId());


                    }

                    order.setDerivedPrice(invoice.getNetAmount());
                    order.setCustomerId(invoice.getCustomerId());
                    order.setNoOfCases(invoice.getNoOfCases());
                    order.setNoOfBottles(invoice.getNoOfBottles());
                    order.setOrderStatusId(invoice.getOrderStatusId());
                    order.setOrderStatus(invoice.getOrderStatus());

                    List<InvoiceItem> invoiceItemList = invoice.getInvoiceItems();

                    if (invoiceItemList != null)
                        for (InvoiceItem invoiceItem : invoiceItemList) {

                            orderItem = new OrderItem();

                            orderItem.setItemCode(invoiceItem.getItemCode());
                            orderItem.setItemId(invoiceItem.getItemId());
                            orderItem.setItemBrand(invoiceItem.getItemBrand());
                            orderItem.setItemPack(invoiceItem.getItemPack());
                            orderItem.setUoM(invoiceItem.getUom());
                            orderItem.setUoMId(invoiceItem.getUomId());
                            orderItem.setItemPrice(invoiceItem.getItemPrice());
                            orderItem.setDerivedPrice(invoiceItem.getDerivedPrice());
                            orderItem.setDiscountPrice(invoiceItem.getDiscountPrice());
                            orderItem.setPrice(invoiceItem.getPrice());
                            orderItem.setQuantity(invoiceItem.getQuantity());
                            orderItem.setImageName(invoiceItem.getImageName());

                            orderItemList.add(orderItem);

                        }

                }

            }


        } catch (Exception ex) {
            Logger.Log(OrderViewFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading invoice ");
            return;
        }

    }

    @Override
    public void showOrderSyncStatus(String message, int status) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }
}

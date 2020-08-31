package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 6/14/2016.
 */
public class CustomerTransaction {

    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("TransactionOn")
    private String transactionOn;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("CustomerTransId")
    private int customerTransId;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionOn() {
        return transactionOn;
    }

    public void setTransactionOn(String transactionOn) {
        this.transactionOn = transactionOn;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getCustomerTransId() {
        return customerTransId;
    }

    public void setCustomerTransId(int customerTransId) {
        this.customerTransId = customerTransId;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Invoice {

    @SerializedName("InvoiceOrders")
    private List<InvoiceOrder> invoiceOrders;
    @SerializedName("VAT")
    public double vAT;
    @SerializedName("InvoiceAddress")
    public InvoiceAddress invoiceAddress;
    @SerializedName("CustomerName")
    public String customerName;
    @SerializedName("CustomerCode")
    public String customerCode;
    @SerializedName("AuditInfo")
    public AuditInfo auditInfo;
    @SerializedName("GeneratedDate")
    public String generatedDate;
    @SerializedName("IsDeleted")
    public boolean isDeleted;
    @SerializedName("IsActive")
    public boolean isActive;
    @SerializedName("InvoiceId")
    public int invoiceId;
    @SerializedName("InvoiceNo")
    public String invoiceNo;
    @SerializedName("CustomerAddress")
    public CustomerAddress customerAddress;
    @SerializedName("CustomerId")
    public int customerId;
    @SerializedName("InvoiceItems")
    public List<InvoiceItem> invoiceItems;
    @SerializedName("NetAmount")
    public double netAmount;
    @SerializedName("GrossAmount")
    public double grossAmount;
    @SerializedName("DiscountAmount")
    public double discountAmount;
    @SerializedName("RouteId")
    public int routeId;
    @SerializedName("RouteCode")
    public String routeCode;
    @SerializedName("PaymentStatus")
    public boolean paymentStatus;
    @SerializedName("NoOfCases")
    public double noOfCases;
    @SerializedName("NoOfBottles")
    public double noOfBottles;
    @SerializedName("OrderStatus")
    private String orderStatus;
    @SerializedName("OrderStatusId")
    private int orderStatusId;
    @SerializedName("PaymentMode")
    private int paymentMode;
    @SerializedName("PaymentInfo")
    private PaymentInfo paymentInfo;
    @SerializedName("InvoiceVATs")
    private List<InvoiceVAT> invoiceVATs;


    public List<InvoiceVAT> getInvoiceVATs() {
        return invoiceVATs;
    }

    public void setInvoiceVATs(List<InvoiceVAT> invoiceVATs) {
        this.invoiceVATs = invoiceVATs;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public List<InvoiceOrder> getInvoiceOrders() {
        return invoiceOrders;
    }

    public void setInvoiceOrders(List<InvoiceOrder> invoiceOrders) {
        this.invoiceOrders = invoiceOrders;
    }

    public double getvAT() {
        return vAT;
    }

    public void setvAT(double vAT) {
        this.vAT = vAT;
    }

    public InvoiceAddress getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(InvoiceAddress invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getNoOfCases() {
        return noOfCases;
    }

    public void setNoOfCases(double noOfCases) {
        this.noOfCases = noOfCases;
    }

    public double getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(double noOfBottles) {
        this.noOfBottles = noOfBottles;
    }
}

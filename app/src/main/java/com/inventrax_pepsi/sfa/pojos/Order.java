package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Order {

    @SerializedName("AutoSyncId")
    private int autoSyncId;
    @SerializedName("CustomerGroupId")
    private int customerGroupId;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("DerivedPrice")
    private double derivedPrice;
    @SerializedName("DiscountPrice")
    private double discountPrice;
    @SerializedName("PaymentStatus")
    private boolean paymentStatus;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("OrderCode")
    private String orderCode;
    @SerializedName("OrderStatusId")
    private int orderStatusId;
    @SerializedName("OrderStatus")
    private String orderStatus;
    @SerializedName("OrderTypeId")
    private int orderTypeId;
    @SerializedName("OrderType")
    private String orderType;
    @SerializedName("OrderPrice")
    private double orderPrice;
    @SerializedName("OrderQuantity")
    private double orderQuantity;
    @SerializedName("OrderItems")
    private List<OrderItem> orderItems;
    @SerializedName("CreatedOn")
    private String createdOn;
    @SerializedName("NoOfSKU")
    private int noOfSku;
    @SerializedName("NoOfCases")
    private double noOfCases;
    @SerializedName("NoOfBottles")
    private double noOfBottles;
    @SerializedName("NoOfFreesInCases")
    private double noOfFreesInCase;
    @SerializedName("NoOfFreesInBottles")
    private double noOfFreesInBottles;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("PaymentInfo")
    private PaymentInfo paymentInfo;


    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public int getNoOfSku() {
        return noOfSku;
    }

    public void setNoOfSku(int noOfSku) {
        this.noOfSku = noOfSku;
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

    public double getNoOfFreesInCase() {
        return noOfFreesInCase;
    }

    public void setNoOfFreesInCase(double noOfFreesInCase) {
        this.noOfFreesInCase = noOfFreesInCase;
    }

    public double getNoOfFreesInBottles() {
        return noOfFreesInBottles;
    }

    public void setNoOfFreesInBottles(double noOfFreesInBottles) {
        this.noOfFreesInBottles = noOfFreesInBottles;
    }

    public int getAutoSyncId() {
        return autoSyncId;
    }

    public void setAutoSyncId(int autoSyncId) {
        this.autoSyncId = autoSyncId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(int customerGroupId) {
        this.customerGroupId = customerGroupId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public double getDerivedPrice() {
        return derivedPrice;
    }

    public void setDerivedPrice(double derivedPrice) {
        this.derivedPrice = derivedPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public double getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}

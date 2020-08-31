package com.inventrax_pepsi.database.pojos;

/**
 * Author   : Naresh P.
 * Date		: 09/03/2016
 * Purpose	: Customer
 */

public class Customer {

    private int autoIncId, customerId, customerTypeId, routeId, orderCap,syncStatus,isScheduledOutlet,isNewCustomer,jsonMessageAutoIncId;
    private String customerCode, customerName, customerType, completeJSON, priceJSON, discountJSON, couponJSON, routeNo, creditLimitJSON;

    public int getJsonMessageAutoIncId() {
        return jsonMessageAutoIncId;
    }

    public void setJsonMessageAutoIncId(int jsonMessageAutoIncId) {
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
    }

    public int getIsNewCustomer() {
        return isNewCustomer;
    }

    public void setIsNewCustomer(int isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }

    public int getIsScheduledOutlet() {
        return isScheduledOutlet;
    }

    public void setIsScheduledOutlet(int isScheduledOutlet) {
        this.isScheduledOutlet = isScheduledOutlet;
    }

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getOrderCap() {
        return orderCap;
    }

    public void setOrderCap(int orderCap) {
        this.orderCap = orderCap;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCompleteJSON() {
        return completeJSON;
    }

    public void setCompleteJSON(String completeJSON) {
        this.completeJSON = completeJSON;
    }

    public String getPriceJSON() {
        return priceJSON;
    }

    public void setPriceJSON(String priceJSON) {
        this.priceJSON = priceJSON;
    }

    public String getDiscountJSON() {
        return discountJSON;
    }

    public void setDiscountJSON(String discountJSON) {
        this.discountJSON = discountJSON;
    }

    public String getCouponJSON() {
        return couponJSON;
    }

    public void setCouponJSON(String couponJSON) {
        this.couponJSON = couponJSON;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getCreditLimitJSON() {
        return creditLimitJSON;
    }

    public void setCreditLimitJSON(String creditLimitJSON) {
        this.creditLimitJSON = creditLimitJSON;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/31/2016.
 */
public class CustSKUOrder {

    @SerializedName("CustomerId")
    public int customerId;
    @SerializedName("CustomerCode")
    public String customerCode;
    @SerializedName("CustomerName")
    public String customerName;
    @SerializedName("RouteId")
    public int routeId;
    @SerializedName("RouteCode")
    public String routeCode;
    @SerializedName("OrderHistory")
    public List<OrderHistory> orderHistory;
    @SerializedName("BrandHistory")
    public List<BrandHistory> brandHistory;


    public List<BrandHistory> getBrandHistory() {
        return brandHistory;
    }

    public void setBrandHistory(List<BrandHistory> brandHistory) {
        this.brandHistory = brandHistory;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }
}

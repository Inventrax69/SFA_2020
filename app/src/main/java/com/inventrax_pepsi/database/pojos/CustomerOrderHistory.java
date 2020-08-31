package com.inventrax_pepsi.database.pojos;

/**
 * Created by android on 3/31/2016.
 */
public class CustomerOrderHistory {

    private int customerId,routeId;
    private String customerCode,routeCode,customerName,brandPackJSON,brandJSON;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBrandPackJSON() {
        return brandPackJSON;
    }

    public void setBrandPackJSON(String brandPackJSON) {
        this.brandPackJSON = brandPackJSON;
    }

    public String getBrandJSON() {
        return brandJSON;
    }

    public void setBrandJSON(String brandJSON) {
        this.brandJSON = brandJSON;
    }
}

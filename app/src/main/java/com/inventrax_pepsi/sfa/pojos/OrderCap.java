package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class OrderCap {

    @SerializedName("CustomerOrderCapId")
    private int customerOrderCapId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("CapValue")
    private double capValue;

    public int getCustomerOrderCapId() {
        return customerOrderCapId;
    }

    public void setCustomerOrderCapId(int customerOrderCapId) {
        this.customerOrderCapId = customerOrderCapId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
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

    public double getCapValue() {
        return capValue;
    }

    public void setCapValue(double capValue) {
        this.capValue = capValue;
    }
}

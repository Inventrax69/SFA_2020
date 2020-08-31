package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naresh on 04-Apr-16.
 */
public class CustomerReturn {

    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("NoOfCases")
    private int noOfCases;
    @SerializedName("NoOfShells")
    private int noOfShells;
    @SerializedName("NoOfBottles")
    private int noOfBottles;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("IsEmpties")
    private boolean isEmpties;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("Crowns")
    private int crowns;
    @SerializedName("CrownsValue")
    private double crownValue;



    public int getCrowns() {
        return crowns;
    }

    public void setCrowns(int crowns) {
        this.crowns = crowns;
    }

    public double getCrownValue() {
        return crownValue;
    }

    public void setCrownValue(double crownValue) {
        this.crownValue = crownValue;
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

    public int getNoOfCases() {
        return noOfCases;
    }

    public void setNoOfCases(int noOfCases) {
        this.noOfCases = noOfCases;
    }

    public int getNoOfShells() {
        return noOfShells;
    }

    public void setNoOfShells(int noOfShells) {
        this.noOfShells = noOfShells;
    }

    public int getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(int noOfBottles) {
        this.noOfBottles = noOfBottles;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public boolean isEmpties() {
        return isEmpties;
    }

    public void setIsEmpties(boolean isEmpties) {
        this.isEmpties = isEmpties;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}

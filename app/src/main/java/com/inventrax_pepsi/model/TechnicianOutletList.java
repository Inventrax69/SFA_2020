package com.inventrax_pepsi.model;

import java.io.Serializable;

/**
 * Created by nareshp on 05/01/2016.
 */
public class TechnicianOutletList implements Serializable {

    private String customerName;
    private String customerCode;
    private String customerID;
    private String outletName;


    private int assetCount;
    private String[] jobOrders;


    public TechnicianOutletList() {

    }



    public TechnicianOutletList(String customerName, String customerCode, String customerID, String outletName, int assetCount, String[] jobOrders) {
        this.customerName = customerName;
        this.customerCode = customerCode;
        this.customerID = customerID;
        this.outletName = outletName;
        this.assetCount = assetCount;
        this.jobOrders = jobOrders;
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

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public int getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }

    public String[] getJobOrders() {
        return jobOrders;
    }

    public void setJobOrders(String[] jobOrders) {
        this.jobOrders = jobOrders;
    }

}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naresh on 26-Mar-16.
 */
public class InvoiceOrder {

    @SerializedName("InvoiceOrderId")
    private int invoiceOrderId;
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("OrderCode")
    private String orderCode;
    @SerializedName("CreatedOn")
    private String createdOn;

    public int getInvoiceOrderId() {
        return invoiceOrderId;
    }

    public void setInvoiceOrderId(int invoiceOrderId) {
        this.invoiceOrderId = invoiceOrderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}

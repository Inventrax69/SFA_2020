package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/31/2016.
 */
public class OrderHistory {

    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("QTY")
    private double qTY;
    @SerializedName("Date")
    private String date;
    @SerializedName("OrderCode")
    private String orderCode;
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("RankId")
    private int rankId;


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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
    }

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
    }

    public double getqTY() {
        return qTY;
    }

    public void setqTY(double qTY) {
        this.qTY = qTY;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }
}

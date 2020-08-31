package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by padmaja.b on 14/08/2020.
 */
public class ActiveStock {

    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("UomCode")
    private String UomCode;
    @SerializedName("UomId")
    private int UomId;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("StoreId")
    private int storeId;
    @SerializedName("StoreCode")
    private String storeCode;
    @SerializedName("Store")
    private String Store;
    @SerializedName("ItemUoMId")
    private int ItemUoMId;
    @SerializedName("MRP")
    private double MRP;
    @SerializedName("RequiredStock")
    private double RequiredStock;

    private double fullBottle;


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

    public String getUomCode() {
        return UomCode;
    }

    public void setUomCode(String uomCode) {
        UomCode = uomCode;
    }

    public int getUomId() {
        return UomId;
    }

    public void setUomId(int uomId) {
        UomId = uomId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }

    public int getItemUoMId() {
        return ItemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        ItemUoMId = itemUoMId;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getRequiredStock() {
        return RequiredStock;
    }

    public void setRequiredStock(double requiredStock) {
        RequiredStock = requiredStock;
    }

    public double getFullBottle() {
        return fullBottle;
    }

    public void setFullBottle(double fullBottle) {
        this.fullBottle = fullBottle;
    }
}

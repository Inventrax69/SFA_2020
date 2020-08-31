package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class ItemTransaction {
    private int autoIncId,itemId,itemUoMId;
    private String itemCode,itemUoM;
    private int noOfBottles,itemUoMQuantity,CR_DR;
    private double quantity;


    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemUoM() {
        return itemUoM;
    }

    public void setItemUoM(String itemUoM) {
        this.itemUoM = itemUoM;
    }

    public int getNoOfBottles() {
        return noOfBottles;
    }

    public void setNoOfBottles(int noOfBottles) {
        this.noOfBottles = noOfBottles;
    }

    public int getItemUoMQuantity() {
        return itemUoMQuantity;
    }

    public void setItemUoMQuantity(int itemUoMQuantity) {
        this.itemUoMQuantity = itemUoMQuantity;
    }

    public int getCR_DR() {
        return CR_DR;
    }

    public void setCR_DR(int CR_DR) {
        this.CR_DR = CR_DR;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}

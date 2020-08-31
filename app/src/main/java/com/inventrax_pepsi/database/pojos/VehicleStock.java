package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 27-Mar-16.
 */
public class VehicleStock {

    private int autoIncId,itemId,transactionType,itemTypeId;
    private String itemCode,itemName,imageName;
    private double caseQuantity,bottleQuantity,lineMRP;


    public double getLineMRP() {
        return lineMRP;
    }

    public void setLineMRP(double lineMRP) {
        this.lineMRP = lineMRP;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public double getCaseQuantity() {
        return caseQuantity;
    }

    public void setCaseQuantity(double caseQuantity) {
        this.caseQuantity = caseQuantity;
    }

    public double getBottleQuantity() {
        return bottleQuantity;
    }

    public void setBottleQuantity(double bottleQuantity) {
        this.bottleQuantity = bottleQuantity;
    }

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

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
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

}

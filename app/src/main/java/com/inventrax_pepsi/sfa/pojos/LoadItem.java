package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class LoadItem {

    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("UnitPrice")
    private double unitPrice;
    @SerializedName("IsCustomerPrice")
    private boolean isCustomerPrice;
    @SerializedName("IsTrade")
    private boolean isTrade;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("LoadItemId")
    private int loadItemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("Uom")
    private String uom;
    @SerializedName("Price")
    private double price;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("ItemTypeId")
    private int itemTypeId;
    @SerializedName("ItemTypeCode")
    private String itemTypeCode;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("LineMRP")
    private double lineMRP;
    @SerializedName("FBquantity")
    private double FBquantitiy;


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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isCustomerPrice() {
        return isCustomerPrice;
    }

    public void setIsCustomerPrice(boolean isCustomerPrice) {
        this.isCustomerPrice = isCustomerPrice;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public void setIsTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLoadItemId() {
        return loadItemId;
    }

    public void setLoadItemId(int loadItemId) {
        this.loadItemId = loadItemId;
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

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public double getFBquantitiy() {
        return FBquantitiy;
    }

    public void setFBquantitiy(double FBquantitiy) {
        this.FBquantitiy = FBquantitiy;
    }
}

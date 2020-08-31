package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/15/2016.
 */
public class OrderItemScheme {

    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("OrderItemSchemeId")
    private int orderItemSchemeId;
    @SerializedName("SchemeCode")
    private String schemeCode;
    @SerializedName("SchemeId")
    private int schemeId;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("Price")
    private double price;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("Value")
    private double value;
    @SerializedName("ItemBrand")
    private String itemBrand;
    @SerializedName("ItemPack")
    private String itemPack;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("ItemType")
    private String itemType;
    @SerializedName("ItemBrandId")
    private  int itemBrandId;
    @SerializedName("ItemPackId")
    private  int itemPackId;
    @SerializedName("ItemPriceId")
    private int itemPriceId;
    @SerializedName("ItemPrice")
    private double itemPrice;
    @SerializedName("ItemTypeCode")
    private String itemTypeCode;
    @SerializedName("ItemTypeId")
    private int itemTypeId;
    @SerializedName("MRP")
    private double MRP;


    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    @SerializedName("IsMixedScheme")
    private  boolean isMixedScheme;

    public boolean isMixedScheme() {
        return isMixedScheme;
    }

    public void setMixedScheme(boolean mixedScheme) {
        isMixedScheme = mixedScheme;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }


    public int getItemBrandId() {
        return itemBrandId;
    }

    public void setItemBrandId(int itemBrandId) {
        this.itemBrandId = itemBrandId;
    }

    public int getItemPackId() {
        return itemPackId;
    }

    public void setItemPackId(int itemPackId) {
        this.itemPackId = itemPackId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemPack() {
        return itemPack;
    }

    public void setItemPack(String itemPack) {
        this.itemPack = itemPack;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
    }

    public int getOrderItemSchemeId() {
        return orderItemSchemeId;
    }

    public void setOrderItemSchemeId(int orderItemSchemeId) {
        this.orderItemSchemeId = orderItemSchemeId;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public int getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(int schemeId) {
        this.schemeId = schemeId;
    }

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

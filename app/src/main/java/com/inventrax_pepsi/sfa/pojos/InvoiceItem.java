package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class InvoiceItem {

    @SerializedName("ItemBrand")
    private String itemBrand;
    @SerializedName("ItemPack")
    private String itemPack;
    @SerializedName("ItemType")
    private String itemType;
    @SerializedName("ItemTypeId")
    private int itemTypeId;
    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("ItemPrice")
    private double itemPrice;
    @SerializedName("UoMQty")
    private double uoMQty;
    @SerializedName("IsDiscountItem")
    private boolean isDiscountItem;
    @SerializedName("DiscountPrice")
    private double discountPrice;
    @SerializedName("DerivedPrice")
    private double derivedPrice;
    @SerializedName("InvoiceItemId")
    private int invoiceItemId;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("UomId")
    private int uomId;
    @SerializedName("Uom")
    private String uom;
    @SerializedName("Price")
    private double price;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("MRP")
    private double MRP;
    @SerializedName("spotDiscountPrice")
    private double spotDiscountPrice;


    public double getSpotDiscountPrice() {
        return spotDiscountPrice;
    }

    public void setSpotDiscountPrice(double spotDiscountPrice) {
        this.spotDiscountPrice = spotDiscountPrice;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    @SerializedName("IsSchemeItem")
    private boolean isSchemeItem;

    public void setDiscountItem(boolean discountItem) {
        isDiscountItem = discountItem;
    }

    public boolean isSchemeItem() {
        return isSchemeItem;
    }

    public void setSchemeItem(boolean schemeItem) {
        isSchemeItem = schemeItem;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getUoMQty() {
        return uoMQty;
    }

    public void setUoMQty(double uoMQty) {
        this.uoMQty = uoMQty;
    }

    public boolean isDiscountItem() {
        return isDiscountItem;
    }

    public void setIsDiscountItem(boolean isDiscountItem) {
        this.isDiscountItem = isDiscountItem;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getDerivedPrice() {
        return derivedPrice;
    }

    public void setDerivedPrice(double derivedPrice) {
        this.derivedPrice = derivedPrice;
    }

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
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
}

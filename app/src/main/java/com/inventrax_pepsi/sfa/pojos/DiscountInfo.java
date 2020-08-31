package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class DiscountInfo {

    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("DiscountValue")
    private int discountValue;
    @SerializedName("IsSpot")
    private boolean isSpot;
    @SerializedName("DiscountTypeId")
    private int discountTypeId;
    @SerializedName("DiscountType")
    private String discountType;
    @SerializedName("DiscountCode")
    private String discountCode;
    @SerializedName("IsDiscountItem")
    private boolean isDiscountItem;
    @SerializedName("DiscountAppliedPriceTypeId")
    private int discountAppliedPriceTypeId;
    @SerializedName("UomId")
    private int uomId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("OrderItemDiscountId")
    private int orderItemDiscountId;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("OrderItemId")
    private int orderItemId;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("DiscountId")
    private int discountId;
    @SerializedName("DiscountPrice")
    private double discountPrice;
    @SerializedName("ItemId")
    private int itemId;

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isSpot() {
        return isSpot;
    }

    public void setIsSpot(boolean isSpot) {
        this.isSpot = isSpot;
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public boolean isDiscountItem() {
        return isDiscountItem;
    }

    public void setIsDiscountItem(boolean isDiscountItem) {
        this.isDiscountItem = isDiscountItem;
    }

    public int getDiscountAppliedPriceTypeId() {
        return discountAppliedPriceTypeId;
    }

    public void setDiscountAppliedPriceTypeId(int discountAppliedPriceTypeId) {
        this.discountAppliedPriceTypeId = discountAppliedPriceTypeId;
    }

    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrderItemDiscountId() {
        return orderItemDiscountId;
    }

    public void setOrderItemDiscountId(int orderItemDiscountId) {
        this.orderItemDiscountId = orderItemDiscountId;
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

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}

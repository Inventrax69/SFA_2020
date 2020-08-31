package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class DiscountItem {

    @SerializedName("DiscountId")
    private int discountId;
    @SerializedName("DiscountApppliedPriceTypeId")
    private int discountApppliedPriceTypeId;
    @SerializedName("DiscountItemId")
    private int discountItemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemUomId")
    private int itemUomId;
    @SerializedName("ItemUom")
    private String itemUom;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("IsDiscountItem")
    private boolean isDiscountItem;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getDiscountApppliedPriceTypeId() {
        return discountApppliedPriceTypeId;
    }

    public void setDiscountApppliedPriceTypeId(int discountApppliedPriceTypeId) {
        this.discountApppliedPriceTypeId = discountApppliedPriceTypeId;
    }

    public int getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(int discountItemId) {
        this.discountItemId = discountItemId;
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

    public int getItemUomId() {
        return itemUomId;
    }

    public void setItemUomId(int itemUomId) {
        this.itemUomId = itemUomId;
    }

    public String getItemUom() {
        return itemUom;
    }

    public void setItemUom(String itemUom) {
        this.itemUom = itemUom;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isDiscountItem() {
        return isDiscountItem;
    }

    public void setIsDiscountItem(boolean isDiscountItem) {
        this.isDiscountItem = isDiscountItem;
    }
}

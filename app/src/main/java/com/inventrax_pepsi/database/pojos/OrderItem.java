package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class OrderItem {

    private int autoIncId,itemId,itemUOMId,discountItemUomId,discountItemId,itemUoMQuantity;
    private String orderCode,itemCode,itemUOM,discountItemCode,discountItemUOM;
    private double quantity,price,discountPrice,derivedPrice;

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

    public int getItemUOMId() {
        return itemUOMId;
    }

    public void setItemUOMId(int itemUOMId) {
        this.itemUOMId = itemUOMId;
    }

    public int getDiscountItemUomId() {
        return discountItemUomId;
    }

    public void setDiscountItemUomId(int discountItemUomId) {
        this.discountItemUomId = discountItemUomId;
    }

    public int getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(int discountItemId) {
        this.discountItemId = discountItemId;
    }

    public int getItemUoMQuantity() {
        return itemUoMQuantity;
    }

    public void setItemUoMQuantity(int itemUoMQuantity) {
        this.itemUoMQuantity = itemUoMQuantity;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemUOM() {
        return itemUOM;
    }

    public void setItemUOM(String itemUOM) {
        this.itemUOM = itemUOM;
    }

    public String getDiscountItemCode() {
        return discountItemCode;
    }

    public void setDiscountItemCode(String discountItemCode) {
        this.discountItemCode = discountItemCode;
    }

    public String getDiscountItemUOM() {
        return discountItemUOM;
    }

    public void setDiscountItemUOM(String discountItemUOM) {
        this.discountItemUOM = discountItemUOM;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}

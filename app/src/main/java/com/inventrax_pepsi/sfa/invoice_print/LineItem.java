package com.inventrax_pepsi.sfa.invoice_print;

/**
 * Created by android on 4/2/2016.
 */
public class LineItem {

    private String item;
    private String qty;
    private float amt;
    private float itemPrice;
    private double discountPrice;
    private String discountType;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountType() { return discountType; }

    public void setDiscountType(String discountType) { this.discountType = discountType; }

}

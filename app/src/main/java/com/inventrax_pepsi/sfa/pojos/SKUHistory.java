package com.inventrax_pepsi.sfa.pojos;

/**
 * Created by android on 3/31/2016.
 */
public class SKUHistory {

    private String date;
    private int quantity;

    public SKUHistory() {
    }

    public SKUHistory(String date, int quantity) {
        this.date = date;
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

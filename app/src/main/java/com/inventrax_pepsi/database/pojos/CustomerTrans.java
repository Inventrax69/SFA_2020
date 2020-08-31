package com.inventrax_pepsi.database.pojos;

/**
 * Created by android on 6/15/2016.
 */
public class CustomerTrans {

    private int autoIncId,jsonMessageAutoIncId,customerId;
    private String json;
    private double amount;

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getJsonMessageAutoIncId() {
        return jsonMessageAutoIncId;
    }

    public void setJsonMessageAutoIncId(int jsonMessageAutoIncId) {
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

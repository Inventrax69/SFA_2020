package com.inventrax_pepsi.database.pojos;

/**
 * Created by android on 4/25/2016.
 */
public class ReadySaleInvoice {

    private int autoIncId,jsonMessageAutoIncId;
    private String invoiceNumber,json;


    public ReadySaleInvoice(int autoIncId, int jsonMessageAutoIncId, String invoiceNumber, String json) {
        this.autoIncId = autoIncId;
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
        this.invoiceNumber = invoiceNumber;
        this.json = json;
    }


    public ReadySaleInvoice() {
    }

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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/13/2016.
 */
public class InvoiceVAT {

    @SerializedName("InvoiceVatId")
    private int invoiceVatId;
    @SerializedName("InvoiceId")
    private int invoiceId;
    @SerializedName("InvoiceNo")
    private String invoiceNo;
    @SerializedName("VAT")
    private double vAT;
    @SerializedName("VATAmount")
    private double vATAmount;
    @SerializedName("TaxationId")
    private int taxationId;

    public int getInvoiceVatId() {
        return invoiceVatId;
    }

    public void setInvoiceVatId(int invoiceVatId) {
        this.invoiceVatId = invoiceVatId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getvAT() {
        return vAT;
    }

    public void setvAT(double vAT) {
        this.vAT = vAT;
    }

    public double getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(double vATAmount) {
        this.vATAmount = vATAmount;
    }

    public int getTaxationId() {
        return taxationId;
    }

    public void setTaxationId(int taxationId) {
        this.taxationId = taxationId;
    }
}

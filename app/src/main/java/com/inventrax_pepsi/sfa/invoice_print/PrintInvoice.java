package com.inventrax_pepsi.sfa.invoice_print;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by android on 4/2/2016.
 */
public class PrintInvoice {

    private InvoiceHeader header;
    private OutletInfo outletInfo;
    private String invoiceNumber;
    private String invoiceDate;
    private ArrayList<LineItem> lineItems;
    private ArrayList<TaxBreakup> taxBreakups;
    private String subTotal;
    private static final String horizontalLine = "------------------------";
    public static final String PROFORMA_INVOICE = " PROFORMA INV/DELN CHLN ";
    public static final String RETAIL_INVOICE = "     RETAIL INVOICE     ";
    private String paymentMode;
    private float paymentAmount;
    private String paymentDate;
    public static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public InvoiceHeader getHeader() {
        return header;
    }

    public void setHeader(InvoiceHeader header) {
        this.header = header;
    }

    public OutletInfo getOutletInfo() {
        return outletInfo;
    }

    public void setOutletInfo(OutletInfo outletInfo) {
        this.outletInfo = outletInfo;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public ArrayList<TaxBreakup> getTaxBreakups() {
        return taxBreakups;
    }

    public void setTaxBreakups(ArrayList<TaxBreakup> taxBreakups) {
        this.taxBreakups = taxBreakups;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public static String getHorizontalLine() {
        return horizontalLine;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}

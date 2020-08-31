package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/30/2016.
 */
public class PaymentInfo {


    @SerializedName("PaymentInfoId")
    private int paymentInfoId;
    @SerializedName("PaymentStatus")
    private String paymentStatus;
    @SerializedName("BankId")
    private int bankId;
    @SerializedName("BankName")
    private String bankName;
    @SerializedName("ChequeNo")
    private String chequeNo;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("PaymentTypeId")
    private int paymentTypeId;
    @SerializedName("PaymentMode")
    private String paymentMode;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("Mode")
    private int mode;
    @SerializedName("InvoiceId")
    private int invoiceId;
    @SerializedName("OrderId")
    private int orderId;
    @SerializedName("OrderCode")
    private String orderCode;


    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getPaymentInfoId() {
        return paymentInfoId;
    }

    public void setPaymentInfoId(int paymentInfoId) {
        this.paymentInfoId = paymentInfoId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}

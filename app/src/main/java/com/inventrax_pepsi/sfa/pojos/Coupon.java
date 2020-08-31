package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Coupon {

    @SerializedName("CouponId")
    private int couponId;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CouponCode")
    private String couponCode;
    @SerializedName("Validity")
    private Validity  validity;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("AuditInfo")
    private  AuditInfo auditInfo;
    @SerializedName("Price")
    private double price;
    @SerializedName("CouponStatus")
    private String couponStatus;
    @SerializedName("CouponStatusId")
    private int couponStatusId;
    @SerializedName("RedeemedAmount")
    private double redeemedAmount;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("CouponApprovals")
    private List<CouponApproval> couponApprovals;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public int getCouponStatusId() {
        return couponStatusId;
    }

    public void setCouponStatusId(int couponStatusId) {
        this.couponStatusId = couponStatusId;
    }

    public double getRedeemedAmount() {
        return redeemedAmount;
    }

    public void setRedeemedAmount(double redeemedAmount) {
        this.redeemedAmount = redeemedAmount;
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

    public List<CouponApproval> getCouponApprovals() {
        return couponApprovals;
    }

    public void setCouponApprovals(List<CouponApproval> couponApprovals) {
        this.couponApprovals = couponApprovals;
    }
}

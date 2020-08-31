package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class CouponApproval {

    @SerializedName("CouponApprovalId")
    private int couponApprovalId;
    @SerializedName("ApprovedUser")
    private String approvedUser;
    @SerializedName("ApprovedUserId")
    private int approvedUserId;
    @SerializedName("ApprovedOn")
    private String approvedOn;
    @SerializedName("CouponCode")
    private String couponCode;
    @SerializedName("CouponId")
    private int couponId;

    public int getCouponApprovalId() {
        return couponApprovalId;
    }

    public void setCouponApprovalId(int couponApprovalId) {
        this.couponApprovalId = couponApprovalId;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public int getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(int approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
}

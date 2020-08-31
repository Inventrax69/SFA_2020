package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Discount {

    @SerializedName("DiscountMaps")
    private List<DiscountMap> discountMaps;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("DiscountType")
    private String discountType;
    @SerializedName("DiscountId")
    private int discountId;
    @SerializedName("DiscountTypeId")
    private int discountTypeId;
    @SerializedName("DiscountName")
    private String discountName;
    @SerializedName("DiscountCode")
    private String discountCode;
    @SerializedName("Description")
    private String description;
    @SerializedName("Validity")
    private Validity validity;
    @SerializedName("DiscountItems")
    private List<DiscountItem> discountItems;
    @SerializedName("DiscountOn")
    private int discountOn;
    @SerializedName("IsSpot")
    private boolean isSpot;
    @SerializedName("Value")
    private double value;

    public List<DiscountMap> getDiscountMaps() {
        return discountMaps;
    }

    public void setDiscountMaps(List<DiscountMap> discountMaps) {
        this.discountMaps = discountMaps;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public List<DiscountItem> getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(List<DiscountItem> discountItems) {
        this.discountItems = discountItems;
    }

    public int getDiscountOn() {
        return discountOn;
    }

    public void setDiscountOn(int discountOn) {
        this.discountOn = discountOn;
    }

    public boolean isSpot() {
        return isSpot;
    }

    public void setIsSpot(boolean isSpot) {
        this.isSpot = isSpot;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

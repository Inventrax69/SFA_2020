package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 5/12/2016.
 */
public class CustomerAuditInfo {

    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("IsSeasonal")
    private boolean isSeasonal;
    @SerializedName("NoOfGlasses")
    private int noOfGlasses;
    @SerializedName("IsPetSelling")
    private boolean isPetSelling;
    @SerializedName("VolPerDayInBottles")
    private double volPerDayInBottles;
    @SerializedName("VolPerYearInBottles")
    private double volPerYearInBottles;
    @SerializedName("CcxVolPerDayInBottles")
    private double ccxVolPerDayInBottles;
    @SerializedName("CcxVolPerYearInBottles")
    private double ccxVolPerYearInBottles;
    @SerializedName("HasPCIAsset")
    private boolean hasPCIAsset;
    @SerializedName("HasCCXAsset")
    private boolean hasCCXAsset;
    @SerializedName("AccountType")
    private int accountType;
    @SerializedName("CcxAssetVolume")
    private String ccxAssetVolume;
    @SerializedName("PciAssetVolume")
    private String pciAssetVolume;
    @SerializedName("AssetCaptureHistories")
    private List<AssetCaptureHistory> assetCaptureHistories;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isSeasonal() {
        return isSeasonal;
    }

    public void setSeasonal(boolean seasonal) {
        isSeasonal = seasonal;
    }

    public int getNoOfGlasses() {
        return noOfGlasses;
    }

    public void setNoOfGlasses(int noOfGlasses) {
        this.noOfGlasses = noOfGlasses;
    }

    public boolean isPetSelling() {
        return isPetSelling;
    }

    public void setPetSelling(boolean petSelling) {
        isPetSelling = petSelling;
    }

    public double getVolPerDayInBottles() {
        return volPerDayInBottles;
    }

    public void setVolPerDayInBottles(double volPerDayInBottles) {
        this.volPerDayInBottles = volPerDayInBottles;
    }

    public double getVolPerYearInBottles() {
        return volPerYearInBottles;
    }

    public void setVolPerYearInBottles(double volPerYearInBottles) {
        this.volPerYearInBottles = volPerYearInBottles;
    }

    public double getCcxVolPerDayInBottles() {
        return ccxVolPerDayInBottles;
    }

    public void setCcxVolPerDayInBottles(double ccxVolPerDayInBottles) {
        this.ccxVolPerDayInBottles = ccxVolPerDayInBottles;
    }

    public double getCcxVolPerYearInBottles() {
        return ccxVolPerYearInBottles;
    }

    public void setCcxVolPerYearInBottles(double ccxVolPerYearInBottles) {
        this.ccxVolPerYearInBottles = ccxVolPerYearInBottles;
    }

    public boolean isHasPCIAsset() {
        return hasPCIAsset;
    }

    public void setHasPCIAsset(boolean hasPCIAsset) {
        this.hasPCIAsset = hasPCIAsset;
    }

    public boolean isHasCCXAsset() {
        return hasCCXAsset;
    }

    public void setHasCCXAsset(boolean hasCCXAsset) {
        this.hasCCXAsset = hasCCXAsset;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getCcxAssetVolume() {
        return ccxAssetVolume;
    }

    public void setCcxAssetVolume(String ccxAssetVolume) {
        this.ccxAssetVolume = ccxAssetVolume;
    }

    public String getPciAssetVolume() {
        return pciAssetVolume;
    }

    public void setPciAssetVolume(String pciAssetVolume) {
        this.pciAssetVolume = pciAssetVolume;
    }

    public List<AssetCaptureHistory> getAssetCaptureHistories() {
        return assetCaptureHistories;
    }

    public void setAssetCaptureHistories(List<AssetCaptureHistory> assetCaptureHistories) {
        this.assetCaptureHistories = assetCaptureHistories;
    }
}

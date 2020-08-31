package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/18/2016.
 */
public class CustomerAsset {

    @SerializedName("CustomerAssetId")
    private int customerAssetId;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("AssetId")
    private int assetId;
    @SerializedName("AgreementId")
    private int agreementId;
    @SerializedName("QrCode")
    private String qrCode;
    @SerializedName("ImageSourcer")
    private String imageSourcer;
    @SerializedName("InstalledBy")
    private int installedBy;
    @SerializedName("ApprovedBy")
    private int approvedBy;
    @SerializedName("InstalledOn")
    private String installedOn;
    @SerializedName("VerifiedBy")
    private int verifiedBy;
    @SerializedName("HasNightGuard")
    private boolean hasNightGuard;
    @SerializedName("PulloutStatus")
    private boolean pulloutStatus;
    @SerializedName("InstalledUser")
    private String installedUser;
    @SerializedName("Model")
    private String model;
    @SerializedName("Make")
    private String make;
    @SerializedName("Volume")
    private String volume;
    @SerializedName("SerialNo")
    private String serialNo;
    @SerializedName("OEMNo")
    private String oEMNo;
    @SerializedName("Size")
    private String size;
    @SerializedName("AssetType")
    private String assetType;
    @SerializedName("LastAuditLatitude")
    private String latitude;
    @SerializedName("LastAuditLongitude")
    private String longitude;
    @SerializedName("CustomerLastAuditDate")
    private String customerLastAuditDate;
    @SerializedName("LastAuditDate")
    private String  LastAuditDate;

    public String getLastAuditDate() {
        return LastAuditDate;
    }

    public void setLastAuditDate(String lastAuditDate) {
        LastAuditDate = lastAuditDate;
    }

    public String getCustomerLastAuditDate() {
        return customerLastAuditDate;
    }

    public void setCustomerLastAuditDate(String customerLastAuditDate) {
        this.customerLastAuditDate = customerLastAuditDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getCustomerAssetId() {
        return customerAssetId;
    }

    public void setCustomerAssetId(int customerAssetId) {
        this.customerAssetId = customerAssetId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getImageSourcer() {
        return imageSourcer;
    }

    public void setImageSourcer(String imageSourcer) {
        this.imageSourcer = imageSourcer;
    }

    public int getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(int installedBy) {
        this.installedBy = installedBy;
    }

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(String installedOn) {
        this.installedOn = installedOn;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public boolean isHasNightGuard() {
        return hasNightGuard;
    }

    public void setHasNightGuard(boolean hasNightGuard) {
        this.hasNightGuard = hasNightGuard;
    }

    public boolean isPulloutStatus() {
        return pulloutStatus;
    }

    public void setPulloutStatus(boolean pulloutStatus) {
        this.pulloutStatus = pulloutStatus;
    }

    public String getInstalledUser() {
        return installedUser;
    }

    public void setInstalledUser(String installedUser) {
        this.installedUser = installedUser;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getoEMNo() {
        return oEMNo;
    }

    public void setoEMNo(String oEMNo) {
        this.oEMNo = oEMNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}

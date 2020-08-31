package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/19/2016.
 */
public class AssetCaptureHistory {

    @SerializedName("AssetId")
    private int assetId;
    @SerializedName("QRCode")
    private String qRCode;
    @SerializedName("OutletAssetId")
    private int outletAssetId;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("EAMOutletId")
    private int eAMOutletId;
    @SerializedName("SerialNo")
    private String serialNo;
    @SerializedName("AssetCaptureHistoryId")
    private int assetCaptureHistoryId;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;


    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public int getOutletAssetId() {
        return outletAssetId;
    }

    public void setOutletAssetId(int outletAssetId) {
        this.outletAssetId = outletAssetId;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int geteAMOutletId() {
        return eAMOutletId;
    }

    public void seteAMOutletId(int eAMOutletId) {
        this.eAMOutletId = eAMOutletId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getAssetCaptureHistoryId() {
        return assetCaptureHistoryId;
    }

    public void setAssetCaptureHistoryId(int assetCaptureHistoryId) {
        this.assetCaptureHistoryId = assetCaptureHistoryId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
}

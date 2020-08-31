package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/26/2016.
 */
public class AssetAvailInfo {

    @SerializedName("AssetId")
    private int assetId;
    @SerializedName("SerialNo")
    private String serialNo;
    @SerializedName("QRCode")
    private String qRCode;
    @SerializedName("PepsiUniqueId")
    private String pepsiUniqueId;
    @SerializedName("OutletName")
    private String outletName;
    @SerializedName("OutletCode")
    private String outletCode;
    @SerializedName("Volume")
    private String volume;
    @SerializedName("AssetType")
    private String assetType;
    @SerializedName("OwnerName")
    private String ownerName;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("Landmark")
    private String landmark;
    @SerializedName("PhoneNo1")
    private String phoneNo1;
    @SerializedName("PhoneNo2")
    private String phoneNo2;
    @SerializedName("CapturedOn")
    private String capturedOn;
    @SerializedName("CapturedBy")
    private String capturedBy;

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public String getPepsiUniqueId() {
        return pepsiUniqueId;
    }

    public void setPepsiUniqueId(String pepsiUniqueId) {
        this.pepsiUniqueId = pepsiUniqueId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhoneNo1() {
        return phoneNo1;
    }

    public void setPhoneNo1(String phoneNo1) {
        this.phoneNo1 = phoneNo1;
    }

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }

    public String getCapturedOn() {
        return capturedOn;
    }

    public void setCapturedOn(String capturedOn) {
        this.capturedOn = capturedOn;
    }

    public String getCapturedBy() {
        return capturedBy;
    }

    public void setCapturedBy(String capturedBy) {
        this.capturedBy = capturedBy;
    }
}

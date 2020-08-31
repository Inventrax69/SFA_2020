package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/12/2016.
 */
public class CustomizedAssetInfo {

    @SerializedName("QRCode")
    private String QRCode;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("OutletCode")
    private String outletCode;
    @SerializedName("OutletName")
    private String outletName;
    @SerializedName("ContactPerson")
    private String contactPerson;
    @SerializedName("AddressLine1")
    private String addressLine1;
    @SerializedName("AddressLine2")
    private String addressLine2;
    @SerializedName("AssetVolume")
    private String assetVolume;
    @SerializedName("AssetType")
    private String assetType;
    @SerializedName("InstalledUsr")
    private String installedUsr;
    @SerializedName("InstalledOn")
    private String installedOn;
    @SerializedName("SerialNo")
    private String serialNo;
    @SerializedName("OEMNo")
    private String OEMNo;
    @SerializedName("MobileNo1")
    private String mobileNo1;
    @SerializedName("MobileNo2")
    private String mobileNo2;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("RouteName")
    private String routeName;

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAssetVolume() {
        return assetVolume;
    }

    public void setAssetVolume(String assetVolume) {
        this.assetVolume = assetVolume;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getInstalledUsr() {
        return installedUsr;
    }

    public void setInstalledUsr(String installedUsr) {
        this.installedUsr = installedUsr;
    }

    public String getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(String installedOn) {
        this.installedOn = installedOn;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getOEMNo() {
        return OEMNo;
    }

    public void setOEMNo(String OEMNo) {
        this.OEMNo = OEMNo;
    }

    public String getMobileNo1() {
        return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        this.mobileNo1 = mobileNo1;
    }

    public String getMobileNo2() {
        return mobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        this.mobileNo2 = mobileNo2;
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
}

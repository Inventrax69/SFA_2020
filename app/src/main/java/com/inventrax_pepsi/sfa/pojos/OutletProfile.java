package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class OutletProfile {

    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("ChannelCode")
    private String channelCode;
    @SerializedName("OrderCap")
    private double orderCap;
    @SerializedName("OutletId")
    private int outletId;
    @SerializedName("ImageSource")
    private String imageSource;
    @SerializedName("ChannelId")
    private int channelId;
    @SerializedName("AccountTypeId")
    private int accountTypeId;
    @SerializedName("AccountType")
    private String accountType;
    @SerializedName("IsTraditional")
    private boolean isTraditional;
    @SerializedName("IsPetSelling")
    private boolean isPetSelling;
    @SerializedName("PaymentMode")
    private String paymentMode;
    @SerializedName("OutletCode")
    private String outletCode;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("DistrictTerritoryId")
    private int districtTerritoryId;
    @SerializedName("IsScheduledOutlet")
    private boolean isScheduledOutlet;
    @SerializedName("AadhaarNumber")
    private String aadhaarNumber;
    @SerializedName("IsDisplayAccount")
    private boolean isDisplayAccount;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("ImagePath")
    private String imagePath;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("OutletAssets")
    private List<OutletAsset> outletAssets;


    public List<OutletAsset> getOutletAssets() {
        return outletAssets;
    }

    public void setOutletAssets(List<OutletAsset> outletAssets) {
        this.outletAssets = outletAssets;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isDisplayAccount() {
        return isDisplayAccount;
    }

    public void setDisplayAccount(boolean displayAccount) {
        isDisplayAccount = displayAccount;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public boolean isScheduledOutlet() {
        return isScheduledOutlet;
    }

    public void setIsScheduledOutlet(boolean isScheduledOutlet) {
        this.isScheduledOutlet = isScheduledOutlet;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public double getOrderCap() {
        return orderCap;
    }

    public void setOrderCap(double orderCap) {
        this.orderCap = orderCap;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isTraditional() {
        return isTraditional;
    }

    public void setIsTraditional(boolean isTraditional) {
        this.isTraditional = isTraditional;
    }

    public boolean isPetSelling() {
        return isPetSelling;
    }

    public void setIsPetSelling(boolean isPetSelling) {
        this.isPetSelling = isPetSelling;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
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

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public int getDistrictTerritoryId() {
        return districtTerritoryId;
    }

    public void setDistrictTerritoryId(int districtTerritoryId) {
        this.districtTerritoryId = districtTerritoryId;
    }
}

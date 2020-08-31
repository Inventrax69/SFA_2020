package com.inventrax_pepsi.sfa.pojos;

/**
 * Created by android on 5/6/2016.
 */
public class OutletAsset {

    private String qrCode,pepsiSerialNumber,oemNumber,assetMake,volume,imagePath,imageData,imageName,latitude,longitude;
    private boolean isVISI,hasNightguard,noSerialOnAsset;

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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPepsiSerialNumber() {
        return pepsiSerialNumber;
    }

    public void setPepsiSerialNumber(String pepsiSerialNumber) {
        this.pepsiSerialNumber = pepsiSerialNumber;
    }

    public String getOemNumber() {
        return oemNumber;
    }

    public void setOemNumber(String oemNumber) {
        this.oemNumber = oemNumber;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public void setAssetMake(String assetMake) {
        this.assetMake = assetMake;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public boolean isVISI() {
        return isVISI;
    }

    public void setVISI(boolean VISI) {
        isVISI = VISI;
    }

    public boolean isHasNightguard() {
        return hasNightguard;
    }

    public void setHasNightguard(boolean hasNightguard) {
        this.hasNightguard = hasNightguard;
    }

    public boolean isNoSerialOnAsset() {
        return noSerialOnAsset;
    }

    public void setNoSerialOnAsset(boolean noSerialOnAsset) {
        this.noSerialOnAsset = noSerialOnAsset;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

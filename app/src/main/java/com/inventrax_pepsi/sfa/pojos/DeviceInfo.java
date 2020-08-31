package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;
import com.inventrax_pepsi.util.AndroidUtils;

/**
 * Created by nareshp on 25/01/2016.
 */
public class DeviceInfo {

    @SerializedName("DeviceName")
    private String deviceName;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("IMEINumber")
    private String IMEINumber;
    @SerializedName("DeviceModel")
    private String deviceModel;
    @SerializedName("DeviceManufacturer")
    private String deviceManufacturer;
    @SerializedName("NetworkOperator")
    private String networkOperator;
    @SerializedName("IpAddress")
    private String ipAddress;
    @SerializedName("SerialNumber")
    private String serialNumber;
    @SerializedName("AppVersion")
    private double appVersion;

    private static DeviceInfo deviceInfo;


    public static synchronized DeviceInfo getInstance(){

        if (deviceInfo==null){
            deviceInfo=new DeviceInfo();

            deviceInfo.setDeviceId(AndroidUtils.getAndroidId());
            deviceInfo.setDeviceManufacturer(AndroidUtils.getDeviceManufacturer());
            deviceInfo.setDeviceModel(AndroidUtils.getDeviceModel());

            if(deviceInfo.getIMEINumber() != null)
                deviceInfo.setIMEINumber(AndroidUtils.getIMEINumber());
            else
                deviceInfo.setIMEINumber("");
            deviceInfo.setNetworkOperator(AndroidUtils.getNetworkOperatorName());
            deviceInfo.setIPAddress(AndroidUtils.getIPAddress(true));
            deviceInfo.setDeviceName(AndroidUtils.getDeviceName());
            deviceInfo.setSerialNumber(AndroidUtils.getDeviceSerialNumber());
            deviceInfo.setAppVersion(AndroidUtils.getVersionCode());
        }

        return deviceInfo;
    }

    private DeviceInfo(){ }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(double appVersion) {
        this.appVersion = appVersion;
    }
}

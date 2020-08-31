package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class LogoutInfo {


    @SerializedName("DeviceInfo")
    private DeviceInfo deviceInfo;
    @SerializedName("UserId")
    private String userId;
    @SerializedName("Pwd")
    private String pwd;
    @SerializedName("LogoutTime")
    private String logoutTime;
    @SerializedName("Mode")
    private int mode;
    @SerializedName("ServiceSessionId")
    private String serviceSessionId;
    @SerializedName("SysUserId")
    private int sysUserId;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getServiceSessionId() {
        return serviceSessionId;
    }

    public void setServiceSessionId(String serviceSessionId) {
        this.serviceSessionId = serviceSessionId;
    }

    public int getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(int sysUserId) {
        this.sysUserId = sysUserId;
    }
}

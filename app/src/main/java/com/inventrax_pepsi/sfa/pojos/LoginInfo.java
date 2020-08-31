package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class LoginInfo {

    @SerializedName("DeviceInfo")
    private DeviceInfo deviceInfo;
    @SerializedName("UserLoginId")
    private String userLoginId;
    @SerializedName("Pwd")
    private String pwd;
    @SerializedName("LoginTime")
    private String loginTime;
    @SerializedName("ServiceSessionId")
    private String serviceSessionId;
    @SerializedName("SysUserId")
    private int sysUserId;
    @SerializedName("Mode")
    private int mode;
    @SerializedName("GCM")
    private String gCM;


    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getgCM() {
        return gCM;
    }

    public void setgCM(String gCM) {
        this.gCM = gCM;
    }
}

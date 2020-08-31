package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class UserTarget {

    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("UserTargetId")
    private int userTargetId;
    @SerializedName("TargetId")
    private int targetId;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("TargetComponent")
    private String targetComponent;
    @SerializedName("TargetValue")
    private int targetValue;
    @SerializedName("TargetScheduleType")
    private String targetScheduleType;

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getUserTargetId() {
        return userTargetId;
    }

    public void setUserTargetId(int userTargetId) {
        this.userTargetId = userTargetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(String targetComponent) {
        this.targetComponent = targetComponent;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public String getTargetScheduleType() {
        return targetScheduleType;
    }

    public void setTargetScheduleType(String targetScheduleType) {
        this.targetScheduleType = targetScheduleType;
    }
}

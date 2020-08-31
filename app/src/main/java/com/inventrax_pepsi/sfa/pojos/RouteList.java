package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class RouteList {

    @SerializedName("UserRouteId")
    private int userRouteId;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("Description")
    private String description;
    @SerializedName("StartingOutletId")
    private int startingOutletId;
    @SerializedName("EndingOutletId")
    private int endingOutletId;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("IsDeleted")
    private boolean isDeleted;

    public int getUserRouteId() {
        return userRouteId;
    }

    public void setUserRouteId(int userRouteId) {
        this.userRouteId = userRouteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartingOutletId() {
        return startingOutletId;
    }

    public void setStartingOutletId(int startingOutletId) {
        this.startingOutletId = startingOutletId;
    }

    public int getEndingOutletId() {
        return endingOutletId;
    }

    public void setEndingOutletId(int endingOutletId) {
        this.endingOutletId = endingOutletId;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

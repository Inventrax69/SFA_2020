package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class Route {

    @SerializedName("DistrictId")
    private int districtId;
    @SerializedName("DistrictCode")
    private String districtCode;
    @SerializedName("DistrictTerritoryId")
    private int districtTerritoryId;
    @SerializedName("TerritoryCode")
    private String territoryCode;
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
    private AuditInfo  auditInfo;
    @SerializedName("Customers")
    private String customers;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public int getDistrictTerritoryId() {
        return districtTerritoryId;
    }

    public void setDistrictTerritoryId(int districtTerritoryId) {
        this.districtTerritoryId = districtTerritoryId;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
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

    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
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
}

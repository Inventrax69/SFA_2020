package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naresh on 15-Mar-16.
 */
public class DistrictFMO {

    @SerializedName("DistrictId")
    private int districtId;
    @SerializedName("DistrictCode")
    private String districtCode;
    @SerializedName("DistrictName")
    private String districtName;
    @SerializedName("DistrictTerritoryId")
    private int districtTerritoryId;
    @SerializedName("TerritoryId")
    private String territoryId;
    @SerializedName("TerritoryCode")
    private int territoryCode;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("Validity")
    private String validity;
    @SerializedName("DistrictFmoId")
    private int districtFmoId;

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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getDistrictTerritoryId() {
        return districtTerritoryId;
    }

    public void setDistrictTerritoryId(int districtTerritoryId) {
        this.districtTerritoryId = districtTerritoryId;
    }

    public String getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }

    public int getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(int territoryCode) {
        this.territoryCode = territoryCode;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public int getDistrictFmoId() {
        return districtFmoId;
    }

    public void setDistrictFmoId(int districtFmoId) {
        this.districtFmoId = districtFmoId;
    }
}

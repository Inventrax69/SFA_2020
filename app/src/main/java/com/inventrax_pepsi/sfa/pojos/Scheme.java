package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naresh on 15-Mar-16.
 */
public class Scheme {

    @SerializedName("SchemeTargetItems")
    private List<SchemeTargetItem> schemeTargetItems;
    @SerializedName("SchemeOfferItems")
    private List<SchemeOfferItem> schemeOfferItems;
    @SerializedName("SchemeId")
    private int schemeId;
    @SerializedName("SchemeCode")
    private String schemeCode;
    @SerializedName("SchemeName")
    private String schemeName;
    @SerializedName("Description")
    private String description;
    @SerializedName("IsSingleOffer")
    private boolean isSingleOffer;
    @SerializedName("Validity")
    private Validity validity;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("DistrictId")
    private int districtId;
    @SerializedName("DistrictCode")
    private String districtCode;
    @SerializedName("DistrictTerritoryId")
    private int districtTerritoryId;
    @SerializedName("TerritoryType")
    private String territoryType;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("TerritoryTypeId")
    private int territoryTypeId;

    public List<SchemeTargetItem> getSchemeTargetItems() {
        return schemeTargetItems;
    }

    public void setSchemeTargetItems(List<SchemeTargetItem> schemeTargetItems) {
        this.schemeTargetItems = schemeTargetItems;
    }

    public List<SchemeOfferItem> getSchemeOfferItems() {
        return schemeOfferItems;
    }

    public void setSchemeOfferItems(List<SchemeOfferItem> schemeOfferItems) {
        this.schemeOfferItems = schemeOfferItems;
    }

    public int getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(int schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSingleOffer() {
        return isSingleOffer;
    }

    public void setIsSingleOffer(boolean isSingleOffer) {
        this.isSingleOffer = isSingleOffer;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
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

    public String getTerritoryType() {
        return territoryType;
    }

    public void setTerritoryType(String territoryType) {
        this.territoryType = territoryType;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public int getTerritoryTypeId() {
        return territoryTypeId;
    }

    public void setTerritoryTypeId(int territoryTypeId) {
        this.territoryTypeId = territoryTypeId;
    }
}

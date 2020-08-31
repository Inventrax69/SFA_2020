package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/12/2016.
 */
public class AssetRequest {

    @SerializedName("AssetRequestId")
    private int assetRequestId;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("Status")
    private int status;
    @SerializedName("RequestedOn")
    private String requestedOn;
    @SerializedName("HandledBy")
    private int handledBy;
    @SerializedName("Priority")
    private String priority;
    @SerializedName("RequestedVolume")
    private String requestedVolume;
    @SerializedName("VerificationStatus")
    private int verificationStatus;
    @SerializedName("CustomerSales")
    private double customerSales;
    @SerializedName("AssetTypeId")
    private int assetTypeId;
    @SerializedName("CoolerVolumeId")
    private int coolerVolumeId;
    @SerializedName("CoolerVolume")
    private String coolerVolume;
    @SerializedName("ProposedAccountType")
    private int proposedAccountType;
    @SerializedName("ProposedVolume")
    private double proposedVolume;
    @SerializedName("ProposedCustomerSales")
    private double proposedCustomerSales;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("HasNightGaurd")
    private int hasNightGaurd;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("AssetType")
    private String assetType;
    @SerializedName("Size")
    private int size;
    @SerializedName("VerifiedBy")
    private String verifiedBy;
    @SerializedName("AssetRequisitionProofType")
    private String assetRequisitionProofType;
    @SerializedName("PriorityId")
    private int priorityId;
    @SerializedName("RequestHandlingPriority")
    private String requestHandlingPriority;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;


    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getCoolerVolume() {
        return coolerVolume;
    }

    public void setCoolerVolume(String coolerVolume) {
        this.coolerVolume = coolerVolume;
    }

    public int getAssetRequestId() {
        return assetRequestId;
    }

    public void setAssetRequestId(int assetRequestId) {
        this.assetRequestId = assetRequestId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public int getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRequestedVolume() {
        return requestedVolume;
    }

    public void setRequestedVolume(String requestedVolume) {
        this.requestedVolume = requestedVolume;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(int verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public double getCustomerSales() {
        return customerSales;
    }

    public void setCustomerSales(double customerSales) {
        this.customerSales = customerSales;
    }

    public int getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(int assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public int getCoolerVolumeId() {
        return coolerVolumeId;
    }

    public void setCoolerVolumeId(int coolerVolumeId) {
        this.coolerVolumeId = coolerVolumeId;
    }

    public int getProposedAccountType() {
        return proposedAccountType;
    }

    public void setProposedAccountType(int proposedAccountType) {
        this.proposedAccountType = proposedAccountType;
    }

    public double getProposedVolume() {
        return proposedVolume;
    }

    public void setProposedVolume(double proposedVolume) {
        this.proposedVolume = proposedVolume;
    }

    public double getProposedCustomerSales() {
        return proposedCustomerSales;
    }

    public void setProposedCustomerSales(double proposedCustomerSales) {
        this.proposedCustomerSales = proposedCustomerSales;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getHasNightGaurd() {
        return hasNightGaurd;
    }

    public void setHasNightGaurd(int hasNightGaurd) {
        this.hasNightGaurd = hasNightGaurd;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getAssetRequisitionProofType() {
        return assetRequisitionProofType;
    }

    public void setAssetRequisitionProofType(String assetRequisitionProofType) {
        this.assetRequisitionProofType = assetRequisitionProofType;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getRequestHandlingPriority() {
        return requestHandlingPriority;
    }

    public void setRequestHandlingPriority(String requestHandlingPriority) {
        this.requestHandlingPriority = requestHandlingPriority;
    }
}

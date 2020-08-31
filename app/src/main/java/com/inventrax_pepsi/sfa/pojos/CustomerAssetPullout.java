package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 5/11/2016.
 */
public class CustomerAssetPullout {

    @SerializedName("CustomerAssetPulloutId")
    private int customerAssetPulloutId;
    @SerializedName("CustomerAssetId")
    private int customerAssetId;
    @SerializedName("AssetPulloutReasonId")
    private int assetPulloutReasonId;
    @SerializedName("AssetPulloutReason")
    private String assetPulloutReason;
    @SerializedName("IsWorking")
    private int isWorking;
    @SerializedName("IsScrap")
    private int isScrap;
    @SerializedName("ApprovedBy")
    private int approvedBy;
    @SerializedName("PulledOutOn")
    private String pulledOutOn;
    @SerializedName("PulledOutBy")
    private int pulledOutBy;
    @SerializedName("Status")
    private int status;
    @SerializedName("MachineStatusId")
    private int machineStatusId;
    @SerializedName("QRCode")
    private String QRCode;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;


    public String getAssetPulloutReason() {
        return assetPulloutReason;
    }

    public void setAssetPulloutReason(String assetPulloutReason) {
        this.assetPulloutReason = assetPulloutReason;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerAssetPulloutId() {
        return customerAssetPulloutId;
    }

    public void setCustomerAssetPulloutId(int customerAssetPulloutId) {
        this.customerAssetPulloutId = customerAssetPulloutId;
    }

    public int getCustomerAssetId() {
        return customerAssetId;
    }

    public void setCustomerAssetId(int customerAssetId) {
        this.customerAssetId = customerAssetId;
    }

    public int getAssetPulloutReasonId() {
        return assetPulloutReasonId;
    }

    public void setAssetPulloutReasonId(int assetPulloutReasonId) {
        this.assetPulloutReasonId = assetPulloutReasonId;
    }

    public int getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(int isWorking) {
        this.isWorking = isWorking;
    }

    public int getIsScrap() {
        return isScrap;
    }

    public void setIsScrap(int isScrap) {
        this.isScrap = isScrap;
    }

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getPulledOutOn() {
        return pulledOutOn;
    }

    public void setPulledOutOn(String pulledOutOn) {
        this.pulledOutOn = pulledOutOn;
    }

    public int getPulledOutBy() {
        return pulledOutBy;
    }

    public void setPulledOutBy(int pulledOutBy) {
        this.pulledOutBy = pulledOutBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMachineStatusId() {
        return machineStatusId;
    }

    public void setMachineStatusId(int machineStatusId) {
        this.machineStatusId = machineStatusId;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

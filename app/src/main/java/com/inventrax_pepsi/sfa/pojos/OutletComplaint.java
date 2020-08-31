package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/12/2016.
 */
public class OutletComplaint {

    @SerializedName("OutletComplaintId")
    private int outletComplaintId;
    @SerializedName("ComplaintStatusId")
    private int complaintStatusId;
    @SerializedName("ComplaintStatus")
    private String complaintStatus;
    @SerializedName("ComplaintTypeId")
    private int complaintTypeId;
    @SerializedName("ComplaintType")
    private String complaintType;
    @SerializedName("Description")
    private String description;
    @SerializedName("HandledBy")
    private int handledBy;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("RouteCode")
    private String routeCode;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("RouteId")
    private int routeId;
    @SerializedName("AssetId")
    private int assetId;
    @SerializedName("SerialNo")
    private String serialNo;
    @SerializedName("QRCode")
    private String qRCode;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;

    public int getOutletComplaintId() {
        return outletComplaintId;
    }

    public void setOutletComplaintId(int outletComplaintId) {
        this.outletComplaintId = outletComplaintId;
    }

    public int getComplaintStatusId() {
        return complaintStatusId;
    }

    public void setComplaintStatusId(int complaintStatusId) {
        this.complaintStatusId = complaintStatusId;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public int getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(int complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
}

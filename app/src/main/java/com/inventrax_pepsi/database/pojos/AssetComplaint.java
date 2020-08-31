package com.inventrax_pepsi.database.pojos;

/**
 * Author   : Naresh P.
 * Date		: 12/04/2016
 * Purpose	: Asset Complaint
 */


public class AssetComplaint {

    private int autoIncId,jsonMessageAutoIncId,complaintId,complaintStatus,customerId;
    private String complaintJSON;

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getJsonMessageAutoIncId() {
        return jsonMessageAutoIncId;
    }

    public void setJsonMessageAutoIncId(int jsonMessageAutoIncId) {
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(int complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getComplaintJSON() {
        return complaintJSON;
    }

    public void setComplaintJSON(String complaintJSON) {
        this.complaintJSON = complaintJSON;
    }
}

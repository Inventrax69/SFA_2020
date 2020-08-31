package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 8/11/2016.
 */

public class DistributorProfile
{
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("DistributorId")
    private int distributorId;
    @SerializedName("DistributorCode")
    private String distributorCode;
    @SerializedName("IsDirectOperation")
    private boolean isDirectOperation;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public boolean isDirectOperation() {
        return isDirectOperation;
    }

    public void setDirectOperation(boolean directOperation) {
        isDirectOperation = directOperation;
    }
}



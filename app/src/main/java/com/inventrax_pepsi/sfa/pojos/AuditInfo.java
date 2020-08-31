package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class AuditInfo {

    @SerializedName("UserId")
    private int userId;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("CreatedDate")
    private String createdDate;
    @SerializedName("LastModifiedUserId")
    private int lastModifiedUserId;
    @SerializedName("LastModifiedDate")
    private String lastModifiedDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public void setLastModifiedUserId(int lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

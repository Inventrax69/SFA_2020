package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class JSONMessage {

    String jsonMessage;
    private int autoIncId, notificationTypeId, noOfRequests,syncStatus,notificationId;

    public JSONMessage(){}

    public JSONMessage(String jsonMessage, int autoIncId, int notificationTypeId, int noOfRequests, int syncStatus, int notificationId) {
        this.jsonMessage = jsonMessage;
        this.autoIncId = autoIncId;
        this.notificationTypeId = notificationTypeId;
        this.noOfRequests = noOfRequests;
        this.syncStatus = syncStatus;
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public int getNoOfRequests() {
        return noOfRequests;
    }

    public void setNoOfRequests(int noOfRequests) {
        this.noOfRequests = noOfRequests;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}

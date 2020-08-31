package com.inventrax_pepsi.database.pojos;

/**
 * Created by android on 5/7/2016.
 */
public class AssetRequest {

    private int autoIncId,customerId,jsonMessageAutoIncId;
    private String json;

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getJsonMessageAutoIncId() {
        return jsonMessageAutoIncId;
    }

    public void setJsonMessageAutoIncId(int jsonMessageAutoIncId) {
        this.jsonMessageAutoIncId = jsonMessageAutoIncId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}

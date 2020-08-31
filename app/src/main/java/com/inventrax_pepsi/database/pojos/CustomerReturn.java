package com.inventrax_pepsi.database.pojos;

/**
 * Created by android on 4/5/2016.
 */
public class CustomerReturn {

    private int autoIncId,customerId,routeId,jsonMessageAutoIncId;
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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
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

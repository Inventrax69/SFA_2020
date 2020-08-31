package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 27-Mar-16.
 */
public class VehicleLoad {

    private int autoInc,loadId,routeId,loadStatus;
    private String routeCode, settlementNo,loadJSON;
    private double loadAmount;


    public int getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(int loadStatus) {
        this.loadStatus = loadStatus;
    }

    public int getAutoInc() {
        return autoInc;
    }

    public void setAutoInc(int autoInc) {
        this.autoInc = autoInc;
    }

    public int getLoadId() {
        return loadId;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getLoadJSON() {
        return loadJSON;
    }

    public void setLoadJSON(String loadJSON) {
        this.loadJSON = loadJSON;
    }

    public double getLoadAmount() {
        return loadAmount;
    }

    public void setLoadAmount(double loadAmount) {
        this.loadAmount = loadAmount;
    }
}

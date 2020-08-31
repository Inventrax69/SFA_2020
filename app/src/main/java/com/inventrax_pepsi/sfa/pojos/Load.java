package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Load {

    @SerializedName("RouteAgentId")
    private int routeAgentId;
    @SerializedName("SettelementStatusId")
    private int settelementStatusId;
    @SerializedName("SalesMethod")
    private String salesMethod;
    @SerializedName("DocumentNumber")
    private String documentNumber;
    @SerializedName("LoadAmount")
    private double loadAmount;
    @SerializedName("StoreId")
    private int storeId;
    @SerializedName("LoadFromId")
    private int loadFromId;
    @SerializedName("LoadToId")
    private int loadToId;
    @SerializedName("IsPlantLoad")
    private boolean isPlantLoad;
    @SerializedName("LoadTypeId")
    private int loadTypeId;
    @SerializedName("LoadId")
    private int loadId;
    @SerializedName("SettlementNo")
    private String settlementNo;
    @SerializedName("Vehicles")
    private List<Vehicle> vehicles;
    @SerializedName("LoadItems")
    private List<LoadItem> loadItems;
    @SerializedName("AgentName")
    private String agentName;
    @SerializedName("Route")
    private Route route;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("VerifiedBy")
    private int verifiedBy;
    @SerializedName("VerifiedUser")
    private String verifiedUser;
    @SerializedName("PlantStoreId")
    private int plantStoreId;
    @SerializedName("PlantStore")
    private String plantStore;
    @SerializedName("DepotStoreId")
    private int depotStoreId;
    @SerializedName("DepotStore")
    private String depotStore;
    @SerializedName("SettlementStatus")
    private String settlementStatus;
    @SerializedName("IsReadySelling")
    private boolean isReadySelling;

    public int getRouteAgentId() {
        return routeAgentId;
    }

    public void setRouteAgentId(int routeAgentId) {
        this.routeAgentId = routeAgentId;
    }

    public int getSettelementStatusId() {
        return settelementStatusId;
    }

    public void setSettelementStatusId(int settelementStatusId) {
        this.settelementStatusId = settelementStatusId;
    }

    public String getSalesMethod() {
        return salesMethod;
    }

    public void setSalesMethod(String salesMethod) {
        this.salesMethod = salesMethod;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public double getLoadAmount() {
        return loadAmount;
    }

    public void setLoadAmount(double loadAmount) {
        this.loadAmount = loadAmount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getLoadFromId() {
        return loadFromId;
    }

    public void setLoadFromId(int loadFromId) {
        this.loadFromId = loadFromId;
    }

    public int getLoadToId() {
        return loadToId;
    }

    public void setLoadToId(int loadToId) {
        this.loadToId = loadToId;
    }

    public boolean isPlantLoad() {
        return isPlantLoad;
    }

    public void setIsPlantLoad(boolean isPlantLoad) {
        this.isPlantLoad = isPlantLoad;
    }

    public int getLoadTypeId() {
        return loadTypeId;
    }

    public void setLoadTypeId(int loadTypeId) {
        this.loadTypeId = loadTypeId;
    }

    public int getLoadId() {
        return loadId;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<LoadItem> getLoadItems() {
        return loadItems;
    }

    public void setLoadItems(List<LoadItem> loadItems) {
        this.loadItems = loadItems;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedUser() {
        return verifiedUser;
    }

    public void setVerifiedUser(String verifiedUser) {
        this.verifiedUser = verifiedUser;
    }

    public int getPlantStoreId() {
        return plantStoreId;
    }

    public void setPlantStoreId(int plantStoreId) {
        this.plantStoreId = plantStoreId;
    }

    public String getPlantStore() {
        return plantStore;
    }

    public void setPlantStore(String plantStore) {
        this.plantStore = plantStore;
    }

    public int getDepotStoreId() {
        return depotStoreId;
    }

    public void setDepotStoreId(int depotStoreId) {
        this.depotStoreId = depotStoreId;
    }

    public String getDepotStore() {
        return depotStore;
    }

    public void setDepotStore(String depotStore) {
        this.depotStore = depotStore;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public boolean isReadySelling() {
        return isReadySelling;
    }

    public void setIsReadySelling(boolean isReadySelling) {
        this.isReadySelling = isReadySelling;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class ItemPrice {

    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("DistrictTerritoryId")
    private int districtTerritoryId;
    @SerializedName("TerritoryId")
    private int territoryId;
    @SerializedName("TerritoryCode")
    private String territoryCode;
    @SerializedName("ItemPriceCode")
    private String itemPriceCode;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("EnableDisc")
    private boolean enableDisc;
    @SerializedName("PriceApplicableTypeId")
    private int priceApplicableTypeId;
    @SerializedName("PriceApplicableValue")
    private double priceApplicableValue;
    @SerializedName("MRPPriceComponents")
    private String mRPPriceComponents;
    @SerializedName("TradePriceComponents")
    private String tradePriceComponents;
    @SerializedName("ItemPriceId")
    private int itemPriceId;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("TradePrice")
    private double tradePrice;
    @SerializedName("MRP")
    private double mRP;
    @SerializedName("IsTrade")
    private boolean isTrade;
    @SerializedName("AuditInfo")
    private AuditInfo  auditInfo;
    @SerializedName("Validity")
    private Validity  validity;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("Units")
    private int units;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("DistrictId")
    private int districtId;
    @SerializedName("DistrictCode")
    private String districtCode;
    @SerializedName("isUserSelected")
    private boolean isUserSelected;


    public boolean isUserSelected() {
        return isUserSelected;
    }

    public void setUserSelected(boolean userSelected) {
        isUserSelected = userSelected;
    }

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
    }

    public int getDistrictTerritoryId() {
        return districtTerritoryId;
    }

    public void setDistrictTerritoryId(int districtTerritoryId) {
        this.districtTerritoryId = districtTerritoryId;
    }

    public int getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public String getItemPriceCode() {
        return itemPriceCode;
    }

    public void setItemPriceCode(String itemPriceCode) {
        this.itemPriceCode = itemPriceCode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isEnableDisc() {
        return enableDisc;
    }

    public void setEnableDisc(boolean enableDisc) {
        this.enableDisc = enableDisc;
    }

    public int getPriceApplicableTypeId() {
        return priceApplicableTypeId;
    }

    public void setPriceApplicableTypeId(int priceApplicableTypeId) {
        this.priceApplicableTypeId = priceApplicableTypeId;
    }

    public double getPriceApplicableValue() {
        return priceApplicableValue;
    }

    public void setPriceApplicableValue(double priceApplicableValue) {
        this.priceApplicableValue = priceApplicableValue;
    }

    public String getmRPPriceComponents() {
        return mRPPriceComponents;
    }

    public void setmRPPriceComponents(String mRPPriceComponents) {
        this.mRPPriceComponents = mRPPriceComponents;
    }

    public String getTradePriceComponents() {
        return tradePriceComponents;
    }

    public void setTradePriceComponents(String tradePriceComponents) {
        this.tradePriceComponents = tradePriceComponents;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public double getmRP() {
        return mRP;
    }

    public void setmRP(double mRP) {
        this.mRP = mRP;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public void setIsTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
}

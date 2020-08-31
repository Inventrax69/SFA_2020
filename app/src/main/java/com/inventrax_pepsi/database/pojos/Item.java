package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class Item {


    private int autoIncId,itemId,itemUoMId;
    private String itemCode,itemJSON,itemType;
    private double itemMRP;
    private double trade_retail;
    private int itemUoMQuantity;
    private String brandName,brandPackName,itemUoM,uomJSON,priceJSON;
    private int isTrade,isFMO,brandDisplaySeq,packDisplaySeq;


    public int getBrandDisplaySeq() {
        return brandDisplaySeq;
    }

    public void setBrandDisplaySeq(int brandDisplaySeq) {
        this.brandDisplaySeq = brandDisplaySeq;
    }

    public int getPackDisplaySeq() {
        return packDisplaySeq;
    }

    public void setPackDisplaySeq(int packDisplaySeq) {
        this.packDisplaySeq = packDisplaySeq;
    }

    public int getIsFMO() {
        return isFMO;
    }

    public void setIsFMO(int isFMO) {
        this.isFMO = isFMO;
    }

    public int getAutoIncId() {

        return autoIncId;
    }

    public int getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(int isTrade) {
        this.isTrade = isTrade;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemUoMId() {
        return itemUoMId;
    }

    public void setItemUoMId(int itemUoMId) {
        this.itemUoMId = itemUoMId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemJSON() {
        return itemJSON;
    }

    public void setItemJSON(String itemJSON) {
        this.itemJSON = itemJSON;
    }

    public double getItemMRP() {
        return itemMRP;
    }

    public void setItemMRP(double itemMRP) {
        this.itemMRP = itemMRP;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandPackName() {
        return brandPackName;
    }

    public void setBrandPackName(String brandPackName) {
        this.brandPackName = brandPackName;
    }

    public String getItemUoM() {
        return itemUoM;
    }

    public void setItemUoM(String itemUoM) {
        this.itemUoM = itemUoM;
    }

    public int getItemUoMQuantity() {
        return itemUoMQuantity;
    }

    public void setItemUoMQuantity(int itemUoMQuantity) {
        this.itemUoMQuantity = itemUoMQuantity;
    }

    public double getTrade_retail() {
        return trade_retail;
    }

    public void setTrade_retail(double trade_retail) {
        this.trade_retail = trade_retail;
    }

    public String getUomJSON() {
        return uomJSON;
    }

    public void setUomJSON(String uomJSON) {
        this.uomJSON = uomJSON;
    }

    public String getPriceJSON() {
        return priceJSON;
    }

    public void setPriceJSON(String priceJSON) {
        this.priceJSON = priceJSON;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}

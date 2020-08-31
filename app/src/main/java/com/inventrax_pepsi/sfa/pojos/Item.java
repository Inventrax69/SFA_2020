package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by android on 3/9/2016.
 */
public class Item {

    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("ItemUoMs")
    private List<ItemUoM> itemUoMs;
    @SerializedName("ItemPrices")
    private List<ItemPrice> itemPrices;
    @SerializedName("ItemType")
    private String itemType;
    @SerializedName("ItemTypeCode")
    private String itemTypeCode;
    @SerializedName("ItemTypeId")
    private int itemTypeId;
    @SerializedName("ItemPackId")
    private int itemPackId;
    @SerializedName("ItemBrandId")
    private int itemBrandId;
    @SerializedName("ItemPack")
    private String itemPack;
    @SerializedName("ItemBrand")
    private String itemBrand;
    @SerializedName("ItemBrandPackId")
    private int itemBrandPackId;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemDescription")
    private String itemDescription;
    @SerializedName("IsFMO")
    private boolean IsFMO;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("BrandDisplaySeq")
    private int brandDisplaySeq;
    @SerializedName("PackDisplaySeq")
    private int packDisplaySeq;
    @SerializedName("TaxationId")
    private int taxationId;
    @SerializedName("VAT")
    private double vAT;
    private Map<Integer,String> itemDisplayMap;
    private Map<Integer,Object> itemRebateMap;

    public double getvAT() {
        return vAT;
    }

    public void setvAT(double vAT) {
        this.vAT = vAT;
    }

    public int getTaxationId() {
        return taxationId;
    }

    public void setTaxationId(int taxationId) {
        this.taxationId = taxationId;
    }

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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isFMO() {
        return IsFMO;
    }

    public void setIsFMO(boolean isFMO) {
        IsFMO = isFMO;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public Map<Integer, String> getItemDisplayMap() {
        return itemDisplayMap;
    }

    public void setItemDisplayMap(Map<Integer, String> itemDisplayMap) {
        this.itemDisplayMap = itemDisplayMap;
    }

    public Map<Integer, Object> getItemRebateMap() {
        return itemRebateMap;
    }

    public void setItemRebateMap(Map<Integer, Object> itemRebateMap) {
        this.itemRebateMap = itemRebateMap;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public List<ItemUoM> getItemUoMs() {
        return itemUoMs;
    }

    public void setItemUoMs(List<ItemUoM> itemUoMs) {
        this.itemUoMs = itemUoMs;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public int getItemPackId() {
        return itemPackId;
    }

    public void setItemPackId(int itemPackId) {
        this.itemPackId = itemPackId;
    }

    public int getItemBrandId() {
        return itemBrandId;
    }

    public void setItemBrandId(int itemBrandId) {
        this.itemBrandId = itemBrandId;
    }

    public String getItemPack() {
        return itemPack;
    }

    public void setItemPack(String itemPack) {
        this.itemPack = itemPack;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public int getItemBrandPackId() {
        return itemBrandPackId;
    }

    public void setItemBrandPackId(int itemBrandPackId) {
        this.itemBrandPackId = itemBrandPackId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}

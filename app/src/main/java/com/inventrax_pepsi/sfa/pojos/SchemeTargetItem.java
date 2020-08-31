package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Naresh on 15-Mar-16.
 */
public class SchemeTargetItem {
    @SerializedName("SchemeItemId")
    private int schemeItemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemUoMId")
    private int itemUoMId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("IsOfferItem")
    private boolean isOfferItem;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("ItemBrand")
    private String itemBrand;
    @SerializedName("ItemPack")
    private String itemPack;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("ItemPackId")
    private  int itemPackId;

    @SerializedName("Units")
    private  double units;


    @SerializedName("MRP")
    private  double MRP;

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }




    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public int getItemPackId() {
        return itemPackId;
    }

    public void setItemPackId(int itemPackId) {
        this.itemPackId = itemPackId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemPack() {
        return itemPack;
    }

    public void setItemPack(String itemPack) {
        this.itemPack = itemPack;
    }

    public int getSchemeItemId() {
        return schemeItemId;
    }

    public void setSchemeItemId(int schemeItemId) {
        this.schemeItemId = schemeItemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isOfferItem() {
        return isOfferItem;
    }

    public void setIsOfferItem(boolean isOfferItem) {
        this.isOfferItem = isOfferItem;
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

    public int getUoMId() {
        return uoMId;
    }

    public void setUoMId(int uoMId) {
        this.uoMId = uoMId;
    }

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class ItemUoM {

    @SerializedName("ItemUomId")
    private int itemUomId;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("Units")
    private int units;
    @SerializedName("UomCode")
    private String uomCode;
    @SerializedName("IsDeleted")
    private boolean isDeleted;

    public int getItemUomId() {
        return itemUomId;
    }

    public void setItemUomId(int itemUomId) {
        this.itemUomId = itemUomId;
    }

    public String getUoM() {
        return uoM;
    }

    public void setUoM(String uoM) {
        this.uoM = uoM;
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

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

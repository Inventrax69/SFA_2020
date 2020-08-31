package com.inventrax_pepsi.sfa.order;

import com.inventrax_pepsi.sfa.pojos.ItemUoM;

import java.util.List;

/**
 * Created by android on 6/7/2016.
 */
public class CustomObject {

    private List<ItemUoM> itemUoMList;
    private int itemId;
    private String brandName,itemPack;
    private double itemMRP;
    private String imageName;
    private double caseQuantity,bottleQuantity;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getItemPack() {
        return itemPack;
    }

    public void setItemPack(String itemPack) {
        this.itemPack = itemPack;
    }

    public double getItemMRP() {
        return itemMRP;
    }

    public void setItemMRP(double itemMRP) {
        this.itemMRP = itemMRP;
    }

    public List<ItemUoM> getItemUoMList() {
        return itemUoMList;
    }

    public void setItemUoMList(List<ItemUoM> itemUoMList) {
        this.itemUoMList = itemUoMList;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getCaseQuantity() {
        return caseQuantity;
    }

    public void setCaseQuantity(double caseQuantity) {
        this.caseQuantity = caseQuantity;
    }

    public double getBottleQuantity() {
        return bottleQuantity;
    }

    public void setBottleQuantity(double bottleQuantity) {
        this.bottleQuantity = bottleQuantity;
    }
}

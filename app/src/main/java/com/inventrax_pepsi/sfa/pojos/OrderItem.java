package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class OrderItem {

    @SerializedName("OrderItemDiscount")
    private OrderItemDiscount orderItemDiscount;
    @SerializedName("OrderItemSchemes")
    private List<OrderItemScheme> orderItemSchemes;
    @SerializedName("UoMCode")
    private String uoMCode;
    @SerializedName("UoM")
    private String uoM;
    @SerializedName("UoMId")
    private int uoMId;
    @SerializedName("ItemCode")
    private String itemCode;
    @SerializedName("Price")
    private double price;
    @SerializedName("DerivedPrice")
    private double derivedPrice;
    @SerializedName("DiscountPrice")
    private double discountPrice;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("OrderItemId")
    private int orderItemId;
    @SerializedName("ItemId")
    private int itemId;
    @SerializedName("ItemUOMId")
    private int itemUOMId;
    @SerializedName("Quantity")
    private double quantity;
    @SerializedName("ItemPrice")
    private double itemPrice;
    @SerializedName("DispatchQty")
    private double dispatchQty;
    @SerializedName("ItemBrand")
    private String itemBrand;
    @SerializedName("ItemPack")
    private String itemPack;
    @SerializedName("MRP")
    private double MRP;
    @SerializedName("ItemType")
    private String itemType;
    @SerializedName("ItemTypeId")
    private int itemTypeId;
    @SerializedName("ItemPriceId")
    private int itemPriceId;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("HideDeleteButton")
    private boolean hideDeleteButton;
    @SerializedName("ItemBrandId")
    private  int itemBrandId;
    @SerializedName("ItemPackId")
    private  int itemPackId;
    @SerializedName("SelectedSchemeId")
    private int selectedSchemeId;
    @SerializedName("SelectedOfferItemId")
    private int selectedOfferItemId;

    public int getSelectedSchemeId() {
        return selectedSchemeId;
    }

    public void setSelectedSchemeId(int selectedSchemeId) {
        this.selectedSchemeId = selectedSchemeId;
    }

    public int getSelectedOfferItemId() {
        return selectedOfferItemId;
    }

    public void setSelectedOfferItemId(int selectedOfferItemId) {
        this.selectedOfferItemId = selectedOfferItemId;
    }

    public int getItemBrandId() {
        return itemBrandId;
    }

    public void setItemBrandId(int itemBrandId) {
        this.itemBrandId = itemBrandId;
    }

    public int getItemPackId() {
        return itemPackId;
    }

    public void setItemPackId(int itemPackId) {
        this.itemPackId = itemPackId;
    }



    public boolean isHideDeleteButton() {
        return hideDeleteButton;
    }

    public void setHideDeleteButton(boolean hideDeleteButton) {
        this.hideDeleteButton = hideDeleteButton;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
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

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public OrderItemDiscount getOrderItemDiscount() {
        return orderItemDiscount;
    }

    public void setOrderItemDiscount(OrderItemDiscount orderItemDiscount) {
        this.orderItemDiscount = orderItemDiscount;
    }

    public List<OrderItemScheme> getOrderItemSchemes() {
        return orderItemSchemes;
    }

    public void setOrderItemSchemes(List<OrderItemScheme> orderItemSchemes) {
        this.orderItemSchemes = orderItemSchemes;
    }

    public String getUoMCode() {
        return uoMCode;
    }

    public void setUoMCode(String uoMCode) {
        this.uoMCode = uoMCode;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDerivedPrice() {
        return derivedPrice;
    }

    public void setDerivedPrice(double derivedPrice) {
        this.derivedPrice = derivedPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
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

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemUOMId() {
        return itemUOMId;
    }

    public void setItemUOMId(int itemUOMId) {
        this.itemUOMId = itemUOMId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(double dispatchQty) {
        this.dispatchQty = dispatchQty;
    }
}

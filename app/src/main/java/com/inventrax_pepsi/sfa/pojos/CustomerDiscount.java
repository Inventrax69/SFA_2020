package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/15/2016.
 */
public class CustomerDiscount {

    @SerializedName("DiscountCode")
    private String discountCode;
    @SerializedName("DiscountId")
    private int discountId;
    @SerializedName("DiscountName")
    private String discountName;
    @SerializedName("DiscountTypeId")
    private int discountTypeId;
    @SerializedName("DiscountValue")
    private double discountValue;
    @SerializedName("DiscountType")
    private String discountType;
    @SerializedName("DiscountAppliedOn")
    private int discountAppliedOn;
    @SerializedName("IsSpot")
    private boolean isSpot;
    @SerializedName("CreatedOn")
    private String createdOn;
    @SerializedName("CreatedBy")
    private int createdBy;
    @SerializedName("DiscountApplicableTypeId")
    private int discountApplicableTypeId;
    @SerializedName("CreatedUser")
    private String createdUser;
    @SerializedName("DiscountApplicableId")
    private int discountApplicableId;
    @SerializedName("ValidFrom")
    private String validFrom;
    @SerializedName("ValidTo")
    private String validTo;
    @SerializedName("ApplicablePriceTypeId")
    private int applicablePriceTypeId;
    @SerializedName("TargetItemUoMId")
    private int targetItemUoMId;
    @SerializedName("TargetItemQty")
    private double targetItemQty;
    @SerializedName("TargetDiscItemId")
    private int targetDiscItemId;
    @SerializedName("TargetUoM")
    private String targetUoM;
    @SerializedName("TargetUoMCode")
    private String targetUoMCode;
    @SerializedName("TargetUoMId")
    private int targetUoMId;
    @SerializedName("TargetItemId")
    private int targetItemId;
    @SerializedName("TargetItemName")
    private String targetItemName;
    @SerializedName("TargetItemCode")
    private String targetItemCode;
    @SerializedName("ItemEnableForDisc")
    private boolean itemEnableForDisc;
    @SerializedName("IsTrade")
    private boolean isTrade;
    @SerializedName("MRP")
    private double mRP;
    @SerializedName("Trade")
    private double trade;
    @SerializedName("PriceAppliedValue")
    private double priceAppliedValue;
    @SerializedName("PriceAppliedTypeId")
    private int priceAppliedTypeId;
    @SerializedName("OfferItemUoMId")
    private int offerItemUoMId;
    @SerializedName("OfferQty")
    private double offerQty;
    @SerializedName("OfferDiscItemId")
    private int offerDiscItemId;
    @SerializedName("OfferUoM")
    private String offerUoM;
    @SerializedName("OfferUoMCode")
    private String offerUoMCode;
    @SerializedName("OfferUoMId")
    private int offerUoMId;
    @SerializedName("OfferItemId")
    private int offerItemId;
    @SerializedName("OfferItemCode")
    private String offerItemCode;
    @SerializedName("OfferItemName")
    private String offerItemName;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("CustomerGroupId")
    private int customerGroupId;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustomerGroup")
    private String customerGroup;

    public int getTargetUoMUnits() {
        return targetUoMUnits;
    }

    public void setTargetUoMUnits(int targetUoMUnits) {
        this.targetUoMUnits = targetUoMUnits;
    }

    @SerializedName("TargetUoMUnits")
    private  int targetUoMUnits;

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getTargetItemQty() {
        return targetItemQty;
    }

    public void setTargetItemQty(double targetItemQty) {
        this.targetItemQty = targetItemQty;
    }

    public double getmRP() {
        return mRP;
    }

    public void setmRP(double mRP) {
        this.mRP = mRP;
    }

    public double getTrade() {
        return trade;
    }

    public void setTrade(double trade) {
        this.trade = trade;
    }

    public double getPriceAppliedValue() {
        return priceAppliedValue;
    }

    public void setPriceAppliedValue(double priceAppliedValue) {
        this.priceAppliedValue = priceAppliedValue;
    }

    public double getOfferQty() {
        return offerQty;
    }

    public void setOfferQty(double offerQty) {
        this.offerQty = offerQty;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public int getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getDiscountAppliedOn() {
        return discountAppliedOn;
    }

    public void setDiscountAppliedOn(int discountAppliedOn) {
        this.discountAppliedOn = discountAppliedOn;
    }

    public boolean isSpot() {
        return isSpot;
    }

    public void setIsSpot(boolean isSpot) {
        this.isSpot = isSpot;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getDiscountApplicableTypeId() {
        return discountApplicableTypeId;
    }

    public void setDiscountApplicableTypeId(int discountApplicableTypeId) {
        this.discountApplicableTypeId = discountApplicableTypeId;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public int getDiscountApplicableId() {
        return discountApplicableId;
    }

    public void setDiscountApplicableId(int discountApplicableId) {
        this.discountApplicableId = discountApplicableId;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public int getApplicablePriceTypeId() {
        return applicablePriceTypeId;
    }

    public void setApplicablePriceTypeId(int applicablePriceTypeId) {
        this.applicablePriceTypeId = applicablePriceTypeId;
    }

    public int getTargetItemUoMId() {
        return targetItemUoMId;
    }

    public void setTargetItemUoMId(int targetItemUoMId) {
        this.targetItemUoMId = targetItemUoMId;
    }


    public int getTargetDiscItemId() {
        return targetDiscItemId;
    }

    public void setTargetDiscItemId(int targetDiscItemId) {
        this.targetDiscItemId = targetDiscItemId;
    }

    public String getTargetUoM() {
        return targetUoM;
    }

    public void setTargetUoM(String targetUoM) {
        this.targetUoM = targetUoM;
    }

    public String getTargetUoMCode() {
        return targetUoMCode;
    }

    public void setTargetUoMCode(String targetUoMCode) {
        this.targetUoMCode = targetUoMCode;
    }

    public int getTargetUoMId() {
        return targetUoMId;
    }

    public void setTargetUoMId(int targetUoMId) {
        this.targetUoMId = targetUoMId;
    }

    public int getTargetItemId() {
        return targetItemId;
    }

    public void setTargetItemId(int targetItemId) {
        this.targetItemId = targetItemId;
    }

    public String getTargetItemName() {
        return targetItemName;
    }

    public void setTargetItemName(String targetItemName) {
        this.targetItemName = targetItemName;
    }

    public String getTargetItemCode() {
        return targetItemCode;
    }

    public void setTargetItemCode(String targetItemCode) {
        this.targetItemCode = targetItemCode;
    }

    public boolean isItemEnableForDisc() {
        return itemEnableForDisc;
    }

    public void setItemEnableForDisc(boolean itemEnableForDisc) {
        this.itemEnableForDisc = itemEnableForDisc;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public void setIsTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }



    public int getPriceAppliedTypeId() {
        return priceAppliedTypeId;
    }

    public void setPriceAppliedTypeId(int priceAppliedTypeId) {
        this.priceAppliedTypeId = priceAppliedTypeId;
    }

    public int getOfferItemUoMId() {
        return offerItemUoMId;
    }

    public void setOfferItemUoMId(int offerItemUoMId) {
        this.offerItemUoMId = offerItemUoMId;
    }

    public int getOfferDiscItemId() {
        return offerDiscItemId;
    }

    public void setOfferDiscItemId(int offerDiscItemId) {
        this.offerDiscItemId = offerDiscItemId;
    }

    public String getOfferUoM() {
        return offerUoM;
    }

    public void setOfferUoM(String offerUoM) {
        this.offerUoM = offerUoM;
    }

    public String getOfferUoMCode() {
        return offerUoMCode;
    }

    public void setOfferUoMCode(String offerUoMCode) {
        this.offerUoMCode = offerUoMCode;
    }

    public int getOfferUoMId() {
        return offerUoMId;
    }

    public void setOfferUoMId(int offerUoMId) {
        this.offerUoMId = offerUoMId;
    }

    public int getOfferItemId() {
        return offerItemId;
    }

    public void setOfferItemId(int offerItemId) {
        this.offerItemId = offerItemId;
    }

    public String getOfferItemCode() {
        return offerItemCode;
    }

    public void setOfferItemCode(String offerItemCode) {
        this.offerItemCode = offerItemCode;
    }

    public String getOfferItemName() {
        return offerItemName;
    }

    public void setOfferItemName(String offerItemName) {
        this.offerItemName = offerItemName;
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

    public int getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(int customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }
}

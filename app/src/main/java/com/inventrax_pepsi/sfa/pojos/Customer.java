package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 3/9/2016.
 */
public class Customer{

    @SerializedName("OrderCap")
    private int orderCap;
    @SerializedName("AlternateEmail")
    private String alternateEmail;
    @SerializedName("DBA")
    private String dBA;
    @SerializedName("OrderCaps")
    private List<OrderCap> orderCaps;
    @SerializedName("Discounts")
    private List<Discount> discounts;
    @SerializedName("Coupons")
    private List<Coupon> coupons;
    @SerializedName("ItemPrices")
    private List<ItemPrice> itemPrices;
    @SerializedName("IsDeleted")
    private boolean isDeleted;
    @SerializedName("IsActive")
    private boolean isActive;
    @SerializedName("CustomerGroup")
    private String customerGroup;
    @SerializedName("OutletProfile")
    private OutletProfile outletProfile;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("MasterId")
    private int masterId;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustomerCode")
    private String customerCode;
    @SerializedName("ContactPersonName")
    private String contactPersonName;
    @SerializedName("MobileNo1")
    private String mobileNo1;
    @SerializedName("MobileNo2")
    private String mobileNo2;
    @SerializedName("Email")
    private String email;
    @SerializedName("CustomerGroupId")
    private int customerGroupId;
    @SerializedName("AuditInfo")
    private AuditInfo auditInfo;
    @SerializedName("AddressBook")
    private List<AddressBook> addressBook;
    @SerializedName("DistributorProfile")
    private DistributorProfile distributorProfile;
    @SerializedName("PlantProfile")
    private String plantProfile;
    @SerializedName("IsCreditAccount")
    private boolean isCreditAccount;
    @SerializedName("TIN")
    private String tIN;
    @SerializedName("BillingPriceTypeId")
    private int billingPriceTypeId;
    @SerializedName("AutoIncId")
    private int autoIncId;
    @SerializedName("CustomerAssets")
    private List<CustomerAsset> customerAssets;
    @SerializedName("CreditDueDays")
    private int creditDueDays;
    @SerializedName("CreditLimit")
    private double creditLimit;
    @SerializedName("IsAuditRequired")
    private boolean isAuditRequired;
    @SerializedName("CustomerTransaction")
    private CustomerTransaction customerTransaction;
    @SerializedName("ErpCode")
    private String erpCode;
    @SerializedName("CustomerInfo")
    private String customerInfo;
    @SerializedName("AssetCount")
    private int assetCount;


    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public int getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }

    public boolean isAuditRequired() {
        return isAuditRequired;
    }

    public void setAuditRequired(boolean auditRequired) {
        isAuditRequired = auditRequired;
    }

    public CustomerTransaction getCustomerTransaction() {
        return customerTransaction;
    }

    public void setCustomerTransaction(CustomerTransaction customerTransaction) {
        this.customerTransaction = customerTransaction;
    }

    public int getCreditDueDays() {
        return creditDueDays;
    }

    public void setCreditDueDays(int creditDueDays) {
        this.creditDueDays = creditDueDays;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public List<CustomerAsset> getCustomerAssets() {
        return customerAssets;
    }

    public void setCustomerAssets(List<CustomerAsset> customerAssets) {
        this.customerAssets = customerAssets;
    }

    public int getAutoIncId() {
        return autoIncId;
    }

    public void setAutoIncId(int autoIncId) {
        this.autoIncId = autoIncId;
    }

    public int getBillingPriceTypeId() {
        return billingPriceTypeId;
    }

    public void setBillingPriceTypeId(int billingPriceTypeId) {
        this.billingPriceTypeId = billingPriceTypeId;
    }

    public String gettIN() {
        return tIN;
    }

    public void settIN(String tIN) {
        this.tIN = tIN;
    }

    public boolean isCreditAccount() {
        return isCreditAccount;
    }

    public void setCreditAccount(boolean creditAccount) {
        isCreditAccount = creditAccount;
    }

    public int getOrderCap() {
        return orderCap;
    }

    public void setOrderCap(int orderCap) {
        this.orderCap = orderCap;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getdBA() {
        return dBA;
    }

    public void setdBA(String dBA) {
        this.dBA = dBA;
    }

    public List<OrderCap> getOrderCaps() {
        return orderCaps;
    }

    public void setOrderCaps(List<OrderCap> orderCaps) {
        this.orderCaps = orderCaps;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public List<ItemPrice> getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(List<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
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

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public OutletProfile getOutletProfile() {
        return outletProfile;
    }

    public void setOutletProfile(OutletProfile outletProfile) {
        this.outletProfile = outletProfile;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getMobileNo1() {
        return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        this.mobileNo1 = mobileNo1;
    }

    public String getMobileNo2() {
        return mobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        this.mobileNo2 = mobileNo2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(int customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public List<AddressBook> getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(List<AddressBook> addressBook) {
        this.addressBook = addressBook;
    }

    public DistributorProfile getDistributorProfile() {
        return distributorProfile;
    }

    public void setDistributorProfile(DistributorProfile distributorProfile) {
        this.distributorProfile = distributorProfile;
    }

    public String getPlantProfile() {
        return plantProfile;
    }

    public void setPlantProfile(String plantProfile) {
        this.plantProfile = plantProfile;
    }
}

package com.inventrax_pepsi.sfa.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 3/9/2016.
 */
public class DiscountMap {

    @SerializedName("DiscountMapId")
    private int discountMapId;
    @SerializedName("Key")
    private String key;
    @SerializedName("KeyId")
    private int keyId;
    @SerializedName("Value")
    private double value;
    @SerializedName("Validity")
    private String validity;
    @SerializedName("DistrictId")
    private int districtId;
    @SerializedName("CustomerId")
    private int customerId;
    @SerializedName("CustomerCode")
    private String customerCode;

    public int getDiscountMapId() {
        return discountMapId;
    }

    public void setDiscountMapId(int discountMapId) {
        this.discountMapId = discountMapId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
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
}

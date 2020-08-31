package com.inventrax_pepsi.database.pojos;

/**
 * Created by Naresh on 09-Mar-16.
 */
public interface ICustomer {
    // Table Names
    String TABLE_CUSTOMER = "SFA_CUSTOMER";
    // Common column names
    String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    String KEY_CUSTOMER_TYPE_ID = "CUSTOMER_TYPE_ID";
    String KEY_ROUTE_ID = "ROUTE_ID";
    String KEY_ORDER_CAP = "ORDER_CAP";
    String KEY_CUSTOMER_CODE = "CUSTOMER_CODE";
    String KEY_CUSTOMER_NAME = "CUSTOMER_NAME";
    String KEY_CUSTOMER_TYPE = "CUSTOMER_TYPE";
    String KEY_COMPLETE_JSON = "COMPLETE_JSON";
    String KEY_PRICE_JSON = "PRICE_JSON";
    String KEY_DISCOUNT_JSON = "DISCOUNT_JSON";
    String KEY_COUPON_JSON = "COUPON_JSON";
    String KEY_ROUTE_CODE = "ROUTE_CODE";
    String KEY_CREDIT_LIMIT_JSON = "CREDIT_LIMIT_JSON";
}

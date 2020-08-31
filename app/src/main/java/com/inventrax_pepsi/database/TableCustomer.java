package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableCustomer {

    // Table Names
    public static final String TABLE_CUSTOMER = "SFA_CUSTOMER";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID ="CUSTOMER_ID";
    private static final String KEY_CUSTOMER_TYPE_ID="CUSTOMER_TYPE_ID";
    private static final String KEY_ROUTE_ID="ROUTE_ID";
    private static final String KEY_ORDER_CAP="ORDER_CAP";
    private static final String KEY_CUSTOMER_CODE="CUSTOMER_CODE";
    private static final String KEY_CUSTOMER_NAME="CUSTOMER_NAME";
    private static final String KEY_CUSTOMER_TYPE="CUSTOMER_TYPE";
    private static final String KEY_COMPLETE_JSON="COMPLETE_JSON";
    private static final String KEY_PRICE_JSON="PRICE_JSON";
    private static final String KEY_DISCOUNT_JSON="DISCOUNT_JSON";
    private static final String KEY_COUPON_JSON="COUPON_JSON";
    private static final String KEY_ROUTE_CODE="ROUTE_CODE";
    private static final String KEY_CREDIT_LIMIT_JSON="CREDIT_LIMIT_JSON";
    private static final String KEY_SYNC_STATUS="SYNC_STATUS";
    private static final String KEY_IS_SCHEDULED_CUSTOMER="IS_SCHEDULED_CUSTOMER";
    private static final String KEY_IS_NEW_CUSTOMER = "IS_NEW_CUSTOMER";
    private static final String KEY_JSON_MESSAGE_ID="JSON_MESSAGE_ID";

    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER= "CREATE TABLE " +
            TABLE_CUSTOMER + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_CUSTOMER_TYPE_ID + " INTEGER," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_ORDER_CAP+ " INTEGER," +
            KEY_CUSTOMER_CODE + " TEXT UNIQUE," +
            KEY_CUSTOMER_NAME + " TEXT," +
            KEY_CUSTOMER_TYPE + " TEXT," +
            KEY_COMPLETE_JSON + " TEXT," +
            KEY_PRICE_JSON + " TEXT," +
            KEY_DISCOUNT_JSON + " TEXT," +
            KEY_COUPON_JSON + " TEXT," +
            KEY_ROUTE_CODE + " TEXT," +
            KEY_IS_SCHEDULED_CUSTOMER + " INTEGER , " +
            KEY_IS_NEW_CUSTOMER + " INTEGER , " +
            KEY_JSON_MESSAGE_ID+ " INTEGER , " +
            KEY_CREDIT_LIMIT_JSON + " TEXT ," +
            KEY_SYNC_STATUS + " INTEGER " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomer(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Customer
     */
    public long createCustomer(Customer customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customer.getCustomerId());
        values.put(KEY_CUSTOMER_TYPE_ID ,customer.getCustomerTypeId());
        values.put(KEY_ROUTE_ID ,customer.getRouteId());
        values.put(KEY_ORDER_CAP,customer.getOrderCap());
        values.put(KEY_CUSTOMER_CODE ,customer.getCustomerCode());
        values.put(KEY_CUSTOMER_NAME ,customer.getCustomerName());
        values.put(KEY_CUSTOMER_TYPE ,customer.getCustomerType());
        values.put(KEY_COMPLETE_JSON ,customer.getCompleteJSON());
        values.put(KEY_PRICE_JSON ,customer.getPriceJSON());
        values.put(KEY_DISCOUNT_JSON ,customer.getDiscountJSON());
        values.put(KEY_COUPON_JSON ,customer.getCouponJSON());
        values.put(KEY_ROUTE_CODE ,customer.getRouteNo());
        values.put(KEY_CREDIT_LIMIT_JSON ,customer.getCreditLimitJSON());
        values.put(KEY_SYNC_STATUS ,customer.getSyncStatus());
        values.put(KEY_IS_SCHEDULED_CUSTOMER,customer.getIsScheduledOutlet());
        values.put(KEY_IS_NEW_CUSTOMER,customer.getIsNewCustomer());
        values.put(KEY_JSON_MESSAGE_ID,customer.getJsonMessageAutoIncId());

        long customer_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER, null, values);

        return customer_id;
    }

    /**
     * Updating a customer
     */
    public int updateCustomer(Customer customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customer.getCustomerId());
        values.put(KEY_CUSTOMER_TYPE_ID ,customer.getCustomerTypeId());
        values.put(KEY_ROUTE_ID ,customer.getRouteId());
        values.put(KEY_ORDER_CAP,customer.getOrderCap());
        values.put(KEY_CUSTOMER_CODE ,customer.getCustomerCode());
        values.put(KEY_CUSTOMER_NAME ,customer.getCustomerName());
        values.put(KEY_CUSTOMER_TYPE ,customer.getCustomerType());
        values.put(KEY_COMPLETE_JSON ,customer.getCompleteJSON());
        values.put(KEY_PRICE_JSON ,customer.getPriceJSON());
        values.put(KEY_DISCOUNT_JSON ,customer.getDiscountJSON());
        values.put(KEY_COUPON_JSON ,customer.getCouponJSON());
        values.put(KEY_ROUTE_CODE ,customer.getRouteNo());
        values.put(KEY_CREDIT_LIMIT_JSON ,customer.getCreditLimitJSON());
        values.put(KEY_SYNC_STATUS ,customer.getSyncStatus());
        values.put(KEY_IS_SCHEDULED_CUSTOMER,customer.getIsScheduledOutlet());
        values.put(KEY_IS_NEW_CUSTOMER,customer.getIsNewCustomer());
        values.put(KEY_JSON_MESSAGE_ID,customer.getJsonMessageAutoIncId());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(customer.getAutoIncId())});
    }

    /**
     * Deleting a customer
     */
    public int deleteCustomer(long customer_id) {

       return writableSqLiteDatabase.delete(TABLE_CUSTOMER, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomers(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_CUSTOMER);
    }

    /**
     * get single customer
     */
    public Customer getCustomer(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id + "  AND  "  + KEY_IS_NEW_CUSTOMER + " = 0 "  ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        if (c != null && c.moveToFirst() ) {

            customer = new Customer();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
            customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
            customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
            customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
            customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
            customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
            customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
            customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
            customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
            customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

        }

        if (c!=null)
            c.close();

        return customer;
    }


    public Customer getCustomerByAutoIncId(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        if (c != null && c.moveToFirst() ) {

            customer = new Customer();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
            customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
            customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
            customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
            customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
            customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
            customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
            customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
            customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
            customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

        }

        if (c!=null)
            c.close();

        return customer;
    }

    /**
     * get single customer
     */
    public Customer getCustomer(String customerCode) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE "
                + KEY_CUSTOMER_CODE + " = '" + customerCode + "'" ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        if (c != null && c.moveToFirst() ) {

            customer = new Customer();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
            customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
            customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
            customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
            customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
            customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
            customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
            customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
            customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
            customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

        }

        if (c!=null)
            c.close();

        return customer;
    }


    public Customer getNewCustomer(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id + "  AND  "  + KEY_IS_NEW_CUSTOMER + " = 1 "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        if (c != null && c.moveToFirst() ) {

            customer = new Customer();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
            customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
            customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
            customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
            customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
            customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
            customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
            customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
            customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
            customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

        }

        if (c!=null)
            c.close();

        return customer;
    }



    /**
     * getting all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customerArrayList = new ArrayList<Customer>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + "  WHERE  "  + KEY_IS_NEW_CUSTOMER + " = 0 "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                customer = new Customer();

                customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
                customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
                customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
                customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
                customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
                customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
                customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

                customerArrayList.add(customer);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerArrayList;
    }

    public List<Customer> getAllNewCustomers() {
        List<Customer> customerArrayList = new ArrayList<Customer>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + "  WHERE  "  + KEY_IS_NEW_CUSTOMER + " = 1 "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                customer = new Customer();

                customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
                customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
                customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
                customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
                customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
                customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
                customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

                customerArrayList.add(customer);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerArrayList;
    }


    /**
     * getting all customers
     */
    public List<Customer> getAllCustomersByRoute(String routeCode) {

        List<Customer> customerArrayList = new ArrayList<Customer>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE  " +  KEY_ROUTE_CODE + " = '" + routeCode + "' AND " + KEY_IS_NEW_CUSTOMER + " = 0  ORDER BY  " + KEY_IS_SCHEDULED_CUSTOMER + " DESC " ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                customer = new Customer();

                customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
                customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
                customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
                customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
                customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
                customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
                customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

                customerArrayList.add(customer);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerArrayList;
    }

    public List<Customer> getAllNewCustomersByRoute(String routeCode) {

        List<Customer> customerArrayList = new ArrayList<Customer>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE  " +  KEY_ROUTE_CODE + " = '" + routeCode + "' AND " + KEY_IS_NEW_CUSTOMER + " = 1  ORDER BY  " + KEY_IS_SCHEDULED_CUSTOMER + " DESC " ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                customer = new Customer();

                customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
                customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
                customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
                customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
                customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
                customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
                customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

                customerArrayList.add(customer);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerArrayList;
    }


    public List<Customer> getAllCustomersByRoute(String routeCode, int isScheduled) {

        List<Customer> customerArrayList = new ArrayList<Customer>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER + " WHERE  " +  KEY_ROUTE_CODE + " = '" + routeCode + "'" + " AND  " + KEY_IS_SCHEDULED_CUSTOMER + " = " + isScheduled + " AND  " + KEY_IS_NEW_CUSTOMER + " = 0 "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Customer customer=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                customer = new Customer();

                customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customer.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customer.setOrderCap(c.getInt(c.getColumnIndex(KEY_ORDER_CAP)));
                customer.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                customer.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                customer.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                customer.setCompleteJSON(c.getString(c.getColumnIndex(KEY_COMPLETE_JSON)));
                customer.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                customer.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customer.setCouponJSON(c.getString(c.getColumnIndex(KEY_COUPON_JSON)));
                customer.setRouteNo(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                customer.setCreditLimitJSON(c.getString(c.getColumnIndex(KEY_CREDIT_LIMIT_JSON)));
                customer.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                customer.setIsScheduledOutlet(c.getInt(c.getColumnIndex(KEY_IS_SCHEDULED_CUSTOMER)));
                customer.setIsNewCustomer(c.getInt(c.getColumnIndex(KEY_IS_NEW_CUSTOMER)));
                customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));

                customerArrayList.add(customer);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerArrayList;
    }








}

package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.CustomerOrderHistory;

/**
 * Created by android on 3/31/2016.
 */
public class TableCustomerOrderHistory {


    // Table Names
    public static final String TABLE_CUSTOMER_ORDER_HISTORY = "SFA_CUSTOMER_ORDER_HISTORY";

    // Common column names
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_CUSTOMER_CODE = "CUSTOMER_CODE";
    private static final String KEY_CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String KEY_ROUTE_ID = "ROUTE_ID";
    private static final String KEY_ROUTE_CODE = "ROUTE_CODE";
    private static final String KEY_BRAND_JSON = "BRAND_JSON";
    private static final String KEY_BRAND_PACK_JSON = "BRAND_PACK_JSON";

    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER_ORDER_HISTORY= "CREATE TABLE " +
            TABLE_CUSTOMER_ORDER_HISTORY + "(" +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_CUSTOMER_CODE + " TEXT ," +
            KEY_CUSTOMER_NAME + " TEXT," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_ROUTE_CODE+ " TEXT," +
            KEY_BRAND_JSON + " TEXT ," +
            KEY_BRAND_PACK_JSON + " TEXT " + ")";


    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomerOrderHistory(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Customer Order History
     */
    public long createCustomerOrderHistory(CustomerOrderHistory customerOrderHistory) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customerOrderHistory.getCustomerId());
        values.put(KEY_CUSTOMER_CODE,customerOrderHistory.getCustomerCode());
        values.put(KEY_CUSTOMER_NAME,customerOrderHistory.getCustomerName());
        values.put(KEY_ROUTE_ID,customerOrderHistory.getRouteId());
        values.put(KEY_ROUTE_CODE,customerOrderHistory.getRouteCode());
        values.put(KEY_BRAND_JSON,customerOrderHistory.getBrandJSON());
        values.put(KEY_BRAND_PACK_JSON,customerOrderHistory.getBrandPackJSON());

        long customer_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER_ORDER_HISTORY, null, values);

        return customer_id;
    }


    /**
     * Updating a Customer Order History
     */
    public long updateCustomerOrderHistory(CustomerOrderHistory customerOrderHistory) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customerOrderHistory.getCustomerId());
        values.put(KEY_CUSTOMER_CODE,customerOrderHistory.getCustomerCode());
        values.put(KEY_CUSTOMER_NAME,customerOrderHistory.getCustomerName());
        values.put(KEY_ROUTE_ID,customerOrderHistory.getRouteId());
        values.put(KEY_ROUTE_CODE,customerOrderHistory.getRouteCode());
        values.put(KEY_BRAND_JSON,customerOrderHistory.getBrandJSON());
        values.put(KEY_BRAND_PACK_JSON,customerOrderHistory.getBrandPackJSON());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_ORDER_HISTORY, values, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customerOrderHistory.getCustomerId())});
    }


    /**
     * Deleting a customer order history
     */
    public int deleteCustomerOrderHistory(long customer_id) {

        return writableSqLiteDatabase.delete(TABLE_CUSTOMER_ORDER_HISTORY, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomerHistories(){
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_CUSTOMER_ORDER_HISTORY);
    }



    /**
     * get single customer order history
     */
    public CustomerOrderHistory getCustomerOrderHistory(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_ORDER_HISTORY + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerOrderHistory customerOrderHistory=null;

        if (c != null && c.moveToFirst() ) {

            customerOrderHistory = new CustomerOrderHistory();


            customerOrderHistory.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customerOrderHistory.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customerOrderHistory.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            customerOrderHistory.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            customerOrderHistory.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            customerOrderHistory.setBrandJSON(c.getString(c.getColumnIndex(KEY_BRAND_JSON)));
            customerOrderHistory.setBrandPackJSON(c.getString(c.getColumnIndex(KEY_BRAND_PACK_JSON)));

        }

        if (c!=null)
            c.close();

        return customerOrderHistory;
    }


    /**
     * Updating a Customer Order History
     */
    public long updateBrandJSON(CustomerOrderHistory customerOrderHistory) {

        ContentValues values = new ContentValues();

        values.put(KEY_BRAND_JSON,customerOrderHistory.getBrandJSON());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_ORDER_HISTORY, values, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customerOrderHistory.getCustomerId())});
    }


    /**
     * Updating a Customer Order History
     */

    public long updateBrandPackJSON(CustomerOrderHistory customerOrderHistory) {

        ContentValues values = new ContentValues();

        values.put(KEY_BRAND_PACK_JSON,customerOrderHistory.getBrandPackJSON());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_ORDER_HISTORY, values, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customerOrderHistory.getCustomerId())});
    }



}

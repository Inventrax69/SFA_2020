package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.CustomerPrice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/15/2016.
 */
public class TableCustomerPrice {

    // Table Names
    public static final String TABLE_CUSTOMER_PRICE = "SFA_CUSTOMER_PRICE";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID="CUSTOMER_ID";
    private static final String KEY_ROUTE_ID="ROUTE_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_ITEM_CODE="ITEM_CODE";
    private static final String KEY_UOM_ID="UOM_ID";
    private static final String KEY_UOM_CODE="UOM_CODE";
    private static final String KEY_JSON="JSON";
    private static final String KEY_TRADE_PRICE="TRADE_PRICE";
    private static final String KEY_MRP="MRP";
    private static final String KEY_IS_TRADE="IS_TRADE";


    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER_PRICE= "CREATE TABLE " +
            TABLE_CUSTOMER_PRICE + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_ROUTE_ID + " INTEGER ," +
            KEY_ITEM_ID+ " INTEGER ," +
            KEY_ITEM_CODE + " TEXT ," +
            KEY_UOM_ID + " INTEGER ," +
            KEY_UOM_CODE + " TEXT," +
            KEY_TRADE_PRICE + " REAL ," +
            KEY_MRP + " REAL ," +
            KEY_IS_TRADE + " INTEGER ," +
            KEY_JSON + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomerPrice(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Customer Price
     */
    public long createCustomerPrice(CustomerPrice customerPrice) {

        ContentValues values = new ContentValues();


        values.put(KEY_CUSTOMER_ID,customerPrice.getCustomerId());
        values.put(KEY_ROUTE_ID,customerPrice.getRouteId());
        values.put(KEY_ITEM_ID,customerPrice.getItemId());
        values.put(KEY_ITEM_CODE,customerPrice.getItemCode());
        values.put(KEY_UOM_ID,customerPrice.getUomId());
        values.put(KEY_UOM_CODE,customerPrice.getUomCode());
        values.put(KEY_JSON,customerPrice.getJSON());
        values.put(KEY_TRADE_PRICE,customerPrice.getTradePrice());
        values.put(KEY_IS_TRADE,customerPrice.getIsTrade());
        values.put(KEY_MRP,customerPrice.getMRP());


        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER_PRICE, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a customer price
     */
    public int updateCustomerPrice(CustomerPrice customerPrice) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customerPrice.getCustomerId());
        values.put(KEY_ROUTE_ID,customerPrice.getRouteId());
        values.put(KEY_ITEM_ID,customerPrice.getItemId());
        values.put(KEY_ITEM_CODE,customerPrice.getItemCode());
        values.put(KEY_UOM_ID,customerPrice.getUomId());
        values.put(KEY_UOM_CODE,customerPrice.getUomCode());
        values.put(KEY_JSON,customerPrice.getJSON());
        values.put(KEY_TRADE_PRICE,customerPrice.getTradePrice());
        values.put(KEY_IS_TRADE,customerPrice.getIsTrade());
        values.put(KEY_MRP,customerPrice.getMRP());


        return writableSqLiteDatabase.update(TABLE_CUSTOMER_PRICE, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(customerPrice.getAutoIncId())});
    }

    /**
     * Deleting a customer price
     */
    public int deleteCustomerPrice(long customer_id) {

        return writableSqLiteDatabase.delete(TABLE_CUSTOMER_PRICE, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomerPrices(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_CUSTOMER_PRICE);
    }


    /**
     * get single customer price
     */
    public CustomerPrice getCustomerPrice(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_PRICE + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerPrice customerPrice=null;

        if (c != null && c.moveToFirst()) {

            customerPrice = new CustomerPrice();

            customerPrice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customerPrice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customerPrice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customerPrice.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            customerPrice.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            customerPrice.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
            customerPrice.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
            customerPrice.setJSON(c.getString(c.getColumnIndex(KEY_JSON)));

            customerPrice.setTradePrice(c.getDouble(c.getColumnIndex(KEY_TRADE_PRICE)));
            customerPrice.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
            customerPrice.setMRP(c.getDouble(c.getColumnIndex(KEY_MRP)));

        }

        if (c!=null)
            c.close();

        return customerPrice;
    }


    /**
     * get single customer price
     */
    public List<CustomerPrice> getAllCustomerPrices() {

        List<CustomerPrice> customerPriceArrayList = new ArrayList<CustomerPrice>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_PRICE ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerPrice customerPrice=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {


                customerPrice = new CustomerPrice();

                customerPrice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customerPrice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customerPrice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customerPrice.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                customerPrice.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                customerPrice.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                customerPrice.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                customerPrice.setJSON(c.getString(c.getColumnIndex(KEY_JSON)));
                customerPrice.setTradePrice(c.getDouble(c.getColumnIndex(KEY_TRADE_PRICE)));
                customerPrice.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
                customerPrice.setMRP(c.getDouble(c.getColumnIndex(KEY_MRP)));

                customerPriceArrayList.add(customerPrice);

            }while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerPriceArrayList;
    }

    /**
     * get single customer price
     */
    public List<CustomerPrice> getAllCustomerPricesByCustomerId(int customerId,int item_id) {

        List<CustomerPrice> customerPriceArrayList = new ArrayList<CustomerPrice>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_PRICE + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customerId + " AND  " + KEY_ITEM_ID + " = " +item_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerPrice customerPrice=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {


                customerPrice = new CustomerPrice();

                customerPrice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customerPrice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customerPrice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customerPrice.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                customerPrice.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                customerPrice.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                customerPrice.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                customerPrice.setJSON(c.getString(c.getColumnIndex(KEY_JSON)));
                customerPrice.setTradePrice(c.getDouble(c.getColumnIndex(KEY_TRADE_PRICE)));
                customerPrice.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
                customerPrice.setMRP(c.getDouble(c.getColumnIndex(KEY_MRP)));

                customerPriceArrayList.add(customerPrice);

            }while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerPriceArrayList;
    }




}

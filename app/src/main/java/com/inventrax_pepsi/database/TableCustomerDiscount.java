package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.CustomerDiscount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/15/2016.
 */
public class TableCustomerDiscount {

    // Table Names
    public static final String TABLE_CUSTOMER_DISCOUNT = "SFA_CUSTOMER_DISCOUNT";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID="CUSTOMER_ID";
    private static final String KEY_ROUTE_ID="ROUTE_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_ITEM_CODE="ITEM_CODE";
    private static final String KEY_UOM_ID="UOM_ID";
    private static final String KEY_UOM_CODE="UOM_CODE";
    private static final String KEY_DISCOUNT_JSON="DISCOUNT_JSON";
    private static final String KEY_QUANTITY="QUANTITY";
    private static final String KEY_DISCOUNT_ID="DISCOUNT_ID";

    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER_DISCOUNT= "CREATE TABLE " +
            TABLE_CUSTOMER_DISCOUNT + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_ROUTE_ID + " INTEGER ," +
            KEY_ITEM_ID+ " INTEGER ," +
            KEY_DISCOUNT_ID+ " INTEGER ," +
            KEY_QUANTITY+ " INTEGER ," +
            KEY_ITEM_CODE + " TEXT ," +
            KEY_UOM_ID + " INTEGER ," +
            KEY_UOM_CODE + " TEXT," +
            KEY_DISCOUNT_JSON + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomerDiscount(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Customer Discount
     */
    public long createCustomerDiscount(CustomerDiscount customerDiscount) {

        ContentValues values = new ContentValues();


        values.put(KEY_CUSTOMER_ID,customerDiscount.getCustomerId());
        values.put(KEY_ROUTE_ID,customerDiscount.getRouteId());
        values.put(KEY_ITEM_ID,customerDiscount.getItemId());
        values.put(KEY_ITEM_CODE,customerDiscount.getItemCode());
        values.put(KEY_UOM_ID,customerDiscount.getUomId());
        values.put(KEY_UOM_CODE,customerDiscount.getUomCode());
        values.put(KEY_DISCOUNT_JSON,customerDiscount.getDiscountJSON());
        values.put(KEY_QUANTITY,customerDiscount.getQuantity());
        values.put(KEY_DISCOUNT_ID,customerDiscount.getDiscountId());



        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER_DISCOUNT, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a customer discount
     */
    public int updateCustomerDiscount(CustomerDiscount customerDiscount) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,customerDiscount.getCustomerId());
        values.put(KEY_ROUTE_ID,customerDiscount.getRouteId());
        values.put(KEY_ITEM_ID,customerDiscount.getItemId());
        values.put(KEY_ITEM_CODE,customerDiscount.getItemCode());
        values.put(KEY_UOM_ID,customerDiscount.getUomId());
        values.put(KEY_UOM_CODE,customerDiscount.getUomCode());
        values.put(KEY_DISCOUNT_JSON,customerDiscount.getDiscountJSON());
        values.put(KEY_QUANTITY,customerDiscount.getQuantity());
        values.put(KEY_DISCOUNT_ID,customerDiscount.getDiscountId());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_DISCOUNT, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(customerDiscount.getAutoIncId())});
    }

    /**
     * Deleting a customer discount
     */
    public int deleteCustomerDiscount(long customer_id) {

        return writableSqLiteDatabase.delete(TABLE_CUSTOMER_DISCOUNT, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomerDiscounts(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_CUSTOMER_DISCOUNT);
    }

    /**
     * get single customer discount
     */
    public CustomerDiscount getCustomerDiscount(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DISCOUNT + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerDiscount customerDiscount=null;

        if (c != null && c.moveToFirst() ) {


            customerDiscount = new CustomerDiscount();

            customerDiscount.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customerDiscount.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customerDiscount.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customerDiscount.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            customerDiscount.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            customerDiscount.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
            customerDiscount.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
            customerDiscount.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customerDiscount.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
            customerDiscount.setDiscountId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ID)));

        }

        if (c!=null)
            c.close();

        return customerDiscount;
    }


    /**
     * get single customer discount
     */
    public List<CustomerDiscount> getAllCustomerDiscounts() {

        List<CustomerDiscount> customerDiscountsArrayList = new ArrayList<CustomerDiscount>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DISCOUNT ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerDiscount customerDiscount=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {


            customerDiscount = new CustomerDiscount();

            customerDiscount.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customerDiscount.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customerDiscount.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            customerDiscount.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            customerDiscount.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            customerDiscount.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
            customerDiscount.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
            customerDiscount.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
            customerDiscount.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
            customerDiscount.setDiscountId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ID)));

            customerDiscountsArrayList.add(customerDiscount);

            }while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerDiscountsArrayList;
    }


    public List<CustomerDiscount> getAllCustomerDiscountsByCustomerId(int customerId) {

        List<CustomerDiscount> customerDiscountsArrayList = new ArrayList<CustomerDiscount>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DISCOUNT + " WHERE  " + KEY_CUSTOMER_ID + " = " +customerId  ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerDiscount customerDiscount=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {


                customerDiscount = new CustomerDiscount();

                customerDiscount.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customerDiscount.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customerDiscount.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customerDiscount.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                customerDiscount.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                customerDiscount.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                customerDiscount.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                customerDiscount.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customerDiscount.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
                customerDiscount.setDiscountId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ID)));

                customerDiscountsArrayList.add(customerDiscount);

            }while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerDiscountsArrayList;


    }


    public List<CustomerDiscount> getAllCustomerDiscountsByCustomerId(int customerId,int item_id) {

        List<CustomerDiscount> customerDiscountsArrayList = new ArrayList<CustomerDiscount>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_DISCOUNT + " WHERE  " + KEY_CUSTOMER_ID + " = " +customerId + " AND " + KEY_ITEM_ID + " = " + item_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerDiscount customerDiscount=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {


                customerDiscount = new CustomerDiscount();

                customerDiscount.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customerDiscount.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customerDiscount.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                customerDiscount.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                customerDiscount.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                customerDiscount.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                customerDiscount.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                customerDiscount.setDiscountJSON(c.getString(c.getColumnIndex(KEY_DISCOUNT_JSON)));
                customerDiscount.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
                customerDiscount.setDiscountId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ID)));

                customerDiscountsArrayList.add(customerDiscount);

            }while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return customerDiscountsArrayList;


    }

}

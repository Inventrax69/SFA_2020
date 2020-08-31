package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.CustomerReturn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 4/5/2016.
 */
public class TableCustomerReturn {


    // Table Names
    public static final String TABLE_CUSTOMER_RETURN = "SFA_CUSTOMER_RETURN";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_ROUTE_ID = "ROUTE_ID";
    private static final String KEY_JSON = "JSON";
    private static final String KEY_JSON_AUTO_INC_ID = "JSON_AUTO_INC_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER_RETURN = "CREATE TABLE " +
            TABLE_CUSTOMER_RETURN + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_JSON_AUTO_INC_ID + " INTEGER," +
            KEY_JSON + " TEXT " + " )";


    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomerReturn(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }


    /**
     * Creating a Customer Return
     */
    public long createCustomerReturn(CustomerReturn customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
        values.put(KEY_JSON, customer.getJson());
        values.put(KEY_JSON_AUTO_INC_ID, customer.getJsonMessageAutoIncId());
        values.put(KEY_ROUTE_ID, customer.getRouteId());

        long customer_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER_RETURN, null, values);

        return customer_id;
    }


    /**
     * Creating a Customer Return
     */
    public long updateCustomerReturn(CustomerReturn customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
        values.put(KEY_JSON, customer.getJson());
        values.put(KEY_JSON_AUTO_INC_ID, customer.getJsonMessageAutoIncId());
        values.put(KEY_ROUTE_ID, customer.getRouteId());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_RETURN, values, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer.getAutoIncId())});
    }

    /**
     * Deleting a customer return
     */
    public int deleteCustomerReturn(long customer_id) {

        return writableSqLiteDatabase.delete(TABLE_CUSTOMER_RETURN, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomerReturns() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_CUSTOMER_RETURN);
    }

    /**
     * get single customer return
     */
    public CustomerReturn getCustomerReturn(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_RETURN + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id + " ORDER BY  " + KEY_AUTO_INC_ID + " DESC ";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerReturn customer = null;

        if (c != null && c.moveToFirst()) {

            customer = new CustomerReturn();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_AUTO_INC_ID)));
            customer.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));

        }

        if (c != null)
            c.close();

        return customer;
    }


    public List<CustomerReturn> getAllCustomerReturns() {

        List<CustomerReturn> customerReturnList = new ArrayList<CustomerReturn>();

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_RETURN;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerReturn customerReturn = null;

        if (c != null && c.moveToFirst()) {

            do {

                customerReturn = new CustomerReturn();

                customerReturn.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                customerReturn.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_AUTO_INC_ID)));
                customerReturn.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
                customerReturn.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                customerReturn.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));

                customerReturnList.add(customerReturn);

            } while (c.moveToNext());

        }

        if (c != null)
            c.close();

        return customerReturnList;


    }


}

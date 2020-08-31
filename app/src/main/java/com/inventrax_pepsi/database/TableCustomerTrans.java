package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.CustomerTrans;

/**
 * Created by android on 6/15/2016.
 */
public class TableCustomerTrans {

    // Table Names
    public static final String TABLE_CUSTOMER_TRANS = "SFA_CUSTOMER_TRANS";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_AMOUNT = "AMOUNT";
    private static final String KEY_JSON = "JSON";
    private static final String KEY_JSON_AUTO_INC_ID = "JSON_AUTO_INC_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_CUSTOMER_TRANS = "CREATE TABLE " +
            TABLE_CUSTOMER_TRANS + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_AMOUNT + " REAL," +
            KEY_JSON_AUTO_INC_ID + " INTEGER," +
            KEY_JSON + " TEXT " + " )";


    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableCustomerTrans(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }


    /**
     * Creating a Customer Trans
     */
    public long createCustomerTrans(CustomerTrans customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
        values.put(KEY_JSON, customer.getJson());
        values.put(KEY_JSON_AUTO_INC_ID, customer.getJsonMessageAutoIncId());
        values.put(KEY_AMOUNT, customer.getAmount());

        long customer_id = writableSqLiteDatabase.insert(TABLE_CUSTOMER_TRANS, null, values);

        return customer_id;
    }


    /**
     * Creating a Customer Trans
     */
    public long updateCustomerTrans(CustomerTrans customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
        values.put(KEY_JSON, customer.getJson());
        values.put(KEY_JSON_AUTO_INC_ID, customer.getJsonMessageAutoIncId());
        values.put(KEY_AMOUNT, customer.getAmount());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_TRANS, values, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer.getCustomerId())});
    }


    /**
     * Creating a Customer Trans
     */
    public long updateCustomerTransByAutoIncId(CustomerTrans customer) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
        values.put(KEY_JSON, customer.getJson());
        values.put(KEY_JSON_AUTO_INC_ID, customer.getJsonMessageAutoIncId());
        values.put(KEY_AMOUNT, customer.getAmount());

        return writableSqLiteDatabase.update(TABLE_CUSTOMER_TRANS, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(customer.getAutoIncId())});
    }


    /**
     * Deleting a customer Trans
     */
    public int deleteCustomerTrans(long customer_id) {

        return writableSqLiteDatabase.delete(TABLE_CUSTOMER_TRANS, KEY_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer_id)});
    }

    public void deleteAllCustomerTrans() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_CUSTOMER_TRANS);
    }


    /**
     * get single customer return
     */
    public CustomerTrans getCustomerTrans(long customer_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_TRANS + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id + " ORDER BY  " + KEY_AUTO_INC_ID + " DESC ";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        CustomerTrans customer = null;

        if (c != null && c.moveToFirst()) {

            customer = new CustomerTrans();

            customer.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            customer.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_AUTO_INC_ID)));
            customer.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
            customer.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            customer.setAmount(c.getDouble(c.getColumnIndex(KEY_AMOUNT)));

        }

        if (c != null)
            c.close();

        return customer;
    }

    /**
     * get single customer return
     */
    public double getCustomerPaidAmount(long customer_id) {

        String selectQuery = "SELECT  SUM("+KEY_AMOUNT+") AS "+ KEY_AMOUNT +" FROM " + TABLE_CUSTOMER_TRANS + " WHERE "
                + KEY_CUSTOMER_ID + " = " + customer_id + " ORDER BY  " + KEY_AUTO_INC_ID + " DESC ";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        double amount = 0;

        if (c != null && c.moveToFirst()) {
            amount=c.getDouble(c.getColumnIndex(KEY_AMOUNT));
        }

        if (c != null)
            c.close();

        return amount;
    }




}

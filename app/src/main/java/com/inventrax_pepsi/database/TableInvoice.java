package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.DeliveryInvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 3/25/2016.
 */
public class TableInvoice {

    // Table Names
    public static final String TABLE_INVOICE = "SFA_INVOICE";

    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_INVOICE_ID = "INVOICE_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_ROUTE_ID = "ROUTE_ID";
    private static final String KEY_STATUS = "STATUS";
    private static final String KEY_INVOICE_NO = "INVOICE_NO";
    private static final String KEY_ORDER_NO = "ORDER_NO";
    private static final String KEY_ORDER_ID = "ORDER_ID";
    private static final String KEY_CUSTOMER_CODE = "CUSTOMER_CODE";
    private static final String KEY_ROUTE_CODE = "ROUTE_CODE";
    private static final String KEY_CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String KEY_INVOICE_JSON = "INVOICE_JSON";
    private static final String KEY_INVOICE_AMOUNT = "INVOICE_AMOUNT";
    private static final String KEY_JSON_MESSAGE_AUTO_INC_ID="JSON_MESSAGE_AUTO_INC_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_INVOICE = "CREATE TABLE " +
            TABLE_INVOICE + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_INVOICE_ID + " INTEGER ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_STATUS + " INTEGER," +
            KEY_JSON_MESSAGE_AUTO_INC_ID + " INTEGER," +
            KEY_INVOICE_NO + " TEXT," +
            KEY_ORDER_NO + " TEXT," +
            KEY_ORDER_ID + " INTEGER," +
            KEY_CUSTOMER_CODE + " TEXT," +
            KEY_ROUTE_CODE + " TEXT," +
            KEY_CUSTOMER_NAME + " TEXT," +
            KEY_INVOICE_JSON + " TEXT," +
            KEY_INVOICE_AMOUNT + " REAL " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableInvoice(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }


    /**
     * Creating a invoice
     */
    public long createInvoice(DeliveryInvoice deliveryInvoice) {

        ContentValues values = new ContentValues();


        values.put(KEY_INVOICE_ID, deliveryInvoice.getInvoiceId());
        values.put(KEY_CUSTOMER_ID, deliveryInvoice.getCustomerId());
        values.put(KEY_ROUTE_ID, deliveryInvoice.getRouteId());
        values.put(KEY_STATUS, deliveryInvoice.getStatus());
        values.put(KEY_INVOICE_NO, deliveryInvoice.getInvoiceNumber());
        values.put(KEY_ORDER_NO, deliveryInvoice.getOrderNumber());
        values.put(KEY_ORDER_ID, deliveryInvoice.getOrderId());
        values.put(KEY_CUSTOMER_CODE, deliveryInvoice.getCustomerCode());
        values.put(KEY_ROUTE_CODE, deliveryInvoice.getRouteCode());
        values.put(KEY_CUSTOMER_NAME, deliveryInvoice.getCustomerName());
        values.put(KEY_INVOICE_JSON, deliveryInvoice.getInvoiceJSON());
        values.put(KEY_INVOICE_AMOUNT, deliveryInvoice.getInvoiceAmount());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,deliveryInvoice.getJsonMessageAutoIncId());


        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_INVOICE, null, values);

        return auto_inc_id;
    }


    /**
     * Updating a invoice
     */
    public int updateInvoice(DeliveryInvoice deliveryInvoice) {

        ContentValues values = new ContentValues();

        values.put(KEY_INVOICE_ID, deliveryInvoice.getInvoiceId());
        values.put(KEY_CUSTOMER_ID, deliveryInvoice.getCustomerId());
        values.put(KEY_ROUTE_ID, deliveryInvoice.getRouteId());
        values.put(KEY_STATUS, deliveryInvoice.getStatus());
        values.put(KEY_INVOICE_NO, deliveryInvoice.getInvoiceNumber());
        values.put(KEY_ORDER_NO, deliveryInvoice.getOrderNumber());
        values.put(KEY_ORDER_ID, deliveryInvoice.getOrderId());
        values.put(KEY_CUSTOMER_CODE, deliveryInvoice.getCustomerCode());
        values.put(KEY_ROUTE_CODE, deliveryInvoice.getRouteCode());
        values.put(KEY_CUSTOMER_NAME, deliveryInvoice.getCustomerName());
        values.put(KEY_INVOICE_JSON, deliveryInvoice.getInvoiceJSON());
        values.put(KEY_INVOICE_AMOUNT, deliveryInvoice.getInvoiceAmount());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,deliveryInvoice.getJsonMessageAutoIncId());

        return writableSqLiteDatabase.update(TABLE_INVOICE, values, KEY_INVOICE_ID + " = ?",
                new String[]{String.valueOf(deliveryInvoice.getInvoiceId())});
    }


    /**
     * Updating a invoice
     */
    public int updateInvoiceJSON(DeliveryInvoice deliveryInvoice) {

        ContentValues values = new ContentValues();

        values.put(KEY_INVOICE_JSON, deliveryInvoice.getInvoiceJSON());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,deliveryInvoice.getJsonMessageAutoIncId());

        return writableSqLiteDatabase.update(TABLE_INVOICE, values, KEY_INVOICE_ID + " = ?",
                new String[]{String.valueOf(deliveryInvoice.getInvoiceId())});
    }

    /**
     * Updating a invoice
     */
    public int updateInvoiceStatus(DeliveryInvoice deliveryInvoice) {

        ContentValues values = new ContentValues();

        values.put(KEY_INVOICE_JSON, deliveryInvoice.getInvoiceJSON());

        return writableSqLiteDatabase.update(TABLE_INVOICE, values, KEY_INVOICE_ID + " = ?",
                new String[]{String.valueOf(deliveryInvoice.getInvoiceId())});
    }


    /**
     * Deleting a invoice
     */
    public int deleteInvoice(long invoice_id) {

        return writableSqLiteDatabase.delete(TABLE_INVOICE, KEY_INVOICE_ID + " = ?",
                new String[]{String.valueOf(invoice_id)});
    }

    public void deleteAllInvoices() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_INVOICE);
    }


    /**
     * get single invoice
     */
    public DeliveryInvoice getInvoice(long invoice_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_INVOICE + " WHERE "
                + KEY_INVOICE_ID + " = " + invoice_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        DeliveryInvoice deliveryInvoice = null;

        if (c != null && c.moveToFirst()) {

            deliveryInvoice = new DeliveryInvoice();

            deliveryInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            deliveryInvoice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            deliveryInvoice.setInvoiceId(c.getInt(c.getColumnIndex(KEY_INVOICE_ID)));
            deliveryInvoice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            deliveryInvoice.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));
            deliveryInvoice.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
            deliveryInvoice.setInvoiceAmount(c.getDouble(c.getColumnIndex(KEY_INVOICE_AMOUNT)));
            deliveryInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
            deliveryInvoice.setOrderNumber(c.getString(c.getColumnIndex(KEY_ORDER_NO)));
            deliveryInvoice.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            deliveryInvoice.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            deliveryInvoice.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            deliveryInvoice.setInvoiceJSON(c.getString(c.getColumnIndex(KEY_INVOICE_JSON)));
            deliveryInvoice.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

        }

        if (c != null)
            c.close();

        return deliveryInvoice;
    }

    /**
     * get single invoice
     */
    public DeliveryInvoice getInvoiceByNumber(String invoiceNumber) {

        String selectQuery = "SELECT  * FROM " + TABLE_INVOICE + " WHERE "
                + KEY_INVOICE_NO + " = '" + invoiceNumber + "'";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        DeliveryInvoice deliveryInvoice = null;

        if (c != null && c.moveToFirst()) {

            deliveryInvoice = new DeliveryInvoice();

            deliveryInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            deliveryInvoice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            deliveryInvoice.setInvoiceId(c.getInt(c.getColumnIndex(KEY_INVOICE_ID)));
            deliveryInvoice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            deliveryInvoice.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));
            deliveryInvoice.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
            deliveryInvoice.setInvoiceAmount(c.getDouble(c.getColumnIndex(KEY_INVOICE_AMOUNT)));
            deliveryInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
            deliveryInvoice.setOrderNumber(c.getString(c.getColumnIndex(KEY_ORDER_NO)));
            deliveryInvoice.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            deliveryInvoice.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
            deliveryInvoice.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            deliveryInvoice.setInvoiceJSON(c.getString(c.getColumnIndex(KEY_INVOICE_JSON)));
            deliveryInvoice.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

        }

        if (c != null)
            c.close();

        return deliveryInvoice;
    }


    /**
     * getting all invoices
     */
    public List<DeliveryInvoice> getAllInvoices() {

        List<DeliveryInvoice> deliveryInvoices = new ArrayList<DeliveryInvoice>();
        String selectQuery = "SELECT  * FROM " + TABLE_INVOICE;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        DeliveryInvoice deliveryInvoice = null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                deliveryInvoice = new DeliveryInvoice();

                deliveryInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                deliveryInvoice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                deliveryInvoice.setInvoiceId(c.getInt(c.getColumnIndex(KEY_INVOICE_ID)));
                deliveryInvoice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                deliveryInvoice.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));
                deliveryInvoice.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
                deliveryInvoice.setInvoiceAmount(c.getDouble(c.getColumnIndex(KEY_INVOICE_AMOUNT)));
                deliveryInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
                deliveryInvoice.setOrderNumber(c.getString(c.getColumnIndex(KEY_ORDER_NO)));
                deliveryInvoice.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                deliveryInvoice.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                deliveryInvoice.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                deliveryInvoice.setInvoiceJSON(c.getString(c.getColumnIndex(KEY_INVOICE_JSON)));
                deliveryInvoice.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

                deliveryInvoices.add(deliveryInvoice);

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return deliveryInvoices;
    }


    /**
     * getting all invoices by route
     */
    public List<DeliveryInvoice> getAllInvoicesByRoute(String routeCode,int customer_id) {

        List<DeliveryInvoice> deliveryInvoices = new ArrayList<DeliveryInvoice>();

        String selectQuery = "SELECT  * FROM " + TABLE_INVOICE + " WHERE   (''='"+ routeCode +"' OR "+KEY_ROUTE_CODE+"='"+ routeCode +"') AND (0= "+customer_id+ " OR  "+ KEY_CUSTOMER_ID + " = "+customer_id+" )";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        DeliveryInvoice deliveryInvoice = null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                deliveryInvoice = new DeliveryInvoice();

                deliveryInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                deliveryInvoice.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                deliveryInvoice.setInvoiceId(c.getInt(c.getColumnIndex(KEY_INVOICE_ID)));
                deliveryInvoice.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                deliveryInvoice.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));
                deliveryInvoice.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
                deliveryInvoice.setInvoiceAmount(c.getDouble(c.getColumnIndex(KEY_INVOICE_AMOUNT)));
                deliveryInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
                deliveryInvoice.setOrderNumber(c.getString(c.getColumnIndex(KEY_ORDER_NO)));
                deliveryInvoice.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                deliveryInvoice.setCustomerName(c.getString(c.getColumnIndex(KEY_CUSTOMER_NAME)));
                deliveryInvoice.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                deliveryInvoice.setInvoiceJSON(c.getString(c.getColumnIndex(KEY_INVOICE_JSON)));
                deliveryInvoice.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

                deliveryInvoices.add(deliveryInvoice);

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return deliveryInvoices;
    }
}

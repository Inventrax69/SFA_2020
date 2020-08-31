package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.ReadySaleInvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 4/25/2016.
 */
public class TableReadySaleInvoice {

    // Table Names
    public static final String TABLE_READY_SALE_INVOICE = "SFA_READY_SALE_INVOICE";

    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_INVOICE_NO = "INVOICE_NO";
    private static final String KEY_JSON = "INVOICE_JSON";
    private static final String KEY_JSON_MESSAGE_AUTO_INC_ID="JSON_MESSAGE_AUTO_INC_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_READY_SALE_INVOICE = "CREATE TABLE " +
            TABLE_READY_SALE_INVOICE + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_JSON_MESSAGE_AUTO_INC_ID + " INTEGER ," +
            KEY_JSON + " TEXT," +
            KEY_INVOICE_NO + " TEXT " + " )";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableReadySaleInvoice(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {

        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;

    }

    /**
     * Creating a ready sale invoice
     */
    public long createReadySaleInvoice(ReadySaleInvoice readySaleInvoice) {

        ContentValues values = new ContentValues();

        values.put(KEY_INVOICE_NO, readySaleInvoice.getInvoiceNumber());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,readySaleInvoice.getJsonMessageAutoIncId());
        values.put(KEY_JSON,readySaleInvoice.getJson());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_READY_SALE_INVOICE, null, values);

        return auto_inc_id;

    }


    /**
     * Updating a ready sale invoice
     */
    public long updateReadySaleInvoice(ReadySaleInvoice readySaleInvoice) {

        ContentValues values = new ContentValues();

        values.put(KEY_INVOICE_NO, readySaleInvoice.getInvoiceNumber());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,readySaleInvoice.getJsonMessageAutoIncId());
        values.put(KEY_JSON,readySaleInvoice.getJson());

        return writableSqLiteDatabase.update(TABLE_READY_SALE_INVOICE, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(readySaleInvoice.getAutoIncId())});

    }


    /**
     * Deleting a ready sale invoice
     */
    public int deleteReadySaleInvoice(long invoice_id) {

        return writableSqLiteDatabase.delete(TABLE_READY_SALE_INVOICE, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(invoice_id)});
    }

    public void deleteAllReadySaleInvoices() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_READY_SALE_INVOICE);
    }


    public  ReadySaleInvoice getReadySaleInvoice(int auto_inc_id){

        String selectQuery = "SELECT  * FROM " + TABLE_READY_SALE_INVOICE + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        ReadySaleInvoice readySaleInvoice = null;

        if (c != null && c.moveToFirst()) {

            readySaleInvoice = new ReadySaleInvoice();

            readySaleInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            readySaleInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
            readySaleInvoice.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
            readySaleInvoice.setJsonMessageAutoIncId(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID));

        }

        if (c != null)
            c.close();

        return readySaleInvoice;

    }


    public  ReadySaleInvoice getReadySaleInvoice(String invoiceNumber){

        String selectQuery = "SELECT  * FROM " + TABLE_READY_SALE_INVOICE + " WHERE "
                + KEY_INVOICE_NO + " = '" + invoiceNumber + "'";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        ReadySaleInvoice readySaleInvoice = null;

        if (c != null && c.moveToFirst()) {

            readySaleInvoice = new ReadySaleInvoice();

            readySaleInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            readySaleInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
            readySaleInvoice.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
            readySaleInvoice.setJsonMessageAutoIncId(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID));

        }

        if (c != null)
            c.close();

        return readySaleInvoice;

    }


    public List<ReadySaleInvoice> getAllReadySaleInvoices(){

        List<ReadySaleInvoice> readySaleInvoices=new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_READY_SALE_INVOICE ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        ReadySaleInvoice readySaleInvoice = null;

        if (c != null && c.moveToFirst()) {

            do {

                readySaleInvoice = new ReadySaleInvoice();

                readySaleInvoice.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                readySaleInvoice.setInvoiceNumber(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
                readySaleInvoice.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
                readySaleInvoice.setJsonMessageAutoIncId(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID));

                readySaleInvoices.add(readySaleInvoice);

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return readySaleInvoices;

    }



}

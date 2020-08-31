package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.AssetCapture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 4/19/2016.
 */
public class TableAssetCapture {

    // Table Names
    public static final String TABLE_ASSET_CAPTURE = "SFA_ASSET_CAPTURE";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_ASSET_ID = "ASSET_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_JSON_MESSAGE_ID = "JSON_MESSAGE_ID";
    private static final String KEY_QR_CODE = "QR_CODE";
    private static final String KEY_JSON = "JSON";


    // Table Create Statements
    public static final String CREATE_TABLE_ASSET_CAPTURE= "CREATE TABLE " +
            TABLE_ASSET_CAPTURE + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_QR_CODE + " TEXT," +
            KEY_JSON_MESSAGE_ID + " INTEGER," +
            KEY_ASSET_ID + " INTEGER," +
            KEY_JSON + " TEXT " +  ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableAssetCapture(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Asset Capture
     */
    public long createAssetCapture(AssetCapture assetCapture) {

        ContentValues values = new ContentValues();

        values.put(KEY_QR_CODE,assetCapture.getQRCode());
        values.put(KEY_CUSTOMER_ID,assetCapture.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetCapture.getJsonMessageAutoIncId());
        values.put(KEY_ASSET_ID,assetCapture.getAssetId());
        values.put(KEY_JSON,assetCapture.getJson());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ASSET_CAPTURE, null, values);



        return auto_inc_id;
    }


    /**
     * Creating a Asset Capture
     */
    public long updateAssetCapture(AssetCapture assetCapture) {

        ContentValues values = new ContentValues();

        values.put(KEY_QR_CODE,assetCapture.getQRCode());
        values.put(KEY_CUSTOMER_ID,assetCapture.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetCapture.getJsonMessageAutoIncId());
        values.put(KEY_ASSET_ID,assetCapture.getAssetId());
        values.put(KEY_JSON,assetCapture.getJson());

        return writableSqLiteDatabase.update(TABLE_ASSET_CAPTURE, values, KEY_QR_CODE + " = ?",
                new String[]{String.valueOf(assetCapture.getQRCode())});
    }


    /**
     * Deleting a asset capture
     */
    public int deleteAssetCapture(String QR_CODE) {

        return writableSqLiteDatabase.delete(TABLE_ASSET_CAPTURE, KEY_QR_CODE+ " = ?",
                new String[]{String.valueOf(QR_CODE)});
    }

    public void deleteAllAssetCaptures(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ASSET_CAPTURE);
    }


    /**
     * get single asset capture
     */
    public AssetCapture getAssetCapture(String qr_code) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_CAPTURE + " WHERE "
                + KEY_QR_CODE + " = '" + qr_code + "'";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetCapture assetCapture=null;

        if (c != null && c.moveToFirst() ) {

            assetCapture=new AssetCapture();

            assetCapture.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetCapture.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetCapture.setAssetId(c.getInt(c.getColumnIndex(KEY_ASSET_ID)));
            assetCapture.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetCapture.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
            assetCapture.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
        }

        if (c!=null)
            c.close();

        return assetCapture;
    }


    /**
     * get single asset capture
     */
    public AssetCapture getAssetCapture(int auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_CAPTURE + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetCapture assetCapture=null;

        if (c != null && c.moveToFirst() ) {

            assetCapture=new AssetCapture();

            assetCapture.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetCapture.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetCapture.setAssetId(c.getInt(c.getColumnIndex(KEY_ASSET_ID)));
            assetCapture.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetCapture.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
            assetCapture.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
        }

        if (c!=null)
            c.close();

        return assetCapture;
    }



    /**
     * getting all asset captures
     */
    public List<AssetCapture> getAllAssetCaptures() {
        List<AssetCapture> assetCaptures= new ArrayList<AssetCapture>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_CAPTURE;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetCapture assetCapture=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                assetCapture=new AssetCapture();

                assetCapture.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                assetCapture.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                assetCapture.setAssetId(c.getInt(c.getColumnIndex(KEY_ASSET_ID)));
                assetCapture.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
                assetCapture.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
                assetCapture.setJson(c.getString(c.getColumnIndex(KEY_JSON)));

                assetCaptures.add(assetCapture);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return assetCaptures;
    }


    /**
     * getting all asset captures
     */
    public List<AssetCapture> getAllAssetCapturesByCustomerId(int customer_id) {
        List<AssetCapture> assetCaptures= new ArrayList<AssetCapture>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_CAPTURE +   " WHERE " + KEY_CUSTOMER_ID + " =  " +customer_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetCapture assetCapture=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                assetCapture=new AssetCapture();

                assetCapture.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                assetCapture.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                assetCapture.setAssetId(c.getInt(c.getColumnIndex(KEY_ASSET_ID)));
                assetCapture.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
                assetCapture.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
                assetCapture.setJson(c.getString(c.getColumnIndex(KEY_JSON)));

                assetCaptures.add(assetCapture);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return assetCaptures;
    }




}

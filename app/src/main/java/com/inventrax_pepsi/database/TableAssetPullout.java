package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.AssetPullout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 5/7/2016.
 */
public class TableAssetPullout {

    // Table Names
    public static final String TABLE_ASSET_PULLOUT = "SFA_ASSET_PULLOUT";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_JSON_MESSAGE_ID = "JSON_MESSAGE_ID";
    private static final String KEY_QR_CODE = "QR_CODE";
    private static final String KEY_JSON = "JSON";


    // Table Create Statements
    public static final String CREATE_TABLE_ASSET_PULLOUT = "CREATE TABLE " +
            TABLE_ASSET_PULLOUT + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_QR_CODE + " TEXT," +
            KEY_JSON_MESSAGE_ID + " INTEGER," +
            KEY_JSON + " TEXT " +  ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableAssetPullout(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Asset Pullout
     */
    public long createAssetPullout(AssetPullout assetPullout) {

        ContentValues values = new ContentValues();

        values.put(KEY_QR_CODE,assetPullout.getQRCode());
        values.put(KEY_CUSTOMER_ID,assetPullout.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetPullout.getJsonMessageAutoIncId());
        values.put(KEY_JSON,assetPullout.getJson());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ASSET_PULLOUT, null, values);

        return auto_inc_id;
    }


    /**
     * Creating a Asset Pullout
     */
    public long updateAssetPullout(AssetPullout assetPullout) {

        ContentValues values = new ContentValues();

        values.put(KEY_QR_CODE,assetPullout.getQRCode());
        values.put(KEY_CUSTOMER_ID,assetPullout.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetPullout.getJsonMessageAutoIncId());
        values.put(KEY_JSON,assetPullout.getJson());

        return writableSqLiteDatabase.update(TABLE_ASSET_PULLOUT, values, KEY_QR_CODE + " = ?",
                new String[]{String.valueOf(assetPullout.getQRCode())});
    }


    /**
     * Deleting a asset pullout
     */
    public int deleteAssetPullout(String QR_CODE) {

        return writableSqLiteDatabase.delete(TABLE_ASSET_PULLOUT, KEY_QR_CODE+ " = ?",
                new String[]{String.valueOf(QR_CODE)});
    }

    public void deleteAllAssetPullouts(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ASSET_PULLOUT);
    }


    /**
     * get single asset pullout
     */
    public AssetPullout getAssetPullout(String qr_code) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_PULLOUT + " WHERE "
                + KEY_QR_CODE + " = '" + qr_code + "'";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetPullout assetPullout=null;

        if (c != null && c.moveToFirst() ) {

            assetPullout=new AssetPullout();

            assetPullout.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetPullout.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetPullout.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetPullout.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
            assetPullout.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
        }

        if (c!=null)
            c.close();

        return assetPullout;
    }

    /**
     * get single asset pullout
     */
    public AssetPullout getAssetPullout(int auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_PULLOUT + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetPullout assetPullout=null;

        if (c != null && c.moveToFirst() ) {

            assetPullout=new AssetPullout();

            assetPullout.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetPullout.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetPullout.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetPullout.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
            assetPullout.setJson(c.getString(c.getColumnIndex(KEY_JSON)));
        }

        if (c!=null)
            c.close();

        return assetPullout;
    }


    /**
     * getting all asset pullouts
     */
    public List<AssetPullout> getAllAssetPulloutsByCustomerId(int customer_id) {
        List<AssetPullout> assetPullouts= new ArrayList<AssetPullout>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_PULLOUT +   " WHERE " + KEY_CUSTOMER_ID + " =  " +customer_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetPullout assetPullout=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                assetPullout=new AssetPullout();

                assetPullout.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                assetPullout.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                assetPullout.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
                assetPullout.setQRCode(c.getString(c.getColumnIndex(KEY_QR_CODE)));
                assetPullout.setJson(c.getString(c.getColumnIndex(KEY_JSON)));

                assetPullouts.add(assetPullout);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return assetPullouts;
    }





}

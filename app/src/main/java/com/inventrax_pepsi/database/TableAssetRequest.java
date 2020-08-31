package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.AssetRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 5/7/2016.
 */
public class TableAssetRequest {

    // Table Names
    public static final String TABLE_ASSET_REQUEST = "SFA_ASSET_REQUEST";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_JSON_MESSAGE_ID = "JSON_MESSAGE_ID";
    private static final String KEY_JSON = "JSON";

    // Table Create Statements
    public static final String CREATE_TABLE_ASSET_REQUEST = "CREATE TABLE " +
            TABLE_ASSET_REQUEST + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_JSON_MESSAGE_ID + " INTEGER," +
            KEY_JSON + " TEXT " +  ")";


    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableAssetRequest(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Asset Request
     */
    public long createAssetRequest(AssetRequest assetRequest) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,assetRequest.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetRequest.getJsonMessageAutoIncId());
        values.put(KEY_JSON,assetRequest.getJson());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ASSET_REQUEST, null, values);

        return auto_inc_id;
    }


    /**
     * Creating a Asset Request
     */
    public long updateAssetRequest(AssetRequest assetRequest) {

        ContentValues values = new ContentValues();


        values.put(KEY_CUSTOMER_ID,assetRequest.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetRequest.getJsonMessageAutoIncId());
        values.put(KEY_JSON,assetRequest.getJson());

        return writableSqLiteDatabase.update(TABLE_ASSET_REQUEST, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(assetRequest.getAutoIncId())});
    }

    /**
     * Deleting a asset pullout
     */

    public int deleteAssetRequest(int autoIncId) {

        return writableSqLiteDatabase.delete(TABLE_ASSET_REQUEST, KEY_AUTO_INC_ID+ " = ?",
                new String[]{String.valueOf(autoIncId)});
    }

    public void deleteAllAssetRequests(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ASSET_REQUEST);
    }


    /**
     * get single asset request
     */
    public AssetRequest getAssetRequest(int auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_REQUEST + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetRequest assetRequest=null;

        if (c != null && c.moveToFirst() ) {

            assetRequest=new AssetRequest();

            assetRequest.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetRequest.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetRequest.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetRequest.setJson(c.getString(c.getColumnIndex(KEY_JSON)));

        }

        if (c!=null)
            c.close();

        return assetRequest;
    }


    /**
     * getting all asset requests
     */
    public List<AssetRequest> getAllAssetRequestsByCustomerId(int customer_id) {
        List<AssetRequest> assetRequests= new ArrayList<AssetRequest>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_REQUEST +   " WHERE " + KEY_CUSTOMER_ID + " =  " +customer_id ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetRequest assetRequest=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                assetRequest = new AssetRequest();

                assetRequest.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                assetRequest.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                assetRequest.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
                assetRequest.setJson(c.getString(c.getColumnIndex(KEY_JSON)));

                assetRequests.add(assetRequest);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return assetRequests;
    }



}

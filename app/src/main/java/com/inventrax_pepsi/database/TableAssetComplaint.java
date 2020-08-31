package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.AssetComplaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 4/12/2016.
 */
public class TableAssetComplaint {

    // Table Names
    public static final String TABLE_ASSET_COMPLAINT = "SFA_ASSET_COMPLAINT";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_COMPLAINT_ID = "COMPLAINT_ID";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_JSON_MESSAGE_ID = "JSON_MESSAGE_ID";
    private static final String KEY_COMPLAINT_STATUS = "COMPLAINT_STATUS";
    private static final String KEY_JSON = "JSON";


    // Table Create Statements
    public static final String CREATE_TABLE_ASSET_COMPLAINT= "CREATE TABLE " +
            TABLE_ASSET_COMPLAINT + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_COMPLAINT_ID + " INTEGER," +
            KEY_JSON_MESSAGE_ID + " INTEGER," +
            KEY_COMPLAINT_STATUS + " INTEGER," +
            KEY_JSON + " TEXT " +  ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableAssetComplaint(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Asset Complaint
     */
    public long createAssetComplaint(AssetComplaint assetComplaint) {

        ContentValues values = new ContentValues();

        values.put(KEY_COMPLAINT_ID,assetComplaint.getComplaintId());
        values.put(KEY_CUSTOMER_ID,assetComplaint.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetComplaint.getJsonMessageAutoIncId());
        values.put(KEY_COMPLAINT_STATUS,assetComplaint.getComplaintStatus());
        values.put(KEY_JSON,assetComplaint.getComplaintJSON());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ASSET_COMPLAINT, null, values);



        return auto_inc_id;
    }

    /**
     * Updating a asset complaint
     */
    public int updateAssetComplaint(AssetComplaint assetComplaint) {

        ContentValues values = new ContentValues();

        values.put(KEY_COMPLAINT_ID,assetComplaint.getComplaintId());
        values.put(KEY_CUSTOMER_ID,assetComplaint.getCustomerId());
        values.put(KEY_JSON_MESSAGE_ID,assetComplaint.getJsonMessageAutoIncId());
        values.put(KEY_COMPLAINT_STATUS,assetComplaint.getComplaintStatus());
        values.put(KEY_JSON,assetComplaint.getComplaintJSON());

        return writableSqLiteDatabase.update(TABLE_ASSET_COMPLAINT, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(assetComplaint.getAutoIncId())});
    }



    /**
     * Deleting a customer
     */
    public int deleteAssetComplaint(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_ASSET_COMPLAINT, KEY_AUTO_INC_ID+ " = ?",
                new String[]{String.valueOf(KEY_AUTO_INC_ID)});
    }

    public void deleteAllAssetComplaints(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ASSET_COMPLAINT);
    }

    /**
     * get single asset complaint
     */
    public AssetComplaint getAssetComplaint(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_COMPLAINT + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetComplaint assetComplaint=null;

        if (c != null && c.moveToFirst() ) {

            assetComplaint = new AssetComplaint();

            assetComplaint.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            assetComplaint.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            assetComplaint.setComplaintId(c.getInt(c.getColumnIndex(KEY_COMPLAINT_ID)));
            assetComplaint.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
            assetComplaint.setComplaintStatus(c.getInt(c.getColumnIndex(KEY_COMPLAINT_STATUS)));
            assetComplaint.setComplaintJSON(c.getString(c.getColumnIndex(KEY_JSON)));
        }

        if (c!=null)
            c.close();

        return assetComplaint;
    }


    /**
     * getting all asset complaints
     */
    public List<AssetComplaint> getAllAssetComplaints() {
        List<AssetComplaint> assetComplaintArrayList = new ArrayList<AssetComplaint>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSET_COMPLAINT;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        AssetComplaint assetComplaint=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                assetComplaint = new AssetComplaint();

                assetComplaint.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                assetComplaint.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                assetComplaint.setComplaintId(c.getInt(c.getColumnIndex(KEY_COMPLAINT_ID)));
                assetComplaint.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_ID)));
                assetComplaint.setComplaintStatus(c.getInt(c.getColumnIndex(KEY_COMPLAINT_STATUS)));
                assetComplaint.setComplaintJSON(c.getString(c.getColumnIndex(KEY_JSON)));

                assetComplaintArrayList.add(assetComplaint);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return assetComplaintArrayList;
    }



}

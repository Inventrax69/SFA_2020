package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.database.pojos.UserTracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableUserTracking {

    // Table Names
    public static final String TABLE_USER_TRACKING = "SFA_USER_TRACKING";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID="CUSTOMER_ID";
    private static final String KEY_ROUTE_ID="ROUTE_ID";
    private static final String KEY_CUSTOMER_TYPE_ID="CUSTOMER_TYPE_ID";
    private static final String KEY_CUSTOMER_CODE="CUSTOMER_CODE";
    private static final String KEY_CUSTOMER_TYPE="CUSTOMER_TYPE";
    private static final String KEY_CHECK_IN_TIME_STAMP="CHECK_IN_TIME_STAMP";
    private static final String KEY_CHECK_OUT_TIME_STAMP="CHECK_OUT_TIME_STAMP";
    private static final String KEY_LATITUDE="LATITUDE";
    private static final String KEY_LONGITUDE="LONGITUDE";
    private static final String KEY_ROUTE_CODE="ROUTE_CODE";
    private static final String KEY_JSON_MESSAGE_AUTO_INC_ID="JSON_MESSAGE_AUTO_INC_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_USER_TRACKING= "CREATE TABLE " +
            TABLE_USER_TRACKING + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_CUSTOMER_TYPE_ID + " INTEGER," +
            KEY_JSON_MESSAGE_AUTO_INC_ID+ " INTEGER," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_CHECK_IN_TIME_STAMP+ " TEXT," +
            KEY_CUSTOMER_CODE + " TEXT ," +
            KEY_CHECK_OUT_TIME_STAMP + " TEXT," +
            KEY_CUSTOMER_TYPE + " TEXT," +
            KEY_LATITUDE + " TEXT," +
            KEY_LONGITUDE + " TEXT," +
            KEY_ROUTE_CODE + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableUserTracking(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a User Tracking
     */
    public long createUserTracking(UserTracking userTracking) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,userTracking.getCustomerId());
        values.put(KEY_ROUTE_ID,userTracking.getRouteId());
        values.put(KEY_CUSTOMER_TYPE_ID,userTracking.getCustomerTypeId());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,userTracking.getJsonMessageAutoIncId());
        values.put(KEY_CUSTOMER_CODE,userTracking.getCustomerCode());
        values.put(KEY_CUSTOMER_TYPE,userTracking.getCustomerType());
        values.put(KEY_CHECK_IN_TIME_STAMP,userTracking.getCheckInTimestamp());
        values.put(KEY_CHECK_OUT_TIME_STAMP,userTracking.getCheckOutTimestamp());
        values.put(KEY_LATITUDE,userTracking.getLatitude());
        values.put(KEY_LONGITUDE,userTracking.getLongitude());
        values.put(KEY_ROUTE_CODE,userTracking.getRouteCode());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_USER_TRACKING, null, values);

        return auto_inc_id;
    }


    /**
     * Updating a User Tracking
     */
    public long updateUserTracking(UserTracking userTracking) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,userTracking.getCustomerId());
        values.put(KEY_ROUTE_ID,userTracking.getRouteId());
        values.put(KEY_CUSTOMER_TYPE_ID,userTracking.getCustomerTypeId());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,userTracking.getJsonMessageAutoIncId());
        values.put(KEY_CUSTOMER_CODE,userTracking.getCustomerCode());
        values.put(KEY_CUSTOMER_TYPE,userTracking.getCustomerType());
        values.put(KEY_CHECK_IN_TIME_STAMP,userTracking.getCheckInTimestamp());
        values.put(KEY_CHECK_OUT_TIME_STAMP,userTracking.getCheckOutTimestamp());
        values.put(KEY_LATITUDE,userTracking.getLatitude());
        values.put(KEY_LONGITUDE,userTracking.getLongitude());
        values.put(KEY_ROUTE_CODE,userTracking.getRouteCode());

        return writableSqLiteDatabase.update(TABLE_USER_TRACKING, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(userTracking.getAutoIncId())});
    }

    /**
     * Deleting a User Tracking
     */
    public int deleteUserTracking(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_USER_TRACKING, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});

    }

    public void deleteAllUserTrackingInfo(){

        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_USER_TRACKING);

    }

    /**
     * get single user tracking
     */
    public UserTracking getUserTracking(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_USER_TRACKING + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        UserTracking userTracking=null;

        if (c != null && c.moveToFirst() ) {

            userTracking=new UserTracking();

            userTracking.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            userTracking.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            userTracking.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            userTracking.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
            userTracking.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            userTracking.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
            userTracking.setCheckInTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_IN_TIME_STAMP)));
            userTracking.setCheckOutTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_OUT_TIME_STAMP)));
            userTracking.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
            userTracking.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
            userTracking.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            userTracking.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

        }

        if (c!=null)
            c.close();

        return userTracking;
    }

    /**
     * getting all user tracking details
     */
    public List<UserTracking> getAllUserTrackingDetails() {
        List<UserTracking> userTrackingArrayList = new ArrayList<UserTracking>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_TRACKING;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        UserTracking userTracking=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                userTracking = new UserTracking();

                userTracking.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                userTracking.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                userTracking.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                userTracking.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                userTracking.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                userTracking.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                userTracking.setCheckInTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_IN_TIME_STAMP)));
                userTracking.setCheckOutTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_OUT_TIME_STAMP)));
                userTracking.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                userTracking.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                userTracking.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                userTracking.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));


                userTrackingArrayList.add(userTracking);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return userTrackingArrayList;
    }

    public List<UserTracking> getAllUserTrackingDetails(int customerId) {
        List<UserTracking> userTrackingArrayList = new ArrayList<UserTracking>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_TRACKING + " WHERE " + KEY_CUSTOMER_ID + " = "+customerId;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        UserTracking userTracking=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                userTracking = new UserTracking();

                userTracking.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                userTracking.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                userTracking.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                userTracking.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                userTracking.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                userTracking.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                userTracking.setCheckInTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_IN_TIME_STAMP)));
                userTracking.setCheckOutTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_OUT_TIME_STAMP)));
                userTracking.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                userTracking.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                userTracking.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                userTracking.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));


                userTrackingArrayList.add(userTracking);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return userTrackingArrayList;
    }


    public UserTracking getSingleUserTracking(int customer_id) {

        UserTracking userTracking = null;

        try {

            String selectQuery = "SELECT  * FROM " + TABLE_USER_TRACKING + " WHERE "
                    + KEY_CUSTOMER_ID + " = " + customer_id + " AND  " + KEY_CHECK_OUT_TIME_STAMP + " IS NULL ";
            Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);



            if (c != null && c.moveToFirst() ) {

                userTracking = new UserTracking();

                userTracking.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                userTracking.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                userTracking.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                userTracking.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                userTracking.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                userTracking.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                userTracking.setCheckInTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_IN_TIME_STAMP)));
                userTracking.setCheckOutTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_OUT_TIME_STAMP)));
                userTracking.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                userTracking.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                userTracking.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                userTracking.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

            }

            if (c != null)
                c.close();

        }catch (Exception ex){
            Logger.Log(TableUserTracking.class.getName(),ex);
            return null;
        }

        return userTracking;
    }

    public UserTracking getSingleUserTrackingForCheckOut(int customer_id) {

        UserTracking userTracking = null;

        try {

            String selectQuery = "SELECT  * FROM " + TABLE_USER_TRACKING + " WHERE "
                    + KEY_CUSTOMER_ID + " = " + customer_id + " AND  " + KEY_CHECK_IN_TIME_STAMP + " IS NOT NULL ";
            Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);





            if (c != null && c.moveToFirst() ) {

                userTracking = new UserTracking();

                userTracking.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                userTracking.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                userTracking.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                userTracking.setCustomerTypeId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_TYPE_ID)));
                userTracking.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                userTracking.setCustomerType(c.getString(c.getColumnIndex(KEY_CUSTOMER_TYPE)));
                userTracking.setCheckInTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_IN_TIME_STAMP)));
                userTracking.setCheckOutTimestamp(c.getString(c.getColumnIndex(KEY_CHECK_OUT_TIME_STAMP)));
                userTracking.setLatitude(c.getString(c.getColumnIndex(KEY_LATITUDE)));
                userTracking.setLongitude(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
                userTracking.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                userTracking.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));

            }

            if (c != null)
                c.close();

        }catch (Exception ex){
            Logger.Log(TableUserTracking.class.getName(),ex);
            return null;
        }

        return userTracking;
    }


    public int getOutletVisitStatus(int customer_id){

        int status=-1;

        try
        {
            String selectQuery = "SELECT  CASE WHEN " + KEY_CHECK_IN_TIME_STAMP + " IS NULL THEN  -1  WHEN "+ KEY_CHECK_OUT_TIME_STAMP  + " IS NOT NOT NULL THEN 1 ELSE 0 END AS N   FROM " + TABLE_USER_TRACKING + " WHERE "
                    + KEY_CUSTOMER_ID + " = " + customer_id  + " ORDER BY " + KEY_AUTO_INC_ID + " DESC "   ;
            Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

            if (c != null && c.moveToFirst() ) {

                status=c.getInt(c.getColumnIndex("N"));
            }

            if (c != null)
                c.close();

        }catch (Exception ex){

        }

        return status;

    }


}

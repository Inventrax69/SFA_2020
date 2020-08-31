package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.common.constants.JsonMessageNotificationType;
import com.inventrax_pepsi.database.pojos.JSONMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableJSONMessage {

    // Table Names
    public static final String TABLE_JSON_MESSAGE = "SFA_JSON_MESSAGE";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_NOTIFICATION_TYPE_ID = "NOTIFICATION_TYPE_ID";
    private static final String KEY_NOTIFICATION_ID = "NOTIFICATION_ID";
    private static final String KEY_NO_OF_REQUESTS = "NO_OF_REQUESTS";
    private static final String KEY_JSON_MESSAGE = "JSON_MESSAGE";
    private static final String KEY_SYNC_STATUS = "SYNC_STATUS";


    // Table Create Statements
    public static final String CREATE_TABLE_JSON_MESSAGE = "CREATE TABLE " +
            TABLE_JSON_MESSAGE + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_NOTIFICATION_TYPE_ID + " INTEGER ," +
            KEY_NOTIFICATION_ID + " INTEGER ," +
            KEY_NO_OF_REQUESTS + " INTEGER ," +
            KEY_JSON_MESSAGE + " TEXT ," +
            KEY_SYNC_STATUS + " INTEGER " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableJSONMessage(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }

    /**
     * Creating a JSON Message
     */
    public long createJSONMessage(JSONMessage jsonMessage) {

        ContentValues values = new ContentValues();

        values.put(KEY_NOTIFICATION_TYPE_ID, jsonMessage.getNotificationTypeId());
        values.put(KEY_NOTIFICATION_ID, jsonMessage.getNotificationId());
        values.put(KEY_NO_OF_REQUESTS, jsonMessage.getNoOfRequests());
        values.put(KEY_JSON_MESSAGE, jsonMessage.getJsonMessage());
        values.put(KEY_SYNC_STATUS, jsonMessage.getSyncStatus());


        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_JSON_MESSAGE, null, values);

        return auto_inc_id;
    }


    /**
     * Updating a JSON Message
     */
    public int updateJSONMessage(JSONMessage jsonMessage) {

        ContentValues values = new ContentValues();

        values.put(KEY_NOTIFICATION_TYPE_ID, jsonMessage.getNotificationTypeId());
        values.put(KEY_NOTIFICATION_ID, jsonMessage.getNotificationId());
        values.put(KEY_NO_OF_REQUESTS, jsonMessage.getNoOfRequests());
        values.put(KEY_JSON_MESSAGE, jsonMessage.getJsonMessage());
        values.put(KEY_SYNC_STATUS, jsonMessage.getSyncStatus());

        return writableSqLiteDatabase.update(TABLE_JSON_MESSAGE, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(jsonMessage.getAutoIncId())});
    }

    /**
     * Deleting a JSON Message
     */

    public int deleteJSONMessage(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_JSON_MESSAGE, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});
    }

    public void deleteAllJSONMessages() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_JSON_MESSAGE);
    }

    /**
     * Get a single JSON Message
     */
    public JSONMessage getJSONMessage(int auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null && c.moveToFirst()) {

            jsonMessage = new JSONMessage();

            jsonMessage.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            jsonMessage.setNotificationTypeId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_TYPE_ID)));
            jsonMessage.setNoOfRequests(c.getInt(c.getColumnIndex(KEY_NO_OF_REQUESTS)));
            jsonMessage.setJsonMessage(c.getString(c.getColumnIndex(KEY_JSON_MESSAGE)));
            jsonMessage.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
            jsonMessage.setNotificationId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_ID)));

        }

        if (c != null)
            c.close();

        return jsonMessage;

    }

    public int getOrderSyncStatus(int order_id) {

        int status = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE "
                + KEY_NOTIFICATION_ID + " = " + order_id + " AND " + KEY_NOTIFICATION_TYPE_ID + " = 1 ";
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null && c.moveToFirst()) {

            do {

                status = c.getInt(c.getColumnIndex(KEY_SYNC_STATUS));

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return status;

    }

    public int getCustomerSyncStatus(int auto_inc_id) {

        int status = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE "
                + KEY_NOTIFICATION_ID + " = " + auto_inc_id + " AND " + KEY_NOTIFICATION_TYPE_ID + " = " + JsonMessageNotificationType.Customer.getNotificationType();
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null && c.moveToFirst()) {

            do {

                status = c.getInt(c.getColumnIndex(KEY_SYNC_STATUS));

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return status;

    }


    public int getInvoiceSyncStatus(int invoice_id) {

        int status = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE "
                + KEY_NOTIFICATION_ID + " = " + invoice_id + " AND " + KEY_NOTIFICATION_TYPE_ID + " = 2 ";
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null && c.moveToFirst()) {

            do {
                status = c.getInt(c.getColumnIndex(KEY_SYNC_STATUS));
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return status;

    }


    /**
     * get all un synced JSONMessages
     */
    public List<JSONMessage> getAllJSONMessages(int syncStatus,int notificationTypeId) {

        List<JSONMessage> jsonMessageList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE  " + KEY_SYNC_STATUS + " = " + syncStatus + " AND  " + KEY_NOTIFICATION_TYPE_ID + " = "+ notificationTypeId + "  ORDER BY  " + KEY_AUTO_INC_ID + " ASC "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null) {

            if (c.moveToFirst()) {

                do {

                    jsonMessage=new JSONMessage();

                    jsonMessage.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                    jsonMessage.setNotificationTypeId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_TYPE_ID)));
                    jsonMessage.setNoOfRequests(c.getInt(c.getColumnIndex(KEY_NO_OF_REQUESTS)));
                    jsonMessage.setJsonMessage(c.getString(c.getColumnIndex(KEY_JSON_MESSAGE)));
                    jsonMessage.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                    jsonMessage.setNotificationId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_ID)));

                    jsonMessageList.add(jsonMessage);

                } while (c.moveToNext());
            }
        }

        if (c != null)
            c.close();

        return jsonMessageList;
    }


    /**
     * get all JSONMessages with sync status
     */

    public List<JSONMessage> getAllJSONMessages(int syncStatus) {

        List<JSONMessage> jsonMessageList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_JSON_MESSAGE + " WHERE  " + KEY_SYNC_STATUS + " = " + syncStatus ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        JSONMessage jsonMessage = null;

        if (c != null) {

            if (c.moveToFirst()) {

                do {

                    jsonMessage=new JSONMessage();

                    jsonMessage.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                    jsonMessage.setNotificationTypeId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_TYPE_ID)));
                    jsonMessage.setNoOfRequests(c.getInt(c.getColumnIndex(KEY_NO_OF_REQUESTS)));
                    jsonMessage.setJsonMessage(c.getString(c.getColumnIndex(KEY_JSON_MESSAGE)));
                    jsonMessage.setSyncStatus(c.getInt(c.getColumnIndex(KEY_SYNC_STATUS)));
                    jsonMessage.setNotificationId(c.getInt(c.getColumnIndex(KEY_NOTIFICATION_ID)));

                    jsonMessageList.add(jsonMessage);

                } while (c.moveToNext());
            }
        }

        if (c != null)
            c.close();

        return jsonMessageList;
    }


}
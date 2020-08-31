package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.VehicleLoad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 27-Mar-16.
 */
public class TableVehicleLoad {

    // Table Names
    public static final String TABLE_VEHICLE_LOAD = "SFA_VEHICLE_LOAD";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_LOAD_ID = "LOAD_ID";
    private static final String KEY_ROUTE_ID = "ROUTE_ID";
    private static final String KEY_LOAD_AMOUNT = "LOAD_AMOUNT";
    private static final String KEY_ROUTE_CODE = "ROUTE_CODE";
    private static final String KEY_SETTLEMENT_NO = "SETTLEMENT_NO";
    private static final String KEY_LOAD_JSON = "LOAD_JSON";
    private static final String KEY_LOAD_STATUS = "LOAD_STATUS";


    // Table Create Statements
    public static final String CREATE_TABLE_VEHICLE_LOAD = "CREATE TABLE " +
            TABLE_VEHICLE_LOAD + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_LOAD_ID + " INTEGER UNIQUE," +
            KEY_LOAD_AMOUNT + " REAL," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_LOAD_STATUS + " INTEGER," +
            KEY_SETTLEMENT_NO + " TEXT," +
            KEY_LOAD_JSON + " TEXT ," +
            KEY_ROUTE_CODE + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableVehicleLoad(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }


    /**
     * Creating a vehicle load
     */
    public long createVehicleLoad(VehicleLoad vehicleLoad) {

        ContentValues values = new ContentValues();

        values.put(KEY_LOAD_ID, vehicleLoad.getLoadId());
        values.put(KEY_LOAD_AMOUNT, vehicleLoad.getLoadAmount());
        values.put(KEY_ROUTE_ID, vehicleLoad.getRouteId());
        values.put(KEY_SETTLEMENT_NO, vehicleLoad.getSettlementNo());
        values.put(KEY_LOAD_JSON, vehicleLoad.getLoadJSON());
        values.put(KEY_ROUTE_CODE, vehicleLoad.getRouteCode());
        values.put(KEY_LOAD_STATUS,vehicleLoad.getLoadStatus());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_VEHICLE_LOAD, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a vehicle load
     */
    public long updateVehicleLoad(VehicleLoad vehicleLoad) {

        ContentValues values = new ContentValues();

        values.put(KEY_LOAD_ID, vehicleLoad.getLoadId());
        values.put(KEY_LOAD_AMOUNT, vehicleLoad.getLoadAmount());
        values.put(KEY_ROUTE_ID, vehicleLoad.getRouteId());
        values.put(KEY_SETTLEMENT_NO, vehicleLoad.getSettlementNo());
        values.put(KEY_LOAD_JSON, vehicleLoad.getLoadJSON());
        values.put(KEY_ROUTE_CODE, vehicleLoad.getRouteCode());
        values.put(KEY_LOAD_STATUS,vehicleLoad.getLoadStatus());

        return writableSqLiteDatabase.update(TABLE_VEHICLE_LOAD, values, KEY_LOAD_ID + " = ?",
                new String[]{String.valueOf(vehicleLoad.getLoadId())});
    }


    /**
     * Updating a vehicle load status
     */
    public void updateVehicleLoadStatus() {

        writableSqLiteDatabase.execSQL(" UPDATE " + TABLE_VEHICLE_LOAD + " SET " + KEY_LOAD_STATUS + " =1");
    }


    /**
     * Deleting a vehicle load
     */
    public int deleteVehicleLoad(long load_id) {

        return writableSqLiteDatabase.delete(TABLE_VEHICLE_LOAD, KEY_LOAD_ID + " = ?",
                new String[]{String.valueOf(load_id)});
    }

    public void deleteAllVehicleLoads() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_VEHICLE_LOAD);
    }


    /**
     * get single vehicleLoad
     */
    public VehicleLoad getVehicleLoad(long load_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_VEHICLE_LOAD + " WHERE "
                + KEY_LOAD_ID + " = " + load_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VehicleLoad vehicleLoad = null;

        if (c != null && c.moveToFirst()) {

            vehicleLoad = new VehicleLoad();

            vehicleLoad.setAutoInc(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            vehicleLoad.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            vehicleLoad.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            vehicleLoad.setLoadId(c.getInt(c.getColumnIndex(KEY_LOAD_ID)));
            vehicleLoad.setLoadAmount(c.getDouble(c.getColumnIndex(KEY_LOAD_AMOUNT)));
            vehicleLoad.setLoadJSON(c.getString(c.getColumnIndex(KEY_LOAD_JSON)));
            vehicleLoad.setSettlementNo(c.getString(c.getColumnIndex(KEY_SETTLEMENT_NO)));
            vehicleLoad.setLoadStatus(c.getInt(c.getColumnIndex(KEY_LOAD_STATUS)));
        }

        if (c != null)
            c.close();

        return vehicleLoad;
    }

    public List<VehicleLoad> getAllVehicleLoads() {

        List<VehicleLoad> vehicleLoads = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_VEHICLE_LOAD;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VehicleLoad vehicleLoad = null;

        if (c != null && c.moveToFirst()) {

            do {

                vehicleLoad = new VehicleLoad();

                vehicleLoad.setAutoInc(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                vehicleLoad.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                vehicleLoad.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                vehicleLoad.setLoadId(c.getInt(c.getColumnIndex(KEY_LOAD_ID)));
                vehicleLoad.setLoadAmount(c.getDouble(c.getColumnIndex(KEY_LOAD_AMOUNT)));
                vehicleLoad.setLoadJSON(c.getString(c.getColumnIndex(KEY_LOAD_JSON)));
                vehicleLoad.setSettlementNo(c.getString(c.getColumnIndex(KEY_SETTLEMENT_NO)));
                vehicleLoad.setLoadStatus(c.getInt(c.getColumnIndex(KEY_LOAD_STATUS)));

                vehicleLoads.add(vehicleLoad);
            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return vehicleLoads;
    }

    public double getLoadAmount() {

        String selectQuery = "SELECT  SUM("+KEY_LOAD_AMOUNT+") AS Load FROM " + TABLE_VEHICLE_LOAD ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        double loadAmount=0;

        if (c != null && c.moveToFirst()) {

            loadAmount=c.getDouble(c.getColumnIndex("Load"));

        }

        if (c != null)
            c.close();

        return loadAmount;
    }

    public int getLoadStatus() {

        String selectQuery = "SELECT  " + KEY_LOAD_STATUS + "  AS LoadStatus FROM " + TABLE_VEHICLE_LOAD ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        int loadStatus=0;

        if (c != null && c.moveToFirst()) {

            loadStatus=c.getInt(c.getColumnIndex("LoadStatus"));

        }

        if (c != null)
            c.close();

        return loadStatus;
    }




}

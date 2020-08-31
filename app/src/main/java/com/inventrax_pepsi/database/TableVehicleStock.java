package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.VehicleStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 27-Mar-16.
 */
public class TableVehicleStock {

    // Table Names
    public static final String TABLE_VEHICLE_STOCK = "SFA_VEHICLE_STOCK";

    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    public static final String KEY_ITEM_ID = "ITEM_ID";
    private static final String KEY_ITEM_CODE = "ITEM_CODE";
    private static final String KEY_ITEM_NAME = "ITEM_NAME";
    public static final String KEY_CASE_QUANTITY = "CASE_QUANTITY";
    public static final String KEY_BOTTLE_QUANTITY = "BOTTLE_QUANTITY";
    private static final String KEY_TRANSACTION_TYPE = "TRANSACTION_TYPE";
    private static final String KEY_ITEM_TYPE_ID = "ITEM_TYPE_ID";
    private static final String KEY_IMAGE_NAME = "IMAGE_NAME";
    private static final String KEY_ITEM_MRP = "ITEM_MRP";


    // Table Create Statements
    public static final String CREATE_TABLE_VEHICLE_STOCK = "CREATE TABLE " +
            TABLE_VEHICLE_STOCK + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_ITEM_ID + " INTEGER ," +
            KEY_CASE_QUANTITY + " REAL," +
            KEY_BOTTLE_QUANTITY + " REAL," +
            KEY_ITEM_MRP + " REAL," +
            KEY_ITEM_CODE + " TEXT," +
            KEY_ITEM_NAME + " TEXT," +
            KEY_IMAGE_NAME + " TEXT, " +
            KEY_ITEM_TYPE_ID + " INTEGER ," +
            KEY_TRANSACTION_TYPE + " INTEGER " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableVehicleStock(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }

    /**
     * Creating a vehicle stock
     */
    public long createVehicleStock(VehicleStock vehicleStock) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID, vehicleStock.getItemId());
        values.put(KEY_BOTTLE_QUANTITY, vehicleStock.getBottleQuantity());
        values.put(KEY_CASE_QUANTITY, vehicleStock.getCaseQuantity());
        values.put(KEY_ITEM_CODE, vehicleStock.getItemCode());
        values.put(KEY_ITEM_NAME, vehicleStock.getItemName());
        values.put(KEY_ITEM_TYPE_ID, vehicleStock.getItemTypeId());
        values.put(KEY_TRANSACTION_TYPE, vehicleStock.getTransactionType());
        values.put(KEY_IMAGE_NAME, vehicleStock.getImageName());
        values.put(KEY_ITEM_MRP,vehicleStock.getLineMRP());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_VEHICLE_STOCK, null, values);

        return auto_inc_id;
    }

    /**
     * Deleting a vehicle stock
     */
    public int deleteVehicleStock(long itemId) {

        return writableSqLiteDatabase.delete(TABLE_VEHICLE_STOCK, KEY_ITEM_ID + " = ?",
                new String[]{String.valueOf(itemId)});
    }

    public void deleteAllVehicleStocks() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_VEHICLE_STOCK);
    }


    /**
     * get single vehicle stock
     */
    public VehicleStock getVehicleStock(long itemId) {

        String selectQuery = "SELECT  * FROM " + TABLE_VEHICLE_STOCK + " WHERE "
                + KEY_ITEM_ID + " = " + itemId;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VehicleStock vehicleStock = null;

        if (c != null && c.moveToFirst()) {

            vehicleStock = new VehicleStock();

            vehicleStock.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            vehicleStock.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            vehicleStock.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            vehicleStock.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
            vehicleStock.setItemTypeId(c.getInt(c.getColumnIndex(KEY_ITEM_TYPE_ID)));
            vehicleStock.setTransactionType(c.getInt(c.getColumnIndex(KEY_TRANSACTION_TYPE)));
            vehicleStock.setCaseQuantity(c.getDouble(c.getColumnIndex(KEY_CASE_QUANTITY)));
            vehicleStock.setBottleQuantity(c.getDouble(c.getColumnIndex(KEY_BOTTLE_QUANTITY)));
            vehicleStock.setImageName(c.getString(c.getColumnIndex(KEY_IMAGE_NAME)));
            vehicleStock.setLineMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));
        }

        if (c != null)
            c.close();

        return vehicleStock;
    }

    /**
     * get single vehicle stock
     */
    public VehicleStock getVehicleStock(long itemId,double itemMRP) {

        String selectQuery = "SELECT  * FROM " + TABLE_VEHICLE_STOCK + " WHERE "
                + KEY_ITEM_ID + " = " + itemId +  "  AND  "+ KEY_ITEM_MRP + " = " + itemMRP    ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VehicleStock vehicleStock = null;

        if (c != null && c.moveToFirst()) {

            vehicleStock = new VehicleStock();

            vehicleStock.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            vehicleStock.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            vehicleStock.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            vehicleStock.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
            vehicleStock.setItemTypeId(c.getInt(c.getColumnIndex(KEY_ITEM_TYPE_ID)));
            vehicleStock.setTransactionType(c.getInt(c.getColumnIndex(KEY_TRANSACTION_TYPE)));
            vehicleStock.setCaseQuantity(c.getDouble(c.getColumnIndex(KEY_CASE_QUANTITY)));
            vehicleStock.setBottleQuantity(c.getDouble(c.getColumnIndex(KEY_BOTTLE_QUANTITY)));
            vehicleStock.setImageName(c.getString(c.getColumnIndex(KEY_IMAGE_NAME)));
            vehicleStock.setLineMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));
        }

        if (c != null)
            c.close();

        return vehicleStock;
    }



    public List<VehicleStock> getAllVehicleStocks() {

        List<VehicleStock> vehicleStocks = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_VEHICLE_STOCK + " WHERE  (" + KEY_BOTTLE_QUANTITY+" > 0  OR " + KEY_CASE_QUANTITY + " > 0 )"  ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VehicleStock vehicleStock = null;

        if (c != null && c.moveToFirst()) {

            do {
                vehicleStock = new VehicleStock();

                vehicleStock.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                vehicleStock.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                vehicleStock.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                vehicleStock.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                vehicleStock.setItemTypeId(c.getInt(c.getColumnIndex(KEY_ITEM_TYPE_ID)));
                vehicleStock.setTransactionType(c.getInt(c.getColumnIndex(KEY_TRANSACTION_TYPE)));
                vehicleStock.setCaseQuantity(c.getDouble(c.getColumnIndex(KEY_CASE_QUANTITY)));
                vehicleStock.setBottleQuantity(c.getDouble(c.getColumnIndex(KEY_BOTTLE_QUANTITY)));
                vehicleStock.setImageName(c.getString(c.getColumnIndex(KEY_IMAGE_NAME)));
                vehicleStock.setLineMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));

                vehicleStocks.add(vehicleStock);

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return vehicleStocks;
    }

    public double getCaseQuantity() {

        String selectQuery = "SELECT  SUM("+KEY_CASE_QUANTITY+") AS  QUANTITY   FROM " + TABLE_VEHICLE_STOCK ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        double caseQuantity=0;

        if (c != null && c.moveToFirst()) {

            do {

                caseQuantity= c.getDouble(c.getColumnIndex("QUANTITY"));

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return caseQuantity;
    }


    public double getBottleQuantity() {

        String selectQuery = "SELECT  SUM( "+ KEY_BOTTLE_QUANTITY +" ) AS  QUANTITY    FROM " + TABLE_VEHICLE_STOCK ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        double caseQuantity=0;

        if (c != null && c.moveToFirst()) {

            do {

                caseQuantity= c.getDouble(c.getColumnIndex("QUANTITY"));

            } while (c.moveToNext());
        }

        if (c != null)
            c.close();

        return caseQuantity;
    }



    public boolean updateVanInventory(long itemId,double caseQty,double bottleQty){


            String updateQuery = "UPDATE " + TABLE_VEHICLE_STOCK + " SET " + KEY_CASE_QUANTITY + " = " + caseQty + ", " + KEY_BOTTLE_QUANTITY + " = " + bottleQty   + "  WHERE "+KEY_ITEM_ID + " = " + itemId ;

            writableSqLiteDatabase.execSQL(updateQuery);

            return true;

    }

    public boolean updateVanInventory(long itemId,double itemMRP,double caseQty,double bottleQty){


        String updateQuery = "UPDATE " + TABLE_VEHICLE_STOCK + " SET " + KEY_CASE_QUANTITY + " = " + caseQty + ", " + KEY_BOTTLE_QUANTITY + " = " + bottleQty   + "  WHERE " + KEY_ITEM_ID + " = " + itemId + " AND  " + KEY_ITEM_MRP + " = " + itemMRP ;

        writableSqLiteDatabase.execSQL(updateQuery);

        return true;

    }


}

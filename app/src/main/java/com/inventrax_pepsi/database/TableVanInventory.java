package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.VanInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableVanInventory {

    // Table Names
    public static final String TABLE_VAN_INVENTORY = "SFA_VAN_INVENTORY";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_ITEM_UOM_ID="ITEM_UOM_ID";
    private static final String KEY_ITEM_CODE="ITEM_CODE";
    private static final String KEY_ITEM_NAME="ITEM_NAME";
    private static final String KEY_ITEM_UOM="ITEM_UOM";
    private static final String KEY_QUANTITY="QUANTITY";
    private static final String KEY_STOCK_VALUE="STOCK_VALUE";
    private static final String KEY_ITEM_UOM_QUANTITY="ITEM_UOM_QUANTITY";

    private static final String KEY_LOAD_ID="LOAD_ID";
    private static final String KEY_SETTLEMENT_NO="SETTLEMENT_NO";
    private static final String KEY_ROUTE_ID="ROUTE_ID";


    // Table Create Statements
    public static final String CREATE_TABLE_VAN_INVENTORY= "CREATE TABLE " +
            TABLE_VAN_INVENTORY + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_ROUTE_ID + " INTEGER ," +
            KEY_ITEM_ID + " INTEGER ," +
            KEY_ITEM_UOM_ID + " INTEGER," +
            KEY_LOAD_ID + " INTEGER," +
            KEY_SETTLEMENT_NO + " TEXT," +
            KEY_ITEM_CODE + " TEXT," +
            KEY_ITEM_NAME+ " TEXT," +
            KEY_ITEM_UOM + " TEXT ," +
            KEY_QUANTITY + " REAL," +
            KEY_STOCK_VALUE + " REAL," +
            KEY_ITEM_UOM_QUANTITY + " INTEGER " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableVanInventory(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Van Inventory
     */
    public long createVanInventory(VanInventory vanInventory) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID,vanInventory.getItemId());
        values.put(KEY_ITEM_UOM_ID,vanInventory.getUomId());
        values.put(KEY_ITEM_CODE,vanInventory.getItemCode());
        values.put(KEY_ITEM_NAME,vanInventory.getItemName());
        values.put(KEY_ITEM_UOM,vanInventory.getItemUOM());
        values.put(KEY_QUANTITY,vanInventory.getQuantity());
        values.put(KEY_STOCK_VALUE,vanInventory.getStockValue());
        values.put(KEY_ITEM_UOM_QUANTITY,vanInventory.getItemUoMQuantity());

        values.put(KEY_LOAD_ID,vanInventory.getLoadId());
        values.put(KEY_SETTLEMENT_NO,vanInventory.getSettlementNo());
        values.put(KEY_ROUTE_ID,vanInventory.getRouteId());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_VAN_INVENTORY, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a Van Inventory
     */
    public long updateVanInventory(VanInventory vanInventory) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID,vanInventory.getItemId());
        values.put(KEY_ITEM_UOM_ID,vanInventory.getUomId());
        values.put(KEY_ITEM_CODE,vanInventory.getItemCode());
        values.put(KEY_ITEM_NAME,vanInventory.getItemName());
        values.put(KEY_ITEM_UOM,vanInventory.getItemUOM());
        values.put(KEY_QUANTITY,vanInventory.getQuantity());
        values.put(KEY_STOCK_VALUE,vanInventory.getStockValue());
        values.put(KEY_ITEM_UOM_QUANTITY,vanInventory.getItemUoMQuantity());

        values.put(KEY_LOAD_ID,vanInventory.getLoadId());
        values.put(KEY_SETTLEMENT_NO,vanInventory.getSettlementNo());
        values.put(KEY_ROUTE_ID,vanInventory.getRouteId());

        return writableSqLiteDatabase.update(TABLE_VAN_INVENTORY, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(vanInventory.getAutoIncId())});
    }

    /**
     * Deleting a van inventory
     */

    public int deleteVanInventory(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_VAN_INVENTORY, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});
    }

    public void deleteAllVanInventory(){
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_VAN_INVENTORY);
    }


    /**
     * get single Van Inventory
     */
    public VanInventory getVanInventory(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_VAN_INVENTORY + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VanInventory vanInventory=null;

        if (c != null && c.moveToFirst()) {

            vanInventory = new VanInventory();

            vanInventory.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            vanInventory.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            vanInventory.setUomId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
            vanInventory.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            vanInventory.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
            vanInventory.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
            vanInventory.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));
            vanInventory.setStockValue(c.getDouble(c.getColumnIndex(KEY_STOCK_VALUE)));
            vanInventory.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
            vanInventory.setLoadId(c.getInt(c.getColumnIndex(KEY_LOAD_ID)));
            vanInventory.setSettlementNo(c.getString(c.getColumnIndex(KEY_SETTLEMENT_NO)));
            vanInventory.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));


        }

        if (c!=null)
            c.close();

        return vanInventory;
    }


    /**
     * getting all van inventory
     */
    public List<VanInventory> getAllVanInventoryDetails() {
        List<VanInventory> vanInventoryArrayList = new ArrayList<VanInventory>();
        String selectQuery = "SELECT  * FROM " + TABLE_VAN_INVENTORY;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        VanInventory vanInventory=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                vanInventory = new VanInventory();

                vanInventory.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                vanInventory.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                vanInventory.setUomId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
                vanInventory.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                vanInventory.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                vanInventory.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                vanInventory.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));
                vanInventory.setStockValue(c.getDouble(c.getColumnIndex(KEY_STOCK_VALUE)));
                vanInventory.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
                vanInventory.setLoadId(c.getInt(c.getColumnIndex(KEY_LOAD_ID)));
                vanInventory.setSettlementNo(c.getString(c.getColumnIndex(KEY_SETTLEMENT_NO)));
                vanInventory.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));

                vanInventoryArrayList.add(vanInventory);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return vanInventoryArrayList;
    }

}

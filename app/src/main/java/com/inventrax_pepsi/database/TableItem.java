package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableItem {


    // Table Names
    public static final String TABLE_ITEM = "SFA_ITEM";

    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_ITEM_ID = "ITEM_ID";
    private static final String KEY_ITEM_UOM_ID = "ITEM_UOM_ID";
    private static final String KEY_ITEM_CODE = "ITEM_CODE";
    private static final String KEY_ITEM_JSON = "ITEM_JSON";
    private static final String KEY_PRICE_JSON = "PRICE_JSON";
    private static final String KEY_UOM_JSON="UOM_JSON";
    private static final String KEY_ITEM_MRP = "ITEM_MRP";
    private static final String KEY_TRADE_RETAIL = "TRADE_RETAIL";
    private static final String KEY_ITEM_UOM_QUANTITY = "ITEM_UOM_QUANTITY";
    private static final String KEY_BRAND_NAME = "BRAND_NAME";
    private static final String KEY_BRAND_PACK_NAME = "BRAND_PACK_NAME";
    private static final String KEY_ITEM_UOM = "ITEM_UOM";
    private static final String KEY_IS_TRADE = "IS_TRADE";
    private static final String KEY_ITEM_TYPE = "ITEM_TYPE";
    private static final String KEY_IS_FMO = "IS_FMO";
    private static final String KEY_BRAND_DISPLAY_SEQ = "BRAND_DISPLAY_SEQ";
    private static final String KEY_PACK_DISPLAY_SEQ = "PACK_DISPLAY_SEQ";



    // Table Create Statements
    public static final String CREATE_TABLE_ITEM = "CREATE TABLE " +
            TABLE_ITEM + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_ITEM_ID + " INTEGER UNIQUE," +
            KEY_ITEM_UOM_ID + " INTEGER," +
            KEY_ITEM_CODE + " TEXT UNIQUE," +
            KEY_ITEM_JSON + " TEXT," +
            KEY_PRICE_JSON + " TEXT," +
            KEY_ITEM_TYPE + " TEXT," +
            KEY_UOM_JSON + " TEXT," +
            KEY_ITEM_MRP + " REAL," +
            KEY_TRADE_RETAIL + " REAL," +
            KEY_ITEM_UOM_QUANTITY + " INTEGER," +
            KEY_IS_FMO + " INTEGER," +
            KEY_BRAND_DISPLAY_SEQ + " INTEGER," +
            KEY_PACK_DISPLAY_SEQ + " INTEGER," +
            KEY_IS_TRADE + " INTEGER," +
            KEY_BRAND_NAME + " TEXT," +
            KEY_BRAND_PACK_NAME + " TEXT," +
            KEY_ITEM_UOM + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableItem(SQLiteDatabase readableSqLiteDatabase, SQLiteDatabase writableSqLiteDatabase) {
        this.readableSqLiteDatabase = readableSqLiteDatabase;
        this.writableSqLiteDatabase = writableSqLiteDatabase;
    }

    /**
     * Creating a item
     */
    public long createItem(Item item) {

        long item_id =0;

            ContentValues values = new ContentValues();

            values.put(KEY_ITEM_ID, item.getItemId());
            values.put(KEY_ITEM_UOM_ID, item.getItemUoMId());
            values.put(KEY_ITEM_CODE, item.getItemCode());
            values.put(KEY_ITEM_JSON, item.getItemJSON());
            values.put(KEY_PRICE_JSON, item.getPriceJSON());
            values.put(KEY_UOM_JSON, item.getUomJSON());
            values.put(KEY_ITEM_MRP, item.getItemMRP());
            values.put(KEY_TRADE_RETAIL, item.getTrade_retail());
            values.put(KEY_ITEM_UOM_QUANTITY, item.getItemUoMQuantity());
            values.put(KEY_BRAND_NAME, item.getBrandName());
            values.put(KEY_BRAND_PACK_NAME, item.getBrandPackName());
            values.put(KEY_ITEM_UOM, item.getItemUoM());
            values.put(KEY_IS_TRADE, item.getIsTrade());
            values.put(KEY_ITEM_TYPE, item.getItemType());
            values.put(KEY_IS_FMO,item.getIsFMO());
            values.put(KEY_BRAND_DISPLAY_SEQ,item.getBrandDisplaySeq());
            values.put(KEY_PACK_DISPLAY_SEQ,item.getPackDisplaySeq());




        try {

            item_id = writableSqLiteDatabase.insert(TABLE_ITEM, null, values);


        }catch (Exception ex){
        }

        return item_id;
    }

    /**
     * Updating a item
     */
    public int updateItem(Item item) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID, item.getItemId());
        values.put(KEY_ITEM_UOM_ID, item.getItemUoMId());
        values.put(KEY_ITEM_CODE, item.getItemCode());
        values.put(KEY_ITEM_JSON, item.getItemJSON());
        values.put(KEY_PRICE_JSON,item.getPriceJSON());
        values.put(KEY_UOM_JSON,item.getUomJSON());
        values.put(KEY_ITEM_MRP, item.getItemMRP());
        values.put(KEY_TRADE_RETAIL, item.getTrade_retail());
        values.put(KEY_ITEM_UOM_QUANTITY, item.getItemUoMQuantity());
        values.put(KEY_BRAND_NAME, item.getBrandName());
        values.put(KEY_BRAND_PACK_NAME, item.getBrandPackName());
        values.put(KEY_ITEM_UOM, item.getItemUoM());
        values.put(KEY_IS_TRADE, item.getIsTrade());
        values.put(KEY_ITEM_TYPE, item.getItemType());
        values.put(KEY_IS_FMO,item.getIsFMO());
        values.put(KEY_BRAND_DISPLAY_SEQ,item.getBrandDisplaySeq());
        values.put(KEY_PACK_DISPLAY_SEQ,item.getPackDisplaySeq());

        return writableSqLiteDatabase.update(TABLE_ITEM, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(item.getAutoIncId())});
    }

    /**
     * Deleting a item
     */
    public int deleteItem(long item_id) {

        return writableSqLiteDatabase.delete(TABLE_ITEM, KEY_ITEM_ID + " = ?",
                new String[]{String.valueOf(item_id)});
    }

    public void deleteAllItems() {
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_ITEM);
    }

    /**
     * get single item
     */
    public Item getItem(long item_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " WHERE "
                + KEY_ITEM_ID + " = " + item_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Item item = null;

        if (c != null && c.moveToFirst()) {

            item = new Item();

            item.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            item.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            item.setItemUoMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
            item.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            item.setItemJSON(c.getString(c.getColumnIndex(KEY_ITEM_JSON)));
            item.setItemMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));
            item.setTrade_retail(c.getInt(c.getColumnIndex(KEY_TRADE_RETAIL)));
            item.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
            item.setBrandName(c.getString(c.getColumnIndex(KEY_BRAND_NAME)));
            item.setBrandPackName(c.getString(c.getColumnIndex(KEY_BRAND_PACK_NAME)));
            item.setItemUoM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
            item.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
            item.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
            item.setUomJSON(c.getString(c.getColumnIndex(KEY_UOM_JSON)));
            item.setItemType(c.getString(c.getColumnIndex(KEY_ITEM_TYPE)));
            item.setIsFMO(c.getInt(c.getColumnIndex(KEY_IS_FMO)));
            item.setBrandDisplaySeq(c.getInt(c.getColumnIndex(KEY_BRAND_DISPLAY_SEQ)));
            item.setPackDisplaySeq(c.getInt(c.getColumnIndex(KEY_PACK_DISPLAY_SEQ)));


        }

        if (c != null)
            c.close();

        return item;
    }

    /**
     * getting all items
     */
    public List<Item> getAllItems() {

        List<Item> itemArrayList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM ; // + " ORDER BY " + KEY_IS_FMO + " DESC , " + KEY_PACK_DISPLAY_SEQ + " ASC "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Item item = null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                item = new Item();

                item.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                item.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                item.setItemUoMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
                item.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                item.setItemJSON(c.getString(c.getColumnIndex(KEY_ITEM_JSON)));
                item.setItemMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));
                item.setTrade_retail(c.getInt(c.getColumnIndex(KEY_TRADE_RETAIL)));
                item.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
                item.setBrandName(c.getString(c.getColumnIndex(KEY_BRAND_NAME)));
                item.setBrandPackName(c.getString(c.getColumnIndex(KEY_BRAND_PACK_NAME)));
                item.setItemUoM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                item.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
                item.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                item.setUomJSON(c.getString(c.getColumnIndex(KEY_UOM_JSON)));
                item.setItemType(c.getString(c.getColumnIndex(KEY_ITEM_TYPE)));
                item.setIsFMO(c.getInt(c.getColumnIndex(KEY_IS_FMO)));
                item.setBrandDisplaySeq(c.getInt(c.getColumnIndex(KEY_BRAND_DISPLAY_SEQ)));
                item.setPackDisplaySeq(c.getInt(c.getColumnIndex(KEY_PACK_DISPLAY_SEQ)));

                itemArrayList.add(item);

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return itemArrayList;
    }


    /**
     * getting all items
     */
    public List<String> getAllItemPacks() {

        List<String> itemArrayList = new ArrayList<String>();
        String selectQuery = "SELECT  distinct " + KEY_BRAND_PACK_NAME  + " FROM " + TABLE_ITEM + " ORDER BY  " + KEY_PACK_DISPLAY_SEQ + " ASC "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);



        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                itemArrayList.add(c.getString(c.getColumnIndex(KEY_BRAND_PACK_NAME)));

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return itemArrayList;
    }

    /**
     * getting all items
     */
    public List<String> getAllItemBrands() {

        List<String> itemArrayList = new ArrayList<String>();
        String selectQuery = "SELECT distinct " + KEY_BRAND_NAME + "  FROM " + TABLE_ITEM + " ORDER BY  " + KEY_BRAND_DISPLAY_SEQ + " ASC "  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);



        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                itemArrayList.add(c.getString(c.getColumnIndex(KEY_BRAND_NAME)));

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return itemArrayList;
    }


    /**
     * getting all active stock items
     */
    /**
     * getting all items
     */
    public List<Item> getAllActiveStockItems() {

        List<Item> itemArrayList = new ArrayList<Item>();

        String selectQuery = "SELECT DISTINCT  TI.* FROM " + TABLE_ITEM + " TI  JOIN " + TableVehicleStock.TABLE_VEHICLE_STOCK + " TV ON  TI."+TableVehicleStock.KEY_ITEM_ID + " = TV." + KEY_ITEM_ID  + " AND ( TV."+TableVehicleStock.KEY_CASE_QUANTITY+" > 0  OR  TV."+ TableVehicleStock.KEY_BOTTLE_QUANTITY + " > 0 )";

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Item item = null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                item = new Item();

                item.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                item.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                item.setItemUoMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
                item.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                item.setItemJSON(c.getString(c.getColumnIndex(KEY_ITEM_JSON)));
                item.setItemMRP(c.getDouble(c.getColumnIndex(KEY_ITEM_MRP)));
                item.setTrade_retail(c.getInt(c.getColumnIndex(KEY_TRADE_RETAIL)));
                item.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
                item.setBrandName(c.getString(c.getColumnIndex(KEY_BRAND_NAME)));
                item.setBrandPackName(c.getString(c.getColumnIndex(KEY_BRAND_PACK_NAME)));
                item.setItemUoM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                item.setIsTrade(c.getInt(c.getColumnIndex(KEY_IS_TRADE)));
                item.setPriceJSON(c.getString(c.getColumnIndex(KEY_PRICE_JSON)));
                item.setUomJSON(c.getString(c.getColumnIndex(KEY_UOM_JSON)));
                item.setItemType(c.getString(c.getColumnIndex(KEY_ITEM_TYPE)));
                item.setIsFMO(c.getInt(c.getColumnIndex(KEY_IS_FMO)));
                item.setBrandDisplaySeq(c.getInt(c.getColumnIndex(KEY_BRAND_DISPLAY_SEQ)));
                item.setPackDisplaySeq(c.getInt(c.getColumnIndex(KEY_PACK_DISPLAY_SEQ)));

                itemArrayList.add(item);

            } while (c.moveToNext());
        }


        if (c != null)
            c.close();

        return itemArrayList;
    }










}

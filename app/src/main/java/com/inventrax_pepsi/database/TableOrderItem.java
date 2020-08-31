package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.OrderItem;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableOrderItem {

    // Table Names
    public static final String TABLE_ORDER_ITEM = "SFA_ORDER_ITEM";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_ITEM_UOM_ID="ITEM_UOM_ID";
    private static final String KEY_DISCOUNT_ITEM_UOM_ID="DISCOUNT_ITEM_UOM_ID";
    private static final String KEY_DISCOUNT_ITEM_ID="DISCOUNT_ITEM_ID";
    private static final String KEY_ITEM_UOM_QUANTITY="ITEM_UOM_QUANTITY";
    private static final String KEY_ORDER_CODE="ORDER_CODE";
    private static final String KEY_ITEM_CODE="ITEM_CODE";
    private static final String KEY_ITEM_UOM="ITEM_UOM";
    private static final String KEY_DISCOUNT_ITEM_CODE="DISCOUNT_ITEM_CODE";
    private static final String KEY_DISCOUNT_ITEM_UOM="DISCOUNT_ITEM_UOM";
    private static final String KEY_QUANTITY="QUANTITY";
    private static final String KEY_PRICE="PRICE";
    private static final String KEY_DISCOUNT_PRICE="DISCOUNT_PRICE";
    private static final String KEY_DERIVED_PRICE="DERIVED_PRICE";

    // Table Create Statements
    public static final String CREATE_TABLE_ORDER_ITEM= "CREATE TABLE " +
            TABLE_ORDER_ITEM + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_ITEM_ID + " INTEGER ," +
            KEY_ITEM_UOM_ID + " INTEGER," +
            KEY_DISCOUNT_ITEM_UOM_ID + " INTEGER," +
            KEY_DISCOUNT_ITEM_ID+ " INTEGER," +
            KEY_ITEM_UOM_QUANTITY + " INTEGER ," +
            KEY_ORDER_CODE + " TEXT," +
            KEY_ITEM_CODE + " TEXT," +
            KEY_ITEM_UOM + " TEXT," +
            KEY_DISCOUNT_ITEM_CODE + " TEXT ," +
            KEY_DISCOUNT_ITEM_UOM + " TEXT," +
            KEY_QUANTITY + " REAL," +
            KEY_PRICE + " REAL," +
            KEY_DERIVED_PRICE + " REAL ," +
            KEY_DISCOUNT_PRICE + " REAL " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableOrderItem(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }


    /**
     * Creating a Order Item
     */
    public long createOrderItem(OrderItem orderItem) {

        ContentValues values = new ContentValues();


        values.put(KEY_ITEM_ID,orderItem.getItemId());
        values.put(KEY_ITEM_UOM_ID,orderItem.getItemUOMId());
        values.put(KEY_DISCOUNT_ITEM_UOM_ID,orderItem.getDiscountItemUomId());
        values.put(KEY_DISCOUNT_ITEM_ID,orderItem.getDiscountItemId());
        values.put(KEY_ITEM_UOM_QUANTITY,orderItem.getItemUoMQuantity());
        values.put(KEY_ORDER_CODE,orderItem.getOrderCode());
        values.put(KEY_ITEM_CODE,orderItem.getItemCode());
        values.put(KEY_ITEM_UOM,orderItem.getItemUOM());
        values.put(KEY_DISCOUNT_ITEM_CODE,orderItem.getDiscountItemCode());
        values.put(KEY_DISCOUNT_ITEM_UOM,orderItem.getDiscountItemUOM());
        values.put(KEY_QUANTITY,orderItem.getQuantity());
        values.put(KEY_PRICE,orderItem.getPrice());
        values.put(KEY_DISCOUNT_PRICE,orderItem.getDiscountPrice());
        values.put(KEY_DERIVED_PRICE,orderItem.getDerivedPrice());


        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ORDER_ITEM, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a Order Item
     */
    public long updateOrderItem(OrderItem orderItem) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID,orderItem.getItemId());
        values.put(KEY_ITEM_UOM_ID,orderItem.getItemUOMId());
        values.put(KEY_DISCOUNT_ITEM_UOM_ID,orderItem.getDiscountItemUomId());
        values.put(KEY_DISCOUNT_ITEM_ID,orderItem.getDiscountItemId());
        values.put(KEY_ITEM_UOM_QUANTITY,orderItem.getItemUoMQuantity());
        values.put(KEY_ORDER_CODE,orderItem.getOrderCode());
        values.put(KEY_ITEM_CODE,orderItem.getItemCode());
        values.put(KEY_ITEM_UOM,orderItem.getItemUOM());
        values.put(KEY_DISCOUNT_ITEM_CODE,orderItem.getDiscountItemCode());
        values.put(KEY_DISCOUNT_ITEM_UOM,orderItem.getDiscountItemUOM());
        values.put(KEY_QUANTITY,orderItem.getQuantity());
        values.put(KEY_PRICE,orderItem.getPrice());
        values.put(KEY_DISCOUNT_PRICE,orderItem.getDiscountPrice());
        values.put(KEY_DERIVED_PRICE,orderItem.getDerivedPrice());

        return writableSqLiteDatabase.update(TABLE_ORDER_ITEM, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(orderItem.getAutoIncId())});
    }


    /**
     * Deleting a order item
     */
    public int deleteOrderItem(long auto_inc_id) {
        return writableSqLiteDatabase.delete(TABLE_ORDER_ITEM, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});
    }

    public void deleteAllOrderItems(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ORDER_ITEM);
    }

    /**
     * get single order item
     */
    public OrderItem getOrderItem(String order_code) {

        String selectQuery = "SELECT  * FROM " + TABLE_ORDER_ITEM + " WHERE "
                + KEY_ORDER_CODE + " = '" + order_code+"'";
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        OrderItem orderItem=null;

        if (c != null && c.moveToFirst() ) {

            orderItem = new OrderItem();

            orderItem.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            orderItem.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            orderItem.setItemUOMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
            orderItem.setDiscountItemUomId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ITEM_UOM_ID)));
            orderItem.setDiscountItemId(c.getInt(c.getColumnIndex(KEY_DISCOUNT_ITEM_ID)));
            orderItem.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
            orderItem.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
            orderItem.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            orderItem.setItemUOM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
            orderItem.setDiscountItemCode(c.getString(c.getColumnIndex(KEY_DISCOUNT_ITEM_CODE)));
            orderItem.setDiscountItemUOM(c.getString(c.getColumnIndex(KEY_DISCOUNT_ITEM_UOM)));
            orderItem.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
            orderItem.setPrice(c.getInt(c.getColumnIndex(KEY_PRICE)));
            orderItem.setDiscountPrice(c.getDouble(c.getColumnIndex(KEY_DISCOUNT_PRICE)));
            orderItem.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));

        }

        if (c!=null)
            c.close();

        return orderItem;
    }








}



package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableOrder {

    // Table Names
    public static final String TABLE_ORDER = "SFA_ORDER";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_CUSTOMER_ID="CUSTOMER_ID";
    private static final String KEY_ROUTE_ID="ROUTE_ID";
    private static final String KEY_ORDER_ID="ORDER_ID";
    private static final String KEY_ORDER_TYPE="ORDER_TYPE";
    private static final String KEY_PAYMENT_STATUS="PAYMENT_STATUS";
    private static final String KEY_ORDER_STATUS="ORDER_STATUS";
    private static final String KEY_JSON_MESSAGE_AUTO_INC_ID="JSON_MESSAGE_AUTO_INC_ID";
    private static final String KEY_PAYMENT_MODE="PAYMENT_MODE";
    private static final String KEY_ORDER_CODE="ORDER_CODE";
    private static final String KEY_CUSTOMER_CODE="CUSTOMER_CODE";
    private static final String KEY_ROUTE_CODE="ROUTE_CODE";
    private static final String KEY_ORDER_QUANTITY="ORDER_QUANTITY";
    private static final String KEY_ORDER_PRICE="ORDER_PRICE";
    private static final String KEY_DERIVED_PRICE="DERIVED_PRICE";
    private static final String KEY_CREATED_ON="CREATED_ON";
    private static final String KEY_ORDER_JSON="ORDER_JSON";


    // Table Create Statements
    public static final String CREATE_TABLE_ORDER= "CREATE TABLE " +
            TABLE_ORDER + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_CUSTOMER_ID + " INTEGER ," +
            KEY_ORDER_ID + " INTEGER ," +
            KEY_ORDER_TYPE + " INTEGER," +
            KEY_ROUTE_ID + " INTEGER," +
            KEY_PAYMENT_STATUS+ " INTEGER," +
            KEY_CUSTOMER_CODE + " TEXT ," +
            KEY_ORDER_STATUS + " INTEGER," +
            KEY_JSON_MESSAGE_AUTO_INC_ID + " INTEGER," +
            KEY_PAYMENT_MODE + " INTEGER," +
            KEY_ORDER_CODE + " TEXT UNIQUE," +
            KEY_ORDER_QUANTITY + " REAL," +
            KEY_ORDER_PRICE + " REAL," +
            KEY_ROUTE_CODE + " TEXT," +
            KEY_DERIVED_PRICE + " REAL ," +
            KEY_ORDER_JSON + " TEXT ," +
            KEY_CREATED_ON + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableOrder(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Order
     */
    public long createOrder(Order order) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,order.getCustomerId());
        values.put(KEY_ROUTE_ID,order.getRouteId());
        values.put(KEY_ORDER_TYPE,order.getOrderType());
        values.put(KEY_PAYMENT_STATUS,order.getPaymentStatus());
        values.put(KEY_ORDER_STATUS,order.getOrderStatus());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,order.getJsonMessageAutoIncId());
        values.put(KEY_PAYMENT_MODE,order.getPaymentMode());
        values.put(KEY_ORDER_CODE,order.getOrderCode());
        values.put(KEY_CUSTOMER_CODE,order.getCustomerCode());
        values.put(KEY_ROUTE_CODE,order.getRouteCode());
        values.put(KEY_ORDER_QUANTITY,order.getOrderQuantity());
        values.put(KEY_ORDER_PRICE,order.getOrderPrice());
        values.put(KEY_DERIVED_PRICE,order.getDerivedPrice());
        values.put(KEY_CREATED_ON,DatabaseHelper.getDateTime());
        values.put(KEY_ORDER_JSON,order.getOrderJSON());
        values.put(KEY_ORDER_ID,order.getOrderId());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_ORDER, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a Order
     */
    public long updateOrder(Order order) {

        ContentValues values = new ContentValues();

        values.put(KEY_CUSTOMER_ID,order.getCustomerId());
        values.put(KEY_ROUTE_ID,order.getRouteId());
        values.put(KEY_ORDER_TYPE,order.getOrderType());
        values.put(KEY_PAYMENT_STATUS,order.getPaymentStatus());
        values.put(KEY_ORDER_STATUS,order.getOrderStatus());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,order.getJsonMessageAutoIncId());
        values.put(KEY_PAYMENT_MODE,order.getPaymentMode());
        values.put(KEY_ORDER_CODE,order.getOrderCode());
        values.put(KEY_CUSTOMER_CODE,order.getCustomerCode());
        values.put(KEY_ROUTE_CODE,order.getRouteCode());
        values.put(KEY_ORDER_QUANTITY,order.getOrderQuantity());
        values.put(KEY_ORDER_PRICE,order.getOrderPrice());
        values.put(KEY_DERIVED_PRICE,order.getDerivedPrice());
        values.put(KEY_CREATED_ON, DatabaseHelper.getDateTime() );
        values.put(KEY_ORDER_JSON,order.getOrderJSON());
        values.put(KEY_ORDER_ID,order.getOrderId());

        return writableSqLiteDatabase.update(TABLE_ORDER, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(order.getAutoIncId())});

    }


    public long updateOrderJSON(Order order) {

        ContentValues values = new ContentValues();

        values.put(KEY_PAYMENT_STATUS,order.getPaymentStatus());
        values.put(KEY_ORDER_STATUS,order.getOrderStatus());
        values.put(KEY_JSON_MESSAGE_AUTO_INC_ID,order.getJsonMessageAutoIncId());
        values.put(KEY_PAYMENT_MODE,order.getPaymentMode());
        values.put(KEY_ORDER_JSON,order.getOrderJSON());

        return writableSqLiteDatabase.update(TABLE_ORDER, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(order.getAutoIncId())});

    }

    public long updateOrderStatus(Order order) {

        ContentValues values = new ContentValues();

        values.put(KEY_ORDER_STATUS,order.getOrderStatus());
        values.put(KEY_ORDER_JSON,order.getOrderJSON());

        return writableSqLiteDatabase.update(TABLE_ORDER, values, KEY_ORDER_CODE + " = ?",
                new String[]{String.valueOf(order.getOrderCode())});

    }



    /**
     * Deleting a order
     */

    public int deleteOrder(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_ORDER, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});
    }

    public void deleteAllOrders(){
        writableSqLiteDatabase.execSQL("DELETE FROM "+TABLE_ORDER);
    }

    /**
     * get single order
     */
    public Order getOrder(String order_code) {

        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " WHERE "
                + KEY_ORDER_CODE + " = '" + order_code+"'";
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Order order=null;

        if (c != null && c.moveToFirst()) {

            order = new Order();


            order.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            order.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            order.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            order.setOrderType(c.getInt(c.getColumnIndex(KEY_ORDER_TYPE)));
            order.setPaymentStatus(c.getInt(c.getColumnIndex(KEY_PAYMENT_STATUS)));
            order.setOrderStatus(c.getInt(c.getColumnIndex(KEY_ORDER_STATUS)));
            order.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));
            order.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
            order.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
            order.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            order.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            order.setOrderQuantity(c.getDouble(c.getColumnIndex(KEY_ORDER_QUANTITY)));
            order.setOrderPrice(c.getDouble(c.getColumnIndex(KEY_ORDER_PRICE)));
            order.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));
            order.setCreatedOn(c.getString(c.getColumnIndex(KEY_CREATED_ON)));
            order.setOrderJSON(c.getString(c.getColumnIndex(KEY_ORDER_JSON)));
            order.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));

        }

        if (c!=null)
            c.close();

        return order;
    }

    /**
     * get single order
     */
    public Order getOrder(int auto_sync_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_sync_id + " ORDER BY " + KEY_AUTO_INC_ID + " ASC " ;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Order order=null;

        if (c != null && c.moveToFirst()) {

            order = new Order();


            order.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            order.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
            order.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
            order.setOrderType(c.getInt(c.getColumnIndex(KEY_ORDER_TYPE)));
            order.setPaymentStatus(c.getInt(c.getColumnIndex(KEY_PAYMENT_STATUS)));
            order.setOrderStatus(c.getInt(c.getColumnIndex(KEY_ORDER_STATUS)));
            order.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));
            order.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
            order.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
            order.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
            order.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
            order.setOrderQuantity(c.getDouble(c.getColumnIndex(KEY_ORDER_QUANTITY)));
            order.setOrderPrice(c.getDouble(c.getColumnIndex(KEY_ORDER_PRICE)));
            order.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));
            order.setCreatedOn(c.getString(c.getColumnIndex(KEY_CREATED_ON)));
            order.setOrderJSON(c.getString(c.getColumnIndex(KEY_ORDER_JSON)));
            order.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));

        }

        if (c!=null)
            c.close();

        return order;
    }


    /**
     * getting all Orders
     */
    public List<Order> getAllOrders() {
        List<Order> orderArrayList = new ArrayList<Order>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Order order=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                order = new Order();

                order.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                order.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                order.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                order.setOrderType(c.getInt(c.getColumnIndex(KEY_ORDER_TYPE)));
                order.setPaymentStatus(c.getInt(c.getColumnIndex(KEY_PAYMENT_STATUS)));
                order.setOrderStatus(c.getInt(c.getColumnIndex(KEY_ORDER_STATUS)));
                order.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));
                order.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
                order.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
                order.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                order.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                order.setOrderQuantity(c.getDouble(c.getColumnIndex(KEY_ORDER_QUANTITY)));
                order.setOrderPrice(c.getDouble(c.getColumnIndex(KEY_ORDER_PRICE)));
                order.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));
                order.setCreatedOn(c.getString(c.getColumnIndex(KEY_CREATED_ON)));
                order.setOrderJSON(c.getString(c.getColumnIndex(KEY_ORDER_JSON)));
                order.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));

                orderArrayList.add(order);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return orderArrayList;
    }


    public List<Order> getAllOrdersByCustomerId(int customer_id) {
        List<Order> orderArrayList = new ArrayList<Order>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " WHERE   " + customer_id + " = 0  OR  " + KEY_CUSTOMER_ID + " = "  + customer_id  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Order order=null;

        // looping through all rows and adding to list
        if (c!=null && c.moveToFirst()) {
            do {

                order = new Order();

                order.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                order.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                order.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                order.setOrderType(c.getInt(c.getColumnIndex(KEY_ORDER_TYPE)));
                order.setPaymentStatus(c.getInt(c.getColumnIndex(KEY_PAYMENT_STATUS)));
                order.setOrderStatus(c.getInt(c.getColumnIndex(KEY_ORDER_STATUS)));
                order.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));
                order.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
                order.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
                order.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                order.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                order.setOrderQuantity(c.getDouble(c.getColumnIndex(KEY_ORDER_QUANTITY)));
                order.setOrderPrice(c.getDouble(c.getColumnIndex(KEY_ORDER_PRICE)));
                order.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));
                order.setCreatedOn(c.getString(c.getColumnIndex(KEY_CREATED_ON)));
                order.setOrderJSON(c.getString(c.getColumnIndex(KEY_ORDER_JSON)));
                order.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));

                orderArrayList.add(order);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return orderArrayList;
    }


    public List<Order> getAllOrdersByRouteCode(String route_code) {
        List<Order> orderArrayList = new ArrayList<Order>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER + " WHERE  " + KEY_ROUTE_CODE + " = '"  + route_code +"'" ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Order order=null;

        // looping through all rows and adding to list
        if (c!=null && c.moveToFirst()) {
            do {

                order = new Order();

                order.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                order.setCustomerId(c.getInt(c.getColumnIndex(KEY_CUSTOMER_ID)));
                order.setRouteId(c.getInt(c.getColumnIndex(KEY_ROUTE_ID)));
                order.setOrderType(c.getInt(c.getColumnIndex(KEY_ORDER_TYPE)));
                order.setPaymentStatus(c.getInt(c.getColumnIndex(KEY_PAYMENT_STATUS)));
                order.setOrderStatus(c.getInt(c.getColumnIndex(KEY_ORDER_STATUS)));
                order.setJsonMessageAutoIncId(c.getInt(c.getColumnIndex(KEY_JSON_MESSAGE_AUTO_INC_ID)));
                order.setPaymentMode(c.getInt(c.getColumnIndex(KEY_PAYMENT_MODE)));
                order.setOrderCode(c.getString(c.getColumnIndex(KEY_ORDER_CODE)));
                order.setCustomerCode(c.getString(c.getColumnIndex(KEY_CUSTOMER_CODE)));
                order.setRouteCode(c.getString(c.getColumnIndex(KEY_ROUTE_CODE)));
                order.setOrderQuantity(c.getDouble(c.getColumnIndex(KEY_ORDER_QUANTITY)));
                order.setOrderPrice(c.getDouble(c.getColumnIndex(KEY_ORDER_PRICE)));
                order.setDerivedPrice(c.getDouble(c.getColumnIndex(KEY_DERIVED_PRICE)));
                order.setCreatedOn(c.getString(c.getColumnIndex(KEY_CREATED_ON)));
                order.setOrderJSON(c.getString(c.getColumnIndex(KEY_ORDER_JSON)));
                order.setOrderId(c.getInt(c.getColumnIndex(KEY_ORDER_ID)));

                orderArrayList.add(order);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return orderArrayList;
    }


}

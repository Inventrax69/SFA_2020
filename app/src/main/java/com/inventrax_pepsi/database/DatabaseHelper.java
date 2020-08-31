package com.inventrax_pepsi.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inventrax_pepsi.application.AbstractApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by android on 1/29/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 5 ;

    // Database Name
    private static final String DATABASE_NAME = "SFA.db";

    private static DatabaseHelper databaseHelper;

    private TableCustomer tableCustomer;
    private TableItem tableItem;
    private TableItemTransaction tableItemTransaction;
    private TableJSONMessage tableJSONMessage;
    private TableOrder tableOrder;
    private TableOrderItem tableOrderItem;
    private TableScheme tableScheme;
    private TableUser tableUser;
    private TableUserTracking tableUserTracking;
    private TableVanInventory tableVanInventory;
    private TableCustomerPrice tableCustomerPrice;
    private TableCustomerDiscount tableCustomerDiscount;
    private TableInvoice tableInvoice;
    private TableVehicleLoad tableVehicleLoad;
    private TableVehicleStock tableVehicleStock;
    private TableCustomerOrderHistory tableCustomerOrderHistory;
    private TableCustomerReturn tableCustomerReturn;
    private TableAssetComplaint tableAssetComplaint;
    private TableAssetCapture tableAssetCapture;
    private TableReadySaleInvoice tableReadySaleInvoice;
    private TableAssetPullout tableAssetPullout;
    private TableAssetRequest tableAssetRequest;
    private TableCustomerTrans tableCustomerTrans;


    public DatabaseHelper() {

        super(AbstractApplication.get(), DATABASE_NAME, null, DATABASE_VERSION);

        tableCustomer=new TableCustomer(this.getReadableDatabase(),this.getWritableDatabase());
        tableItem=new TableItem(this.getReadableDatabase(),this.getWritableDatabase());
        tableItemTransaction=new TableItemTransaction(this.getReadableDatabase(),this.getWritableDatabase());
        tableJSONMessage=new TableJSONMessage(this.getReadableDatabase(),this.getWritableDatabase());
        tableOrder=new TableOrder(this.getReadableDatabase(),this.getWritableDatabase());
        tableOrderItem=new TableOrderItem(this.getReadableDatabase(),this.getWritableDatabase());
        tableScheme=new TableScheme(this.getReadableDatabase(),this.getWritableDatabase());
        tableUser=new TableUser(this.getReadableDatabase(),this.getWritableDatabase());
        tableUserTracking=new TableUserTracking(this.getReadableDatabase(),this.getWritableDatabase());
        tableVanInventory=new TableVanInventory(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerDiscount=new TableCustomerDiscount(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerPrice=new TableCustomerPrice(this.getReadableDatabase(),this.getWritableDatabase());
        tableInvoice=new TableInvoice(this.getReadableDatabase(),this.getWritableDatabase());
        tableVehicleLoad=new TableVehicleLoad(this.getReadableDatabase(),this.getWritableDatabase());
        tableVehicleStock=new TableVehicleStock(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerOrderHistory=new TableCustomerOrderHistory(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerReturn=new TableCustomerReturn(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetComplaint=new TableAssetComplaint(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetCapture=new TableAssetCapture(this.getReadableDatabase(),this.getWritableDatabase());
        tableReadySaleInvoice=new TableReadySaleInvoice(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetPullout = new TableAssetPullout(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetRequest  = new TableAssetRequest(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerTrans = new TableCustomerTrans(this.getReadableDatabase(),this.getWritableDatabase());


    }

    public DatabaseHelper(int dbVersion) {

        super(AbstractApplication.get(), DATABASE_NAME, null, dbVersion);

        tableCustomer=new TableCustomer(this.getReadableDatabase(),this.getWritableDatabase());
        tableItem=new TableItem(this.getReadableDatabase(),this.getWritableDatabase());
        tableItemTransaction=new TableItemTransaction(this.getReadableDatabase(),this.getWritableDatabase());
        tableJSONMessage=new TableJSONMessage(this.getReadableDatabase(),this.getWritableDatabase());
        tableOrder=new TableOrder(this.getReadableDatabase(),this.getWritableDatabase());
        tableOrderItem=new TableOrderItem(this.getReadableDatabase(),this.getWritableDatabase());
        tableScheme=new TableScheme(this.getReadableDatabase(),this.getWritableDatabase());
        tableUser=new TableUser(this.getReadableDatabase(),this.getWritableDatabase());
        tableUserTracking=new TableUserTracking(this.getReadableDatabase(),this.getWritableDatabase());
        tableVanInventory=new TableVanInventory(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerDiscount=new TableCustomerDiscount(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerPrice=new TableCustomerPrice(this.getReadableDatabase(),this.getWritableDatabase());
        tableInvoice=new TableInvoice(this.getReadableDatabase(),this.getWritableDatabase());
        tableVehicleLoad=new TableVehicleLoad(this.getReadableDatabase(),this.getWritableDatabase());
        tableVehicleStock=new TableVehicleStock(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerOrderHistory=new TableCustomerOrderHistory(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerReturn=new TableCustomerReturn(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetComplaint=new TableAssetComplaint(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetCapture=new TableAssetCapture(this.getReadableDatabase(),this.getWritableDatabase());
        tableReadySaleInvoice=new TableReadySaleInvoice(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetPullout = new TableAssetPullout(this.getReadableDatabase(),this.getWritableDatabase());
        tableAssetRequest  = new TableAssetRequest(this.getReadableDatabase(),this.getWritableDatabase());
        tableCustomerTrans = new TableCustomerTrans(this.getReadableDatabase(),this.getWritableDatabase());

    }

    public static synchronized DatabaseHelper getInstance() {

        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;
    }

    /**
     * get datetime
     */
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TableCustomer.CREATE_TABLE_CUSTOMER);
        db.execSQL(TableItem.CREATE_TABLE_ITEM);
        db.execSQL(TableItemTransaction.CREATE_TABLE_ITEM_TRANSACTION);
        db.execSQL(TableJSONMessage.CREATE_TABLE_JSON_MESSAGE);
        db.execSQL(TableOrder.CREATE_TABLE_ORDER);
        db.execSQL(TableOrderItem.CREATE_TABLE_ORDER_ITEM);
        db.execSQL(TableScheme.CREATE_TABLE_SCHEME);
        db.execSQL(TableUser.CREATE_TABLE_USER);
        db.execSQL(TableUserTracking.CREATE_TABLE_USER_TRACKING);
        db.execSQL(TableVanInventory.CREATE_TABLE_VAN_INVENTORY);
        db.execSQL(TableCustomerDiscount.CREATE_TABLE_CUSTOMER_DISCOUNT);
        db.execSQL(TableCustomerPrice.CREATE_TABLE_CUSTOMER_PRICE);
        db.execSQL(TableInvoice.CREATE_TABLE_INVOICE);
        db.execSQL(TableVehicleLoad.CREATE_TABLE_VEHICLE_LOAD);
        db.execSQL(TableVehicleStock.CREATE_TABLE_VEHICLE_STOCK);
        db.execSQL(TableCustomerOrderHistory.CREATE_TABLE_CUSTOMER_ORDER_HISTORY);
        db.execSQL(TableCustomerReturn.CREATE_TABLE_CUSTOMER_RETURN);
        db.execSQL(TableAssetComplaint.CREATE_TABLE_ASSET_COMPLAINT);
        db.execSQL(TableAssetCapture.CREATE_TABLE_ASSET_CAPTURE);
        db.execSQL(TableReadySaleInvoice.CREATE_TABLE_READY_SALE_INVOICE);
        db.execSQL(TableAssetPullout.CREATE_TABLE_ASSET_PULLOUT);
        db.execSQL(TableAssetRequest.CREATE_TABLE_ASSET_REQUEST);
        db.execSQL(TableCustomerTrans.CREATE_TABLE_CUSTOMER_TRANS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " +TableCustomer.TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " +TableItem.TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " +TableItemTransaction.TABLE_ITEM_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " +TableJSONMessage.TABLE_JSON_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " +TableOrder.TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " +TableOrderItem.TABLE_ORDER_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " +TableScheme.TABLE_SCHEME);
        db.execSQL("DROP TABLE IF EXISTS " +TableUser.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " +TableUserTracking.TABLE_USER_TRACKING);
        db.execSQL("DROP TABLE IF EXISTS " +TableVanInventory.TABLE_VAN_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TableCustomerPrice.TABLE_CUSTOMER_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TableCustomerDiscount.TABLE_CUSTOMER_DISCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TableInvoice.TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TableVehicleStock.TABLE_VEHICLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TableVehicleLoad.TABLE_VEHICLE_LOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TableCustomerOrderHistory.TABLE_CUSTOMER_ORDER_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TableCustomerReturn.TABLE_CUSTOMER_RETURN);
        db.execSQL("DROP TABLE IF EXISTS " + TableAssetComplaint.TABLE_ASSET_COMPLAINT);
        db.execSQL("DROP TABLE IF EXISTS " + TableAssetCapture.TABLE_ASSET_CAPTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TableReadySaleInvoice.TABLE_READY_SALE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TableAssetPullout.TABLE_ASSET_PULLOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TableAssetRequest.TABLE_ASSET_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TableCustomerTrans.TABLE_CUSTOMER_TRANS);

        // create new tables
        onCreate(db);

    }


    public TableVanInventory getTableVanInventory() {
        return tableVanInventory;
    }

    public TableUserTracking getTableUserTracking() {
        return tableUserTracking;
    }

    public TableUser getTableUser() {
        return tableUser;
    }

    public TableScheme getTableScheme() {
        return tableScheme;
    }

    public TableOrderItem getTableOrderItem() {
        return tableOrderItem;
    }

    public TableOrder getTableOrder() {
        return tableOrder;
    }

    public TableJSONMessage getTableJSONMessage() {
        return tableJSONMessage;
    }

    public TableItemTransaction getTableItemTransaction() {
        return tableItemTransaction;
    }

    public TableItem getTableItem() {
        return tableItem;
    }

    public TableCustomer getTableCustomer() {
        return tableCustomer;
    }

    public TableCustomerPrice getTableCustomerPrice() {
        return tableCustomerPrice;
    }

    public TableCustomerDiscount getTableCustomerDiscount() {
        return tableCustomerDiscount;
    }

    public TableInvoice getTableInvoice() {
        return tableInvoice;
    }

    public TableVehicleLoad getTableVehicleLoad() {
        return tableVehicleLoad;
    }

    public TableVehicleStock getTableVehicleStock() {
        return tableVehicleStock;
    }

    public TableCustomerOrderHistory getTableCustomerOrderHistory() {
        return tableCustomerOrderHistory;
    }

    public TableCustomerReturn getTableCustomerReturn() {
        return tableCustomerReturn;
    }

    public TableAssetComplaint getTableAssetComplaint() {
        return tableAssetComplaint;
    }

    public TableAssetCapture getTableAssetCapture() {
        return tableAssetCapture;
    }

    public TableReadySaleInvoice getTableReadySaleInvoice() {
        return tableReadySaleInvoice;
    }

    public TableAssetPullout getTableAssetPullout() {
        return tableAssetPullout;
    }

    public TableAssetRequest getTableAssetRequest() { return tableAssetRequest; }

    public TableCustomerTrans getTableCustomerTrans() {
        return tableCustomerTrans;
    }
}

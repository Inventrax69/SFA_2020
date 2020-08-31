package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.ItemTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableItemTransaction {

    // Table Names
    public static final String TABLE_ITEM_TRANSACTION = "SFA_ITEM_TRANSACTION";

    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_ITEM_UOM_ID="ITEM_UOM_ID";
    private static final String KEY_ITEM_CODE="ITEM_CODE";
    private static final String KEY_ITEM_UOM="ITEM_UOM";
    private static final String KEY_ITEM_UOM_QUANTITY="ITEM_UOM_QUANTITY";
    private static final String KEY_NO_OF_BOTTLES="NO_OF_BOTTLES";
    private static final String KEY_CR_DR="CR_DR";
    private static final String KEY_QUANTITY="QUANTITY";

    // Table Create Statements
    public static final String CREATE_TABLE_ITEM_TRANSACTION= "CREATE TABLE " +
            TABLE_ITEM_TRANSACTION + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_ITEM_ID + " INTEGER ," +
            KEY_ITEM_UOM_ID + " INTEGER," +
            KEY_ITEM_CODE + " TEXT ," +
            KEY_CR_DR + " INTEGER," +
            KEY_NO_OF_BOTTLES + " INTEGER," +
            KEY_ITEM_UOM_QUANTITY + " INTEGER," +
            KEY_QUANTITY + " REAL," +
            KEY_ITEM_UOM + " TEXT "  + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableItemTransaction(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a Item Transaction
     */
    public long createItemTransaction(ItemTransaction itemTransaction) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID,itemTransaction.getItemId());
        values.put(KEY_ITEM_UOM_ID,itemTransaction.getItemUoMId());
        values.put(KEY_ITEM_CODE,itemTransaction.getItemCode());
        values.put(KEY_ITEM_UOM,itemTransaction.getItemUoM());
        values.put(KEY_ITEM_UOM_QUANTITY,itemTransaction.getItemUoMQuantity());
        values.put(KEY_NO_OF_BOTTLES,itemTransaction.getNoOfBottles());
        values.put(KEY_CR_DR,itemTransaction.getCR_DR());
        values.put(KEY_QUANTITY,itemTransaction.getQuantity());

        long item_transaction_id = writableSqLiteDatabase.insert(TABLE_ITEM_TRANSACTION, null, values);

        return item_transaction_id ;
    }

    /**
     * Updating a customer
     */
    public int updateItemTransaction(ItemTransaction itemTransaction) {

        ContentValues values = new ContentValues();

        values.put(KEY_ITEM_ID,itemTransaction.getItemId());
        values.put(KEY_ITEM_UOM_ID,itemTransaction.getItemUoMId());
        values.put(KEY_ITEM_CODE,itemTransaction.getItemCode());
        values.put(KEY_ITEM_UOM,itemTransaction.getItemUoM());
        values.put(KEY_ITEM_UOM_QUANTITY,itemTransaction.getItemUoMQuantity());
        values.put(KEY_NO_OF_BOTTLES,itemTransaction.getNoOfBottles());
        values.put(KEY_CR_DR,itemTransaction.getCR_DR());
        values.put(KEY_QUANTITY,itemTransaction.getQuantity());


        return writableSqLiteDatabase.update(TABLE_ITEM_TRANSACTION, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(itemTransaction.getAutoIncId())});
    }

    /**
     * Deleting a item transaction
     */
    public int deleteItemTransaction(long auto_inc_id) {

        return writableSqLiteDatabase.delete(TABLE_ITEM_TRANSACTION, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(auto_inc_id)});
    }

    public void deleteAllItemTransactions(){
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_ITEM_TRANSACTION);
    }

    /**
     * get single Item Transaction
     */
    public ItemTransaction getItemTransaction(long auto_inc_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_TRANSACTION + " WHERE "
                + KEY_AUTO_INC_ID + " = " + auto_inc_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        ItemTransaction itemTransaction=null;

        if (c != null && c.moveToFirst() ) {

            itemTransaction = new ItemTransaction();

            itemTransaction.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            itemTransaction.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            itemTransaction.setItemUoMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
            itemTransaction.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
            itemTransaction.setItemUoM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
            itemTransaction.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
            itemTransaction.setNoOfBottles(c.getInt(c.getColumnIndex(KEY_NO_OF_BOTTLES)));
            itemTransaction.setCR_DR(c.getInt(c.getColumnIndex(KEY_CR_DR)));
            itemTransaction.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
        }

        if (c!=null)
            c.close();

        return itemTransaction;
    }

    /**
     * getting all item transactions
     */
    public List<ItemTransaction> getAllItemTransactions() {
        List<ItemTransaction> itemTransactionArrayList = new ArrayList<ItemTransaction>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM_TRANSACTION;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        ItemTransaction itemTransaction=null;

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                itemTransaction = new ItemTransaction();

                itemTransaction.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                itemTransaction.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                itemTransaction.setItemUoMId(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_ID)));
                itemTransaction.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));
                itemTransaction.setItemUoM(c.getString(c.getColumnIndex(KEY_ITEM_UOM)));
                itemTransaction.setItemUoMQuantity(c.getInt(c.getColumnIndex(KEY_ITEM_UOM_QUANTITY)));
                itemTransaction.setNoOfBottles(c.getInt(c.getColumnIndex(KEY_NO_OF_BOTTLES)));
                itemTransaction.setCR_DR(c.getInt(c.getColumnIndex(KEY_CR_DR)));
                itemTransaction.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));


                itemTransactionArrayList.add(itemTransaction);

            } while (c.moveToNext());
        }

        if (c!=null)
            c.close();

        return itemTransactionArrayList;
    }

}



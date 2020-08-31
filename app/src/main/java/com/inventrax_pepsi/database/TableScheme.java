package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.Scheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableScheme {

    // Table Names
    public static final String TABLE_SCHEME = "SFA_SCHEME";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_SCHEME_ID="SCHEME_ID";
    private static final String KEY_ITEM_ID="ITEM_ID";
    private static final String KEY_SCHEME_CODE="SCHEME_CODE";
    private static final String KEY_SCHEME_NAME="SCHEME_NAME";
    private static final String KEY_SCHEME_JSON="SCHEME_JSON";
    private static final String KEY_ITEM_CODE="ITEM_CODE";

    private static final String KEY_UOM_ID="UOM_ID";
    private static final String KEY_UOM_CODE="UOM_CODE";
    private static final String KEY_QUANTITY="QUANTITY";

    // Table Create Statements
    public static final String CREATE_TABLE_SCHEME= "CREATE TABLE " +
            TABLE_SCHEME + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_SCHEME_ID + " INTEGER ," +
            KEY_ITEM_ID + " INTEGER," +
            KEY_UOM_ID + " INTEGER," +
            KEY_UOM_CODE + " TEXT," +
            KEY_QUANTITY + " REAL," +
            KEY_SCHEME_CODE + " TEXT," +
            KEY_SCHEME_NAME+ " TEXT," +
            KEY_SCHEME_JSON + " TEXT ," +
            KEY_ITEM_CODE + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableScheme(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a scheme
     */
    public long createScheme(Scheme scheme) {

        ContentValues values = new ContentValues();

        values.put(KEY_SCHEME_ID,scheme.getSchemeId());
        values.put(KEY_ITEM_ID,scheme.getItemId());
        values.put(KEY_SCHEME_CODE,scheme.getSchemeCode());
        values.put(KEY_SCHEME_NAME,scheme.getSchemeName());
        values.put(KEY_SCHEME_JSON,scheme.getSchemeJSON());
        values.put(KEY_ITEM_CODE,scheme.getItemCode());

        values.put(KEY_UOM_ID,scheme.getUomId());
        values.put(KEY_UOM_CODE,scheme.getUomCode());
        values.put(KEY_QUANTITY,scheme.getQuantity());


        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_SCHEME, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a scheme
     */
    public long updateScheme(Scheme scheme) {

        ContentValues values = new ContentValues();

        values.put(KEY_SCHEME_ID,scheme.getSchemeId());
        values.put(KEY_ITEM_ID,scheme.getItemId());
        values.put(KEY_SCHEME_CODE,scheme.getSchemeCode());
        values.put(KEY_SCHEME_NAME,scheme.getSchemeName());
        values.put(KEY_SCHEME_JSON,scheme.getSchemeJSON());
        values.put(KEY_ITEM_CODE,scheme.getItemCode());

        values.put(KEY_UOM_ID,scheme.getUomId());
        values.put(KEY_UOM_CODE,scheme.getUomCode());
        values.put(KEY_QUANTITY,scheme.getQuantity());

        return writableSqLiteDatabase.update(TABLE_SCHEME, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(scheme.getAutoIncId())});
    }

    /**
     * Deleting a scheme
     */
    public int deleteScheme(long scheme_id) {

        return writableSqLiteDatabase.delete(TABLE_SCHEME, KEY_SCHEME_ID + " = ?",
                new String[]{String.valueOf(scheme_id)});
    }

    public void deleteAllSchemes(){
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_SCHEME);
    }

    /**
     * get single scheme
     */
    public Scheme getScheme(long scheme_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME + " WHERE "
                + KEY_SCHEME_ID + " = " + scheme_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Scheme scheme=null;

        if (c != null && c.moveToFirst() ) {

            scheme = new Scheme();

            scheme.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            scheme.setSchemeId(c.getInt(c.getColumnIndex(KEY_SCHEME_ID)));
            scheme.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            scheme.setSchemeCode(c.getString(c.getColumnIndex(KEY_SCHEME_CODE)));
            scheme.setSchemeName(c.getString(c.getColumnIndex(KEY_SCHEME_NAME)));
            scheme.setSchemeJSON(c.getString(c.getColumnIndex(KEY_SCHEME_JSON)));
            scheme.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));

            scheme.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
            scheme.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
            scheme.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));

        }

        if (c!=null)
            c.close();

        return scheme;
    }


    /**
     * get all schemes
     */
    public List<Scheme> getAllSchemes() {

        List<Scheme> schemeList=new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME  ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Scheme scheme=null;

        if (c != null) {

            if (c.moveToFirst()) {

                do {

                    scheme = new Scheme();

                    scheme.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                    scheme.setSchemeId(c.getInt(c.getColumnIndex(KEY_SCHEME_ID)));
                    scheme.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                    scheme.setSchemeCode(c.getString(c.getColumnIndex(KEY_SCHEME_CODE)));
                    scheme.setSchemeName(c.getString(c.getColumnIndex(KEY_SCHEME_NAME)));
                    scheme.setSchemeJSON(c.getString(c.getColumnIndex(KEY_SCHEME_JSON)));
                    scheme.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));

                    scheme.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                    scheme.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                    scheme.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));

                    schemeList.add(scheme);

                } while (c.moveToNext());
            }
        }

        if (c!=null)
            c.close();

        return schemeList;
    }


    public List<Scheme> getAllFilteredSchemes() {

        List<Scheme> schemeList=new ArrayList<>();

        int itemId=0;

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME  + " ORDER BY  " + KEY_ITEM_ID + " ASC , " + KEY_SCHEME_ID + " DESC " ;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Scheme scheme=null;

        if (c != null) {

            if (c.moveToFirst()) {

                do {

                    if (itemId!=c.getInt(c.getColumnIndex(KEY_ITEM_ID))) {

                        itemId = c.getInt(c.getColumnIndex(KEY_ITEM_ID));

                        scheme = new Scheme();

                        scheme.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                        scheme.setSchemeId(c.getInt(c.getColumnIndex(KEY_SCHEME_ID)));
                        scheme.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                        scheme.setSchemeCode(c.getString(c.getColumnIndex(KEY_SCHEME_CODE)));
                        scheme.setSchemeName(c.getString(c.getColumnIndex(KEY_SCHEME_NAME)));
                        scheme.setSchemeJSON(c.getString(c.getColumnIndex(KEY_SCHEME_JSON)));
                        scheme.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));

                        scheme.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                        scheme.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                        scheme.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));

                        schemeList.add(scheme);
                    }

                } while (c.moveToNext());
            }
        }

        if (c!=null)
            c.close();

        return schemeList;
    }


    public List<Scheme> getAllSchemesByItemId(int item_id) {

        List<Scheme> schemeList=new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEME + " WHERE "
                + KEY_ITEM_ID + " = " + item_id;

        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        Scheme scheme=null;

        if (c != null) {

            if (c.moveToFirst()) {

                do {

                    scheme = new Scheme();

                    scheme.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
                    scheme.setSchemeId(c.getInt(c.getColumnIndex(KEY_SCHEME_ID)));
                    scheme.setItemId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                    scheme.setSchemeCode(c.getString(c.getColumnIndex(KEY_SCHEME_CODE)));
                    scheme.setSchemeName(c.getString(c.getColumnIndex(KEY_SCHEME_NAME)));
                    scheme.setSchemeJSON(c.getString(c.getColumnIndex(KEY_SCHEME_JSON)));
                    scheme.setItemCode(c.getString(c.getColumnIndex(KEY_ITEM_CODE)));

                    scheme.setUomId(c.getInt(c.getColumnIndex(KEY_UOM_ID)));
                    scheme.setUomCode(c.getString(c.getColumnIndex(KEY_UOM_CODE)));
                    scheme.setQuantity(c.getDouble(c.getColumnIndex(KEY_QUANTITY)));

                    schemeList.add(scheme);

                } while (c.moveToNext());
            }
        }

        if (c!=null)
            c.close();

        return schemeList;
    }


}

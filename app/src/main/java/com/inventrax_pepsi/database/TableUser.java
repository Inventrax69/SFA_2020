package com.inventrax_pepsi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inventrax_pepsi.database.pojos.User;

/**
 * Created by Naresh on 09-Mar-16.
 */
public class TableUser {

    // Table Names
    public static final String TABLE_USER = "SFA_USER";
    // Common column names
    private static final String KEY_AUTO_INC_ID = "AUTO_INC_ID";
    private static final String KEY_USER_ID="USER_ID";
    private static final String KEY_USER_LOGIN_ID="USER_LOGIN_ID";
    private static final String KEY_USER_JSON="USER_JSON";
    private static final String KEY_LOGIN_INFO_JSON="LOGIN_INFO_JSON";
    private static final String KEY_LOAD_OUT_JSON="LOAD_OUT_JSON";


    // Table Create Statements
    public static final String CREATE_TABLE_USER= "CREATE TABLE " +
            TABLE_USER + "(" +
            KEY_AUTO_INC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            KEY_USER_ID + " INTEGER UNIQUE," +
            KEY_USER_LOGIN_ID + " INTEGER," +
            KEY_USER_JSON + " TEXT," +
            KEY_LOGIN_INFO_JSON+ " TEXT," +
            KEY_LOAD_OUT_JSON + " TEXT " + ")";

    private SQLiteDatabase readableSqLiteDatabase;
    private SQLiteDatabase writableSqLiteDatabase;

    public TableUser(SQLiteDatabase readableSqLiteDatabase,SQLiteDatabase writableSqLiteDatabase){
        this.readableSqLiteDatabase=readableSqLiteDatabase;
        this.writableSqLiteDatabase=writableSqLiteDatabase;
    }

    /**
     * Creating a User
     */
    public long createUser(User user) {

        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID,user.getUserId());
        values.put(KEY_USER_LOGIN_ID,user.getUserLogInId());
        values.put(KEY_USER_JSON,user.getUserJSON());
        values.put(KEY_LOGIN_INFO_JSON,user.getLoginInfoJSON());
        values.put(KEY_LOAD_OUT_JSON,user.getLoadOutJSON());

        long auto_inc_id = writableSqLiteDatabase.insert(TABLE_USER, null, values);

        return auto_inc_id;
    }

    /**
     * Updating a User
     */
    public long updateUser(User user) {

        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID,user.getUserId());
        values.put(KEY_USER_LOGIN_ID,user.getUserLogInId());
        values.put(KEY_USER_JSON,user.getUserJSON());
        values.put(KEY_LOGIN_INFO_JSON,user.getLoginInfoJSON());
        values.put(KEY_LOAD_OUT_JSON,user.getLoadOutJSON());

        return writableSqLiteDatabase.update(TABLE_USER, values, KEY_AUTO_INC_ID + " = ?",
                new String[]{String.valueOf(user.getAutoIncId())});
    }

    /**
     * Deleting a user
     */
    public int deleteUser(long user_id) {

        return writableSqLiteDatabase.delete(TABLE_USER, KEY_USER_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    public void deleteAllUsers(){
        writableSqLiteDatabase.execSQL("DELETE FROM " + TABLE_USER);
    }

    /**
     * get single User
     */
    public User getUser(long user_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_USER_ID + " = " + user_id;
        Cursor c = readableSqLiteDatabase.rawQuery(selectQuery, null);

        User user=null;

        if (c != null && c.moveToFirst()) {

            user = new User();

            user.setAutoIncId(c.getInt(c.getColumnIndex(KEY_AUTO_INC_ID)));
            user.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            user.setUserLogInId(c.getString(c.getColumnIndex(KEY_USER_LOGIN_ID)));
            user.setUserJSON(c.getString(c.getColumnIndex(KEY_USER_JSON)));
            user.setLoginInfoJSON(c.getString(c.getColumnIndex(KEY_LOGIN_INFO_JSON)));
            user.setLoadOutJSON(c.getString(c.getColumnIndex(KEY_LOAD_OUT_JSON)));

        }

        if (c!=null)
            c.close();

        return user;
    }

}

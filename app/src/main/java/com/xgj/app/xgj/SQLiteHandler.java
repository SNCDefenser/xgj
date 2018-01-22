package com.xgj.app.xgj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "xgj";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_ITEM = "item";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TOKEN = "token";
    // Item table attributes
    private static final String ITEM_ID = "id";
    private static final String ITEM_NAME = "name";
    private static final String ITEM_TYPE = "type";
    private static final String ITEM_TAGS = "tags";
    private static final String ITEM_PLACE = "places";
    private static final String ITEM_OWNER = "owner";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_TOKEN + " TEXT"+ ")";

//        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
//                + ITEM_ID + " INTEGER PRIMARY KEY," + ITEM_NAME + " TEXT UNIQUE," + ITEM_TYPE + " TEXT,"
//                + ITEM_OWNER + " TEXT," + ITEM_TAGS + " TEXT," + ITEM_PLACE + " TEXT," + ")";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + ITEM_NAME + " TEXT UNIQUE," + ITEM_TYPE + " TEXT,"
                + ITEM_OWNER + " TEXT," + ITEM_TAGS + " TEXT," + ITEM_PLACE + " TEXT," + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);

        // Create tables again
        onCreate(db);
    }

    public void addUser(String name, String email, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_TOKEN, uid); // Token

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addItem(String name,  String type, String tags, String shop, String owner) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + ITEM_NAME + " TEXT UNIQUE," + ITEM_TYPE + " TEXT,"
                + ITEM_OWNER + " TEXT," + ITEM_TAGS + " TEXT," + ITEM_PLACE + " TEXT" + ")";

        db.execSQL(CREATE_ITEM_TABLE);

        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, name); // Name
        values.put(ITEM_TYPE,type); //Type
        values.put(ITEM_OWNER, owner); // Owner
        values.put(ITEM_TAGS, tags); // itemtags
        values.put(ITEM_PLACE, shop); // shop
        // Inserting Row
        long id = db.insert(TABLE_ITEM, null, values);
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public HashMap<String, String> getUserDetails(String email) {
        HashMap<String, String> user = new HashMap<String, String>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){
            if(cursor.getString(2).equals(email)){
                user.put("name", cursor.getString(1));
                user.put("email", cursor.getString(2));
                user.put("token", cursor.getString(3));
                break;
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public HashMap<String, Map<String, String>> getItemDetails(String email) {
        HashMap<String, Map<String, String>> user = new HashMap<>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEM;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor == null) return user;
        while(cursor.moveToNext()){
            String s = cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4);
            if(cursor.getString(2).equals(email) && cursor.getString(1).equals("0")){
                Map<String, String> item = new HashMap<>();
                item.put("tags", cursor.getString(3)); // itemtags
                item.put("place", cursor.getString(4)); // shop
                user.put(cursor.getString(0), item);
            }
        }
        cursor.moveToFirst();
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching items from Sqlite: " + user.toString());

        return user;
    }


    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteAllItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ITEM, null, null);
        db.close();

        Log.d(TAG, "Deleted all items info from sqlite");
    }
}
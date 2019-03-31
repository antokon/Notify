package com.example.antonio.notify.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Antonio on 2/16/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notification.db";

    // Contacts table name
    private static final String TABLE_NAME = "notifications";

    // Contacts Table Columns names
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NOTIFICATION";
    private static final String COL_3 = "MESSAGE";
    private static final String COL_4 = "LATITUDE";
    private static final String COL_5 = "LONGITUDE";

    private static final String DATABASE_CREATE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " + COL_1 + " INTEGER, " + COL_2 + " TEXT, " + COL_3 + " TEXT, "+ COL_4 + " FLOAT, "+ COL_5 + " FLOAT, );" ;

    //private DatabaseHandler m_dbHelper;
    //public static SQLiteDatabase db;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public DatabaseHandler open() throws SQLException
//    {
//        db = m_dbHelper.getWritableDatabase();
//        return this;
//    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.execSQL(DATABASE_NAME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTIFICATION TEXT, MESSAGE TEXT, LATITUDE DOUBLE, LONGITUDE DOUBLE )" );

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public boolean addNotification(String notification, String message, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_2, notification); // Contact Name
        values.put(COL_3, message); // Contact Phone Number
        values.put(COL_4, latitude); // Contact Name
        values.put(COL_5, longitude); // Contact Phone Number
        // Inserting Row
        long result=db.insert(TABLE_NAME, null, values);
        if(result == -1)
            return false;
        else
            return true;
        // Closing database connection
        //db.close

    }
     // Getting All Contacts
    public Cursor getAllNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;

        // return contact list

    }


    // Getting contacts Count
    public int getNotificationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();

    }
    // Updating single contact
    public boolean updateNotification(String id, String notification, String message, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, notification); // Contact Name
        values.put(COL_3, message); // Contact Phone Number
        values.put(COL_4, latitude); // Contact Name
        values.put(COL_5, longitude); // Contact Phone Number

        // updating TABLE_NOTIFICATION
        db.update(TABLE_NAME, values, "ID = ?",new String[] { id });
        return true;
    }

     // Deleting single contact
    public Integer  deleteNotification(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});

    }


    //---closes the database---
//    public void close()
//    {
//        if (db != null)
//            db.close();
//        if (m_dbHelper != null)
//            m_dbHelper.close();
//    }



}

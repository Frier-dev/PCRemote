package com.friertech.pcremote;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseHelper extends SQLiteOpenHelper {

    public String mac;
    public String ip;
    public String broadcast;

    public static final String DEVICE_TABEL = "DEVICE_TABEL";
    public static final String COLUMN_DEVICE_NAME = "DEVICE_NAME";
    public static final String COLUMN_DEVICE_MAC = "DEVICE_MAC";
    public static final String COLUMN_DEVICE_IP = "DEVICE_IP";
    public static final String COLUMN_DEVICE_BROADCAST = "DEVICE_BROADCAST";
    public static final String COLUMN_ACTIVE_DEVICE = "ACTIVE_DEVICE";
    public static final String COLUMN_ID = "ID";



    public DatabaseHelper(@Nullable Context context) {
        super(context, "devices2.db", null, 1);
    }

    // this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + DEVICE_TABEL + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DEVICE_NAME + " TEXT, " + COLUMN_DEVICE_MAC + " TEXT, " + COLUMN_DEVICE_IP + " TEXT, " + COLUMN_DEVICE_BROADCAST + " TEXT, " +COLUMN_ACTIVE_DEVICE + " BOOL)";

        db.execSQL(createTableStatement);
    }

    // this is called if the database version number changes. It prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String GetActiveDevice(){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        String deviceName = "N/A";

        String queryString = "SELECT * FROM " + DEVICE_TABEL + " WHERE ACTIVE_DEVICE = true";
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                deviceName = cursor.getString(1);
            } while(cursor.moveToNext());
        } else {
            // failure. do not add anything to the list.
        }

// close both cursor and db when done.
        cursor.close();
        db.close();
        return deviceName;
    }

    public boolean addOne(DevicesModel devicesModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DEVICE_NAME, devicesModel.getName());
        cv.put(COLUMN_DEVICE_MAC, devicesModel.getMac());
        cv.put(COLUMN_DEVICE_IP, devicesModel.getIp());
        cv.put(COLUMN_DEVICE_BROADCAST, devicesModel.getBroadcast());
        cv.put(COLUMN_ACTIVE_DEVICE, devicesModel.isActive());

        long insert = db.insert(DEVICE_TABEL, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }


    public boolean deleteOne(DevicesModel devicesModel){
        // find deviceModel in the database. if it found, delete it and return true.
        // if its not found, return false
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + DEVICE_TABEL + " WHERE " + COLUMN_ID + " = " + devicesModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }

    }

    public boolean updateAllToFalse(DevicesModel devicesModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryStringUpdateAll = "UPDATE " + DEVICE_TABEL + " SET ACTIVE_DEVICE = false";
        Cursor newUpdate = db.rawQuery(queryStringUpdateAll, null);
        if(newUpdate.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }

    public List<DevicesModel> Active(){
        List<DevicesModel> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + DEVICE_TABEL + " WHERE ACTIVE_DEVICE = true";
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                mac = cursor.getString(2);
                ip = cursor.getString(3);
                broadcast = cursor.getString(4);
            } while(cursor.moveToNext());

        } else {
            // failure. do not add anything to the list.
        }

// close both cursor and db when done.
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean favoriteOne(DevicesModel devicesModel){
        // find devicesmodel in the database. if it found, loop through all items and change isActive : false
        // take the clicked item and change isActive : true
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE "+ DEVICE_TABEL + " SET ACTIVE_DEVICE = true WHERE " + COLUMN_ID + " = " + devicesModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        } else{
            return false;
        }
    }


    public List<DevicesModel> getAll() {
        List<DevicesModel> returnList = new ArrayList<>();

        //get data from database
        String queryString = "SELECT * FROM " + DEVICE_TABEL;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new device object. Put them into the return list
            do {
                int deviceID = cursor.getInt(0);
                String deviceName = cursor.getString(1);
                String deviceMAC = cursor.getString(2);
                String deviceIP = cursor.getString(3);
                String deviceBroadcast = cursor.getString(4);
                boolean activeDevice = cursor.getInt(5) == 1 ? true: false;

                DevicesModel newDevice = new DevicesModel(deviceID, deviceName, deviceMAC, deviceIP, deviceBroadcast, activeDevice);
                returnList.add(newDevice);
            } while(cursor.moveToNext());

        } else {
            // failure. do not add anything to the list.
        }

        // close both cursor and db when done.
        cursor.close();
        db.close();
        return returnList;
    }

}

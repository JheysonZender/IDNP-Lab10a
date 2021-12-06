package com.example.lab10a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int BD_version = 1;
    public static final String TABLE_NAME = "SQLiteGPS";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_latitude  = "latitude";
    public static final String COLUMN_longitude = "longitude";
    public static final String COLUMN_altitude = "altitude";


    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, BD_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = " CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_latitude + " DOUBLE NOT NULL, "
                + COLUMN_longitude + " DOUBLE NOT NULL, "
                + COLUMN_altitude + " DOUBLE NOT NULL); "  ;
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = " DROP TABLE IF EXISTS SQLiteGPS";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public boolean addData (double lat, double lon, double alt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_latitude, lat);
        contentValues.put(COLUMN_longitude, lon);
        contentValues.put(COLUMN_altitude, alt);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    
    public Cursor getListaContenidos(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}

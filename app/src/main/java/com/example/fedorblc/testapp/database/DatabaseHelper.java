package com.example.fedorblc.testapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fedorblc.testapp.database.DatabaseContract.CountryEntry;

/**
 * Created by fedorblc on 2/16/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "countries.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RSS_TABLE = "CREATE TABLE " + CountryEntry.TABLE_NAME + " (" +
                CountryEntry._ID + " INTEGER PRIMARY KEY," +
                CountryEntry.COLUMN_NAME + " TEXT, " +
                CountryEntry.COLUMN_CODE + " INTEGER" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_RSS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME);
    }

}

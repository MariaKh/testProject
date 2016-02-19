package com.example.fedorblc.testapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.fedorblc.testapp.database.DatabaseContract.CountryEntry;

/**
 * Created by fedorblc on 2/16/16.
 */
public class DatabaseProvider extends ContentProvider {

    private static final int COUNTRY_TYPE = 100;
    private DatabaseHelper mOpenHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_COUNTRIES, COUNTRY_TYPE);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result;
        if (mUriMatcher.match(uri) == COUNTRY_TYPE) {
            result = mOpenHelper.getReadableDatabase().query(CountryEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case COUNTRY_TYPE: {
                long _id = db.insert(CountryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CountryEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int id;

        switch (match) {
            case COUNTRY_TYPE: {
                id = db.update(DatabaseContract.CountryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (id != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRY_TYPE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(DatabaseContract.CountryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

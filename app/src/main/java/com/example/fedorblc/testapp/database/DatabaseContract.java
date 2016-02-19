package com.example.fedorblc.testapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fedorblc on 2/16/16.
 */
public class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.example.fedorblc.testapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_COUNTRIES = "countries";

    public static final class CountryEntry implements BaseColumns {

        public static final String TABLE_NAME = PATH_COUNTRIES;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

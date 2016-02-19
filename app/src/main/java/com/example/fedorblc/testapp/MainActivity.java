package com.example.fedorblc.testapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.fedorblc.testapp.adapters.RecyclerViewAdapter;
import com.example.fedorblc.testapp.database.DatabaseContract;
import com.example.fedorblc.testapp.database.DatabaseContract.CountryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTIFY_ID = 101;

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int verticalPadding = (int) getResources().getDimension(R.dimen.divider_height);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (view.getTag() == null) {
                    outRect.top = verticalPadding;
                }
                outRect.bottom = verticalPadding;
            }
        });
        mAdapter = new RecyclerViewAdapter(this, null, new RecyclerViewAdapter.OnAdapterItemClickListener() {
            @Override
            public void onEditClick(Cursor cursor) {
                Toast.makeText(MainActivity.this, cursor.getString(cursor.getColumnIndex(DatabaseContract.CountryEntry.COLUMN_NAME)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClearClick(Cursor cursor) {
                Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.ic_clear_white_24dp)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.CountryEntry.COLUMN_NAME)))
                        .setContentText(String.valueOf(cursor.getInt(cursor.getColumnIndex(CountryEntry.COLUMN_CODE))));
                Notification notification;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    notification = builder.build();
                } else {
                    notification = builder.getNotification();
                }
                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFY_ID, notification);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        initDataBaseFirstTime();
        getSupportLoaderManager().initLoader(0, null, MainActivity.this);
    }

    private void initDataBaseFirstTime() {
        if (getSharedPreferences(getPackageName(), MODE_PRIVATE).getBoolean(getString(R.string.first_launch), true)) {
            String[] mCountryNameArray = getResources().getStringArray(R.array.country_name_array);
            int[] mCountryCodeArray = getResources().getIntArray(R.array.country_code_array);
            ContentValues[] mContentValues = new ContentValues[mCountryNameArray.length];
            for (int i = 0; i < mCountryNameArray.length; i++) {
                ContentValues values = new ContentValues();
                values.put(CountryEntry.COLUMN_NAME, mCountryNameArray[i]);
                values.put(CountryEntry.COLUMN_CODE, mCountryCodeArray[i]);
                mContentValues[i] = values;
            }
            int result = getApplicationContext().getContentResolver().bulkInsert(CountryEntry.CONTENT_URI, mContentValues);
            if (result > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
            }
            getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().putBoolean(getString(R.string.first_launch), false).commit();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CountryEntry.CONTENT_URI,
                new String[]{CountryEntry.TABLE_NAME + "." + CountryEntry._ID,
                        CountryEntry.COLUMN_NAME, CountryEntry.COLUMN_CODE}
                , null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

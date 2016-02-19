package com.example.fedorblc.testapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fedorblc.testapp.R;
import com.example.fedorblc.testapp.database.DatabaseContract;

/**
 * Created by fedorblc on 2/16/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private LayoutInflater mInflater;

    public interface OnAdapterItemClickListener {
        void onEditClick(Cursor cursor);

        void onClearClick(Cursor cursor);
    }

    private OnAdapterItemClickListener mItemListener;

    private ItemViewHolder.IItemViewHolderClicks mListener = new ItemViewHolder.IItemViewHolderClicks() {
        @Override
        public void onEditClick(int position) {
            if (mItemListener != null) {
                mCursor.moveToPosition(position);
                mItemListener.onEditClick(mCursor);
            }
        }

        @Override
        public void onClearClick(int position) {
            if (mItemListener != null) {
                mCursor.moveToPosition(position);
                mItemListener.onClearClick(mCursor);
            }
        }
    };

    public RecyclerViewAdapter(Context context, Cursor cursor,
                               OnAdapterItemClickListener listener) {
        this.mCursor = cursor;
        this.mContext = context;
        mItemListener = listener;
        mInflater = LayoutInflater.from(mContext);
    }

    // Create new views
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View view = mInflater.inflate(R.layout.list_item_layout, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        if (mCursor == null || mCursor.isClosed()) {
            return;
        }
        mCursor.moveToPosition(position);
        viewHolder.mPosition = position;
        viewHolder.mCountryName.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.CountryEntry.COLUMN_NAME)));
        viewHolder.mCountryCode.setText(String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.CountryEntry.COLUMN_CODE))));
    }


    @Override
    public int getItemViewType(int position) {
        int type = 0;
        return type;
    }

    // Return the size of Cursor Items
    @Override
    public int getItemCount() {
        int size = 0;
        if (mCursor != null) {
            size = mCursor.getCount();
        }
        return size;
    }

    /**
     * View Holder class to handle initialization of the UI elements
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private IItemViewHolderClicks mListener;
        private int mPosition = -1;
        private TextView mCountryName;
        private TextView mCountryCode;
        private ImageView mEditImage;
        private ImageView mClearImage;

        public ItemViewHolder(View itemLayoutView, IItemViewHolderClicks listener) {
            super(itemLayoutView);
            mListener = listener;
            mCountryName = (TextView) itemLayoutView.findViewById(R.id.country_name);
            mCountryCode = (TextView) itemLayoutView.findViewById(R.id.country_code);
            mEditImage = (ImageView) itemLayoutView.findViewById(R.id.edit_action);
            mClearImage = (ImageView) itemLayoutView.findViewById(R.id.clear_action);
            mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition != -1) {
                        mListener.onEditClick(mPosition);
                    }
                }
            });
            mClearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition != -1) {
                        mListener.onClearClick(mPosition);
                    }
                }
            });
        }

        public interface IItemViewHolderClicks {
            void onEditClick(int position);

            void onClearClick(int position);
        }
    }

    public void swapCursor(Cursor cursor) {
        if (this.mCursor != null && !this.mCursor.isClosed()) {
            mCursor.close();
        }
        this.mCursor = cursor;
        notifyDataSetChanged();
    }
}

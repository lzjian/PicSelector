package com.lzjian.picselector.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected final String TAG = getClass().getSimpleName();

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> data, int mItemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mItemLayoutId = mItemLayoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonViewHolder viewHolder = getViewHolder(convertView, parent);
        convert(position, viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(int position, CommonViewHolder helper, T item);

    private CommonViewHolder getViewHolder(View convertView, ViewGroup parent) {
        return CommonViewHolder.get(mContext, convertView, parent, mItemLayoutId);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> mData) {
        this.mData = mData;
    }

    protected void log(String msg){
        Log.i(TAG, msg);
    }
}

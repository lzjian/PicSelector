package com.lzjian.picselector.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzjian.picselector.App;

public class CommonViewHolder {

    private final SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    private CommonViewHolder(Context context, ViewGroup parent,int layoutId) {
        mContext = App.getInstance();
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId,
                parent, false);
        //setTag
        mConvertView.setTag(this);
    }

    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        CommonViewHolder holder = null;
        if (convertView == null) {
            holder = new CommonViewHolder(context, parent, layoutId);
        } else {
            holder = (CommonViewHolder) convertView.getTag();
        }
        return holder;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * @desciption:通过id获取到view
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * @description:设置文字
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
}

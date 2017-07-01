package com.lzjian.picselector.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lzjian.picselector.R;
import com.lzjian.picselector.adapter.PicFolderAdapter;
import com.lzjian.picselector.model.ImageFolder;
import com.lzjian.picselector.util.DensityUtils;
import com.lzjian.picselector.util.WindowUtils;

import java.util.List;

public class PicFolderPopupWindow {

    public static final int bottombarHeight = 48; // 单位dp

    public static final String TAG = "PicFolderPopupWindow";

    //上下文对象
    private Context mContext;

    private List<ImageFolder> mImageFolders;
    //PopupWindow对象
    private PopupWindow mPopupWindow;
    //点击事件
    private onListViewItemClickListener onListViewItemClickListener;

    private PicFolderAdapter mAdapter;

    private View popupWindow_view;

    private ListView listView;

    public PicFolderPopupWindow(Context context) {
        mContext = context;
    }

    public PicFolderPopupWindow(Context context, List<ImageFolder> imageFolders) {
        mContext = context;
        mImageFolders = imageFolders;
        init();
    }

    public void init() {
        popupWindow_view = LayoutInflater.from(mContext).inflate(R.layout.listview_pic_folder, null);
        listView = (ListView) popupWindow_view.findViewById(R.id.lv_pic_folder);
        mAdapter = new PicFolderAdapter(mContext, mImageFolders, R.layout.item_pic_folder);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectedPosition(position);
                mAdapter.notifyDataSetChanged();
                onListViewItemClickListener.onItemClick(parent, view, position, id);
            }
        });

        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(mContext, 400));
        mPopupWindow.setAnimationStyle(R.style.ppw_anim_bottom_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(false);
            }
        });
    }

    public void show(View view) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
//            mPopupWindow.showAsDropDown(view);
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, DensityUtils.dip2px(mContext, 48));
        }
        setWindowAlpha(true);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    public void setWindowAlpha(boolean isOpen) {
        WindowUtils.setWindowAlpha(mContext, isOpen);
    }

    public interface onListViewItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public void setOnListViewItemClickListener(onListViewItemClickListener onListViewItemClickListener) {
        this.onListViewItemClickListener = onListViewItemClickListener;
    }
}


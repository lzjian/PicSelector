package com.lzjian.picselector.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lzjian.picselector.App;
import com.lzjian.picselector.model.ImageFolder;
import com.lzjian.picselector.R;
import com.lzjian.picselector.util.DensityUtils;
import com.lzjian.picselector.util.GlideHelper;

import java.util.List;

public class PicFolderAdapter extends CommonAdapter<ImageFolder> {

    private final String TAG = getClass().getSimpleName();

    private int selectedPosition = 0;

    public PicFolderAdapter(Context context, List<ImageFolder> mDatas, int mItemLayoutId) {
        super(context, mDatas, mItemLayoutId);
    }

    @Override
    public void convert(int position, CommonViewHolder helper, ImageFolder item) {
        helper.setText(R.id.tv_folder_name, item.getName());
        helper.setText(R.id.tv_pic_num, item.getCount()+"张");

        GlideHelper.show(mContext, item.getFirstImagePath(), (ImageView) helper.getView(R.id.iv_first_image),
                DensityUtils.dip2px(App.getInstance(), 72), DensityUtils.dip2px(App.getInstance(), 72), null);

        if (selectedPosition == position){
            helper.getView(R.id.iv_checked).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.iv_checked).setVisibility(View.GONE);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}

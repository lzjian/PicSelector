package com.lzjian.picselector.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzjian.picselector.App;
import com.lzjian.picselector.R;
import com.lzjian.picselector.util.ChangeIvBgUtils;
import com.lzjian.picselector.util.DensityUtils;
import com.lzjian.picselector.util.GlideHelper;
import com.lzjian.picselector.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectMultiPicAdapter extends BaseAdapter {

    public final String TAG = getClass().getSimpleName();

    private Context mContext;
    private List<String> mData;
    private int mLayoutId;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> selectedPaths = new ArrayList<>();
    private int canSelectNum;
    private onChangeSelectedPicNumListener mOnChangeSelectedPicNumListener;

    public SelectMultiPicAdapter(Context context, List<String> datas) {
        mContext = context;
        mData = datas;
        mLayoutId = R.layout.item_select_multi_pic;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else {
            convertView = mLayoutInflater.inflate(mLayoutId, null);
            viewHolder = new ViewHolder(convertView);
        }
        convertView.setTag(viewHolder);

        final Boolean isSelected = selectedPaths.contains(mData.get(position));
        GlideHelper.show(mContext, mData.get(position), viewHolder.iv_image,
                DensityUtils.dip2px(App.getInstance(), 120), DensityUtils.dip2px(App.getInstance(), 120), new GlideHelper.CallBack() {
            @Override
            public void onSuccess() {
                if (isSelected){
                    ChangeIvBgUtils.changeIvBgGray(viewHolder.iv_image);
                }else{
                    ChangeIvBgUtils.resetIvBg(viewHolder.iv_image);
                }
            }
        });
        if (isSelected){
            selectImg(viewHolder);
        }else{
            cancelSelectImg(viewHolder);
        }
        viewHolder.rl_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.isSelected == 0){
                    Log.i(TAG, "现在选中");
                    if (selectedPaths.size() < canSelectNum){
                        selectedPaths.add(mData.get(position));
                        mOnChangeSelectedPicNumListener.changeSelectedPicNum(selectedPaths.size());
                        selectImg(viewHolder);
                        ChangeIvBgUtils.changeIvBgGray(viewHolder.iv_image);
                    }else{
                        ToastUtils.show("你最多只能选择"+canSelectNum+"张图片");
                    }
                }else{
                    Log.i(TAG, "取消选中");
                    selectedPaths.remove(mData.get(position));
                    mOnChangeSelectedPicNumListener.changeSelectedPicNum(selectedPaths.size());
                    cancelSelectImg(viewHolder);
                    ChangeIvBgUtils.resetIvBg(viewHolder.iv_image);
                }
            }
        });

//        viewHolder.rl_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, mData.get(position));
//                ShowBigImgActivity.startActivity((Activity) mContext, mData.get(position), Constant.LOCAL);
//            }
//        });

        return convertView;
    }

    // 图片被选择
    private void selectImg(ViewHolder viewHolder){
        viewHolder.isSelected = 1;
        viewHolder.iv_is_selected.setImageResource(R.drawable.ic_pic_selected);
    }

    // 图片没被选择或取消选择
    private void cancelSelectImg(ViewHolder viewHolder){
        viewHolder.isSelected = 0;
        viewHolder.iv_is_selected.setImageResource(R.drawable.ic_pic_unselected);
    }

    private class ViewHolder {
        private RelativeLayout rl_image;
        private ImageView iv_image;
        private RelativeLayout rl_select_image;
        private ImageView iv_is_selected;
        private int isSelected;

        ViewHolder(View view) {
            isSelected = 0;
            rl_image = (RelativeLayout) view.findViewById(R.id.rl_image);
            iv_image= (ImageView) view.findViewById(R.id.iv_image);
            rl_select_image = (RelativeLayout) view.findViewById(R.id.rl_select_image);
            iv_is_selected= (ImageView) view.findViewById(R.id.iv_is_selected);
        }
    }

    public ArrayList<String> getSelectedPaths() {
        return selectedPaths;
    }

    public void setSelectedPaths(ArrayList<String> mSelectedPaths) {
        this.selectedPaths = mSelectedPaths;
    }

    public int getCanSelectNum() {
        return canSelectNum;
    }

    public void setCanSelectNum(int canSelectNum) {
        this.canSelectNum = canSelectNum;
    }

    public interface onChangeSelectedPicNumListener{
        void changeSelectedPicNum(int num);
    }

    public void setOnChangeSelectedPicNum(onChangeSelectedPicNumListener mOnChangeSelectedPicNumListener) {
        this.mOnChangeSelectedPicNumListener = mOnChangeSelectedPicNumListener;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
    }

}


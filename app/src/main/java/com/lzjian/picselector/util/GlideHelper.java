package com.lzjian.picselector.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lzjian.picselector.R;

public class GlideHelper {

    public static final String TAG = "GlideHelper";

    // imgStr是本地图片或网络图片路径,width是用glide生成的bitmap的宽度,height是bitmap的高度
    // 每次用这个方法展示,图片都会缓存到LruCache中
    public static void show(Context context, final String imgStr, final ImageView iv, int width, int height, final CallBack callBack){
        Log.i(TAG, imgStr);
        iv.setImageResource(R.drawable.ic_default);
        iv.setTag(imgStr);
        Bitmap bitmap = LruCacheHelper.getInstance().getBitmapFormLruCache(imgStr);
        //如果不为空，说明LruCache中已经缓存了该图片，则读取缓存直接显示，
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            // 长宽默认设置成48, 48
            if (width == 0){
                width = 48;
            }
            if (height == 0){
                height = 48;
            }
            Glide.with(context)
                    .load(imgStr)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // 跳过磁盘缓存
                    .skipMemoryCache(true) // 跳过内存缓存(跳过它们是为了自己写缓存)
                    .into(new SimpleTarget<Bitmap>(width, height) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null){
                                LruCacheHelper.getInstance().addBitmapToLruCache(imgStr, bitmap);
                                if (iv.getTag() != null && iv.getTag().equals(imgStr)) {
                                    iv.setImageBitmap(bitmap);
                                    if (callBack != null){
                                        callBack.onSuccess();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    public interface CallBack{
        void onSuccess();
    }
}

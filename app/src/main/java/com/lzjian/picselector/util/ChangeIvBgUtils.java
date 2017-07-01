package com.lzjian.picselector.util;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ChangeIvBgUtils {

    // 让图片背景变暗
    public static void changeIvBgGray(ImageView iv){
        Drawable drawable = iv.getDrawable();
        if (drawable != null){
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            iv.setImageDrawable(drawable);
        }
    }

    // 让图片背景恢复原样
    public static void resetIvBg(ImageView iv){
        Drawable drawable = iv.getDrawable();
        if (drawable != null){
            drawable.clearColorFilter();
            iv.setImageDrawable(drawable);
        }
    }

}

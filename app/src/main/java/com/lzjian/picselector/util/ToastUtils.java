package com.lzjian.picselector.util;

import android.widget.Toast;

import com.lzjian.picselector.App;

public class ToastUtils {

    public static void show(String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
    }
}

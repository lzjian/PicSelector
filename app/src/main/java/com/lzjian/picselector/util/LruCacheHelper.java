package com.lzjian.picselector.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruCacheHelper {

    private static LruCacheHelper instance;

    private final static int maxMemory = (int) Runtime.getRuntime().maxMemory();

    private final static int cacheSizes = maxMemory / 5;
    //图片缓存
    private static LruCache<String, Bitmap> mLruCache;

    public LruCacheHelper() {
        mLruCache = new LruCache<String, Bitmap>(cacheSizes) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 单例获取该Helper
     * @param
     * @return
     */
    public static synchronized LruCacheHelper getInstance() {
        if (instance == null) {
            synchronized (LruCacheHelper .class) {
                if (instance == null){
                    instance = new LruCacheHelper();
                }
            }
        }
        return instance;
    }

    /**
     * @param key
     * @return
     * @description:从LruCache中获取一张图片，可能会返回null
     */
    public Bitmap getBitmapFormLruCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * @param key (这个key貌似不能太长)
     * @param bitmap
     * @descripion:在LruCache添加一张图片
     */
    public void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (getBitmapFormLruCache(key) == null) {
            if (bitmap != null) {
                mLruCache.put(key, bitmap);
            }
        }
    }
}
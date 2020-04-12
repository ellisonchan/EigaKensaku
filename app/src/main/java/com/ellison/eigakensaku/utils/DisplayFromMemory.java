package com.ellison.eigakensaku.utils;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

public class DisplayFromMemory {
    private LruCache<String, Bitmap> mMemoryCache;

    public DisplayFromMemory() {
        final long maxMem = Runtime.getRuntime().maxMemory() / 8;
        Log.e("ellison2021", "MAX MEM:" + Runtime.getRuntime().maxMemory() + " maxMem:" + maxMem);

        // Need to define cache size to keep from OOM
        mMemoryCache = new LruCache<String, Bitmap>((int) maxMem) {
            @Override
            protected void entryRemoved(boolean evicted,@NonNull String key,@NonNull Bitmap oldValue,@Nullable Bitmap newValue){
                super.entryRemoved(evicted,key,oldValue,newValue);
            }

            @Override
            protected int sizeOf(@NonNull String key,@NonNull Bitmap value){
                // Specific the item's size
                return value.getByteCount();
            }
        };
    }

    public Bitmap get(String url) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(url);
        }
        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        if (mMemoryCache != null) {
            mMemoryCache.put(url, bitmap);
        }
    }

}

package com.ellison.eigakensaku.myimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.ellison.eigakensaku.R;

import androidx.annotation.NonNull;

public class EllisonImageDisplayer {
    private static DisplayFromMemory mMemory = null;
    private static DisplayFromDisk mDisk = null;
    private static DisplayFromNetwork mNetWork = null;

    private static EllisonImageDisplayer mDisplayer = null;

    private EllisonImageDisplayer(Context context) {
        mMemory = new DisplayFromMemory();
        mDisk = new DisplayFromDisk(context);
        mNetWork = new DisplayFromNetwork(mMemory, mDisk, context);
    }

    public static EllisonImageDisplayer getDisplayer(Context context) {
        if (mDisplayer == null) {
            synchronized (EllisonImageDisplayer.class) {
                if (mDisplayer == null) {
                    mDisplayer = new EllisonImageDisplayer(context);
                }
            }
        }
        return mDisplayer;
    }

    public void displayImage(@NonNull ImageView iv, @NonNull String url) {
        iv.setImageResource(R.drawable.rv_item_post_loading);

        Log.e("ellison2021", "display Image url:" + url);
        if (mMemory != null) {
            Bitmap bitmap = mMemory.get(url);
            if (bitmap != null && !bitmap.isRecycled()) {
                Log.e("ellison2021", "display Image memory is not null");
                iv.setImageBitmap(bitmap);
                return;
            }
        }

        if (mDisk != null) {
            Bitmap bitmap = mDisk.get(url);
            if (bitmap != null && !bitmap.isRecycled()) {
                Log.e("ellison2021", "display Image disk is not null");
                iv.setImageBitmap(bitmap);
                if (mMemory != null) {
                    Log.e("ellison2021", "display Image disk is not null & saved to memory");
                    mMemory.put(url, bitmap);
                }
                return;
            }
        }

        if (mNetWork != null) {
            Log.e("ellison2021", "display Image disk & memory both null & get from network");
            mNetWork.load(url, iv);
        }
    }

}

package com.ellison.eigakensaku.debug;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageLoadCallback extends SimpleImageLoadingListener {
    @Override
    public void onLoadingStarted(String imageUri, View view) {
        Log.e("ellison", "ImageLoadCallback#onLoadingStarted() uri:" + imageUri + " view:" + view);
        super.onLoadingStarted(imageUri, view);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        Log.e("ellison", "ImageLoadCallback#onLoadingFailed() uri:" + imageUri + " view:" + view + " FailReason:" + failReason.getType() + " STACK:", failReason.getCause());
        super.onLoadingFailed(imageUri, view, failReason);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        Log.e("ellison", "ImageLoadCallback#onLoadingComplete() uri:" + imageUri + " view:" + view);
        super.onLoadingComplete(imageUri, view, loadedImage);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        Log.e("ellison", "ImageLoadCallback#onLoadingCancelled() uri:" + imageUri + " view:" + view);
        super.onLoadingCancelled(imageUri, view);
    }
}

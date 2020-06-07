package com.ellison.eigakensaku.glide;

import android.util.Log;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;

public class ImageRequestListener implements com.bumptech.glide.request.RequestListener {
    private static final String TAG = ImageRequestListener.class.getSimpleName();

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        Log.e(TAG, "onLoadFailed e:" + e + " model:" + model + " target:" + target + " isFirstResource:" + isFirstResource);
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        return false;
    }
}

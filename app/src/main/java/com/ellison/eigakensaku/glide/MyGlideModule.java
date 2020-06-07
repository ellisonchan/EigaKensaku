package com.ellison.eigakensaku.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;

@GlideModule
public class MyGlideModule extends AppGlideModule {
    private static final long DISK_CACHE_SIZE = 150 * 1024 * 1024;
    private static final String DISK_CACHE_DIR = "movie_post";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, DISK_CACHE_DIR, DISK_CACHE_SIZE));
        super.applyOptions(context, builder);
    }
}
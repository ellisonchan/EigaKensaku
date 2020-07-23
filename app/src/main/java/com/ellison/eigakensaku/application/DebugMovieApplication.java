package com.ellison.eigakensaku.application;

import android.content.Context;

import androidx.annotation.NonNull;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

// for debug
public class DebugMovieApplication extends MovieApplication {
    @Override
    protected RefWatcher installLeakCanary() {
        RefWatcher refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(10, TimeUnit.SECONDS)
                .buildAndInstall();
        return refWatcher;
    }
}

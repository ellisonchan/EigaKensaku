package com.ellison.eigakensaku.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieApplication extends Application {
    private static ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new EveryActivityLifecycleCallbacks());
    }

    public static ImageLoader getImageLoader(@NonNull Context context) {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    mImageLoader = ImageLoader.getInstance();
                    mImageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
                }
            }
        }
        return mImageLoader;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public final static class EveryActivityLifecycleCallbacks implements ActivityLifecycleCallbacks{
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }
}

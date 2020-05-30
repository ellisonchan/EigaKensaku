package com.ellison.eigakensaku.application;

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

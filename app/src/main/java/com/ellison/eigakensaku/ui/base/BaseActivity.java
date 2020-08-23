package com.ellison.eigakensaku.ui.base;

import android.app.ActivityManager;
import android.os.Bundle;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onCreate() savedInstanceState:" + savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    protected abstract void init();

    @Override
    protected void onRestart() {
        Utils.logDebug(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Utils.logDebug(TAG, "onStart()");
        super.onStart();
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Utils.logDebug(TAG, "onRestoreInstanceState() savedInstanceState:" + savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        ActivityManager am = getSystemService(ActivityManager.class);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        Utils.logDebug(TAG, "onResume() memsize:" + am.getMemoryClass() + " total:" + memoryInfo.totalMem/1000000 + " avail:" + memoryInfo.availMem/1000000 + " threshold:" + memoryInfo.threshold/1000000);

        super.onResume();
    }

    @Override
    protected void onPause() {
        Utils.logDebug(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Utils.logDebug(TAG, "onSaveInstanceState() outState:" + outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Utils.logDebug(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Utils.logDebug(TAG, "onDestroy()");
        super.onDestroy();
    }
}

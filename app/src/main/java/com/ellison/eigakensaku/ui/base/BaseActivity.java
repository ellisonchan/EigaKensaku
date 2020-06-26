package com.ellison.eigakensaku.ui.base;

import android.os.Bundle;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.ui.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Toolbar mToolbar;
    protected FloatingActionButton mFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }
//
//        mFabButton = (FloatingActionButton) findViewById(R.id.fab);
//        if(mFabButton != null) {
//            mFabButton.setOnClickListener(this);
//        }

        // init();
    }

    protected abstract void init();

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
//        if (v == null) {
//            return;
//        }
//
//        switch (v.getId()) {
//            case R.id.fab:
//                onFabBtnClicked();
//                break;
//        }
    }

//    protected abstract void onFabBtnClicked();

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
}

package com.ellison.eigakensaku.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.ellison.eigakensaku.ui.base.BaseActivity;
import com.ellison.eigakensaku.ui.base.BaseFragment;
import com.ellison.eigakensaku.ui.info.InfoFragment;
import com.ellison.eigakensaku.ui.search.SearchFragment;
import com.ellison.eigakensaku.ui.star.StarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;

import com.ellison.eigakensaku.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.view_pager)
    ViewPager mPager;
    @BindView(R.id.nav_view)
    BottomNavigationView mNavigation;

    private List<Fragment> fragments = new ArrayList<Fragment>(3);
    private Fragment searchFragment, starFragment, infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void init() {
        searchFragment = new SearchFragment();
        starFragment = new StarFragment();
        infoFragment = new InfoFragment();

        fragments.add(searchFragment);
        fragments.add(starFragment);
        fragments.add(infoFragment);

        if (mPager != null) {
            mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                @Override
                public int getCount() {
                    return fragments.size();
                }

                @NonNull
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }
            });

            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mNavigation.getMenu().getItem(position).setChecked(true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        if (mNavigation != null) {
            mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_search:
                            mPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_star:
                            mPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_info:
                            mPager.setCurrentItem(2);
                            break;
                    }
                    return false;
                }
            });
        }
    }
}
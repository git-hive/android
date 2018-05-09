package com.hive.hive.main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.hive.hive.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    //    BottomNavigationViewEx mBottomNavigationViewEx;
    BottomNavigationView mBottomNavigationView;
    ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);

//        mBottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);


        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.action_home);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.action_feed);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.action_association);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        mBottomNavigationViewEx.setIconVisibility(false);
//        mBottomNavigationViewEx.enableItemShiftingMode(false);
//        mBottomNavigationViewEx.enableShiftingMode(false);
//        mBottomNavigationViewEx.enableAnimation(false);
//        mBottomNavigationViewEx.setTextSize(12);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.action_feed:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.action_association:
                        mViewPager.setCurrentItem(2);
                        break;
//                    case R.id.action_shop:
//                        mViewPager.setCurrentItem(3);
//                        break;
//                    case R.id.action_map:
//                        mViewPager.setCurrentItem(4);
//                        break;
                }
                return true;
            }
        });

    }
}

package com.hive.hive.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

import com.hive.hive.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    BottomNavigationViewEx mBottomNavigationViewEx;
    ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);

        MainFirebaseHandle.getCurrentAssociation(this);


    }

    @Override
    public void onStop(){
        super.onStop();
        mPagerAdapter.getmFeedFragment().getmRecyclerAdapter().sendToFirebase();
    }

    public void initViews(){
        mBottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        mViewPager = findViewById(R.id.view_pager);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationViewEx.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationViewEx.setIconVisibility(false);
        mBottomNavigationViewEx.enableItemShiftingMode(false);
        mBottomNavigationViewEx.enableShiftingMode(false);
        mBottomNavigationViewEx.enableAnimation(false);
        mBottomNavigationViewEx.setTextSize(12);

        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        //saving supports when leaving feed
                        mPagerAdapter.getmFeedFragment().getmRecyclerAdapter().sendToFirebase();
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.action_feed:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.action_association:
                        //save supports, when leaving feed
                        mPagerAdapter.getmFeedFragment().getmRecyclerAdapter().sendToFirebase();
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

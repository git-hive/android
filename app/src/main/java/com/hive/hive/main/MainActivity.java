package com.hive.hive.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hive.hive.R;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.login.LoginActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    BottomNavigationViewEx mBottomNavigationViewEx;
    ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);
        if(HomeFragment.mUser == null){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, getResources().getString(R.string.no_association), Toast.LENGTH_SHORT).show();
            finish();
        }
        initViews();

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

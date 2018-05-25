package com.hive.hive.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hive.hive.association.AssociationFragment;
import com.hive.hive.feed.FeedFragment;
import com.hive.hive.home.HomeFragment;
import com.hive.hive.map.MapFragment;
import com.hive.hive.store.StoreFragment;

/**
 * Created by vplentz on 03/01/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    //    private static int NUM_ITEMS = 5;
    private static int NUM_ITEMS = 3;
    private FeedFragment mFeedFragment;
    private HomeFragment mHomeFragment;
    private AssociationFragment mAssociationFragment;
    public ViewPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);

        mHomeFragment = HomeFragment.newInstance();
        mFeedFragment = FeedFragment.newInstance();
        mAssociationFragment = AssociationFragment.newInstance();
    }


    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return mHomeFragment;
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return mFeedFragment;

            case 2: // Fragment # 1 - This will show SecondFragment
                return mAssociationFragment;

//            case 3: // Fragment # 1 - This will show SecondFragment
//                return StoreFragment.newInstance();
//            case 4: // Fragment # 1 - This will show SecondFragment
//                return MapFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    public FeedFragment getmFeedFragment() {
        return mFeedFragment;
    }

    public HomeFragment getmHomeFragment() {
        return mHomeFragment;
    }

    public AssociationFragment getmAssociationFragment() {
        return mAssociationFragment;
    }
}
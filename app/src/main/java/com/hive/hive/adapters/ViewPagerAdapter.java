package com.hive.hive.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hive.hive.fragments.AssociationFragment;
import com.hive.hive.fragments.FeedFragment;
import com.hive.hive.fragments.HomeFragment;
import com.hive.hive.fragments.MapsFragment;
import com.hive.hive.fragments.ShopFragment;

/**
 * Created by vplentz on 03/01/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 5;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
                return HomeFragment.newInstance("0", "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return FeedFragment.newInstance("1", "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return AssociationFragment.newInstance("2", "Page # 3");
            case 3: // Fragment # 1 - This will show SecondFragment
                return ShopFragment.newInstance("3", "Page # 4");
            case 4: // Fragment # 1 - This will show SecondFragment
                return MapsFragment.newInstance("4", "Page # 5");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
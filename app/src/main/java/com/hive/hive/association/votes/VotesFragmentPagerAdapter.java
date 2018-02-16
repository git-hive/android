package com.hive.hive.association.votes;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hive.hive.association.votes.tabs.CurrentFragment;
import com.hive.hive.association.votes.tabs.FutureFragment;
import com.hive.hive.association.votes.tabs.OldFragment;

/**
 * Created by birck on 04/02/18.
 */

public class VotesFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Passadas", "Ativas", "Futuras" };
    private Context context;

    public VotesFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return OldFragment.newInstance(position+1);
            case 1: // Fragment # 0 - This will show SecondFragment different title
                return CurrentFragment.newInstance(position+1);
            case 2: // Fragment # 1 - This will show ThirdFragment
                return FutureFragment.newInstance(position+1);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
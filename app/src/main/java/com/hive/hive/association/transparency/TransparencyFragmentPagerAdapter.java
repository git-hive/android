package com.hive.hive.association.transparency;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hive.hive.association.transparency.tabs.document.DocumentFragment;
import com.hive.hive.association.transparency.tabs.budget.BudgetFragment;
import com.hive.hive.association.transparency.tabs.overview.OverviewFragment;
import com.hive.hive.association.transparency.tabs.staff.StaffFragment;
import com.hive.hive.utils.FileUtils;

/**
 * Created by Marco Antônio Birck on 16/02/18.
 * Update by Nícolas Oreques de Araujo on 03/04/18
 */

public class TransparencyFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = TransparencyFragmentPagerAdapter.class.getSimpleName();

    //--- Parameters
    final int PAGE_COUNT = 1;
    private String tabTitles[] = new String[]{"Documentos"};


    //--- Context
    private Context context;
    private Activity activity;

    TransparencyFragmentPagerAdapter(FragmentManager fm, Context context, Activity activity) {
        super(fm);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DocumentFragment.newInstance(position + 1);
//            case 1: // Fragment # 0 - This will show SecondFragment different title
//                return DocumentFragment.newInstance(position + 1);
//            case 2: // Fragment # 1 - This will show ThirdFragment
//                return StaffFragment.newInstance(position + 1);
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
package com.hive.hive.association.transparency;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hive.hive.association.transparency.tabs.bills.BillsFragment;
import com.hive.hive.association.transparency.tabs.budget.BudgetFragment;
import com.hive.hive.association.transparency.tabs.cash.CashFragment;
import com.hive.hive.association.transparency.tabs.staff.StaffFragment;

/**
 * Created by birck on 16/02/18.
 */

public class TransparencyFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Caixa", "Boletos", "Orçamentos", "Funcionários" };
    private Context context;

    public TransparencyFragmentPagerAdapter(FragmentManager fm, Context context) {
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
                return CashFragment.newInstance(position+1);
            case 1: // Fragment # 0 - This will show SecondFragment different title
                return BillsFragment.newInstance(position+1);
            case 2: // Fragment # 1 - This will show ThirdFragment
                return BudgetFragment.newInstance(position+1);
            case 3: // Fragment # 1 - This will show FourthFragment
                return StaffFragment.newInstance(position+1);
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
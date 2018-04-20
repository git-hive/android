package com.hive.hive.utils.circularFilter.scale;


/**
 * Created by birck on 18/02/18.
 */


import android.content.Context;

import com.hive.hive.utils.circularFilter.BaseFragment;
import com.hive.hive.utils.circularFilter.CircularFilterUtils;
import com.leochuan.ScaleLayoutManager;


public class ScaleLayoutActivity extends BaseFragment<ScaleLayoutManager, ScalePopUpWindow> {

    @Override
    protected ScaleLayoutManager createLayoutManager() {
        return new ScaleLayoutManager(this.getActivity().getApplicationContext(), CircularFilterUtils.Dp2px(this.getActivity().getApplicationContext(), 30));
    }

    @Override
    protected ScalePopUpWindow createSettingPopUpWindow() {
        return new ScalePopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
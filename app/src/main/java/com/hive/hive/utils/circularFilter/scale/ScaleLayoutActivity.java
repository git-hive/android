package com.hive.hive.utils.circularFilter.scale;


/**
 * Created by birck on 18/02/18.
 */


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hive.hive.utils.circularFilter.BaseFragment;
import com.hive.hive.utils.circularFilter.CircularFilterUtils;
import com.leochuan.ScaleLayoutManager;


public class ScaleLayoutActivity extends BaseFragment<ScaleLayoutManager, ScalePopUpWindow> {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected ScaleLayoutManager createLayoutManager() {
        return new ScaleLayoutManager(this.getContext(), CircularFilterUtils.Dp2px(this.getContext(), 30));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected ScalePopUpWindow createSettingPopUpWindow() {
        return new ScalePopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
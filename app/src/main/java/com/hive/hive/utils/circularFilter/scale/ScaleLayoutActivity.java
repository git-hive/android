package com.hive.hive.utils.circularFilter.scale;


/**
 * Created by birck on 18/02/18.
 */


import com.hive.hive.utils.circularFilter.Util;
import com.leochuan.ScaleLayoutManager;

/**
 * Created by Dajavu on 27/10/2017.
 */

public class ScaleLayoutActivity extends BaseActivity<ScaleLayoutManager, ScalePopUpWindow> {

    @Override
    protected ScaleLayoutManager createLayoutManager() {
        return new ScaleLayoutManager(this, Util.Dp2px(this, 10));
    }

    @Override
    protected ScalePopUpWindow createSettingPopUpWindow() {
        return new ScalePopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
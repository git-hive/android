package com.hive.hive.utils.circularFilter;

import android.content.Context;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by birck on 18/02/18.
 */




public abstract class SettingPopUpWindow extends PopupWindow {
    public SettingPopUpWindow(Context context) {
        super(context);
        setOutsideTouchable(true);
        setWidth(Util.Dp2px(context, 320));
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
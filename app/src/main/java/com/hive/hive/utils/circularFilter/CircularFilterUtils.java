package com.hive.hive.utils.circularFilter;

import android.content.Context;

import java.util.Locale;

/**
 * Created by birck on 18/02/18.
 */

public class CircularFilterUtils {
    public static int Dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String formatFloat(float value) {
        return String.format(Locale.getDefault(), "%.3f", value);
    }
}
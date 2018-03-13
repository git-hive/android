package com.hive.hive.association.transparency.tabs.staff;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by birck on 17/02/18.
 */


public class CustomGridView extends GridView {

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = 200;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else { // Be whatever you want
            width = desiredWidth;
        }

        // MUST CALL THIS

        setMeasuredDimension(width, mHeight);
    }

    int mHeight;

    public void SetHeight(int height) {
        mHeight = height;
    }
}
package com.hive.hive.utils.hexagonsPercentBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import android.graphics.RectF;

import android.os.Bundle;
import android.os.Parcelable;

import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.hive.hive.R;


/**
 * Created by Birck
 */

public class HexagonView extends View{


    private Paint paint = new Paint();

    public HexagonView(Context context) {
        this(context, null);
    }

    public HexagonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexagonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override protected void onDraw(Canvas canvas) {
        // General Rect
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        canvas.drawRect(0, 0, 80, 80, paint);

        // Up Rect
        paint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, 40, 80, paint );

        // Down Rect
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(40, 0, 80, 80, paint );

    }



}

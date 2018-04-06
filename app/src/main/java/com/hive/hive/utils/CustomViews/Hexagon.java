package com.hive.hive.utils.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by birck on 06/04/18.
 */


public class Hexagon extends View {
    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    public float mCenterX, mCenterY;
    private float width, height;
    private int maskColor, mHexagonColor;
    private char mFirstLetter;

    public Hexagon(Context context) {
        super(context);
        init();
    }

    public Hexagon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Hexagon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        hexagonPath = new Path();
        hexagonBorderPath = new Path();
        maskColor = 0xFF01FF77;
    }

    public void setRadius(float r) {
        this.radius = r;
        calculatePath();
    }

    public void setMaskColor(int color) {
        this.maskColor = color;
        invalidate();
    }

    private void calculatePath() {
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = width/2;
        float centerY = height/2;
        mCenterX = centerX;
        mCenterY = centerY;
        hexagonPath.moveTo(centerX, centerY + radius);
        hexagonPath.lineTo(centerX - triangleHeight, centerY + radius/2);
        hexagonPath.lineTo(centerX - triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX, centerY - radius);
        hexagonPath.lineTo(centerX + triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX + triangleHeight, centerY + radius/2);
        hexagonPath.moveTo(centerX, centerY + radius);

        float radiusBorder = radius - 5;
        float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + radiusBorder/2);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - radiusBorder/2);
        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - radiusBorder/2);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + radiusBorder/2);
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        invalidate();
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        c.clipPath(hexagonPath);
        c.drawColor(mHexagonColor);

        Paint mPaint = new Paint();

        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.FILL);

        c.drawText(String.valueOf(mFirstLetter).toLowerCase(), mCenterX, mCenterY+(3*getResources().getDisplayMetrics().density), mPaint);
    }

    // getting the view size and default radius
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height =  MeasureSpec.getSize(heightMeasureSpec);
        radius = height / 2 - 10;
        calculatePath();
    }

    public void setFirstLetter(char letter){
        mFirstLetter = letter;
    }
    public void setHexagonColor(int color){
        mHexagonColor = color;
    }
}
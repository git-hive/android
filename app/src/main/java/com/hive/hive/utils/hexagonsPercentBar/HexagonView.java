package com.hive.hive.utils.hexagonsPercentBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Birck
 */

public class HexagonView extends View{
    // Defining bar fixed size

    public Paint trianglePaint = new Paint();

    float DEN = getResources().getDisplayMetrics().density;
    float FINAL_WIDTH = 300;
    float FINAL_HEIGHT = 35;

    //          Closely Related      //
    float FINAL_SPACE = 8;
    float FINAL_SPACE_UNITY = 2 * DEN;
    //////////////////////////////////

    float FINAL_HEXAGON_RATIO = (float) 0.1;
    float FIXED_FULL_STEP = FINAL_WIDTH * FINAL_HEXAGON_RATIO * DEN;
    int FINAL_BAR_LENGTH = 10;

    // Config values
    int mNumOfOptions = 0;
    ArrayList<Float> mPercentage;
    ArrayList<Integer> mBarColors;

    //Hexagon stuff
    float ratioRadius = (float) 0.17;
    int numberOfPoint = 32; //default


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
        super.onDraw(canvas);
        trianglePaint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);

        float start_x = 0 * DEN, start_y = 0 * DEN, end_x =  FIXED_FULL_STEP, end_y= FINAL_HEIGHT * DEN;

        int index = 0;

        float fillAll = 10;
        float currentFilled = 0;

        for (Float value:
                mPercentage) {
            System.out.println(" I ENTER _________________________________________");

            while(true){

                    if(fillAll - currentFilled > value) {

                        // Hexagon

                        Paint paint = new Paint();
                        System.out.println(" I first _________________________________________ "+(fillAll - currentFilled)+" "+value+" "+start_x+ " "+index+" "+value/10);

                        paint.setColor(mBarColors.get(index));
                        canvas.drawRect(start_x, start_y, start_x + ((value/10) * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), end_y, paint);

                        start_x += (value/10 * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN);

                        currentFilled += value;



                        index += 1;
                        break;

                    }else if(fillAll - currentFilled <= value){
                        System.out.println(" I Second _________________________________________ "+(fillAll - currentFilled)+" "+value+" "+start_x+ " "+index+" "+value/10);

                        paint.setColor(mBarColors.get(index));

                        canvas.drawRect(start_x, start_y, start_x + ((fillAll - currentFilled)/10 * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), end_y, paint);

                        start_x += (fillAll - currentFilled)/10 * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN;

                        value -= (fillAll-currentFilled);

                        start_x += FINAL_SPACE_UNITY;

                        currentFilled = 0;
                    }

            }

        }

        //drawTriangles(canvas, 0 , 30);
        //drawTriangles(canvas, 0*DEN , 0, 10*DEN);
        start_x = 0;
        for(int i=0;i<numberOfPoint;i++){
            drawTriangles(canvas, start_x + (i * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), 0, 10 * DEN);
            start_x += FINAL_SPACE_UNITY;
        }


    }



    public void setConfig(ArrayList<Float> percentage){
        mPercentage = percentage;
        postInvalidate();
    }

    public void autoInit(int numOfOptions){
        mPercentage = new ArrayList<>();
        mNumOfOptions = numOfOptions;
//        for(int i=0;i<mNumOfOptions;i++){
//            mPercentage.add(i, (float)100/mNumOfOptions);
//            System.out.println(mPercentage.get(i) +"   HEEEEEEY");
//        }
        mPercentage.add(0, (float) 37.0);
        mPercentage.add(1, (float) 13.0);
        mPercentage.add(2, (float) 14.0);
        mPercentage.add(3, (float) 36.0);

        mBarColors = new ArrayList<>();
        mBarColors.add(0, Color.RED);
        mBarColors.add(1, Color.BLUE);
        mBarColors.add(2, Color.YELLOW);
        mBarColors.add(3, Color.MAGENTA);

    }

    public void drawTriangles(Canvas canvas, float start_x, float start_y, float size){

        float new_start_x;

        Path path1 = new Path();
        path1.moveTo(start_x, start_y);

        path1.lineTo(start_x + size, 0);
        path1.lineTo(start_x, start_y + size);
        path1.lineTo(start_x, 0);

        canvas.drawPath(path1, trianglePaint);

        new_start_x = start_x + ((FINAL_WIDTH * FINAL_HEXAGON_RATIO) * DEN);

        Path path2 = new Path();
        path2.moveTo(new_start_x - size, start_y);

        path2.lineTo(new_start_x, 0);
        path2.lineTo(new_start_x - size, start_y + size);
        path2.lineTo(new_start_x - size, 0);

        Matrix mMatrix = new Matrix();
        RectF bounds = new RectF();
        path2.computeBounds(bounds, true);
        mMatrix.postRotate(90, bounds.centerX(), bounds.centerY());
        path2.transform(mMatrix);

        canvas.drawPath(path2, trianglePaint);

        start_y = 25 * DEN;
        Path path3 = new Path();
        path3.moveTo(start_x, start_y);

        path3.lineTo(start_x + size, 25 * DEN);
        path3.lineTo(start_x, start_y + size);
        path3.lineTo(start_x, 25 *DEN);
        Matrix mMatrix3 = new Matrix();
        RectF bounds3 = new RectF();

        path3.computeBounds(bounds3, true);
        mMatrix3.postRotate(-90, bounds3.centerX(), bounds3.centerY());
        path3.transform(mMatrix3);

        canvas.drawPath(path3, trianglePaint);


        start_y = (25) * DEN;
        Path path4 = new Path();
        path4.moveTo(start_x+ (20*DEN), start_y);

        path4.lineTo(start_x + size + (20*DEN), 25 * DEN);
        path4.lineTo(start_x+(20*DEN), start_y + size);
        path4.lineTo(start_x+(20*DEN), 25 *DEN);
        Matrix mMatrix4 = new Matrix();
        RectF bounds4 = new RectF();

        path4.computeBounds(bounds4, true);
        mMatrix4.postRotate(180, bounds4.centerX(), bounds4.centerY());
        path4.transform(mMatrix4);

        canvas.drawPath(path4, trianglePaint);



    }


}

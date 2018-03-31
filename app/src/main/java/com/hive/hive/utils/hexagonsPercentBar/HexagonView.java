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
    float FINAL_WIDTH = 200;
    float FINAL_HEIGHT = 24;

    //          Closely Related      //
    float FINAL_SPACE = 8;
    float FINAL_SPACE_UNITY = 7 * DEN;
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
        trianglePaint.setAntiAlias(true);
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
        start_y = 0;
        for(int i=0;i<numberOfPoint;i++){
            drawTriangles(canvas, start_x + (i * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), start_y * DEN, 10 * DEN);
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
        mBarColors.add(0, Color.parseColor("#ff6347"));
        mBarColors.add(1, Color.parseColor("#82b3b3"));
        mBarColors.add(2, Color.parseColor("#fbfb33"));
        mBarColors.add(3, Color.parseColor("#90ee90"));

    }

    public void drawTriangles(Canvas canvas, float start_x, float start_y, float size){
        float new_start_x;
        float factor = 4 * DEN;
        float height_size = FINAL_HEIGHT * DEN;

        // -- First Hexagon -- //
        Path path1 = new Path();
        path1.moveTo(start_x, start_y);

        path1.lineTo(start_x  + size, 0 * DEN);
        path1.lineTo(start_x, start_y + size - factor);
        path1.lineTo(start_x, 0 * DEN);

        canvas.drawPath(path1, trianglePaint);
        // --------------------------------//


        // -- Second Hexgon -- //
        new_start_x = start_x + size;
        Path path2 = new Path();
        path2.moveTo(new_start_x, start_y);

        path2.lineTo(new_start_x + size, 0);
        path2.lineTo(new_start_x +size, start_y + size - factor);
        path2.lineTo(new_start_x , start_y);

        canvas.drawPath(path2, trianglePaint);
        // ----------------------------- //

        // -- third Hexagon -- //
        Path path3 = new Path();
        path3.moveTo(start_x, start_y + height_size - size + factor);

        path3.lineTo(start_x, start_y+height_size);
        path3.lineTo(start_x+size, start_y+height_size);
        path3.lineTo(start_x, start_y + height_size - size + factor);

        canvas.drawPath(path3, trianglePaint);
        // --------------------------------//


        // -- Fourth Hexagon -- //
        new_start_x = start_x + FINAL_WIDTH * FINAL_HEXAGON_RATIO * DEN;
        Path path4 = new Path();
        path4.moveTo(new_start_x, start_y + height_size - size + factor);

        path4.lineTo(new_start_x, start_y + height_size);
        path4.lineTo(new_start_x - size, start_y + height_size);
        path4.lineTo(new_start_x, start_y + height_size - size + factor);

        canvas.drawPath(path4, trianglePaint);
        // --------------------------------//



    }


}

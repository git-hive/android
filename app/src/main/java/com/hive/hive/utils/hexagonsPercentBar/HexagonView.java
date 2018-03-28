package com.hive.hive.utils.hexagonsPercentBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Birck
 */

public class HexagonView extends View{
    // Defining bar fixed size
    float DEN = getResources().getDisplayMetrics().density;
    float FINAL_WIDTH = 300;
    float FINAL_HEIGHT = 30;

    //          Closely Related      //
    float FINAL_SPACE = 8;
    float FINAL_SPACE_UNITY = 1 * DEN;
    //////////////////////////////////

    float FINAL_HEXAGON_RATIO = (float) 0.1;
    float FIXED_FULL_STEP = FINAL_WIDTH * FINAL_HEXAGON_RATIO * DEN;
    int FINAL_BAR_LENGTH = 10;

    // Config values
    int mNumOfOptions = 0;
    ArrayList<Float> mPercentage;
    ArrayList<Integer> mBarColors;


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
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        //canvas.drawRect(0 * DEN, 0 *DEN, (FINAL_WIDTH + FINAL_SPACE) * DEN , (FINAL_HEIGHT + FINAL_SPACE) * DEN, paint);



        float start_x = 0 * DEN, start_y = 0 * DEN, end_x =  FIXED_FULL_STEP, end_y= FINAL_HEIGHT * DEN;

        int index = 0;

        float fillAll = 10;
        float currentFilled = 0;

        for (Float value:
                mPercentage) {
            System.out.println(" I ENTER _________________________________________");

            while(true){

//                if(value > 0 && value<10){

                // If value is smaller than an hexagon and no fills it
                    if(fillAll - currentFilled > value) {
                        System.out.println(" I first _________________________________________ "+(fillAll - currentFilled)+" "+value+" "+start_x+ " "+index+" "+value/10);

                        paint.setColor(mBarColors.get(index));
                        canvas.drawRect(start_x, start_y, start_x + ((value/10) * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), end_y, paint);

                        start_x += (value/10 * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN);

                        currentFilled += value;

                        // When put the space or not between hexagons

//                            start_x += FINAL_SPACE_UNITY;
//                            currentFilled = 0;

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


//                }else if(value >= 10){
//                    paint.setColor(mBarColors.get(index));
//                    start_x += end_x + FINAL_SPACE_UNITY;
//                    end_x += (FIXED_FULL_STEP);
//
//                    canvas.drawRect(start_x, start_y, end_x, end_y, paint);
//
//                    // Decrease one hexagon
//                    value -= 10;
//                    currentFilled = 0;
//                }

            }

        }
//        // Up Rect
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(0, 0, 40, 80, paint);
//
//        // Down Rect
//        paint.setStrokeWidth(0);
//        paint.setColor(Color.CYAN);
//        canvas.drawRect(40, 0, 80, 80, paint);

    }

    public void setConfig(ArrayList<Float> percentage){
        mPercentage = percentage;
        postInvalidate();
    }

    public void autoInit(int numOfOptions){
        mPercentage = new ArrayList<>();
        mNumOfOptions = numOfOptions;
        for(int i=0;i<mNumOfOptions;i++){
            mPercentage.add(i, (float)100/mNumOfOptions);
            System.out.println(mPercentage.get(i) +"   HEEEEEEY");
        }

        mBarColors = new ArrayList<>();
        mBarColors.add(0, Color.RED);
        mBarColors.add(1, Color.BLUE);
        mBarColors.add(2, Color.YELLOW);
        mBarColors.add(3, Color.MAGENTA);
    }



}

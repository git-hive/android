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

import com.hive.hive.utils.Utils;
import com.hive.hive.utils.circularFilter.CircularFilterUtils;

import java.util.ArrayList;
import java.util.stream.IntStream;


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

    // Config values
    ArrayList<Float> mPercentage;
    ArrayList<Integer> mBarColors;

    //Hexagon stuff
    float ratioRadius = (float) 0.17;
    int numberOfPoint = 32; //default


    private Paint paint = new Paint();

    public HexagonView(Context context) {
        this(context, null);
        autoInit();
    }

    public HexagonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        autoInit();

    }

    public HexagonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        autoInit();
    }


    @Override protected void onDraw(Canvas canvas) {
        // General Rect
        float theta = (float) 0.01;
        super.onDraw(canvas);
        trianglePaint.setColor(Color.WHITE);
        trianglePaint.setAntiAlias(true);
        paint.setStrokeWidth(0);

        float start_x = 0 * DEN, start_y = 0 * DEN, end_x =  FIXED_FULL_STEP, end_y= FINAL_HEIGHT * DEN;

        int index = 0;

        float fillAll = 10;
        float currentFilled = 0;
       // System.out.println(" MFuckingPercentage "+mPercentage.size());
        for (Float value:
                mPercentage) {
         //   System.out.println(" I ENTER _________________________________________");

            while(true){
                    //Making hard coded stop to really small numbers
//                    if(value < theta) {
//                        value = Float.valueOf(0);
//                    }



                    if(fillAll - currentFilled > value) {

                        // Hexagon

                        Paint paint = new Paint();
                      //  System.out.println(" I first _________________________________________ "+(fillAll - currentFilled)+" "+value+" "+start_x+ " "+index+" "+value/10);

                        paint.setColor(mBarColors.get(index));
                        canvas.drawRect(start_x, start_y, start_x + ((value/10) * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN), end_y, paint);

                        start_x += (value/10 * FINAL_HEXAGON_RATIO * FINAL_WIDTH * DEN);

                        currentFilled += value;



                        index += 1;
                        break;

                    }else if(fillAll - currentFilled <= value){
                  //      System.out.println(" I Second _________________________________________ "+(fillAll - currentFilled)+" "+value+" "+start_x+ " "+index+" "+value/10);

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



    public void setConfig(ArrayList<Float> scoreValues){

        // Such an uggly code
        float totalScores = 0;
        for (Float score:
             scoreValues) {
            //System.out.println(" HAAYAYAHAHYAAHAYAYA"+score);
            totalScores +=  score;
        }

        //System.out.println(" Total scores "+ totalScores);
        for(int i = 0;i<scoreValues.size();i++){
            if(totalScores == 0) {
                //System.out.println(" BEYYYYYYYYYYYYYYYY 1 "+ scoreValues.get(i)+" " + scoreValues.get(i) / totalScores);
                mPercentage.add(i, (float) 25.0);
               // break;
            }else {
                System.out.println(" BEYYYYYYYYYYYYYYYY  2 "+ scoreValues.get(i)+" " + scoreValues.get(i) / totalScores);
                mPercentage.add(i, Utils.round(scoreValues.get(i) / totalScores * 100, 2));
              //  break;
            }
            //mPercentage.add(i, (float) 25.0);
        }

        postInvalidate();
    }

    public void autoInit(){
        mPercentage = new ArrayList<>();

        mBarColors = new ArrayList<>();
        mBarColors.add(0, Color.parseColor("#ff6347"));
        mBarColors.add(1, Color.parseColor("#82b3b3"));
        mBarColors.add(2, Color.parseColor("#ffbb3f"));
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

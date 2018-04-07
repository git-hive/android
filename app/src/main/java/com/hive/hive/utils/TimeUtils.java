package com.hive.hive.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.hive.hive.R;
import com.hive.hive.model.association.Session;

import java.util.Calendar;


public class TimeUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;



    public static String getTimeFromTimestamp(Context context,long timestamp){
        String text = "";
        if (timestamp > DAY) {
            text = text.concat(timestamp / DAY+" ").concat(context.getString(R.string.days));
            timestamp %= DAY;
        }
        if (timestamp > HOUR) {
            text = text.concat(" "+timestamp / HOUR+" ").concat(context.getString(R.string.hours));
            timestamp %= HOUR;
        }
        if (timestamp > MINUTE) {
            text = text.concat(" "+timestamp / MINUTE+" ").concat(context.getString(R.string.minutes));
        }
        return text;
    }

    public static CountDownTimer clock(final TextView textView, Session currentSession, final Context context){
        CountDownTimer timer = null;
        if(currentSession != null) {
           timer = new CountDownTimer(currentSession.getEndsAt() - Calendar.getInstance().getTimeInMillis(), 60000) {//ticks every minute
                @Override
                public void onTick(long l) {
                    textView.setText(TimeUtils.getTimeFromTimestamp(context, l));
                }

                @Override
                public void onFinish() {
                    textView.setText(context.getString(R.string.session_fin));
                }
            };
            timer.start();
        }
        return timer;
    }

}

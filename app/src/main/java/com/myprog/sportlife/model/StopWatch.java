package com.myprog.sportlife.model;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

public class StopWatch {
    private int sec = 0;
    public static boolean isStart = false;
    private Intent intent;
    private Context context;
    private String stopWatchAction = "com.myprog.sporlifeStopWatch";

    public StopWatch(Context context){
        this.context = context;
    }

    public void start(){
        isStart = true;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = sec/3600;
                int minutes = (sec%3600)/60;
                int seconds = sec % 60;
                sec++;
                intent = new Intent();
                intent.setAction(stopWatchAction);
                intent.putExtra("hours", hours);
                intent.putExtra("minutes", minutes);
                intent.putExtra("seconds", seconds);
                context.sendBroadcast(intent);
                if(isStart)
                    handler.postDelayed(this, 1000);
            }
        });
    }

    public void stop(){
        isStart = false;
    }
}

package com.myprog.sportlife.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;

import com.myprog.sportlife.R;
import com.myprog.sportlife.data.DataManager;

// Проверить в реальных условиях

public class Accelerometer implements SensorEventListener {
    private Context context;
    private SoundPool sSoundPool;
    private double magnitudePrevious = 10;
    private int counter = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    public Accelerometer(Context context){
        this.context = context;
    }

    public void start(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sSoundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();

        sSoundPool.load(context, R.raw.sound, 1);
    }

    public void stop(){
        DataManager.updateInDBCountSteps(counter);
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double magnitude = Math.sqrt(event.values[0] * event.values[0] +
                event.values[1] * event.values[1] +
                event.values[2] * event.values[2]);

        double magnitudeDelta = magnitude - magnitudePrevious;
        magnitudePrevious = magnitude;

        if(magnitudeDelta > 7.5){
            counter++;
            sSoundPool.play(1,1.0f, 1.0f, 1, 0 ,0f);
            if(counter % 50 == 0)
                DataManager.updateInDBCountSteps(counter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.myprog.sportlife.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.myprog.sportlife.R;
import com.myprog.sportlife.model.Accelerometer;
import com.myprog.sportlife.model.Coordinate;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.StopWatch;

import java.util.ArrayList;

public class ServiceTraining extends Service {

    String channelid = "channel";
    public static final String gps_action = "com.myprog.sporlifeGetGPS";
    public static final int chanelId = 2;
    private String serviceTitle = "Идёт тренировка";
    private String serviceAction = "com.myprog.sportLife";
    private int counterCoordinate = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ArrayList<Coordinate> listCoordinate;
    private int delay = 0; // 10 sec
    private Accelerometer accelerometer;
    private StopWatch stopWatch;
    private double distance = 0;
    private double calories;
    private Location previousLocation;
    //private Intent intentBroadcastReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelid,
                    channelid, NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Уведомление тренировки");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        //intentBroadcastReceiver = new Intent();
        //intentBroadcastReceiver.setAction(gps_action);

        listCoordinate = new ArrayList<>();

        Notification notification = new NotificationCompat.Builder(this, channelid)
                .setContentTitle(serviceTitle)
                .setSmallIcon(R.drawable.run)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        startForeground(2, notification);

        startGps();
        startAccelerometer();
        startStopWatch();

        return START_NOT_STICKY;
    }


    private void startStopWatch(){
        stopWatch = new StopWatch(this);
        stopWatch.start();
    }

    private void startAccelerometer(){
        accelerometer = new Accelerometer(ServiceTraining.this);
        accelerometer.start();
    }

    private void startGps(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || delay != 5) {
                    delay++;
                    previousLocation = locationResult.getLocations().get(0);
                    return;
                }


                for (Location location : locationResult.getLocations()) {
                    counterCoordinate++;
                    double speed = location.getSpeed() * 3.6;
                    distance += location.distanceTo(previousLocation)/1000;
                    calories += (65 * speed * (location.distanceTo(previousLocation)/1000)) / 4.184;
                    sendValueToFragment(speed, distance, calories);
                    Log.d("TrainingFragment", String.valueOf(speed));
                    Coordinate coordinate = new Coordinate(counterCoordinate, speed, location.getLatitude(), location.getLongitude());
                    DataManager.addCoordinatesToTraining(coordinate);
                    previousLocation = location;
                }
            }
        };

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                getMainLooper());
    }

    private void sendValueToFragment(double speed, double distance, double calories){
        Intent intent = new Intent();
        intent.setAction(serviceAction);
        intent.putExtra("speed", speed);
        intent.putExtra("distance", distance);
        intent.putExtra("calories", calories);
        ServiceTraining.this.sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
        accelerometer.stop();
        stopWatch.stop();
        stopSelf();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}

package com.myprog.sportlife.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.myprog.sportlife.Interface.DetailTrainingListener;
import com.myprog.sportlife.R;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.Coordinate;
import com.myprog.sportlife.model.Training;
import com.myprog.sportlife.model.WorkaroundMapFragment;

import java.util.ArrayList;

public class DetailTrainingActivity extends AppCompatActivity implements DetailTrainingListener{

    private Training training;
    private ArrayList<Coordinate> allCoordinate;
    private TextView textViewDateTotal, textViewDateStart, textViewDateEnd, textViewTotalDistance,
            textViewSteps, textViewAverageSpeed, textViewMaxSpeed, textViewMinSpeed, textViewAmountKal;
    private GoogleMap mMap;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training);

        DataManager.addDetailTrainingListener(this);
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("TrainingId");
        DataManager.getTrainingAndListCoordinate(id);
    }

    private void init(){
        textViewDateTotal = findViewById(R.id.detailTraining_TotalDate);
        textViewDateStart = findViewById(R.id.detailTraining_StartDate);
        textViewDateEnd = findViewById(R.id.detailTraining_EndDate);
        textViewTotalDistance = findViewById(R.id.detailTraining_TotalDistance);
        textViewSteps = findViewById(R.id.detailTraining_Steps);
        textViewAverageSpeed = findViewById(R.id.detailTraining_AverageSpeed);
        textViewMaxSpeed = findViewById(R.id.detailTraining_MaxSpeed);
        textViewMinSpeed = findViewById(R.id.detailTraining_MinSpeed);
        textViewAmountKal = findViewById(R.id.detailTraining_KalAmount);

        String time = String.valueOf(training.getTotalTime());
        String totalTime = "";
        ArrayList<String> listTime = new ArrayList<>();
        for(String s: time.split(  " ")){
            listTime.add(s);
        }
        if(!listTime.get(0).equals("0") && !listTime.get(0).equals("null")){
            totalTime += listTime.get(0) + "ч. " + listTime.get(1) + "м. " + listTime.get(2) + "c.";
        } else if(!listTime.get(1).equals("0")){
            totalTime += listTime.get(1) + "м. " + listTime.get(2) + "c.";
        } else if(!listTime.get(2).equals("0") ){
            totalTime += listTime.get(2) + "с. ";
        } else {
            totalTime += " 0с.";
        }
        textViewDateTotal.append(" " + totalTime);

        String timeStart = String.valueOf(training.getDateStart());
        timeStart = timeStart.substring(0, 20);
        textViewDateStart.append(" " + timeStart);

        String timeEnd = String.valueOf(training.getDateEnd());
        timeEnd = timeEnd.substring(0, 20);
        textViewDateEnd.append(" " + timeEnd);

        double totalDistance = training.getTotalDistance();
        if(totalDistance > 1000) {
            totalDistance /= 1000;
            String totalDistanceS = " " + String.valueOf(totalDistance).substring(0, 5) + " км";
            textViewTotalDistance.append(totalDistanceS);
        }else if(totalDistance > 0){
            totalDistance = Math.round(totalDistance);
            String totalDistanceS = " " + totalDistance + " м";
            textViewTotalDistance.append(totalDistanceS);
        } else {
            textViewTotalDistance.append(" 0");
        }

        textViewSteps.append(" " + training.getCountStep());

        double averageSpeed = training.getAverageSpeed();
        if(averageSpeed > 10){
            String averageSpeedS = String.valueOf(averageSpeed);
            averageSpeedS = averageSpeedS.substring(0, 4);
            textViewAverageSpeed.append(" " + averageSpeedS + " км.ч");
        } else if(averageSpeed > 0){
            String averageSpeedS = String.valueOf(averageSpeed);
            averageSpeedS = averageSpeedS.substring(0, 3);
            textViewAverageSpeed.append(" " + averageSpeedS + " км.ч");
        } else {
            textViewAverageSpeed.append(" " + 0 + " км.ч");
        }

        double maxSpeed = 0;
        for(int i = 0; i < allCoordinate.size() - 5; i++){
            double maxSpeedtmp = allCoordinate.get(i).getSpeed() + allCoordinate.get(i+1).getSpeed()
                    + allCoordinate.get(i+2).getSpeed() + allCoordinate.get(i+3).getSpeed()
                    + allCoordinate.get(i+4).getSpeed();
            if((maxSpeedtmp - maxSpeed) > 0.001){
                maxSpeed = maxSpeedtmp;
            }
        }
        if(maxSpeed == 0) {
            for (Coordinate coor : allCoordinate) {
                double maxSpeedtmp = coor.getSpeed();
                if ((maxSpeedtmp - maxSpeed) > 0.001) {
                    maxSpeed = maxSpeedtmp;
                }
            }
            if(maxSpeed > 10){
                String maxSpeedS = String.valueOf(maxSpeed);
                maxSpeedS = maxSpeedS.substring(0, 4);
                textViewMaxSpeed.append(" " + maxSpeedS + " км.ч");
            } else if(maxSpeed > 0){
                String maxSpeedS = String.valueOf(maxSpeed);
                maxSpeedS = maxSpeedS.substring(0, 3);
                textViewMaxSpeed.append(" " + maxSpeedS + " км.ч");
            } else {
                textViewMaxSpeed.append(" " + 0 + " км.ч");
            }
        } else {
            if(maxSpeed > 10){
                maxSpeed = maxSpeed / 5;
                String maxSpeedS = String.valueOf(maxSpeed);
                maxSpeedS = maxSpeedS.substring(0, 4);
                textViewMaxSpeed.append(" " + maxSpeedS + " км.ч");
            } else if(maxSpeed > 0){
                maxSpeed = maxSpeed / 5;
                String maxSpeedS = String.valueOf(maxSpeed);
                maxSpeedS = maxSpeedS.substring(0, 3);
                textViewMaxSpeed.append(" " + maxSpeedS + " км.ч");
            } else {
                textViewMaxSpeed.append(" " + 0 + " км.ч");
            }
        }

        double minSpeed = 100;
        for(int i = 0; i < allCoordinate.size() - 5; i++){
            double minSpeedtmp = allCoordinate.get(i).getSpeed() + allCoordinate.get(i+1).getSpeed()
                    + allCoordinate.get(i+2).getSpeed() + allCoordinate.get(i+3).getSpeed()
                    + allCoordinate.get(i+4).getSpeed();
            if((minSpeed - minSpeedtmp) > 0.001){
                minSpeed = minSpeedtmp;
            }
        }
        if(minSpeed == 100) {
            for (Coordinate coor : allCoordinate) {
                double minSpeedtmp = coor.getSpeed();
                if ((minSpeed - minSpeedtmp) > 0.001) {
                    minSpeed = minSpeedtmp;
                }
            }
            if(minSpeed > 10 && minSpeed != 100){
                String minSpeedS = String.valueOf(minSpeed);
                minSpeedS = minSpeedS.substring(0, 4);
                textViewMinSpeed.append(" " + minSpeedS + " км.ч");
            } else if(minSpeed > 0 && minSpeed != 100){
                String minSpeedS = String.valueOf(minSpeed);
                minSpeedS = minSpeedS.substring(0, 3);
                textViewMinSpeed.append(" " + minSpeedS + " км.ч");
            } else {
                textViewMinSpeed.append(" " + 0 + " км.ч");
            }
        } else {
            if(minSpeed > 10){
                minSpeed = minSpeed / 5;
                String minSpeedS = String.valueOf(minSpeed);
                minSpeedS = minSpeedS.substring(0, 4);
                textViewMinSpeed.append(" " + minSpeedS + " км.ч");
            } else if(minSpeed > 0){
                minSpeed = minSpeed / 5 ;
                String minSpeedS = String.valueOf(minSpeed);
                minSpeedS = minSpeedS.substring(0, 3);
                textViewMinSpeed.append(" " + minSpeedS + " км.ч");
            } else {
                textViewMinSpeed.append(" " + 0 + " км.ч");
            }
        }

        if(training.getCalories() > 0) {
            String amountKal = " " + String.valueOf(training.getCalories()).substring(0, 5) + " кКал";
            textViewAmountKal.append(amountKal);
        } else {
            textViewAmountKal.append(" " + training.getCalories() + " кКал");
        }



    }

    public void initMap(){
        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment)getSupportFragmentManager().findFragmentById(R.id.detailTraining_Map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap){
                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    mScrollView = findViewById(R.id.detailTraining_ScrollView);
                    ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailTraining_Map))
                            .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                @Override
                                public void onTouch()
                                {
                                    mScrollView.requestDisallowInterceptTouchEvent(true);
                                }
                            });
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.RED);
                    if(allCoordinate.size() > 0) {
                        for (Coordinate coordinate : allCoordinate) {
                            polylineOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                allCoordinate.get(0).getLatitude(), allCoordinate.get(0).getLongitude()
                        ), 16));
                        mMap.addPolyline(polylineOptions);
                    }
                }
            });
        }
    }

    @Override
    public void onChanged(Training training, ArrayList<Coordinate> arrayList) {
        this.training = training;
        allCoordinate = arrayList;
        init();
        initMap();
    }
}

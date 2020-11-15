package com.myprog.sportlife.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.myprog.sportlife.data.DataManager;

import java.util.ArrayList;

// Переделать полностью!!!


public class SpeedAverage {
    private ArrayList<Coordinate> allCoordinate;

    public SpeedAverage(ArrayList<Coordinate> allCoordinate){
        this.allCoordinate = allCoordinate;
    }

    public double start(){
        double averageSpeed = 0;
        for(Coordinate coordinate: allCoordinate){
            averageSpeed += coordinate.getSpeed();
        }
        if(allCoordinate.size() != 0){
            averageSpeed = averageSpeed / allCoordinate.size();
        }

        return averageSpeed;
    }
}
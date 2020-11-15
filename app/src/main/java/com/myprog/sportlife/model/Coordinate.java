package com.myprog.sportlife.model;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int id;
    private double speed;
    private double latitude;
    private double longitude;

    public Coordinate(){}

    public Coordinate(int id, double speed, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.speed = speed;
        this.longitude = longitude;
    }

    public int getId(){
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSpeed() {
        return speed;
    }
}

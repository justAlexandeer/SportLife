package com.myprog.sportlife.model;

import java.util.Date;

public class Training {
    private Date dateStart;
    private Date dateEnd;
    private int id;
    private int countStep;
    private double averageSpeed;
    private double totalDistance;
    private double calories;
    private double rating;
    private String totalTime;

    public Training(){
    }

    public Training(Date dateStart, int idTraining){
        this.dateStart = dateStart;
        this.dateEnd = null;
        this.id = idTraining;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public int getId() {
        return id;
    }

    public int getCountStep() {
        return countStep;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public double getTotalDistance(){
        return totalDistance;
    }

    public double getCalories() {
        return  calories;
    }

    public String getTotalTime(){
        return totalTime;
    }

    public double getRating(){return rating;}
}

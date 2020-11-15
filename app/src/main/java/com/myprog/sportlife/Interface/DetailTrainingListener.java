package com.myprog.sportlife.Interface;

import com.myprog.sportlife.model.Coordinate;
import com.myprog.sportlife.model.Training;

import java.util.ArrayList;

public interface DetailTrainingListener {
    void onChanged(Training training, ArrayList<Coordinate> arrayList);
}

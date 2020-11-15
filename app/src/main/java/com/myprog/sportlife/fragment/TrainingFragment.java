package com.myprog.sportlife.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.myprog.sportlife.Interface.OnBackPressedListener;
import com.myprog.sportlife.R;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.StopWatch;
import com.myprog.sportlife.model.Training;
import com.myprog.sportlife.model.User;
import com.myprog.sportlife.service.ServiceTraining;

import java.util.Calendar;
import java.util.Date;

public class TrainingFragment extends Fragment implements OnBackPressedListener {

    private Button buttonStart;
    private View view;
    private ImageView imageButton;
    private FragmentActivity myContext;
    private Intent serviceIntent;
    private BroadcastReceiver broadcastReceiverStopWatch, broadcastReceiverService;
    private String stopWatchAction = "com.myprog.sporlifeStopWatch";
    private String serviceAction = "com.myprog.sportLife";
    private BottomNavigationView bottomNavigationView;

    private TextView textStopWatch;
    private TextView textSpeed;
    private TextView textDistance;
    private TextView textCalories;

    private double distance;

    public static Boolean isStart = false;

    private double calories = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_training, container, false);
        init();

        return view;
    }

    private void init(){
        buttonStart = view.findViewById(R.id.fragmentTraining_Start);

        textStopWatch = view.findViewById(R.id.fragmentTraining_StopWatch);
        textSpeed = view.findViewById(R.id.fragmentTraining_Speed);
        textDistance = view.findViewById(R.id.fragmentTraining_Distance);
        textCalories = view.findViewById(R.id.fragmentTraining_Calories);

        bottomNavigationView = myContext.findViewById(R.id.main_BottomNavigation);

        buttonStart.setText(R.string.trainingFragmentStart);
        textSpeed.setText("0.00 км ч");
        textDistance.setText("0.00 км");
        textCalories.setText("0.00 кКал");
        textStopWatch.setText("0:00:00");

        imageButton = myContext.findViewById(R.id.main_BottomNavigationImage);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart) {
                    startTrainingService();
                } else {
                    getRating();
                }
            }
        });

        //Приемник на обновление секундомера
        broadcastReceiverStopWatch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(StopWatch.isStart) {
                    int hours = intent.getExtras().getInt("hours");
                    int minutes = intent.getExtras().getInt("minutes");
                    int seconds = intent.getExtras().getInt("seconds");

                    String time = String.format("%d:%02d:%02d", hours, minutes, seconds);
                    textStopWatch.setText(time);
                } else {
                    textStopWatch.setText("0:00:00");
                }
            }
        };

        IntentFilter intentFilterStopWatch = new IntentFilter(stopWatchAction);
        myContext.registerReceiver(broadcastReceiverStopWatch, intentFilterStopWatch);

        //Приемник с сервис
        broadcastReceiverService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double speed = intent.getExtras().getDouble("speed");
                distance = intent.getExtras().getDouble("distance");
                calories = intent.getExtras().getDouble("calories");
                String tmpString = String.valueOf(speed).substring(0,4) + " км ч";
                textSpeed.setText(tmpString);
                tmpString = String.valueOf(distance).substring(0,4) + " км";
                textDistance.setText(tmpString);
                tmpString = String.valueOf(calories).substring(0,4) + " кКал";
                textCalories.setText(tmpString);
            }
        };

        IntentFilter intentFilterService = new IntentFilter(serviceAction);
        myContext.registerReceiver(broadcastReceiverService, intentFilterService);



    }

    public void startTrainingService() {
        isStart = true;
        buttonStart.setText(R.string.trainingFragmentStop);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        serviceIntent = new Intent(myContext, ServiceTraining.class);
        ContextCompat.startForegroundService(myContext, serviceIntent);
        User.countTraining++;
        DataManager.updateInDBCountTraining();
        Training training = new Training(getDateNow(), User.countTraining);
        DataManager.setTraining(training);
    }

    public void getRating(){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(myContext);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.rating_bar,  null);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_indicator);
        ratingBar.setRating(2.5f);
        materialAlertDialogBuilder.setTitle(R.string.trainingFragmentAlertDialogRatingTitle)
                .setPositiveButton(R.string.trainingFragmentAlertDialogRatingTrue,
                        (dialog, which) -> {
                            float rating = ratingBar.getRating();
                            stopTrainingService(rating);})
                .setView(view)
                .show();
    }

    public void stopTrainingService(float rating) {
        isStart = false;
        bottomNavigationView.setVisibility(View.VISIBLE);
        buttonStart.setText(R.string.trainingFragmentStart);
        textSpeed.setText("0.00 км ч");
        textDistance.setText("0.00 км");
        textCalories.setText("0.00 кКал");
        textStopWatch.setText("0:00:00");
        imageButton.setVisibility(View.VISIBLE);
        DataManager.setTrainingEnd(getDateNow());
        DataManager.setTotalDistance(distance*1000);
        DataManager.setAverageSpeedIntDB();
        DataManager.setCalories(calories);
        DataManager.setRating(rating);
        myContext.stopService(serviceIntent);
    }

    public Date getDateNow(){
        Date dateNow = Calendar.getInstance().getTime();
        return dateNow;
    }

    @Override
    public void onBackPressed(){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(myContext);
        materialAlertDialogBuilder
                .setTitle(R.string.trainingFragmentAlertDialogTitle)
                .setMessage(R.string.trainingFragmentAlertDialogText)
                .setNegativeButton(R.string.trainingFragmentAlertDialogFalse, (dialog, which) ->
                        dialog.dismiss())
                .setPositiveButton(R.string.trainingFragmentAlertDialogTrue, (dialog, which) ->
                        getRating())
                .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity)context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isStart) {
            myContext.stopService(serviceIntent);
            stopTrainingService(2.5f);
        }
    }
}

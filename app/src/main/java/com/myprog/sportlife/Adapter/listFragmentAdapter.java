package com.myprog.sportlife.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myprog.sportlife.R;
import com.myprog.sportlife.model.Training;
import com.myprog.sportlife.view.DetailTrainingActivity;

import java.util.ArrayList;

public class listFragmentAdapter extends RecyclerView.Adapter<listFragmentAdapter.MyViewHolder>{

    ArrayList<Training> allTraining;
    Context myContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDateStart;
        TextView textViewTotalTime;
        TextView textViewTotalDistance;
        TextView textViewSteps;
        TextView textViewRaitingTraining;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewDateStart = itemView.findViewById(R.id.trainingCell_DateStart);
            textViewTotalTime = itemView.findViewById(R.id.trainingCell_TotalTime);
            textViewTotalDistance = itemView.findViewById(R.id.trainingCell_TotalDistance);
            textViewSteps = itemView.findViewById(R.id.trainingCell_Steps);
            textViewRaitingTraining = itemView.findViewById(R.id.trainingCell_RatingTraining);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(myContext, DetailTrainingActivity.class);
                    int idOfPosition = getAdapterPosition();
                    Training training = allTraining.get(idOfPosition);
                    int idOfTraining = training.getId();
                    intent.putExtra("TrainingId", idOfTraining);
                    myContext.startActivity(intent);
                }
            });
        }
    }

    public listFragmentAdapter(ArrayList<Training> allTraining, Context context){
        this.allTraining = allTraining;
        myContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View view = inflate.inflate(R.layout.training_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Training training = allTraining.get(position);

        String timeStart = String.valueOf(training.getDateStart());
        timeStart = timeStart.substring(0, 20);
        holder.textViewDateStart.setText(timeStart);

        String time = String.valueOf(training.getTotalTime());
        String totalTime = "Время ";
        ArrayList<String> listTime = new ArrayList<>();
        for(String s: time.split(  " ")){
            listTime.add(s);
        }
        if(!listTime.get(0).equals("0") && !listTime.get(0).equals("null")){
            totalTime += listTime.get(0) + "ч. " + listTime.get(1) + "м. " + listTime.get(2) + "c.";
        } else if(!listTime.get(1).equals("0")){
            totalTime += listTime.get(1) + "м. " + listTime.get(2) + "c.";
        } else if(!listTime.get(2).equals("0")){
            totalTime += listTime.get(2) + "с. ";
        } else {
            totalTime += " 0с.";
        }
        holder.textViewTotalTime.setText(totalTime);

        double totalDistance = training.getTotalDistance();
        if(totalDistance > 1000) {
            totalDistance /= 1000;
            String totalDistanceS = "Расстояние " + String.valueOf(totalDistance).substring(0, 3) + " км";
            holder.textViewTotalDistance.setText(totalDistanceS);
        }else if(totalDistance > 0){
            totalDistance = Math.round(totalDistance);
            String totalDistanceS = "Расстояние " + totalDistance + " м";
            holder.textViewTotalDistance.setText(totalDistanceS);
        } else {
            holder.textViewTotalDistance.setText("Расстояние 0");
        }

        String steps = "Шаги " + training.getCountStep();
        holder.textViewSteps.setText(steps);

        String ratingTraining = String.valueOf(training.getRating());
        if(ratingTraining.charAt(2) == '0') {
            ratingTraining = ratingTraining.substring(0,1) + " / 5";
        }
        else{
            ratingTraining = ratingTraining + " / 5";
        }
        holder.textViewRaitingTraining.setText(ratingTraining);
    }

    @Override
    public int getItemCount() {
        return allTraining.size();
    }
}

package com.myprog.sportlife.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.myprog.sportlife.CustomView.SunburstChart;
import com.myprog.sportlife.Interface.DetailActivityListener;
import com.myprog.sportlife.R;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.Training;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailActivityFragment extends Fragment implements DetailActivityListener {
    private View view;
    private TextView textView;
    private TextView textViewTime;
    private TextView textViewKal;
    private TextView textViewStep;
    private TextView textViewCountTraining, textViewAverageStar, textViewNoTraining;
    private LineChart lineChart;
    private Calendar calendar, calendarChar;
    Context context;
    private int position, days, count;
    private float countAverageStar = 0;
    private int countTraining = 0, countNoTraining = 0;
    private ArrayList<Training> trainingList;
    SunburstChart sunburstChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_activity, container, false);
        return view;
    }

    public void setPosition(int position){
        this.position = position;
    }

    @Override
    public void onStart(){
        super.onStart();
        init();
    }

    public void init(){
        position -= 100;
        textView = view.findViewById(R.id.fragmentDetailActivity_Text);
        textViewTime = view.findViewById(R.id.fragmentDetailActivity_Time);
        textViewKal = view.findViewById(R.id.fragmentDetailActivity_Kal);
        textViewCountTraining = view.findViewById(R.id.fragmentDetailActivity_CountTraining);
        textViewAverageStar = view.findViewById(R.id.fragmentDetailActivity_AverageStar);
        textViewNoTraining = view.findViewById(R.id.fragmentDetailActivity_NoTraining);
        sunburstChart = view.findViewById(R.id.fragmentDetailActivity_Canvas);
        textViewStep = view.findViewById(R.id.fragmentDetailActivity_Step);
        lineChart = view.findViewById(R.id.fragmentDetailActivity_LineChart);

        calendar = Calendar.getInstance();
        days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        count = calendar.get(Calendar.DAY_OF_YEAR);
        if(position<0){
            for(int i=0; i != position; i--){
                count = count - 7;
                if(count <= 0){
                    calendar.roll(Calendar.YEAR, false);
                    days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                    count = days + count;
                }
                calendar.set(Calendar.DAY_OF_YEAR, count);
                textView.setText(df.format(calendar.getTime()));
            }
        }
        if(position > 0){
            for(int i=0; i != position; i++){
                count = count + 7;
                if(count >= 365){
                    calendar.roll(Calendar.YEAR, true);
                    count = count - days;
                    days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                calendar.set(Calendar.DAY_OF_YEAR, count);
                textView.setText(df.format(calendar.getTime()));
            }
        }
        textView.setText(df.format(calendar.getTime()));

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date dateStart = calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, 8);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date dateEnd = calendar.getTime();
        textView.append(" - ");
        textView.append(df.format(calendar.getTime()));
        DataManager.getTrainingAtThatTime(dateStart, dateEnd);
    }

    private void updateFragment(){
        ArrayList<Entry> dataValues = new ArrayList<>();
        float arr[] = new float[7];
        calendarChar = Calendar.getInstance();
        calendarChar.set(Calendar.DAY_OF_WEEK, calendarChar.getFirstDayOfWeek());
        double allTime = 0;
        int totalKal = 0;
        int stepCounter = 0;
        int numberDayOfWeek;

        for(Training training:trainingList){
            totalKal += training.getCalories();
            stepCounter += training.getCountStep();
            String time = training.getTotalTime();

            Calendar calendarTmp = Calendar.getInstance();
            Date date = training.getDateStart();
            calendarTmp.setTime(date);
            if(calendarTmp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                numberDayOfWeek = 6;
            else
                numberDayOfWeek = calendarTmp.get(Calendar.DAY_OF_WEEK)-2;
            arr[numberDayOfWeek]+= (float)Math.floor(training.getTotalDistance());
            ArrayList<String> listTime = new ArrayList<>();
            for (String s : time.split(" ")) {
                listTime.add(s);
            }
            countTraining++;
            countAverageStar += training.getRating();
            allTime += Double.valueOf(listTime.get(0)) * 60 + Double.valueOf(listTime.get(1));
        }

        numberDayOfWeek=0;
        for(float distance: arr){
            dataValues.add(new Entry(numberDayOfWeek, arr[numberDayOfWeek]));
            if(distance == 0)
                countNoTraining++;
            numberDayOfWeek++;
        }

        String tmp = String.format(getResources().getString(R.string.detailActivityFragmentTime), (int)allTime);
        textViewTime.setText(tmp);
        tmp = String.format(getResources().getString(R.string.detailActivityFragmentKal), (int)totalKal);
        textViewKal.setText(tmp);
        tmp = String.format(getResources().getString(R.string.detailActivityFragmentStep), (int)stepCounter);
        textViewStep.setText(tmp);

        if(dataValues.size() != 0) {
            LineDataSet lineDataSet = new LineDataSet(dataValues, "dataSet1");
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet);
            LineData data = new LineData(dataSets);

            XAxis xAxis = lineChart.getXAxis();
            YAxis leftAxis = lineChart.getAxisLeft();
            YAxis rightAxis = lineChart.getAxisRight();

            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(14);
            xAxis.setValueFormatter(new MyAxisValueFormatter());

            leftAxis.setEnabled(false);
            rightAxis.setEnabled(false);

            Legend legend =  lineChart.getLegend();
            legend.setEnabled(false);

            lineChart.getDescription().setEnabled(false);
            lineChart.setData(data);
            lineChart.setExtraBottomOffset(5);
            lineChart.setTouchEnabled(false);
            lineDataSet.setValueTextSize(14);
            lineDataSet.setValueTextColor(Color.BLACK);
            lineDataSet.setLineWidth(3);
            lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            lineDataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorPrimary));
            lineDataSet.setCircleRadius(8f);

            lineChart.getData().setHighlightEnabled(false);
            lineChart.invalidate();
        }

        countAverageStar /= trainingList.size();
        String countAverageStarS = String.valueOf(countAverageStar);
        if(countAverageStarS.length() > 4){
            countAverageStarS = countAverageStarS.substring(0,4);
        }
        textViewCountTraining.setText(String.valueOf(countTraining));
        textViewAverageStar.setText(countAverageStarS);
        textViewNoTraining.setText(String.valueOf(countNoTraining));
    }

    private class MyAxisValueFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String tmp;
            switch ((int)value) {
                case(0): tmp = "Пн"; break;
                case(1): tmp = "Вт"; break;
                case(2): tmp = "Ср"; break;
                case(3): tmp = "Чт"; break;
                case(4): tmp = "Пт"; break;
                case(5): tmp = "Сб"; break;
                case(6): tmp = "Вс"; break;
                default: tmp = ""; break;
            }
            return tmp;
        }
    }

    public void setInfoToLayout(){
        sunburstChart.setTraining(trainingList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        DataManager.addDetailActivityListener(this);
    }


    @Override
    public void onChanged(ArrayList<Training> arrayList) {
        trainingList = arrayList;
        if(isVisible() && isAdded()) {
            setInfoToLayout();
            updateFragment();
        }
    }
}

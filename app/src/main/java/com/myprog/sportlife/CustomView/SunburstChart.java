package com.myprog.sportlife.CustomView;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.myprog.sportlife.R;
import com.myprog.sportlife.model.Training;

import java.util.ArrayList;

public class SunburstChart extends View {
    private Paint paintCircle;
    private Paint paintPointFirst;
    private Paint paintPointSecond;
    private Paint paintLine;

    private ValueAnimator animatorCircle1;
    private ValueAnimator animatorCircle2;
    private ValueAnimator animatorCircle3;
    private ValueAnimator animatorLine1;
    private ValueAnimator animatorLine2;
    private ValueAnimator animatorLine3;

    private Context context;

    private double angleTotalTime;
    private double angleTotalStep;
    private double angleTotalKal;

    private int measuredHeight;
    private int measuredWidth;
    private float radiusFirstCircle;
    private float radiusThirdCircle;
    private float radiusCircle;

    private ArrayList<Training> allTraining;

    private RectF oval = new RectF();

    private float cxCircle1;
    private float cyCircle1;
    private float cxCircle2;
    private float cyCircle2;
    private float cxCircle3;
    private float cyCircle3;
    private float angleLine1;
    private float angleLine2;
    private float angleLine3;

    private Canvas canvas;

    public SunburstChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Log.d("SunburstChart","Constructor");
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        measuredHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        measuredWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();

        radiusFirstCircle = measuredWidth*0.4f; //Радиус внешнего кольца, примерно 2/3 ширины экрана
        radiusThirdCircle = radiusFirstCircle*(1/3f); //Радиус внутренннего круга, 1/3 от внешнгео
        radiusCircle = (radiusFirstCircle - radiusThirdCircle) / 6;

        cxCircle1 = (float)((measuredWidth/2)+(radiusFirstCircle-radiusCircle)*Math.cos((float)Math.toRadians(-90)));
        cyCircle1 = (float)((measuredHeight/2)-(radiusFirstCircle-radiusCircle)*Math.sin((float)Math.toRadians(-90)));

        cxCircle2 = (float)((measuredWidth/2)+(radiusThirdCircle+radiusCircle*3)*Math.cos((float)Math.toRadians(-90)));
        cyCircle2 = (float)((measuredHeight/2)-(radiusThirdCircle+radiusCircle*3)*Math.sin((float)Math.toRadians(-90)));

        cxCircle3 = (float)((measuredWidth/2)+(radiusThirdCircle+radiusCircle)*Math.cos((float)Math.toRadians(90)));
        cyCircle3 = (float)((measuredHeight/2)-(radiusThirdCircle+radiusCircle)*Math.sin((float)Math.toRadians(-90)));

        super.onLayout(changed, left, top, right, bottom);
    }

    private void drawAnimation(){
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();

        // 1 Кольцо
        animatorCircle1 = ValueAnimator.ofFloat(-90.f, (float)-angleTotalTime-90);
        animatorCircle1.setDuration(2000);
        animatorCircle1.setInterpolator(accelerateDecelerateInterpolator);
        animatorCircle1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cxCircle1 = (float)((measuredWidth/2)+(radiusFirstCircle-radiusCircle)*
                        Math.cos(Math.toRadians((float)animation.getAnimatedValue())));
                cyCircle1 = (float)((measuredHeight/2)-(radiusFirstCircle-radiusCircle)*
                        Math.sin(Math.toRadians((float)animation.getAnimatedValue())));
                invalidate();
            }
        });

        // 2 кольцо
        animatorCircle2 = ValueAnimator.ofFloat(-90.f, (float)-angleTotalKal-90);
        animatorCircle2.setDuration(2000);
        animatorCircle2.setInterpolator(accelerateDecelerateInterpolator);
        animatorCircle2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cxCircle2 = (float)((measuredWidth/2)+(radiusThirdCircle+radiusCircle*3)*
                        Math.cos(Math.toRadians((float)animation.getAnimatedValue())));
                cyCircle2 = (float)((measuredHeight/2)-(radiusThirdCircle+radiusCircle*3)*
                        Math.sin(Math.toRadians((float)animation.getAnimatedValue())));
                invalidate();
            }
        });

        // 3 кольцо
        animatorCircle3 = ValueAnimator.ofFloat(-90.f, (float)-angleTotalStep-90);
        animatorCircle3.setDuration(2000);
        animatorCircle3.setInterpolator(accelerateDecelerateInterpolator);
        animatorCircle3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cxCircle3 = (float)((measuredWidth/2)+(radiusThirdCircle+radiusCircle)*
                        Math.cos(Math.toRadians((float)animation.getAnimatedValue())));
                cyCircle3 = (float)((measuredHeight/2)-(radiusThirdCircle+radiusCircle)*
                        Math.sin(Math.toRadians((float)animation.getAnimatedValue())));
                invalidate();
            }
        });

        //1 Линия
        animatorLine1 = ValueAnimator.ofFloat(0.f, (float)angleTotalTime);
        animatorLine1.setDuration(2000);
        animatorLine1.setInterpolator(accelerateDecelerateInterpolator);
        animatorLine1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angleLine1 = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        //2 Линия
        animatorLine2 = ValueAnimator.ofFloat(0.f, (float)angleTotalKal);
        animatorLine2.setDuration(2000);
        animatorLine2.setInterpolator(accelerateDecelerateInterpolator);
        animatorLine2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angleLine2 = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        //3 Линия
        animatorLine3 = ValueAnimator.ofFloat(0.f, (float)angleTotalStep);
        animatorLine3.setDuration(2000);
        animatorLine3.setInterpolator(accelerateDecelerateInterpolator);
        animatorLine3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angleLine3 = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorCircle1).with(animatorCircle2).with(animatorCircle3)
                .with(animatorLine1).with(animatorLine2).with(animatorLine3);
        animatorSet.start();
    }


    private void init(){
        paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(ContextCompat.getColor(context, R.color.gray));
        paintCircle.setAntiAlias(true);

        paintPointFirst = new Paint();
        paintPointFirst.setStyle(Paint.Style.STROKE);
        paintPointFirst.setAntiAlias(true);

        paintPointSecond = new Paint();
        paintPointSecond.setAntiAlias(true);

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);

        paintPointSecond.setColor(Color.WHITE);
        paintPointSecond.setAntiAlias(true);

        Log.d("SunburstChart","init");
    }

    public void setTraining(ArrayList<Training> allTraining){
        this.allTraining = allTraining;
        updateChar();
    }

    private void updateChar(){
        int totalKal = 0;
        int stepCounter = 0;
        double allTime = 0;
        for(Training training:allTraining){
            totalKal+= training.getCalories();
            stepCounter += training.getCountStep();
            String time = training.getTotalTime();
            ArrayList<String> listTime = new ArrayList<>();
            for(String s: time.split(  " ")){
                listTime.add(s);
            }
            allTime += Double.valueOf(listTime.get(0)) * 60 + Double.valueOf(listTime.get(1));
        }

        // Калории 100% = 800 угол 100% = 360
        angleTotalKal = (totalKal / 8.0) * 3.6;
        Log.d("SunburstChart", String.valueOf(angleTotalKal));

        //Шаги 100% = 5000
        angleTotalStep = (stepCounter / 50.0) * 3.6;
        Log.d("SunburstChart", String.valueOf(angleTotalStep));

        //Время тренировко = 160 мин
        angleTotalTime =  (allTime / 1.6) * 3.6;
        Log.d("SunburstChart", String.valueOf(angleTotalTime));

        drawAnimation();
    }


    @Override
    protected void onDraw(Canvas canvas){
        Log.d("SunburstChart","onDraw");
        this.canvas = canvas;

        paintCircle.setStrokeWidth(radiusCircle);

        canvas.drawCircle(measuredWidth/2,measuredHeight/2,radiusFirstCircle-radiusCircle,paintCircle);
        canvas.drawCircle(measuredWidth/2,measuredHeight/2,radiusThirdCircle+radiusCircle*3,paintCircle);
        canvas.drawCircle(measuredWidth/2,measuredHeight/2,radiusThirdCircle+radiusCircle,paintCircle);

        //3 кольцо
        paintLine.setStrokeWidth(radiusCircle);
        paintLine.setColor(ContextCompat.getColor(context, R.color.red));
        float radius = radiusThirdCircle+radiusCircle;
        float left = measuredWidth/2 - radius;
        float top = measuredHeight/2 - radius;
        float right = measuredWidth/2 + radius;
        float bottom = measuredHeight/2 + radius;
        oval.set(left, top, right, bottom);
        canvas.drawArc(oval, 90, angleLine3,false, paintLine);

        //2 кольцо
        paintLine.setColor(ContextCompat.getColor(context, R.color.yellow));
        radius = radiusThirdCircle+radiusCircle*3;
        left = measuredWidth/2 - radius;
        top = measuredHeight/2 - radius;
        right = measuredWidth/2 + radius;
        bottom = measuredHeight/2 + radius;
        oval.set(left, top, right, bottom);
        canvas.drawArc(oval, 90, angleLine2,false,paintLine);

        //1 кольцо
        paintLine.setColor(ContextCompat.getColor(context, R.color.blue));
        radius = radiusFirstCircle-radiusCircle;
        left = measuredWidth/2 - radius;
        top = measuredHeight/2 - radius;
        right = measuredWidth/2 + radius;
        bottom = measuredHeight/2 + radius;
        oval.set(left, top, right, bottom);
        canvas.drawArc(oval, 90, angleLine1,false,paintLine);

        //Точка 1 кольца
        paintPointFirst.setStrokeWidth(radiusCircle/2);
        paintPointFirst.setColor(ContextCompat.getColor(context, R.color.blue));
        canvas.drawCircle(cxCircle1, cyCircle1, radiusCircle/2, paintPointFirst);
        canvas.drawCircle(cxCircle1, cyCircle1, radiusCircle/2, paintPointSecond);

        //Точка 2 кольца
        paintPointFirst.setColor(ContextCompat.getColor(context, R.color.yellow));
        canvas.drawCircle(cxCircle2, cyCircle2, radiusCircle/2, paintPointFirst);
        canvas.drawCircle(cxCircle2, cyCircle2, radiusCircle/2, paintPointSecond);

        //Точка 3 кольца
        paintPointFirst.setColor(ContextCompat.getColor(context, R.color.red));
        canvas.drawCircle(cxCircle3, cyCircle3, radiusCircle/2, paintPointFirst);
        canvas.drawCircle(cxCircle3, cyCircle3, radiusCircle/2, paintPointSecond);

    }

}

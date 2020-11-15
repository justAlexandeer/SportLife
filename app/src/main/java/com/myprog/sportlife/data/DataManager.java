package com.myprog.sportlife.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myprog.sportlife.Interface.DetailActivityListener;
import com.myprog.sportlife.Interface.DetailTrainingListener;
import com.myprog.sportlife.Interface.ListFragmentListener;
import com.myprog.sportlife.model.Coordinate;
import com.myprog.sportlife.model.SpeedAverage;
import com.myprog.sportlife.model.Training;
import com.myprog.sportlife.model.User;

import java.util.ArrayList;
import java.util.Date;



public class DataManager {
    public static FirebaseFirestore db;
    public static String userID;
    public static final String COLLECTION_USER = "User";
    public static final String COLLECTION_TRAINING = "Training";
    public static final String COLLECTION_COORDINATE = "Coordinate";
    private static ListFragmentListener listenerListFragment;
    private static DetailTrainingListener listenerDetailTraining;
    private static DetailActivityListener listenerDetailActivity;

    public static void addUserAfterRegistration(User user){
        if(db == null){
            createInstance();
        }
        db.collection(COLLECTION_USER)
                .document(User.id)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Log.d("Test", "Добавлен новый пользователь в бд, после регистрации");
                    }
                });
    }

    public static void setTraining(Training training){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .set(training);
    }

    public static void setTrainingEnd(Date date){
        if(db == null || userID == null){
            createInstance();
        }

        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .update("dateEnd", date);

        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                        Training training = (documentSnapshot.toObject(Training.class));

                        long dif = training.getDateEnd().getTime() - training.getDateStart().getTime();
                        long tmp;
                        String totalTime;

                        tmp = dif / (1000 * 60 * 60);
                        totalTime = tmp + " ";
                        dif = dif % (1000 * 60 * 60);

                        tmp = dif / (1000 * 60);
                        totalTime += tmp + " ";
                        dif = dif % (1000 * 60);

                        tmp = dif / 1000;
                        totalTime += tmp + " ";

                        db.collection(COLLECTION_USER).document(userID)
                                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                                .update("totalTime", totalTime);
                    }
                });
    }

    public static void setCalories(double calories) {
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .update("calories", calories);
    }

    public static void setRating(double rating) {
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .update("rating", rating);
    }

    public static void setAverageSpeedIntDB(){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .collection(COLLECTION_COORDINATE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Coordinate> allCoordinate = new ArrayList<>();
                        for(QueryDocumentSnapshot document: task.getResult()){
                            Coordinate coordinate = document.toObject(Coordinate.class);
                            allCoordinate.add(coordinate);
                        }
                        SpeedAverage speedAverage = new SpeedAverage(allCoordinate);
                        Double averageSpeed = speedAverage.start();

                        db.collection(COLLECTION_USER).document(userID)
                                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                                .update("averageSpeed", averageSpeed);
                    }
                });
    }


    public static void setTotalDistance(double totalDistance){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .update("totalDistance", totalDistance);
    }

    public static void updateInDBCountTraining(){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .update("countTraining", User.countTraining);
    }


    public static void updateInDBCountSteps(int count){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .update("countStep", count);
    }

    public static void addCoordinatesToTraining(Coordinate coordinate){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(User.countTraining))
                .collection(COLLECTION_COORDINATE).document()
                .set(coordinate);
    }

    public static void getAboutUserTraining(){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot){
                    User user = (documentSnapshot.toObject(User.class));
                    if(user != null) {
                        User.countTraining = user.getCountTraining();
                    }
                }
            });

    }

    public static void getListTraining() {
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING)
                .orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        ArrayList<Training> listTraining = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Training trainingNow = document.toObject(Training.class);
                            listTraining.add(trainingNow);
                        }
                        listenerListFragment.onChanged(listTraining);
                    }
                });
    }

    public static void getTrainingAndListCoordinate(final int id){
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING).document(String.valueOf(id))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                        final Training training;
                        training = (documentSnapshot.toObject(Training.class));

                        db.collection(COLLECTION_USER).document(userID)
                                .collection(COLLECTION_TRAINING).document(String.valueOf(id))
                                .collection(COLLECTION_COORDINATE)
                                .orderBy("id", Query.Direction.ASCENDING)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        ArrayList<Coordinate> allCoordinate = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Coordinate coordinate = document.toObject(Coordinate.class);
                                            allCoordinate.add(coordinate);
                                        }
                                        listenerDetailTraining.onChanged(training, allCoordinate);
                                    }
                                });
                    }
                });

    }

    public static void getTrainingAtThatTime(Date dateStart, Date dateEnd){
        if(db == null || userID == null){
            createInstance();
        }
        db.collection(COLLECTION_USER).document(userID)
                .collection(COLLECTION_TRAINING)
                .whereGreaterThanOrEqualTo("dateStart", dateStart)
                .whereLessThanOrEqualTo("dateStart", dateEnd)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Training> allTraining = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Training trainig = document.toObject(Training.class);
                            allTraining.add(trainig);
                        }
                        listenerDetailActivity.onChanged(allTraining);
                    }
                });
    }

    public static void addDetailActivityListener(DetailActivityListener listener){
        DataManager.listenerDetailActivity = listener;
    }

    public static void addFragmentListListener(ListFragmentListener listener){
        DataManager.listenerListFragment = listener;
    }

    public static void addDetailTrainingListener(DetailTrainingListener listener){
        DataManager.listenerDetailTraining = listener;
    }

    public static void createInstance(){
        DataManager.db = FirebaseFirestore.getInstance();
        DataManager.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

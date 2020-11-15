package com.myprog.sportlife.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myprog.sportlife.Interface.OnBackPressedListener;
import com.myprog.sportlife.R;
import com.myprog.sportlife.fragment.*;
import com.myprog.sportlife.data.DataManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataManager.getAboutUserTraining();

        bottomNavigationView = findViewById(R.id.main_BottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentContainer,
               new ActivityFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.menu_activity:
                            selectedFragment = new ActivityFragment();
                            break;
                        case R.id.menu_training:
                            selectedFragment = new TrainingFragment();
                            break;
                        case R.id.menu_list:
                            selectedFragment = new ListFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentContainer,
                                selectedFragment).commit();

                    return true;
                }
            };
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }
        if (backPressedListener != null && TrainingFragment.isStart) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

}

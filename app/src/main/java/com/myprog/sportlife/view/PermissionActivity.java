package com.myprog.sportlife.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myprog.sportlife.R;

public class PermissionActivity extends AppCompatActivity {

    TextView mainText;
    Button permissionButton;
    private int locationRequestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startNewActivity();
        }
        init();
    }

    private void init(){
        mainText = findViewById(R.id.permissionMainText);
        permissionButton = findViewById(R.id.permissionButton);

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePermission();
            }
        });
    }

    public void startNewActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void takePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                locationRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                   startNewActivity();
                else {
                    Toast toast = Toast.makeText(PermissionActivity.this, "Вы не дали разрешения, повторите попытку", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, +800);
                    toast.show();
                }
            }
            break;
        }
    }
}

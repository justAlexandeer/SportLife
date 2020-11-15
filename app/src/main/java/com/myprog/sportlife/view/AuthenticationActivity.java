package com.myprog.sportlife.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myprog.sportlife.R;
import com.myprog.sportlife.data.DataManager;
import com.myprog.sportlife.model.User;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonStart;
    private Button buttonRegister;
    private String email;
    private String password;
    private Intent intent;


    // Исправить логин и праоль чтобы вводились посмотреть правила ввода в гугл
    // Добавить исключения для регистрации и ввода(если пользователь уже зарегистрирован)
    // Ну и в целом сделать нормальную активити
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        init();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(intent);
            finish();
        }
    }

    public void init() {
        editTextEmail = findViewById(R.id.authenticationEmailEditText);
        editTextPassword = findViewById(R.id.authenticationPasswordEditText);
        buttonRegister = findViewById(R.id.authenticationRegisterButton);
        buttonStart = findViewById(R.id.authenticationStartButton);
        intent = new Intent(this, PermissionActivity.class);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText() != null && editTextPassword.getText() != null) {
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    startUser(email, password);
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText() != null && editTextPassword.getText() != null) {
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    registerUser(email, password);
                }
            }
        });

    }

    public void startUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(AuthenticationActivity.this, "Не правильно указанные данные", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registerUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User(id, email, password);
                            DataManager.addUserAfterRegistration(user);
                            Toast.makeText(AuthenticationActivity.this, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthenticationActivity.this, "Пользователь с таким Email уже существует", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}

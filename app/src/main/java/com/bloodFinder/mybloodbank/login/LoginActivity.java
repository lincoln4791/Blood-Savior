package com.bloodFinder.mybloodbank.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.userRegistration.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView noAccountSignUp;
    private EditText et_email,et_password;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_loginActivity_EmailField);
        et_password = findViewById(R.id.et_loginActivity_passwordField);
        btnLogin = findViewById(R.id.btnLogin_LoginActivity);
        noAccountSignUp = findViewById(R.id.tv_noAccountSignUp_LoginActivity);

        noAccountSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,getString(R.string.loginSuccessfull), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,getString(R.string.loginFailedwithaargument,task.getException()), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
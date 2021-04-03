package com.bloodFinder.bloodSavior.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.bloodSavior.common.MyLoadingProgress;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.mainActivity.MainActivity;
import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.resetPassword.ResetPassword;
import com.bloodFinder.bloodSavior.signUp.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView noAccountSignUp,tv_forgerPassword;
    private EditText et_email ,et_password;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        getSupportActionBar().hide();
        et_email = findViewById(R.id.et_loginActivity_EmailField);
        et_password = findViewById(R.id.et_loginActivity_passwordField);
        btnLogin = findViewById(R.id.btnLogin_LoginActivity);
        noAccountSignUp = findViewById(R.id.tv_noAccountSignUp_LoginActivity);
        tv_forgerPassword = findViewById(R.id.tv_forgetPassword_LoginActivity);

        noAccountSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, Register.class)));

        btnLogin.setOnClickListener(v -> login());

        tv_forgerPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
            startActivity(intent);
        });
    }

    private void login() {

        if(Util.internetAvailable(LoginActivity.this)){
            if(et_email.getText().toString().equals("") || et_password.getText().toString().equals("")){
                Toast.makeText(this, getString(R.string.fillUpEmailAndPasswordCorrectly), Toast.LENGTH_SHORT).show();
            }
            else{
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                MyLoadingProgress myLoadingProgress = new MyLoadingProgress(LoginActivity.this);
                myLoadingProgress.startLoadingProgress();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            myLoadingProgress.dismissAlertDialogue();
                            //Toast.makeText(LoginActivity.this,getString(R.string.loginSuccessfull), Toast.LENGTH_SHORT).show();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    Util.updateToken(LoginActivity.this,instanceIdResult.getToken());
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            });
                        }
                        else{
                            myLoadingProgress.dismissAlertDialogue();
                            Toast.makeText(LoginActivity.this,getString(R.string.loginFailedwithaargument,task.getException()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
            Util.noInternetConnection(LoginActivity.this);
        }





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }
}
package com.bloodFinder.mybloodbank.resetPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private TextView tv_resetPasswordInstruction;
    private EditText et_email;
    private Button btn_reset,btn_close;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tv_resetPasswordInstruction = findViewById(R.id.tv_resetSent_resetPasswordActivity);
        et_email = findViewById(R.id.et_email_resetPassword);
        btn_reset = findViewById(R.id.btn_reset_resetPassword);
        btn_close = findViewById(R.id.close_resetActivity);

        mAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(v -> {
            resetPassword();
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void resetPassword() {
        String email = et_email.getText().toString().trim();
        if(email.equals("")){
            et_email.setError(getString(R.string.hint_enter_email));
        }

        else{
            btn_reset.setEnabled(false);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        btn_reset.setEnabled(true);
                        btn_reset.setVisibility(View.GONE);
                        et_email.setVisibility(View.GONE);
                        btn_close.setVisibility(View.VISIBLE);
                        tv_resetPasswordInstruction.setVisibility(View.VISIBLE);
                    }
                    else{
                        btn_reset.setEnabled(true);
                        Toast.makeText(ResetPassword.this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
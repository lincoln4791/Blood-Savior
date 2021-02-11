package com.bloodFinder.mybloodbank.userRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative;
    private EditText etName,etPhoneNumber,etEmail,et_password,et_confirmPassword,et_district,et_area,et_age;
    private String username,bloodGroup,userPhoneNumber,userEmail,userPassword,confirmPassword,gender,district,area,age;
    private TextView haveAccountLogIn;
    private View customProgressBarFull;
    MyLoadingProgress myLoadingProgress;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.et_name_registrationActivity);
        etPhoneNumber = findViewById(R.id.et_phoneNumber_registrationActivity);
        etEmail = findViewById(R.id.et_email_registrationActivity);
        et_password = findViewById(R.id.et_password_registrationActivity);
        et_confirmPassword = findViewById(R.id.et_confirmPassword_registrationActivity);
        customProgressBarFull = findViewById(R.id.customProgressBarFull_RegisterActivity);
        et_area = findViewById(R.id.et_area_registrationActivity);
        et_district = findViewById(R.id.et_district_registrationActivity);
        et_age = findViewById(R.id.et_age_registrationActivity);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btnRegister = findViewById(R.id.btnRegister_RegistrationActivity);
        haveAccountLogIn = findViewById(R.id.already_have_account_Sign_in_RegisterActivity);

        bloodGroup = getSelectedBloodGroup();
        gender = getGender();

        haveAccountLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLoadingProgress = new MyLoadingProgress(Register.this);
                myLoadingProgress.startLoadingProgress();
                createUserWithEmailAndPassword();
            }
        });

    }

    private void createUserWithEmailAndPassword() {
        username = etName.getText().toString();
        userPhoneNumber = etPhoneNumber.getText().toString();
        userEmail = etEmail.getText().toString();
        userPassword = et_password.getText().toString();
        confirmPassword = et_confirmPassword.getText().toString();
        userPhoneNumber = etPhoneNumber.getText().toString();
        district = et_district.getText().toString();
        age = et_age.getText().toString();
        area = et_area.getText().toString();


         if(bloodGroup == null){
            Toast.makeText(this, getString(R.string.pleaseSelectBloodGroup), Toast.LENGTH_SHORT).show();
        }

        else if(gender == null){
            Toast.makeText(this, getString(R.string.genderr), Toast.LENGTH_SHORT).show();
        }

         else if(username.equals("")){
             etName.setError(getString(R.string.enter_your_name));
         }

         else if(age.equals("")){
             et_age.setError(getString(R.string.enter_your_age));
         }

         else if(district.equals("")){
             et_district.setError(getString(R.string.enter_your_district));
         }

         else if(area.equals("")){
             et_area.setError(getString(R.string.enter_your_area));
         }

         else if(userPhoneNumber.equals("")){
             etName.setError(getString(R.string.enter_your_phoneNumber));
         }

         else if(userEmail.equals("")){
             etEmail.setError(getString(R.string.enter_your_email));
         }

         else if(userPassword.equals("")){
             et_password.setError(getString(R.string.enter_your_password));
         }

         else if(confirmPassword.equals("")){
             et_confirmPassword.setError(getString(R.string.ConfirmPassword));
         }

         else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
             etEmail.setError(getString(R.string.incorrectEmailFOrmat));
         }

         else if(!userPassword.equals(confirmPassword)){
             et_confirmPassword.setError(getString(R.string.passwordDidntMatch));
         }


        else{
            mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String myUID = task.getResult().getUser().getUid();
                        Map userDetailsMap = new HashMap();
                        userDetailsMap.put(NodeNames.USER_NAME,username);
                        userDetailsMap.put(NodeNames.GENDER,gender);
                        userDetailsMap.put(NodeNames.AGE,age);
                        userDetailsMap.put(NodeNames.DISTRICT,district);
                        userDetailsMap.put(NodeNames.AREA,area);
                        userDetailsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                        userDetailsMap.put(NodeNames.PHONE_NUMBER,userPhoneNumber);
                        userDetailsMap.put(NodeNames.EMAIL,userEmail);
                        userDetailsMap.put(NodeNames.TIMESTAMP, ServerValue.TIMESTAMP);
                        userDetailsMap.put(NodeNames.USER_ID, myUID);
                        mRootRef.child(NodeNames.USERS).child(myUID).setValue(userDetailsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    myLoadingProgress.dismissAlertDialogue();
                                    Toast.makeText(Register.this, getString(R.string.signupSuccessful), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                }

                                else{
                                    myLoadingProgress.dismissAlertDialogue();
                                    Toast.makeText(Register.this, R.string.profileUpdateFailed, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                    else{
                        myLoadingProgress.dismissAlertDialogue();
                        Toast.makeText(Register.this, "Failed "+task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d("tag","Failed "+task.getException());
                        customProgressBarFull.setVisibility(View.GONE);
                    }

                }
            });
        }



    }








    private String getSelectedBloodGroup() {
        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_RegisterActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_RegisterActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_RegisterActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_RegisterActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_RegisterActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_RegisterActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_RegisterActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_RegisterActivity);

        CompoundButton.OnCheckedChangeListener checkedChangeListenerBloodGroup = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bloodGroup = buttonView.getText().toString();
                    Log.d("Tag", "blood Group Is : " + getSelectedBloodGroup());
                }
                else{
                    bloodGroup = null;
                }
            }
        };

        chip_APositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ANegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_BPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_BNegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_OPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ONegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ABPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ABNegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        return bloodGroup;
    }





    private String getGender() {
        Chip chip_male, chip_female;
        chip_female = findViewById(R.id.chip_gender_female_RegistrationActivity);
        chip_male = findViewById(R.id.chip_gender_male_RegistrationActivity);

        CompoundButton.OnCheckedChangeListener checkedChangeListenerGender = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gender = buttonView.getText().toString();
                    //Log.d("Tag", "Gender Is : " +gender);
                }
                else{
                    gender = null;
                }
            }
        };

        chip_male.setOnCheckedChangeListener(checkedChangeListenerGender);
        chip_female.setOnCheckedChangeListener(checkedChangeListenerGender);

        return gender;
    }
}
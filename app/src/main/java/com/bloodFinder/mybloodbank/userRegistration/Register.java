package com.bloodFinder.mybloodbank.userRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bloodFinder.mybloodbank.selectCountry.SelectCountry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative;
    private Chip chip_male, chip_female;
    private EditText etName,etPhoneNumber,etEmail,et_password,et_confirmPassword,et_area,et_age;
    private String username,bloodGroup,userPhoneNumber,userEmail,userPassword,confirmPassword,gender,district,area,age,userOrder="";
    private TextView haveAccountLogIn, tv_district;
    private View customProgressBarFull;
    private ArrayList<String > bloodList,genderList;
    private MyLoadingProgress myLoadingProgress;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private final int RC_SELECT_COUNTRY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        etName = findViewById(R.id.et_name_registrationActivity);
        etPhoneNumber = findViewById(R.id.et_phoneNumber_registrationActivity);
        etEmail = findViewById(R.id.et_email_registrationActivity);
        et_password = findViewById(R.id.et_password_registrationActivity);
        et_confirmPassword = findViewById(R.id.et_confirmPassword_registrationActivity);
        customProgressBarFull = findViewById(R.id.customProgressBarFull_RegisterActivity);
        et_area = findViewById(R.id.et_area_registrationActivity);
        tv_district = findViewById(R.id.tv_district_registrationActivity);
        et_age = findViewById(R.id.et_age_registrationActivity);
        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_RegisterActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_RegisterActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_RegisterActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_RegisterActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_RegisterActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_RegisterActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_RegisterActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_RegisterActivity);
        chip_female = findViewById(R.id.chip_gender_female_RegistrationActivity);
        chip_male = findViewById(R.id.chip_gender_male_RegistrationActivity);
        bloodList = new ArrayList<>();
        genderList = new ArrayList<>();

        btnRegister = findViewById(R.id.btnRegister_RegistrationActivity);
        haveAccountLogIn = findViewById(R.id.already_have_account_Sign_in_RegisterActivity);

        getSelectedBloodGroup();
        getGender();
        getUserOrder();

        haveAccountLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserWithEmailAndPassword();
            }
        });

        tv_district.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCountry.class);
            startActivityForResult(intent,RC_SELECT_COUNTRY);
        });

    }

    private void createUserWithEmailAndPassword() {

        getSelectedBloodGroup();

        if(bloodList.isEmpty()){
            Toast.makeText(this, getString(R.string.chooseBloodGroup), Toast.LENGTH_SHORT).show();
        }
        else{
            bloodGroup = bloodList.get(0);

            if(genderList.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_your_gender), Toast.LENGTH_SHORT).show();
            }
            else{
                gender=genderList.get(0);

                username = etName.getText().toString();
                userPhoneNumber = etPhoneNumber.getText().toString();
                userEmail = etEmail.getText().toString();
                userPassword = et_password.getText().toString();
                confirmPassword = et_confirmPassword.getText().toString();
                userPhoneNumber = etPhoneNumber.getText().toString();
                district = tv_district.getText().toString();
                age = et_age.getText().toString();
                area = et_area.getText().toString();

                if(username.equals("")){
                    etName.setError(getString(R.string.enter_your_name));
                }

                else if(age.equals("")){
                    et_age.setError(getString(R.string.enter_your_age));
                }

                else if(district.equals("")){
                    tv_district.setError(getString(R.string.enter_your_district));
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

                else if(userOrder==null){
                    Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                }


                else{
                    myLoadingProgress = new MyLoadingProgress(Register.this);
                    myLoadingProgress.startLoadingProgress();
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
                                userDetailsMap.put(NodeNames.USER_ORDER, userOrder);
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

        }

    }








    private void getSelectedBloodGroup() {

        CompoundButton.OnCheckedChangeListener checkedChangeListenerBloodGroup = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bloodList.add(buttonView.getText().toString());
                }
                else{
                    bloodList.remove(buttonView.getText().toString());
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
    }





    private void getGender() {


        CompoundButton.OnCheckedChangeListener checkedChangeListenerGender = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    genderList.add(buttonView.getText().toString());
                }
                else{
                    genderList.remove(buttonView.getText().toString());
                }
            }
        };

        chip_male.setOnCheckedChangeListener(checkedChangeListenerGender);
        chip_female.setOnCheckedChangeListener(checkedChangeListenerGender);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SELECT_COUNTRY){
            if(resultCode==RESULT_OK){
                if (data != null) {
                   district = data.getStringExtra(Extras.COUNTRY_NAME);
                }
                tv_district.setText(district);
            }
        }

    }



    private void getUserOrder() {
        mRootRef.child(NodeNames.USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int order = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))+1;
                    userOrder = String.valueOf(order);

                }
                else{
                    userOrder = "1";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}
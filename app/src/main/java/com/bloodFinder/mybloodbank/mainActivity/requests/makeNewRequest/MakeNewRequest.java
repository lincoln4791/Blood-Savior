package com.bloodFinder.mybloodbank.mainActivity.requests.makeNewRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class MakeNewRequest extends AppCompatActivity {
    private ChipGroup cg_BloodSelection,cg_cause,cg_gender;
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative;
    private EditText et_district,et_area, et_postDescription;
    private CardView cv_uploadImage,cv_datePicker;
    private Spinner spinner_unitBagsRequired;
    private ToggleButton btnAdvancedSearch;
    private ConstraintLayout cl_AdvancedSearch;
    private String bloodGroup,cause,gender,district,area,postDescription,imageUri,requiredDate,requiredUnitBagsBlood;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Button btnMakeRequest;
    private TextView tv_requiredDate;
    private View customProgressBar;
    private View llProgressBar;
    private MyLoadingProgress myLoadingProgress;

    private DatabaseReference mRootRef;
    private String myUID= FirebaseAuth.getInstance().getUid();
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_request);


        cg_BloodSelection = findViewById(R.id.chipGroup_bloodGroups_makeNewRequest);
        cg_cause = findViewById(R.id.chipGroup_cause_makeNewRequestActivity);
        cg_gender = findViewById(R.id.chipGroup_gender_makeNewRequestActivity);
        et_district = findViewById(R.id.et_district_makeNewDistrictActivity);
        et_area = findViewById(R.id.et_area_makeNewDistrictActivity);
        et_postDescription = findViewById(R.id.et_postDescription_makeNewDistrictActivity);
        cv_uploadImage = findViewById(R.id.cv_uploadImage_makeNewRequestActivity);
        cv_datePicker = findViewById(R.id.cv_datePicker_makeNewRequestActivity);
        spinner_unitBagsRequired = findViewById(R.id.spinner_unitBagsRequired_makeNewRequestActivity);
        btnAdvancedSearch = findViewById(R.id.toggolBtn_advancedSearch_makeNewRequestActivity);
        cl_AdvancedSearch = findViewById(R.id.cl_advancedSearch_makeNewRequestActivity);
        btnMakeRequest = findViewById(R.id.btn_makeRequest_makeNewRequestActivity);
        tv_requiredDate=findViewById(R.id.tv_datePicker_makeNewDistrictActivity);
        customProgressBar = findViewById(R.id.customProgressBarFull_makeNewRequestActivity);
        llProgressBar = findViewById(R.id.ll_progressbar_makeNewFriendActivity);
        myLoadingProgress = new MyLoadingProgress(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bloodGroup = getSelectedBloodGroup();
        cause = getCause();
        gender = getGender();

        //Spinner Initilization
        initSpinner();

        cv_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiredDate = getDate();
            }
        });

        btnAdvancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toggolBtnText = btnAdvancedSearch.getText().toString();
                if(toggolBtnText.equals(getString(R.string.openAdvancedSearch))){
                    cl_AdvancedSearch.setVisibility(View.GONE);
                }
                else{
                    cl_AdvancedSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        btnMakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewRequest();
            }
        });





    }








    private void initSpinner() {

        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.requiredAmountOfBlood, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_unitBagsRequired.setAdapter(spinnerAdapter);

        spinner_unitBagsRequired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    requiredUnitBagsBlood = null;
                    //Log.d("tag","Blood needed : 1");
                }

                if(position==1){
                    requiredUnitBagsBlood = "1";
                    //Log.d("tag","Blood needed : 2");
                }

                if(position==2){
                    requiredUnitBagsBlood = "2";
                    //Log.d("tag","Blood needed : 3");
                }

                if(position==3){
                    requiredUnitBagsBlood = "3";
                    //Log.d("tag","Blood needed : 3");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MakeNewRequest.this, getString(R.string.pleaseSelectAMountOfBlood), Toast.LENGTH_SHORT).show();
            }
        });
    }











    private void makeNewRequest() {

        district = et_district.getText().toString();
        area = et_area.getText().toString();
        postDescription = et_postDescription.getText().toString();

        if(district.equals("")){
            et_district.setError(getString(R.string.enterDistrict));
        }

        else if(area.equals("")){
            et_area.setError(getString(R.string.enterArea));
        }

        else if(postDescription.equals("")){
            et_postDescription.setError(getString(R.string.writeSomethingHere));
        }


        else if(bloodGroup == null){
            Toast.makeText(this, getString(R.string.pleaseSelectBloodGroup), Toast.LENGTH_SHORT).show();
        }

        else if(cause == null){
            Toast.makeText(this, getString(R.string.pleaseSelectCause), Toast.LENGTH_SHORT).show();
        }

        else if(requiredDate == null){
            Toast.makeText(this, "Seletc Date", Toast.LENGTH_SHORT).show();
        }

        else if(gender == null){
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
        }

        else if(requiredUnitBagsBlood == null){
            Toast.makeText(this, getString(R.string.pleaseSelectAMountOfBlood), Toast.LENGTH_SHORT).show();
        }

        else{
            myLoadingProgress.startLoadingProgress();
            String pushID = mRootRef.push().getKey();
            String postID = pushID;

            Map postsMap = new HashMap();
            postsMap.put(NodeNames.POST_ID,postID);
            postsMap.put(NodeNames.TIMESTAMP, ServerValue.TIMESTAMP);
            postsMap.put(NodeNames.POST_CREATOR_ID,myUID);
            postsMap.put(NodeNames.POST_CREATOR_NAME,currentUser.getDisplayName());
            postsMap.put(NodeNames.POST_CREATOR_PHOTO,currentUser.getPhotoUrl());
            postsMap.put(NodeNames.GENDER,requiredUnitBagsBlood);
            postsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
            postsMap.put(NodeNames.DISTRICT,district);
            postsMap.put(NodeNames.AREA,area);
            postsMap.put(NodeNames.POST_DESCRIPTION,postDescription);

            mRootRef.child(NodeNames.POSTS).child(pushID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MakeNewRequest.this, "Post Successfull", Toast.LENGTH_SHORT).show();
                        myLoadingProgress.dismissAlertDialogue();
                        startActivity(new Intent(MakeNewRequest.this,MainActivity.class));
                    }
                    else{
                        myLoadingProgress.dismissAlertDialogue();
                        Toast.makeText(MakeNewRequest.this, "post Failed", Toast.LENGTH_SHORT).show();
                        llProgressBar.setVisibility(View.VISIBLE);
                        btnMakeRequest.setVisibility(View.VISIBLE);
                    }
                }
            });
        }








    }








    private String getDate() {
        DatePicker datePicker = new DatePicker(this);
        int currentDay = datePicker.getDayOfMonth();
        int currentMonth = (datePicker.getMonth()+1);
        int currentYear = datePicker.getYear();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        requiredDate = String.valueOf(currentDay)+"/"+String.valueOf(currentMonth)+"/"+currentYear;
                        tv_requiredDate.setText(requiredDate);
                        //Log.d("tag",requiredDate);
                    }
                },currentDay, currentMonth,currentYear
        );
        datePickerDialog.show();
        return requiredDate;
    }







    private String getGender() {
        Chip chip_male, chip_female;
        chip_female = findViewById(R.id.chip_gender_female_makeNewRequestActivity);
        chip_male = findViewById(R.id.chip_gender_male_makeNewRequestActivity);

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











    private String getCause() {
        Chip chip_cancer, chip_dengu, chip_ceasar,chip_thalasemia,chip_other,chip_delivery;
        chip_cancer = findViewById(R.id.chip_cause_cancer_makeNewRequestActivity);
        chip_dengu = findViewById(R.id.chip_cause_dengue_makeNewRequestActivity);
        chip_ceasar = findViewById(R.id.chip_cause_caesar_makeNewRequestActivity);
        chip_thalasemia = findViewById(R.id.chip_cause_thalassemia_makeNewRequestActivity);
        chip_other = findViewById(R.id.chip_cause_other_makeNewRequestActivity);
        chip_delivery = findViewById(R.id.chip_cause_delevery_makeNewRequestActivity);

        CompoundButton.OnCheckedChangeListener checkedChangeListenerCause = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cause = buttonView.getText().toString();
                    //Log.d("Tag", "cause Is : " +cause);
                }
                else{
                    cause = null;
                }
            }
        };

        chip_cancer.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_dengu.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_ceasar.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_thalasemia.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_other.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_delivery.setOnCheckedChangeListener(checkedChangeListenerCause);
        return cause;
    }












    private String getSelectedBloodGroup() {
        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_makeNewRequest);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_makeNewRequest);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_makeNewRequest);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_makeNewRequest);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_makeNewRequest);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_makeNewRequest);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_makeNewRequest);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_makeNewRequest);

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
}
package com.bloodFinder.mybloodbank.mainActivity.requests.makeNewRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakeNewRequest extends AppCompatActivity {
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative;
    private EditText et_district,et_area, et_postDescription,et_phone;
    private ImageView iv_postImage,iv_cancelImagePreview;
    private CardView cv_uploadImage,cv_datePicker;
    private Spinner spinner_unitBagsRequired;
    private ToggleButton btnAdvancedSearch;
    private ConstraintLayout cl_AdvancedSearch;
    private String bloodGroup,cause,gender,district,area,postDescription,userPhoto,requiredDate,requiredUnitBagsBlood,phone,postPhoto=""
            ,post_order= "";
    private Uri localImageUri;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Button btnMakeRequest;
    private TextView tv_requiredDate;
    private View customProgressBar;
    private View llProgressBar;
    private MyLoadingProgress myLoadingProgress;
    private ArrayList<String> bloodList,genderList,causeList;
    private Boolean postImageFlag = false;

    private DatabaseReference mRootRef;
    private StorageReference storageRootRef;
    private String myUID= FirebaseAuth.getInstance().getUid();
    private FirebaseUser currentUser;

    private static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_request);

        et_district = findViewById(R.id.et_district_makeNewRequestActivity);
        et_area = findViewById(R.id.et_area_makeNewRequestActivity);
        et_postDescription = findViewById(R.id.et_postDescription_makeNewRequestActivity);
        iv_postImage = findViewById(R.id.iv_postImagePreview_makeNewRequestActivity);
        iv_cancelImagePreview = findViewById(R.id.iv_cancelImagePreview_makeNewRequestActivity);
        et_phone = findViewById(R.id.et_phone_makeNewRequestActivity);
        cv_uploadImage = findViewById(R.id.cv_uploadImage_makeNewRequestActivity);
        cv_datePicker = findViewById(R.id.cv_datePicker_makeNewRequestActivity);
        spinner_unitBagsRequired = findViewById(R.id.spinner_unitBagsRequired_makeNewRequestActivity);
        btnAdvancedSearch = findViewById(R.id.toggolBtn_advancedSearch_makeNewRequestActivity);
        cl_AdvancedSearch = findViewById(R.id.cl_advancedSearch_makeNewRequestActivity);
        btnMakeRequest = findViewById(R.id.btn_makeRequest_makeNewRequestActivity);
        tv_requiredDate=findViewById(R.id.tv_datePicker_makeNewRequestActivity);
        customProgressBar = findViewById(R.id.customProgressBarFull_makeNewRequestActivity);
        llProgressBar = findViewById(R.id.ll_progressbar_makeNewRequestActivity);
        myLoadingProgress = new MyLoadingProgress(this);
        bloodList = new ArrayList<>();
        genderList = new ArrayList<>();
        causeList = new ArrayList<>();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        storageRootRef = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getSelectedBloodGroup();
        getCause();
        getGender();
        getPostOrder();

        //Spinner Initilization to get unit amount of blood
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


        cv_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });


        iv_cancelImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_postImage.setVisibility(View.GONE);
                iv_cancelImagePreview.setVisibility(View.GONE);
                localImageUri = null;
                postImageFlag = false;
                Glide.with(MakeNewRequest.this).load(localImageUri).into(iv_postImage);
            }
        });

    }




    private void getPostOrder() {
            mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                         int order = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))+1;
                         post_order = String.valueOf(order);

                    }
                    else{
                        post_order = "1";
                    }
                    Log.d("tag","postOrder"+post_order);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
                    requiredUnitBagsBlood = "";
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

                if(causeList.isEmpty()){
                    Toast.makeText(this, getString(R.string.pleaseSelectCause_Disease), Toast.LENGTH_SHORT).show();
                }
                else{

                    cause=causeList.get(0);
                    district = et_district.getText().toString();
                    area = et_area.getText().toString();
                    postDescription = et_postDescription.getText().toString();
                    phone =et_phone.getText().toString();


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

                    else if(requiredUnitBagsBlood.equals("")){
                        Toast.makeText(this, getString(R.string.pleaseSelectAMountOfBlood), Toast.LENGTH_SHORT).show();
                    }

                    else if(phone.equals("")){
                        et_phone.setError(getString(R.string.phoneNumber));
                    }

                    else if(post_order.equals("")){
                        Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    }

                    else if(post_order==null){
                        Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    }

                    else{
                        myLoadingProgress.startLoadingProgress();
                        //String pushID = mRootRef.push().getKey();
                        String postID = post_order;

                        if(postImageFlag){
                            postWithImage(postID);
                        }

                        else{
                            Map postsMap = new HashMap();
                            postsMap.put(NodeNames.POST_ID,postID);
                            postsMap.put(NodeNames.TIMESTAMP, ServerValue.TIMESTAMP);
                            postsMap.put(NodeNames.REQUIRED_DATE, requiredDate);
                            postsMap.put(NodeNames.POST_CREATOR_ID,myUID);
                            postsMap.put(NodeNames.POST_CREATOR_NAME,currentUser.getDisplayName());
                            postsMap.put(NodeNames.POST_CREATOR_PHOTO,currentUser.getPhotoUrl());
                            postsMap.put(NodeNames.GENDER,gender);
                            postsMap.put(NodeNames.CAUSE,cause);
                            postsMap.put(NodeNames.PHONE_NUMBER,phone);
                            postsMap.put(NodeNames.UNIT_BAGS,requiredUnitBagsBlood);
                            postsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                            postsMap.put(NodeNames.DISTRICT,district);
                            postsMap.put(NodeNames.AREA,area);
                            postsMap.put(NodeNames.POST_DESCRIPTION,postDescription);
                            postsMap.put(NodeNames.POST_PHOTO,"");
                            postsMap.put(NodeNames.POST_ORDER,post_order);


                            mRootRef.child(NodeNames.POSTS).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){

                                        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(MakeNewRequest.this, getString(R.string.postSuccessful), Toast.LENGTH_SHORT).show();
                                                    myLoadingProgress.dismissAlertDialogue();
                                                    finish();
                                                    startActivity(new Intent(MakeNewRequest.this,MainActivity.class));
                                                }
                                                else{
                                                    myLoadingProgress.dismissAlertDialogue();
                                                    Toast.makeText(MakeNewRequest.this, getString(R.string.failedToPost), Toast.LENGTH_SHORT).show();
                                                    llProgressBar.setVisibility(View.VISIBLE);
                                                    btnMakeRequest.setVisibility(View.VISIBLE);
                                                }

                                            }
                                        });


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



                }

            }
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







    private void getGender() {
        Chip chip_male, chip_female;
        chip_female = findViewById(R.id.chip_gender_female_makeNewRequestActivity);
        chip_male = findViewById(R.id.chip_gender_male_makeNewRequestActivity);

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











    private void getCause() {
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
                    causeList.add(buttonView.getText().toString());
                    //Log.d("Tag", "cause Is : " +cause);
                }
                else{
                    causeList.remove(buttonView.getText().toString());
                }
            }
        };

        chip_cancer.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_dengu.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_ceasar.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_thalasemia.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_other.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_delivery.setOnCheckedChangeListener(checkedChangeListenerCause);
        //return cause;
    }












    private void getSelectedBloodGroup() {
        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_makeNewRequestActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_makeNewRequestActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_makeNewRequestActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_makeNewRequestActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_makeNewRequestActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_makeNewRequestActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_makeNewRequestActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_makeNewRequestActivity);

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
        //return bloodGroup;
    }






    private void imagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode == RESULT_OK){
                iv_postImage.setVisibility(View.VISIBLE);
                iv_cancelImagePreview.setVisibility(View.VISIBLE);
                postImageFlag=true;
                localImageUri = data.getData();
                Glide.with(MakeNewRequest.this).load(localImageUri).placeholder(R.drawable.featuredd)
                        .error(R.drawable.featuredd).into(iv_postImage);

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 102){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_CODE);
            }
            else{
                Toast.makeText(this, getString(R.string.accessPermissionIsRequired), Toast.LENGTH_SHORT).show();
            }
        }
    }







    public  void postWithImage(String postID){
        String fileName = postID+".jpg";
        StorageReference fileRef = storageRootRef.child(NodeNames.POST_IMAGES_FOLDER).child(myUID+"/"+fileName);
        fileRef.putFile(localImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        postPhoto = uri.toString();

                        Map postsMap = new HashMap();
                        postsMap.put(NodeNames.POST_ID,postID);
                        postsMap.put(NodeNames.TIMESTAMP, ServerValue.TIMESTAMP);
                        postsMap.put(NodeNames.REQUIRED_DATE, requiredDate);
                        postsMap.put(NodeNames.POST_CREATOR_ID,myUID);
                        postsMap.put(NodeNames.POST_CREATOR_NAME,currentUser.getDisplayName());
                        postsMap.put(NodeNames.POST_CREATOR_PHOTO,currentUser.getPhotoUrl());
                        postsMap.put(NodeNames.GENDER,gender);
                        postsMap.put(NodeNames.CAUSE,cause);
                        postsMap.put(NodeNames.PHONE_NUMBER,phone);
                        postsMap.put(NodeNames.UNIT_BAGS,requiredUnitBagsBlood);
                        postsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                        postsMap.put(NodeNames.DISTRICT,district);
                        postsMap.put(NodeNames.AREA,area);
                        postsMap.put(NodeNames.POST_DESCRIPTION,postDescription);
                        postsMap.put(NodeNames.POST_PHOTO,postPhoto);
                        postsMap.put(NodeNames.POST_ORDER,post_order);


                        mRootRef.child(NodeNames.POSTS).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){

                                    mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(MakeNewRequest.this, getString(R.string.postSuccessful), Toast.LENGTH_SHORT).show();
                                                iv_postImage.setVisibility(View.GONE);
                                                iv_cancelImagePreview.setVisibility(View.GONE);
                                                myLoadingProgress.dismissAlertDialogue();
                                                finish();
                                                startActivity(new Intent(MakeNewRequest.this,MainActivity.class));
                                            }
                                            else{
                                                myLoadingProgress.dismissAlertDialogue();
                                                Toast.makeText(MakeNewRequest.this, getString(R.string.failedToPost), Toast.LENGTH_SHORT).show();
                                                llProgressBar.setVisibility(View.VISIBLE);
                                                btnMakeRequest.setVisibility(View.VISIBLE);
                                            }

                                        }
                                    });


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
                });
            }
        });
    }






}
package com.bloodFinder.mybloodbank.mainActivity.requests.editRequestPost;

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
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.UtilDB;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest.SingleRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRequest extends AppCompatActivity {
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,
            chip_ABNegative,chip_male,chip_female,chip_cancer, chip_dengu, chip_caesar,chip_thalasemia,chip_other,chip_delivery;;
    private EditText et_district,et_area, et_postDescription,et_phone;
    private ImageView iv_postImagePreview,iv_cancelPostImagePreview;
    private CardView cv_uploadImage,cv_datePicker;
    private Spinner spinner_unitBagsRequired;
    private ToggleButton btnAdvancedSearch;
    private ConstraintLayout cl_AdvancedSearch;
    private String bloodGroup,cause,gender,district,area,postDescription,imageUri,requiredDate, requiredUnitBags,phone,postCreatorID,
            postID,postCreatorName,postCreatorPhoto,postImage,postLove,postView,getPostDescription,post,accepted,donated,timeStamp,
            acceptedFlag,loveFlag,completedFlag;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Button btn_updateRequest;
    private TextView tv_requiredDate;
    private View customProgressBar;
    private View llProgressBar;
    private MyLoadingProgress myLoadingProgress;
    private ArrayList<String> bloodList,genderList,causeList;
    private Uri localImageUri = null;
    private Boolean postImageFlag = false;

    private DatabaseReference mRootRef;
    private StorageReference storageRootRef;
    private String myUID= FirebaseAuth.getInstance().getUid();
    private FirebaseUser currentUser;


    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        et_district = findViewById(R.id.et_district_editRequestActivity);
        et_area = findViewById(R.id.et_area_editRequestActivity);
        et_postDescription = findViewById(R.id.et_postDescription_editRequestActivity);
        et_phone = findViewById(R.id.et_phone_editRequestActivity);
        iv_postImagePreview = findViewById(R.id.iv_postImagePreview_editRequestActivity);
        iv_cancelPostImagePreview = findViewById(R.id.iv_cancelImagePreview_editRequestActivity);
        cv_uploadImage = findViewById(R.id.cv_uploadImage_editRequestActivity);
        cv_datePicker = findViewById(R.id.cv_datePicker_editRequestActivity);
        spinner_unitBagsRequired = findViewById(R.id.spinner_unitBagsRequired_editRequestActivity);
        btnAdvancedSearch = findViewById(R.id.toggolBtn_advancedSearch_editRequestActivity);
        cl_AdvancedSearch = findViewById(R.id.cl_advancedSearch_editRequestActivity);
        btn_updateRequest = findViewById(R.id.btn_updateRequest_editRequestActivity);
        tv_requiredDate=findViewById(R.id.tv_datePicker_editRequestActivity);
        customProgressBar = findViewById(R.id.customProgressBarFull_editRequestActivity);
        llProgressBar = findViewById(R.id.ll_progressbar_editRequestActivity);
        chip_male = findViewById(R.id.chip_gender_male_editRequestActivity);
        chip_female = findViewById(R.id.chip_gender_female_editRequestActivity);
        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_editRequestActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_editRequestActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_editRequestActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_editRequestActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_editRequestActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_editRequestActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_editRequestActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_editRequestActivity);
        chip_cancer = findViewById(R.id.chip_cause_cancer_editRequestActivity);
        chip_dengu = findViewById(R.id.chip_cause_dengue_editRequestActivity);
        chip_caesar = findViewById(R.id.chip_cause_caesar_editRequestActivity);
        chip_thalasemia = findViewById(R.id.chip_cause_thalassemia_editRequestActivity);
        chip_other = findViewById(R.id.chip_cause_other_editRequestActivity);
        chip_delivery = findViewById(R.id.chip_cause_delevery_editRequestActivity);
        myLoadingProgress = new MyLoadingProgress(this);
        bloodList = new ArrayList<>();
        genderList = new ArrayList<>();
        causeList = new ArrayList<>();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        storageRootRef = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getIntentData();
        bloodList.add(bloodGroup);
        genderList.add(gender);
        causeList.add(cause);
        setEditTextsData();
        setCheckedBloodGroup(bloodGroup);
        setCheckedGender(gender);
        setCause();
        btn_updateRequest.setVisibility(View.VISIBLE);
        getSelectedBloodGroup();
        getGender();
        getCause();

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

        btn_updateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRequest();
            }
        });



        cv_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });


        iv_cancelPostImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_postImagePreview.setVisibility(View.GONE);
                iv_cancelPostImagePreview.setVisibility(View.GONE);
                localImageUri = null;
                postImage = "";
                postImageFlag = false;
                Log.d("tag","flag"+postImageFlag);
                Glide.with(EditRequest.this).load(localImageUri).into(iv_postImagePreview);
            }
        });

    }

    private void setCause() {
        switch (cause) {
            case Constants.DENGUE:
                chip_dengu.setChecked(true);
                break;
            case Constants.CAESAR:
                chip_caesar.setChecked(true);
                break;
            case Constants.CANCER:
                chip_cancer.setChecked(true);
                break;
            case Constants.THALASSEMIA:
                chip_thalasemia.setChecked(true);
                break;
            case Constants.OTHER:
                chip_other.setChecked(true);
                break;
            default:
                Toast.makeText(this, getString(R.string.causeError), Toast.LENGTH_SHORT).show();
                break;
        }
    }







    private void getIntentData() {
        postCreatorID = getIntent().getStringExtra(Extras.POST_CREATOR_ID);
        postID = getIntent().getStringExtra(Extras.POST_ID);
        postCreatorName = getIntent().getStringExtra(Extras.POST_CREATOR_NAME);
        postCreatorPhoto = getIntent().getStringExtra(Extras.POST_CREATOR_PHOTO);
        postDescription = getIntent().getStringExtra(Extras.POST_DESCRIPTION);
        postLove = getIntent().getStringExtra(Extras.POST_LOVE);
        postView = getIntent().getStringExtra(Extras.POST_VIEW);
        bloodGroup = getIntent().getStringExtra(Extras.BLOOD_GROUP);
        area = getIntent().getStringExtra(Extras.AREA);
        district = getIntent().getStringExtra(Extras.DISTRICT);
        accepted = getIntent().getStringExtra(Extras.ACCEPTED);
        donated = getIntent().getStringExtra(Extras.DONATED);
        timeStamp = getIntent().getStringExtra(Extras.TIMESTAMP);
        cause = getIntent().getStringExtra(Extras.CAUSE);
        gender = getIntent().getStringExtra(Extras.GENDER);
        phone = getIntent().getStringExtra(Extras.PHONE_NUMBER);
        requiredUnitBags = getIntent().getStringExtra(Extras.UNIT_BAGS);
        requiredDate  = getIntent().getStringExtra(Extras.REQUIRED_DATE);
        acceptedFlag = getIntent().getStringExtra(Extras.ACCEPTED_FLAG);
        loveFlag = getIntent().getStringExtra(Extras.LOVE_FLAG);
        completedFlag = getIntent().getStringExtra(Extras.COMPLETED_FLAG);
        postImage = "";
        if(getIntent().hasExtra(Extras.POST_IMAGE)){
            if(getIntent().getStringExtra(Extras.POST_IMAGE)!=null){
                if(!getIntent().getStringExtra(Extras.POST_IMAGE).equals("")){
                    localImageUri = Uri.parse(getIntent().getStringExtra(Extras.POST_IMAGE));
                    postImageFlag = true;
                    iv_postImagePreview.setVisibility(View.VISIBLE);
                    iv_cancelPostImagePreview.setVisibility(View.VISIBLE);
                    Glide.with(EditRequest.this).load(localImageUri).placeholder(R.drawable.featuredd)
                            .error(R.drawable.featuredd).into(iv_postImagePreview);
                }

            }

        }
    }


    private void initSpinner() {

        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.requiredAmountOfBlood, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_unitBagsRequired.setAdapter(spinnerAdapter);
        if(requiredUnitBags.equals("1")){
            spinner_unitBagsRequired.setSelection(1);
        }
        else if(requiredUnitBags.equals("2")){
            spinner_unitBagsRequired.setSelection(2);
        }
        else if(requiredUnitBags.equals("3")){
            spinner_unitBagsRequired.setSelection(3);
        }
        else{
            spinner_unitBagsRequired.setSelection(4);
        }

        spinner_unitBagsRequired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    requiredUnitBags = "";
                    //Log.d("tag","Blood needed : 1");
                }

                if(position==1){
                    requiredUnitBags = "1";
                    //Log.d("tag","Blood needed : 2");
                }

                if(position==2){
                    requiredUnitBags = "2";
                    //Log.d("tag","Blood needed : 3");
                }

                if(position==3){
                    requiredUnitBags = "3";
                    //Log.d("tag","Blood needed : 3");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }











    private void updateRequest() {

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

                    else if(requiredUnitBags == null){
                        Toast.makeText(this, getString(R.string.pleaseSelectAMountOfBlood), Toast.LENGTH_SHORT).show();
                    }

                    else if(phone.equals("")){
                        et_phone.setError(getString(R.string.phoneNumber));
                    }

                    else{
                        myLoadingProgress.startLoadingProgress();

                        if(postImageFlag){
                            updateWithImage(postID);
                        }
                        else{


                            Map postsMap = new HashMap();
                            postsMap.put(NodeNames.POST_ID,postID);
                            postsMap.put(NodeNames.TIMESTAMP, ServerValue.TIMESTAMP);
                            postsMap.put(NodeNames.POST_CREATOR_ID,myUID);
                            postsMap.put(NodeNames.POST_CREATOR_NAME, UtilDB.USER_NAME);
                            postsMap.put(NodeNames.POST_CREATOR_PHOTO,UtilDB.USER_PHOTO);
                            postsMap.put(NodeNames.POST_PHOTO,postImage);
                            postsMap.put(NodeNames.GENDER,gender);
                            postsMap.put(NodeNames.CAUSE,cause);
                            postsMap.put(NodeNames.PHONE_NUMBER,phone);
                            postsMap.put(NodeNames.UNIT_BAGS, requiredUnitBags);
                            postsMap.put(NodeNames.REQUIRED_DATE,requiredDate);
                            postsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                            postsMap.put(NodeNames.DISTRICT,district);
                            postsMap.put(NodeNames.AREA,area);
                            postsMap.put(NodeNames.POST_DESCRIPTION,postDescription);


                            mRootRef.child(NodeNames.POSTS).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){

                                        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(EditRequest.this, getString(R.string.postUpdated), Toast.LENGTH_SHORT).show();
                                                    myLoadingProgress.dismissAlertDialogue();
                                                    Intent intent = new Intent(EditRequest.this, SingleRequest.class);
                                                    intent.putExtra(Extras.POST_CREATOR_ID,postCreatorID);
                                                    intent.putExtra(Extras.POST_CREATOR_PHOTO,postCreatorPhoto);
                                                    intent.putExtra(Extras.POST_CREATOR_NAME,postCreatorName);
                                                    intent.putExtra(Extras.POST_ID,postID);
                                                    intent.putExtra(Extras.POST_DESCRIPTION,postDescription);
                                                    intent.putExtra(Extras.POST_IMAGE,postImage);
                                                    intent.putExtra(Extras.POST_LOVE,postLove);
                                                    intent.putExtra(Extras.POST_VIEW,postView);
                                                    intent.putExtra(Extras.BLOOD_GROUP,bloodGroup);
                                                    intent.putExtra(Extras.AREA,area);
                                                    intent.putExtra(Extras.DISTRICT,district);
                                                    intent.putExtra(Extras.ACCEPTED,accepted);
                                                    intent.putExtra(Extras.DONATED,donated);
                                                    intent.putExtra(Extras.TIMESTAMP,timeStamp);
                                                    intent.putExtra(Extras.CAUSE,cause);
                                                    intent.putExtra(Extras.GENDER,gender);
                                                    intent.putExtra(Extras.PHONE_NUMBER,phone);
                                                    intent.putExtra(Extras.UNIT_BAGS, requiredUnitBags);
                                                    intent.putExtra(Extras.REQUIRED_DATE,requiredDate);
                                                    intent.putExtra(Extras.ACCEPTED_FLAG,acceptedFlag);
                                                    intent.putExtra(Extras.LOVE_FLAG,loveFlag);
                                                    intent.putExtra(Extras.COMPLETED_FLAG,completedFlag);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    myLoadingProgress.dismissAlertDialogue();
                                                    Toast.makeText(EditRequest.this, getString(R.string.failedToUpdate), Toast.LENGTH_SHORT).show();
                                                    llProgressBar.setVisibility(View.VISIBLE);
                                                    btn_updateRequest.setVisibility(View.VISIBLE);
                                                }

                                            }
                                        });


                                    }
                                    else{
                                        myLoadingProgress.dismissAlertDialogue();
                                        Toast.makeText(EditRequest.this, "post Failed", Toast.LENGTH_SHORT).show();
                                        llProgressBar.setVisibility(View.VISIBLE);
                                        btn_updateRequest.setVisibility(View.VISIBLE);
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
        CompoundButton.OnCheckedChangeListener checkedChangeListenerCause = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    causeList.add(buttonView.getText().toString());
                    //Log.d("Tag", "cause Is : " +causeList.get(0));
                }
                else{
                    causeList.remove(buttonView.getText().toString());
                    //Log.d("Tag", "cause removed : " +causeList.get(1));
                }
            }
        };

        chip_cancer.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_dengu.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_caesar.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_thalasemia.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_other.setOnCheckedChangeListener(checkedChangeListenerCause);
        chip_delivery.setOnCheckedChangeListener(checkedChangeListenerCause);
        //return cause;
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
        //return bloodGroup;
    }





    private void setEditTextsData() {
        et_district.setText(district);
        et_area.setText(area);
        et_postDescription.setText(postDescription);
        et_phone.setText(phone);
        tv_requiredDate.setText(requiredDate);
    }



    private void setCheckedBloodGroup(String bloodGroup) {
        Log.d("tag","Blood Group Set checked : "+bloodGroup);
        switch (bloodGroup) {
            case "A+":
                chip_APositive.setChecked(true);
                break;
            case "A-":
                chip_ANegative.setChecked(true);
                break;
            case ("B+"):
                chip_BPositive.setChecked(true);
                break;
            case ("B-"):
                chip_BNegative.setChecked(true);
                break;
            case ("AB+"):
                chip_ABPositive.setChecked(true);
                break;
            case ("AB-"):
                chip_ABNegative.setChecked(true);
                break;
            case ("O+"):
                chip_OPositive.setChecked(true);
                break;
            case ("O-"):
                chip_ONegative.setChecked(true);
                break;

            //default:
                //Toast.makeText(this, getString(R.string.bloodGroupError), Toast.LENGTH_SHORT).show();
                //break;
        }
    }





    private void setCheckedGender( String gender) {

        if(gender.equals("Male")){
            chip_male.setChecked(true);
        }
        else if(gender.equals("Female")){
            chip_female.setChecked(true);
        }
        else{
            Toast.makeText(this, getString(R.string.genderError), Toast.LENGTH_SHORT).show();
        }
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
                iv_postImagePreview.setVisibility(View.VISIBLE);
                iv_cancelPostImagePreview.setVisibility(View.VISIBLE);
                postImageFlag=true;
                localImageUri = data.getData();
                postImage = localImageUri.toString();
                Glide.with(EditRequest.this).load(localImageUri).placeholder(R.drawable.featuredd)
                        .error(R.drawable.featuredd).into(iv_postImagePreview);

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
                Toast.makeText(this, "Access Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }








    public  void updateWithImage(String postID){
        String fileName = postID+".jpg";
        StorageReference fileRef = storageRootRef.child(NodeNames.POST_IMAGES_FOLDER).child(myUID+"/"+fileName);
        fileRef.putFile(localImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        postImage = uri.toString();

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
                        postsMap.put(NodeNames.UNIT_BAGS, requiredUnitBags);
                        postsMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                        postsMap.put(NodeNames.DISTRICT,district);
                        postsMap.put(NodeNames.AREA,area);
                        postsMap.put(NodeNames.POST_DESCRIPTION,postDescription);
                        postsMap.put(NodeNames.POST_PHOTO,postImage);
                        postsMap.put(NodeNames.COMPLETED_FLAG,completedFlag);


                        mRootRef.child(NodeNames.POSTS).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){

                                    mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).child(postID).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(EditRequest.this, getString(R.string.withPhoto), Toast.LENGTH_SHORT).show();
                                                iv_postImagePreview.setVisibility(View.GONE);
                                                iv_cancelPostImagePreview.setVisibility(View.GONE);
                                                myLoadingProgress.dismissAlertDialogue();
                                                finish();
                                                startActivity(new Intent(EditRequest.this, MainActivity.class));
                                            }
                                            else{
                                                myLoadingProgress.dismissAlertDialogue();
                                                Toast.makeText(EditRequest.this, getString(R.string.failedToPost), Toast.LENGTH_SHORT).show();
                                                llProgressBar.setVisibility(View.VISIBLE);
                                                btn_updateRequest.setVisibility(View.VISIBLE);
                                            }

                                        }
                                    });


                                }
                                else{
                                    myLoadingProgress.dismissAlertDialogue();
                                    Toast.makeText(EditRequest.this, "post Failed", Toast.LENGTH_SHORT).show();
                                    llProgressBar.setVisibility(View.VISIBLE);
                                    btn_updateRequest.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                });
            }
        });
    }





}
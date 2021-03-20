package com.bloodFinder.mybloodbank.userProfile.editProfileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.selectCountry.SelectCountry;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;
    private TextView tv_requiredDate,tv_district;
    private ImageView iv_profilePicture,iv_changePicture;
    private Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative, chip_male, chip_female;
    private EditText etName, et_phone,et_area,et_age;
    private String userName,bloodGroup, phone, email,gender,district="",area,age, userPhoto="",required_date;
    private ArrayList<String> bloodList,genderList;
    private View customProgressBarFull;
    private MyLoadingProgress myLoadingProgress;
    private Button btn_saveData;
    private ImageButton btn_saveProfilePicture,btn_cancelToSaveProfilePicture;
    private Uri cloudImageUri,localImageUri;
    private ConstraintLayout cl_holderSaveCancel;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private FirebaseUser currentUser;
    private StorageReference storageRootRef;
    private String userID,myUID;
    private final int RC_SELECT_COUNTRY = 1;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_editProfileActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
        etName = findViewById(R.id.et_name_editProfileActivity);
        et_phone = findViewById(R.id.et_phoneNumber_editProfileActivity);
        customProgressBarFull = findViewById(R.id.customProgressBarFull_editProfileActivity);
        et_area = findViewById(R.id.et_area_editProfileActivity);
        iv_changePicture = findViewById(R.id.iv_changePhoto_editProfileActivity);
        tv_district = findViewById(R.id.tv_district_editProfileActivity);
        et_age = findViewById(R.id.et_age_editProfileActivity);
        btn_saveData = findViewById(R.id.btnSave_editProfileActivity);
        btn_saveProfilePicture = findViewById(R.id.iv_savePicture_EditProfileActivity);
        btn_saveProfilePicture = findViewById(R.id.iv_savePicture_EditProfileActivity);
        btn_cancelToSaveProfilePicture = findViewById(R.id.iv_cancelToSavePicture_EditProfileActivity);
        iv_profilePicture = findViewById(R.id.iv_profilePicture_editProfileActivity);
        cl_holderSaveCancel = findViewById(R.id.cl_holder_save_cancel_EditProfileActivity);
        bloodList= new ArrayList<>();
        genderList = new ArrayList<>();
        myLoadingProgress = new MyLoadingProgress(this);

        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_editProfileActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_editProfileActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_editProfileActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_editProfileActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_editProfileActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_editProfileActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_editProfileActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_editProfileActivity);
        chip_female = findViewById(R.id.chip_gender_female_editProfileActivity);
        chip_male = findViewById(R.id.chip_gender_male_editProfileActivity);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        storageRootRef = FirebaseStorage.getInstance().getReference();
        mAuth =  FirebaseAuth.getInstance();
        myUID = FirebaseAuth.getInstance().getUid();


        if(getIntent().hasExtra(Extras.BLOOD_GROUP)){
            getIntentData();
            bloodList.add(bloodGroup);
            genderList.add(gender);
            setEditTextsData();
            setCheckedBloodGroup(bloodGroup);
            setCheckedGender(gender);
            setProfilePicture();
            btn_saveData.setVisibility(View.VISIBLE);
            getSelectedBloodGroup();
            getGender();
        }
        else{
            //fetchUserDetailsFromDatabase();
        }


        iv_changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePicturePressed();
            }
        });


        btn_saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        btn_saveProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserPhoto();
            }
        });


        btn_cancelToSaveProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(EditProfileActivity.this).load(userPhoto).into(iv_profilePicture);
                cl_holderSaveCancel.setVisibility(View.GONE);
            }
        });


        tv_district.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCountry.class);
            startActivityForResult(intent,RC_SELECT_COUNTRY);
        });



    }

    private void setProfilePicture() {
        Glide.with(EditProfileActivity.this).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(iv_profilePicture);
    }

    private void fetchUserDetailsFromDatabase() {
        mRootRef.child(NodeNames.USERS).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName ="";
                    area="";
                    district="";
                    gender="";
                    age="";
                    phone="";
                    bloodGroup="";
                    email = "";



                    if(snapshot.child(NodeNames.EMAIL).exists()){
                        if(snapshot.child(NodeNames.EMAIL).getValue()!=null){
                            if(!snapshot.child(NodeNames.EMAIL).getValue().toString().equals("")){
                                email = snapshot.getValue().toString();
                            }
                        }
                    }

                    if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                        if(snapshot.child(NodeNames.USER_PHOTO).getValue()!=null){
                            if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                userPhoto = snapshot.getValue().toString();
                            }
                        }
                    }
                    /*Glide.with(getContext()).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                            .error(R.drawable.ic_profile_picture).into(iv_profilePicture);*/
                    //iv_profilePicture.setImageResource(R.drawable.lincolnn);



                    if(snapshot.child(NodeNames.USER_NAME).getValue()!=null){
                        if(!snapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")){
                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                        }
                    }
                    etName.setText(userName);

                    if(snapshot.child(NodeNames.AREA).getValue()!=null){
                        if(!snapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                            area = snapshot.child((NodeNames.AREA)).getValue().toString();
                        }
                    }
                    et_area.setText(area);


                    if(snapshot.child(NodeNames.DISTRICT).getValue()!=null){
                        if(!snapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                            district = snapshot.child(NodeNames.DISTRICT).getValue().toString();
                        }
                    }
                    tv_district.setText(district);

                    if(snapshot.child(NodeNames.GENDER).getValue()!=null){
                        if(!snapshot.child(NodeNames.GENDER).getValue().toString().equals("")){
                            gender = snapshot.child(NodeNames.GENDER).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.AGE).getValue()!=null){
                        if(!snapshot.child(NodeNames.AGE).getValue().toString().equals("")){
                            age = snapshot.child(NodeNames.AGE).getValue().toString();
                        }
                    }
                    et_age.setText(age);


                    if(snapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                        if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                            phone = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                        }
                    }

                    if(snapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                        if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                            bloodGroup = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                        }
                    }

                    /*if(snapshot.child(NodeNames.DONATION_STATUS).getValue()!=null){
                        if(!snapshot.child(NodeNames.DONATION_STATUS).getValue().toString().equals("")){
                            donationStatus = snapshot.child(NodeNames.DONATION_STATUS).getValue().toString();
                        }
                    }
                    tv_donationStatus.setText(donationStatus);

                    if(snapshot.child(NodeNames.TOTAL_DONATION).getValue()!=null){
                        if(!snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString().equals("")){
                            totalDonation = snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString();
                        }
                    }
                    tv_totalDonation.setText(totalDonation);


                    if(snapshot.child(NodeNames.LAST_DONATION).getValue()!=null){
                        if(!snapshot.child(NodeNames.LAST_DONATION).getValue().toString().equals("")){
                            lastDonation = snapshot.child(NodeNames.LAST_DONATION).getValue().toString();
                        }
                    }
                    tv_lastDonation.setText(lastDonation);*/

                    bloodList.add(bloodGroup);
                    genderList.add(gender);
                    setEditTextsData();
                    setCheckedBloodGroup(bloodGroup);
                    setCheckedGender(gender);
                    btn_saveData.setVisibility(View.VISIBLE);
                    getSelectedBloodGroup();
                    getGender();
                }
                else {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.snapNotExists), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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









    private void setCheckedBloodGroup(String bloodGroup) {
        //Log.d("tag","Blood Group Set checked : "+bloodGroup);
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
            default:
                Toast.makeText(this, getString(R.string.bloodGroupError), Toast.LENGTH_SHORT).show();
                break;
        }
    }






    private void setEditTextsData() {
        etName.setText(userName);
        tv_district.setText(district);
        et_area.setText(area);
        et_age.setText(age);
        et_phone.setText(phone);
    }





    private void getIntentData() {

        userID = getIntent().getStringExtra(Extras.USER_ID);
        userName = getIntent().getStringExtra(Extras.USER_NAME);
        userPhoto = getIntent().getStringExtra(Extras.USER_PHOTO);
        bloodGroup = getIntent().getStringExtra(Extras.BLOOD_GROUP);
        district = getIntent().getStringExtra(Extras.DISTRICT);
        area = getIntent().getStringExtra(Extras.AREA);
        age = getIntent().getStringExtra(Extras.AGE);
        gender = getIntent().getStringExtra(Extras.GENDER);
        phone = getIntent().getStringExtra(Extras.PHONE_NUMBER);
        email = getIntent().getStringExtra(Extras.EMAIL);
        required_date = getIntent().getStringExtra(Extras.REQUIRED_DATE);

        if(!userPhoto.equals("")){
            cloudImageUri=Uri.parse(userPhoto);
        }
    }









    private void saveData() {

        getSelectedBloodGroup();
        if(bloodList.isEmpty()){

            Toast.makeText(this, getString(R.string.chooseBloodGroup), Toast.LENGTH_SHORT).show();
        }
        else{
            bloodGroup = bloodList.get(0);
            getGender();
            if(genderList.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_your_gender), Toast.LENGTH_SHORT).show();
            }
            else{
                gender = genderList.get(0);

                userName = etName.getText().toString();
                phone = et_phone.getText().toString();
                phone = et_phone.getText().toString();
                district = tv_district.getText().toString();
                age = et_age.getText().toString();
                area = et_area.getText().toString();



                if(userName.equals("")){
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

                else if(district.equals("")){
                    tv_district.setError(getString(R.string.enter_your_district));
                }

                else if(phone.equals("")){
                    etName.setError(getString(R.string.enter_your_phoneNumber));
                }

                else{
                    myLoadingProgress.startLoadingProgress();
                    Map<String,Object> userDataMap = new HashMap();
                    userDataMap.put(NodeNames.USER_NAME, userName);
                    userDataMap.put(NodeNames.BLOOD_GROUP,bloodGroup);
                    userDataMap.put(NodeNames.GENDER,gender);
                    userDataMap.put(NodeNames.DISTRICT,district);
                    userDataMap.put(NodeNames.AREA,area);
                    userDataMap.put(NodeNames.AGE,age);
                    userDataMap.put(NodeNames.PHONE_NUMBER,phone);

                    mRootRef.child(NodeNames.USERS).child(myUID).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                myLoadingProgress.dismissAlertDialogue();
                                Intent intent = new Intent(EditProfileActivity.this,EditProfileActivity.class);
                                intent.putExtra(Extras.USER_ID,myUID);
                                intent.putExtra(Extras.USER_NAME,userName);
                                intent.putExtra(Extras.USER_PHOTO,userPhoto);
                                intent.putExtra(Extras.BLOOD_GROUP,bloodGroup);
                                intent.putExtra(Extras.GENDER,gender);
                                intent.putExtra(Extras.PHONE_NUMBER,phone);
                                intent.putExtra(Extras.EMAIL,email);
                                intent.putExtra(Extras.DISTRICT,district);
                                intent.putExtra(Extras.AREA,area);
                                intent.putExtra(Extras.AGE,age);
                                intent.putExtra(Extras.PHONE_NUMBER,phone);
                                startActivity(intent);
                                Toast.makeText(EditProfileActivity.this, getString(R.string.profileSuccessfullyUpdated), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(EditProfileActivity.this, getString(R.string.profileUpdateFailed), Toast.LENGTH_SHORT).show();
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
                   //Log.d("tag","Blood Group on checked : "+bloodList.get(0));
                }

                else{
                    bloodList.remove(buttonView.getText().toString());
                    //Log.d("tag","Blood Group off checked : "+bloodList.get(0));
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
        //Log.d("tag","Blood Group get returned  checked : "+bloodGroup);
        //return bloodGroup;
        //return bloodGroup;
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
        //return gender;
    }






    public void changeProfilePicturePressed(){
        if(cloudImageUri == null){
            imagePicker();
        }
        else{
            PopupMenu popupMenu = new PopupMenu(EditProfileActivity.this,iv_changePicture);
            popupMenu.getMenuInflater().inflate(R.menu.menu_pic_profilepicture,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if(id == R.id.menuItem_changeImageID){
                        imagePicker();
                    }
                    else if (id == R.id.menuItem_removeImageID){
                        removePhoto();
                    }


                    return false;
                }
            });
            popupMenu.show();
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
                localImageUri = data.getData();
                Glide.with(EditProfileActivity.this).load(localImageUri).placeholder(R.drawable.ic_profile_picture)
                        .error(R.drawable.ic_profile_picture).into(iv_profilePicture);
                cl_holderSaveCancel.setVisibility(View.VISIBLE);

            }
        }

        if(requestCode==RC_SELECT_COUNTRY){
            if(resultCode==RESULT_OK){
                if (data != null) {
                    district = data.getStringExtra(Extras.COUNTRY_NAME);
                }
                tv_district.setText(district);
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









    public void removePhoto(){
                    myLoadingProgress.startLoadingProgress();
                    myUID = currentUser.getUid();
                    HashMap<String, Object > hashMap = new HashMap<>();
                    hashMap.put(NodeNames.USER_PHOTO,"");
                    mRootRef.child(NodeNames.USERS).child(myUID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditProfileActivity.this, "Photo Removed Successfully", Toast.LENGTH_SHORT).show();
                                myLoadingProgress.dismissAlertDialogue();
                                Log.d("tag","Alert Dialogue");
                            }

                            else{
                                myLoadingProgress.dismissAlertDialogue();
                                Toast.makeText(EditProfileActivity.this, "Profile Update failed"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }










    public  void saveUserPhoto(){
        myLoadingProgress.startLoadingProgress();
        String fileName = myUID+".jpg";
        StorageReference fileRef = storageRootRef.child(NodeNames.IMAGES_FOLDER+"/"+fileName);
        fileRef.putFile(localImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            cloudImageUri = uri;
                            Map<String,Object> userMap = new HashMap();
                            userMap.put(NodeNames.USER_PHOTO,cloudImageUri.toString());
                            mRootRef.child(NodeNames.USERS).child(myUID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        localImageUri=uri;
                                        cloudImageUri = uri;
                                        myLoadingProgress.dismissAlertDialogue();
                                        Glide.with(EditProfileActivity.this).load(localImageUri).into(iv_profilePicture);
                                        Toast.makeText(EditProfileActivity.this, getString(R.string.profileSuccessfullyUpdated), Toast.LENGTH_SHORT).show();
                                        cl_holderSaveCancel.setVisibility(View.GONE);
                                    }
                                    else{
                                        myLoadingProgress.dismissAlertDialogue();
                                        cl_holderSaveCancel.setVisibility(View.GONE);
                                        Toast.makeText(EditProfileActivity.this, getString(R.string.failedToUpdateProfilePicture), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
                else{
                    myLoadingProgress.dismissAlertDialogue();
                    cl_holderSaveCancel.setVisibility(View.GONE);
                    Toast.makeText(EditProfileActivity.this, getString(R.string.somethingWentWrong)+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
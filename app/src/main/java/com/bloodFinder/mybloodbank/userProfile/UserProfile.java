package com.bloodFinder.mybloodbank.userProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.chats.ChattingActivity.ChattingActivity;
import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;
    private String userID;
    private ImageView iv_profilePicture, iv_chat,iv_call;
    private TextView tv_userName,tv_area,tv_district,tv_gender,tv_age,tv_phone,tv_bloodGroup,tv_donationStatus
            ,tv_lastDonation,tv_totalDonation;
    private String userName,area,district,gender,age,phone,bloodGroup,donationStatus,lastDonation,totalDonation,userPhoto;
    private TextView tv_logout;
    private ConstraintLayout cl_chatAndCallHolder;
    private RecyclerView recyclerView;
    private List<ModelClassUserProfile> modelClassUserProfileList;
    private AdapterUserProfile adapterUserProfile;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRootRef,dbrUsers,dbrPosts;
    private String myUID;
    private SimpleArcLoader arcLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_userProfileActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        iv_profilePicture = findViewById(R.id.iv_profilePicture_userProfileActivity);
        iv_chat = findViewById(R.id.iv_chat_userProfileActivity);
        iv_call = findViewById(R.id.iv_call_userProfileActivity);
        tv_userName = findViewById(R.id.tv_userName_userProfileActivity);
        tv_area = findViewById(R.id.tv_area_userProfileActivity);
        tv_district = findViewById(R.id.tv_district_userProfileActivity);
        tv_gender = findViewById(R.id.tv_gender_userProfileActivity);
        tv_age = findViewById(R.id.tv_age_userProfileActivity);
        tv_phone = findViewById(R.id.tv_phone_userProfileActivity);
        tv_bloodGroup = findViewById(R.id.tv_bloodGroup_userProfileActivity);
        tv_donationStatus = findViewById(R.id.tv_donationStatus_userProfileActivity);
        tv_lastDonation = findViewById(R.id.tv_lastDonationDateValue_userProfileActivity);
        tv_totalDonation = findViewById(R.id.tv_totalDonationValue_userProfileActivity);
        cl_chatAndCallHolder = findViewById(R.id.cl_chatAndCallHolder_userProfileActivity);
        arcLoader = findViewById(R.id.arcLoader_UserProfileActivity);

        recyclerView = findViewById(R.id.rv_Posts_userProfileActivity);
        modelClassUserProfileList = new ArrayList<>();
        adapterUserProfile = new AdapterUserProfile(this,modelClassUserProfileList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterUserProfile);

        userID = getIntent().getStringExtra(Extras.USER_ID);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        dbrUsers = mRootRef.child(NodeNames.USERS);
        dbrPosts = mRootRef.child(NodeNames.POSTS);
        myUID = FirebaseAuth.getInstance().getUid();

        if(userID.equals(myUID)){
            cl_chatAndCallHolder.setVisibility(View.GONE);
        }

        loadUser();
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat();
            }
        });


        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, getString(R.string.youAreNotPermittedToCallThisUserYEt), Toast.LENGTH_SHORT).show();
               /* Uri numberToCall = Uri.parse("tel:"+phone);
                Intent intent = new Intent(Intent.ACTION_DIAL,numberToCall);
                startActivity(intent);*/
            }
        });



    }

    private void startChat() {
        Intent intent = new Intent(UserProfile.this, ChattingActivity.class);
        intent.putExtra(Extras.USER_ID,userID);
        intent.putExtra(Extras.USER_NAME,userName);
        intent.putExtra(Extras.USER_PHOTO,userPhoto);
        startActivity(intent);
    }


    private void loadUser() {
        dbrUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName="";
                    userPhoto = "";
                    area="";
                    district="";
                    gender="";
                    age="";
                    phone="";
                    bloodGroup="";
                    donationStatus=getString(R.string.i_want_to_donate);
                    lastDonation=getString(R.string.dateNotGiven);
                    totalDonation=getString(R.string.zero);


                    if(snapshot.child(NodeNames.USER_PHOTO).getValue()!=null){
                        if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                            userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                        }
                    }

                    Glide.with(UserProfile.this).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                            .placeholder(R.drawable.ic_profile_picture).error(R.drawable.ic_profile_picture).into(iv_profilePicture);



                    if(snapshot.child(NodeNames.USER_NAME).getValue()!=null){
                        if(!snapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")){
                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                        }
                    }
                    tv_userName.setText(userName);


                    if(snapshot.child(NodeNames.AREA).getValue()!=null){
                        if(!snapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                            area = snapshot.child((NodeNames.AREA)).getValue().toString();
                        }
                    }
                    tv_area.setText(area);


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
                    tv_gender.setText(gender);


                    if(snapshot.child(NodeNames.AGE).getValue()!=null){
                        if(!snapshot.child(NodeNames.AGE).getValue().toString().equals("")){
                            age = snapshot.child(NodeNames.AGE).getValue().toString();
                        }
                    }
                    tv_age.setText(age);


                    if(snapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                        if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                            phone = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                        }
                    }
                    //tv_phone.setText(phone);

                    if(snapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                        if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                            bloodGroup = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                        }
                    }
                    tv_bloodGroup.setText(bloodGroup);

                    if(snapshot.child(NodeNames.DONATION_STATUS).getValue()!=null){
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
                    tv_lastDonation.setText(lastDonation);

                    getFeeds();

                }
                else{
                    Toast.makeText(UserProfile.this, getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        });
    }










    private void getFeeds() {
        arcLoader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("tag","snapshot exists");
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        //String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                        //if(postCreatorID.equals(userID)){
                            String postPhoto = "";
                            String postBloodGroup = "";
                            String postTimeAgo = "";
                            String postDistrict = "";
                            String postArea = "";
                            String postDescription = "";
                            String postID = "";
                            String postPhone = "";
                            String postAccepted = "0";
                            String postDonated = "0";
                            String postCause = "";
                            String postLove = "0";
                            String postView = "0";
                            String postRequiredUnitBags = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String postGender = "";
                            String postRequiredDate = "";

                            if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                    postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                    postBloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                    postTimeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                    postDistrict = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                    postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                    postLove  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                    postView = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                    postArea = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                    postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                }
                            }


                        if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                postLove = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                postView = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                postAccepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                postDonated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                postCause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                postRequiredUnitBags = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                postPhone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                postGender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                postRequiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                            if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                            }
                        }


                        //Love Counter
                        if(dataSnapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).exists()){
                            for(DataSnapshot loveReactSnapshot : dataSnapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).getChildren()){
                                if(loveReactSnapshot.getValue().toString().equals(myUID)){
                                    loveCheckerFlag = Constants.TRUE;
                                }
                            }
                        }

                        if(dataSnapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).exists()){
                            for(DataSnapshot acceptedIdsSnapshot : dataSnapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).getChildren()){
                                if(acceptedIdsSnapshot.getValue().toString().equals(myUID)){
                                    acceptedFlag = Constants.TRUE;
                                }
                            }
                        }


                            ModelClassUserProfile object = new ModelClassUserProfile(userName,userPhoto,postBloodGroup,postDistrict,
                                    postDescription,postPhoto,postArea,postLove,postView,postTimeAgo,userID,postID,postCause,postGender,
                                    postAccepted,postDonated,postPhone,postRequiredUnitBags,postRequiredDate,loveCheckerFlag,acceptedFlag,completedFlag);

                            modelClassUserProfileList.add(object);
                            adapterUserProfile.notifyDataSetChanged();

                            arcLoader.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        /*}
                        else{

                        }*/

                    }
                }
                else{
                    Toast.makeText(UserProfile.this, "SnapShot Not Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
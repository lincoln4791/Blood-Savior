package com.bloodFinder.mybloodbank.filterPosts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.selectCountry.SelectCountry;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class FilterPosts extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;
    private TextView tv_district,tv_emptyListMessage;
    private Button btn_search;
    private String district="";
    private ImageView iv_backspaceDistrict;
    private SimpleArcLoader arcLoader;
    private ProgressBar pb_morePostLoading;
    private ArrayList<String> bloodList,causeList;
    private List<ModelClassFilteredPosts> modelClassFilteredPostsList;
    private RecyclerView recyclerView;
    private AdapterFilteredPosts adapterFilteredPosts;

    private DatabaseReference mRootRef;
    private String myUID;
    private final int RC_SELECT_COUNTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_posts);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_filterPostsActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
        tv_district = findViewById(R.id.tv_district_filterPostsActivity);
        tv_emptyListMessage = findViewById(R.id.tv_emptyFeed_filterPostsActivity);
        btn_search = findViewById(R.id.btn_search_filterPostsActivity);
        arcLoader = findViewById(R.id.arcLoader_filterPostsActivity);
        recyclerView = findViewById(R.id.rv_Posts_filterPostsActivity);
        pb_morePostLoading = findViewById(R.id.pb_morePostLoading_filterPostsActivity);
        iv_backspaceDistrict = findViewById(R.id.iv_backspaceDistrict_filterPostsActivity);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();
        bloodList = new ArrayList<>();
        causeList = new ArrayList<>();
        modelClassFilteredPostsList = new ArrayList<>();
        adapterFilteredPosts = new AdapterFilteredPosts(FilterPosts.this,modelClassFilteredPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterFilteredPosts);





        tv_district.setOnClickListener(v -> {
            Intent intent = new Intent(FilterPosts.this,SelectCountry.class);
            startActivityForResult(intent,RC_SELECT_COUNTRY);
        });

        btn_search.setOnClickListener(v -> {
            arcLoader.start();
            arcLoader.setVisibility(View.VISIBLE);
            selectSearchCriteria();

        });

        iv_backspaceDistrict.setOnClickListener(v -> {
            tv_district.setText("");
            district = "";
            iv_backspaceDistrict.setVisibility(View.GONE);
        });


        getSelectedBloodGroup();
        getCause();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode == RC_SELECT_COUNTRY){
               district = data.getStringExtra(Extras.COUNTRY_NAME);
                tv_district.setText(district);
                iv_backspaceDistrict.setVisibility(View.VISIBLE);

            }
        }
    }




    private void getSelectedBloodGroup() {
        Chip chip_APositive,chip_ANegative,chip_BPositive,chip_BNegative,chip_OPositive,chip_ONegative,chip_ABPositive,chip_ABNegative;

        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_filterPostsActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_filterPostsActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_filterPostsActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_filterPostsActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_filterPostsActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_filterPostsActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_filterPostsActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_filterPostsActivity);

        CompoundButton.OnCheckedChangeListener checkedChangeListenerBloodGroup = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bloodList.add(buttonView.getText().toString());
                    Log.d("tag","blood Added"+buttonView.getText().toString());
                }
                else{
                    bloodList.remove(buttonView.getText().toString());
                    Log.d("tag","removed"+buttonView.getText().toString());
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






    private void getCause() {
        Chip chip_cancer, chip_dengu, chip_ceasar,chip_thalasemia,chip_other,chip_delivery;
        chip_cancer = findViewById(R.id.chip_cause_cancer_filterPostsActivity);
        chip_dengu = findViewById(R.id.chip_cause_dengue_filterPostsActivity);
        chip_ceasar = findViewById(R.id.chip_cause_caesar_filterPostsActivity);
        chip_thalasemia = findViewById(R.id.chip_cause_thalassemia_filterPostsActivity);
        chip_other = findViewById(R.id.chip_cause_other_filterPostsActivity);
        chip_delivery = findViewById(R.id.chip_cause_delevery_filterPostsActivity);

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
    }






    private void filterResultForBloodGroupOnly() {
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        getSelectedBloodGroup();
        getCause();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String bloodGroup = "";
                        bloodGroup = bloodList.get(0);
                        if(bloodGroup.equals(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString())){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String district = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String cause = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }






    private  void filterResultForDistrictOnly(){

        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String district = "";
                        district = tv_district.getText().toString();
                        if(district.equals(dataSnapshot.child(NodeNames.DISTRICT).getValue().toString())){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String bloodGroup = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String cause = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });

    }







    private void filterResultForCauseOnly() {
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String cause = "";
                        cause = causeList.get(0);
                        if(cause.equals(dataSnapshot.child(NodeNames.CAUSE).getValue().toString())){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String bloodGroup = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String district = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }








    private void filterResultForBloodListAndDistrictListBoth() {
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String bloodGroup = "";
                        String district= "";
                        bloodGroup = bloodList.get(0);
                        district = tv_district.getText().toString();
                        if(bloodGroup.equals(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString()) &&
                                district.equals(dataSnapshot.child(NodeNames.DISTRICT).getValue().toString()) ){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String cause = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void filterResultForBloodListAndCauseListBoth() {
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String bloodGroup = "";
                        String cause= "";
                        bloodGroup = bloodList.get(0);
                        cause = causeList.get(0);
                        if(bloodGroup.equals(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString()) &&
                                cause.equals(dataSnapshot.child(NodeNames.CAUSE).getValue().toString()) ){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String district = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }







    private void filterResultForCauseListAndDistrictListBoth() {
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String district = "";
                        String cause= "";
                        district = tv_district.getText().toString();
                        cause = causeList.get(0);
                        if(district.equals(dataSnapshot.child(NodeNames.DISTRICT).getValue().toString()) &&
                                cause.equals(dataSnapshot.child(NodeNames.CAUSE).getValue().toString()) ){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String bloodGroup = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void filterResultForBloodListCauseListAndDistrictListAll(){
        modelClassFilteredPostsList.clear();
        tv_emptyListMessage.setVisibility(View.GONE);
        adapterFilteredPosts.notifyDataSetChanged();
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String district = "";
                        String cause= "";
                        String bloodGroup = "";
                        district = tv_district.getText().toString();
                        cause = causeList.get(0);
                        bloodGroup = bloodList.get(0);
                        if(district.equals(dataSnapshot.child(NodeNames.DISTRICT).getValue().toString()) &&
                                cause.equals(dataSnapshot.child(NodeNames.CAUSE).getValue().toString()) &&
                                bloodGroup.equals(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString())){
                            String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                            String postPhoto = "";
                            String timeAgo = "";
                            String postDescription = "";
                            String area = "";
                            String love = "0";
                            String views = "0";
                            String postID = "";
                            String gender = "";
                            String accepted = "0";
                            String donated = "0";
                            String phone = "";
                            String unitBag = "0";
                            String requiredDate = "";
                            String loveCheckerFlag = Constants.FALSE;
                            String acceptedFlag = Constants.FALSE;
                            String completedFlag = Constants.FALSE;
                            String post_order = "0";


                            if(dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null){
                                if(!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().equals("")){
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if(completedFlag.equals(Constants.FALSE)){


                                if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DISTRICT).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                        postDescription  = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().equals("")){
                                        love  = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.CAUSE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.GENDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.GENDER).getValue().equals("")){
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().equals("")){
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.DONATED).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.DONATED).getValue().equals("")){
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().equals("")){
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_ORDER).getValue().equals("")){
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if(dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                    if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
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


                           /* String finalPostDescription = postDescription;
                            String finalPostPhoto = postPhoto;
                            String finalArea = area;
                            String finalLove = love;
                            String finalViews = views;
                            String finalTimeAgo = timeAgo;
                            String finalBloodGroup = bloodGroup;
                            String finalTimeAgo1 = timeAgo;
                            String finalDistrict = district;
                            String finalPostID = postID;
                            String finalCause = cause;
                            String finalGender = gender;
                            String finalAccepted = accepted;
                            String finalDonated = donated;
                            String finalPhone = phone;
                            String finalUnitBag = unitBag;
                            String finalRequiredDate = requiredDate;
                            String finalLoveCheckerFlag = loveCheckerFlag;
                            String finalAcceptedFlag = acceptedFlag;
                            String finalCompletedFlag = completedFlag;
                            String finalPost_order = post_order;*/
                                ModelClassFilteredPosts object = new ModelClassFilteredPosts(bloodGroup,district,postDescription,postPhoto,
                                        area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                        loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                                modelClassFilteredPostsList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if(snapshot.exists()){
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                                if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            //modelClassFeedFragmentList.set(0,object);
                                            //Log.d("tag","S2 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                            //Collections.sort(modelClassFeedFragmentList,new SortPosts());
                                            //Collections.reverse(modelClassFeedFragmentList);
                                            arcLoader.stop();
                                            arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFilteredPosts.notifyDataSetChanged();
                                            //count++;


                                        }



                                        else{
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public  void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(FilterPosts.this, getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else{
                                //post will not be show in news feed because post is completed
                            }
                        }

                    }
                    if(modelClassFilteredPostsList.isEmpty()){
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                    }
                }
                else{
                    tv_emptyListMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FilterPosts.this, getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void selectSearchCriteria() {
        int searchCriteriaCount =0;
        if(!bloodList.isEmpty()){
            searchCriteriaCount++;
        }
        if(!district.equals("")){
            searchCriteriaCount++;
        }

        if(!causeList.isEmpty()){
            searchCriteriaCount++;
        }

        if(searchCriteriaCount==0){
            Toast.makeText(this, "Please select at least one criteria ", Toast.LENGTH_SHORT).show();
            arcLoader.stop();
            arcLoader.setVisibility(View.GONE);
        }
        else{
            if(searchCriteriaCount==1){
                if(!bloodList.isEmpty()){
                    filterResultForBloodGroupOnly();
                }
                else if(!district.equals("")){
                    filterResultForDistrictOnly();
                }
                else{
                    filterResultForCauseOnly();
                }
            }


            else if(searchCriteriaCount==2){
                if(!bloodList.isEmpty() && !district.equals("")){
                    filterResultForBloodListAndDistrictListBoth();
                }

                else if(!bloodList.isEmpty() && !causeList.isEmpty()){
                    filterResultForBloodListAndCauseListBoth();
                }

                else {
                    filterResultForCauseListAndDistrictListBoth();
                }
            }

            else{
                filterResultForBloodListCauseListAndDistrictListAll();
            }

        }


    }



}
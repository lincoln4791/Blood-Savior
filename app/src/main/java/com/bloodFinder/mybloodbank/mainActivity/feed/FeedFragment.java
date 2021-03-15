package com.bloodFinder.mybloodbank.mainActivity.feed;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.bloodBank.BloodBank;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.SortPosts;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.covid19Report.Covid19Report;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bloodFinder.mybloodbank.mainActivity.requests.makeNewRequest.MakeNewRequest;
import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private List<ModelClassFeedFragment> modelClassFeedFragmentList,tempList;
    private AdapterFeedFragment adapterFeedFragment;
    private SimpleArcLoader arcLoader;
    private ProgressBar pb_morePostLoading;
    private CardView cv_featuredCovid19,cv_findDonors,cv_makeNewRequest,cv_bloodBank,cv_topDonors;
    private TextView tv_allCaughtUp;
    private LinearLayoutManager linearLayoutManager;
    private Boolean isScrolling = false;
    private Query query;

    private DatabaseReference mRootRef,dbrPosts,dbrUsers;
    private String userID,myUID;
    private int currentPage = 1;
    private int counter = 1;
    private int lastPostIndex;
    private String lastNode;

    private static final int RECORD_PER_PAGE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Util.connectionAvailable(getContext())){
            linearLayoutManager = new LinearLayoutManager(getContext());
            modelClassFeedFragmentList = new ArrayList<>();
            tempList = new ArrayList<>();
            adapterFeedFragment = new AdapterFeedFragment(getContext(),modelClassFeedFragmentList);
            recyclerView = view.findViewById(R.id.rv_Posts_FeedFragment);
            nestedScrollView = view.findViewById(R.id.nsv_feedFragment);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapterFeedFragment);
            arcLoader = view.findViewById(R.id.arcLoader_FeedFragment);
            cv_featuredCovid19 = view.findViewById(R.id.cv_featuredCovid19_FeedFragment);
            cv_findDonors = view.findViewById(R.id.cv_findDonors_feedFragment);
            cv_makeNewRequest = view.findViewById(R.id.cv_makeNewRequest_MainActivity);
            cv_bloodBank = view.findViewById(R.id.cv_bloodBank_MainActivity);
            cv_topDonors = view.findViewById(R.id.cv_topDonors_MainActivity);
            pb_morePostLoading = view.findViewById(R.id.pb_morePostLoading_feedFragment);
            tv_allCaughtUp = view.findViewById(R.id.tv_allCaughtUp_feedFragment);

            mRootRef = FirebaseDatabase.getInstance().getReference();
            dbrPosts = mRootRef.child(NodeNames.POSTS);
            dbrUsers = mRootRef.child(NodeNames.USERS);
            myUID = FirebaseAuth.getInstance().getUid();
            arcLoader.start();

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                        //Log.d("tag","new page");
                        //currentPage++;
                        //fetchAllData();
                        //loadMoreData();
                        pb_morePostLoading.setVisibility(View.VISIBLE);
                        lastPostIndex = lastPostIndex-3;
                        downloadMoreData();
                    }
                }
            });

            cv_featuredCovid19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(new Intent(getContext(), Covid19Report.class));

                }
            });


            cv_findDonors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Find Donors CLicked", Toast.LENGTH_SHORT).show();
                }
            });

            cv_makeNewRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MakeNewRequest.class));
                }
            });

            cv_topDonors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "top Donors Clicked", Toast.LENGTH_SHORT).show();
                }
            });


            cv_bloodBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), BloodBank.class));
                }
            });


            getLastNode();


          /*  recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                        isScrolling =true;
                        Log.d("tag","Scrolling "+isScrolling);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int currentItems = linearLayoutManager.getChildCount();
                    int totalItems = linearLayoutManager.getItemCount();
                    int scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if(isScrolling && currentItems+scrolledOutItems==totalItems){
                        currentPage++;
                        Log.d("tag","Scrolling "+currentPage);
                        fetchAllData();
                    }
                }
            });*/

            //fetchAllData();
            //test();
        }
        else{
            Dialog noInternetDialog = new Dialog(getContext());
            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.alert_no_internet,null);
            noInternetDialog.setContentView(view1);
            noInternetDialog.setCancelable(false);
            noInternetDialog.show();

            Button refresh = view1.findViewById(R.id.btn_noInternetAlert);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            });

        }


    }

    private void getLastNode() {
        mRootRef.child(NodeNames.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  lastPostIndex= (int) snapshot.getChildrenCount();
                  lastNode = String.valueOf(lastPostIndex);

                  for(counter=lastPostIndex ; lastPostIndex-counter<3;counter--){
                      String tempPostID = String.valueOf(counter);
                      mRootRef.child(NodeNames.POSTS).child(tempPostID).addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              downloadData(snapshot);
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.dataBaseError)+error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchAllData() {
        modelClassFeedFragmentList.clear();

        query = mRootRef.child(NodeNames.POSTS).limitToLast(currentPage*RECORD_PER_PAGE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Log.d("tag","order :"+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                        String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                        String postPhoto = "";
                        String bloodGroup = "";
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
                            ModelClassFeedFragment object = new ModelClassFeedFragment(bloodGroup,district,postDescription,postPhoto,
                                    area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                    loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                            modelClassFeedFragmentList.add(object);
                            //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                            dbrUsers.child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        arcLoader.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapterFeedFragment.notifyDataSetChanged();
                                        //count++;


                                    }



                                    else{
                                        //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                        //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public  void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else{
                            //post will not be show in news feed because post is completed
                        }

                    }
                }
                else{
                    TextView tv_emptyText = getView().findViewById(R.id.tv_emptyFeed_feedFragment);
                    tv_emptyText.setVisibility(View.VISIBLE);
                    arcLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }


    private synchronized void callBackForData(firebaseCallBack firebaseCallBack,String postCreatorID){
        dbrUsers.child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = "";
                String userPhoto = "";
                if(snapshot.exists()){
                    userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();

                    if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                        if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                            userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                        }
                    }
                    firebaseCallBack.onCallBack(userName,userPhoto);

                }



                else{
                    //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                    //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public  void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.canceled), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private interface firebaseCallBack {
        void onCallBack(String userName, String userPhoto);
    }


    public void loadMoreData(){
        query = mRootRef.child(NodeNames.POSTS).limitToLast(currentPage*RECORD_PER_PAGE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Log.d("tag","order :"+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                        String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                        String postPhoto = "";
                        String bloodGroup = "";
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
                            ModelClassFeedFragment object = new ModelClassFeedFragment(bloodGroup,district,postDescription,postPhoto,
                                    area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                                    loveCheckerFlag,acceptedFlag,completedFlag,post_order);
                            modelClassFeedFragmentList.add(object);
                            //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                            dbrUsers.child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        arcLoader.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapterFeedFragment.notifyDataSetChanged();
                                        //count++;


                                    }



                                    else{
                                        //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                        //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public  void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else{
                            //post will not be show in news feed because post is completed
                        }

                    }
                }
                else{
                    //Toast.makeText(getContext(), "SnapShot Not Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    private void downloadData(DataSnapshot dataSnapshot){
        String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
        String postPhoto = "";
        String bloodGroup = "";
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
            ModelClassFeedFragment object = new ModelClassFeedFragment(bloodGroup,district,postDescription,postPhoto,
                    area,love,views,timeAgo,postCreatorID,postID,cause,gender,accepted,donated,phone,unitBag,requiredDate,
                    loveCheckerFlag,acceptedFlag,completedFlag,post_order);
            modelClassFeedFragmentList.add(object);
            //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
            dbrUsers.child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        arcLoader.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        pb_morePostLoading.setVisibility(View.GONE);
                        adapterFeedFragment.notifyDataSetChanged();
                        //count++;


                    }



                    else{
                        //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                        //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public  void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                }
            });
        }

        else{
            //post will not be show in news feed because post is completed
        }

    }








    private void downloadMoreData(){
        lastNode = String.valueOf(lastPostIndex);
        for(counter=lastPostIndex ; lastPostIndex-counter<3;counter--){
            if(counter>0){
                String tempPostID = String.valueOf(counter);
                mRootRef.child(NodeNames.POSTS).child(tempPostID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        downloadData(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else{
                tv_allCaughtUp.setVisibility(View.VISIBLE);
                pb_morePostLoading.setVisibility(View.GONE);
            }

        }
    }

}
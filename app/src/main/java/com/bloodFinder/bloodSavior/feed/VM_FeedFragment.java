package com.bloodFinder.bloodSavior.feed;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Constants;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VM_FeedFragment extends AndroidViewModel {

    public MutableLiveData<List<ModelClassFeedFragment>> mutableList = new MutableLiveData<>();
    public MutableLiveData<Boolean> mutableIsLoading = new MutableLiveData<>();
    public DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public String myUID = FirebaseAuth.getInstance().getUid();
    public boolean isMAx = false;
    private boolean isLoading = false;
    public int postIndex = 0;
    public int currentPage = 1;
    public int recordPerPage = 4;
    public List<ModelClassFeedFragment> list = new ArrayList<>();
    public List<ModelClassFeedFragment> tempList = new ArrayList<>();
    public String lastNode;

    public VM_FeedFragment(@NonNull Application application) {
        super(application);
    }


    public void getLastNode() {
        isLoading=true;
        mutableIsLoading.setValue(isLoading);
        Query query = mRootRef.child(NodeNames.POSTS).orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        lastNode = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                        lastNode = String.valueOf(Integer.parseInt(lastNode)+1);
                        postIndex = Integer.parseInt(lastNode);
                        String startingNode = String.valueOf(Integer.parseInt(lastNode) - recordPerPage);
                        firstLoad(startingNode,recordPerPage);
                    }

                } else {
                    Log.d("tag", "snapshot not exists");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void firstLoad(String key, int limitToLoad) {
        if (!isMAx) {
            //isLoading=true;
            //mutableIsLoading.setValue(isLoading);
            Log.d("tag", "Key : " + key);
            Query query = mRootRef.child(NodeNames.POSTS).orderByKey().startAt(key).limitToFirst(limitToLoad);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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


                            if (dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue() != null) {
                                if (!dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString().equals("")) {
                                    completedFlag = dataSnapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                }
                            }

                            if (completedFlag.equals(Constants.FALSE)) {


                                if (dataSnapshot.child(NodeNames.POST_PHOTO).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString().equals("")) {
                                        postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")) {
                                        bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.TIMESTAMP).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")) {
                                        timeAgo = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.DISTRICT).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")) {
                                        district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString().equals("")) {
                                        postDescription = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.POST_LOVE).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString().equals("")) {
                                        love = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }


                                if (dataSnapshot.child(NodeNames.AREA).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")) {
                                        area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.POST_ID).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_ID).getValue().toString().equals("")) {
                                        postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.CAUSE).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.CAUSE).getValue().toString().equals("")) {
                                        cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.GENDER).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")) {
                                        gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.ACCEPTED).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString().equals("")) {
                                        accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.DONATED).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.DONATED).getValue().toString().equals("")) {
                                        donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")) {
                                        phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.UNIT_BAGS).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString().equals("")) {
                                        unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString().equals("")) {
                                        requiredDate = dataSnapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if (dataSnapshot.child(NodeNames.POST_ORDER).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString().equals("")) {
                                        post_order = dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                //View Counter
                                if (dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).exists()) {
                                    int viewCounter = 0;
                                    for (DataSnapshot viewSnapshot : dataSnapshot.child(NodeNames.VIEW_COUNTER_FOLDER).getChildren()) {
                                        viewCounter++;
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null) {
                                    if (!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString().equals("")) {
                                        views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                                    }
                                }


                                //Love Counter
                                if (dataSnapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).exists()) {
                                    for (DataSnapshot loveReactSnapshot : dataSnapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).getChildren()) {
                                        if (loveReactSnapshot.getValue().toString().equals(myUID)) {
                                            loveCheckerFlag = Constants.TRUE;
                                        }
                                    }
                                }

                                if (dataSnapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).exists()) {
                                    for (DataSnapshot acceptedIdsSnapshot : dataSnapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).getChildren()) {
                                        if (acceptedIdsSnapshot.getValue().toString().equals(myUID)) {
                                            acceptedFlag = Constants.TRUE;
                                        }
                                    }
                                }


                                ModelClassFeedFragment object = new ModelClassFeedFragment(bloodGroup, district, postDescription, postPhoto,
                                        area, love, views, timeAgo, postCreatorID, postID, cause, gender, accepted, donated, phone, unitBag, requiredDate,
                                        loveCheckerFlag, acceptedFlag, completedFlag, post_order);
                                tempList.add(object);
                                //Log.d("tag","S1 : "+dataSnapshot.child(NodeNames.POST_ORDER).getValue().toString());
                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Log.d("tag","after "+dataSnapshot.child("test").getValue().toString());
                                        String userName = "";
                                        String userPhoto = "";
                                        if (snapshot.exists()) {
                                            userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                                            if (snapshot.child(NodeNames.USER_PHOTO).exists()) {
                                                if (!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")) {
                                                    userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                                }
                                            }

                                            object.setPostCreatorName(userName);
                                            object.setPostCreatorPhoto(userPhoto);
                                            postIndex--;

                                            Log.d("tag", "key : " + key);
                                            Log.d("tag", "postIndex : " + postIndex);

                                            if (postIndex == Integer.parseInt(key)) {
                                                Log.d("tag", "adapter called");
                                                Log.d("tag","size : "+list.size());
                                                mutableList.setValue(list);

                                                //adapter.notifyDataSetChanged();
                                            }

                                            if (postIndex == 1) {
                                                isMAx = true;
                                            }

                                            /*arcLoader.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            pb_morePostLoading.setVisibility(View.GONE);
                                            adapterFeedFragment.notifyDataSetChanged();*/
                                            //count++;
                                        } else {
                                            //This line occuring a crush when i go inlandscape mood when feedfragment is loading
                                            //Toast.makeText(getActivity(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplication().getApplicationContext(), getApplication().getApplicationContext().getApplicationContext().getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                postIndex--;
                                //post will not be show in news feed because post is completed
                            }


                        }
                        //Collections.sort(tempList,new Sort.SortPosts());
                        Collections.reverse(tempList);
                        list.addAll(tempList);
                        isLoading=false;
                        mutableIsLoading.setValue(isLoading);
                        //mutableList.setValue(list);
                        //adapter.notifyDataSetChanged();
                        tempList.clear();
                        /*for (int i = 0; i < recordPerPage * currentPage; i++) {
                            Log.d("tag", "list is : " + list.get(i).getValue());
                        }*/
                        Log.d("tag", "done");
                    } else {
                        Log.d("tag", "snapShot not exists");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(getApplication().getApplicationContext(), "max Data Reached", Toast.LENGTH_SHORT).show();
        }
    }


}

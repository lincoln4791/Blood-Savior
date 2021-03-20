package com.bloodFinder.mybloodbank.requests.AcceptedFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.SortPosts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AcceptedFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ModelClassAcceptedFragment> modelClassAcceptedFragmentList;
    private AdapterAcceptedFragment adapterAcceptedFragment;
    private TextView tv_emptyList;

    private DatabaseReference mRootRef;
    String myUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_posts_acceptedRequestsFragment);
        modelClassAcceptedFragmentList= new ArrayList<>();
        adapterAcceptedFragment = new AdapterAcceptedFragment(modelClassAcceptedFragmentList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterAcceptedFragment);
        tv_emptyList = view.findViewById(R.id.tv_emptyListText_acceptedFragment);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();

        mRootRef.child(NodeNames.ACCEPTED_REQUESTS).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();
                        String postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();

                        mRootRef.child(NodeNames.POSTS).child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String area = "";
                                String district = "";
                                String bloodGroup = "";
                                String timeStamp = "";
                                String accepted = "0";
                                String donated = "0";
                                String postPhoto = "";
                                String cause = "";
                                String gender = "";
                                String postDescription = "";
                                String love = "0";
                                String view = "0";
                                String phone = "";
                                String unitBag = "";
                                String requiredDate = "";
                                String acceptedFlag = Constants.FALSE;
                                String loveCheckerFlag = Constants.FALSE;
                                String completedFlag = Constants.FALSE;
                                String post_order = "0";

                                if(snapshot.child(NodeNames.AREA).getValue()!=null){
                                    if(!snapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                                        area = snapshot.child(NodeNames.AREA).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.DISTRICT).getValue()!=null){
                                    if(!snapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                                        district = snapshot.child(NodeNames.DISTRICT).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                                    if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                                        bloodGroup = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                    }
                                }


                                if(snapshot.child(NodeNames.ACCEPTED).getValue()!=null){
                                    if(!snapshot.child(NodeNames.ACCEPTED).getValue().toString().equals("")){
                                        accepted = snapshot.child(NodeNames.ACCEPTED).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.DONATED).getValue()!=null){
                                    if(!snapshot.child(NodeNames.DONATED).getValue().toString().equals("")){
                                        donated = snapshot.child(NodeNames.DONATED).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.TIMESTAMP).getValue()!=null){
                                    if(!snapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")){
                                        timeStamp = snapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                    }
                                }


                                if(snapshot.child(NodeNames.POST_PHOTO).getValue()!=null){
                                    if(!snapshot.child(NodeNames.POST_PHOTO).getValue().toString().equals("")){
                                        postPhoto = snapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.CAUSE).getValue()!=null){
                                    if(!snapshot.child(NodeNames.CAUSE).getValue().toString().equals("")){
                                        cause = snapshot.child(NodeNames.CAUSE).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.GENDER).getValue()!=null){
                                    if(!snapshot.child(NodeNames.GENDER).getValue().toString().equals("")){
                                        gender = snapshot.child(NodeNames.GENDER).getValue().toString();
                                    }
                                }



                                if(snapshot.child(NodeNames.POST_DESCRIPTION).getValue()!=null){
                                    if(!snapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString().equals("")){
                                        postDescription = snapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.POST_LOVE).getValue()!=null){
                                    if(!snapshot.child(NodeNames.POST_LOVE).getValue().toString().equals("")){
                                        love = snapshot.child(NodeNames.POST_LOVE).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.POST_VIEWS).getValue()!=null){
                                    if(!snapshot.child(NodeNames.POST_VIEWS).getValue().toString().equals("")){
                                        view = snapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                                    if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                                        phone = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.UNIT_BAGS).getValue()!=null){
                                    if(!snapshot.child(NodeNames.UNIT_BAGS).getValue().toString().equals("")){
                                        unitBag = snapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                                    }
                                }


                                if(snapshot.child(NodeNames.REQUIRED_DATE).getValue()!=null){
                                    if(!snapshot.child(NodeNames.REQUIRED_DATE).getValue().toString().equals("")){
                                        requiredDate = snapshot.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                    }
                                }


                                if(snapshot.child(NodeNames.COMPLETED_FLAG).getValue()!=null){
                                    if(!snapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString().equals("")){
                                        completedFlag = snapshot.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.POST_ORDER).getValue()!=null){
                                    if(!snapshot.child(NodeNames.POST_ORDER).getValue().toString().equals("")){
                                        post_order = snapshot.child(NodeNames.POST_ORDER).getValue().toString();
                                    }
                                }

                                if(snapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).exists()){
                                    for(DataSnapshot acceptedSnapshot : snapshot.child(NodeNames.ACCEPTED_IDS_FOLDER).getChildren()){
                                        if(acceptedSnapshot.getValue().toString().equals(myUID)){
                                            acceptedFlag=Constants.TRUE;
                                        }
                                    }
                                }

                                //Love Counter
                                if(snapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).exists()){
                                    for(DataSnapshot loveReactSnapshot : snapshot.child(NodeNames.LOVE_REACT_COUNT_FOLDER).getChildren()){
                                        if(loveReactSnapshot.getValue().toString().equals(myUID)){
                                            loveCheckerFlag = Constants.TRUE;
                                        }
                                    }
                                }

                                String finalBloodGroup = bloodGroup;
                                String finalArea = area;
                                String finalUnitBag = unitBag;
                                String finalAcceptedFlag = acceptedFlag;
                                String finalPostPhoto = postPhoto;
                                String finalTimeStamp = timeStamp;
                                String finalCause = cause;
                                String finalGender = gender;
                                String finalDistrict = district;
                                String finalAccepted = accepted;
                                String finalDonated = donated;
                                String finalLoveCheckerFlag = loveCheckerFlag;
                                String finalRequiredDate = requiredDate;
                                String finalCompletedFlag = completedFlag;
                                String finalPost_order = post_order;
                                String finalPostDescription = postDescription;
                                String finalLove = love;
                                String finalView = view;
                                String finalPhone = phone;

                                mRootRef.child(NodeNames.USERS).child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        String userName = snapshot1.child(NodeNames.USER_NAME).getValue().toString();
                                        String postCreatorPhotoTemp = "";
                                        if(snapshot1.child(NodeNames.USER_PHOTO).exists()){
                                            if(!snapshot1.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                                postCreatorPhotoTemp = snapshot1.child(NodeNames.USER_PHOTO).getValue().toString();
                                            }
                                        }

                                        ModelClassAcceptedFragment object = new ModelClassAcceptedFragment(finalBloodGroup, finalArea, finalDistrict, finalAccepted, finalDonated,postCreatorID,
                                                userName,postID, finalPostPhoto, finalTimeStamp, finalCause, finalGender, postCreatorPhotoTemp, finalPostDescription, finalLove, finalView, finalPhone
                                                , finalUnitBag, finalAcceptedFlag, finalLoveCheckerFlag, finalRequiredDate, finalCompletedFlag, finalPost_order);
                                        modelClassAcceptedFragmentList.add(object);
                                        Collections.sort(modelClassAcceptedFragmentList,new SortPosts.SortAcceptedPosts());
                                        Collections.reverse(modelClassAcceptedFragmentList);
                                        adapterAcceptedFragment.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    tv_emptyList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
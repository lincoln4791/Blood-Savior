package com.bloodFinder.mybloodbank.requests.MyRequests;

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
import com.bloodFinder.mybloodbank.common.UtilDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRequestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMyRequestsFragment adapterMyRequestsFragment;
    private List<ModelClassMyRequests> modelClassMyRequestsList;

    private DatabaseReference mRootRef;
    private String myUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.rv_Posts_MyRequestFragment);
        modelClassMyRequestsList = new ArrayList<>();
        adapterMyRequestsFragment = new AdapterMyRequestsFragment(getContext(),modelClassMyRequestsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterMyRequestsFragment);
        myUID = FirebaseAuth.getInstance().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        modelClassMyRequestsList.clear();

        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String postCreatorID = myUID;
                        String postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();

                        mRootRef.child(NodeNames.POSTS).child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if(snapshot1.exists()){
                                    String userName = "";
                                    String postCreatorPhoto = "";
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
                                    String post_order = Constants.FALSE;

                                    userName = UtilDB.USER_NAME;
                                    postCreatorPhoto = UtilDB.USER_PHOTO;

                                    if(snapshot1.child(NodeNames.AREA).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.AREA).getValue().toString().equals("")){
                                            area = snapshot1.child(NodeNames.AREA).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.DISTRICT).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                                            district = snapshot1.child(NodeNames.DISTRICT).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                                            bloodGroup = snapshot1.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                        }
                                    }


                                    if(snapshot1.child(NodeNames.ACCEPTED).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.ACCEPTED).getValue().toString().equals("")){
                                            accepted = snapshot1.child(NodeNames.ACCEPTED).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.DONATED).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.DONATED).getValue().toString().equals("")){
                                            donated = snapshot1.child(NodeNames.DONATED).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.TIMESTAMP).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.TIMESTAMP).getValue().toString().equals("")){
                                            timeStamp = snapshot1.child(NodeNames.TIMESTAMP).getValue().toString();
                                        }
                                    }


                                    if(snapshot1.child(NodeNames.POST_PHOTO).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.POST_PHOTO).getValue().toString().equals("")){
                                            postPhoto = snapshot1.child(NodeNames.POST_PHOTO).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.CAUSE).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.CAUSE).getValue().toString().equals("")){
                                            cause = snapshot1.child(NodeNames.CAUSE).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.GENDER).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.GENDER).getValue().toString().equals("")){
                                            gender = snapshot1.child(NodeNames.GENDER).getValue().toString();
                                        }
                                    }



                                    if(snapshot1.child(NodeNames.POST_DESCRIPTION).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.POST_DESCRIPTION).getValue().toString().equals("")){
                                            postDescription = snapshot1.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.POST_LOVE).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.POST_LOVE).getValue().toString().equals("")){
                                            love = snapshot1.child(NodeNames.POST_LOVE).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.POST_VIEWS).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.POST_VIEWS).getValue().toString().equals("")){
                                            view = snapshot1.child(NodeNames.POST_VIEWS).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                                            phone = snapshot1.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.UNIT_BAGS).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.UNIT_BAGS).getValue().toString().equals("")){
                                            unitBag = snapshot1.child(NodeNames.UNIT_BAGS).getValue().toString();
                                        }
                                    }


                                    if(snapshot1.child(NodeNames.REQUIRED_DATE).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.REQUIRED_DATE).getValue().toString().equals("")){
                                            requiredDate = snapshot1.child(NodeNames.REQUIRED_DATE).getValue().toString();
                                        }
                                    }


                                    if(snapshot1.child(NodeNames.COMPLETED_FLAG).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.COMPLETED_FLAG).getValue().toString().equals("")){
                                            completedFlag = snapshot1.child(NodeNames.COMPLETED_FLAG).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.POST_ORDER).getValue()!=null){
                                        if(!snapshot1.child(NodeNames.POST_ORDER).getValue().toString().equals("")){
                                            post_order = snapshot1.child(NodeNames.POST_ORDER).getValue().toString();
                                        }
                                    }

                                    if(snapshot1.child(NodeNames.ACCEPTED_IDS_FOLDER).exists()){
                                        for(DataSnapshot acceptedSnapshot : snapshot1.child(NodeNames.ACCEPTED_IDS_FOLDER).getChildren()){
                                            if(acceptedSnapshot.getValue().toString().equals(myUID)){
                                                acceptedFlag=Constants.TRUE;
                                            }
                                        }
                                    }

                                    //Love Counter
                                    if(snapshot1.child(NodeNames.LOVE_REACT_COUNT_FOLDER).exists()){
                                        for(DataSnapshot loveReactSnapshot : snapshot1.child(NodeNames.LOVE_REACT_COUNT_FOLDER).getChildren()){
                                            if(loveReactSnapshot.getValue().toString().equals(myUID)){
                                                loveCheckerFlag = Constants.TRUE;
                                            }
                                        }
                                    }



                                    ModelClassMyRequests object = new ModelClassMyRequests(bloodGroup,area,district,accepted,donated,postCreatorID,
                                            userName,postID,postPhoto,timeStamp,cause,gender, postCreatorPhoto,postDescription,love,view,phone
                                            ,unitBag,acceptedFlag,loveCheckerFlag,requiredDate,completedFlag,post_order);
                                    modelClassMyRequestsList.add(object);
                                    Collections.sort(modelClassMyRequestsList,new SortPosts.SortMyPosts());
                                    Collections.reverse(modelClassMyRequestsList);
                                    adapterMyRequestsFragment.notifyDataSetChanged();
                                }
                                else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else{
                    TextView tv_emptyList = getView().findViewById(R.id.tv_emptyListText_myRequests);
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
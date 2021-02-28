package com.bloodFinder.mybloodbank.mainActivity.requests.MyRequests;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        recyclerView = view.findViewById(R.id.rv_Posts_MyRequestFragment);
        modelClassMyRequestsList = new ArrayList<>();
        adapterMyRequestsFragment = new AdapterMyRequestsFragment(getContext(),modelClassMyRequestsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterMyRequestsFragment);
        myUID = FirebaseAuth.getInstance().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference();


        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String area = "";
                        String district = "";
                        String bloodGroup = "";
                        String timeStamp = "";
                        String accepted = "0";
                        String donated = "0";
                        String userName = "";
                        String postPhoto = "";
                        String cause = "";
                        String gender = "";
                        String postCreatorPhoto = "";
                        String postDescription = "";
                        String love = "";
                        String view = "";
                        String phone = "";
                        String unitBag = "";

                        String postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                        Log.d("tag","postID : "+postID);

                        if(dataSnapshot.child(NodeNames.AREA).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                                area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.DISTRICT).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                                district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                                bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                            }
                        }


                        if(dataSnapshot.child(NodeNames.ACCEPTED).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString().equals("")){
                                accepted = dataSnapshot.child(NodeNames.ACCEPTED).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.DONATED).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.DONATED).getValue().toString().equals("")){
                                donated = dataSnapshot.child(NodeNames.DONATED).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.TIMESTAMP).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")){
                                timeStamp = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_CREATOR_NAME).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_CREATOR_NAME).getValue().toString().equals("")){
                                userName = dataSnapshot.child(NodeNames.POST_CREATOR_NAME).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_PHOTO).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString().equals("")){
                                postPhoto = dataSnapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.CAUSE).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.CAUSE).getValue().toString().equals("")){
                                cause = dataSnapshot.child(NodeNames.CAUSE).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.GENDER).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")){
                                gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue().toString().equals("")){
                                postCreatorPhoto = dataSnapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString().equals("")){
                                postDescription = dataSnapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_LOVE).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString().equals("")){
                                love = dataSnapshot.child(NodeNames.POST_LOVE).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString().equals("")){
                                view = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                                phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                            }
                        }

                        if(dataSnapshot.child(NodeNames.UNIT_BAGS).getValue()!=null){
                            if(!dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString().equals("")){
                                unitBag = dataSnapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                            }
                        }



                        ModelClassMyRequests modelClassMyRequests = new ModelClassMyRequests(bloodGroup,area,district,accepted,donated,myUID,
                                userName,postID,postPhoto,timeStamp,cause,gender, postCreatorPhoto,postDescription,love,view,unitBag,phone);
                        modelClassMyRequestsList.add(modelClassMyRequests);
                        adapterMyRequestsFragment.notifyDataSetChanged();

                    }
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.snapNotExists), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getString(R.string.dataBaseError), Toast.LENGTH_SHORT).show();
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
}
package com.bloodFinder.mybloodbank.mainActivity.feed;

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
import com.bloodFinder.mybloodbank.common.MyLoadingProgress;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ModelClassFeedFragment> modelClassFeedFragmentList;
    private AdapterFeedFragment adapterFeedFragment;
    private SimpleArcLoader arcLoader;


    private DatabaseReference mRootRef,dbrPosts,dbrUsers;
    private String userID,myUID,userName= "",userPhoto="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_Posts_FeedFragment);
        modelClassFeedFragmentList = new ArrayList<>();
        adapterFeedFragment = new AdapterFeedFragment(getContext(),modelClassFeedFragmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterFeedFragment);
        arcLoader = view.findViewById(R.id.arcLoader_FeedFragment);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        dbrPosts = mRootRef.child(NodeNames.POSTS);
        dbrUsers = mRootRef.child(NodeNames.USERS);
        arcLoader.start();

        dbrPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                       String postCreatorID = dataSnapshot.child(NodeNames.POST_CREATOR_ID).getValue().toString();

                        dbrUsers.child(postCreatorID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();

                                    if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                                        if(!snapshot.child(NodeNames.USER_PHOTO).equals("")){
                                            userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                        }
                                    }


                                    String postPhoto = "";
                                    String bloodGroup = "";
                                    String timeAgo = "";
                                    String district = "";
                                    String postDescription = "";
                                    String address = "";
                                    String love = "";
                                    String views = "";
                                    String postID = "";

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

                                    if(dataSnapshot.child(NodeNames.POST_VIEWS).getValue() != null){
                                        if(!dataSnapshot.child(NodeNames.POST_VIEWS).getValue().equals("")){
                                            views = dataSnapshot.child(NodeNames.POST_VIEWS).getValue().toString();
                                        }
                                    }

                                    if(dataSnapshot.child(NodeNames.AREA).getValue() != null){
                                        if(!dataSnapshot.child(NodeNames.AREA).getValue().equals("")){
                                            address = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                        }
                                    }

                                    if(dataSnapshot.child(NodeNames.POST_ID).getValue() != null){
                                        if(!dataSnapshot.child(NodeNames.POST_ID).getValue().equals("")){
                                            postID = dataSnapshot.child(NodeNames.POST_ID).getValue().toString();
                                        }
                                    }


                                    ModelClassFeedFragment object = new ModelClassFeedFragment(userName,userPhoto,bloodGroup,timeAgo,district,
                                            postDescription,postPhoto,address,love,views, ServerValue.TIMESTAMP.toString(),postCreatorID,postID);

                                    modelClassFeedFragmentList.add(object);
                                    adapterFeedFragment.notifyDataSetChanged();
                                    arcLoader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);


                                }
                                else{
                                    Toast.makeText(getContext(), getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), getString(R.string.canceled), Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                }
                else{
                    Toast.makeText(getContext(), "SnapShot Not Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
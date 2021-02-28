package com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleRequest extends AppCompatActivity {
    private TextView tv_bloodGroup,tv_cause,tv_district, tv_phone,tv_gender, tv_unitBag, tv_time,tv_userName, tv_BloodGroupInPost,
            tv_timeAgo,tv_Area, tv_postDescription;
    private ImageView iv_profilePicture, iv_postImage;
    private Button btn_acceptBtn, btn_rejectButton, btn_shareButton;
    private LinearLayout ll_acceptRejectHolder,ll_editDeleteHolder;
    String postCounterStr = "0";


    private DatabaseReference mRootRef;
    private String myUID, postCreatorID,postID;
    int postCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_request);

        tv_bloodGroup = findViewById(R.id.tv_bloodGroup_SingleRequestActivity);
        tv_cause = findViewById(R.id.tv_cause_SingleRequestActivity);
        tv_district = findViewById(R.id.tv_district_SingleRequestActivity);
        tv_phone = findViewById(R.id.tv_phoneNumber_SingleRequestActivity);
        tv_gender = findViewById(R.id.tv_gender_SingleRequestActivity);
        tv_unitBag = findViewById(R.id.tv_unitBag_SingleRequestActivity);
        tv_time = findViewById(R.id.tv_time_SingleRequestActivity);
        tv_userName = findViewById(R.id.tv_userName_SingleRequestActivity);
        tv_BloodGroupInPost = findViewById(R.id.tv_bloodGroupInPost_SingleRequestActivity);
        tv_timeAgo = findViewById(R.id.tv_timeAgo_SingleRequestActivity);
        tv_Area = findViewById(R.id.tv_area_SingleRequestActivity);
        tv_postDescription = findViewById(R.id.tv_postDescription_SingleRequestActivity);
        iv_profilePicture = findViewById(R.id.iv_profilePicture_SingleRequestActivity);
        iv_postImage = findViewById(R.id.iv_postImage_SingleRequestActivity);
        iv_postImage = findViewById(R.id.iv_postImage_SingleRequestActivity);
        btn_acceptBtn = findViewById(R.id.btn_acceptRequest_SingleRequestActivity);
        btn_rejectButton = findViewById(R.id.btn_rejectRequest_SingleRequestActivity);
        btn_shareButton = findViewById(R.id.btn_shareRequest_SingleRequestActivity);
        ll_acceptRejectHolder = findViewById(R.id.ll_acceptRejectRequestHolder_singleRequestActivity);
        ll_editDeleteHolder = findViewById(R.id.ll_editDeleteRequestHolder_singleRequestActivity);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();
        postCreatorID = getIntent().getStringExtra(Extras.POST_CREATOR_ID);
        postID = getIntent().getStringExtra(Extras.POST_ID);

        countView();
        loadPost();

        btn_acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();
            }
        });

        btn_rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();
            }
        });

        btn_shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareRequest();
            }
        });

        tv_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleRequest.this, UserProfile.class);
                intent.putExtra(Extras.USER_ID, postCreatorID);
                startActivity(intent);
            }
        });

        iv_profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleRequest.this, UserProfile.class);
                intent.putExtra(Extras.USER_ID, postCreatorID);
                startActivity(intent);
            }
        });




    }

    private void countView() {
        mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.POST_VIEWS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("tag","snapshot exists : "+snapshot.getValue().toString());
                    if(!snapshot.getValue().toString().equals("")){
                        Log.d("tag","snapshot if 1 : "+snapshot.getValue().toString());
                        postCounterStr = snapshot.getValue().toString();
                        Log.d("tag","if : "+postCounter);
                    }
                }

                postCounter = Integer.parseInt(postCounterStr);
                Log.d("tag","counterINT : "+postCounter);
                postCounter = postCounter+1;
                mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.POST_VIEWS).setValue(String.valueOf(postCounter))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID).child(postID)
                                            .child(NodeNames.POST_VIEWS).setValue(String.valueOf(postCounter)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                            else{

                                            }
                                        }
                                    });
                                }
                                else{

                                }
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadPost() {

        if(getIntent().hasExtra(Extras.POST_CREATOR_ID)){
            String postCreatorName = getIntent().getStringExtra(Extras.POST_CREATOR_NAME);
            String postCreatorPhoto = getIntent().getStringExtra(Extras.POST_CREATOR_PHOTO);
            String postDescription = getIntent().getStringExtra(Extras.POST_DESCRIPTION);
            String postImage = getIntent().getStringExtra(Extras.POST_IMAGE);
            String postLove = getIntent().getStringExtra(Extras.POST_LOVE);
            String postView = getIntent().getStringExtra(Extras.POST_VIEW);
            String bloodGroup = getIntent().getStringExtra(Extras.BLOOD_GROUP);
            String area = getIntent().getStringExtra(Extras.AREA);
            String district = getIntent().getStringExtra(Extras.DISTRICT);
            String accepted = getIntent().getStringExtra(Extras.ACCEPTED);
            String donated = getIntent().getStringExtra(Extras.DONATED);
            String timeStamp = getIntent().getStringExtra(Extras.TIMESTAMP);
            String cause = getIntent().getStringExtra(Extras.CAUSE);
            String gender = getIntent().getStringExtra(Extras.GENDER);
            String phoneNumber = getIntent().getStringExtra(Extras.PHONE_NUMBER);
            String unitBag = getIntent().getStringExtra(Extras.UNIT_BAGS);

            tv_bloodGroup.setText(bloodGroup);
            tv_BloodGroupInPost.setText(bloodGroup);
            tv_cause.setText(cause);
            tv_district.setText(district);
            tv_phone.setText(phoneNumber);
            tv_gender.setText(gender);
            tv_unitBag.setText(unitBag);
            tv_userName.setText(postCreatorName);
            tv_postDescription.setText(postDescription);
            tv_Area.setText(area);

            tv_timeAgo.setText(Util.getTimeAgo(Long.parseLong(timeStamp)));

            iv_profilePicture.setImageResource(R.drawable.ic_profile_picture);
            iv_postImage.setImageResource(R.drawable.donateblood_savelife_cover);

            Date date = new Date(Long.parseLong(timeStamp));
            DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate= dateFormater.format(date);
            tv_time.setText(currentDate);

            if(postCreatorID.equals(myUID)){
                ll_acceptRejectHolder.setVisibility(View.GONE);
                ll_editDeleteHolder.setVisibility(View.VISIBLE);
            }

        }
        else{
            mRootRef.child(NodeNames.POSTS).child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        String bloodGroup= "";
                        String cause= "";
                        String district = "";
                        String phoneNumber = "";
                        String gender = "";
                        String unitBag = "";
                        long timeStamp = 0;
                        String userName = "";
                        String area = "";
                        String postDescription = "";
                        String profileImage = "";
                        String postImage = "";

                        if(snapshot.child(NodeNames.BLOOD_GROUP).exists()){
                            if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().equals("")){
                                bloodGroup = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.CAUSE).exists()){
                            if(!snapshot.child(NodeNames.CAUSE).getValue().equals("")){
                                cause = snapshot.child(NodeNames.CAUSE).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.DISTRICT).exists()){
                            if(!snapshot.child(NodeNames.DISTRICT).getValue().equals("")){
                                district = snapshot.child(NodeNames.DISTRICT).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.PHONE_NUMBER).exists()){
                            if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().equals("")){
                                phoneNumber = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.GENDER).exists()){
                            if(!snapshot.child(NodeNames.GENDER).getValue().equals("")){
                                gender = snapshot.child(NodeNames.GENDER).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.UNIT_BAGS).exists()){
                            if(!snapshot.child(NodeNames.UNIT_BAGS).getValue().equals("")){
                                unitBag = snapshot.child(NodeNames.UNIT_BAGS).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.TIMESTAMP).exists()){
                            if(!snapshot.child(NodeNames.TIMESTAMP).getValue().equals("")){
                                timeStamp = (long) snapshot.child(NodeNames.TIMESTAMP).getValue();
                            }
                        }

                        if(snapshot.child(NodeNames.POST_CREATOR_NAME).exists()){
                            if(!snapshot.child(NodeNames.POST_CREATOR_NAME).getValue().equals("")){
                                userName = snapshot.child(NodeNames.POST_CREATOR_NAME).getValue().toString();
                            }
                        }


                        if(snapshot.child(NodeNames.AREA).exists()){
                            if(!snapshot.child(NodeNames.AREA).getValue().equals("")){
                                area = snapshot.child(NodeNames.AREA).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.POST_DESCRIPTION).exists()){
                            if(!snapshot.child(NodeNames.POST_DESCRIPTION).getValue().equals("")){
                                postDescription = snapshot.child(NodeNames.POST_DESCRIPTION).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.POST_CREATOR_PHOTO).exists()){
                            if(!snapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue().equals("")){
                                profileImage = snapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue().toString();
                            }
                        }

                        if(snapshot.child(NodeNames.POST_PHOTO).exists()){
                            if(!snapshot.child(NodeNames.POST_PHOTO).getValue().equals("")){
                                area = snapshot.child(NodeNames.POST_PHOTO).getValue().toString();
                            }
                        }


                        tv_bloodGroup.setText(bloodGroup);
                        tv_BloodGroupInPost.setText(bloodGroup);
                        tv_cause.setText(cause);
                        tv_district.setText(district);
                        tv_phone.setText(phoneNumber);
                        tv_gender.setText(gender);
                        tv_unitBag.setText(unitBag);
                        tv_userName.setText(userName);
                        tv_postDescription.setText(postDescription);
                        tv_Area.setText(area);

                        tv_timeAgo.setText(Util.getTimeAgo(timeStamp));

                        iv_profilePicture.setImageResource(R.drawable.ic_profile_picture);
                        iv_postImage.setImageResource(R.drawable.donateblood_savelife_cover);

                        Date date = new Date(timeStamp);
                        DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
                        String currentDate= dateFormater.format(date);
                        tv_time.setText(currentDate);


                    }else{
                        Toast.makeText(SingleRequest.this, "no post or user Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }







    private void acceptRequest() {
        Toast.makeText(this, "clicked accepted", Toast.LENGTH_SHORT).show();
    }




    private void rejectRequest() {
        Toast.makeText(this, "clicked rejected", Toast.LENGTH_SHORT).show();
    }





    private void shareRequest() {
        Toast.makeText(this, "clicked shared", Toast.LENGTH_SHORT).show();
    }
}
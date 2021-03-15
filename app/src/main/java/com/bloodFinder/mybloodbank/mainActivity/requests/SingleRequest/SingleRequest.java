package com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.mainActivity.chats.ChattingActivity.ChattingActivity;
import com.bloodFinder.mybloodbank.mainActivity.requests.editRequestPost.EditRequest;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SingleRequest extends AppCompatActivity {
    private TextView tv_bloodGroup,tv_cause,tv_district, tv_phone,tv_gender, tv_unitBag, tv_requiredDate,tv_userName, tv_BloodGroupInPost,
            tv_timeAgo,tv_Area, tv_postDescription,tv_love,tv_view;
    private String postCreatorName,postCreatorPhoto,postDescription,postImage,postLove,postView,bloodGroup,area,district,accepted,donated,
            timeStamp,cause,gender,phoneNumber,unitBag,requiredDate;
    private String acceptedFlag,loveFlag,completedFlag,post_Order;
    private ImageView iv_profilePicture;
    private ImageView iv_postImage;
    private ImageView iv_loveImage;
    private ImageView iv_map,iv_call,iv_chat;
    private Button btn_acceptBtn, btn_rejectButton, btn_shareButton,btn_editPost,btn_deletePost,btn_sharePost_OfEditDeleteHolder;
    private LinearLayout ll_acceptRejectHolder,ll_editDeleteHolder,ll_completedHolder;
    private CardView cv_postLoveHolder, cv_postViewHolder,cv_postShareHolder;
    private String postCounterStr = "0";
    private int acceptedRequestCount;
    private int loveCount;
    private DatabaseReference mRootRef;
    private String myUID, postCreatorID,postID;
    private int postCounter;

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
        tv_requiredDate = findViewById(R.id.tv_requiredDate_SingleRequestActivity);
        tv_userName = findViewById(R.id.tv_userName_SingleRequestActivity);
        tv_BloodGroupInPost = findViewById(R.id.tv_bloodGroupInPost_SingleRequestActivity);
        tv_timeAgo = findViewById(R.id.tv_timeAgo_SingleRequestActivity);
        tv_Area = findViewById(R.id.tv_area_SingleRequestActivity);
        tv_postDescription = findViewById(R.id.tv_postDescription_SingleRequestActivity);
        tv_love = findViewById(R.id.tv_loveValue_SingleRequestActivity);
        tv_view = findViewById(R.id.tv_viewValue_SingleRequestActivity);
        iv_profilePicture = findViewById(R.id.iv_profilePicture_SingleRequestActivity);
        iv_postImage = findViewById(R.id.iv_postImage_SingleRequestActivity);
        iv_postImage = findViewById(R.id.iv_postImage_SingleRequestActivity);
        iv_loveImage = findViewById(R.id.iv_loveImage_SingleRequestActivity);
        iv_call = findViewById(R.id.iv_call_SingleRequestActivity);
        iv_chat = findViewById(R.id.iv_chat_SingleRequestActivity);
        iv_map = findViewById(R.id.iv_map_SingleRequestActivity);
        cv_postLoveHolder = findViewById(R.id.cv_postLoveHolder_singleRequestActivity);
        cv_postViewHolder = findViewById(R.id.cv_postViewHolder_singleRequestActivity);
        cv_postShareHolder = findViewById(R.id.cv_postShareHolder_singleRequestActivity);
        btn_acceptBtn = findViewById(R.id.btn_acceptRequest_SingleRequestActivity);
        btn_rejectButton = findViewById(R.id.btn_rejectRequest_SingleRequestActivity);
        btn_shareButton = findViewById(R.id.btn_shareRequest_SingleRequestActivity);
        btn_editPost = findViewById(R.id.btn_edit_SingleRequestActivity);
        btn_deletePost = findViewById(R.id.btn_deleteRequest_SingleRequestActivity);
        btn_sharePost_OfEditDeleteHolder = findViewById(R.id.btn_shareEditDeleteHolder_SingleRequestActivity);
        ll_acceptRejectHolder = findViewById(R.id.ll_acceptRejectRequestHolder_singleRequestActivity);
        ll_editDeleteHolder = findViewById(R.id.ll_editDeleteRequestHolder_singleRequestActivity);
        ll_completedHolder = findViewById(R.id.ll_completedDeleteRequestHolder_singleRequestActivity);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();

        getIntentData();
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

        cv_postLoveHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLoveClicked();
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phn = phoneNumber;
                String number = "tel:"+phn;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(number));
                startActivity(intent);
            }
        });

        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleRequest.this, ChattingActivity.class);
                intent.putExtra(Extras.USER_ID,postCreatorID);
                intent.putExtra(Extras.USER_NAME,postCreatorName);
                intent.putExtra(Extras.USER_PHOTO,postCreatorPhoto);
                startActivity(intent);
            }
        });

        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleRequest.this, "map Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phn = phoneNumber;
                String number = "tel:"+phn;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(number));
                startActivity(intent);
            }
        });

        btn_editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditRequestActivity();
            }
        });



        btn_deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completePost();
            }
        });




    }




    private void getIntentData() {
        postCreatorID = getIntent().getStringExtra(Extras.POST_CREATOR_ID);
        postID = getIntent().getStringExtra(Extras.POST_ID);
        postCreatorName = getIntent().getStringExtra(Extras.POST_CREATOR_NAME);
        postCreatorPhoto = getIntent().getStringExtra(Extras.POST_CREATOR_PHOTO);
        postDescription = getIntent().getStringExtra(Extras.POST_DESCRIPTION);
        postImage = getIntent().getStringExtra(Extras.POST_IMAGE);
        postLove = getIntent().getStringExtra(Extras.POST_LOVE);
        postView = getIntent().getStringExtra(Extras.POST_VIEW);
        bloodGroup = getIntent().getStringExtra(Extras.BLOOD_GROUP);
        area = getIntent().getStringExtra(Extras.AREA);
        district = getIntent().getStringExtra(Extras.DISTRICT);
        accepted = getIntent().getStringExtra(Extras.ACCEPTED);
        donated = getIntent().getStringExtra(Extras.DONATED);
        timeStamp = getIntent().getStringExtra(Extras.TIMESTAMP);
        cause = getIntent().getStringExtra(Extras.CAUSE);
        gender = getIntent().getStringExtra(Extras.GENDER);
        phoneNumber = getIntent().getStringExtra(Extras.PHONE_NUMBER);
        unitBag = getIntent().getStringExtra(Extras.UNIT_BAGS);
        requiredDate = getIntent().getStringExtra(Extras.REQUIRED_DATE);
        post_Order = getIntent().getStringExtra(Extras.POST_ORDER);

        acceptedFlag = getIntent().getStringExtra(Extras.ACCEPTED_FLAG);
        loveFlag = getIntent().getStringExtra(Extras.LOVE_FLAG);
        completedFlag = getIntent().getStringExtra(Extras.COMPLETED_FLAG);

        acceptedRequestCount=Integer.parseInt(accepted);
        loveCount = Integer.parseInt(postLove);
    }


    private void countView() {
        mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.POST_VIEWS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(!snapshot.getValue().toString().equals("")){
                        postCounterStr = snapshot.getValue().toString();
                    }
                }

                postCounter = Integer.parseInt(postCounterStr);
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

        if (getIntent().hasExtra(Extras.POST_CREATOR_NAME)) {
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
            tv_love.setText(postLove);
            tv_view.setText(postView);

            tv_timeAgo.setText(Util.getTimeAgo(Long.parseLong(timeStamp)));

            Glide.with(SingleRequest.this).load(postCreatorPhoto).placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture).into(iv_profilePicture);

            Glide.with(SingleRequest.this).load(postImage).placeholder(R.drawable.featuredd)
                    .error(R.drawable.featuredd).into(iv_postImage);

            /*Date date = new Date(Long.parseLong(timeStamp));
            DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate= dateFormater.format(date);
            tv_requiredDate.setText(currentDate);*/
            tv_requiredDate.setText(requiredDate);



            if (loveFlag.equals(Constants.TRUE)) {
                iv_loveImage.setImageResource(R.drawable.ic_love_red);
            }

            if (postCreatorID.equals(myUID)) {
                if(completedFlag.equals(Constants.TRUE)){
                    ll_completedHolder.setVisibility(View.VISIBLE);
                    Log.d("tag","completed "+completedFlag);
                   /* ll_acceptRejectHolder.setVisibility(View.GONE);
                    ll_editDeleteHolder.setVisibility(View.GONE);*/
                }
                else{
                    ll_editDeleteHolder.setVisibility(View.VISIBLE);
                  /*  ll_completedHolder.setVisibility(View.GONE);
                    ll_acceptRejectHolder.setVisibility(View.GONE);*/
                }

            } else {
                ll_acceptRejectHolder.setVisibility(View.VISIBLE);
                if(acceptedFlag.equals(Constants.TRUE)){
                    btn_acceptBtn.setEnabled(false);
                    btn_acceptBtn.setText("accepted");
                }



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
                        String userPhoto = "";
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
                                userPhoto = snapshot.child(NodeNames.POST_CREATOR_PHOTO).getValue().toString();
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

                        Glide.with(SingleRequest.this).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                                .error(R.drawable.ic_profile_picture).into(iv_profilePicture);

                        Glide.with(SingleRequest.this).load(postImage).placeholder(R.drawable.featuredd)
                                .error(R.drawable.featuredd).into(iv_postImage);

                        /*Date date = new Date(timeStamp);
                        DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
                        String currentDate= dateFormater.format(date);
                        tv_time.setText(currentDate);*/


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
        btn_acceptBtn.setEnabled(false);
        acceptedRequestCount = acceptedRequestCount+1;
        accepted = String.valueOf(acceptedRequestCount);
        mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.ACCEPTED).setValue(String.valueOf(acceptedRequestCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.ACCEPTED_IDS_FOLDER).child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID).child(postID).child(NodeNames.ACCEPTED)
                                        .setValue(String.valueOf(acceptedRequestCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID).child(postID).child(NodeNames.ACCEPTED_IDS_FOLDER)
                                                    .child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Map<String,String > map = new HashMap<>();
                                                        map.put(NodeNames.POST_CREATOR_ID,postCreatorID);
                                                        map.put(NodeNames.POST_ID,postID);
                                                        map.put(NodeNames.TIMESTAMP,timeStamp);

                                                        mRootRef.child(NodeNames.ACCEPTED_REQUESTS).child(myUID).child(postID)
                                                                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    btn_rejectButton.setEnabled(true);
                                                                    Toast.makeText(SingleRequest.this, getString(R.string.accepted), Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    btn_acceptBtn.setEnabled(true);
                                                                    Toast.makeText(SingleRequest.this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else{

                                                    }
                                                }
                                            }) ;
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
                else{

                }
            }
        });
    }




    private void rejectRequest() {
        btn_rejectButton.setEnabled(false);
        //acceptedRequestCount = Integer.parseInt(accepted);
        acceptedRequestCount = acceptedRequestCount-1;
        accepted = String.valueOf(acceptedRequestCount);
        mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.ACCEPTED).setValue(String.valueOf(acceptedRequestCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.ACCEPTED_IDS_FOLDER).child(myUID).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID).child(postID).child(NodeNames.ACCEPTED)
                                        .setValue(String.valueOf(acceptedRequestCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID).child(postID).child(NodeNames.ACCEPTED_IDS_FOLDER)
                                                    .child(myUID).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        mRootRef.child(NodeNames.ACCEPTED_REQUESTS).child(myUID).child(postID)
                                                                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    btn_acceptBtn.setEnabled(true);
                                                                    Toast.makeText(SingleRequest.this, getString(R.string.rejected), Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    btn_rejectButton.setEnabled(true);
                                                                    Toast.makeText(SingleRequest.this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else{

                                                    }
                                                }
                                            }) ;
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
                else{

                }
            }
        });
    }





    private void shareRequest() {
        Toast.makeText(this, "clicked shared", Toast.LENGTH_SHORT).show();
    }



    private void postLoveClicked() {
        if(loveFlag.equals(Constants.TRUE)){
            iv_loveImage.setImageResource(R.drawable.ic_love);
            Log.d("tag","love count prev"+loveCount);
            loveCount = loveCount-1;
            postLove = String.valueOf(loveCount);
            Log.d("tag","love count"+loveCount);
            tv_love.setText(String.valueOf(loveCount));
            loveFlag=Constants.FALSE;
            mRootRef.child(NodeNames.POSTS).child(postID)
                    .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( task.isSuccessful()){
                        mRootRef.child(NodeNames.POSTS).child(postID)
                                .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID)
                                            .child(postID)
                                            .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID)
                                                        .child(postID)
                                                        .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                        }
                                                        else{
                                                            Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(SingleRequest.this, getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else{
                        Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        else{
            iv_loveImage.setImageResource(R.drawable.ic_love_red);
            Log.d("tag","love count prev"+loveCount);
            loveCount = loveCount+1;
            postLove = String.valueOf(loveCount);
            Log.d("tag","love count"+loveCount);
            tv_love.setText(String.valueOf(loveCount));
            loveFlag=Constants.TRUE;
            mRootRef.child(NodeNames.POSTS).child(postID)
                    .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( task.isSuccessful()){
                        mRootRef.child(NodeNames.POSTS).child(postID)
                                .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID)
                                            .child(postID)
                                            .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(postCreatorID)
                                                        .child(postID)
                                                        .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                        }
                                                        else{
                                                            Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(SingleRequest.this,getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SingleRequest.this,getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }








    private void goToEditRequestActivity() {
        Intent intent = new Intent(SingleRequest.this, EditRequest.class);
        intent.putExtra(Extras.POST_CREATOR_ID,postCreatorID);
        intent.putExtra(Extras.POST_CREATOR_PHOTO,postCreatorPhoto);
        intent.putExtra(Extras.POST_CREATOR_NAME,postCreatorName);
        intent.putExtra(Extras.POST_ID,postID);
        intent.putExtra(Extras.POST_DESCRIPTION,postDescription);
        intent.putExtra(Extras.POST_IMAGE,postImage);
        intent.putExtra(Extras.POST_LOVE,postLove);
        intent.putExtra(Extras.POST_VIEW,postView);
        intent.putExtra(Extras.BLOOD_GROUP,bloodGroup);
        intent.putExtra(Extras.AREA,area);
        intent.putExtra(Extras.DISTRICT,district);
        intent.putExtra(Extras.ACCEPTED,accepted);
        intent.putExtra(Extras.DONATED,donated);
        intent.putExtra(Extras.TIMESTAMP,timeStamp);
        intent.putExtra(Extras.CAUSE,cause);
        intent.putExtra(Extras.GENDER,gender);
        intent.putExtra(Extras.PHONE_NUMBER,phoneNumber);
        intent.putExtra(Extras.UNIT_BAGS,unitBag);
        intent.putExtra(Extras.REQUIRED_DATE,requiredDate);
        intent.putExtra(Extras.ACCEPTED_FLAG,acceptedFlag);
        intent.putExtra(Extras.LOVE_FLAG,loveFlag);
        startActivity(intent);
    }







    private void completePost() {
            mRootRef.child(NodeNames.POSTS).child(postID).child(NodeNames.COMPLETED_FLAG).setValue(Constants.TRUE)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ll_editDeleteHolder.setVisibility(View.GONE);
                                ll_completedHolder.setVisibility(View.VISIBLE);
                                Toast.makeText(SingleRequest.this, "Completed", Toast.LENGTH_SHORT).show();;
                            }
                            else{

                            }

                        }
                    });
    }






}



package com.bloodFinder.mybloodbank.mainActivity.feed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest.SingleRequest;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdapterFeedFragment extends RecyclerView.Adapter<AdapterFeedFragment.MyViewHolder> {
    private Context context;
    private List<ModelClassFeedFragment> modelClassFeedFragmentList;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private String myUID = FirebaseAuth.getInstance().getUid();
    private int loveCount,viewCount;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    public AdapterFeedFragment(Context context, List<ModelClassFeedFragment> modelClassFeedFragmentList) {
        this.context = context;
        this.modelClassFeedFragmentList = modelClassFeedFragmentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_feed_fragment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(modelClassFeedFragmentList.get(position).getPostCreatorName());
        holder.bloodGroup.setText(modelClassFeedFragmentList.get(position).getBloodGroup());
        holder.area.setText(modelClassFeedFragmentList.get(position).getArea());
        holder.description.setText(modelClassFeedFragmentList.get(position).getPostDescription());
        //holder.love.setText(modelClassFeedFragmentList.get(position).getLove());
        //holder.views.setText(modelClassFeedFragmentList.get(position).getViews());
        holder.love.setText(modelClassFeedFragmentList.get(position).getPostLove());
        holder.views.setText(modelClassFeedFragmentList.get(position).getPostView());
        holder.bloodGroup2.setText(modelClassFeedFragmentList.get(position).getBloodGroup());
        holder.district.setText(modelClassFeedFragmentList.get(position).getDistrict());
        holder.postOrder.setText(modelClassFeedFragmentList.get(position).getPost_order());

        holder.timeAgo.setText(Util.getTimeAgo(Long.parseLong(modelClassFeedFragmentList.get(position).getTimeStamp())));

        if(modelClassFeedFragmentList.get(position).getLoveCheckerFlag().equals(Constants.TRUE)){
            holder.iv_loveImage.setImageResource(R.drawable.ic_love_red);
        }

        Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostCreatorPhoto())
                .placeholder(R.drawable.ic_profile_picture).error(R.drawable.ic_profile_picture).into(holder.iv_profilePicture);



        Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostImage()).placeholder(R.drawable.featuredd)
                .error(R.drawable.featuredd).into(holder.postImage);




        //CLick Methods
        holder.iv_profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);
            }
        });


        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SingleRequest.class);
                intent.putExtra(Extras.POST_CREATOR_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                intent.putExtra(Extras.POST_CREATOR_PHOTO,modelClassFeedFragmentList.get(position).getPostCreatorPhoto());
                intent.putExtra(Extras.POST_CREATOR_NAME,modelClassFeedFragmentList.get(position).getPostCreatorName());
                intent.putExtra(Extras.POST_ID,modelClassFeedFragmentList.get(position).getPostID());
                intent.putExtra(Extras.POST_DESCRIPTION,modelClassFeedFragmentList.get(position).getPostDescription());
                intent.putExtra(Extras.POST_IMAGE,modelClassFeedFragmentList.get(position).getPostImage());
                intent.putExtra(Extras.POST_LOVE,modelClassFeedFragmentList.get(position).getPostLove());
                intent.putExtra(Extras.POST_VIEW,modelClassFeedFragmentList.get(position).getPostView());
                intent.putExtra(Extras.BLOOD_GROUP,modelClassFeedFragmentList.get(position).getBloodGroup());
                intent.putExtra(Extras.AREA,modelClassFeedFragmentList.get(position).getArea());
                intent.putExtra(Extras.DISTRICT,modelClassFeedFragmentList.get(position).getDistrict());
                intent.putExtra(Extras.ACCEPTED,modelClassFeedFragmentList.get(position).getAccepted());
                intent.putExtra(Extras.DONATED,modelClassFeedFragmentList.get(position).getDonated());
                intent.putExtra(Extras.TIMESTAMP,modelClassFeedFragmentList.get(position).getTimeStamp());
                intent.putExtra(Extras.CAUSE,modelClassFeedFragmentList.get(position).getCause());
                intent.putExtra(Extras.GENDER,modelClassFeedFragmentList.get(position).getGender());
                intent.putExtra(Extras.PHONE_NUMBER,modelClassFeedFragmentList.get(position).getPhone());
                intent.putExtra(Extras.UNIT_BAGS,modelClassFeedFragmentList.get(position).getUnitBag());
                intent.putExtra(Extras.REQUIRED_DATE,modelClassFeedFragmentList.get(position).getRequiredDate());
                intent.putExtra(Extras.ACCEPTED_FLAG,modelClassFeedFragmentList.get(position).getAcceptedFlag());
                intent.putExtra(Extras.LOVE_FLAG,modelClassFeedFragmentList.get(position).getLoveCheckerFlag());
                intent.putExtra(Extras.COMPLETED_FLAG,modelClassFeedFragmentList.get(position).getCompletedFlag());
                intent.putExtra(Extras.POST_ORDER,modelClassFeedFragmentList.get(position).getPost_order());
                context.startActivity(intent);
            }
        });






        holder.cv_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelClassFeedFragmentList.get(position).getLoveCheckerFlag().equals(Constants.TRUE)){
                    loveCount = Integer.parseInt(modelClassFeedFragmentList.get(position).getPostLove());
                    loveCount = loveCount-1;
                    modelClassFeedFragmentList.get(position).setPostLove(String.valueOf(loveCount));
                    modelClassFeedFragmentList.get(position).setLoveCheckerFlag(Constants.FALSE);
                    holder.iv_loveImage.setImageResource(R.drawable.ic_love);
                        holder.love.setText(String.valueOf(loveCount));
                        mRootRef.child(NodeNames.POSTS).child(modelClassFeedFragmentList.get(position).getPostID())
                                .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if( task.isSuccessful()){
                                    mRootRef.child(NodeNames.POSTS).child(modelClassFeedFragmentList.get(position).getPostID())
                                            .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(modelClassFeedFragmentList.get(position).getPostCreatorID())
                                                        .child(modelClassFeedFragmentList.get(position).getPostID())
                                                        .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(modelClassFeedFragmentList.get(position).getPostCreatorID())
                                                                    .child(modelClassFeedFragmentList.get(position).getPostID())
                                                                    .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){

                                                                    }
                                                                    else{
                                                                        Toast.makeText(context, context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        else{
                                                            Toast.makeText(context, context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                            else{
                                                Toast.makeText(context, context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(context,context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                }

                else{
                    loveCount = Integer.parseInt(modelClassFeedFragmentList.get(position).getPostLove());
                    loveCount = loveCount+1;
                    modelClassFeedFragmentList.get(position).setPostLove(String.valueOf(loveCount));
                    modelClassFeedFragmentList.get(position).setLoveCheckerFlag(Constants.TRUE);
                    holder.iv_loveImage.setImageResource(R.drawable.ic_love_red);
                    holder.love.setText(String.valueOf(loveCount));
                    mRootRef.child(NodeNames.POSTS).child(modelClassFeedFragmentList.get(position).getPostID())
                            .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful()){
                                mRootRef.child(NodeNames.POSTS).child(modelClassFeedFragmentList.get(position).getPostID())
                                        .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(modelClassFeedFragmentList.get(position).getPostCreatorID())
                                                    .child(modelClassFeedFragmentList.get(position).getPostID())
                                                    .child(NodeNames.LOVE_REACT_COUNT_FOLDER).child(myUID).setValue(myUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        mRootRef.child(NodeNames.POSTS_ORDER_BY_USER).child(modelClassFeedFragmentList.get(position).getPostCreatorID())
                                                                .child(modelClassFeedFragmentList.get(position).getPostID())
                                                                .child(NodeNames.POST_LOVE).setValue(String.valueOf(loveCount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){

                                                                }
                                                                else{
                                                                    Toast.makeText(context,context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        Toast.makeText(context,context.getString(R.string.snapshotDoesnotExists), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                        else {
                                            Toast.makeText(context,context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(context,context.getString(R.string.failedToInteractWithPost), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return modelClassFeedFragmentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_profilePicture;
        private ImageView postImage,iv_loveImage;
        private TextView userName;
        private TextView bloodGroup;
        private TextView timeAgo;
        private TextView district;
        private TextView bloodGroup2;
        private TextView area;
        private TextView description;
        private TextView love;
        private TextView views;
        private TextView postOrder;
        private CardView cv_love;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profilePicture = itemView.findViewById(R.id.profilePicture_sampleFeedFragment);
            postImage = itemView.findViewById(R.id.iv_postImage_SampleFeedFragment);
            iv_loveImage = itemView.findViewById(R.id.iv_love_SampleFeedFragment);
            cv_love = itemView.findViewById(R.id.cv_love_SampleFeedFragment);
            userName = itemView.findViewById(R.id.tv_userName_sampleFeedFragment);
            bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleFeedFragment);
            timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleFeedFragment);
            district = itemView.findViewById(R.id.tv_district_sampleFeedFragment);
            area = itemView.findViewById(R.id.tv_address_sampleFeedFragment);
            bloodGroup2 = itemView.findViewById(R.id.tv_bloodGroup2_sampleFeedFragment);
            love = itemView.findViewById(R.id.tv_loveValue_SampleFeedFragment);
            views = itemView.findViewById(R.id.tv_viewValue_SampleFeedFragment);
            description = itemView.findViewById(R.id.tv_description_sampleFeedFragment);
            postOrder = itemView.findViewById(R.id.tv_postOrder_sampleFeedFragment);
        }
    }
}

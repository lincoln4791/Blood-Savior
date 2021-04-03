package com.bloodFinder.bloodSavior.feed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Constants;
import com.bloodFinder.bloodSavior.common.Extras;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.databinding.SampleFeedFragmentBinding;
import com.bloodFinder.bloodSavior.posts.SingleRequest.SingleRequest;
import com.bloodFinder.bloodSavior.userProfile.userProfile.UserProfile;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        SampleFeedFragmentBinding sampleFeedFragmentBinding= SampleFeedFragmentBinding.inflate(inflater,parent,false);
        return new MyViewHolder(sampleFeedFragmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.sampleFeedFragmentBinding.tvTimeAgoSampleFeedFragment.setText(Util.getTimeAgo(Long.parseLong(modelClassFeedFragmentList.get(position).getTimeAgo())));
        holder.sampleFeedFragmentBinding.setObjectFeedFragment(modelClassFeedFragmentList.get(position));

        if(modelClassFeedFragmentList.get(position).getLoveCheckerFlag().equals(Constants.TRUE)){
            holder.sampleFeedFragmentBinding.ivLoveSampleFeedFragment.setImageResource(R.drawable.ic_love_red);
        }

      /*  Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostCreatorPhoto())
                .placeholder(R.drawable.ic_profile_picture).error(R.drawable.ic_profile_picture).into(holder.iv_profilePicture);



        Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostImage()).placeholder(R.drawable.featuredd)
                .error(R.drawable.featuredd).into(holder.postImage);*/




        //CLick Methods
        holder.sampleFeedFragmentBinding.profilePictureSampleFeedFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);
            }
        });

        holder.sampleFeedFragmentBinding.tvUserNameSampleFeedFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);
            }
        });


        holder.sampleFeedFragmentBinding.cvPostImageSampleFeedFragment.setOnClickListener(new View.OnClickListener() {
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






        holder.sampleFeedFragmentBinding.cvLoveSampleFeedFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelClassFeedFragmentList.get(position).getLoveCheckerFlag().equals(Constants.TRUE)){
                    loveCount = Integer.parseInt(modelClassFeedFragmentList.get(position).getPostLove());
                    loveCount = loveCount-1;
                    modelClassFeedFragmentList.get(position).setPostLove(String.valueOf(loveCount));
                    modelClassFeedFragmentList.get(position).setLoveCheckerFlag(Constants.FALSE);
                    holder.sampleFeedFragmentBinding.ivLoveSampleFeedFragment.setImageResource(R.drawable.ic_love);
                        holder.sampleFeedFragmentBinding.tvLoveValueSampleFeedFragment.setText(String.valueOf(loveCount));
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
                    holder.sampleFeedFragmentBinding.ivLoveSampleFeedFragment.setImageResource(R.drawable.ic_love_red);
                    holder.sampleFeedFragmentBinding.tvLoveValueSampleFeedFragment.setText(String.valueOf(loveCount));
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





        holder.sampleFeedFragmentBinding.cvShareSampleFeedFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject= "Blood Savior";
                String bloodGroup = modelClassFeedFragmentList.get(position).getBloodGroup();
                String district = modelClassFeedFragmentList.get(position).getDistrict();
                String area = modelClassFeedFragmentList.get(position).getArea();
                String phoneNumber = modelClassFeedFragmentList.get(position).getPhone();
                String postCreatorName = modelClassFeedFragmentList.get(position).getPostCreatorName();
                String body = postCreatorName+" এর রক্তের প্রয়োজন, রক্তের গ্রুপ "+bloodGroup+", স্থান "+area+" , "+district+"। যোগাযোগ : "+phoneNumber+
                        " আপনার ১ ব্যাগ রক্ত বাচিয়ে দিতে পারে একটি প্রান। -Blood Savior, রক্তের সন্ধানে সর্বদা জাগ্রত, Now Available on play store.";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                intent.putExtra(Intent.EXTRA_EMAIL,"dev.lincoln47@gmail.com");
                intent.putExtra(Intent.EXTRA_PHONE_NUMBER,modelClassFeedFragmentList.get(position).getPhone());
                context.startActivity(Intent.createChooser(intent,"share with "));
            }
        });




    }

    @Override
    public int getItemCount() {
        return modelClassFeedFragmentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SampleFeedFragmentBinding sampleFeedFragmentBinding;
      /*  private ImageView iv_profilePicture;
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
        private CardView cv_love,cv_share;*/
        public MyViewHolder(@NonNull SampleFeedFragmentBinding sampleFeedFragmentBinding) {
            super(sampleFeedFragmentBinding.getRoot());
            this.sampleFeedFragmentBinding =sampleFeedFragmentBinding;
          /*  iv_profilePicture = itemView.findViewById(R.id.profilePicture_sampleFeedFragment);
            postImage = itemView.findViewById(R.id.iv_postImage_SampleFeedFragment);
            iv_loveImage = itemView.findViewById(R.id.iv_love_SampleFeedFragment);
            cv_love = itemView.findViewById(R.id.cv_love_SampleFeedFragment);
            cv_share = itemView.findViewById(R.id.cv_share_SampleFeedFragment);
            userName = itemView.findViewById(R.id.tv_userName_sampleFeedFragment);
            bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleFeedFragment);
            timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleFeedFragment);
            district = itemView.findViewById(R.id.tv_district_sampleFeedFragment);
            area = itemView.findViewById(R.id.tv_address_sampleFeedFragment);
            bloodGroup2 = itemView.findViewById(R.id.tv_bloodGroup2_sampleFeedFragment);
            love = itemView.findViewById(R.id.tv_loveValue_SampleFeedFragment);
            views = itemView.findViewById(R.id.tv_viewValue_SampleFeedFragment);
            description = itemView.findViewById(R.id.tv_description_sampleFeedFragment);
            postOrder = itemView.findViewById(R.id.tv_postOrder_sampleFeedFragment);*/
        }
    }
}

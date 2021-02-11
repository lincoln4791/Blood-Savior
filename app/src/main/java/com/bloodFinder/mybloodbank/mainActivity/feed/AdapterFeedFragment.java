package com.bloodFinder.mybloodbank.mainActivity.feed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.mainActivity.MainActivity;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterFeedFragment extends RecyclerView.Adapter<AdapterFeedFragment.MyViewHolder> {
    private Context context;
    private List<ModelClassFeedFragment> modelClassFeedFragmentList;

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
        holder.love.setText("10 Loves");
        holder.views.setText("214 Views");
        holder.bloodGroup2.setText(modelClassFeedFragmentList.get(position).getBloodGroup());
        holder.district.setText(modelClassFeedFragmentList.get(position).getDistrict());
        Log.d("tag","size: "+modelClassFeedFragmentList.size());

        holder.timeAgo.setText("24 minutes ago");


        Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostCreatorPhoto()).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(holder.profilePicture);

        Glide.with(context).load(modelClassFeedFragmentList.get(position).getPostImage()).placeholder(R.drawable.featuredd)
                .error(R.drawable.featuredd).into(holder.postImage);






        //CLick Methods
        holder.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassFeedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);

            }
        });





    }

    @Override
    public int getItemCount() {
        return modelClassFeedFragmentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicture;
        private ImageView postImage;
        private TextView userName;
        private TextView bloodGroup;
        private TextView timeAgo;
        private TextView district;
        private TextView bloodGroup2;
        private TextView area;
        private TextView description;
        private TextView love;
        private TextView views;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture_sampleFeedFragment);
            postImage = itemView.findViewById(R.id.iv_postImage_SampleFeedFragment);
            userName = itemView.findViewById(R.id.tv_userName_sampleFeedFragment);
            bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleFeedFragment);
            timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleFeedFragment);
            district = itemView.findViewById(R.id.tv_district_sampleFeedFragment);
            area = itemView.findViewById(R.id.tv_address_sampleFeedFragment);
            bloodGroup2 = itemView.findViewById(R.id.bloodGroup2_sampleFeedFragment);
            love = itemView.findViewById(R.id.tv_loveValue_SampleFeedFragment);
            views = itemView.findViewById(R.id.tv_viewValue_SampleFeedFragment);
            description = itemView.findViewById(R.id.tv_description_sampleFeedFragment);
        }
    }
}

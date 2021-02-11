package com.bloodFinder.mybloodbank.userProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.mainActivity.feed.AdapterFeedFragment;
import com.bloodFinder.mybloodbank.mainActivity.feed.ModelClassFeedFragment;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterUserProfile extends RecyclerView.Adapter<AdapterUserProfile.MyViewJHolder> {

    private Context context;
    private List<ModelClassUserProfile> modelClassUserProfileList;

    public AdapterUserProfile(Context context, List<ModelClassUserProfile> modelClassUserProfileList) {
        this.context = context;
        this.modelClassUserProfileList = modelClassUserProfileList;
    }

    @NonNull
    @Override
    public MyViewJHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_feed_fragment,parent,false);
        return new MyViewJHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewJHolder holder, int position) {

        holder.userName.setText(modelClassUserProfileList.get(position).getPostCreatorName());
        holder.bloodGroup.setText(modelClassUserProfileList.get(position).getBloodGroup());
        holder.area.setText(modelClassUserProfileList.get(position).getArea());
        holder.description.setText(modelClassUserProfileList.get(position).getPostDescription());
        //holder.love.setText(modelClassFeedFragmentList.get(position).getLove());
        //holder.views.setText(modelClassFeedFragmentList.get(position).getViews());
        holder.love.setText("10 Loves");
        holder.views.setText("214 Views");
        holder.bloodGroup2.setText(modelClassUserProfileList.get(position).getBloodGroup());
        holder.district.setText(modelClassUserProfileList.get(position).getDistrict());

        holder.timeAgo.setText("24 minutes ago");


        Glide.with(context).load(modelClassUserProfileList.get(position).getPostCreatorPhoto()).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(holder.profilePicture);

        Glide.with(context).load(modelClassUserProfileList.get(position).getPostImage()).placeholder(R.drawable.featuredd)
                .error(R.drawable.featuredd).into(holder.postImage);

    }

    @Override
    public int getItemCount() {
        return modelClassUserProfileList.size();
    }

    public class MyViewJHolder extends RecyclerView.ViewHolder {
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
        public MyViewJHolder(@NonNull View itemView) {
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

package com.bloodFinder.mybloodbank.userProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.mainActivity.feed.AdapterFeedFragment;
import com.bloodFinder.mybloodbank.mainActivity.feed.ModelClassFeedFragment;
import com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest.SingleRequest;
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
        View view= LayoutInflater.from(context).inflate(R.layout.sample_myrequests_fragment,parent,false);
        return new MyViewJHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewJHolder holder, int position) {

        holder.tv_bloodGroup.setText(modelClassUserProfileList.get(position).getBloodGroup());
        holder.tv_area.setText(modelClassUserProfileList.get(position).getArea());
        holder.tv_district.setText(modelClassUserProfileList.get(position).getDistrict());
        holder.tv_accepted.setText(modelClassUserProfileList.get(position).getAccepted());
        holder.tv_donated.setText(modelClassUserProfileList.get(position).getDonated());

        String timeAgo = Util.getTimeAgo(Long.parseLong(modelClassUserProfileList.get(position).getTimeStamp()));
        holder.tv_timeAgo.setText(timeAgo);

        holder.cv_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleRequest.class);
                intent.putExtra(Extras.POST_CREATOR_ID,modelClassUserProfileList.get(position).getPostCreatorID());
                intent.putExtra(Extras.POST_CREATOR_PHOTO,modelClassUserProfileList.get(position).getPostCreatorPhoto());
                intent.putExtra(Extras.POST_CREATOR_NAME,modelClassUserProfileList.get(position).getPostCreatorName());
                intent.putExtra(Extras.POST_ID,modelClassUserProfileList.get(position).getPostID());
                intent.putExtra(Extras.POST_DESCRIPTION,modelClassUserProfileList.get(position).getPostDescription());
                intent.putExtra(Extras.POST_IMAGE,modelClassUserProfileList.get(position).getPostImage());
                intent.putExtra(Extras.POST_LOVE,modelClassUserProfileList.get(position).getPostLove());
                intent.putExtra(Extras.POST_VIEW,modelClassUserProfileList.get(position).getPostView());
                intent.putExtra(Extras.BLOOD_GROUP,modelClassUserProfileList.get(position).getBloodGroup());
                intent.putExtra(Extras.AREA,modelClassUserProfileList.get(position).getArea());
                intent.putExtra(Extras.DISTRICT,modelClassUserProfileList.get(position).getDistrict());
                intent.putExtra(Extras.ACCEPTED,modelClassUserProfileList.get(position).getAccepted());
                intent.putExtra(Extras.DONATED,modelClassUserProfileList.get(position).getDonated());
                intent.putExtra(Extras.TIMESTAMP,modelClassUserProfileList.get(position).getTimeStamp());
                intent.putExtra(Extras.CAUSE,modelClassUserProfileList.get(position).getCause());
                intent.putExtra(Extras.GENDER,modelClassUserProfileList.get(position).getGender());
                intent.putExtra(Extras.PHONE_NUMBER,modelClassUserProfileList.get(position).getPhone());
                intent.putExtra(Extras.UNIT_BAGS,modelClassUserProfileList.get(position).getUnitBag());
                intent.putExtra(Extras.REQUIRED_DATE,modelClassUserProfileList.get(position).getRequiredDate());
                intent.putExtra(Extras.ACCEPTED_FLAG,modelClassUserProfileList.get(position).getAcceptedFlag());
                intent.putExtra(Extras.LOVE_FLAG,modelClassUserProfileList.get(position).getLoveCheckerFlag());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassUserProfileList.size();
    }

    public class MyViewJHolder extends RecyclerView.ViewHolder {
        private TextView tv_bloodGroup;
        private TextView tv_area;
        private TextView tv_district;
        private TextView tv_timeAgo;
        private TextView tv_accepted;
        private TextView tv_donated;
        private CardView cv_holder;
        public MyViewJHolder(@NonNull View itemView) {
            super(itemView);
            tv_bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleMyRequestsFragment);
            tv_area = itemView.findViewById(R.id.tv_area_sampleMyRequestsFragment);
            tv_district = itemView.findViewById(R.id.tv_district_sampleMyRequestsFragment);
            tv_timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleMyRequestsFragment);
            tv_accepted = itemView.findViewById(R.id.tv_acceptedValue_sampleMyRequestFragment);
            tv_donated = itemView.findViewById(R.id.tv_donatedValue_sampleMyRequestsFragment);
            cv_holder = itemView.findViewById(R.id.cv_holder_sampleMyRequestsFragment);
        }
    }
}

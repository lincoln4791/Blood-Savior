package com.bloodFinder.bloodSavior.posts.AcceptedFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Constants;
import com.bloodFinder.bloodSavior.common.Extras;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.chats.ChattingActivity.ChattingActivity;
import com.bloodFinder.bloodSavior.posts.SingleRequest.SingleRequest;
import com.bloodFinder.bloodSavior.userProfile.userProfile.UserProfile;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterAcceptedFragment extends RecyclerView.Adapter<AdapterAcceptedFragment.MyVIewHolder> {
    private List<ModelClassAcceptedFragment> modelClassAcceptedFragmentList;
    private Context context;

    public AdapterAcceptedFragment(List<ModelClassAcceptedFragment> modelClassAcceptedFragmentList, Context context) {
        this.modelClassAcceptedFragmentList = modelClassAcceptedFragmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_accepted_requests,parent,false);
        return new MyVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVIewHolder holder, int position) {
        holder.tv_userName.setText(modelClassAcceptedFragmentList.get(position).getPostCreatorName());
        holder.tv_district.setText(modelClassAcceptedFragmentList.get(position).getDistrict());
        holder.tv_area.setText(modelClassAcceptedFragmentList.get(position).getArea());
        holder.tv_bloodGroup.setText(modelClassAcceptedFragmentList.get(position).getBloodGroup());
        holder.tv_timeAgo.setText(Util.getTimeAgo(Long.parseLong(modelClassAcceptedFragmentList.get(position).getTimeStamp())));


        if(modelClassAcceptedFragmentList.get(position).getCompletedFlag().equals(Constants.TRUE)){
            holder.iv_completedSign.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(modelClassAcceptedFragmentList.get(position).getPostCreatorPhoto())
                .error(R.drawable.ic_profile_picture).placeholder(R.drawable.ic_profile_picture).into(holder.iv_profilePicture);

        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phn = modelClassAcceptedFragmentList.get(position).getPhone();
                String number = "tel:"+phn;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(number));
                context.startActivity(intent);
            }
        });

        holder.btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra(Extras.USER_ID,modelClassAcceptedFragmentList.get(position).getPostCreatorID());
                intent.putExtra(Extras.USER_NAME,modelClassAcceptedFragmentList.get(position).getPostCreatorName());
                intent.putExtra(Extras.USER_PHOTO,modelClassAcceptedFragmentList.get(position).getPostCreatorPhoto());
                context.startActivity(intent);
            }
        });


        holder.btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "map CLicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cv_postHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSingleRequestActivity(position);
            }
        });

        holder.cv_profilePictureHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                intent.putExtra(Extras.USER_ID,modelClassAcceptedFragmentList.get(position).getPostCreatorID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassAcceptedFragmentList.size();
    }

    public class MyVIewHolder extends RecyclerView.ViewHolder {
        private TextView tv_userName,tv_area,tv_district,tv_timeAgo,tv_bloodGroup;
        private ImageView iv_profilePicture,iv_completedSign;
        private Button btn_call, btn_chat, btn_map;
        private CardView cv_postHolder,cv_profilePictureHolder;
        public MyVIewHolder(@NonNull View itemView) {
            super(itemView);

            tv_userName = itemView.findViewById(R.id.tv_userName_sampleAcceptedFragment);
            tv_area = itemView.findViewById(R.id.tv_area_sampleAcceptedFragment);
            tv_district = itemView.findViewById(R.id.tv_district_sampleAcceptedFragment);
            tv_timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleAcceptedFragment);
            tv_bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleAcceptedFragment);
            iv_profilePicture = itemView.findViewById(R.id.iv_profilePicture_sampleAcceptedFragment);
            iv_completedSign = itemView.findViewById(R.id.iv_completed_sampleMyAcceptedFragment);
            btn_call = itemView.findViewById(R.id.btn_call_sampleAcceptedFragment);
            btn_chat = itemView.findViewById(R.id.btn_chat_sampleAcceptedFragment);
            btn_map = itemView.findViewById(R.id.btn_map_sampleAcceptedFragment);
            cv_postHolder = itemView.findViewById(R.id.cv_postHolder_sampleAcceptedFragment);
            cv_profilePictureHolder = itemView.findViewById(R.id.cv_profilePictureHolder_sampleAcceptedFragment);

        }
    }

    private void goToSingleRequestActivity(int position){
        Intent intent = new Intent(context, SingleRequest.class);
        intent.putExtra(Extras.POST_CREATOR_ID,modelClassAcceptedFragmentList.get(position).getPostCreatorID());
        intent.putExtra(Extras.POST_CREATOR_PHOTO,modelClassAcceptedFragmentList.get(position).getPostCreatorPhoto());
        intent.putExtra(Extras.POST_CREATOR_NAME,modelClassAcceptedFragmentList.get(position).getPostCreatorName());
        intent.putExtra(Extras.POST_ID,modelClassAcceptedFragmentList.get(position).getPostID());
        intent.putExtra(Extras.POST_DESCRIPTION,modelClassAcceptedFragmentList.get(position).getPostDescription());
        intent.putExtra(Extras.POST_IMAGE,modelClassAcceptedFragmentList.get(position).getPostImage());
        intent.putExtra(Extras.POST_LOVE,modelClassAcceptedFragmentList.get(position).getPostLove());
        intent.putExtra(Extras.POST_VIEW,modelClassAcceptedFragmentList.get(position).getPostView());
        intent.putExtra(Extras.BLOOD_GROUP,modelClassAcceptedFragmentList.get(position).getBloodGroup());
        intent.putExtra(Extras.AREA,modelClassAcceptedFragmentList.get(position).getArea());
        intent.putExtra(Extras.DISTRICT,modelClassAcceptedFragmentList.get(position).getDistrict());
        intent.putExtra(Extras.ACCEPTED,modelClassAcceptedFragmentList.get(position).getAccepted());
        intent.putExtra(Extras.DONATED,modelClassAcceptedFragmentList.get(position).getDonated());
        intent.putExtra(Extras.TIMESTAMP,modelClassAcceptedFragmentList.get(position).getTimeStamp());
        intent.putExtra(Extras.CAUSE,modelClassAcceptedFragmentList.get(position).getCause());
        intent.putExtra(Extras.GENDER,modelClassAcceptedFragmentList.get(position).getGender());
        intent.putExtra(Extras.PHONE_NUMBER,modelClassAcceptedFragmentList.get(position).getPhone());
        intent.putExtra(Extras.UNIT_BAGS,modelClassAcceptedFragmentList.get(position).getUnitBag());
        intent.putExtra(Extras.REQUIRED_DATE,modelClassAcceptedFragmentList.get(position).getRequiredDate());
        intent.putExtra(Extras.ACCEPTED_FLAG,modelClassAcceptedFragmentList.get(position).getAcceptedFlag());
        intent.putExtra(Extras.LOVE_FLAG,modelClassAcceptedFragmentList.get(position).getLoveCheckerFlag());
        intent.putExtra(Extras.COMPLETED_FLAG,modelClassAcceptedFragmentList.get(position).getCompletedFlag());
        intent.putExtra(Extras.POST_ORDER,modelClassAcceptedFragmentList.get(position).getPost_order());
        context.startActivity(intent);
    }
}

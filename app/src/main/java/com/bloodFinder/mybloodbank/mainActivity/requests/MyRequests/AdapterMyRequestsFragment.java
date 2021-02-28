package com.bloodFinder.mybloodbank.mainActivity.requests.MyRequests;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.Util;
import com.bloodFinder.mybloodbank.mainActivity.requests.SingleRequest.SingleRequest;

import java.util.List;

public class AdapterMyRequestsFragment extends RecyclerView.Adapter<AdapterMyRequestsFragment.MyViewHolder> {
    private Context context;
    private List<ModelClassMyRequests> modelClassMyRequestsList;

    public AdapterMyRequestsFragment(Context context, List<ModelClassMyRequests> modelClassMyRequestsList) {
        this.context = context;
        this.modelClassMyRequestsList = modelClassMyRequestsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_myrequests_fragment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_bloodGroup.setText(modelClassMyRequestsList.get(position).getBloodGroup());
        holder.tv_area.setText(modelClassMyRequestsList.get(position).getArea());
        holder.tv_district.setText(modelClassMyRequestsList.get(position).getDistrict());
        holder.tv_accepted.setText(modelClassMyRequestsList.get(position).getAccepted());
        holder.tv_donated.setText(modelClassMyRequestsList.get(position).getDonated());

        String timeAgo = Util.getTimeAgo(Long.parseLong(modelClassMyRequestsList.get(position).getTimeStamp()));
        holder.tv_timeAgo.setText(timeAgo);

        holder.cv_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleRequest.class);
                intent.putExtra(Extras.POST_CREATOR_ID,modelClassMyRequestsList.get(position).getPostCreatorID());
                intent.putExtra(Extras.POST_CREATOR_PHOTO,modelClassMyRequestsList.get(position).getPostCreatorPhoto());
                intent.putExtra(Extras.POST_CREATOR_NAME,modelClassMyRequestsList.get(position).getPostCreatorName());
                intent.putExtra(Extras.POST_ID,modelClassMyRequestsList.get(position).getPostID());
                intent.putExtra(Extras.POST_DESCRIPTION,modelClassMyRequestsList.get(position).getPostDescription());
                intent.putExtra(Extras.POST_IMAGE,modelClassMyRequestsList.get(position).getPostImage());
                intent.putExtra(Extras.POST_LOVE,modelClassMyRequestsList.get(position).getPostLove());
                intent.putExtra(Extras.POST_VIEW,modelClassMyRequestsList.get(position).getPostView());
                intent.putExtra(Extras.BLOOD_GROUP,modelClassMyRequestsList.get(position).getBloodGroup());
                intent.putExtra(Extras.AREA,modelClassMyRequestsList.get(position).getArea());
                intent.putExtra(Extras.DISTRICT,modelClassMyRequestsList.get(position).getDistrict());
                intent.putExtra(Extras.ACCEPTED,modelClassMyRequestsList.get(position).getAccepted());
                intent.putExtra(Extras.DONATED,modelClassMyRequestsList.get(position).getDonated());
                intent.putExtra(Extras.TIMESTAMP,modelClassMyRequestsList.get(position).getTimeStamp());
                intent.putExtra(Extras.CAUSE,modelClassMyRequestsList.get(position).getCause());
                intent.putExtra(Extras.GENDER,modelClassMyRequestsList.get(position).getGender());
                intent.putExtra(Extras.PHONE_NUMBER,modelClassMyRequestsList.get(position).getPhone());
                intent.putExtra(Extras.UNIT_BAGS,modelClassMyRequestsList.get(position).getUnitBag());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassMyRequestsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_bloodGroup;
        private TextView tv_area;
        private TextView tv_district;
        private TextView tv_timeAgo;
        private TextView tv_accepted;
        private TextView tv_donated;
        private CardView cv_holder;
        public MyViewHolder(@NonNull View itemView) {
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

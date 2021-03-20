package com.bloodFinder.mybloodbank.allUsers.searchByName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.allUsers.AdapterAllUsers;
import com.bloodFinder.mybloodbank.allUsers.ModelClassAllUsers;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchByName extends RecyclerView.Adapter<AdapterSearchByName.MyVIewHolder> implements Filterable {
    private Context context;
    private List<ModelClassSearchByName> modelClassSearchByNameList,modelClassSearchByNameListFiltered;

    public AdapterSearchByName(Context context, List<ModelClassSearchByName> modelClassSearchByNameList) {
        this.context = context;
        this.modelClassSearchByNameList = modelClassSearchByNameList;
        this.modelClassSearchByNameListFiltered = modelClassSearchByNameList;
    }

    @NonNull
    @Override
    public MyVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_all_users,parent,false);
        return new MyVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVIewHolder holder, int position) {

        holder.tv_userName.setText(modelClassSearchByNameListFiltered.get(position).getUserName());
        holder.tv_district.setText(modelClassSearchByNameListFiltered.get(position).getDistrict());
        holder.tv_area.setText(modelClassSearchByNameListFiltered.get(position).getArea());
        holder.tv_bloodGroup.setText(modelClassSearchByNameListFiltered.get(position).getBloodGroup());
        Glide.with(context).load(modelClassSearchByNameListFiltered.get(position).getUserPhoto()).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(holder.iv_profilePicture);

    }

    @Override
    public int getItemCount() {
        return modelClassSearchByNameListFiltered.size();
    }



    public class MyVIewHolder extends RecyclerView.ViewHolder {
        private TextView tv_userName,tv_area,tv_district,tv_timeAgo,tv_bloodGroup;
        private ImageView iv_profilePicture;
        private CardView cv_holder_sampleAllUsers;
        public MyVIewHolder(@NonNull View itemView) {
            super(itemView);
            tv_userName = itemView.findViewById(R.id.tv_userName_sampleAllUsers);
            tv_area = itemView.findViewById(R.id.tv_area_sampleAllUsers);
            tv_district = itemView.findViewById(R.id.tv_district_sampleAllUsers);
            tv_timeAgo = itemView.findViewById(R.id.tv_timeAgo_sampleAllUsers);
            tv_bloodGroup = itemView.findViewById(R.id.tv_bloodGroup_sampleAllUsers);
            iv_profilePicture = itemView.findViewById(R.id.iv_profilePicture_sampleAllUsers);
            cv_holder_sampleAllUsers = itemView.findViewById(R.id.cv_holder_sampleAllUsers);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    modelClassSearchByNameListFiltered = modelClassSearchByNameList;
                } else {
                    List<ModelClassSearchByName> filteredList = new ArrayList<>();
                    for (ModelClassSearchByName row : modelClassSearchByNameList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUserName().toLowerCase().contains(charString.toLowerCase()) || row.getUserName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    modelClassSearchByNameListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = modelClassSearchByNameListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                modelClassSearchByNameListFiltered = (ArrayList<ModelClassSearchByName>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}

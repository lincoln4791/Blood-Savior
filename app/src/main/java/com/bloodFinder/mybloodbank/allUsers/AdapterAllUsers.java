package com.bloodFinder.mybloodbank.allUsers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.chats.ChattingActivity.ChattingActivity;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllUsers extends RecyclerView.Adapter<AdapterAllUsers.MyVIewHolder> implements Filterable {
    private Context context;
    private List<ModelClassAllUsers> modelClassAllUsersList,modelClassAllUsersListFiltered;

    public AdapterAllUsers(Context context, List<ModelClassAllUsers> modelClassAllUsersList) {
        this.context = context;
        this.modelClassAllUsersList = modelClassAllUsersList;
        this.modelClassAllUsersListFiltered = modelClassAllUsersList;
    }

    @NonNull
    @Override
    public MyVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_all_users,parent,false);
        return new MyVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVIewHolder holder, int position) {
        holder.tv_userName.setText(modelClassAllUsersList.get(position).getUserName());
        holder.tv_district.setText(modelClassAllUsersList.get(position).getDistrict());
        holder.tv_area.setText(modelClassAllUsersList.get(position).getArea());
        holder.tv_bloodGroup.setText(modelClassAllUsersList.get(position).getBloodGroup());

        holder.btn_call.setOnClickListener(v -> {
            Toast.makeText(context, context.getString(R.string.youAreNotPermittedToCallThisUserYEt), Toast.LENGTH_SHORT).show();

           /* String phn = modelClassAllUsersList.get(position).getPhone();
            String number = "tel:"+phn;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(number));
            context.startActivity(intent);*/

        });

        Glide.with(context).load(modelClassAllUsersList.get(position).getUserPhoto()).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(holder.iv_profilePicture);

        holder.btn_chat.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChattingActivity.class);
            intent.putExtra(Extras.USER_ID,modelClassAllUsersList.get(position).getUserID());
            intent.putExtra(Extras.USER_NAME,modelClassAllUsersList.get(position).getUserName());
            intent.putExtra(Extras.USER_PHOTO,modelClassAllUsersList.get(position).getUserPhoto());
            context.startActivity(intent);
        });


        holder.btn_map.setOnClickListener(v -> Toast.makeText(context, "map CLicked", Toast.LENGTH_SHORT).show());



        holder.cv_holder_sampleAllUsers.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile.class);
            intent.putExtra(Extras.USER_ID,modelClassAllUsersList.get(position).getUserID());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return modelClassAllUsersListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    modelClassAllUsersListFiltered = modelClassAllUsersList;
                } else {
                    List<ModelClassAllUsers> filteredList = new ArrayList<>();
                    for (ModelClassAllUsers row : modelClassAllUsersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUserName().toLowerCase().contains(charString.toLowerCase()) || row.getUserName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    modelClassAllUsersListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = modelClassAllUsersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modelClassAllUsersListFiltered = (ArrayList<ModelClassAllUsers>) results.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class MyVIewHolder extends RecyclerView.ViewHolder {
        private TextView tv_userName,tv_area,tv_district,tv_timeAgo,tv_bloodGroup;
        private Button btn_call, btn_chat, btn_map;
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
            btn_call = itemView.findViewById(R.id.btn_call_sampleAllUsers);
            btn_chat = itemView.findViewById(R.id.btn_chat_sampleAllUsers);
            btn_map = itemView.findViewById(R.id.btn_map_sampleAllUsers);
            cv_holder_sampleAllUsers = itemView.findViewById(R.id.cv_holder_sampleAllUsers);
        }
    }







}

package com.bloodFinder.mybloodbank.selectCountry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.userRegistration.Register;

import java.util.List;

public class AdapterSelectCountry extends RecyclerView.Adapter<AdapterSelectCountry.MyViewHolder> {
    private List<ModelClassSelectCountry> countryList;
    private Context context;

    public AdapterSelectCountry(List<ModelClassSelectCountry> countryList, Context context) {
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_selectcountry,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_countyName.setText(countryList.get(position).getCountryName());

        holder.cv_countryNameHolder.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(Extras.COUNTRY_NAME,countryList.get(position).getCountryName());
            if(context instanceof SelectCountry){
                ((SelectCountry) context).returnData(countryList.get(position).getCountryName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_countyName;
        CardView cv_countryNameHolder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_countyName = itemView.findViewById(R.id.tv_countryName_sampleSelectCountry);
            cv_countryNameHolder = itemView.findViewById(R.id.cv_countryName_sampleSelectCountry);
        }
    }
}

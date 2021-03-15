package com.bloodFinder.mybloodbank.covid19Report.affectedCountries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.covid19Report.countryWiseReport.CountryWiseReport;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountriesAdapter extends RecyclerView.Adapter<AffectedCountriesAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<AffectedCountriesModel> affectedCountriesModelList,affectedCountriesModelListFiltered;


    public AffectedCountriesAdapter(Context context, List<AffectedCountriesModel> affectedCountriesModelList) {
        this.context = context;
        this.affectedCountriesModelList = affectedCountriesModelList;
        this.affectedCountriesModelListFiltered = affectedCountriesModelList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_layout_affected_countries,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String countryName = affectedCountriesModelListFiltered.get(position).getCountryName();
        String flagImage = affectedCountriesModelListFiltered.get(position).getFlag();
        Glide.with(context).load(affectedCountriesModelListFiltered.get(position).getFlag()).into(holder.ivFlag);
        holder.tvCountryName.setText(countryName);
        holder.cl_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CountryWiseReport.class);
                intent.putExtra(Extras.COUNTRY_NAME,countryName);
                intent.putExtra(Extras.FLAG,flagImage);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d("tag","get Item Count Length : "+affectedCountriesModelListFiltered.size());
        return affectedCountriesModelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    affectedCountriesModelListFiltered = affectedCountriesModelList;
                } else {
                    List<AffectedCountriesModel> filteredList = new ArrayList<>();
                    for (AffectedCountriesModel row : affectedCountriesModelList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountryName().toLowerCase().contains(charString.toLowerCase()) || row.getCountryName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    affectedCountriesModelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = affectedCountriesModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                affectedCountriesModelListFiltered = (ArrayList<AffectedCountriesModel>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFlag;
        private TextView tvCountryName;
        private ConstraintLayout cl_holder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFlag = itemView.findViewById(R.id.imageView4);
            tvCountryName = itemView.findViewById(R.id.tv_countryName_sampleAffectedCountries);
            cl_holder = itemView.findViewById(R.id.cl_sampleLayout_affecredCountries);
        }
    }
}

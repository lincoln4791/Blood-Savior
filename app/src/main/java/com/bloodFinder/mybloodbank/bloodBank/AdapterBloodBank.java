package com.bloodFinder.mybloodbank.bloodBank;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;

import java.util.List;

public class AdapterBloodBank extends RecyclerView.Adapter<AdapterBloodBank.MyViewHolder> {
    private Context context;
    private List<ModelClassBloodBank> modelClassBloodBankList;

    public AdapterBloodBank(Context context, List<ModelClassBloodBank> modelClassBloodBankList) {
        this.context = context;
        this.modelClassBloodBankList = modelClassBloodBankList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_bloodbank_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bloodBankName.setText(modelClassBloodBankList.get(position).getBloodBankName());
        holder.bloodBankAddress.setText(modelClassBloodBankList.get(position).getBloodBankAddress());
        holder.bloodBankPhoneNumber.setText(modelClassBloodBankList.get(position).getBloodBankPhoneNumber());

        holder.bloodBankPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberToDial = "tel:"+modelClassBloodBankList.get(position).getBloodBankPhoneNumber();
                Uri number =Uri.parse(numberToDial);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(number);
                context.startActivity(intent);
            }
        });

        holder.bloodBankMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Map Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassBloodBankList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView bloodBankName, bloodBankAddress,bloodBankPhoneNumber;
        private ImageView bloodBankMap;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodBankName = itemView.findViewById(R.id.tv_bloodBankName_sampleBloodBank);
            bloodBankAddress = itemView.findViewById(R.id.tv_bloodBankAddress_sampleBloodBank);
            bloodBankPhoneNumber = itemView.findViewById(R.id.tv_bloodBankPhoneNumber_sampleBloodBank);
            bloodBankMap = itemView.findViewById(R.id.iv_map_sampleBloodBank);
        }
    }
}

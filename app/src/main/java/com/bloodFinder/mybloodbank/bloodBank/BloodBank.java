package com.bloodFinder.mybloodbank.bloodBank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bloodFinder.mybloodbank.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BloodBank extends AppCompatActivity {

    private String[] bloodBankName;
    private String[] bloodBankAddress;
    private String[] bloodBankPhoneNumber;
    private List<ModelClassBloodBank> modelClassBloodBankList;
    private AdapterBloodBank adapterBloodBank;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Blood Bank");
        bloodBankAddress = getResources().getStringArray(R.array.bloodBankAddress);
        bloodBankPhoneNumber = getResources().getStringArray(R.array.bloodBankPhoneNUmber);
        bloodBankName = getResources().getStringArray(R.array.bloodBankName);
        recyclerView = findViewById(R.id.rv_bloodBankDetails_BloodBankActivity);
        modelClassBloodBankList = new ArrayList<>();
        adapterBloodBank = new AdapterBloodBank(BloodBank.this,modelClassBloodBankList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterBloodBank);

        for(int i=0 ; i<bloodBankName.length ; i++){
            ModelClassBloodBank modelClassBloodBank = new ModelClassBloodBank(bloodBankName[i],bloodBankAddress[i],bloodBankPhoneNumber[i]);
            modelClassBloodBankList.add(modelClassBloodBank);
            adapterBloodBank.notifyDataSetChanged();
        }


    }
}
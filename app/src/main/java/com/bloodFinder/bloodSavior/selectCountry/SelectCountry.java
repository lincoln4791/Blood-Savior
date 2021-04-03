package com.bloodFinder.bloodSavior.selectCountry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Extras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectCountry extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;
    private List<ModelClassSelectCountry> countryList;
    private AdapterSelectCountry adapterSelectCountry;
    private RecyclerView recyclerView;
    private String[] list;
    private List<String> templist;

    private int RC_SELECT_COUNTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_selectCountryActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        recyclerView = findViewById(R.id.rv_selectCountryActivity);
        countryList = new ArrayList<>();
        adapterSelectCountry = new AdapterSelectCountry(countryList,SelectCountry.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSelectCountry);
        list = getResources().getStringArray(R.array.bd_districts);
        templist = new ArrayList<String>();


        for(int i =0 ;i<list.length ; i++){
            ModelClassSelectCountry modelClassSelectCountry = new ModelClassSelectCountry(list[i]);
            templist.add(list[i]);
        }
        Collections.sort(templist);

        for(int i =0 ;i<templist.size(); i++){
            ModelClassSelectCountry modelClassSelectCountry = new ModelClassSelectCountry(templist.get(i));
            countryList.add(modelClassSelectCountry);
        }
        adapterSelectCountry.notifyDataSetChanged();
    }


    public void returnData(String countryName){
        Intent intent = new Intent();
        intent.putExtra(Extras.COUNTRY_NAME,countryName);
        setResult(RESULT_OK,intent);
        finish();
    }

}
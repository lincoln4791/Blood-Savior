package com.bloodFinder.bloodSavior.aboutUs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.bloodFinder.bloodSavior.R;

public class AboutUs extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_aboutUs);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
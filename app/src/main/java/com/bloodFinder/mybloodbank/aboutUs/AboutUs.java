package com.bloodFinder.mybloodbank.aboutUs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bloodFinder.mybloodbank.R;

import java.util.Objects;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Objects.requireNonNull(getSupportActionBar()).setTitle("About Us");
    }
}
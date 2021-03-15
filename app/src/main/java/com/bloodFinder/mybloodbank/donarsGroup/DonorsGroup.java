package com.bloodFinder.mybloodbank.donarsGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.EndPoints;

import java.util.Objects;

public class DonorsGroup extends AppCompatActivity {
    private CardView cv_group1,cv_group2,cv_group3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donars_group);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Donars Group");
        cv_group1 = findViewById(R.id.cv_group1_DonorsGroupActivity);
        cv_group2 = findViewById(R.id.cv_group2_DonorsGroupActivity);
        cv_group3 = findViewById(R.id.cv_group3_DonorsGroupActivity);

        cv_group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(EndPoints.LINK_BANGLADESH_BLOOD_DONATE_GROUP);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        cv_group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(EndPoints.LINK_SECCHAY_ROKTODAN_BLOOD_FOUNDATION);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        cv_group3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(EndPoints.LINK_BLOOD_FIGHTERS);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}
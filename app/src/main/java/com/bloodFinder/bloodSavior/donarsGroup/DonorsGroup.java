package com.bloodFinder.bloodSavior.donarsGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.EndPoints;

public class DonorsGroup extends AppCompatActivity {
    private CardView cv_group1,cv_group2,cv_group3;
    private Toolbar toolbar;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donars_group);

        toolbar = findViewById(R.id.toolbar_aboutUs);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_donorsGroup);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

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
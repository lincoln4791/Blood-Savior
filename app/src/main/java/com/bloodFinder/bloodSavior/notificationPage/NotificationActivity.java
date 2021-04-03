package com.bloodFinder.bloodSavior.notificationPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Util;

public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.toolbar_notificationActivity);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_notificationActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        loadNotificationsPage();
    }

    private void loadNotificationsPage() {
        if(Util.internetAvailable(NotificationActivity.this)){

        }
        else{
            Util.noInternetConnection(this);
        }
    }
}
package com.bloodFinder.mybloodbank.common;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.bloodFinder.mybloodbank.R;

public final class MyLoadingProgress {
    Activity activity;
    AlertDialog alertDialog;
    public MyLoadingProgress(Activity activity){
        this.activity = activity;
    }

    public void startLoadingProgress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(LayoutInflater.from(activity).inflate(R.layout.sample_custom_progressbar_full,null));
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        //builder.show();
    }

    public void dismissAlertDialogue(){
        alertDialog.dismiss();
    }
}

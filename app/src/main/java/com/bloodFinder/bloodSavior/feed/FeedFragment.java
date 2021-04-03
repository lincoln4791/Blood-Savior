package com.bloodFinder.bloodSavior.feed;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.allUsers.AllUsers;
import com.bloodFinder.bloodSavior.bloodBank.BloodBank;
import com.bloodFinder.bloodSavior.common.CallBackFirebaseCall;
import com.bloodFinder.bloodSavior.common.Constants;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.common.UtilDB;
import com.bloodFinder.bloodSavior.mainActivity.MainActivity;
import com.bloodFinder.bloodSavior.filterPosts.FilterPosts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment {
    @BindView(R.id.rv_Posts_FeedFragment) RecyclerView recyclerView;
    @BindView(R.id.nsv_feedFragment) NestedScrollView nestedScrollView;
    @BindView(R.id.arcLoader_FeedFragment) SimpleArcLoader arcLoader;
    @BindView(R.id.pb_morePostLoading_feedFragment)  ProgressBar pb_morePostLoading;
    @BindView(R.id.cv_featuredCovid19_FeedFragment) CardView cv_featuredCovid19;
    @BindView(R.id.cv_findDonors_feedFragment) CardView cv_findDonors;
    @BindView(R.id.cv_allUsers_feedFragment) CardView cv_allUsers;
    @BindView(R.id.cv_bloodBank_feedFragment) CardView cv_bloodBank;
    @BindView(R.id.cv_topDonors_feedFragments) CardView cv_topDonors;
    @BindView(R.id.cv_findNearbyDonorsBar_feedFragment) CardView cv_nearbyDonors;
    @BindView(R.id.tv_allCaughtUp_feedFragment) TextView tv_allCaughtUp;


    private AdapterFeedFragment adapterFeedFragment;
    private LinearLayoutManager linearLayoutManager;
    private VM_FeedFragment vm_feedFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(view);


        //
        if (Util.internetAvailable(getContext())) {
            vm_feedFragment = ViewModelProviders.of(this).get(VM_FeedFragment.class);
            linearLayoutManager = new LinearLayoutManager(getContext());
            adapterFeedFragment = new AdapterFeedFragment(getContext(), vm_feedFragment.list);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapterFeedFragment);
            adapterFeedFragment.notifyDataSetChanged();

            if(vm_feedFragment.list.isEmpty()){
                vm_feedFragment.getLastNode();
            }

            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //pb_morePostLoading.setVisibility(View.VISIBLE);
                   if(!vm_feedFragment.isMAx){
                       vm_feedFragment.currentPage++;
                       vm_feedFragment.lastNode = String.valueOf(Integer.parseInt(vm_feedFragment.lastNode) - vm_feedFragment.recordPerPage);
                       String startingNode = String.valueOf(Integer.parseInt(vm_feedFragment.lastNode) - vm_feedFragment.recordPerPage);
                       int limitToLoad = vm_feedFragment.recordPerPage;

                       if(Integer.parseInt(startingNode) < 0){
                           startingNode = "1";
                           limitToLoad = Integer.parseInt(vm_feedFragment.lastNode)-1;
                           Log.d("tag","last dataGroup : "+limitToLoad);
                       }
                            vm_feedFragment.firstLoad(startingNode,limitToLoad);
                   }
                   else{
                       Toast.makeText(getContext(), getString(R.string.allCaughtUp), Toast.LENGTH_SHORT).show();
                   }
                }
            });

            observe();

            cv_nearbyDonors.setOnClickListener(v -> Toast.makeText(getContext(), getString(R.string.thisFeatureIsUnderConstruction), Toast.LENGTH_SHORT).show());


            cv_findDonors.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), FilterPosts.class));
            });


            cv_allUsers.setOnClickListener(v ->
                    startActivity(new Intent(getContext(), AllUsers.class)));


            cv_topDonors.setOnClickListener(v -> Toast.makeText(getContext(), getString(R.string.thisPageIsUnderConstruction), Toast.LENGTH_SHORT).show());


            cv_bloodBank.setOnClickListener(v -> startActivity(new Intent(getContext(), BloodBank.class)));


            cv_featuredCovid19.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(UtilDB.FEATURED_IMAGE_URI, "image/jpg");
                startActivity(intent);
            });



        } else {
            Dialog noInternetDialog = new Dialog(getContext());
            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.alert_no_internet, null);
            noInternetDialog.setContentView(view1);
            noInternetDialog.setCancelable(false);
            noInternetDialog.show();

            Button refresh = view1.findViewById(R.id.btn_noInternetAlert);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            });

        }


    }





    public void observe (){
        vm_feedFragment.mutableList.observe(this, m_mainActivity3s -> {
            adapterFeedFragment.notifyDataSetChanged();
            Log.d("tag","observed");
        });


        vm_feedFragment.mutableIsLoading.observe(this, isLoading -> {
            if(isLoading){
                arcLoader.start();
                arcLoader.setVisibility(View.VISIBLE);
            }
            else{
                arcLoader.stop();
                arcLoader.setVisibility(View.GONE);
            }
        });
    }






}
package com.bloodFinder.bloodSavior.posts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.login.LoginActivity;
import com.bloodFinder.bloodSavior.posts.AcceptedFragment.AcceptedFragment;
import com.bloodFinder.bloodSavior.posts.MyRequests.MyRequestsFragment;
import com.bloodFinder.bloodSavior.posts.makeNewRequest.MakeNewRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RequestsFragment extends Fragment {
    private FloatingActionButton btn_makeNewRequest;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Util.internetAvailable(getContext())){
            btn_makeNewRequest = view.findViewById(R.id.fab_makeNewRequest_RequestsFragment);
            tabLayout = view.findViewById(R.id.tl_RequestsFragment);
            viewPager = view.findViewById(R.id.vp_RequestsFragment);

            setViewPager();

            btn_makeNewRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MakeNewRequest.class));
                }
            });
        }
        else{
            Toast toast = new Toast(getContext());
            View toastView = LayoutInflater.from(getContext()).inflate(R.layout.toast_no_internet,null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class AdapterTabLayout extends FragmentPagerAdapter {

        public AdapterTabLayout(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
                    return  myRequestsFragment;

                case 1 :
                    AcceptedFragment acceptedFragment = new AcceptedFragment();
                    return acceptedFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabLayout.getTabCount();
        }
    }

    private void setViewPager() {
            tabLayout.addTab(tabLayout.newTab().setText("My Requests"));
            tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            AdapterTabLayout adapterTabLayout = new AdapterTabLayout(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager.setAdapter(adapterTabLayout);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }
}
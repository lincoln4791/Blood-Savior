package com.bloodFinder.mybloodbank.mainActivity.chats;

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

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.mainActivity.chats.ChatList.ChatList;
import com.bloodFinder.mybloodbank.mainActivity.chats.invite.InviteFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatsFragment extends Fragment {
  private TabLayout tabLayout;
  private ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tl_ChatsFragment);
        viewPager = view.findViewById(R.id.vp_ChatsFragment);

        setViewPager();

    }



    class AdapterTableLayout extends FragmentPagerAdapter {

        public AdapterTableLayout(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ChatList chatList = new ChatList();
                    return  chatList;

                case 1:
                    InviteFragment inviteFragment = new InviteFragment();
                    return  inviteFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabLayout.getTabCount();
        }
    }





    private void setViewPager() {
            tabLayout.addTab(tabLayout.newTab().setText("My Chats"));
            tabLayout.addTab(tabLayout.newTab().setText("Invites"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            AdapterTableLayout adapterTableLayout = new AdapterTableLayout(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager.setAdapter(adapterTableLayout);

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
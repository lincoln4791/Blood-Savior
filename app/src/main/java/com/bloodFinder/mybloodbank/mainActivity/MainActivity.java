package com.bloodFinder.mybloodbank.mainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.aboutUs.AboutUs;
import com.bloodFinder.mybloodbank.common.EndPoints;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.UtilDB;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.chats.ChatsFragment;
import com.bloodFinder.mybloodbank.feed.FeedFragment;
import com.bloodFinder.mybloodbank.donations.DonationsFragment;
import com.bloodFinder.mybloodbank.notificationPage.NotificationActivity;
import com.bloodFinder.mybloodbank.userProfile.ProfileFragment;
import com.bloodFinder.mybloodbank.requests.RequestsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView iv_notification;
    private Boolean doubleBackPress = false;
    private FeedFragment feedFragment ;
    private RequestsFragment requestsFragment;
    private ChatsFragment chatsFragment;
    private DonationsFragment donationsFragment;
    private ProfileFragment profileFragment;
    private int postIndexCounter;
    private int lastIndex;
    private int lastNode;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Toolbar toolbar = findViewById(R.id.toolbar_mainActivity);
        actionBar.setCustomView(toolbar);

        mAuth = FirebaseAuth.getInstance();

        tabLayout = findViewById(R.id.tl_MainActivity);
        viewPager = findViewById(R.id.vp_MAinActivity);
        iv_notification = findViewById(R.id.iv_notification_MainActivity);
        feedFragment = new FeedFragment();
        requestsFragment = new RequestsFragment();
        chatsFragment = new ChatsFragment();
        donationsFragment = new DonationsFragment();
        profileFragment = new ProfileFragment();


        viewPager.setOffscreenPageLimit(0);
        drawerLayout = findViewById(R.id.drawer_layout_MainActivity);
        navigationView = findViewById(R.id.nav_view_MainActivity);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                switch (itemID){
                    case R.id.menu_aboutUs_actionbar_home:
                        startActivity(new Intent(MainActivity.this, AboutUs.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_rewardCard_actionbar_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        //Toast.makeText(MainActivity.this, "Reward Card Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_rateUs_actionbar_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        //Toast.makeText(MainActivity.this, "Rate Us Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_privacyPolicy_actionbar_home:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(EndPoints.PRIVACY_POLICY));
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logOut_actionbar_home:
                        confirmLogout();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });




        setViewPager();

    }



        public class Adapter extends FragmentPagerAdapter {

            public Adapter(@NonNull FragmentManager fm, int behavior) {
                super(fm, behavior);
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {

                switch (position){
                    case 0:

                        TabLayout.Tab tab = tabLayout.getTabAt(0);
                        tab.setIcon(R.drawable.ic_app_logo);
                        return  feedFragment;

                    case 1:

                        return requestsFragment;

                    case 2:

                        return chatsFragment;

                    case 3:
                        return donationsFragment;

                    case 4:
                        return profileFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        }




    private void setViewPager(){
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_feed_main_activity));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_requests_main_activity));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_chats_main_activity));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_donations_main_activity));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_profile_main_activity));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Adapter adapter = new Adapter(getSupportFragmentManager(),Adapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

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
       loadProfile();
    }


    private void loadProfile() {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        String myUID = FirebaseAuth.getInstance().getUid();
        mRootRef.child(NodeNames.USERS).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    UtilDB.USER_PHOTO = "";

                    if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                        if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                            UtilDB.USER_PHOTO = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.USER_NAME).getValue()!=null){
                        if(!snapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")){
                            UtilDB.USER_NAME = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.AREA).getValue()!=null){
                        if(!snapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                            UtilDB.AREA = snapshot.child((NodeNames.AREA)).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.DISTRICT).getValue()!=null){
                        if(!snapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                            UtilDB.DISTRICT = snapshot.child(NodeNames.DISTRICT).getValue().toString();
                        }
                    }

                    if(snapshot.child(NodeNames.GENDER).getValue()!=null){
                        if(!snapshot.child(NodeNames.GENDER).getValue().toString().equals("")){
                            UtilDB.GENDER = snapshot.child(NodeNames.GENDER).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.AGE).getValue()!=null){
                        if(!snapshot.child(NodeNames.AGE).getValue().toString().equals("")){
                            UtilDB.AGE = snapshot.child(NodeNames.AGE).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                        if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                            UtilDB.PHONE_NUMBER = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                        }
                    }


                    if(snapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                        if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                            UtilDB.BLOOD_GROUP = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                        }
                    }

                    if(snapshot.child(NodeNames.DONATION_STATUS).getValue()!=null){
                        if(!snapshot.child(NodeNames.DONATION_STATUS).getValue().toString().equals("")){
                            UtilDB.DONATION_STATUS = snapshot.child(NodeNames.DONATION_STATUS).getValue().toString();
                        }
                    }

                    if(snapshot.child(NodeNames.TOTAL_DONATION).getValue()!=null){
                        if(!snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString().equals("")){
                            UtilDB.TOTAL_DONATION = snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString();
                        }
                    }



                    if(snapshot.child(NodeNames.LAST_DONATION).getValue()!=null){
                        if(!snapshot.child(NodeNames.LAST_DONATION).getValue().toString().equals("")){
                            UtilDB.LAST_DONATION = snapshot.child(NodeNames.LAST_DONATION).getValue().toString();
                        }
                    }





                }
                else{
                    //Toast.makeText(getContext(), "snapNotExists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getContext(), "snapShotCanceled", Toast.LENGTH_SHORT).show();
            }
        });
    }






    public void logOut(){
        mAuth.signOut();
        finishAffinity();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }



    private void confirmLogout() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_logout,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        view.findViewById(R.id.btn_yes_alertImage_dialog_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        view.findViewById(R.id.btn_no_alertImage_dialog_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(tabLayout.getSelectedTabPosition()>0){
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }

        else{
            if(!doubleBackPress){
                doubleBackPress = true;
                Toast.makeText(this, getString(R.string.pressBAckButtonAgainToCloseTheApplication), Toast.LENGTH_SHORT).show();
                Handler handler =  new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPress =false;
                    }
                },1500);
            }

            else{
                finishAffinity();
            }
        }
    }
}
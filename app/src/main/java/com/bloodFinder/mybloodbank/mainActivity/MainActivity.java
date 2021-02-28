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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.aboutUs.AboutUs;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.mainActivity.chats.ChatsFragment;
import com.bloodFinder.mybloodbank.mainActivity.feed.FeedFragment;
import com.bloodFinder.mybloodbank.mainActivity.history.HistoryFragment;
import com.bloodFinder.mybloodbank.privacyPolicy.PrivacyPolicy;
import com.bloodFinder.mybloodbank.userProfile.ProfileFragment;
import com.bloodFinder.mybloodbank.mainActivity.requests.RequestsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView iv_notification;


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
                        Toast.makeText(MainActivity.this, "Reward Card Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_rateUs_actionbar_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "Rate Us Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_privacyPolicy_actionbar_home:
                        startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logOut_actionbar_home:
                        logOut();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "notification Clicked", Toast.LENGTH_SHORT).show();
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
                        FeedFragment feedFragment = new FeedFragment();
                        return  feedFragment;

                    case 1:
                        RequestsFragment requestsFragment = new RequestsFragment();
                        return requestsFragment;

                    case 2:
                        ChatsFragment chatsFragment = new ChatsFragment();
                        return chatsFragment;

                    case 3:
                        HistoryFragment historyFragment = new HistoryFragment();
                        return historyFragment;

                    case 4:
                        ProfileFragment profileFragment = new ProfileFragment();
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
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_history_main_activity));
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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }






    public void logOut(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}
package com.bloodFinder.mybloodbank.allUsers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.allUsers.searchByName.SearchByName;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.selectCountry.SelectCountry;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_searchByName;
    private ImageView iv_back;
    private TextView tv_district, tv_emptyListMessage;
    private Button btn_search;
    private String district = "", bloodGroup = "";
    private ImageView iv_backspaceDistrict;
    private SimpleArcLoader arcLoader;
    private ProgressBar pb_morePostLoading;
    private ArrayList<String> bloodList, causeList;
    private RecyclerView recyclerView;
    private List<ModelClassAllUsers> modelClassAllUsersList;
    private AdapterAllUsers adapterAllUsers;

    private DatabaseReference mRootRef;
    private String myUID;
    private final int RC_SELECT_COUNTRY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        toolbar = findViewById(R.id.toolbar_allUsersActivity);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        iv_back = findViewById(R.id.iv_back_toolbar_allUsersActivity);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        tv_district = findViewById(R.id.tv_district_allUsersActivity);
        tv_emptyListMessage = findViewById(R.id.tv_emptyFeed_allUsersActivity);
        tv_searchByName = findViewById(R.id.tv_searchByName_allUsers);
        btn_search = findViewById(R.id.btn_search_allUsersActivity);
        arcLoader = findViewById(R.id.arcLoader_allUsersActivity);
        recyclerView = findViewById(R.id.rv_Posts_allUsersActivity);
        pb_morePostLoading = findViewById(R.id.pb_morePostLoading_allUsersActivity);
        iv_backspaceDistrict = findViewById(R.id.iv_backspaceDistrict_allUsersActivity);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();
        bloodList = new ArrayList<>();
        causeList = new ArrayList<>();
        modelClassAllUsersList = new ArrayList<>();
        adapterAllUsers = new AdapterAllUsers(AllUsers.this, modelClassAllUsersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tv_searchByName.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchByName.class));
        });

        tv_district.setOnClickListener(v -> {
            Intent intent = new Intent(AllUsers.this, SelectCountry.class);
            startActivityForResult(intent, RC_SELECT_COUNTRY);
        });

        btn_search.setOnClickListener(v -> {
            arcLoader.start();
            arcLoader.setVisibility(View.VISIBLE);
            modelClassAllUsersList.clear();
            adapterAllUsers.notifyDataSetChanged();
            tv_emptyListMessage.setVisibility(View.GONE);
            selectSearchCriteria();

        });

        iv_backspaceDistrict.setOnClickListener(v -> {
            tv_district.setText("");
            district = "";
            iv_backspaceDistrict.setVisibility(View.GONE);
        });


        getSelectedBloodGroup();
        fetchAllUsers();
        recyclerView.setAdapter(adapterAllUsers);
        adapterAllUsers.notifyDataSetChanged();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SELECT_COUNTRY) {
                district = data.getStringExtra(Extras.COUNTRY_NAME);
                tv_district.setText(district);
                iv_backspaceDistrict.setVisibility(View.VISIBLE);

            }
        }
    }


    private void getSelectedBloodGroup() {
        Chip chip_APositive, chip_ANegative, chip_BPositive, chip_BNegative, chip_OPositive, chip_ONegative, chip_ABPositive, chip_ABNegative;

        chip_APositive = findViewById(R.id.chip_APositive_bloodGroup_allUsersActivity);
        chip_ANegative = findViewById(R.id.chip_ANegative_bloodGroup_allUsersActivity);
        chip_BPositive = findViewById(R.id.chip_BPositive_bloodGroup_allUsersActivity);
        chip_BNegative = findViewById(R.id.chip_BNegative_bloodGroup_allUsersActivity);
        chip_OPositive = findViewById(R.id.chip_OPositive_bloodGroup_allUsersActivity);
        chip_ONegative = findViewById(R.id.chip_ONegative_bloodGroup_allUsersActivity);
        chip_ABPositive = findViewById(R.id.chip_ABPositive_bloodGroup_allUsersActivity);
        chip_ABNegative = findViewById(R.id.chip_ABNegative_bloodGroup_allUsersActivity);

        CompoundButton.OnCheckedChangeListener checkedChangeListenerBloodGroup = (buttonView, isChecked) -> {
            if (isChecked) {
                bloodList.add(buttonView.getText().toString());
                Log.d("tag", "blood Added" + buttonView.getText().toString());
            } else {
                bloodList.remove(buttonView.getText().toString());
                Log.d("tag", "removed" + buttonView.getText().toString());
            }
        };

        chip_APositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ANegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_BPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_BNegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_OPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ONegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ABPositive.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        chip_ABNegative.setOnCheckedChangeListener(checkedChangeListenerBloodGroup);
        //return bloodGroup;
    }


    private void selectSearchCriteria() {
        modelClassAllUsersList.clear();
        arcLoader.start();
        arcLoader.setVisibility(View.VISIBLE);

        int searchCriteriaCount = 0;
        if (!bloodList.isEmpty()) {
            searchCriteriaCount++;
        }
        if (!district.equals("")) {
            searchCriteriaCount++;
        }

        if (searchCriteriaCount == 0) {
            fetchAllUsers();
            arcLoader.stop();
            arcLoader.setVisibility(View.GONE);
        }
        else if (searchCriteriaCount == 1) {
            if (!bloodList.isEmpty()) {
                bloodGroup = bloodList.get(0);
                fetchUsersWithBloodGroupFilterOnly();
            }
            else {
                district = tv_district.getText().toString();
                fetchUsersWithDistrictFilterOnly();
            }
        }
        else {
            fetchUsersWithBloodGroupAndDistrictBoth();
        }


    }

    private void fetchUsersWithBloodGroupAndDistrictBoth() {
        mRootRef.child(NodeNames.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals(bloodGroup) &&
                                dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals(district)) {
                            String userName = "";
                            String userPhoto = "";
                            String userID = "";
                            String age = "";
                            String gender = "";
                            String district = "";
                            String area = "";
                            String timeStamp = "";
                            String bloodGroup = "";
                            String email = "";
                            String phone = "";
                            String userOrder = "";

                            if (dataSnapshot.child(NodeNames.USER_NAME).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")) {
                                    userName = dataSnapshot.child(NodeNames.USER_NAME).getValue().toString();
                                }
                            }
                            Log.d("tag", "userName" + userName);

                            if (dataSnapshot.child(NodeNames.USER_PHOTO).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")) {
                                    userPhoto = dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ID).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ID).getValue().toString().equals("")) {
                                    userID = dataSnapshot.child(NodeNames.USER_ID).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AGE).exists()) {
                                if (!dataSnapshot.child(NodeNames.AGE).getValue().toString().equals("")) {
                                    age = dataSnapshot.child(NodeNames.AGE).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.GENDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")) {
                                    gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.DISTRICT).exists()) {
                                if (!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")) {
                                    district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AREA).exists()) {
                                if (!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")) {
                                    area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.TIMESTAMP).exists()) {
                                if (!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")) {
                                    timeStamp = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.BLOOD_GROUP).exists()) {
                                if (!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")) {
                                    bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.PHONE_NUMBER).exists()) {
                                if (!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")) {
                                    phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.EMAIL).exists()) {
                                if (!dataSnapshot.child(NodeNames.EMAIL).getValue().toString().equals("")) {
                                    email = dataSnapshot.child(NodeNames.EMAIL).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ORDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString().equals("")) {
                                    userOrder = dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString();
                                }
                            }

                            ModelClassAllUsers modelClassAllUsers = new ModelClassAllUsers(userName, userPhoto, userID, area, district, bloodGroup,
                                    age, email, gender, timeStamp, phone);

                            modelClassAllUsersList.add(modelClassAllUsers);
                            adapterAllUsers.notifyDataSetChanged();
                            arcLoader.stop();
                            arcLoader.setVisibility(View.GONE);
                        }

                    }
                    if (modelClassAllUsersList.isEmpty()) {
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                    }


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchUsersWithDistrictFilterOnly() {
        mRootRef.child(NodeNames.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals(district)) {
                            String userName = "";
                            String userPhoto = "";
                            String userID = "";
                            String age = "";
                            String gender = "";
                            String district = "";
                            String area = "";
                            String timeStamp = "";
                            String bloodGroup = "";
                            String email = "";
                            String phone = "";
                            String userOrder = "";

                            if (dataSnapshot.child(NodeNames.USER_NAME).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")) {
                                    userName = dataSnapshot.child(NodeNames.USER_NAME).getValue().toString();
                                }
                            }
                            Log.d("tag", "userName" + userName);

                            if (dataSnapshot.child(NodeNames.USER_PHOTO).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")) {
                                    userPhoto = dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ID).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ID).getValue().toString().equals("")) {
                                    userID = dataSnapshot.child(NodeNames.USER_ID).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AGE).exists()) {
                                if (!dataSnapshot.child(NodeNames.AGE).getValue().toString().equals("")) {
                                    age = dataSnapshot.child(NodeNames.AGE).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.GENDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")) {
                                    gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.DISTRICT).exists()) {
                                if (!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")) {
                                    district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AREA).exists()) {
                                if (!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")) {
                                    area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.TIMESTAMP).exists()) {
                                if (!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")) {
                                    timeStamp = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.BLOOD_GROUP).exists()) {
                                if (!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")) {
                                    bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.PHONE_NUMBER).exists()) {
                                if (!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")) {
                                    phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.EMAIL).exists()) {
                                if (!dataSnapshot.child(NodeNames.EMAIL).getValue().toString().equals("")) {
                                    email = dataSnapshot.child(NodeNames.EMAIL).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ORDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString().equals("")) {
                                    userOrder = dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString();
                                }
                            }

                            ModelClassAllUsers modelClassAllUsers = new ModelClassAllUsers(userName, userPhoto, userID, area, district, bloodGroup,
                                    age, email, gender, timeStamp, phone);

                            modelClassAllUsersList.add(modelClassAllUsers);
                            adapterAllUsers.notifyDataSetChanged();
                            arcLoader.stop();
                            arcLoader.setVisibility(View.GONE);
                        }

                    }
                    if (modelClassAllUsersList.isEmpty()) {
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                    }


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchUsersWithBloodGroupFilterOnly() {
        mRootRef.child(NodeNames.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals(bloodGroup)) {
                            String userName = "";
                            String userPhoto = "";
                            String userID = "";
                            String age = "";
                            String gender = "";
                            String district = "";
                            String area = "";
                            String timeStamp = "";
                            String bloodGroup = "";
                            String email = "";
                            String phone = "";
                            String userOrder = "";

                            if (dataSnapshot.child(NodeNames.USER_NAME).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")) {
                                    userName = dataSnapshot.child(NodeNames.USER_NAME).getValue().toString();
                                }
                            }
                            Log.d("tag", "userName" + userName);

                            if (dataSnapshot.child(NodeNames.USER_PHOTO).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")) {
                                    userPhoto = dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ID).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ID).getValue().toString().equals("")) {
                                    userID = dataSnapshot.child(NodeNames.USER_ID).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AGE).exists()) {
                                if (!dataSnapshot.child(NodeNames.AGE).getValue().toString().equals("")) {
                                    age = dataSnapshot.child(NodeNames.AGE).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.GENDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")) {
                                    gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.DISTRICT).exists()) {
                                if (!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")) {
                                    district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.AREA).exists()) {
                                if (!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")) {
                                    area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.TIMESTAMP).exists()) {
                                if (!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")) {
                                    timeStamp = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.BLOOD_GROUP).exists()) {
                                if (!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")) {
                                    bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.PHONE_NUMBER).exists()) {
                                if (!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")) {
                                    phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.EMAIL).exists()) {
                                if (!dataSnapshot.child(NodeNames.EMAIL).getValue().toString().equals("")) {
                                    email = dataSnapshot.child(NodeNames.EMAIL).getValue().toString();
                                }
                            }

                            if (dataSnapshot.child(NodeNames.USER_ORDER).exists()) {
                                if (!dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString().equals("")) {
                                    userOrder = dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString();
                                }
                            }

                            ModelClassAllUsers modelClassAllUsers = new ModelClassAllUsers(userName, userPhoto, userID, area, district, bloodGroup,
                                    age, email, gender, timeStamp, phone);

                            modelClassAllUsersList.add(modelClassAllUsers);
                            adapterAllUsers.notifyDataSetChanged();
                            arcLoader.stop();
                            arcLoader.setVisibility(View.GONE);
                        }

                    }
                    if (modelClassAllUsersList.isEmpty()) {
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                        tv_emptyListMessage.setVisibility(View.VISIBLE);
                    }


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchAllUsers() {


        mRootRef.child(NodeNames.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String userName = "";
                        String userPhoto = "";
                        String userID = "";
                        String age = "";
                        String gender = "";
                        String district = "";
                        String area = "";
                        String timeStamp = "";
                        String bloodGroup = "";
                        String email = "";
                        String phone = "";
                        String userOrder = "";

                        if (dataSnapshot.child(NodeNames.USER_NAME).exists()) {
                            if (!dataSnapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")) {
                                userName = dataSnapshot.child(NodeNames.USER_NAME).getValue().toString();
                            }
                        }
                        Log.d("tag", "userName" + userName);

                        if (dataSnapshot.child(NodeNames.USER_PHOTO).exists()) {
                            if (!dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")) {
                                userPhoto = dataSnapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.USER_ID).exists()) {
                            if (!dataSnapshot.child(NodeNames.USER_ID).getValue().toString().equals("")) {
                                userID = dataSnapshot.child(NodeNames.USER_ID).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.AGE).exists()) {
                            if (!dataSnapshot.child(NodeNames.AGE).getValue().toString().equals("")) {
                                age = dataSnapshot.child(NodeNames.AGE).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.GENDER).exists()) {
                            if (!dataSnapshot.child(NodeNames.GENDER).getValue().toString().equals("")) {
                                gender = dataSnapshot.child(NodeNames.GENDER).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.DISTRICT).exists()) {
                            if (!dataSnapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")) {
                                district = dataSnapshot.child(NodeNames.DISTRICT).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.AREA).exists()) {
                            if (!dataSnapshot.child(NodeNames.AREA).getValue().toString().equals("")) {
                                area = dataSnapshot.child(NodeNames.AREA).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.TIMESTAMP).exists()) {
                            if (!dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString().equals("")) {
                                timeStamp = dataSnapshot.child(NodeNames.TIMESTAMP).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.BLOOD_GROUP).exists()) {
                            if (!dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")) {
                                bloodGroup = dataSnapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.PHONE_NUMBER).exists()) {
                            if (!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")) {
                                phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.EMAIL).exists()) {
                            if (!dataSnapshot.child(NodeNames.EMAIL).getValue().toString().equals("")) {
                                email = dataSnapshot.child(NodeNames.EMAIL).getValue().toString();
                            }
                        }

                        if (dataSnapshot.child(NodeNames.USER_ORDER).exists()) {
                            if (!dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString().equals("")) {
                                userOrder = dataSnapshot.child(NodeNames.USER_ORDER).getValue().toString();
                            }
                        }

                        ModelClassAllUsers modelClassAllUsers = new ModelClassAllUsers(userName, userPhoto, userID, area, district, bloodGroup,
                                age, email, gender, timeStamp, phone);

                        modelClassAllUsersList.add(modelClassAllUsers);
                        adapterAllUsers.notifyDataSetChanged();
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);


                    }


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
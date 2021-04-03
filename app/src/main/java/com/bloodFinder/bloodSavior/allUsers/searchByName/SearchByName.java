package com.bloodFinder.bloodSavior.allUsers.searchByName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchByName extends AppCompatActivity {
    private List<ModelClassSearchByName> modelClassSearchByNameList;
    private AdapterSearchByName adapterSearchByName;
    private RecyclerView recyclerView;
    private SimpleArcLoader arcLoader;
    private TextView emptyListText;
    private EditText et_searchByName;

    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);

        Toolbar toolbar = findViewById(R.id.toolbar_searchByName);
        getSupportActionBar().hide();
        getSupportActionBar().setCustomView(toolbar);

        ImageView iv_back = findViewById(R.id.iv_back_toolbar_searchByName);

        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });
        arcLoader = findViewById(R.id.arcLoader_searchByName);
        et_searchByName = findViewById(R.id.et_searchByName_searchByName);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        modelClassSearchByNameList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_Posts_searchByName);
        adapterSearchByName = new AdapterSearchByName(this,modelClassSearchByNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSearchByName);



        fetchAllUsers();


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

                        ModelClassSearchByName modelClassSearchByName = new ModelClassSearchByName(userName, userPhoto, userID, area, district, bloodGroup,
                                age, email, gender, timeStamp, phone);

                        modelClassSearchByNameList.add(modelClassSearchByName);
                        adapterSearchByName.notifyDataSetChanged();
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);

                    }
                    textChangeListenerForSearchEditText();


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private  void textChangeListenerForSearchEditText(){
        et_searchByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterSearchByName.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    };
}
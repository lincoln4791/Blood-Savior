package com.bloodFinder.mybloodbank.userProfile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.bloodBank.BloodBank;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.login.LoginActivity;
import com.bloodFinder.mybloodbank.userProfile.editProfileActivity.EditProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private ImageView iv_profilePicture,iv_editProfile;
    private TextView tv_userName,tv_area,tv_district,tv_gender,tv_age,tv_phone,tv_bloodGroup,tv_donationStatus
            ,tv_lastDonation,tv_totalDonation;
    private String userName,area,district,gender,age,phone,bloodGroup,donationStatus,lastDonation,totalDonation,userPhoto,email;
    private TextView tv_logout,tv_bloodBank;

    private String myUID = FirebaseAuth.getInstance().getUid();
    private String userID;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_profilePicture = view.findViewById(R.id.iv_profilePicture_ProfileFragment);
        iv_editProfile = view.findViewById(R.id.iv_editProfile_profileFragment);
        tv_userName = view.findViewById(R.id.tv_userName_ProfileFragment);
        tv_area = view.findViewById(R.id.tv_area_ProfileFragment);
        tv_district = view.findViewById(R.id.tv_district_ProfileFragment);
        tv_gender = view.findViewById(R.id.tv_gender_profileFragment);
        tv_age = view.findViewById(R.id.tv_age_profileFragment);
        tv_phone = view.findViewById(R.id.tv_phone_ProfileFragment);
        tv_bloodGroup = view.findViewById(R.id.tv_bloodGroup_profileFragment);
        tv_donationStatus = view.findViewById(R.id.tv_donationStatus_profileFragment);
        tv_lastDonation = view.findViewById(R.id.tv_lastDonationDateValue_profileFragment);
        tv_totalDonation = view.findViewById(R.id.tv_totalDonationValue_profileFragment);
        tv_logout = view.findViewById(R.id.tv_logout_profileFragment);
        tv_bloodBank = view.findViewById(R.id.tv_bloodBanks_profileFragment);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        tv_bloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BloodBank.class));
            }
        });


        iv_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra(Extras.USER_ID,myUID);
                intent.putExtra(Extras.USER_NAME,userName);
                intent.putExtra(Extras.BLOOD_GROUP,bloodGroup);
                intent.putExtra(Extras.GENDER,gender);
                intent.putExtra(Extras.PHONE_NUMBER,phone);
                intent.putExtra(Extras.EMAIL,email);
                intent.putExtra(Extras.DISTRICT,district);
                intent.putExtra(Extras.AREA,area);
                intent.putExtra(Extras.AGE,age);
                intent.putExtra(Extras.PHONE_NUMBER,phone);
                startActivity(intent);
            }
        });



        mRootRef.child(NodeNames.USERS).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    userName="";
                    area="";
                    district="";
                    gender="";
                    age="";
                    phone="";
                    bloodGroup="";
                    donationStatus=getString(R.string.i_want_to_donate);
                    lastDonation=getString(R.string.dateNotGiven);
                    totalDonation=getString(R.string.zero);


                         if(snapshot.child(NodeNames.EMAIL).exists()){
                             if(snapshot.child(NodeNames.EMAIL).getValue()!=null){
                                 if(!snapshot.child(NodeNames.EMAIL).getValue().toString().equals("")){
                                     email = snapshot.getValue().toString();
                                 }
                             }
                         }

                          if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                              if(snapshot.child(NodeNames.USER_PHOTO).getValue()!=null){
                                  if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                      userPhoto = snapshot.getValue().toString();
                                  }
                              }
                          }
                    /*Glide.with(getContext()).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                            .error(R.drawable.ic_profile_picture).into(iv_profilePicture);*/
                    iv_profilePicture.setImageResource(R.drawable.lincolnn);



                        if(snapshot.child(NodeNames.USER_NAME).getValue()!=null){
                            if(!snapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")){
                                userName = snapshot.child(NodeNames.USER_NAME).getValue().toString();
                            }
                        }
                        tv_userName.setText(userName);

                        if(snapshot.child(NodeNames.AREA).getValue()!=null){
                            if(!snapshot.child(NodeNames.AREA).getValue().toString().equals("")){
                                area = snapshot.child((NodeNames.AREA)).getValue().toString();
                            }
                        }
                        tv_area.setText(area);


                        if(snapshot.child(NodeNames.DISTRICT).getValue()!=null){
                            if(!snapshot.child(NodeNames.DISTRICT).getValue().toString().equals("")){
                                district = snapshot.child(NodeNames.DISTRICT).getValue().toString();
                            }
                        }
                        tv_district.setText(district);

                        if(snapshot.child(NodeNames.GENDER).getValue()!=null){
                            if(!snapshot.child(NodeNames.GENDER).getValue().toString().equals("")){
                                gender = snapshot.child(NodeNames.GENDER).getValue().toString();
                            }
                        }
                        tv_gender.setText(gender);


                        if(snapshot.child(NodeNames.AGE).getValue()!=null){
                            if(!snapshot.child(NodeNames.AGE).getValue().toString().equals("")){
                                age = snapshot.child(NodeNames.AGE).getValue().toString();
                            }
                        }
                        tv_age.setText(age);


                        if(snapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                            if(!snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                                phone = snapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                            }
                        }
                        tv_phone.setText(phone);

                        if(snapshot.child(NodeNames.BLOOD_GROUP).getValue()!=null){
                            if(!snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString().equals("")){
                                bloodGroup = snapshot.child(NodeNames.BLOOD_GROUP).getValue().toString();
                            }
                        }
                        tv_bloodGroup.setText(bloodGroup);

                        if(snapshot.child(NodeNames.DONATION_STATUS).getValue()!=null){
                            if(!snapshot.child(NodeNames.DONATION_STATUS).getValue().toString().equals("")){
                                donationStatus = snapshot.child(NodeNames.DONATION_STATUS).getValue().toString();
                            }
                        }
                        tv_donationStatus.setText(donationStatus);

                        if(snapshot.child(NodeNames.TOTAL_DONATION).getValue()!=null){
                            if(!snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString().equals("")){
                                totalDonation = snapshot.child(NodeNames.TOTAL_DONATION).getValue().toString();
                            }
                        }
                        tv_totalDonation.setText(totalDonation);


                        if(snapshot.child(NodeNames.LAST_DONATION).getValue()!=null){
                            if(!snapshot.child(NodeNames.LAST_DONATION).getValue().toString().equals("")){
                                lastDonation = snapshot.child(NodeNames.LAST_DONATION).getValue().toString();
                            }
                        }
                        tv_lastDonation.setText(lastDonation);
                        iv_editProfile.setVisibility(View.VISIBLE);


                }
                else{
                    Toast.makeText(getContext(), "snapNotExists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "snapShotCanceled", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void logOut(){
        mAuth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finishAffinity();
    }
}
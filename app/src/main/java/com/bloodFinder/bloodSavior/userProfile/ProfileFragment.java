package com.bloodFinder.bloodSavior.userProfile;

import android.app.Dialog;
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

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.bloodBank.BloodBank;
import com.bloodFinder.bloodSavior.common.Extras;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.bloodFinder.bloodSavior.common.Util;
import com.bloodFinder.bloodSavior.common.UtilDB;
import com.bloodFinder.bloodSavior.donarsGroup.DonorsGroup;
import com.bloodFinder.bloodSavior.login.LoginActivity;
import com.bloodFinder.bloodSavior.userProfile.editProfileActivity.EditProfileActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private ImageView iv_profilePicture,iv_editProfile;
    private TextView tv_userName,tv_area,tv_district,tv_gender,tv_age,tv_phone,tv_bloodGroup,tv_donationStatus
            ,tv_lastDonation,tv_totalDonation;
    private String userName,area,district,gender,age,phone,bloodGroup,donationStatus,lastDonation,totalDonation,userPhoto,email;
    private TextView tv_logout,tv_bloodBank,tv_donorsGroup;


    private String myUID = FirebaseAuth.getInstance().getUid();
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
        if(Util.internetAvailable(getContext())){
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
            tv_donorsGroup = view.findViewById(R.id.tv_donorsGroup_profileFragment);


            mRootRef = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();

            tv_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Util.internetAvailable(getContext())){
                        confirmLogout();
                    }
                    else{
                        Util.noInternetConnection(getContext());
                    }

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
                    gotoEditProfileActivity();
                }
            });

            tv_donorsGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoDonorsGroupActivity();
                }
            });


            loadProfile();

        }
        else{
            Toast toast = new Toast(getContext());
            View toastView = LayoutInflater.from(getContext()).inflate(R.layout.toast_no_internet,null);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }


    }



    private void gotoDonorsGroupActivity() {
        startActivity(new Intent(getContext(),DonorsGroup.class));
    }

    private void loadProfile() {

        userName = UtilDB.USER_NAME;
        userPhoto = UtilDB.USER_PHOTO;
        area = UtilDB.AREA;
        district  = UtilDB.DISTRICT;
        gender = UtilDB.GENDER;
        age = UtilDB.AGE;
        phone = UtilDB.PHONE_NUMBER;
        bloodGroup = UtilDB.BLOOD_GROUP;
        donationStatus = UtilDB.DONATION_STATUS;
        lastDonation = UtilDB.LAST_DONATION;
        totalDonation = UtilDB.TOTAL_DONATION;

        tv_userName.setText(userName);
        tv_area.setText(area);
        tv_district.setText(district);
        tv_gender.setText(gender);
        tv_age.setText(age);
        tv_phone.setText(phone);
        tv_bloodGroup.setText(bloodGroup);
        tv_donationStatus.setText(getString(R.string.i_want_to_donate));
        tv_lastDonation.setText(R.string.demoDate);
        tv_totalDonation.setText(getString(R.string.five));

        Glide.with(getContext()).load(userPhoto).placeholder(R.drawable.ic_profile_picture)
                .error(R.drawable.ic_profile_picture).into(iv_profilePicture);

        iv_editProfile.setVisibility(View.VISIBLE);


    }

    private void gotoEditProfileActivity() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        intent.putExtra(Extras.USER_ID,myUID);
        intent.putExtra(Extras.USER_NAME,userName);
        intent.putExtra(Extras.USER_PHOTO,userPhoto);
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








    private void confirmLogout() {
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logout,null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();

        view.findViewById(R.id.btn_yes_alertImage_dialog_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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






    public void logOut(){
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String myUID = currentUser.getUid();
        DatabaseReference dbrTokensMy = mRootRef.child(NodeNames.TOKENS_FOLDER).child(myUID);


        dbrTokensMy.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseAuth.signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finishAffinity();

                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.somethingWentWrong,task.getException()), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }








}
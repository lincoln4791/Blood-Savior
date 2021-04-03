package com.bloodFinder.bloodSavior.invite;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bloodFinder.bloodSavior.R;
import com.bloodFinder.bloodSavior.common.NodeNames;
import com.bloodFinder.bloodSavior.common.SortPosts;
import com.bloodFinder.bloodSavior.common.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteFragment extends Fragment {

    private List<ModelClassInvite> modelClassInvitesList;
    private AdapterInvite adapterInvite;
    private RecyclerView recyclerView;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String myUID;

    private int RC_READ_CONTRACT = 1 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modelClassInvitesList = new ArrayList<>();
        adapterInvite = new AdapterInvite(modelClassInvitesList,getContext());
        recyclerView = view.findViewById(R.id.rv_InviteFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterInvite);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        myUID = FirebaseAuth.getInstance().getUid();

        showPhoneNumbers();

    }

    private void showPhoneNumbers(){
        if(Util.internetAvailable(getContext())){
            mRootRef.child(NodeNames.PHONE_NUMBERS).child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String userName = "";
                        String phone = "";
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child(NodeNames.USER_NAME).getValue()!=null){
                                if(!dataSnapshot.child(NodeNames.USER_NAME).getValue().toString().equals("")){
                                    userName = dataSnapshot.child(NodeNames.USER_NAME).getValue().toString();
                                }
                            }

                            if(dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue()!=null){
                                if(!dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString().equals("")){
                                    phone = dataSnapshot.child(NodeNames.PHONE_NUMBER).getValue().toString();
                                }
                            }

                            ModelClassInvite modelClassInvite = new ModelClassInvite(userName,phone);
                            modelClassInvitesList.add(modelClassInvite);
                        }
                        Collections.sort(modelClassInvitesList,new SortPosts.SortInviteList());
                        adapterInvite.notifyDataSetChanged();
                    }
                    else{

                        if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
                            getPhoneNumbers();
                        }
                        else{
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},RC_READ_CONTRACT);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
         Util.noInternetConnection(getContext());
        }
    }




    private void getPhoneNumbers() {
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext()){
            String nameOfTheUser = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String numberOfTheUser = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // When android gets phone number from mobile, phone number contains spaces, dashes nad underscore,so these replace function will remove those
            numberOfTheUser = numberOfTheUser.replace(" ","");
            numberOfTheUser = numberOfTheUser.replace("-","");
            numberOfTheUser = numberOfTheUser.replace("_","");

            ModelClassInvite userObject = new ModelClassInvite(nameOfTheUser,numberOfTheUser);
            Map detailsMap = new HashMap();
            detailsMap.put(NodeNames.USER_NAME,nameOfTheUser);
            detailsMap.put(NodeNames.PHONE_NUMBER,numberOfTheUser);
            String pushID= mRootRef.push().getKey();

            mRootRef.child(NodeNames.PHONE_NUMBERS).child(myUID).child(pushID).setValue(detailsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(getContext(),getString(R.string.failedToUpdatePhoneNUmber), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
        showPhoneNumbers();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==RC_READ_CONTRACT){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getPhoneNumbers();
            }
            else{
                Toast.makeText(getContext(), "Please grant Permission to read contract ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
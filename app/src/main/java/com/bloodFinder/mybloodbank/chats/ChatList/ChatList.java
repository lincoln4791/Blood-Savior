package com.bloodFinder.mybloodbank.chats.ChatList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ChatList extends Fragment {
    private TextView emptyChatListText;
    private View customProgressbar;
    private AdapterChatList adapter;
    private DatabaseReference dbrUsers,dbrChats,dbrFriendRequests;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private String myUID;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ChildEventListener childEventListener;
    private Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatllist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myUID = FirebaseAuth.getInstance().getUid();


        recyclerView = view.findViewById(R.id.rv_fragment_chat_chatlist_ID);
        customProgressbar = view.findViewById(R.id.customProgressbar_chatFragment_ID);
        emptyChatListText = view.findViewById(R.id.tv_fragment_chat_emptychatlistText_ID);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        dbrUsers = databaseReference.child(NodeNames.USERS);
        dbrChats = databaseReference.child(NodeNames.CHAT_FOLDER);
        dbrFriendRequests = databaseReference.child(NodeNames.FRIEND_REQUEST_STATUS_FOLDER).child(currentUser.getUid());
        query= dbrChats.child(myUID).orderByChild(NodeNames.TIMESTAMP);

        customProgressbar.setVisibility(View.VISIBLE);

        dbrChats.child(myUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loadProfiles(snapshot);
                }
                else{
                    customProgressbar.setVisibility(View.GONE);
                    emptyChatListText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    private void loadProfiles(DataSnapshot snapshot) {
        List<ModelClassChatList> chatListModelClassList;
        chatListModelClassList = new ArrayList<>();
        adapter = new AdapterChatList(getContext(),chatListModelClassList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        if(snapshot.exists()){
            emptyChatListText.setVisibility(View.GONE);
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                String lastMessage = "";
                String lastMessageTime = "";
                String unreadMessageCount = "";
                String userID = dataSnapshot.child(NodeNames.USER_ID).getValue().toString();

                if (dataSnapshot.child(NodeNames.LAST_MESSAGE).exists()) {
                    if (!dataSnapshot.child(NodeNames.LAST_MESSAGE).toString().equals("")) {
                        lastMessage = dataSnapshot.child(NodeNames.LAST_MESSAGE).getValue().toString();
                    }
                }

                if (dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).exists()) {
                    if (!dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).toString().equals("")) {
                        lastMessageTime = dataSnapshot.child(NodeNames.LAST_MESSAGE_TIME).getValue().toString();
                    }
                }

                if (dataSnapshot.child(NodeNames.UNREAD_COUNT).exists()) {
                    if (!dataSnapshot.child(NodeNames.UNREAD_COUNT).toString().equals("")) {
                        unreadMessageCount = dataSnapshot.child(NodeNames.UNREAD_COUNT).getValue().toString();
                    }
                }

                String finalUnreadMessageCount = unreadMessageCount;
                String finalLastMessage = lastMessage;
                String finalLastMessageTime = lastMessageTime;
                dbrUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = "";
                        String userPhoto = "";
                        if(snapshot.child(NodeNames.NAME).exists()){
                            if(!snapshot.child(NodeNames.NAME).getValue().toString().equals("")){
                                userName = snapshot.child(NodeNames.NAME).getValue().toString();
                                Log.d("tag","username : "+userName);
                            }
                        }

                        if(snapshot.child(NodeNames.USER_PHOTO).exists()){
                            if(!snapshot.child(NodeNames.USER_PHOTO).getValue().toString().equals("")){
                                userPhoto = snapshot.child(NodeNames.USER_PHOTO).getValue().toString();
                            }
                        }

                        ModelClassChatList object = new ModelClassChatList(userName,userID,userPhoto, finalUnreadMessageCount, finalLastMessage, finalLastMessageTime);
                        chatListModelClassList.add(object);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            customProgressbar.setVisibility(View.GONE);

        }
        else{
            customProgressbar.setVisibility(View.GONE);
            emptyChatListText.setVisibility(View.VISIBLE);
        }
    }

}
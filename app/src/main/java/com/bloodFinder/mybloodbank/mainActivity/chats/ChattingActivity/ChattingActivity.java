package com.bloodFinder.mybloodbank.mainActivity.chats.ChattingActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.common.NodeNames;
import com.bloodFinder.mybloodbank.common.Util;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_writeMessage;
    private ImageView sendAttachment, sendMessageBtn;
    private LinearLayout ll_imageSendingPreview;
    private ImageView iv_imageToLoad;
    private FloatingActionButton floatingActionButton;

    private ChildEventListener childEventListener;
    private View customProgressBar;
    private List<ModelClassChatting> chatsModelList;
    private ChattingAdapter chattingAdapter;
    private Query query;
    private int currentPage = 1;
    private Context context;
    private LinearLayout llSendChatLayout;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference, dbrChat, dbrUsers, dbrMessages;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String myUID = currentUser.getUid(), userID, userName, userPhotoName;

    //loadSendingImagePreview


    //Bottom Sheet Dialogue
    private LinearLayout ll_camera, ll_gallery, ll_video;
    private ImageView iv_close;
    private BottomSheetDialog bottomSheetDialog;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 101;
    private static final int REQUEST_CODE_PICK_IMAGE = 102;
    private static final int REQUEST_CODE_PICK_VIDEO = 103;
    private static final int REQUEST_CODE_FORWARD_MESSAGE = 105;
    private static final int RECORD_PER_PAGE = 30;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 104;

    private LinearLayout llProgress;

    //ActionBar
    private ImageView actionBarProfilePicture, actionBarOnlineStatusImage;
    private TextView actionBarUserName, actionBarOnlineStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            View actionBarViewGroup = (View) LayoutInflater.from(this).inflate(R.layout.sample_actionbar_layout, null);
            actionBar.setTitle("");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setCustomView(actionBarViewGroup);
            actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);

            actionBarUserName = actionBarViewGroup.findViewById(R.id.tv_userName_samoleLayoutActionBar);
            actionBarProfilePicture = actionBarViewGroup.findViewById(R.id.displayProfilePicture_sampleLayoutActionBar_ID);
            actionBarOnlineStatus = actionBarViewGroup.findViewById(R.id.tvOnLineStatus_sampleLayoutActionBar);
            actionBarOnlineStatusImage = actionBarViewGroup.findViewById(R.id.ivOnlineStatus_sampleLayoutActionBar);
        }

        recyclerView = findViewById(R.id.rv_chattingActivity);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_chattingActivity);
        //customProgressBar = findViewById(R.id.customProgressBarID);
        et_writeMessage = findViewById(R.id.et_writeMessage_chattingActivity);
        sendAttachment = findViewById(R.id.iv_sendAttachment_chattingActivity);
        sendMessageBtn = findViewById(R.id.iv_sendMessage_chattingActivity);
        llProgress = findViewById(R.id.llProgress);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dbrChat = databaseReference.child(NodeNames.CHAT_FOLDER);
        dbrUsers = databaseReference.child(NodeNames.USERS);
        dbrMessages = databaseReference.child(NodeNames.MESSAGES_FOLDER);
        ll_imageSendingPreview = findViewById(R.id.ll_sendingImagePreview_chattingActivity);
        iv_imageToLoad = findViewById(R.id.iv_ImageToloadInPreview_ChattingActivity);
        //btn_sendImageFromPreviewBtn = findViewById(R.id.iv_sendImageFromPreview_chatActivity_ID);
        llSendChatLayout = findViewById(R.id.ll_sendChat_chattingActivity);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        if (getIntent().hasExtra(Extras.USER_ID)) {
            userID = getIntent().getStringExtra(Extras.USER_ID);
        }
        if (getIntent().hasExtra(Extras.USER_PHOTO_NAME)) {
            userName = getIntent().getStringExtra(Extras.USER_NAME);
        }
        if (getIntent().hasExtra(Extras.USER_NAME)) {
            userPhotoName = getIntent().getStringExtra(Extras.USER_PHOTO_NAME);
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatsModelList = new ArrayList<>();
        chattingAdapter = new ChattingAdapter(this, chatsModelList);


        //Custom ActionBar
        databaseReference.child(NodeNames.USERS).child(userID).child(NodeNames.PROFILE_PICTURES).child(NodeNames.PHOTO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (!snapshot.getValue().toString().equals("")) {
                                String userPhotoRef = snapshot.getValue().toString();
                                Glide.with(getApplicationContext()).load(Uri.parse(userPhotoRef)).into(actionBarProfilePicture);
                            } else {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_profile_picture).into(actionBarProfilePicture);
                            }

                        } else {
                            Glide.with(getApplicationContext()).load(R.drawable.ic_profile_picture).into(actionBarProfilePicture);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        actionBarUserName.setText(userName);


        //Loading message
        loadMessage();
        //This code will remove unread count message when user seen any chat message
        databaseReference.child(NodeNames.CHAT_FOLDER).child(myUID).child(userID).child(NodeNames.UNREAD_COUNT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    databaseReference.child(NodeNames.CHAT_FOLDER).child(myUID).child(userID).child(NodeNames.UNREAD_COUNT)
                            .setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(context, "Unread count removed", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(context, "Failed to Unread count removed"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        recyclerView.scrollToPosition(chatsModelList.size() - 1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                loadMessage();
            }
        });
        recyclerView.setAdapter(chattingAdapter);


        //Sending Attachment-Inflating attachment window Layout
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.sample_layout_attachments, null);
        bottomSheetDialog.setContentView(view);
        ll_camera = view.findViewById(R.id.ll_Camera_sampleLayoutAttachment);
        ll_video = view.findViewById(R.id.ll_video_sampleLayoutAttachment);
        ll_gallery = view.findViewById(R.id.ll_gallery_sampleLayoutAttachment);
        iv_close = view.findViewById(R.id.iv_close_sampleLayoutAttachment);

        ll_video.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_gallery.setOnClickListener(this);
        iv_close.setOnClickListener(this);


        // sending a message to a user
        sendMessageBtn.setOnClickListener(this);
        sendAttachment.setOnClickListener(this);


    }


    private void sendMessage(String message, String messageType, String pushID) {
        try {
            if (message != "") {
                HashMap messageHashMap = new HashMap();
                messageHashMap.put(NodeNames.MESSAGE_ID, pushID);
                messageHashMap.put(NodeNames.MESSAGE_FROM, myUID);
                messageHashMap.put(NodeNames.MESSAGE_TYPE, messageType);
                messageHashMap.put(NodeNames.MESSAGE_TIME, ServerValue.TIMESTAMP);
                messageHashMap.put(NodeNames.MESSAGE, message);


                String currentUserReference = NodeNames.MESSAGES_FOLDER + "/" + myUID + "/" + userID + "/" + pushID;
                String chatUserReference = NodeNames.MESSAGES_FOLDER + "/" + userID + "/" + myUID + "/" + pushID;

                HashMap messageUserPut = new HashMap();
                messageUserPut.put(currentUserReference, messageHashMap);
                messageUserPut.put(chatUserReference, messageHashMap);

                databaseReference.updateChildren(messageUserPut, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Toast.makeText(ChattingActivity.this, getString(R.string.failedToSendMessage) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(ChatsActivity.this, R.string.successful, Toast.LENGTH_SHORT).show();
                            String notificationTitle = "";
                            String notificationMessage = message;
                            if (messageType.equals(Constants.MESSAGE_TYPE_TEXT)) {
                                notificationTitle = getString(R.string.newMessageFrom, currentUser.getDisplayName());
                            } else if (messageType.equals(Constants.MESSAGE_TYPE_IMAGE)) {
                                notificationTitle = getString(R.string.newImageReceivedFrom, currentUser.getDisplayName());
                                //notificationMessage = getString(R.string.imageReceived);
                            } else if (messageType.equals(Constants.MESSAGE_TYPE_VIDEO)) {
                                notificationTitle = getString(R.string.newVideoReceivedFrom, currentUser.getDisplayName());
                                //notificationMessage = getString(R.string.videoReceived);
                            }


                            Util.sendNotification(ChattingActivity.this, notificationTitle, notificationMessage, userID);
                            Util.updateChatDetails(context, userID, notificationMessage);
                            et_writeMessage.setText("");
                        }
                    }
                });


            }

        } catch (Exception exception) {
            Toast.makeText(ChattingActivity.this, getString(R.string.failedToSendMessage) + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void loadMessage() {

        chatsModelList.clear();
        query = dbrMessages.child(myUID).child(userID).limitToLast(currentPage * RECORD_PER_PAGE);

/*        if(childEventListener != null){
            query.removeEventListener(childEventListener);
        }*/

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ModelClassChatting chatsModelObject = snapshot.getValue(ModelClassChatting.class);
                chatsModelList.add(chatsModelObject);
                recyclerView.scrollToPosition(chatsModelList.size() - 1);
                swipeRefreshLayout.setRefreshing(false);
                //chatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //ChatsModel chatsModelObject = snapshot.getValue(ChatsModel.class);
                //chatsModelList.add(chatsModelObject);
                //recyclerView.scrollToPosition(chatsModelList.size()-1);
                //swipeRefreshLayout.setRefreshing(false);
                //chatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadMessage();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);

            }
        };

        query.addChildEventListener(childEventListener);
    }


    // On click Listeners
    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.iv_sendMessage_chattingActivity)) {
            String message = et_writeMessage.getText().toString();
            String pushID = databaseReference.child(NodeNames.MESSAGES_FOLDER).child(myUID).child(userID).push().getKey();
            sendMessage(message, Constants.MESSAGE_TYPE_TEXT, pushID);
        }

        if (v == findViewById(R.id.iv_sendAttachment_chattingActivity)) {
            //checking permission whether it can access mobile storage or not
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.show();
                    //Toast.makeText(this, "gg", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

            //it will close the keyboard when bottomSheetDialogue opens
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }


        }

        if (v == ll_camera) {
            //Toast.makeText(this,"pic Image clicked",Toast.LENGTH_SHORT);
            bottomSheetDialog.dismiss();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
        } else if (v == ll_gallery) {
            //Toast.makeText(this,"gallery clicked",Toast.LENGTH_SHORT);
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

        } else if (v == ll_video) {
            //Toast.makeText(this,"Video clicked",Toast.LENGTH_SHORT);
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
        } else if (v.equals(iv_close)) {
            //Toast.makeText(this,"close clicked",Toast.LENGTH_SHORT);
            bottomSheetDialog.dismiss();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.show();
                }
            } else {
                Toast.makeText(this, "Storage Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                ll_imageSendingPreview.setVisibility(View.VISIBLE);
                llSendChatLayout.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(bitmap).into(iv_imageToLoad);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_imageSendingPreview.setVisibility(View.GONE);
                        llSendChatLayout.setVisibility(View.VISIBLE);
                        floatingActionButton.setVisibility(View.GONE);
                        uploadBytes(byteArrayOutputStream, Constants.MESSAGE_TYPE_IMAGE);
                    }
                });

                //uploadBytes(byteArrayOutputStream,Constants.MESSAGE_TYPE_IMAGE);

            } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                Uri uri = data.getData();

                ll_imageSendingPreview.setVisibility(View.VISIBLE);
                llSendChatLayout.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(iv_imageToLoad);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_imageSendingPreview.setVisibility(View.GONE);
                        llSendChatLayout.setVisibility(View.VISIBLE);
                        floatingActionButton.setVisibility(View.GONE);
                        uploadImagesAndVideos(uri, Constants.MESSAGE_TYPE_IMAGE);
                    }
                });

            } else if (requestCode == REQUEST_CODE_PICK_VIDEO) {
                Uri uri = data.getData();
                ll_imageSendingPreview.setVisibility(View.VISIBLE);
                llSendChatLayout.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(uri).into(iv_imageToLoad);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_imageSendingPreview.setVisibility(View.GONE);
                        llSendChatLayout.setVisibility(View.VISIBLE);
                        floatingActionButton.setVisibility(View.GONE);
                        uploadImagesAndVideos(uri, Constants.MESSAGE_TYPE_VIDEO);
                    }
                });

            } else if (requestCode == REQUEST_CODE_FORWARD_MESSAGE) {
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra(Extras.MESSAGE_ID, data.getStringExtra(Extras.MESSAGE_ID));
                intent.putExtra(Extras.MESSAGE, data.getStringExtra(Extras.MESSAGE));
                intent.putExtra(Extras.MESSAGE_TYPE, data.getStringExtra(Extras.MESSAGE_TYPE));

                intent.putExtra(Extras.USER_NAME, data.getStringExtra(Extras.USER_NAME));
                intent.putExtra(Extras.USER_ID, data.getStringExtra(Extras.USER_ID));
                intent.putExtra(Extras.USER_PHOTO_NAME, data.getStringExtra(Extras.USER_PHOTO_NAME));

                startActivity(intent);
                finish();
            }
        }
    }


    public void uploadImagesAndVideos(Uri uri, String messageType) {
        String pushID = databaseReference.child(NodeNames.MESSAGES_FOLDER).child(myUID).child(userID).push().getKey();
        String folderName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? NodeNames.MESSAGE_IMAGES_FOLDER : NodeNames.MESSAGE_VIDEOS_FOLDER;
        String fileName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? pushID + ".jpg" : pushID + ".mp4";

        StorageReference fileRef = storageReference.child(folderName).child(fileName);
        UploadTask uploadTask = fileRef.putFile(uri);
        uploadingProgress(uploadTask, fileRef, pushID, messageType);
    }


    public void uploadBytes(ByteArrayOutputStream byteArrayOutputStream, String messageType) {
        String pushID = databaseReference.child(NodeNames.MESSAGES_FOLDER).child(myUID).child(userID).push().getKey();
        String folderName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? NodeNames.MESSAGE_IMAGES_FOLDER : NodeNames.MESSAGE_VIDEOS_FOLDER;
        String fileName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? pushID + ".jpg" : pushID + ".mp4";

        StorageReference fileRef = storageReference.child(folderName).child(fileName);
        UploadTask uploadTask = fileRef.putBytes(byteArrayOutputStream.toByteArray());
        uploadingProgress(uploadTask, fileRef, pushID, messageType);
    }


    private void uploadingProgress(UploadTask task, StorageReference fileRef, String pushID, String messageType) {

        View view = LayoutInflater.from(this).inflate(R.layout.sample_uploading_progressbar_layout, null);
        ProgressBar pb_uploadingProgress = view.findViewById(R.id.pb_sampleUploadigLayout);
        TextView tv_uploadingText = view.findViewById(R.id.tv_uploading_samplaUploadingPB);
        ImageView iv_Play = view.findViewById(R.id.iv_play_sampleUploadinglayout);
        ImageView iv_Pause = view.findViewById(R.id.iv_pause_sampleUploadinglayout);
        ImageView iv_Stop = view.findViewById(R.id.iv_stop_sampleUploadinglayout);

        iv_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.pause();
                iv_Play.setVisibility(View.VISIBLE);
                iv_Pause.setVisibility(View.GONE);
            }
        });

        iv_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.resume();
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.GONE);
            }
        });

        iv_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel();

            }
        });

        llProgress.addView(view);
        tv_uploadingText.setText("Uploading " + messageType + " : " + "0%");
        //tv_uploadingText.setText(getString(R.string.uploadProgressText));

        task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int progress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pb_uploadingProgress.setProgress(progress);
                tv_uploadingText.setText("Uploading " + messageType + " : " + String.valueOf(progress) + "%");

            }
        });

        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                llProgress.removeView(view);
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            sendMessage(downloadUri, messageType, pushID);


                        }
                    });
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void deleteMessage(String messageType, String messageID) {
        Log.d("tag", "message type : " + messageType);
        Log.d("tag", "message ID : " + messageID);

        DatabaseReference dbrMessageUser = databaseReference.child(NodeNames.MESSAGES_FOLDER).child(userID).child(myUID).child(messageID);

        dbrMessageUser.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference dbrMessageMy = databaseReference.child(NodeNames.MESSAGES_FOLDER).child(myUID).child(userID).child(messageID);
                    dbrMessageMy.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                if (messageType.equals(Constants.MESSAGE_TYPE_IMAGE)) {
                                    StorageReference strImages = storageReference.child(NodeNames.MESSAGE_IMAGES_FOLDER).child(messageID + ".jpg");
                                    strImages.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChattingActivity.this, "Message has been deleted successfully ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ChattingActivity.this, getString(R.string.failedToDeleteMessage) + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (messageType.equals(Constants.MESSAGE_TYPE_VIDEO)) {
                                    StorageReference strVideos = storageReference.child(NodeNames.MESSAGE_VIDEOS_FOLDER).child(messageID + ".mp4");
                                    strVideos.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChattingActivity.this, "Message has been deleted successfully ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ChattingActivity.this, getString(R.string.failedToDeleteMessage) + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            } else {
                                Toast.makeText(ChattingActivity.this, getString(R.string.failedToDeleteMessage) + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(ChattingActivity.this, getString(R.string.failedToDeleteMessage) + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void downloadFile(String messageType, String messageID, boolean isShare) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        } else {
            //Log.d("tag","message type e : "+messageType);
            //Log.d("tag","message ID e : "+messageID);
            String folderName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? NodeNames.MESSAGE_IMAGES_FOLDER : NodeNames.MESSAGE_VIDEOS_FOLDER;
            String fileName = messageType.equals(Constants.MESSAGE_TYPE_IMAGE) ? messageID + ".jpg" : messageID + ".mp4";
            StorageReference fileRef = storageReference.child(folderName).child(fileName);
            String localFilePath = getExternalFilesDir(null).getAbsolutePath() + "/" + fileName;

            File localFile = new File(localFilePath);

            try {
                if (localFile.exists() || localFile.createNewFile()) {
                    FileDownloadTask fileDownloadTask = fileRef.getFile(localFile);
                    View view = LayoutInflater.from(this).inflate(R.layout.sample_uploading_progressbar_layout, null);

                    ProgressBar pb_uploadingProgress = view.findViewById(R.id.pb_sampleUploadigLayout);
                    TextView tv_uploadingText = view.findViewById(R.id.tv_uploading_samplaUploadingPB);
                    ImageView iv_Play = view.findViewById(R.id.iv_play_sampleUploadinglayout);
                    ImageView iv_Pause = view.findViewById(R.id.iv_pause_sampleUploadinglayout);
                    ImageView iv_Stop = view.findViewById(R.id.iv_stop_sampleUploadinglayout);

                    iv_Pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fileDownloadTask.pause();
                            iv_Play.setVisibility(View.VISIBLE);
                            iv_Pause.setVisibility(View.GONE);
                        }
                    });

                    iv_Play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fileDownloadTask.resume();
                            iv_Pause.setVisibility(View.VISIBLE);
                            iv_Play.setVisibility(View.GONE);
                        }
                    });

                    iv_Stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fileDownloadTask.cancel();

                        }
                    });

                    llProgress.addView(view);
                    tv_uploadingText.setText("Downloading " + messageType + "0%");
                    fileDownloadTask.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                            int progress = (int) (100 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                            pb_uploadingProgress.setProgress(progress);
                        }
                    });
                    fileDownloadTask.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            llProgress.removeView(view);
                            if (task.isSuccessful()) {

                                //this code will execute only if user wants to share downloaded image or video
                                if (isShare) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(intent.EXTRA_STREAM, Uri.parse(localFilePath));
                                    if (messageType.equals(Constants.MESSAGE_TYPE_IMAGE)) {
                                        intent.setType("image/jpg");
                                        startActivity(intent);
                                    } else if (messageType.equals(Constants.MESSAGE_TYPE_VIDEO)) {
                                        intent.setType("video/mp4");
                                        startActivity(intent);
                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(llProgress, "view", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("VIEW", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Uri uri = Uri.parse(localFilePath);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            if (messageType.equals(Constants.MESSAGE_TYPE_IMAGE)) {
                                                intent.setDataAndType(uri, "image/jpg");
                                                startActivity(intent);
                                            } else if (messageType.equals(Constants.MESSAGE_TYPE_VIDEO)) {
                                                intent.setDataAndType(uri, "video/mp4");
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                    snackbar.show();
                                }


                            } else {
                                Toast.makeText(ChattingActivity.this, getString(R.string.failedToDownloadFile) + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    Toast.makeText(this, "Failed To Download FIle", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Failed to download file : " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}


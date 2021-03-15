package com.bloodFinder.mybloodbank.mainActivity.chats.ChattingActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Constants;
import com.bloodFinder.mybloodbank.common.Extras;
import com.bloodFinder.mybloodbank.userProfile.UserProfile;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.MyChatsHolder> {

    private Context context;
    private List<ModelClassChatting> modelClassChattingList;
    private FirebaseAuth firebaseAuth;
    private ConstraintLayout selectedLayout;

    //chat options, reply, download, share, forward etc...
    private ActionMode actionMode;

    public ChattingAdapter(Context context, List<ModelClassChatting> ModelClassChattingList) {
        this.context = context;
        this.modelClassChattingList = ModelClassChattingList;
    }

    @NonNull
    @Override
    public MyChatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_chatting_layout,parent,false);
        return new MyChatsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatsHolder holder, int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        String fromUserID = modelClassChattingList.get(position).getMessage_from();
        String currentUserID = firebaseAuth.getCurrentUser().getUid();

        SimpleDateFormat dsf = new SimpleDateFormat("dd-MM-yy HH-mm");
        String dateTime = dsf.format(new Date(modelClassChattingList.get(position).getMessage_time()));
        String [] splitString = dateTime.split(" ");
        String messageTime = splitString[1].replace("-",":");


//Implementation of Chat Conversation View
        if (fromUserID.equals(currentUserID)){
            if(modelClassChattingList.get(position).getMessage_type().equals(Constants.MESSAGE_TYPE_TEXT)){
                holder.ll_sentMessage.setVisibility(View.VISIBLE);
                holder.cv_sentImageMessage.setVisibility(View.GONE);
            }
            else{
                holder.ll_sentMessage.setVisibility(View.GONE);
                holder.cv_sentImageMessage.setVisibility(View.VISIBLE);
            }

            holder.ll_receivedMessage.setVisibility(View.GONE);
            holder.cv_receivedImageMessage.setVisibility(View.GONE);
            holder.tv_sentMessageText.setText(modelClassChattingList.get(position).getMessage());
            holder.tv_sentMessageTime.setText(messageTime);
            holder.tv_sentImageMessageTime.setText(messageTime);
            Glide.with(context).load(modelClassChattingList.get(position).getMessage()).placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture).into(holder.iv_sentImageMessage);
        }

        else {

            if(modelClassChattingList.get(position).getMessage_type().equals(Constants.MESSAGE_TYPE_TEXT)){
                holder.ll_receivedMessage.setVisibility(View.VISIBLE);
                holder.cv_receivedImageMessage.setVisibility(View.GONE);
            }
            else{
                holder.ll_receivedMessage.setVisibility(View.GONE);
                holder.cv_receivedImageMessage.setVisibility(View.VISIBLE);
            }
            holder.ll_sentMessage.setVisibility(View.GONE);
            holder.cv_sentImageMessage.setVisibility(View.GONE);
            holder.tv_receivedMessageText.setText(modelClassChattingList.get(position).getMessage());
            holder.tv_receivedMessageTime.setText(messageTime);
            holder.tv_receivedImageMessageTime.setText(messageTime);
            Glide.with(context).load(modelClassChattingList.get(position).getMessage()).placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture).into(holder.iv_receivedImageMessage);
        }


        holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE, modelClassChattingList.get(position).getMessage());
        holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE_TYPE, modelClassChattingList.get(position).getMessage_type());
        holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE_ID, modelClassChattingList.get(position).getMessage_id());
        //selectedLayout = holder.cl_messageSampleLayout;




        holder.cl_messageSampleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageType = v.getTag(R.id.TAG_MESSAGE_TYPE).toString();
                Uri uri = Uri.parse(v.getTag(R.id.TAG_MESSAGE).toString());
                if(messageType.equals(Constants.MESSAGE_TYPE_IMAGE)){
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setDataAndType(uri,"image/jpg");
                    context.startActivity(intent);
                }
                else if(messageType.equals(Constants.MESSAGE_TYPE_VIDEO)){
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setDataAndType(uri,"video/mp4");
                    context.startActivity(intent);
                }
            }
        });







        holder.cl_messageSampleLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(actionMode != null){
                    return false;
                }
                else{
                    holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE, modelClassChattingList.get(position).getMessage());
                    holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE_TYPE, modelClassChattingList.get(position).getMessage_type());
                    holder.cl_messageSampleLayout.setTag(R.id.TAG_MESSAGE_ID, modelClassChattingList.get(position).getMessage_id());

                    //Log.d("tag","TagMessage ID : "+holder.cl_messageSampleLayout.getTag(R.id.TAG_MESSAGE_ID).toString());
                    //Log.d("tag","TagMessage TYPE : "+holder.cl_messageSampleLayout.getTag(R.id.TAG_MESSAGE_TYPE).toString());
                    selectedLayout = holder.cl_messageSampleLayout;
                    actionMode = ((AppCompatActivity)context).startSupportActionMode(actionModeCallback);

                    holder.cl_messageSampleLayout.setBackgroundColor(context.getResources().getColor(R.color.aqua));
                    return true;
                }



            }
        });




    }

    @Override
    public int getItemCount() {
        return modelClassChattingList.size();
    }

    public class MyChatsHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_sentMessage,ll_receivedMessage;
        private CardView cv_sentImageMessage, cv_receivedImageMessage;
        private TextView tv_sentMessageText,tv_sentMessageTime,tv_receivedMessageText, tv_receivedMessageTime,
                tv_sentImageMessageTime,tv_receivedImageMessageTime;
        private ImageView iv_sentImageMessage,iv_receivedImageMessage;
        private ConstraintLayout cl_messageSampleLayout;



        public MyChatsHolder(@NonNull View itemView) {
            super(itemView);
            ll_sentMessage = itemView.findViewById(R.id.ll_SentMessagec_sampleMessagesListLayout);
            ll_receivedMessage = itemView.findViewById(R.id.ll_receivedMessage_sampleMessagesListLayout);
            cv_sentImageMessage = itemView.findViewById(R.id.cv_SentImageMessage_sampleMessagesListLayout);
            cv_receivedImageMessage = itemView.findViewById(R.id.cv_receivedImageMessage_sampleMessagesListLayout);
            tv_sentMessageText = itemView.findViewById(R.id.tv_sentMessage_sampleMessagesListLayout);
            iv_sentImageMessage = itemView.findViewById(R.id.iv_SentImageMessage_sampleMessagesListLayout);
            tv_sentMessageTime= itemView.findViewById(R.id.tv_sentMessageTime_sentMessage_sampleMessagesListLayout);
            tv_sentImageMessageTime = itemView.findViewById(R.id.tv_SentImageMessageTime_sampleMessagesListLayout);
            tv_receivedMessageText = itemView.findViewById(R.id.tv_receivedMessage_sampleMessagesListLayout);
            iv_receivedImageMessage = itemView.findViewById(R.id.iv_receivedImageMessage_sampleMessagesListLayout);
            tv_receivedMessageTime = itemView.findViewById(R.id.tv_receivedMessageTime_sampleMessagesListLayout);
            tv_receivedImageMessageTime = itemView.findViewById(R.id.tv_receivedImageMessageTime_sampleMessagesListLayout);
            cl_messageSampleLayout = itemView.findViewById(R.id.cl_sampleMessagesListLayout);
        }
    }




    public ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = new MenuInflater(context);
            menuInflater.inflate(R.menu.menu_chatting_options,menu);
            MenuItem mnuItemsDownload = menu.findItem(R.id.mnu_chatOptions_download);
            String selectableMessageType = selectedLayout.getTag(R.id.TAG_MESSAGE_TYPE).toString();
            if(selectableMessageType.equals(Constants.MESSAGE_TYPE_TEXT)){

                mnuItemsDownload.setVisible(false);
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemID = item.getItemId();
            String selectedMessageID = selectedLayout.getTag(R.id.TAG_MESSAGE_ID).toString();
            String selectedMessageType = selectedLayout.getTag(R.id.TAG_MESSAGE_TYPE).toString();
            String selectedMessage = selectedLayout.getTag(R.id.TAG_MESSAGE).toString();
            Log.d("tag","tagedd"+selectedMessageType);


            switch (itemID){

                case R.id.mnuChatOptions_dlt:
                    //Toast.makeText(context, "Delete Button Pressed", Toast.LENGTH_SHORT).show();
                    if(context instanceof ChattingActivity){
                        ((ChattingActivity)context).deleteMessage(selectedMessageType,selectedMessageID);
                    }

                    actionMode.finish();
                    break;


                case R.id.mnu_chatOptions_share:
                    //Toast.makeText(context, "Share Button Pressed", Toast.LENGTH_SHORT).show();
                    if(selectedMessageType.equals(Constants.MESSAGE_TYPE_TEXT)){
                        Intent intentShareText = new Intent();
                        intentShareText.putExtra(Intent.EXTRA_TEXT,selectedMessage);
                        intentShareText.setAction(Intent.ACTION_SEND);
                        intentShareText.setType("text/plain");
                        context.startActivity(intentShareText);
                    }
                    else{
                        ((ChattingActivity)context).downloadFile(selectedMessageType,selectedMessageID,true);
                    }
                    actionMode.finish();
                    break;


                case R.id.mnu_chatOptions_download:

                    if(context instanceof ChattingActivity){
                        ((ChattingActivity)context).downloadFile(selectedMessageType,selectedMessageID,false);
                        Toast.makeText(context, "download Button Pressed", Toast.LENGTH_SHORT).show();
                    }
                    actionMode.finish();
                    break;

                default: return false;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode= null;
            selectedLayout.setBackgroundColor(context.getResources().getColor(R.color.chatBackground));

        }
    };


}

package com.bloodFinder.mybloodbank.mainActivity.chats.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodFinder.mybloodbank.R;
import com.bloodFinder.mybloodbank.common.Extras;

import java.util.List;

public class AdapterInvite extends RecyclerView.Adapter<AdapterInvite.MyViewHolder> {
    private List<ModelClassInvite> modelClassInviteList;
    private Context context;

    public AdapterInvite(List<ModelClassInvite> modelClassInviteList, Context context) {
        this.modelClassInviteList = modelClassInviteList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterInvite.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_invite,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInvite.MyViewHolder holder, int position) {
            holder.tv_userName.setText(modelClassInviteList.get(position).getUserName());

            holder.btn_invite.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    String subject= "Blood Bank Bangladesh";
                    String body = "মানুষ মানুষের জন্য, আর একটি মানুষকে ও রক্তের অভাবে মৃত্যুর পথযাত্রী হতে দিব না এই প্রত্যাশায় শুরু হল আমাদের নতুন " +
                            "Blood Bank Bangladesh এর পথযাত্রা। সাথে থাকুন আমাদের";
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                    intent.putExtra(Intent.EXTRA_TEXT,body);
                    intent.putExtra(Intent.EXTRA_EMAIL,"mahmuduulkarim@gmail.com");
                    intent.putExtra(Intent.EXTRA_PHONE_NUMBER,modelClassInviteList.get(position).getPhone());
                    context.startActivity(Intent.createChooser(intent,"share with "));
                }
            });
    }

    @Override
    public int getItemCount() {
        return modelClassInviteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button btn_invite;
        private TextView tv_userName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_invite=itemView.findViewById(R.id.btn_invite_sampleInvite);
            tv_userName = itemView.findViewById(R.id.tv_username_sampleLayout_inviteFragment);


        }
    }
}

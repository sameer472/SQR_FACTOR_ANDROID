package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class ChatWithAFriendActivityAdapter extends RecyclerView.Adapter<ChatWithAFriendActivityAdapter.MyViewHolder> {

    private ArrayList<MessageClass> messageClassArrayList;
    private Context context;
    private int friendId;
    private String friendProfileUrl,friendName;
    int count=0;

    public ChatWithAFriendActivityAdapter(ArrayList<MessageClass> messageClassArrayList, Context context,int friendId,String friendProfileUrl,String friendName) {
        this.messageClassArrayList = messageClassArrayList;
        this.context = context;
        this.friendId=friendId;
        this.friendProfileUrl=friendProfileUrl;
        this.friendName=friendName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_with_a_friend_activity_adapter, parent, false);


        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        MessageClass messageClass=messageClassArrayList.get(position);
        int fromId=messageClass.getUserFrom();

        String dtc = messageClassArrayList.get(position).getCreatedAt();
        Log.v("timeChat",UtilsClass.getTime(dtc)+messageClassArrayList.get(position).getUpdatedAt());
        if(fromId==friendId)
        {
            int x=count++;

            holder.freindLayout.setVisibility(View.VISIBLE);
            holder.frndChatMessage.setText(messageClassArrayList.get(position).getChat());
            holder.frndchatTime.setText(UtilsClass.getTime(dtc));
            holder.frndName.setText(friendName);
            Glide.with(context).load(UtilsClass.baseurl1+friendProfileUrl)
                    .into(holder.frndProfile);

        }
        else
        {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.myMessage.setText(messageClassArrayList.get(position).getChat());
            holder.myChatTime.setText(UtilsClass.getTime(dtc));
            holder.myName.setText(userClass.getUser_name());
            Glide.with(context).load(UtilsClass.baseurl1+userClass.getProfile())
                    .into(holder.myProfile);

        }





    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView frndProfile,myProfile;
        TextView frndName,frndChatMessage,frndchatTime;
        TextView myName,myMessage,myChatTime;
        RelativeLayout freindLayout,myLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            freindLayout=(RelativeLayout)itemView.findViewById(R.id.frnd);

            myLayout=(RelativeLayout)itemView.findViewById(R.id.my);
//            myLayout.setVisibility(View.GONE);
            frndProfile =(ImageView)itemView.findViewById(R.id.chat_frnd_profile);
            myProfile=(ImageView)itemView.findViewById(R.id.chat_my_image);
            frndName =(TextView) itemView.findViewById(R.id.chat_frnd_name);
            frndChatMessage =(TextView)itemView.findViewById(R.id.frnd_message);
            frndchatTime=(TextView)itemView.findViewById(R.id.frnd_chat_time);
            myName=(TextView)itemView.findViewById(R.id.chat_my_name);
            myMessage=(TextView)itemView.findViewById(R.id.chat_my_message);
            myChatTime=(TextView)itemView.findViewById(R.id.chat_my_time);

        }
    }
}
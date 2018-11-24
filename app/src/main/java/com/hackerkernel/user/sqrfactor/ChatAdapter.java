package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private ArrayList<ChatFriends>chatFriends;
    private Context context;


    public ChatAdapter(ArrayList<ChatFriends> chatFriends, Context context) {
        this.chatFriends = chatFriends;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_layout, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatFriends chatFriend = chatFriends.get(position);

//        String name=chatFriend.getName();
//        if(name.equals("null"))
//        {
//            holder.frndName.setText(chatFriend.getUserName());
//        }
//        else
//        {
//            holder.frndName.setText(chatFriend.getName());
//        }

        holder.frndName.setText(UtilsClass.getName(chatFriend.getFirst_name(),chatFriend.getLast_name(),chatFriend.getName(),chatFriend.getUserName()));
//
//        Glide.with(context).load("https://archsqr.in/"+chatFriend.getUserProfile())
//                .into(holder.frndProfile);

        String[] parsedUrl=null;
        if(chatFriend.getUserProfile()!=null)
        {
            parsedUrl=chatFriend.getUserProfile().split("/");
        }

        if(parsedUrl!=null && parsedUrl.length>=2 && (parsedUrl[2].equals("graph.facebook.com")||parsedUrl[2].contains("googleusercontent.com")))
        {

            Glide.with(context).load(chatFriend.getUserProfile())
                    .into(holder.frndProfile);

        }
        else {

            Glide.with(context).load("https://archsqr.in/"+chatFriend.getUserProfile())
                    .into(holder.frndProfile);
        }


        holder.chatMessage.setText(chatFriend.getChat());
        String dtc = chatFriend.getCreated_at();
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date past = format.parse(dtc);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                holder.chat_time.setText(seconds+" sec ago");

            }
            else if(minutes<60)
            {
                holder.chat_time.setText(minutes+" min ago");
            }
            else if(hours<24)
            {
                holder.chat_time.setText(hours+" hours ago");
            }
            else
            {
                holder.chat_time.setText(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
//
        if(!chatFriend.getUnread_count().equals("0"))
        {
            Log.v("count",chatFriend.getUnread_count()+"");
            holder.unreadCount.setText(chatFriend.getUnread_count());
            holder.unreadCount.setVisibility(View.VISIBLE);
        }
        else {

            holder.unreadCount.setVisibility(View.GONE);
        }


        if(chatFriend.getIsOnline().equals("True"))
        {
            holder.online.setVisibility(View.VISIBLE);
            holder.offline.setVisibility(View.GONE);
            //Toast.makeText(context,chatFriend.getIsOnline()+"",Toast.LENGTH_LONG).show();
        }
        else
        {
            holder.online.setVisibility(View.GONE);
            holder.offline.setVisibility(View.VISIBLE);
            //Toast.makeText(context,chatFriend.getIsOnline()+"",Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public long getItemId(int position) {
        return position;
    }
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    @Override
    public int getItemCount() {
        return chatFriends.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView frndProfile,online,offline;
        TextView frndName,chatMessage,unreadCount,chat_time;



        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ChatWithAFriendActivity.class);
                    intent.putExtra("FriendId",chatFriends.get(getAdapterPosition()).getUserID());
                    intent.putExtra("FriendName",UtilsClass.getName(chatFriends.get(getAdapterPosition()).getFirst_name(),chatFriends.get(getAdapterPosition()).getLast_name(),chatFriends.get(getAdapterPosition()).getName(),chatFriends.get(getAdapterPosition()).getUserName()));
                    intent.putExtra("FriendProfileUrl",chatFriends.get(getAdapterPosition()).getUserProfile());
                    intent.putExtra("isOnline",chatFriends.get(getAdapterPosition()).getIsOnline());
                    context.startActivity(intent);
                }
            });
            frndProfile =(ImageView)itemView.findViewById(R.id.chat_frnd_profile);
            unreadCount=(TextView)itemView.findViewById(R.id.unreadCount);
            frndName =(TextView) itemView.findViewById(R.id.chat_frnd_name);
            chatMessage =(TextView)itemView.findViewById(R.id.chat_last_message);
            chat_time =(TextView)itemView.findViewById(R.id.chat_time);
            online=(ImageView) itemView.findViewById(R.id.onlineicon);
            offline =(ImageView)itemView.findViewById(R.id.offlineicon);


        }
    }
}
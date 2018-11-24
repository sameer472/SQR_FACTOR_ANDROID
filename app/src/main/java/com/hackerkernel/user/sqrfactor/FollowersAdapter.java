package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.MyViewAdapter> {

    private ArrayList<FollowerClass> followerClassArrayList;
    private Context context;

    public FollowersAdapter(ArrayList<FollowerClass> followerClassArrayList, Context context) {
        this.followerClassArrayList = followerClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follower_adapter_row, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter holder, int position) {

        FollowerClass followerClass=followerClassArrayList.get(position);
        holder.place.setText(followerClass.getCity()+", "+followerClass.getState()+", "+followerClass.getCountry());
        holder.name.setText(UtilsClass.getName(followerClass.getFirstName(),followerClass.getLastName(),followerClass.getName(),followerClass.getUserName()));


        Glide.with(context).load(UtilsClass.getParsedImageUrl(followerClass.getProfile()))
                .into(holder.profileImage);


        holder.post.setText(followerClass.getPostCount());

        holder.moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.moreImage);
                //inflating menu from xml resource
                popup.inflate(R.menu.followers_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.ReportProfile:
                                //handle case for reportprofile here
                                break;
                            case R.id.BlockProfile:
                                //handle case for blockprofile here
                                break;
                            case R.id.TurnOfNotification:
                                //handle case for TurnOfNotification here
                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });
        //todo
        //set profile image using glide libaray fromserver
    }

    @Override
    public int getItemCount() {
        return followerClassArrayList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView name,place,post,portfolio;
        ImageView moreImage;
        ImageView profileImage;

        public MyViewAdapter(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,UserProfileActivity.class);
                    intent.putExtra("User_id",followerClassArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("ProfileUserName",followerClassArrayList.get(getAdapterPosition()).getUserName());
                    context.startActivity(intent);
                }
            });
            name=(TextView)itemView.findViewById(R.id.name);
            place=(TextView)itemView.findViewById(R.id.place);
            post=(TextView)itemView.findViewById(R.id.post);
            portfolio=(TextView)itemView.findViewById(R.id.portfolio);
            profileImage=(ImageView)itemView.findViewById(R.id.follower_image);
            moreImage=(ImageView)itemView.findViewById(R.id.moregrey);

        }
    }
}
package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.SearchResultClass;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    Context context;
    ArrayList<SearchResultClass> searchResultClasses;
    public SearchResultAdapter(Context context, ArrayList<SearchResultClass> searchResultClasses) {
        this.context=context;
        this.searchResultClasses=searchResultClasses;
    }

    @Override
    public SearchResultAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout_adapter, parent, false);
        return new SearchResultAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.MyViewHolder holder, int position) {

        final SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        //"https://graph.facebook.com/v2.10/10155827831771779/picture?type=normal",


        final SearchResultClass searchResultClass=searchResultClasses.get(position);

        Glide.with(context).load(UtilsClass.getParsedImageUrl(searchResultClass.getProfile()))
                    .into(holder.profileImage);
        holder.name.setText(UtilsClass.getName(searchResultClass.getFirst_name(),searchResultClass.getLast_name(),searchResultClass.getName(),searchResultClass.getUserName()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(searchResultClass.getId()==userClass.getUserId())
                {
                    Intent intent=new Intent(context,ProfileActivity.class);
                    context.startActivity(intent);


                }
                else {
                    Intent intent=new Intent(context,UserProfileActivity.class);
                    intent.putExtra("User_id",searchResultClass.getId());
                    intent.putExtra("ProfileUserName",searchResultClass.getUserName());
                    intent.putExtra("UserType", searchResultClass.getUserType());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return searchResultClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            profileImage=(ImageView)itemView.findViewById(R.id.profile);
            name=(TextView)itemView.findViewById(R.id.name);
        }
    }
}
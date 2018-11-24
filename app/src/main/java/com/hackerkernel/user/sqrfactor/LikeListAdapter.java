package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.MyViewAdapter> {
    private ArrayList<LikeClass> likeClassArrayList;
    private Context context;
    boolean flag = false;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    public LikeListAdapter(ArrayList<LikeClass> likeClassArrayList, Context context) {
        this.likeClassArrayList = likeClassArrayList;
        this.context = context;
    }

    @Override
    public LikeListAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_list_adapter, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter holder, int position) {
        final LikeClass likeClass = likeClassArrayList.get(position);
        SharedPreferences mPrefs = context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        if(likeClass.getId()==userClass.getUserId())
            holder.followbtn.setVisibility(View.GONE);
        holder.profileName.setText(UtilsClass.getName(likeClass.getFirst_name(),likeClass.getLast_name(),likeClass.getName(),likeClass.getUser_name()));



        Glide.with(context).load(UtilsClass.getParsedImageUrl(likeClass.getProfile_url()))
                    .into(holder.profileImage);

        holder.followbtn.setText(likeClass.getIsFollowing());
        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"follow_user",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                Log.v("ResponseLike", s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                                    flag = userFollowClass.isReturnType();
                                    if (flag == false) {
//                                        Log.v("follow", flag + "");
//                                        Toast.makeText(context, "Follow", Toast.LENGTH_LONG).show();
                                        holder.followbtn.setText("Follow");
                                        flag = true;
                                    } else {
//                                        Log.v("following", flag + "");
//                                        Toast.makeText(context, "Following", Toast.LENGTH_LONG).show();
                                        holder.followbtn.setText("Following");
                                        flag = false;
                                        database= FirebaseDatabase.getInstance();
                                        ref = database.getReference();
                                        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                                        Gson gson = new Gson();
                                        String json = mPrefs.getString("MyObject", "");
                                        UserClass userClass = gson.fromJson(json, UserClass.class);

                                        PushNotificationClass pushNotificationClass;
                                        from_user fromUser;
                                        //post post1=new post(""," "," "," ",1);
                                        if(userClass.getName()!="null")
                                        {
                                            fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());

                                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" started following you ",new Date().getTime(),fromUser,"follow");
                                        }
                                        else
                                        {
                                            fromUser=new from_user(userClass.getEmail(),userClass.getFirst_name()+" "+userClass.getLast_name(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                                            pushNotificationClass=new PushNotificationClass(userClass.getFirst_name()+" "+userClass.getLast_name()+" started following you ",new Date().getTime(),fromUser,"follow");
                                        }

                                        String key =ref.child("notification").child(likeClass.getId()+"").child("all").push().getKey();
                                        ref.child("notification").child(likeClass.getId()+"").child("all").child(key).setValue(pushNotificationClass);
                                        Map<String,String> unred=new HashMap<>();
                                        unred.put("unread",key);
                                        ref.child("notification").child(likeClass.getId()+"").child("unread").child(key).setValue(unred);


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer " + TokenClass.Token);

                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("to_user",userClass.getUserId() + "");
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue1.add(stringRequest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return likeClassArrayList.size();
    }



    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView profileName;
        Button followbtn;
        ImageView profileImage;


        public MyViewAdapter(View itemView) {
            super(itemView);


            profileName=(TextView)itemView.findViewById(R.id.like_auth_name);
            profileImage=(ImageView)itemView.findViewById(R.id.like_profileImage);
            followbtn = (Button)itemView.findViewById(R.id.like_list_followbtn);



        }

    }



}
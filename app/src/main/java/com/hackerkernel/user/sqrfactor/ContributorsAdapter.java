package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.MyViewAdapter> {
    private ArrayList<ContributorsClass> contributorsClassArrayList = new ArrayList<>();
    private Context context;
    private int isFollow=0;
    private boolean flag = false;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<UserFollowClass> userFollowClassArrayList;

    public ContributorsAdapter(ArrayList<ContributorsClass> contributorsClassArrayList, Context context) {
        this.contributorsClassArrayList = contributorsClassArrayList ;
        this.context = context;
    }

    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributors_adapter, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter holder, int position) {

        final ContributorsClass contributorsClass=contributorsClassArrayList.get(position);


        holder.name.setText(UtilsClass.getName(contributorsClass.getFirst_name(),contributorsClass.getLast_name(),contributorsClass.getName(),contributorsClass.getUser_name()));
        if(!contributorsClass.getShort_bio().equals("null"))
        holder.place.setText(contributorsClass.getShort_bio());
        if(!contributorsClass.getNumberOf_post().equals("null"))
        holder.post.setText(contributorsClass.getNumberOf_post());
        if(!contributorsClass.getViews().equals("null"))
        holder.view.setText(contributorsClass.getViews());




        String[] parsedUrl=null;
        if(contributorsClass.getProfileImage()!=null)
        {
            parsedUrl=contributorsClass.getProfileImage().split("/");
        }

        if(parsedUrl!=null && parsedUrl.length>=2 && (parsedUrl[2].equals("graph.facebook.com")||parsedUrl[2].contains("googleusercontent.com")))
        {

            Glide.with(context).load(contributorsClass.getProfileImage())
                    .into(holder.prfileImage);

        }
        else {

            Glide.with(context).load(UtilsClass.baseurl1+contributorsClass.getProfileImage())
                    .into(holder.prfileImage);
        }

        holder.prfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("User_id", contributorsClass.getId());
                intent.putExtra("ProfileUserName", contributorsClass.getUser_name());
                intent.putExtra("ProfileUrl", contributorsClass.getProfileImage());
                context.startActivity(intent);
            }
        });

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

                                        holder.followbtn.setText("Follow");
                                        flag = true;
                                    } else {
//
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

                                        String key =ref.child("notification").child(contributorsClass.getId()+"").child("all").push().getKey();
                                        ref.child("notification").child(contributorsClass.getId()+"").child("all").child(key).setValue(pushNotificationClass);
                                        Map<String,String> unred=new HashMap<>();
                                        unred.put("unread",key);
                                        ref.child("notification").child(contributorsClass.getId()+"").child("unread").child(key).setValue(unred);


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

                        params.put("to_user",contributorsClass.getId() + "");
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue1.add(stringRequest);
            }
        });

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
        return contributorsClassArrayList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView name,place,post,view;
        ImageView moreImage;
        ImageView prfileImage;
        Button followbtn;

        public MyViewAdapter(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.contributors_name);
            place=(TextView)itemView.findViewById(R.id.contributors_place);
            post=(TextView)itemView.findViewById(R.id.contributors_postnumber);
            view=(TextView)itemView.findViewById(R.id.contributors_viewnumber);
            prfileImage=(ImageView)itemView.findViewById(R.id.contributors_image);
            moreImage=(ImageView)itemView.findViewById(R.id.contributors_menu);
            followbtn=(Button)itemView.findViewById(R.id.contributors_followbtn);

        }
    }
}

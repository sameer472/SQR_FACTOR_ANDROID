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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class CommentsLimitedAdapter extends RecyclerView.Adapter<CommentsLimitedAdapter.MyViewHolder> {

    private ArrayList<comments_list> comments_limitedArrayList=new ArrayList<>();
    private Context context;
    private int flag=0,user_id,commentable_id,postId;
    private String isShared;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    public CommentsLimitedAdapter(ArrayList<comments_list> comments_limitedArrayList, Context context,int postId) {

        this.comments_limitedArrayList = comments_limitedArrayList;
        this.context = context;
        this.postId=postId;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_limited_adapter,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final comments_list commentsList=comments_limitedArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        if(userClass.getUserId()==user_id)
        {
            holder.comment_menu.setVisibility(View.VISIBLE);
        }


        holder.commentBody.setText(comments_limitedArrayList.get(position).getBody());


//        Glide.with(context).
//                load("https://archsqr.in/"+commentsList.getFrom_user_profile())
//                .into(holder.commenterProfile);

        String[] parsedUrl=commentsList.getFrom_user_profile().split("/");

        if(parsedUrl.length>=2 && (parsedUrl[2].equals("graph.facebook.com")||parsedUrl[2].contains("googleusercontent.com")))
        {
            Glide.with(context).load(commentsList.getFrom_user_profile())
                    .into(holder.commenterProfile);
        }
        else {
            Glide.with(context).load(UtilsClass.baseurl1+commentsList.getFrom_user_profile())
                    .into(holder.commenterProfile);
        }


        holder.commenterUserName.setText(UtilsClass.getName(commentsList.getFrom_user_first_name(),commentsList.getFrom_user_last_name(),commentsList.getFrom_user_name(),commentsList.getFrom_user_user_name()));
        holder.commenterUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                //Log.v("Data",newsFeedStatus.getUserId()+" "+userName+" "+newsFeedStatus.getPostId());
                intent.putExtra("User_id",commentsList.getFrom_user_id());
                intent.putExtra("ProfileUserName",commentsList.getFrom_user_user_name());
                context.startActivity(intent);
            }
        });
        holder.numberOfLikes.setText(commentsList.getCommentLikeCount()+" Likes");
        String dtc = commentsList.getComment_date();
        holder.timeAgo.setText(UtilsClass.getTime(dtc));
        int isAlreadyLiked = 0;

        for (int i = 0; i < commentsList.getCommentLikeClassArrayList().size(); i++) {
            if (userClass.getUserId() == commentsList.getCommentLikeClassArrayList().get(i).getUser_id()) {
                holder.numberOfLikes.setTextColor(context.getResources().getColor(R.color.sqr));
                isAlreadyLiked = 1;
                holder.commentLike.setChecked(true);
                ///flag=1;
            }
        }
        final int isAlreadyLikedFinal = isAlreadyLiked;

        holder.numberOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikeListActivity.class);
                intent.putExtra("id", commentsList.getId());
                intent.putExtra("isComment","true");
                context.startActivity(intent);
            }
        });

        holder.commentLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                  //  Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    int likeCount = commentsList.getCommentLikeCount();
//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                    holder.numberOfLikes.setTextColor(context.getResources().getColor(R.color.sqr));
                    if (isAlreadyLikedFinal == 1)
                        holder.numberOfLikes.setText(likeCount + " Like");
                    else {
                        likeCount = likeCount + 1;
                        holder.numberOfLikes.setText(likeCount + " Like");
                    }

                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    SharedPreferences mPrefs = context.getSharedPreferences("User", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("MyObject", "");
                    UserClass userClass = gson.fromJson(json, UserClass.class);

                    Log.v("daattataatat", userClass.getUserId() + " " + userClass.getProfile() + " ");
                    if ( userClass.getUserId() != commentsList.getFrom_user_id()) {
                        PushNotificationClass pushNotificationClass;
                        from_user fromUser;
                        post post1 = new post("","", commentsList.getBody(), "App\\UsersPostShare", commentsList.getId());
                        if (userClass.getName() != "null") {
                            fromUser = new from_user(userClass.getEmail(), userClass.getName(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                            pushNotificationClass = new PushNotificationClass(" liked your comment ", new Date().getTime(), fromUser, post1, "comment_like");
                        } else {
                            fromUser = new from_user(userClass.getEmail(), userClass.getFirst_name() + " " + userClass.getLast_name(), userClass.getUserId(), userClass.getUser_name(), userClass.getProfile());
                            pushNotificationClass = new PushNotificationClass(userClass.getFirst_name() + " " + userClass.getLast_name() + " liked your comment ", new Date().getTime(), fromUser, post1, "comment_like");
                        }

                        String key = ref.child("notification").child(commentsList.getFrom_user_id() + "").child("all").push().getKey();
                        ref.child("notification").child(commentsList.getFrom_user_id() + "").child("all").child(key).setValue(pushNotificationClass);
                        Map<String, String> unred = new HashMap<>();
                        unred.put("unread", key);
                        ref.child("notification").child(commentsList.getFrom_user_id() + "").child("unread").child(key).setValue(unred);
                    }
                } else {

                    if (isAlreadyLikedFinal == 1) {
                       // Log.v("isAlreadyLiked1", isAlreadyLikedFinal + " ");
                        holder.numberOfLikes.setTextColor(context.getResources().getColor(R.color.gray));
                        int likeCount1 = commentsList.getCommentLikeCount();
                       // Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1 = likeCount1 - 1;
                        holder.numberOfLikes.setText(likeCount1 + " Like");
                    } else {
                       // Log.v("isAlreadyLiked2", isAlreadyLikedFinal + " ");
                       // Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                        holder.numberOfLikes.setTextColor(context.getResources().getColor(R.color.gray));
                        holder.numberOfLikes.setText( commentsList.getCommentLikeCount() + " Like");
                    }


                }
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"like_post",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

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

                        params.put("likeable_id", commentsList.getId()+"");
                        params.put("likeable_type", "comments");
//
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });

        if(userClass.getUserId()==commentsList.getFrom_user_id())
        {
            holder.comment_menu.setVisibility(View.VISIBLE);
        }

        holder.comment_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.comment_delete, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.deleteComment:
                                comments_limitedArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, 1);
                                DeletePost(postId,commentsList.getId());
                                break;


                        }
                        return true;
                    }
                });
            }
        });





    }
    public void DeletePost(final int id, final int comment_id)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"delete_comment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                       // Toast.makeText(context, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Log.v("deletecomment",id+" "+comment_id);
                params.put("id",id+"");
                params.put("comment_id",comment_id+"");

                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {

        return comments_limitedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commentBody,commenterUserName,timeAgo,numberOfLikes;
        ImageView commenterProfile,comment_menu;
        CheckBox commentLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentBody=(TextView)itemView.findViewById(R.id.commentBody);
            commenterUserName=(TextView)itemView.findViewById(R.id.commenterUserName);
            timeAgo=(TextView)itemView.findViewById(R.id.timeAgo);
            comment_menu=(ImageView)itemView.findViewById(R.id.comment_menu);
            commenterProfile=(ImageView)itemView.findViewById(R.id.commenterProfileImage);
            commentLike=(CheckBox) itemView.findViewById(R.id.commentLike);
            numberOfLikes=(TextView)itemView.findViewById(R.id.numberOfLike);

        }
    }
}
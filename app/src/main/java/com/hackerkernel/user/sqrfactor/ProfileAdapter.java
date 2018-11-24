package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private ArrayList<ProfileClass1> profileClassArrayList;
    private Context context;
    private int flag = 0;
    private String userName;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private int flag1 = 0;
    private int commentsCount=0,likeCount=0;


    public ProfileAdapter(ArrayList<ProfileClass1> profileClasses, Context context) {
        this.profileClassArrayList = profileClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProfileClass1 profileClass = profileClassArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        int isAlreadyLiked=0;
        int isAlreadyCommented=0;
        final int[] commentId = new int[1];
        if(profileClass.getType().equals("status"))
        {

            holder.postTitle.setVisibility(View.GONE);
            holder.postTag.setVisibility(View.GONE);
            userName=profileClass.getUser_name_of_post();
            holder.postShortDescription.setText(profileClass.getFullDescription());
            Glide.with(context).load(UtilsClass.baseurl1+profileClass.getUserImageUrl())
                    .into(holder.postBannerImage);

        }

        else if(profileClass.getType().equals("design"))
        {

            holder.postTitle.setVisibility(View.VISIBLE);
            holder.postTag.setVisibility(View.VISIBLE);

            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load(UtilsClass.baseurl1+profileClass.getPostImage())
                    .into(holder.postBannerImage);

        }

        else if(profileClass.getType().equals("article"))
        {

            holder.postTitle.setVisibility(View.VISIBLE);
            holder.postTag.setVisibility(View.VISIBLE);
            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load(UtilsClass.baseurl1+profileClass.getPostImage())
                    .into(holder.postBannerImage);

        }

        Glide.with(context).load(UtilsClass.getParsedImageUrl(userClass.getProfile()))
                .into( holder.usercommentProfile);

        Glide.with(context).load(UtilsClass.getParsedImageUrl(profileClass.getAuthImageUrl()))
                .into(holder.userProfile);

        holder.userName.setText(UtilsClass.getName(profileClass.getFirst_name(),profileClass.getLast_name(),profileClass.getName(),profileClass.getUser_name_of_post()));

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
               // Log.v("Data",profileClass.getUserId()+" "+userName+" "+profileClass.getPostId());
                intent.putExtra("User_id",profileClass.getUserId());
                intent.putExtra("ProfileUserName",userName);
                context.startActivity(intent);

            }
        });
        holder.postBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(context,FullPostActivity.class);
                    intent.putExtra("Post_Slug_ID",profileClass.getSlug());
                    context.startActivity(intent);



            }
        });


        for(int i=0;i<profileClass.getAllLikesId().size();i++)
        {
            if(userClass.getUserId()==profileClass.AllLikesId.get(i))
            {
                holder.buttonLikeList.setTextColor(context.getResources().getColor(R.color.sqr));
                isAlreadyLiked=1;
                holder.buttonLike.setChecked(true);
                ///flag=1;
            }
        }
        final int isAlreadyLikedFinal=isAlreadyLiked;

        for(int i=0;i<profileClass.AllCommentId.size();i++)
        {
            if(userClass.getUserId()==profileClass.AllCommentId.get(i))
            {
                isAlreadyCommented=1;
                 holder.buttonComment.setTextColor(context.getResources().getColor(R.color.sqr));
//                holder.commentCheckBox.setChecked(true);
                //holder.co.setChecked(true);
            }
        }
        final int isAlreadyCommentedFinal=isAlreadyCommented;

        holder.postTime.setText(UtilsClass.getTime(profileClass.getTime()));

        //holder.fullDescription.setText(profileClass.);

        holder.buttonLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,LikeListActivity.class);
                intent.putExtra("id",profileClass.getPostId());
                context.startActivity(intent);
            }

        });

        if(profileClass.getComments()!=null)
            commentsCount=Integer.parseInt(profileClass.getComments());

        holder.buttonComment.setText(commentsCount+" Comment");

        if(profileClass.getLike()!=null)
            likeCount=Integer.parseInt(profileClass.getLike());

            holder.buttonLikeList.setText(likeCount+" Like");

        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(flag1 == 0) {
                    holder.buttonComment.setTextColor(ContextCompat.getColor(context,R.color.sqr));
                    flag1 = 1;

                }
                else {
                    holder.buttonComment.setTextColor(ContextCompat.getColor(context,R.color.gray));
                    flag1 = 0;
                }


                Intent intent = new Intent(context, CommentsPage.class);
                intent.putExtra("PostSharedId",profileClass.getSharedId()); //second param is Serializable
                context.startActivity(intent);

            }
        });
        if(userClass.getUserId()==profileClass.getUserId())
        {
            holder.user_post_menu.setVisibility(View.VISIBLE);
        }

        holder.buttonLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    int likeCount=0;
                    if(profileClass.getLike()!=null)
                    {
                        likeCount=Integer.parseInt(profileClass.getLike());
                    }

//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                    holder.buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                    if(isAlreadyLikedFinal==1)
                        holder.buttonLikeList.setText(likeCount+" Like");
                    else
                    {
                        likeCount=likeCount+1;
                        holder.buttonLikeList.setText(likeCount+" Like");
                    }
                    database= FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("MyObject", "");
                    UserClass userClass = gson.fromJson(json, UserClass.class);

                    String name=UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name());
                    if(profileClass.getType().equals("status")&& userClass.getUserId()!=profileClass.getUserId())
                    {
                        PushNotificationClass pushNotificationClass;
                        from_user fromUser;
                        post post1=new post(profileClass.getFullDescription(),profileClass.getSlug(),"post Title",profileClass.getType(),profileClass.getPostId());
                        fromUser=new from_user(userClass.getEmail(),name,userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                        pushNotificationClass=new PushNotificationClass(" liked your status ",new Date().getTime(),fromUser,post1,"like_post");

                        String key =ref.child("notification").child(profileClass.getUserId()+"").child("all").push().getKey();
                        ref.child("notification").child(profileClass.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);
                        Map<String,String> unred=new HashMap<>();
                        unred.put("unread",key);
                        ref.child("notification").child(profileClass.getUserId()+"").child("unread").child(key).setValue(unred);
                    }
                    else if(userClass.getUserId()!=profileClass.getUserId())
                    {
                        from_user fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                        post post1=new post(profileClass.getShortDescription(),profileClass.getSlug(),profileClass.getPostTitle(),profileClass.getType(),profileClass.getPostId());
                        PushNotificationClass pushNotificationClass=new PushNotificationClass(" liked your post ",new Date().getTime(),fromUser,post1,"like_post");;
                        String key =ref.child("notification").child(profileClass.getUserId()+"").child("all").push().getKey();
                        ref.child("notification").child(profileClass.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);
                        Map<String,String> unred=new HashMap<>();
                        unred.put("unread",key);
                        ref.child("notification").child(profileClass.getUserId()+"").child("unread").child(key).setValue(unred);

                    }
                }
                else {

                    if(isAlreadyLikedFinal==1)
                    {
                        Log.v("isAlreadyLiked1",isAlreadyLikedFinal+" ");
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        int likeCount1=Integer.parseInt(profileClass.getLike());
                        Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1=likeCount1-1;
                        holder.buttonLikeList.setText(likeCount1+" Like");
                    }
                    else
                    {
                        Log.v("isAlreadyLiked2",isAlreadyLikedFinal+" ");
                        Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        holder.buttonLikeList.setText(profileClass.getLike()+" Like");
                    }


                }
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"like_post",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);


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

                        params.put("likeable_id",profileClass.getSharedId()+"");
                        params.put("likeable_type","users_post_share");
//
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });
        holder.user_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.delete_news_post_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.fullView:
                                Intent intent=new Intent(context,FullPostActivity.class);
                                intent.putExtra("Post_Slug_ID",profileClass.getSlug());
                                context.startActivity(intent);
                                break;
                            case R.id.editPost:
                                if (profileClass.getType().equals("design")) {
                                    Intent intent1 = new Intent(context, DesignActivity.class);
                                    intent1.putExtra("Post_Slug_ID", profileClass.getSlug());
                                    intent1.putExtra("Post_ID", profileClass.getPostId());
                                    context.startActivity(intent1);
                                } else if (profileClass.getType().equals("article")) {
                                    Intent intent1 = new Intent(context, ArticleActivity.class);
                                    intent1.putExtra("Post_Slug_ID", profileClass.getSlug());
                                    intent1.putExtra("Post_ID", profileClass.getPostId());
                                    context.startActivity(intent1);
                                } else if (profileClass.getType().equals("status")) {
                                    Intent intent1 = new Intent(context, StatusPostActivity.class);
                                    intent1.putExtra("Post_Slug_ID", profileClass.getSlug());
                                    intent1.putExtra("Post_ID", profileClass.getPostId());
                                    context.startActivity(intent1);
                                }
                                break;
                            case R.id.deletePost:
                                profileClassArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, 1);
                                //DeletePost(newsFeedStatus.getPostId()+"",newsFeedStatus.getSharedId()+"",newsFeedStatus.getIs_Shared());
                                break;
                            case R.id.selectAsFeaturedPost:
                                return true;

                        }
                        return true;
                    }
                });
            }
        });

        holder.user_comment_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.comment_delete, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.deleteComment:
                                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"delete_comment",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {

                                                MDToast.makeText(context, "Your comment deleted successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                                holder.commentCardView.setVisibility(View.GONE);
                                                if(profileClass.getComments()!=null)
                                                    commentsCount=Integer.parseInt(profileClass.getComments());
                                                holder.buttonComment.setText(commentsCount+" Comment");
                                                if(isAlreadyCommentedFinal==0)
                                                {
                                                    holder.buttonComment.setTextColor(context.getResources().getColor(R.color.gray));
                                                }


                                               // holder.buttonComment.setText(result + " Comment");
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
                                        params.put("comment_id",commentId[0]+"");
                                        params.put("id",profileClass.getPostId()+"");
                                        return params;
                                    }
                                };

                                requestQueue.add(stringRequest);
                                break;


                        }
                        return true;
                    }
                });
            }
        });
        holder.commentpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);
                                Log.v("ResponseLike",s);
                                try {
                                    JSONObject jsonObject=new JSONObject(s);
                                    commentId[0] = jsonObject.getJSONObject("comment").getInt("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                holder.user_comment_time.setText("0 minutes ago");
                                holder.user_post_likeListcomment.setText("0 Likes");
                                holder.user_comment.setText(holder.user_write_comment.getText().toString());
                                Glide.with(context).load("https://archsqr.in/"+userClass.getProfile())
                                        .into(holder.user_comment_image);
                                holder.commentCardView.setVisibility(View.VISIBLE);
                                holder.user_write_comment.setText("");
                                database= FirebaseDatabase.getInstance();
                                ref = database.getReference();
                                SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = mPrefs.getString("MyObject", "");
                                UserClass userClass = gson.fromJson(json, UserClass.class);
                                String name=UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name());
                                holder.user_comment_name.setText(name);
                                if(userClass.getUserId()!=profileClass.getUserId())
                                {
                                    post post1=new post(profileClass.getFullDescription(),profileClass.getSlug(),profileClass.getPostTitle(),profileClass.getType(),profileClass.getPostId());
                                    from_user fromUser=new from_user(userClass.getEmail(),name,userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                                    PushNotificationClass pushNotificationClass=new PushNotificationClass(" commented on your post ",new Date().getTime(),fromUser,post1,"comment");

                                    String key =ref.child("notification").child(profileClass.getUserId()+"").child("all").push().getKey();
                                    ref.child("notification").child(profileClass.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);
                                    Map<String,String> unred=new HashMap<>();
                                    unred.put("unread",key);
                                    ref.child("notification").child(profileClass.getUserId()+"").child("unread").child(key).setValue(unred);
                                }


                               if(profileClass.getComments()!=null)
                                commentsCount=Integer.parseInt(profileClass.getComments());
                                commentsCount=commentsCount+1;
                                holder.buttonComment.setText(commentsCount+" Comment");
                                holder.buttonComment.setTextColor(ContextCompat.getColor(context,R.color.sqr));

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

                        params.put("commentable_id",profileClass.getSharedId()+"");
//
                        params.put("comment_text",holder.user_write_comment.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, postTime,postTag,postShortDescription, postDescription,postTitle,buttonLikeList,commentpost,user_comment_name,user_comment_time,user_comment;
        EditText userComment,user_write_comment;
        ImageView usercommentProfile, userProfile, postBannerImage,user_comment_image,user_post_menu,user_comment_menu;
        Button  buttonComment,buttonShare,user_post_likeListcomment;
        CheckBox buttonLike;
        CardView commentCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            postTitle=(TextView)itemView.findViewById(R.id.user_post_title);
            postTag = (TextView)itemView.findViewById(R.id.user_post_tag);
            postBannerImage = (ImageView) itemView.findViewById(R.id.user_post_image);
            userName = (TextView) itemView.findViewById(R.id.userprofle_name);
            postTime = (TextView) itemView.findViewById(R.id.user_post_time);
            postShortDescription = (TextView) itemView.findViewById(R.id.user_post_short_descriptions);
            usercommentProfile = (ImageView) itemView.findViewById(R.id.user_profileImage);
            userProfile = (ImageView) itemView.findViewById(R.id.userprofile_image);
            buttonLike = (CheckBox) itemView.findViewById(R.id.user_post_like);
            buttonLikeList = (TextView) itemView.findViewById(R.id.user_post_likeList);
            buttonComment = (Button) itemView.findViewById(R.id.user_post_comment);
            buttonShare = (Button) itemView.findViewById(R.id.user_post_share);
            buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SqrFactor");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
            commentCardView=(CardView)itemView.findViewById(R.id.userProfile_commentCardView);
            user_post_likeListcomment=(Button)itemView.findViewById(R.id.user_post_likeListcomment);
            user_write_comment=(EditText)itemView.findViewById(R.id.user_write_comment);
            user_comment_image=(ImageView)itemView.findViewById(R.id.user_comment_image);

            user_post_menu=(ImageView)itemView.findViewById(R.id.user_post_menu);
            user_comment_name=(TextView)itemView.findViewById(R.id.user_comment_name);
            user_comment_menu=(ImageView)itemView.findViewById(R.id.user_comment_menu);

            user_comment_time=(TextView)itemView.findViewById(R.id.user_comment_time);
            user_comment=(TextView)itemView.findViewById(R.id.user_comment);
            userComment = (EditText) itemView.findViewById(R.id.user_write_comment);
            commentpost = (TextView) itemView.findViewById(R.id.user_post_button);

        }
    }
}
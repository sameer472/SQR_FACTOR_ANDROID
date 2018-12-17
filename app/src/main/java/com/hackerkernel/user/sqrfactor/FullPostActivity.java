package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.irshulx.Editor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.FullPost;
import com.hackerkernel.user.sqrfactor.Pojo.NewsFeedStatus;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.from_user;
import com.hackerkernel.user.sqrfactor.Pojo.post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FullPostActivity extends AppCompatActivity {
    private ArrayList<NewsFeedStatus> newsFeedStatuses;
    private TextView postTitle, postDescription, postTag, red_authName, red_postTime,red_postViews,red_likeList,likeList,post;
    private Button red_comment, red_share;
    private ImageView red_authImage,userImage,full_post_menu,commentProfileImageUrl;
    private Editor editor;
    private WebView webView;
    private CheckBox like;
    private LinearLayout web,tagLayout1,tagLayout2,tagLayout3;
    private EditText written_comment_body;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String slug;
    private int flag = 0,sharedId;
    UserClass userClass;
    private CardView news_comment_card;
    private TextView commentTime,commentDescription,commentUserName,fullPostDescription;
    private int flag1 = 0,isAlreadyLiked=0;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        progressBar=findViewById(R.id.progress_bar_full_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fullPostDescription=(TextView)findViewById(R.id.fullPostDescription);
        tagLayout1=(LinearLayout)findViewById(R.id.tagLayout1);
        tagLayout2=(LinearLayout)findViewById(R.id.tagLayout2);
        tagLayout3=(LinearLayout)findViewById(R.id.tagLayout3);
        web=(LinearLayout)findViewById(R.id.webView);
        postTitle = findViewById(R.id.full_postTitle);
        postDescription = findViewById(R.id.full_postDescription);
        //postTag = findViewById(R.id.full_postTag);
        red_authName = findViewById(R.id.full_post_authName);
        red_postTime = findViewById(R.id.full_postTime);
        red_postViews = findViewById(R.id.full_postViews);
        like = findViewById(R.id.full_like);
        likeList = findViewById(R.id.full_likeList);
        red_comment = findViewById(R.id.full_comment);
        red_share = findViewById(R.id.full_share);
        red_authImage = findViewById(R.id.full_authImage);
        userImage = findViewById(R.id.full_post_userProfile);
        red_share = findViewById(R.id.full_share);
        red_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SqrFactor");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        post = findViewById(R.id.full_post_commentPostbtn);
        full_post_menu = findViewById(R.id.full_post_menu);
        written_comment_body=findViewById(R.id.full_post_userComment);

        commentTime=findViewById(R.id.news_comment_time_fullpost);
        commentDescription=findViewById(R.id.news_comment_descrip_fullpost);
        commentProfileImageUrl=findViewById(R.id.news_comment_image_fullpost);
        commentUserName=findViewById(R.id.news_comment_name_fullpost);
        news_comment_card=findViewById(R.id.news_comment_card_fullPost);



        if(getIntent()!=null && getIntent().getStringExtra("Post_Slug_ID")!=null)
        {
            slug = getIntent().getStringExtra("Post_Slug_ID");
            LoadFullPostDataFromServer(slug);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(FullPostActivity.this, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void LoadFullPostDataFromServer(String slug)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(FullPostActivity.this);
        // "https://archsqr.in/api/profile/detail/

        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"post-detail/"+slug,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.v("data@123",response);
                        //Toast.makeText(getApplicationContext(),"res"+response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("post");
                            final FullPost fullPost = new FullPost(jsonObjectFullPost);

                            LoadTagsFromServer(fullPost.getId());
                            sharedId=fullPost.getShared_id();
                            if (fullPost.getType().equals("status")) {
                              //postTag.setVisibility(View.GONE);
                              postTitle.setVisibility(View.GONE);
                            } else if (fullPost.getType().equals("design")) {
                              // postTag.setVisibility(View.VISIBLE);
                               postTitle.setVisibility(View.VISIBLE);
                            } else if (fullPost.getType().equals("article")) {
                                //postTag.setVisibility(View.VISIBLE);
                                postTitle.setVisibility(View.VISIBLE);
                            }
                            if(!fullPost.getTitle().equals("null"))
                            {
                                postTitle.setText(fullPost.getTitle());
                            }
                            if(!fullPost.getShort_description().equals("null"))
                            {
                                postDescription.setText(fullPost.getShort_description());
                            }

                            //postTag.setText(fullPost.getType());
                            red_postViews.setText(fullPost.getViews_count()+"");
                            red_authName.setText(UtilsClass.getName(fullPost.getFirst_name(),fullPost.getLast_name(),fullPost.getAuthName(),fullPost.getUser_name_of_post()));
                            red_postTime.setText(UtilsClass.getTime(fullPost.getCreated_at()));
                            Glide.with(getApplicationContext()).load(UtilsClass.getParsedImageUrl(fullPost.getAuthImageUrl()))
                                    .into(red_authImage);


                            if(fullPost.getType().equals("status"))
                            {

                                postDescription.setText(fullPost.getDescription());
                                final ImageView imageView = new ImageView(getApplicationContext());
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                imageView.setAdjustViewBounds(true);
                                imageView.requestLayout();
                                Glide.with(getApplicationContext()).load(UtilsClass.baseurl1+fullPost.getImage())
                                        .into(imageView);
                                web.addView(imageView);
                            }
                            else {

                                final String finalHtml="   <html>\n" +
                                        "  <head>\n" +
                                        "    <title>Combined</title>\n" +
                                        "  </head>\n" +
                                        "  <body>\n" +
                                        "    <div id=\"page1\">\n" +
                                        fullPost.getDescription() +
                                        "    </div>\n" +
                                        "  </body>\n" +
                                        "</html>";
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try { Thread.sleep(200); }
                                        catch (InterruptedException e) {}

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                PostContentHandler postContentHandler=new PostContentHandler(getApplicationContext(),finalHtml,web,fullPostDescription);
                                                postContentHandler.setContentToView();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }


                            likeList.setText(fullPost.getLike()+" Likes");
                            red_comment.setText(fullPost.getComments_count()+" comments");
                            for(int i=0;i<fullPost.getAllLikesId().size();i++)
                            {
                                if(userClass.getUserId()==fullPost.AllLikesId.get(i))
                                {
                                    likeList.setTextColor(getResources().getColor(R.color.red));
                                    isAlreadyLiked=1;
                                    like.setChecked(true);
                                }
                            }

                            for(int i=0;i<fullPost.getAllCommentId().size();i++)
                            {
                                if(userClass.getUserId()==fullPost.AllCommentId.get(i))
                                {
                                    red_comment.setTextColor(getResources().getColor(R.color.sqr));
                                }
                            }


                            LikeAndCommentFunction(fullPost);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
                String token = sharedPreferences.getString("TOKEN", "sqr");
                TokenClass.Token=token;
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }

        };
        requestQueue.add(myReq);
    }

    private void LoadTagsFromServer(final int id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"display_posttags",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray tags=jsonObject.getJSONArray("tags");

                            for(int i=0;i<tags.length();i++)
                            {
                                JSONObject tagObject=tags.getJSONObject(i);
                                TextView textView = new TextView(FullPostActivity.this);
                                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(5,5,5,5);
                                textView.setLayoutParams(layoutParams);
                                textView.setTextSize(14);
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                textView.setTextColor(Color.WHITE);
                                textView.setText(tagObject.getString("name"));
                                textView.setPadding(5, 5, 5, 5);
                                if(i<2)
                                {
                                    tagLayout1.addView(textView);
                                }
                                else if(1<i && i<4)
                                {
                                    tagLayout2.addView(textView);
                                }
                                else {
                                    tagLayout3.addView(textView);
                                }

                            }




//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_id",id+"");
                return params;
            }


        };

        requestQueue.add(myReq);
    }


    private void LikeAndCommentFunction(final FullPost fullPost) {
        SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        Glide.with(FullPostActivity.this).load(UtilsClass.baseurl1+userClass.getProfile())
                .into(userImage);
        likeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LikeListActivity.class);
                intent.putExtra("id",fullPost.getId());
                startActivity(intent);
            }
        });
        red_comment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsPage.class);
                intent.putExtra("PostSharedId",fullPost.getShared_id()); //second param is Serializable
                startActivity(intent);


            }
        });

        like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    int likeCount=Integer.parseInt(fullPost.getLike());
                    likeList.setTextColor(getResources().getColor(R.color.sqr));
                    if(isAlreadyLiked==1)
                        likeList.setText(likeCount+" Like");
                    else
                    {
                        likeCount=likeCount+1;
                        likeList.setText(likeCount+" Like");
                    }

                    database= FirebaseDatabase.getInstance();
                    ref = database.getReference();
//

                    if(userClass.getUserId()!=fullPost.getUser_id())
                    {
                        from_user fromUser;
                        com.hackerkernel.user.sqrfactor.Pojo.post post1=new post(fullPost.getShort_description(),fullPost.getSlug(),fullPost.getTitle(),fullPost.getType(),fullPost.getId());
                        PushNotificationClass pushNotificationClass;
                        if(userClass.getName()!="null")
                        {
                            fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                        }
                        else
                        {
                            fromUser=new from_user(userClass.getEmail(),userClass.getFirst_name()+" "+userClass.getLast_name(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                            pushNotificationClass=new PushNotificationClass(userClass.getFirst_name()+" "+userClass.getLast_name()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                        }

                        String key =ref.child("notification").child(fullPost.getUser_id()+"").child("all").push().getKey();
                        ref.child("notification").child(fullPost.getUser_id()+"").child("all").child(key).setValue(pushNotificationClass);
                        Map<String,String> unred=new HashMap<>();
                        unred.put("unread",key);
                        ref.child("notification").child(fullPost.getUser_id()+"").child("unread").child(key).setValue(unred);
                    }


                }
                else {

                    if(isAlreadyLiked==1)
                    {
                        Log.v("isAlreadyLiked1",isAlreadyLiked+" ");
                        likeList.setTextColor(getResources().getColor(R.color.gray));
                        int likeCount1=Integer.parseInt(fullPost.getLike());
                        //Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1=likeCount1-1;
                        likeList.setText(likeCount1+" Like");
                    }
                    else
                    {
                        Log.v("isAlreadyLiked2",isAlreadyLiked+" ");
                        //Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                        likeList.setTextColor(getResources().getColor(R.color.gray));
                        likeList.setText(fullPost.getLike()+" Like");
                    }


                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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

                        params.put("likeable_id",fullPost.getShared_id()+"");
                        params.put("likeable_type","users_post_share");
//
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);

                                commentTime.setText("0 minutes ago");
                                commentDescription.setText( written_comment_body.getText().toString());
                                Glide.with(getApplicationContext()).load(UtilsClass.baseurl1+userClass.getProfile())
                                        .into(commentProfileImageUrl);
                                commentUserName.setText(userClass.getUser_name());
                                news_comment_card.setVisibility(View.VISIBLE);
                                database= FirebaseDatabase.getInstance();
                                ref = database.getReference();
                                from_user fromUser;
                                post post1=new post(fullPost.getShort_description(),fullPost.getSlug(),fullPost.getTitle(),fullPost.getType(),fullPost.getId());
                                PushNotificationClass pushNotificationClass;
                                if(userClass.getName()!="null")
                                {
                                    fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                                    pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                                }
                                else
                                {
                                    fromUser=new from_user(userClass.getEmail(),userClass.getFirst_name()+" "+userClass.getLast_name(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                                    pushNotificationClass=new PushNotificationClass(userClass.getFirst_name()+" "+userClass.getLast_name()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                                }

                                String key =ref.child("notification").child(fullPost.getUser_id()+"").child("all").push().getKey();
                                ref.child("notification").child(fullPost.getUser_id()+"").child("all").child(key).setValue(pushNotificationClass);
                                Map<String,String> unred=new HashMap<>();
                                unred.put("unread",key);
                                ref.child("notification").child(fullPost.getUser_id()+"").child("unread").child(key).setValue(unred);
                                written_comment_body.setText("");

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

                        params.put("commentable_id",fullPost.getShared_id()+"");
                        params.put("comment_text",written_comment_body.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });


        if(userClass.getUserId()==fullPost.getUser_id())
        {
            full_post_menu.setVisibility(View.VISIBLE);
        }
        full_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.delete_fullpost_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.editPost:
                                if (fullPost.getType().equals("design")) {
                                    Intent intent1 = new Intent(getApplicationContext(), DesignActivity.class);
                                    intent1.putExtra("Post_Slug_ID", fullPost.getSlug());
                                    intent1.putExtra("Post_ID", fullPost.getId());
                                    startActivity(intent1);
                                } else if (fullPost.getType().equals("article")) {
                                    Intent intent1 = new Intent(getApplicationContext(), ArticleActivity.class);
                                    intent1.putExtra("Post_Slug_ID", fullPost.getSlug());
                                    intent1.putExtra("Post_ID", fullPost.getId());
                                    startActivity(intent1);
                                } else if (fullPost.getType().equals("status")) {
                                    Intent intent1 = new Intent(getApplicationContext(), StatusPostActivity.class);
                                    intent1.putExtra("Post_Slug_ID", fullPost.getSlug());
                                    intent1.putExtra("Post_ID", fullPost.getId());
                                    startActivity(intent1);
                                }
                                break;

                            case R.id.deletePost:
                                DeletePost(fullPost.getUser_post_id()+"",fullPost.getShared_id()+"",fullPost.getIs_shared());
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    public void DeletePost(final String  user_post_id, final String  id, final String is_shared) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"delete_post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);
//                        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
                        finish();
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
                params.put("users_post_id",user_post_id+"");
                params.put("id",id+"");
                params.put("is_shared",is_shared+"");
//
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
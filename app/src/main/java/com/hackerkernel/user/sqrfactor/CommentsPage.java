package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.FullPost;
import com.hackerkernel.user.sqrfactor.Pojo.NewsFeedStatus;
import com.hackerkernel.user.sqrfactor.Pojo.ProfileClass1;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.comments_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentsPage extends ToolbarActivity {
    private EditText commentBody;
    private ImageView authImageUrl, userImageUrl, postImage;
    private LinearLayoutManager layoutManager;
    private RecyclerView comentPageRecyclerView;
    private TextView userName, time, postDescription, commentWrite, comments, share,sendButton,
            commentUserName, commentTime, commentDescription, commentLike;
    private ArrayList<comments_list> comments_listArrayList = new ArrayList<>();
    private NewsFeedStatus newsFeedStatus;
    private ProfileClass1 profileClass1;
    private FullPost fullPost;
    private Button postbtn, likelist;
    private String isShared;
    private ImageButton like;
    private CommentsLimitedAdapter commentsLimitedAdapter;
    private int flag = 0, commentable_id,shared_id,user_id;
    private int postSharedId,postId;
    private TextView commentMsg;
    ProgressBar mProgressBar;
    private static final String TAG = CommentsPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_page);


        Bundle extras = getIntent().getExtras();
        postSharedId = extras.getInt("PostSharedId");
        postId=extras.getInt("PostId");
        mProgressBar = findViewById(R.id.comment_progress_bar);
        commentMsg=findViewById(R.id.comMsg);
        comentPageRecyclerView = (RecyclerView) findViewById(R.id.comentPageRecyclerView);

        //Toast.makeText(this,newsFeedStatus.getCommentsLimitedArrayList().get(0).getBody(),Toast.LENGTH_LONG).show();
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comentPageRecyclerView.setLayoutManager(layoutManager);


        commentsLimitedAdapter = new CommentsLimitedAdapter(comments_listArrayList,this,postId);
        comentPageRecyclerView.setAdapter(commentsLimitedAdapter);
        sendButton = (TextView) findViewById(R.id.sendButton);
        commentBody = (EditText) findViewById(R.id.cmmentToSend);

        GetCommetsFromServer();

        final DragToClose dragToClose = findViewById(R.id.comment_drag_to_close);
        dragToClose.setDragListener(new DragListener() {
            @Override
            public void onStartDraggingView() {
                Log.d(TAG, "onStartDraggingView()");
            }

            @Override
            public void onViewCosed() {
                Log.d(TAG, "onViewCosed()");
            }
        });


        SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        userImageUrl = (ImageView)findViewById(R.id.commentPage_profile);

        String[] parsedUrl=null;
        if(userClass.getProfile()!=null)
        {
            parsedUrl=userClass.getProfile().split("/");
        }

        if(parsedUrl!=null && parsedUrl.length>=2 && (parsedUrl[2].equals("graph.facebook.com")||parsedUrl[2].contains("googleusercontent.com")))
        {

            Glide.with(this).load(userClass.getProfile())
                    .into(userImageUrl);


        }
        else {

            Glide.with(this).load(UtilsClass.baseurl1+userClass.getProfile())
                    .into(userImageUrl);

        }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(commentBody.getText().toString()))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    comments_list commentsList=new comments_list(userClass.getUserId(),0,commentBody.getText().toString(),userClass.getUser_name(),userClass.getName(),userClass
                            .getFirst_name(),userClass.getLast_name(),userClass.getProfile(),formatter.format(date));
                    comments_listArrayList.add(commentsList);
                    commentsLimitedAdapter.notifyItemInserted(comments_listArrayList.size());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            comentPageRecyclerView.smoothScrollToPosition(comments_listArrayList.size()-1);
                        }
                    }, 1);

                    sendCommentToServer();

                }

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void GetCommetsFromServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(CommentsPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"commentlist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        mProgressBar.setVisibility(View.GONE);
                        commentMsg.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        Log.v("commentList", s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONArray commentListArray=jsonObject.getJSONArray("comments_list");
                            for (int i=0;i<commentListArray.length();i++)
                            {
                                comments_list commentsList=new comments_list(commentListArray.getJSONObject(i));
                                comments_listArrayList.add(commentsList);

                            }
                            // Collections.reverse(co);

                            commentsLimitedAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                params.put("id",postSharedId+"");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);


        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(comments_listArrayList.size()==0)
                {
                    mProgressBar.setVisibility(View.GONE);
                    commentMsg.setVisibility(View.VISIBLE);
                }else {
                    mProgressBar.setVisibility(View.GONE);
                    commentMsg.setVisibility(View.GONE);
                }
            }
        }, 1500);

    }
    public void sendCommentToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(CommentsPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"comment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike", s);
                        commentBody.setText("");

//                        //Showing toast message of the response
//                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
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

                params.put("commentable_id",  postSharedId+"");
                params.put("comment_text", commentBody.getText().toString());


                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public NewsFeedStatus getMyData() {
        return newsFeedStatus;
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SqrFactor");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }

}
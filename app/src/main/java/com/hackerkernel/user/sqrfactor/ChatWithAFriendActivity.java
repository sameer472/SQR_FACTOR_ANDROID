package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatWithAFriendActivity extends AppCompatActivity {
    static int id;
    MessageClass messageClass=null;
    private boolean currentPage=false;
    String friendProfile, friendName;
    private ArrayList<MessageClass> messageClassArrayList = new ArrayList<>();
    private ChatWithAFriendActivityAdapter chatWithAFriendActivityAdapter;
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private TextView friendNametext;
    private EditText messageToSend;
    private ImageButton sendMessageButton;
    private PullRefreshLayout layout ;
    private boolean isLoading=false,isVisibleBottpmArrow=false,isFirstTimeLoading=true;
    public Context context;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private Toolbar toolbar;
    private ImageView bottom_arrow;
    private String isOnline;
    private  String nextPageUrl;
    private  ActionBar actionBar;
    private UserClass userClass;
    private int loadCount=0;
    private ProgressBar progressBar;
    private TextView noChat;



    //private EndlessRecyclerOnScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_afriend);

        progressBar=findViewById(R.id.progress_bar);
        noChat = findViewById(R.id.chatMsg);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();
        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "sqr");
        TokenClass.Token=token;

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        messageToSend = (EditText) findViewById(R.id.messageToSend);
        sendMessageButton = (ImageButton) findViewById(R.id.sendButton);



        Intent intent = getIntent();
        id = intent.getExtras().getInt("FriendId");
        friendProfile = intent.getExtras().getString("FriendProfileUrl");
        friendName = intent.getExtras().getString("FriendName");
        //isOnline = intent.getExtras().getString("isOnline");

        //Toast.makeText(getApplicationContext(),"res "+id,Toast.LENGTH_LONG).show();



        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(layoutManager);
        chatWithAFriendActivityAdapter = new ChatWithAFriendActivityAdapter(messageClassArrayList, getApplicationContext(), id, friendProfile, friendName);
        recycler.setAdapter(chatWithAFriendActivityAdapter);
        bottom_arrow=(ImageView)findViewById(R.id.bottom_arrow);

        FireBaseListnerForFriend(id);

        StringRequest myReq = new StringRequest(Request.Method.POST, "https://sqrfactor.com/api/myallMSG/getChat/" +id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        noChat.setVisibility(View.GONE);
                        messageClassArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl=jsonObject.getString("nextChats");
                            JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");
                            // Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                messageClassArrayList.add(messageClass);
                            }


                            Collections.reverse(messageClassArrayList);
                            chatWithAFriendActivityAdapter.notifyDataSetChanged();

                            FirebaseListner();

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

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);





        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(messageClassArrayList.size()==0)
                {
                    progressBar.setVisibility(View.GONE);
                    noChat.setVisibility(View.VISIBLE);
                }
            }
        }, 1500);


        bottom_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisibleBottpmArrow) {
                    bottom_arrow.setVisibility(View.GONE);
                    isVisibleBottpmArrow = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(messageClassArrayList.size() - 1);

                        }
                    }, 1);
                }
            }
        });


        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading=false;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=layoutManager.findLastVisibleItemPosition();

                if(dy>0 &&lastId==messageClassArrayList.size()-3 && isVisibleBottpmArrow)
                {
                    bottom_arrow.setVisibility(View.GONE);
                    isVisibleBottpmArrow=false;
                }
                if(dy<0 && lastId<=18 &&! isVisibleBottpmArrow)
                {
                    bottom_arrow.setVisibility(View.VISIBLE);
                    isVisibleBottpmArrow=true;
                }
                if(dy<0 && lastId==10 && !isLoading)
                {
                    isLoading=true;
                    fetchMoreChatDataFromServer();

                }
            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SendMessageToServer(messageToSend.getText().toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                Toast.makeText(getApplicationContext(),"time"+formatter.format(date),Toast.LENGTH_LONG).show();

                if(messageClassArrayList!=null && messageClassArrayList.size()>0)
                {
                    MessageClass messageClass=
                            new MessageClass(messageClassArrayList.get(messageClassArrayList.size()-1).getMessageId()+1,userClass.getUserId(),id,messageToSend.getText().toString(),"1",formatter.format(date),formatter.format(date));

                    messageClassArrayList.add(messageClass);
                    messageToSend.setText("");
                    chatWithAFriendActivityAdapter.notifyItemInserted(messageClassArrayList.size()-1);
                    if(isVisibleBottpmArrow)
                    {
                        isVisibleBottpmArrow=false;
                        bottom_arrow.setVisibility(View.GONE);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(messageClassArrayList.size()-1);
                        }
                    }, 1);
                }
                else {
                    MessageClass messageClass=
                            new MessageClass(1,userClass.getUserId(),id,messageToSend.getText().toString(),"1",formatter.format(date),formatter.format(date));

                    messageClassArrayList.add(messageClass);
                    messageToSend.setText("");
                    noChat.setVisibility(View.GONE);
                    chatWithAFriendActivityAdapter.notifyItemInserted(0);
                    if(isVisibleBottpmArrow)
                    {
                        isVisibleBottpmArrow=false;
                        bottom_arrow.setVisibility(View.GONE);
                    }

                }




            }
        });
    }

    private void FireBaseListnerForFriend(int id) {
        ref.child("Status").child(id+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                IsOnline isOnline=dataSnapshot.child("android").getValue(IsOnline.class);
                IsOnline isOnline1=dataSnapshot.child("web").getValue(IsOnline.class);

                if(isOnline!=null && isOnline.getIsOnline().equals("True"))
                {

                   SetTitle("True");

                }

                else if(isOnline1!=null && isOnline1.getIsOnline().equals("True"))
                {


                    SetTitle("True");
                }
                else {

                    SetTitle("False");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SetTitle(String online)
    {

//        actionBar.setTitle(friendName);
        actionBar.setTitle(Html.fromHtml("<font color='#fffffff'>"+friendName+ "</font>"));

        if (online.equals("True")) {
            actionBar.setSubtitle(Html.fromHtml("<font color='#fffffff'> Online </font>"));
        } else {
            actionBar.setSubtitle(Html.fromHtml("<font color='#fffffff'> Offline </font>"));
        }

    }




    @Override
    protected void onResume() {
        super.onResume();
        loadCount=0;

    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPage=false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        IsOnline isOnline=new IsOnline("False",formatter.format(date));
        ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentPage=true;
        loadCount=0;


    }

    public void fetchMoreChatDataFromServer() {

        if(nextPageUrl!=null)
        {
            final ArrayList<MessageClass> messageClassArrayList1=new ArrayList<>();
            final ArrayList<MessageClass> finalmessageClassArrayList=new ArrayList<MessageClass>(messageClassArrayList);
            Log.v("ArraySize111",finalmessageClassArrayList.size()+"");
            StringRequest myReq = new StringRequest(Request.Method.POST,nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("MorenewsFeedFromServer", response);
//                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            messageClassArrayList1.clear();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                nextPageUrl=jsonObject.getString("nextChats");
                                JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                                JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    //Log.v("Response",response);
                                    MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                    messageClassArrayList1.add(messageClass);
                                }

                                Log.v("ArraySize333",messageClassArrayList1.size()+"");
                                Collections.reverse(messageClassArrayList1);
                                for(int i=0;i<messageClassArrayList1.size();i++)
                                {
                                    messageClassArrayList.add(0,messageClassArrayList1.get(i));
                                    chatWithAFriendActivityAdapter.notifyItemInserted(0);
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
                            //isLoading=false;
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer "+TokenClass.Token);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(myReq);
        }

    }

    public void FirebaseListner() {

        ref.child("Chats").child(userClass.getUserId() + "").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                LastMessage lastMessage = dataSnapshot.getValue(LastMessage.class);
                if ( loadCount!=0 && lastMessage != null && id == lastMessage.getSenderId()) {


                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    if (messageClassArrayList.size() != 0) {
                        messageClass =
                                new MessageClass(messageClassArrayList.get(messageClassArrayList.size() - 1).getMessageId() + 1, id, userClass.getUserId(), lastMessage.getMessage(), "1", formatter.format(date), formatter.format(date));

                    }

                    messageClassArrayList.add(messageClass);
                    chatWithAFriendActivityAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(messageClassArrayList.size() - 1);
                        }
                    }, 1);
                    if(currentPage)
                    {
                        UpdateChatStatus(lastMessage.getChat_id());
                    }


                }
                else {
                    loadCount=1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateChatStatus(final int chatId) {
//        Toast.makeText(getApplicationContext(), chatId+"", Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://sqrfactor.com/api/read_status"
                ,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        //Log.v("ReponseFeed", response);
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",chatId+"");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
        };

        requestQueue.add(myReq);
    }


    private void SendMessageToServer(final String message)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://sqrfactor.com/api/chat/sendChat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {


                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.v("chat",res);
//                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
//                                Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
//                                Toast.makeText(getApplicationContext(),e2.toString(),Toast.LENGTH_LONG).show();
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //Log.v("request",userClass.getUserId()+" "+id+" " +messageToSend.getText().toString());

                params.put("userId",userClass.getUserId()+"");
                params.put("friendId",id+"" );
                params.put("message",message );


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
        };

        requestQueue.add(myReq);

    }

}


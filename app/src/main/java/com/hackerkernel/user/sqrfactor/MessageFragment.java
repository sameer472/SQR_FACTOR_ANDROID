package com.hackerkernel.user.sqrfactor;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.ChatFriends;
import com.hackerkernel.user.sqrfactor.Pojo.IsOnline;
import com.hackerkernel.user.sqrfactor.Pojo.LastMessage;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private ArrayList<ChatFriends> chatFriends = new ArrayList<>();
    //private ArrayList<ChatFriends> chatFriends = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private Context context;
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    public static String userProfile,userName;
    public static  int userId;
    public static FirebaseDatabase database;
    public static DatabaseReference ref;
    UserClass userClass;
    PullRefreshLayout layout;
    private ProgressBar progress_bar_chat;
    private LinearLayout friendMsg;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();
        progress_bar_chat=v.findViewById(R.id.progress_bar_chat);
        friendMsg=(LinearLayout)v.findViewById(R.id.friendMsg);
        recycler = (RecyclerView)v.findViewById(R.id.msg_recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatFriends,getActivity());
        recycler.setAdapter(chatAdapter);

        layout = v.findViewById(R.id.msg_pullRefresh);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //LoadNewsFeedDataFromServer();
                //layout.setRefreshing(false);
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);

                    }
                },100);

            }
        });
        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);
        //String token_id=FirebaseInstanceId.getInstance().getToken();


        if(savedInstanceState==null)
        {
            getAllFriendsList();
            //HomeScreen.getUnReadMsgCount();
        }

        else {
            chatFriends=(ArrayList<ChatFriends>)savedInstanceState.getSerializable("Old");
            chatAdapter.notifyDataSetChanged();
//            DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference().child("Status").child(userId+"");
//            IsOnline isOnline=new IsOnline("False",ServerValue.TIMESTAMP.toString());
//            presenceRef.onDisconnect().setValue(isOnline);
            StatusLinstner();
        }

        ref.child("Chats").child(userClass.getUserId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(), "chat Listing", Toast.LENGTH_SHORT).show();
                LastMessage lastMessage = dataSnapshot.getValue(LastMessage.class);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllFriendsList();

                    }
                }, 200);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFriendsList();

    }


    private void getAllFriendsList() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"message/"+userClass.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("friends");
                            if (chatFriends!=null)
                            {
                                chatFriends.clear();
                            }
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response",response);
                                ChatFriends chatFriends1 = new ChatFriends(jsonArrayData.getJSONObject(i));
                                chatFriends1.setIsOnline("False");
                                chatFriends.add(chatFriends1);
                            }
                            Collections.sort(chatFriends, new Comparator<ChatFriends>(){
                                public int compare(ChatFriends o1, ChatFriends o2){
                                    return o2.getCreated_at().compareTo(o1.getCreated_at());
                                }
                            });
                            progress_bar_chat.setVisibility(View.GONE);
                            chatAdapter.notifyDataSetChanged();
//
                            StatusLinstner();





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

        requestQueue.add(myReq);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(chatFriends.size()==0)
                {
                    progress_bar_chat.setVisibility(View.GONE);
                    friendMsg.setVisibility(View.VISIBLE);
                }
            }
        }, 1600);
    }

    private int getIndexByProperty(int userId) {
        for (int i = 0; i < chatFriends.size(); i++) {
            if (chatFriends.get(i) !=null && chatFriends.get(i).getUserID()==userId) {
                return i;
            }
        }
        return -1;// not there is list
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Old", chatFriends);
    }

    public void StatusLinstner()
    {

        for(int i=0;i<chatFriends.size();i++)
        {
            final int finalI = i;
            ref.child("Status").child(chatFriends.get(i).getUserID()+"").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //Toast.makeText(getActivity(),"listning",Toast.LENGTH_LONG).show();
                    IsOnline isOnline=dataSnapshot.child("android").getValue(IsOnline.class);
                    IsOnline isOnline1=dataSnapshot.child("web").getValue(IsOnline.class);

                    if(isOnline!=null && (isOnline.getIsOnline().equals("True") || isOnline1.getIsOnline().equals("True")))
                    {

                        ChatFriends chatFriend= chatFriends.get(finalI);
                        chatFriend.setIsOnline("True");
                        chatFriends.set(finalI,chatFriend);
                        chatAdapter.notifyItemChanged(finalI);
                        //chatAdapter.notifyItemInserted(finalI);
                        //chatAdapter.notifyDataSetChanged();
                    }

                    else if(isOnline!=null)
                    {
                        ChatFriends chatFriend= chatFriends.get(finalI);
                        chatFriend.setIsOnline("False");
                        chatFriends.set(finalI,chatFriend);
                        //chatAdapter.notifyItemChanged(finalI);
                        //chatAdapter.notifyItemInserted(finalI);
                        chatAdapter.notifyItemChanged(finalI);

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
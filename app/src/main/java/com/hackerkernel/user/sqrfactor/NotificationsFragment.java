package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NotificationClass> notificationsClassArrayList = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;
    private Button send;
    private boolean isLoading = false;
    private Context context;
    PullRefreshLayout layout;
    private ProgressBar progressBar;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private String nextPageUrl;
    LinearLayout commentMsg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();

        commentMsg=view.findViewById(R.id.commentMsg);
        progressBar=view.findViewById(R.id.progress_bar);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        notificationsAdapter = new NotificationsAdapter(notificationsClassArrayList, getActivity());
        recycler.setAdapter(notificationsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isLoading = false;
                //Toast.makeText(context,"moving down",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId = linearLayoutManager.findLastVisibleItemPosition();
//                if(dy>0)
//                {
//                    Toast.makeText(context,"moving up",Toast.LENGTH_SHORT).show();
//                }
                if (dy > 0 && lastId + 3 > linearLayoutManager.getItemCount() && !isLoading) {
                    isLoading = true;
                    Log.v("rolling", linearLayoutManager.getChildCount() + " " + linearLayoutManager.getItemCount() + " " + linearLayoutManager.findLastVisibleItemPosition() + " " +
                            linearLayoutManager.findLastVisibleItemPosition());
                    LoadMoreNotification();

                }
            }
        });

        layout = view.findViewById(R.id.notification_pullRefresh);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //LoadNewsFeedDataFromServer();
                //layout.setRefreshing(false);
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                       NotificationLoad();
                    }
                },1000);

            }
        });


        NotificationLoad();

        return view;

    }




    public void NotificationLoad(){

        if(notificationsClassArrayList!=null && notificationsClassArrayList.size()>0)
            notificationsClassArrayList.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl = jsonObject.getString("nextpage");
                            JSONArray jsonArrayData = jsonObject.getJSONArray("notifications");

                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response", response);
                                NotificationClass notificationsClass = new NotificationClass(jsonArrayData.getJSONObject(i));
                                notificationsClassArrayList.add(notificationsClass);
                            }
                            progressBar.setVisibility(View.GONE);
                            notificationsAdapter.notifyDataSetChanged();


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
                params.put("Authorization", "Bearer " + TokenClass.Token);

                return params;
            }

        };

        requestQueue.add(myReq);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(notificationsClassArrayList.size()==0)
                {
                    commentMsg.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, 1000);

    }
    public void LoadMoreNotification() {
        if (nextPageUrl != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest myReq = new StringRequest(Request.Method.POST,nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArrayData = jsonObject.getJSONArray("notifications");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    Log.v("Response", response);
                                    NotificationClass notificationsClass = new NotificationClass(jsonArrayData.getJSONObject(i));
                                    notificationsClassArrayList.add(notificationsClass);
                                }
                                notificationsAdapter.notifyDataSetChanged();


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
                    params.put("Authorization", "Bearer " + TokenClass.Token);

                    return params;
                }

            };

            requestQueue.add(myReq);

        }
    }
}
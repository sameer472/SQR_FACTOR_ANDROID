package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Pojo.ContributorsClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContributorsActivity extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private ArrayList<ContributorsClass> contributorsClassArrayList = new ArrayList<>();
    private boolean isDataFetched;
    private boolean mIsVisibleToUser;
    private ContributorsAdapter contributorsAdapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Top Contributors");
        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progress_bar_cont);
        recyclerView1 = findViewById(R.id.contributors_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contributorsAdapter = new ContributorsAdapter(contributorsClassArrayList, this);
        recyclerView1.setAdapter(contributorsAdapter);

        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"contributors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isDataFetched = true;
                        Log.v("ReponseFeed33", response);
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //    JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData = jsonObject.getJSONArray("top_contributors");


                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                //Log.v("Response",response);
                                ContributorsClass contributorsClass = new ContributorsClass(jsonArrayData.getJSONObject(i));
                                contributorsClassArrayList.add(contributorsClass);
                            }
                            contributorsAdapter.notifyDataSetChanged();


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


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(contributorsClassArrayList.size()==0)
                {
                    progressBar.setVisibility(View.GONE);

                }
            }
        }, 2000);

    }
}

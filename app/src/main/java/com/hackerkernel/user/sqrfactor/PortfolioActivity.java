package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Pojo.PortfolioClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String userName;
    private TextView noPortfoilo;
    PortfolioAdapter portfolioAdapter;
    private ArrayList<PortfolioClass> portfolioArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar=findViewById(R.id.progress_bar_portfolio);
        noPortfoilo = findViewById(R.id.noPortfoilo);
        recyclerView = findViewById(R.id.recyclerView_portfolio);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        portfolioAdapter = new PortfolioAdapter(portfolioArrayList,this);

        Intent intent=getIntent();
        if(intent!=null)
        {
            userName=intent.getStringExtra("UserName");
        }
        recyclerView.setAdapter(portfolioAdapter);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"profile/portfolio/"+userName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        noPortfoilo.setVisibility(View.GONE);
//                        Log.v("portfolioResponse", response);
//                        Toast.makeText(PortfolioActivity.this, response, Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray posts=jsonObject.getJSONArray("posts");
                            for(int i=0;i<posts.length();i++)
                            {
                                PortfolioClass portfolioClass=new PortfolioClass(posts.getJSONObject(i));
                                portfolioArrayList.add(portfolioClass);
                            }

                            portfolioAdapter.notifyDataSetChanged();



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
                if(portfolioArrayList.size()==0)
                {
                    progressBar.setVisibility(View.GONE);
                    noPortfoilo.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    noPortfoilo.setVisibility(View.GONE);
                }
            }
        }, 1000);

    }
}
package com.hackerkernel.user.sqrfactor;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RedFragment extends Fragment {
    private ProgressBar progressBar;
    private ArrayList<NewsFeedStatus> newsstatus = new ArrayList<>();
    private RecyclerView recyclerView;
    private boolean isScrolling=false;
    int currentItems, totalItems, scrolledItems;
    private RedAdapter redAdapter;
    private LinearLayoutManager layoutManager;
    private Button btn1,btn2,btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_red, container, false);

        recyclerView = v.findViewById(R.id.red_recycler);
        progressBar = v.findViewById(R.id.progress_bar_red);
        progressBar.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        redAdapter = new RedAdapter(getActivity(), newsstatus);
        recyclerView.setAdapter(redAdapter);



//        btn1 = v.findViewById(R.id.red_newsFeedbtn);
        btn2 = v.findViewById(R.id.red_whatsRedbtn);
        btn3 = v.findViewById(R.id.red_Topbtn);

        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.red_pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PageRefersh(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        progressBar.setVisibility(View.VISIBLE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                PageRefersh();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TopContributors.class);
                startActivity(intent);
            }
        });





        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrolledItems = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledItems == totalItems)) {
                    isScrolling = false;
                    fetchDataFromServer();
                }
            }
        });
        PageRefersh();

        return v;
    }
    public void PageRefersh(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"whats-red",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {

                                NewsFeedStatus newsFeedStatus1 = new NewsFeedStatus(jsonArrayData.getJSONObject(i));
                                newsstatus.add(newsFeedStatus1);
                            }
                            redAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);


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
    public void fetchDataFromServer() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"whats-red",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.v("MoreRedResponse", response);
//                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                NewsFeedStatus newsFeedStatus1 = new NewsFeedStatus(jsonArrayData.getJSONObject(i));
                                newsstatus.add(newsFeedStatus1);
                            }

                            redAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);


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

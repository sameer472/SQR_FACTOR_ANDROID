package com.hackerkernel.user.sqrfactor;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchitectureCollege extends Fragment {

    private RecyclerView recyclerView1;
    private ArrayList<ArchitectureCollegeClass> architectureCollegeClassArrayList =new ArrayList<>();
    private boolean isDataFetched;
    private boolean mIsVisibleToUser;
    private ArchitectureCollegeAdapter architectureCollegeAdapter;
    private ProgressBar progress_bar;
    public ArchitectureCollege() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_architecture_college, container, false);
        progress_bar = view.findViewById(R.id.progress_bar_clg);
        recyclerView1 =view.findViewById(R.id.architectureCollege_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        architectureCollegeAdapter=new ArchitectureCollegeAdapter(architectureCollegeClassArrayList,getContext());
        recyclerView1.setAdapter(architectureCollegeAdapter);



        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && !isDataFetched && getContext() != null) {
            //context = getContext();
            LoadData(); //Remove this call from onCreateView
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void LoadData()
    {
        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"colleges",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isDataFetched=true;
                        progress_bar.setVisibility(View.GONE);

//                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //    JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObject.getJSONArray("list_of_colleges_in_same_city");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                //Log.v("Response",response);
                                ArchitectureCollegeClass architectureCollegeClass = new ArchitectureCollegeClass(jsonArrayData.getJSONObject(i));
                                architectureCollegeClassArrayList.add(architectureCollegeClass);
                            }

                            architectureCollegeAdapter.notifyDataSetChanged();
//
//                            //Collections.reverse(messageClassArrayList);
//                            chatWithAFriendActivityAdapter.notifyDataSetChanged();
//                            //recycler.scrollToPosition(messageClassArrayList.size()-1);
//                            FirebaseListner();



//                            recycler.smoothScrollToPosition(messageClassArrayList.size()-1);


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


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(myReq);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(architectureCollegeClassArrayList.size()==0)
                {
                    progress_bar.setVisibility(View.GONE);
                }
            }
        }, 800);

    }


}

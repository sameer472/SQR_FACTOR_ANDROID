package com.hackerkernel.user.sqrfactor;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ContributorsFragment extends Fragment {
    private RecyclerView recyclerView1;
    private ArrayList<ContributorsClass> contributorsClassArrayList =new ArrayList<>();
    private boolean isDataFetched;
    private boolean mIsVisibleToUser;
    private ContributorsAdapter contributorsAdapter;

    public ContributorsFragment() {
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
        View view= inflater.inflate(R.layout.contributors_fragment, container, false);
        recyclerView1 =view.findViewById(R.id.contributors_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       contributorsAdapter=new ContributorsAdapter(contributorsClassArrayList,getContext());
        recyclerView1.setAdapter(contributorsAdapter);



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
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"contributors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isDataFetched=true;
                        Log.v("ReponseFeed33", response);
//                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //    JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObject.getJSONArray("top_contributors");


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
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(myReq);


    }


}

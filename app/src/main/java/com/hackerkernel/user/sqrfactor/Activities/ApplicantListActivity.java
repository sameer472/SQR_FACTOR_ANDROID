package com.hackerkernel.user.sqrfactor.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Adapters.UserAdapter;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicantListActivity extends AppCompatActivity {

    TextView noDataTv;
    private RequestQueue mRequestQueue;
    MySharedPreferences mSp;
    String status;
    List<UserClass> usersList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String user_job_id;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSp = MySharedPreferences.getInstance(ApplicantListActivity.this);
        mRequestQueue = Volley.newRequestQueue(ApplicantListActivity.this);
        bundle = getIntent().getExtras();
        user_job_id = bundle.getString("user_job_id");
        Log.d("user_job_id: ", user_job_id);
        usersList = new ArrayList<>();
        noDataTv = findViewById(R.id.noDataTv);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lm = new LinearLayoutManager(ApplicantListActivity.this);
        recyclerView.setLayoutManager(lm);
        viewApplicant();
        adapter = new UserAdapter(usersList, ApplicantListActivity.this);
        recyclerView.setAdapter(adapter);


    }

    private void viewApplicant() {
        ViewUtils.showProgressBar(ApplicantListActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://archsqr.in/api/job/view-applicant", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("viewApplicant: ", response);
                ViewUtils.dismissProgressBar();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("joblist")) {
                        JSONArray array = object.getJSONArray("joblist");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            JSONObject array1 = object1.getJSONObject("user");
                            UserClass aClass = new UserClass();
                            aClass.setId(array1.getString("id"));
                            aClass.setName(array1.getString("name"));
                            aClass.setEmail(array1.getString("email"));
                            aClass.setMobileNumber(array1.getString("mobile_number"));
                            aClass.setProfilePicPath(array1.getString("profile"));
                            usersList.add(aClass);
                        }
                        showApplicant(true, "");
                        adapter.notifyDataSetChanged();
                    } else {
                        showApplicant(false, object.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ViewUtils.dismissProgressBar();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put(ApplicantListActivity.this.getString(R.string.accept), ApplicantListActivity.this.getString(R.string.application_json));
                header.put(ApplicantListActivity.this.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("user_job_id", user_job_id);
                params.put("user_job_id", "10");
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void showApplicant(boolean b, String message) {
        if (b) {
            noDataTv.setVisibility(View.GONE);

        } else {
            noDataTv.setVisibility(View.VISIBLE);
            noDataTv.setText(message);
            recyclerView.setVisibility(View.GONE);
        }
    }

}

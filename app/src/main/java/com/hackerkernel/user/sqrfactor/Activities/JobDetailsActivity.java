package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class JobDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textview;
    Bundle bundle = new Bundle();
    List<String> jobDetailsList;
    TextView textView1,
            textView2,
            textView3,
            textView4,
            textView5,
            textView6,
            textView7,
            textView8,
            textView9,
            textView10;
    private RequestQueue mRequestQueue;
    MySharedPreferences mSp;
    AlertDialog dialog;
    String status;
    Button view_applicant;
    List<UserClass> usersList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bundle = getIntent().getExtras();
        view_applicant = findViewById(R.id.view_applicant);
        mSp = MySharedPreferences.getInstance(JobDetailsActivity.this);
        jobDetailsList = bundle.getStringArrayList("jobDetails");
        usersList = new ArrayList<>();
        status = bundle.getString("currentUserPost");
        Log.d("status: ", status);
        if (status.equals("true")) {
            view_applicant.setVisibility(View.VISIBLE);
        } else {
            view_applicant.setVisibility(View.GONE);
        }
        mRequestQueue = Volley.newRequestQueue(JobDetailsActivity.this);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        textView5 = findViewById(R.id.textview5);
        textView6 = findViewById(R.id.textview6);
        textView7 = findViewById(R.id.textview7);
        textView8 = findViewById(R.id.textview8);
        textView9 = findViewById(R.id.textview9);
        textView10 = findViewById(R.id.textview10);

        try {
            textView1.setText(jobDetailsList.get(0) + "");
            textView2.setText(jobDetailsList.get(1) + "");
            textView3.setText(jobDetailsList.get(2) + "");
            textView4.setText(jobDetailsList.get(3) + "");
            textView5.setText(jobDetailsList.get(4) + "");
            textView6.setText(jobDetailsList.get(5) + "");
            textView7.setText(jobDetailsList.get(6) + "");
            textView8.setText(jobDetailsList.get(7) + "");
            textView9.setText(jobDetailsList.get(8) + "");
            textView10.setText(jobDetailsList.get(9) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.applyNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        view_applicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobDetailsActivity.this,ApplicantListActivity.class);
                intent.putExtra("user_job_id",jobDetailsList.get(11));
                startActivity(intent);
                //viewApplicant();
            }
        });
    }


    private void viewApplicant() {
        usersList.clear();
        usersList = new ArrayList<>();
        ViewUtils.showProgressBar(JobDetailsActivity.this);
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
                header.put(JobDetailsActivity.this.getString(R.string.accept), JobDetailsActivity.this.getString(R.string.application_json));
                header.put(JobDetailsActivity.this.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_job_id", "10");
                return params;
            }
        };

        mRequestQueue.add(request);
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JobDetailsActivity.this);
        View v = JobDetailsActivity.this.getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
        builder.setView(v);
        final Button apply = v.findViewById(R.id.apply);
        final Button cancel = v.findViewById(R.id.cancel);

        dialog = builder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyForJob();
            }
        });


        dialog.show();
    }

    public void showApplicant(boolean b, String message) {
        TextView noDataTv;
        AlertDialog.Builder builder = new AlertDialog.Builder(JobDetailsActivity.this);
        View v = JobDetailsActivity.this.getLayoutInflater().inflate(R.layout.applicant_layout, null, false);
        builder.setView(v);
        noDataTv = v.findViewById(R.id.noDataTv);
        recyclerView = v.findViewById(R.id.recycler_view);
        if (b) {
            noDataTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            final LinearLayoutManager lm = new LinearLayoutManager(JobDetailsActivity.this);
            recyclerView.setLayoutManager(lm);
            adapter = new UserAdapter(usersList, JobDetailsActivity.this);
            recyclerView.setAdapter(adapter);
        } else {
            noDataTv.setVisibility(View.VISIBLE);
            noDataTv.setText(message);
            recyclerView.setVisibility(View.GONE);
        }


        dialog = builder.create();
        dialog.show();
    }

    private void applyForJob() {
        ViewUtils.showProgressBar(JobDetailsActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://archsqr.in/api/job/apply-job", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ViewUtils.dismissProgressBar();
                Log.d("onResponse: ", response);
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(JobDetailsActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
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
                header.put(JobDetailsActivity.this.getString(R.string.accept), JobDetailsActivity.this.getString(R.string.application_json));
                header.put(JobDetailsActivity.this.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_job_id", jobDetailsList.get(11));
                params.put("user_id", jobDetailsList.get(10));
                return params;
            }
        };

        mRequestQueue.add(request);
    }
}

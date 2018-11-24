package com.hackerkernel.user.sqrfactor.Activities.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Activities.PostJobActivity;
import com.hackerkernel.user.sqrfactor.Adapters.JobsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Pojo.Jobs.EduQulBean;
import com.hackerkernel.user.sqrfactor.Pojo.Jobs.JobBean;
import com.hackerkernel.user.sqrfactor.Pojo.Jobs.SkillsBean;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    Toolbar toolbar;
    private MySharedPreferences mSp;
    List<JobBean> jobBeanList;
    List<SkillsBean> skillsBeanList;
    List<EduQulBean> eduQulBeanList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public int requestCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRequestQueue = Volley.newRequestQueue(JobActivity.this);
        mSp = MySharedPreferences.getInstance(JobActivity.this);
        recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PostJobActivity.class);
                startActivity(i);
            }
        });


        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lm = new LinearLayoutManager(JobActivity.this);
        recyclerView.setLayoutManager(lm);


        jobBeanList = new ArrayList<>();
        skillsBeanList = new ArrayList<>();
        eduQulBeanList = new ArrayList<>();
        getData();
        adapter = new JobsAdapter(jobBeanList, JobActivity.this);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLastItemDisplaying(recyclerView)) {
                    getData();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    public void getData() {
        getJobs(requestCount);
        requestCount++;
    }

    private void getJobs(int requestCount) {
        Log.d("requestCount: ", requestCount + "");
        ViewUtils.showProgressBar(JobActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://archsqr.in/api/job?page=" + requestCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse: ", response);
                ViewUtils.dismissProgressBar();

                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject object1 = object.getJSONObject("joblist");
                    JSONArray array = new JSONArray(object1.getString("data"));
                    Log.d("array1: ", array.toString());
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object2 = array.getJSONObject(i);
                            JobBean bean = new JobBean();
                            bean.setId(object2.getString("id"));
                            bean.setUser_id(object2.getString("user_id"));
                            bean.setSlug(object2.getString("slug"));
                            bean.setJob_title(object2.getString("job_title"));
                            bean.setDescription(object2.getString("description"));
                            bean.setCategory(object2.getString("category"));
                            bean.setType_of_position(object2.getString("type_of_position"));
                            bean.setWork_experience(object2.getString("work_experience"));
                            bean.setFirm(object2.getString("firm"));

                            JSONObject objectCountryMain = new JSONObject(object2.getString("country"));
                            bean.setCountry_id(objectCountryMain.getString("name"));
                            JSONObject objectStateMain = new JSONObject(object2.getString("state"));
                            bean.setState_id(objectStateMain.getString("name"));
                            JSONObject objectCityMain = new JSONObject(object2.getString("city"));
                            bean.setCity_id(objectCityMain.getString("name"));

                            bean.setSalary_type(object2.getString("salary_type"));
                            bean.setMaximum_salary(object2.getString("maximum_salary"));
                            bean.setMinimum_salary(object2.getString("minimum_salary"));
                            bean.setJob_offer_expires_on(object2.getString("job_offer_expires_on"));
                            bean.setCreated_at(object2.getString("created_at"));
                            bean.setUpdated_at(object2.getString("updated_at"));
                            bean.setDeleted_at(object2.getString("deleted_at"));
                            JSONArray array2 = new JSONArray(object2.getString("skills"));
                            if (array2.length() != 0) {
                                for (int j = 0; j < array2.length(); j++) {
                                    JSONObject object3 = array2.getJSONObject(j);
                                    SkillsBean bean2 = new SkillsBean();
                                    bean2.setId(object3.getString("id"));
                                    bean2.setUsers_job_id(object3.getString("users_job_id"));
                                    bean2.setSlug(object3.getString("slug"));
                                    bean2.setSkills(object3.getString("skills"));
                                    bean2.setCreated_at(object3.getString("created_at"));
                                    bean2.setUpdated_at(object3.getString("updated_at"));
                                    bean2.setDeleted_at(object3.getString("deleted_at"));
                                    skillsBeanList.add(bean2);
                                    bean.setSkillsBeanList(skillsBeanList);

                                }
                            }
                            JSONArray array3 = new JSONArray(object2.getString("educational_qualification"));
                            Log.d("array3: ", array3 + "");
                            if (array3.length() != 0) {
                                for (int k = 0; k < array3.length(); k++) {
                                    JSONObject object4 = array3.getJSONObject(k);
                                    EduQulBean bean3 = new EduQulBean();
                                    bean3.setId(object4.getString("id"));
                                    bean3.setUsers_job_id(object4.getString("users_job_id"));
                                    bean3.setEducational_qualification(object4.getString("educational_qualification"));
                                    bean3.setCreated_at(object4.getString("created_at"));
                                    bean3.setUpdated_at(object4.getString("updated_at"));
                                    bean3.setDeleted_at(object4.getString("deleted_at"));
                                    eduQulBeanList.add(bean3);
                                    bean.setEduQulBeanList(eduQulBeanList);
                                }
                            }
                            jobBeanList.add(bean);


                        }
                        adapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {

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
                Log.d("getHeaders: ", Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                header.put(JobActivity.this.getString(R.string.accept), JobActivity.this.getString(R.string.application_json));
                header.put(JobActivity.this.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }
}

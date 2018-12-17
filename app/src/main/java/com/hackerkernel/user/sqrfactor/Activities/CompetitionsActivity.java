package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.Tasks;
import com.hackerkernel.user.sqrfactor.Adapters.CompetitionsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Interfaces.OnLoadMoreListener;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.CompetitionClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CompetitionsActivity extends AppCompatActivity {
    private static final String TAG = "CompetitionsActivity";

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private CompetitionsAdapter mAdapter;
    private List<CompetitionClass> mCompetitions;
    private RequestQueue mRequestQueue;
    private Toolbar mToolbar;

    private String mNextPageUrl = null;

    private ProgressBar mPb;
    private MySharedPreferences mSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPb = findViewById(R.id.pb);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mSp = MySharedPreferences.getInstance(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.competitions_rv);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCompetitions = new ArrayList<>();
        mAdapter = new CompetitionsAdapter(this, mCompetitions, mRecyclerView);

        mFab = findViewById(R.id.fab);

        //infinite scroll
        OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: called");
                Tasks.call(new Callable<Void>() {
                    @Override
                    public Void call() {
                        mCompetitions.add(null);
                        mAdapter.notifyItemInserted(mCompetitions.size() - 1);
                        return null;
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mCompetitions.isEmpty()) {
                            mCompetitions.remove(mCompetitions.size() - 1);
                            mAdapter.notifyItemRemoved(mCompetitions.size());
                        }

                        if (mNextPageUrl != null && !mNextPageUrl.equals("null")) {
                            competitionsApi(true);
                        } else {
                            Toast.makeText(getApplicationContext(), "No more posts", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 2000);
            }
        };
        mAdapter.setOnLoadMoreListener(onLoadMoreListener);

        mRecyclerView.setAdapter(mAdapter);

        competitionsApi(false);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LaunchCompetitionActivity.class);
                startActivity(i);
            }
        });

        Log.d(TAG, "onCreate: api key = " + mSp.getKey(SPConstants.API_KEY));
    }

    private void competitionsApi(final boolean isNextPageRequest) {
        Log.d(TAG, "competitionsApi: called");

        //Build url
        String url;
        if (isNextPageRequest) {
            url = mNextPageUrl;
        } else {
            url = ServerConstants.COMPETITION;

            mPb.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, url/* + "?page=2"*/, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: Competitions response = " + response);

                //Clear list if first request
                if (!isNextPageRequest) {
                    mCompetitions.clear();
                }


                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject competitionsListObject = responseObject.getJSONObject("competitions_list");

                    JSONArray dataArray = competitionsListObject.getJSONArray("data");

                    mNextPageUrl = competitionsListObject.getString("next_page_url");
                    Log.d(TAG, "onResponse: next page url = " + mNextPageUrl);

                    if (dataArray.length() > 0) {

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject singleObject = dataArray.getJSONObject(i);

                            String id = singleObject.getString("id");
                            String userId = singleObject.getString("user_id");
                            String slug = singleObject.getString("slug");
                            String name = singleObject.getString("competition_title");
                            String imageUrl = singleObject.getString("cover_image");
                            String lastRegDate = singleObject.getString("schedule_close_date_of_registration");
                            String lastSubmissionDate = singleObject.getString("schedule_closing_date_of_project_submission");
                            String createdAt = singleObject.getString("created_at");
                            String competitionType = singleObject.getString("competition_type");

                            String prize = singleObject.getString("total_prizemoney");

                            CompetitionClass competition = new CompetitionClass(id, userId, slug, name, createdAt, imageUrl, lastSubmissionDate, lastRegDate, prize, competitionType);
                            mCompetitions.add(competition);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                        }

                    } else {
                        Toast.makeText(CompetitionsActivity.this, "No Report found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, CompetitionsActivity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                Log.d(TAG, "getHeaders: api key = " + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

}

package com.hackerkernel.user.sqrfactor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Activities.CompetitionDetailActivity;
import com.hackerkernel.user.sqrfactor.Adapters.SubmissionsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.SubmissionClass;
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

public class SubmissionsFragment extends Fragment {
    private static final String TAG = "SubmissionsFragment";

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private List<SubmissionClass> mSubmissions;
    private SubmissionsAdapter mSubmissionsAdapter;

    private Spinner mSortSpinner;
    //LinearLayout mContentLayout;
    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private String mSlug;

    int mRequestCount = 1;
    private int mSortRequestCount = 1;

    String mType = "";
    private RecyclerView.LayoutManager layoutManager;

    private boolean mIsSortSpinnerSelected = false;
    private List<SubmissionClass> mSortedSubmissions;
    private SubmissionsAdapter mSubSortAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_submissions, container, false);
        Intent i = getActivity().getIntent();
        mSlug = i.getStringExtra(BundleConstants.SLUG);

        //mContentLayout = view.findViewById(R.id.content_layout);
        mSp = MySharedPreferences.getInstance(getActivity());
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        mRecyclerView = view.findViewById(R.id.submissions_rv);

        mSortSpinner = view.findViewById(R.id.submissions_sort);


        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);


        mSubmissions = new ArrayList<>();
        mSubmissionsAdapter = new SubmissionsAdapter(getActivity(), mSubmissions);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSubmissionsAdapter);
        fetchSubmissions();

        mRecyclerView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (isLastItemDisplaying(recyclerView)) {

                            if (mIsSortSpinnerSelected) {
                                fetchSortedSubmissions();
                            } else {
                                fetchSubmissions();
                            }

                        }

                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mIsSortSpinnerSelected = true;
                mSortRequestCount = 1;

                mSortedSubmissions = new ArrayList<>();

                mSubSortAdapter = new SubmissionsAdapter(getActivity(), mSortedSubmissions);
                mRecyclerView.setAdapter(mSubSortAdapter);

                if (position == 0) {
                    mType = adapterView.getItemAtPosition(position).toString();
                    submissionsSortApi(mSortRequestCount);

                } else if (position == 1) {
                    mType = adapterView.getItemAtPosition(position).toString();
                    submissionsSortApi(mSortRequestCount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void submissionsSortApi(int requestCount) {
        Log.d(TAG, "submissionsSortApi: called");

        final CompetitionDetailActivity compDetailActivity = (CompetitionDetailActivity) getActivity();

        Log.d(TAG, "submissionsSortApi: competition id = " + compDetailActivity.mCompetitionId);
        Log.d(TAG, "submissionsSortApi: type = " + mType);

        final StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.SUBMISSIONS_SORT + "?page=" + requestCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "submission sort response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject compListObj = responseObject.getJSONObject("competitions_list");
                    JSONArray dataArray = compListObj.getJSONArray("data");

                    if (dataArray.length() != 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject singleObject = dataArray.getJSONObject(i);
                            JSONArray commentsArray = singleObject.getJSONArray("comments");

                            String id = singleObject.getString("id");
                            String title = singleObject.getString("title");
                            String code = singleObject.getString("code");
                            String coverUrl = singleObject.getString("cover");
                            String pdfUrl = singleObject.getString("pdf");

                            SubmissionClass submission = new SubmissionClass(id, title, code, coverUrl, pdfUrl, commentsArray);
                            mSortedSubmissions.add(submission);
                            mSubSortAdapter.notifyDataSetChanged();
                        }

                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                Log.d(TAG, "onErrorResponse: error network response = " + error.networkResponse);
//                ViewUtils.dismissProgressBar();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(getActivity().getString(R.string.accept), getActivity().getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", compDetailActivity.mCompetitionId);
                params.put("type", mType);

                return params;
            }
        };
        mRequestQueue.add(request);
    }

    private void fetchSortedSubmissions() {
        submissionsSortApi(mSortRequestCount);
        mSortRequestCount++;
    }

    public void fetchSubmissions() {
        competitionSubmissionsApi(mRequestCount);
        mRequestCount++;
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private void competitionSubmissionsApi(final int requestCount) {
//        ViewUtils.showProgressBar(getActivity());
        Log.d(TAG, "mSlug  = " + mSlug);
        Log.d(TAG, "url = " + ServerConstants.COMPETITION_DETAIL + mSlug + "/submissions" + "?page=" + requestCount);
        final StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.COMPETITION_DETAIL + mSlug + "/submissions" + "?page=" + requestCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                ViewUtils.dismissProgressBar();
                Log.d(TAG, "response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject itemsObject = responseObject.getJSONObject("items");
                    JSONArray dataArray = itemsObject.getJSONArray("data");
                    Log.d(TAG, "dataArray: "+ dataArray.length() + " == " + requestCount);

                    if (dataArray.length() != 0) {
//                        ViewUtils.dismissProgressBar();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject singleObject = dataArray.getJSONObject(i);
                            JSONArray commentsArray = singleObject.getJSONArray("comments");

                            String id = singleObject.getString("id");
                            String title = singleObject.getString("title");
                            String code = singleObject.getString("code");
                            String coverUrl = singleObject.getString("cover");
                            String pdfUrl = singleObject.getString("pdf");

                            SubmissionClass submission = new SubmissionClass(id, title, code, coverUrl, pdfUrl, commentsArray);
                            mSubmissions.add(submission);
                        }
                        mSubmissionsAdapter.notifyDataSetChanged();

                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ViewUtils.dismissProgressBar();
                Activity activity = getActivity();
                if(activity != null){
                    NetworkUtil.handleSimpleVolleyRequestError(error, activity);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(getActivity().getString(R.string.accept), getActivity().getString(R.string.application_json));
                params.put(getActivity().getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        mRequestQueue.add(request);
    }
}

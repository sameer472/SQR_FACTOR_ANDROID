package com.hackerkernel.user.sqrfactor.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Adapters.ResultsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.ResultClass;
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

public class ResultsFragment extends Fragment {
    private static final String TAG = "ResultsFragment";

    private RecyclerView mResultsRV;
    private List<ResultClass> mResults;
    private ResultsAdapter mResultsAdapter;

    ProgressBar mPb;
    MySharedPreferences mSp;
    RequestQueue mRequestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_results, container, false);

        mResultsRV = view.findViewById(R.id.results_rv);

        mPb = view.findViewById(R.id.pb);
        mSp = MySharedPreferences.getInstance(getActivity());
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        mResultsRV.setHasFixedSize(true);
        mResultsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mResults = new ArrayList<>();
        mResultsAdapter = new ResultsAdapter(getActivity(), mResults);
        mResultsRV.setAdapter(mResultsAdapter);

        Intent i = getActivity().getIntent();
        String slug = i.getStringExtra(BundleConstants.SLUG);

        competitionResultsApi(slug);

        return view;
    }

    private void competitionResultsApi(String slug) {
        mPb.setVisibility(View.VISIBLE);
        mResultsRV.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mResultsRV.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        Log.d(TAG, "competitionWallApi: mSlug  = " + slug);
        Log.d(TAG, "competitionDetailApi: url = " + ServerConstants.COMPETITION_DETAIL + slug + "/results");
        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.COMPETITION_DETAIL + slug + "/results", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: called");
                mPb.setVisibility(View.GONE);
                mResultsRV.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: competition results response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    if (!responseObject.isNull("competition")) {
                        JSONObject competitionObj = responseObject.getJSONObject("competition");
                        JSONArray awardArray = competitionObj.getJSONArray("users_competition_design_submition_award");
                        if (awardArray.length() > 0) {

                            for (int i = 0; i < awardArray.length(); i++) {
                                JSONObject singleObject = awardArray.getJSONObject(i);
                                String heading = singleObject.getString("title");

                                ResultClass headingResult = new ResultClass(Constants.TYPE_HEADING, null, null, null, null, 0, 0, null, heading);
                                mResults.add(headingResult);
                                mResultsAdapter.notifyDataSetChanged();

                                JSONArray awardItemArray = singleObject.getJSONArray("users_competition_design_submition_award_item");

                                for (int j = 0; j < awardItemArray.length(); j++) {
                                    JSONObject singleItemObject = awardItemArray.getJSONObject(j);
                                    JSONObject awardObject = singleItemObject.getJSONObject("users_competitions_award");
                                    JSONObject designObject = singleItemObject.getJSONObject("users_competitions_design");
                                    JSONArray likesArray = designObject.getJSONArray("likes");
                                    JSONArray commentsArray = designObject.getJSONArray("comments");

                                    String id = singleItemObject.getString("id");
                                    String prizeTitle = awardObject.getString("award_type");

                                    String title = designObject.getString("title");
                                    String code = designObject.getString("code");
                                    String coverUrl = designObject.getString("cover");
                                    int likesCount = likesArray.length();
                                    int commentsCount = commentsArray.length();

                                    ResultClass result = new ResultClass(Constants.TYPE_DATA, id, coverUrl, title, code, likesCount, commentsCount, prizeTitle, null);
                                    mResults.add(result);
                                    mResultsAdapter.notifyDataSetChanged();

                                }

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: called");
                mPb.setVisibility(View.GONE);
                mResultsRV.setVisibility(View.VISIBLE);
//                NetworkUtil.handleSimpleVolleyRequestError(error, getActivity());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

}

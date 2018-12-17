package com.hackerkernel.user.sqrfactor.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.hackerkernel.user.sqrfactor.Adapters.WallQuestionsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.WallQuestionClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.hackerkernel.user.sqrfactor.UtilsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallFragment extends Fragment {
    private static final String TAG = "WallFragment";

    private RecyclerView mWallRecyclerView;

    List<WallQuestionClass> mWallQuestions;
    WallQuestionsAdapter mWallQuestionsAdapter;

    ProgressBar mPb;
    LinearLayout mContentLayout;
    MySharedPreferences mSp;
    RequestQueue mRequestQueue;

    Button mPostQuestionButton;
    EditText mQuestionTitleEditText;
    EditText mQuestionDescEditText;
    ImageView mProfileImageView;

    String mCompetitionId;
    private String mSlug;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_wall, container, false);

        mPb = view.findViewById(R.id.pb);
        mContentLayout = view.findViewById(R.id.content_layout);
        mSp = MySharedPreferences.getInstance(getActivity());
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        mWallRecyclerView = view.findViewById(R.id.wall_rv);

        mWallRecyclerView.setHasFixedSize(true);
        mWallRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWallRecyclerView.setNestedScrollingEnabled(false);

        mWallQuestions = new ArrayList<>();
        mWallQuestionsAdapter = new WallQuestionsAdapter(getActivity(), mWallQuestions);
        mWallRecyclerView.setAdapter(mWallQuestionsAdapter);

        mPostQuestionButton = view.findViewById(R.id.wall_post_question);
        mQuestionTitleEditText = view.findViewById(R.id.wall_question_title);
        mQuestionDescEditText = view.findViewById(R.id.wall_question_description);
        mProfileImageView = view.findViewById(R.id.wall_profile);

        Log.d(TAG, "onCreateView: profile image path = " + ServerConstants.IMAGE_BASE_URL + mSp.getKey(SPConstants.PROFILE_URL));
       // Picasso.get().load(ServerConstants.IMAGE_BASE_URL + mSp.getKey(SPConstants.PROFILE_URL)).into(mProfileImageView);
        //Picasso.get().load("https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=1695593067204488&height=50&width=50&ext=1547099319&hash=AeSs1zwKtYnji21p").into(mProfileImageView);
        Glide.with(getContext()).load(UtilsClass.getParsedImageUrl(mSp.getKey(SPConstants.PROFILE_URL))).into(mProfileImageView);

        Intent i = getActivity().getIntent();
        mSlug = i.getStringExtra(BundleConstants.SLUG);

        competitionWallApi(mSlug);

        mPostQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompetitionId != null) {

                    if (mQuestionTitleEditText.getText().toString().equals("")) {
                        mQuestionTitleEditText.setError("Enter Title");
                    } else if (mQuestionDescEditText.getText().toString().equals("")) {
                        mQuestionDescEditText.setError("Enter Description");
                    } else if (mQuestionDescEditText.getText().toString().length() <= 10) {
                        mQuestionDescEditText.setError("The description must be at least 10 characters.");

                    } else {
                        wallQuestionAddApi();
                    }

                }
            }
        });

        return view;
    }

    private void wallQuestionAddApi() {
        Log.d(TAG, "wallQuestionAddApi: " + "OK");
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: competition wall response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String message = responseObject.getString("message");
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    competitionWallApi(mSlug);

                    mQuestionTitleEditText.setText("");
                    mQuestionDescEditText.setText("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, getActivity());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("subject", mQuestionTitleEditText.getText().toString());
                params.put("description", mQuestionDescEditText.getText().toString());
                params.put("users_competition_id", mCompetitionId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    private void competitionWallApi(String slug) {
        mWallQuestions.clear();
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        Log.d(TAG, "competitionWallApi: mSlug  = " + slug);
        Log.d(TAG, "competitionDetailApi: url = " + ServerConstants.COMPETITION_DETAIL + slug + "/wall");
        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.COMPETITION_DETAIL + slug + "/wall", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: competition wall response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject competitionObj = responseObject.getJSONObject("competition");

                    mCompetitionId = competitionObj.getString("id");

                    JSONArray questionsArray = competitionObj.getJSONArray("user_competition_wall_question");

                    if (questionsArray.length() > 0) {

                        for (int i = 0; i < questionsArray.length(); i++) {
                            JSONObject singleObject = questionsArray.getJSONObject(i);
                            JSONObject userObject = singleObject.getJSONObject("user");

                            String subject = singleObject.getString("subject");
                            String description = singleObject.getString("description");
                            String id = singleObject.getString("id");
                            String userId = singleObject.getString("user_id");
                            String announcedBy = userObject.getString("name");

                            if (announcedBy.equals("null")) {
                                announcedBy = userObject.getString("first_name") + " " + userObject.getString("last_name");
                            }

                            JSONArray commentsArray = singleObject.getJSONArray("comments");
                            Log.d(TAG, "onResponse: comments array = " + commentsArray);

                            WallQuestionClass wallQuestion = new WallQuestionClass(subject, description, announcedBy, id, userId, commentsArray);
                            mWallQuestions.add(wallQuestion);
                            mWallQuestionsAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Activity activity = getActivity();
                if(activity != null){
                    NetworkUtil.handleSimpleVolleyRequestError(error, activity);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getActivity().getString(R.string.accept), getActivity().getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//
//                params.put(getString(R.string.mobile), "8517841635");
//                params.put(getString(R.string.otp), "202317");
//
//                return params;
//            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

}

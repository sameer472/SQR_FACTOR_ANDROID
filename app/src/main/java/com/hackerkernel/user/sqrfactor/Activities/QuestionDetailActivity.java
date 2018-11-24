package com.hackerkernel.user.sqrfactor.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Adapters.QuestionCommentsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.QuestionCommentClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionDetailActivity extends AppCompatActivity {
    private static final String TAG = "QuestionDetailActivity";

    private WebView mWebView;
    private RecyclerView mRecyclerView;
    private TextView mCommentsCountTV;

    private Toolbar mToolbar;

    private LinearLayout mContentLayout;
    private ProgressBar mPb;
    private RequestQueue mRequestQueue;
    private MySharedPreferences mSp;

    private EditText mCommentEditText;
    private ImageView mProfileImageView;
    private TextView mPostCommentButton,AskedBy,Title;


    private String mUserId;


    List<QuestionCommentClass> mComments;
    QuestionCommentsAdapter mCommentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mUserId = getIntent().getStringExtra(BundleConstants.QUESTION_USER_ID);
        Log.d(TAG, "onCreate: user id = " + mUserId);

        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mSp = MySharedPreferences.getInstance(this);

        mCommentEditText = findViewById(R.id.question_detail_comment);
        mProfileImageView = findViewById(R.id.question_detail_profile);
        mPostCommentButton = findViewById(R.id.question_detail_post_comment);

        String questionId = getIntent().getStringExtra(BundleConstants.QUESTION_ID);

        AskedBy = findViewById(R.id.question_askedBy);
        Title = findViewById(R.id.question_announced_title);


        mWebView = findViewById(R.id.question_detail_desc);
        mRecyclerView = findViewById(R.id.question_detail_rv);
        mCommentsCountTV = findViewById(R.id.question_detail_comment_count);

        Log.d(TAG, "onCreateView: profile mChooseImageButton path = " + ServerConstants.IMAGE_BASE_URL + mSp.getKey(SPConstants.PROFILE_URL));
        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + mSp.getKey(SPConstants.PROFILE_URL)).into(mProfileImageView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mComments = new ArrayList<>();
        mCommentsAdapter = new QuestionCommentsAdapter(this, mComments, questionId);
        mRecyclerView.setAdapter(mCommentsAdapter);

        Intent i = getIntent();
        AskedBy.setText("Asked By "+ i.getStringExtra("Asked By"));


        try {
            JSONArray commentsArray = new JSONArray(i.getStringExtra(BundleConstants.COMMENTS_ARRAY));
            String description = i.getStringExtra(BundleConstants.QUESTION_DESCRIPTION);
            String title = i.getStringExtra(BundleConstants.QUESTION_TITLE);

            Log.d(TAG, "onCreate: comments array = " + i.getStringExtra(BundleConstants.COMMENTS_ARRAY));
            Log.d(TAG, "onCreate: question description = " + description);

            mWebView.loadData(description, "text/html", null);
            Title.setText(title);

            if (commentsArray.length() > 0) {
                mCommentsCountTV.setText(commentsArray.length() + " comments");

                for (int j = 0; j < commentsArray.length(); j++) {
                    JSONObject singleObject = commentsArray.getJSONObject(j);
                    JSONObject userObject = singleObject.getJSONObject("user");


                    String id = singleObject.getString("id");
                    String userId = singleObject.getString("user_id");
                    String commentDescription = singleObject.getString("comment");
                    String askedBy=null;
                    if(userObject.getString("first_name").equals("null") && userObject.getString("last_name").equals("null"))
                    {
                        askedBy  = userObject.getString("name");

                    }
                    else {
                        askedBy = userObject.getString("first_name") + " " + userObject.getString("last_name");
                    }

                    String imageUrl = userObject.getString("profile");

                    QuestionCommentClass comment = new QuestionCommentClass(id, userId, commentDescription, askedBy, "", imageUrl);
                    mComments.add(comment);
                    mCommentsAdapter.notifyDataSetChanged();

                }

            } else {
                mCommentsCountTV.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mPostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String questionId = getIntent().getStringExtra(BundleConstants.QUESTION_ID);
                String comment = mCommentEditText.getText().toString();

                wallQuestionCommentAddApi(questionId, comment);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mUserId != null) {
            String currentUserId = mSp.getKey(SPConstants.USER_ID);

            if (mUserId.equals(currentUserId)) {
                getMenuInflater().inflate(R.menu.question_menu, menu);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.question_menu_edit: {
                showQuestionEditDialog();
                break;
            }

            case R.id.question_menu_delete: {
                showQuestionDeleteDialog();
                break;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void wallQuestionCommentAddApi(final String id, final String comment) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_COMMENT_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: comment add response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String message = responseObject.getString("message");
//                    Toast.makeText(QuestionDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                    JSONObject dataObject = responseObject.getJSONObject("data");
                    JSONObject userObject = dataObject.getJSONObject("user");

                    String id = dataObject.getString("id");
                    String userId = dataObject.getString("user_id");
                    String desc = dataObject.getString("comment");
                    String askedBy = userObject.getString("first_name") + " " + userObject.getString("last_name");
                    String imageUrl = userObject.getString("profile");

                    QuestionCommentClass commentObj = new QuestionCommentClass(id, userId, desc, askedBy, "", imageUrl);
                    mComments.add(0, commentObj);
                    mCommentsAdapter.notifyDataSetChanged();

                    mCommentEditText.setText("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, QuestionDetailActivity.this);
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

                params.put("users_competition_wall_question_id", id);
                params.put("comment", comment);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    private void showQuestionDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this question?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String questionId = getIntent().getStringExtra(BundleConstants.QUESTION_ID);
                        wallQuestionDeleteApi(questionId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void wallQuestionDeleteApi(final String id) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: question update response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String message = responseObject.getString("message");
//                    Toast.makeText(QuestionDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(QuestionDetailActivity.this, CompetitionsActivity.class));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, QuestionDetailActivity.this);
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

                params.put("question_id", id);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    private void showQuestionEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_question_dialog,null);

        Intent i = getIntent();
        final String questionId = i.getStringExtra(BundleConstants.QUESTION_ID);
        String questionTitle = i.getStringExtra(BundleConstants.QUESTION_TITLE);
        String questionDesc = i.getStringExtra(BundleConstants.QUESTION_DESCRIPTION);

        final EditText questionTitleEditText = view.findViewById(R.id.question_title);
        final EditText questionDescEditText = view.findViewById(R.id.question_description);

        questionTitleEditText.setText(questionTitle);
        questionTitleEditText.setSelection(questionTitleEditText.getText().length());


        questionDescEditText.setText(questionDesc);
        questionDescEditText.setSelection(questionDescEditText.getText().length());

        builder.setTitle("Edit your question").setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String questionTitle = questionTitleEditText.getText().toString();
                        String questionDesc = questionDescEditText.getText().toString();

                        if (questionTitle.length() < 1) {
//                            Toast.makeText(QuestionDetailActivity.this, "The subject must not be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (questionDesc.length() < 1){
//                            Toast.makeText(QuestionDetailActivity.this, "The description must not be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        wallQuestionUpdateApi(questionId, questionTitle, questionDesc);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void wallQuestionUpdateApi(final String id, final String title, final String description) {

        Log.d(TAG, "wallQuestionAddApi: " + "OK");
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: question update response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject respObject = responseObject.getJSONObject("Response");

                    String message = respObject.getString("message");
//                    Toast.makeText(QuestionDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                    mWebView.loadData(description, "text/html", null);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, QuestionDetailActivity.this);
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

                params.put("subject", title);
                params.put("description", description);
                params.put("question_id", id);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }
}

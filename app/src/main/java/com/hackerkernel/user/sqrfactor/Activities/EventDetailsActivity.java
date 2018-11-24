package com.hackerkernel.user.sqrfactor.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EventDetailsActivity";

    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FloatingActionButton mFab;
    private TextView mDescTV, mVenue, mOrganizer, mType;
    private ImageView mCoverIV;
    private Button mRegisterButton;

    private ProgressBar mPb;
    private CoordinatorLayout mContentLayout;
    private RequestQueue mRequestQueue;
    private MySharedPreferences mSp;

    private String eventId;
    private String userId;

    private String mPrice;

    List<String> usersListStr;
    ArrayAdapter arrayAdapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        final Intent i = getIntent();
        userId = i.getStringExtra("userId");
        String slug = i.getStringExtra(BundleConstants.SLUG);

        mToolbar = findViewById(R.id.new_toolbar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        mFab = findViewById(R.id.fab);

        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mSp = MySharedPreferences.getInstance(this);

        Log.d(TAG, "onCreate: api key with header = " + Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        mDescTV = findViewById(R.id.event_detail_desc);
        mVenue = findViewById(R.id.venueText);
        mOrganizer = findViewById(R.id.organizerText);
        mType = findViewById(R.id.typeText);
        mCoverIV = findViewById(R.id.event_detail_cover);
        mRegisterButton = findViewById(R.id.event_detail_reg);

        eventDetailApi(slug);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final String link = "https://archsqr.in/event/" + slug + "/detail";


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(link);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: called");

                if (mRegisterButton.getText().equals("Register for Event")) {
                    if(mType.getText().toString().equals("free")){
                        Log.d(TAG, "===============free event selected/registered============");
                        mRequestQueue = MyVolley.getInstance().getRequestQueue();
                        showData();

                    }
                    else if(mType.getText().toString().equals("paid")){
                        if (mPrice.equals("null")) {
                            Toast.makeText(EventDetailsActivity.this, "Price is null", Toast.LENGTH_SHORT).show();

                        } else {
                            mPb.setVisibility(View.VISIBLE);
                            mContentLayout.setVisibility(View.GONE);
                            mRegisterButton.setVisibility(View.GONE);

                            Log.d(TAG, "==============paid event selected this time==============");
                            mRequestQueue = MyVolley.getInstance().getRequestQueue();
                            StringRequest registerRequest = new StringRequest(Request.Method.POST, ServerConstants.EVENT_REGISTER + eventId
                                    , new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    mPb.setVisibility(View.GONE);
                                    mContentLayout.setVisibility(View.VISIBLE);
                                    mRegisterButton.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "CLICK RESPONSE===================== " + response);

                                    try {
                                        JSONObject responseObject = new JSONObject(response);
                                        String responseMessage = responseObject.getString("Response Message");
                                        Toast.makeText(EventDetailsActivity.this, "" + responseMessage, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(EventDetailsActivity.this, EventsActivity.class));
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "onErrorResponse: called");
                                    mPb.setVisibility(View.GONE);
                                    mContentLayout.setVisibility(View.VISIBLE);
                                    mRegisterButton.setVisibility(View.VISIBLE);

                                    int statusCode = error.networkResponse.statusCode;

                                    try {
                                        String body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d("handleSimpleVolley: ", body);
                                        if (statusCode == 400 || statusCode == 401) {
                                            //server error
//                                        String errorMsg = JsonParser.SimpleParser(body);
//                                        Toast.makeText(EventDetailsActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                                            JSONObject object = new JSONObject(body);
                                            String responseMsg = object.getString("Response Message");
                                            Toast.makeText(EventDetailsActivity.this, responseMsg, Toast.LENGTH_SHORT).show();

                                        } else if (statusCode == 422) {
                                            JSONObject object = new JSONObject(body);
                                            Log.d(TAG, "onErrorResponse: "+body);
                                            JSONObject responseObj = object.getJSONObject("Response");
                                            String message = responseObj.getString("message");
                                            Toast.makeText(EventDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            String errorString = MyVolley.handleVolleyError(error);
                                            Toast.makeText(EventDetailsActivity.this, errorString, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                        NetworkUtil.showParsingErrorAlert(EventDetailsActivity.this);
                                    }
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
                            registerRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            mRequestQueue.add(registerRequest);
                        }

                    }
                }
                else if (mRegisterButton.getText().equals("View Registered User")) {

                    mRequestQueue = MyVolley.getInstance().getRequestQueue();
                    StringRequest viewRequest = new StringRequest(Request.Method.POST, ServerConstants.EVENT_REGISTERED_USER
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "MS_SHA: "+response);
                            mPb.setVisibility(View.GONE);
                            mContentLayout.setVisibility(View.VISIBLE);
                            mRegisterButton.setVisibility(View.VISIBLE);

                            try {
                                JSONObject responseObject = new JSONObject(response);
                                JSONArray responseArray = responseObject.getJSONArray("eventUserWithAuth");

                                usersListStr = new ArrayList<>();
                                for(int i = 0; i<responseArray.length(); i++){
                                    JSONObject singleUser = responseArray.getJSONObject(i);
                                    JSONObject userUser = singleUser.getJSONObject("user");
                                    String name = userUser.getString("name");
                                    String first = userUser.getString("first_name");
                                    String last = userUser.getString("last_name");
                                    Log.d(TAG, "onResponse: ==================" + first+last+name);
                                       if(first.isEmpty() && last.isEmpty())
                                        usersListStr.add(name);
                                    else
                                        usersListStr.add(first+" "+last);
                                }

                                for (int i = 0;i<usersListStr.size();i++){
                                    Log.d(TAG, "onResponse: "+usersListStr.get(i));
                                }
                                if(userId.equals(mSp.getKey(SPConstants.USER_ID))){
                                    showDialog();
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
                            mRegisterButton.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onErrorResponse: ====================="+error);
                            if (!NetworkUtil.isNetworkAvailable()) {
                                Toast.makeText(EventDetailsActivity.this, "No internet", Toast.LENGTH_SHORT);
                                return;
                            }
                            else
                            {
                                if(error.networkResponse!=null){
                                    int statusCode = error.networkResponse.statusCode;
                                    String body = null;
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d(TAG, "onErrorResponse: body = " + body);

                                        if (statusCode == 400) {
                                            try {
                                                JSONObject object = new JSONObject(body);
                                                String message = object.getString("message");
                                                Toast.makeText(EventDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

//                                                JSONObject responseObject = new JSONObject(String.valueOf(error));
//                                                String err = responseObject.getString("message");
//                                                Toast.makeText(EventDetailsActivity.this, err, Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else
                                    Toast.makeText(EventDetailsActivity.this, "Request Timed Out !", Toast.LENGTH_SHORT).show();
                            }
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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("eventId", eventId);
                            return params;
                        }
                    };
                    viewRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mRequestQueue.add(viewRequest);
                }
            }
        });

    }

    private void showData() {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);
        Log.d(TAG, "showDataAA: "+"OK");
        Log.d(TAG, "showData: "+ServerConstants.EVENT_REGISTER + eventId);
        StringRequest registerRequest = new StringRequest(Request.Method.POST, ServerConstants.EVENT_REGISTER + eventId
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: called");

                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "CLICK RESPONSE===================== " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    String responseMessage = responseObject.getString("Response Message");
                    Log.d(TAG, "onResponse: response message = " + responseMessage);
                    Toast.makeText(EventDetailsActivity.this, "" + responseMessage, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EventDetailsActivity.this, EventsActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+"ERROR");
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);
                //ViewUtils.dismissProgressBar();

                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;

                    try {
                        String body = new String(error.networkResponse.data, "UTF-8");
                        Log.d("handleSimpleVolley: ", body);
                        if (statusCode == 400 || statusCode == 401) {
                            //server error
                            JSONObject object = new JSONObject(body);
                            String responseMsg = object.getString("Response Message");
                            Toast.makeText(EventDetailsActivity.this, responseMsg, Toast.LENGTH_SHORT).show();

                        } else if (statusCode == 422) {
                            JSONObject object = new JSONObject(body);
                            Log.d(TAG, "onErrorResponse: "+body);
                            JSONObject responseObj = object.getJSONObject("Response");
                            String message = responseObj.getString("message");
                            Toast.makeText(EventDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorString = MyVolley.handleVolleyError(error);
                            Toast.makeText(EventDetailsActivity.this, errorString, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        NetworkUtil.showParsingErrorAlert(EventDetailsActivity.this);
                    }
                } else {
                    String errorString = MyVolley.handleVolleyError(error);
                    Toast.makeText(EventDetailsActivity.this, errorString, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

        };
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(registerRequest);
    }

    private void share(String link) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, link);

        EventDetailsActivity.this.startActivity(Intent.createChooser(share, "Share"));

    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);
        View v = EventDetailsActivity.this.getLayoutInflater().inflate(R.layout.user_list, null, false);
        builder.setView(v);
        final TextView cancel = v.findViewById(R.id.cancelTextView);
        final ListView user_list = v.findViewById(R.id.user_list);
        arrayAdapter = new ArrayAdapter(EventDetailsActivity.this,android.R.layout.simple_list_item_1,usersListStr);
        user_list.setAdapter(arrayAdapter);
        dialog = builder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void eventDetailApi(String slug) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);
        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mRegisterButton.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        Log.d(TAG, "eventDetailApi: url = " + ServerConstants.EVENT_DETAIL + slug + "/detail");
        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.EVENT_DETAIL + slug + "/detail", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: event detail response = " + response);
                try {
                    JSONArray responseArray = new JSONArray(response);
                    JSONObject responseObject = responseArray.getJSONObject(0);
                    JSONObject eventObject = responseObject.getJSONObject("event");

                    eventId = eventObject.getString("id");
                    String coverUrl = eventObject.getString("cover_image");
                    String title = eventObject.getString("event_title");
                    String desc = eventObject.getString("description");
                    String venue = eventObject.getString("venue");
                    Log.d(TAG, "onResponse: sdfgd=========  " + venue);
                    String organizer = eventObject.getString("event_organizer");
                    Log.d(TAG, "onResponse: sdfgd=========  " + organizer);
                    String type = eventObject.getString("event_type");
                    Log.d(TAG, "onResponse: sdfgd=========  " + type);

                    mPrice = eventObject.getString("price");

                    if(userId.equals(mSp.getKey(SPConstants.USER_ID))){
                        mRegisterButton.setText("View Registered User");
                    }
                    else
                        mRegisterButton.setText("Register for Event");

                    mVenue.setText(venue);
                    mOrganizer.setText(organizer);
                    mType.setText(type);

                    mToolbar.setTitle(title);
                    collapsingToolbarLayout.setTitle(title);
                    mDescTV.setText(desc);

                    Picasso.get().load(ServerConstants.IMAGE_BASE_URL + coverUrl).into(mCoverIV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, EventDetailsActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ================"+userId);
        if (userId.equals(mSp.getKey(SPConstants.USER_ID))){
        getMenuInflater().inflate(R.menu.menu_scrolling,menu);
        return super.onCreateOptionsMenu(menu);
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);

            builder.setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            eventsDelete(eventId);
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
        return super.onOptionsItemSelected(item);
    }

    private void eventsDelete(final String eventId) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mRegisterButton.setVisibility(View.VISIBLE);
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.EVENT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: event delete response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String responseStr = responseObject.getString("Response");
                    Toast.makeText(EventDetailsActivity.this, responseStr, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EventDetailsActivity.this,EventsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, EventDetailsActivity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(EventDetailsActivity.this.getString(R.string.accept), EventDetailsActivity.this.getString(R.string.application_json));
                headers.put(EventDetailsActivity.this.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", eventId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }
}

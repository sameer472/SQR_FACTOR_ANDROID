package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.UtilsClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmationActivity";
    private Button done;
    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private String mCompetitionId;
    private TextView textViewId ,textViewStatus,textViewAmount,textViewTime;
    private JSONObject jsonDetails;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mSp = MySharedPreferences.getInstance(this);

        done = findViewById(R.id.success_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(ConfirmationActivity.this,CompetitionDetailActivity.class);
                intent.putExtra(BundleConstants.SLUG, UtilsClass.slug);
                startActivity(intent);
                finish();
            }
        });
        //Getting Intent
        Intent intent = getIntent();
        mCompetitionId = intent.getStringExtra(BundleConstants.COMPETITION_ID);

        try {
             jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        textViewId = (TextView) findViewById(R.id.paymentId);
        textViewStatus= (TextView) findViewById(R.id.paymentStatus);
         textViewAmount = (TextView) findViewById(R.id.paymentAmount);
        textViewTime = (TextView)findViewById(R.id.paymentTime);

//
        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" USD");
        textViewTime.setText(jsonDetails.getString("create_time"));
        sendPayPalData();
    }

public void sendPayPalData(){
    mRequestQueue = MyVolley.getInstance().getRequestQueue();

    StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.PAY_PAL + mCompetitionId, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

//            Toast.makeText(ConfirmationActivity.this, response, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onResponse: paypal confirm response = " + response);

            try {
                Toast.makeText(ConfirmationActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                JSONObject responseObject = new JSONObject(response);


            } catch (Error e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // NetworkUtil.handleSimpleVolleyRequestError(error, ConfirmationActivity.this);
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {


                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    Log.v("paypal",res);
//                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                    // Now you can use any deserializer to make sense of data
                    JSONObject obj = new JSONObject(res);
                } catch (UnsupportedEncodingException e1) {
//                                Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } catch (JSONException e2) {
//                                Toast.makeText(getApplicationContext(),e2.toString(),Toast.LENGTH_LONG).show();
                    // returned data is not JSONObject?
                    e2.printStackTrace();
                }
            }
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

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();


            try {
                params.put("Pay_id",textViewId.getText().toString());
                params.put("Competition_id",mCompetitionId +"");
                params.put("Intent", jsonDetails.getString("intent"));
                params.put("Payment_Method", "paypal");
                params.put("Status","approved");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return params;
        }

    };
    request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    mRequestQueue.add(request);
}
}
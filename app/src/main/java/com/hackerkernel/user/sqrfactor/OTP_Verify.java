package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.goodiebag.pinview.Pinview;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OTP_Verify extends AppCompatActivity {
    private Pinview pinview1;
    private Button validate,skip;
    private String otp;
    private TextView resendOTP,textMobile;
    private UserClass userClass;
    private String mobile;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__verifiy);


        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "sqr");
        TokenClass.Token = token;
        TokenClass tokenClass = new TokenClass(token);
        Log.v("Token1", token);


       textMobile=(TextView)findViewById(R.id.textmobile);

        if(getIntent()!=null)
        {
            mobile=getIntent().getStringExtra("mobile");
            textMobile.setText("Please type the verification code sent to "+mobile);

        }
        pinview1 = (Pinview) findViewById(R.id.pinview1);
        resendOTP = (TextView)findViewById(R.id.resend_otp);
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOTP();
            }
        });
        validate = (Button)findViewById(R.id.validate_button);
        skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTP_Verify.this,HomeScreen.class);
                startActivity(intent);
            }
        });
        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
//                Toast.makeText(OTP_Verify.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                otp = pinview.getValue();
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"verify_otp",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("Reponse", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer " + TokenClass.Token);
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("otp",otp);
                        return params;
                    }


                };

                requestQueue.add(myReq);
            }
        });


    }

   public void ResendOTP(){
       RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
       StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"resendotp",
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.v("Reponse", response);
                       try {
                           JSONObject jsonObject = new JSONObject(response);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {

                   }
               }) {

           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               params.put("Accept", "application/json");
               params.put("Authorization", "Bearer " + TokenClass.Token);
               return params;
           }

           @Override
           protected Map<String, String> getParams() {
               Map<String, String> params = new HashMap<String, String>();
               return params;
           }


       };

       requestQueue.add(myReq);
   }



}
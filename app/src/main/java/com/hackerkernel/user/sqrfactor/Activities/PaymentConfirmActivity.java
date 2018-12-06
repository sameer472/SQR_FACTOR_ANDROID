package com.hackerkernel.user.sqrfactor.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.JsonArray;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.MDToast;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.PayPalConfig;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaymentConfirmActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "PaymentConfirmActivity";

    private Toolbar mToolbar;

    private TextView mCompTitleTV;
    private TextView mAmountTextView,mAmountTextView1;
    private TextView mOrganizerTV;
    private TextView mParticipantsTV;
    private TextView mentorText,mMentorTV;
    private ImageView Organizer_image;

    private Button mProceedButton;

    private ProgressBar mPb;
    private LinearLayout mContentLayout;
    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private String mCompetitionId;
    private String mUserName;
    private String mUserEmail;
    private String mUserPhone;

    private Button editTeam;
    private RadioGroup payment_method;
    private RadioButton payPal,payUmoney;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private String amount,transactionId,orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);

        Checkout.preload(getApplicationContext());

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        mToolbar.setNavigationIcon(R.drawable.back_arrow);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        mSp = MySharedPreferences.getInstance(this);
        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        mCompTitleTV = findViewById(R.id.pay_confirm_comp_title);
        mAmountTextView = findViewById(R.id.pay_confirm_amount);
        mAmountTextView1 = findViewById(R.id.pay_confirm_amount1);
        mOrganizerTV = findViewById(R.id.pay_confirm_organizer);
        Organizer_image = findViewById(R.id.organizer_image);
        mParticipantsTV = findViewById(R.id.pay_confirm_participants);
        mMentorTV = findViewById(R.id.pay_confirm_mentor);
        mentorText = findViewById(R.id.mentor_text);

        payment_method = findViewById(R.id.payment_method);
        payPal = findViewById(R.id.pay_pal);
        payUmoney = findViewById(R.id.payUmoney);

        editTeam = findViewById(R.id.edit_team);

        mProceedButton = findViewById(R.id.pay_confirm_proceed);

        mCompetitionId = getIntent().getStringExtra(BundleConstants.COMPETITION_ID);

        editTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentConfirmActivity.this,ParticipateActivity.class);
                intent.putExtra(BundleConstants.COMPETITION_ID,mCompetitionId);
                intent.putExtra(BundleConstants.COMPETITION_EDIT_BTN,"edit");
                startActivity(intent);
            }
        });


        paymentConfirmApi();

        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: called");

                if(payPal.isChecked()){
                    getPayment();
                    Intent intent = new Intent(PaymentConfirmActivity.this, PayPalService.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    startService(intent);

                }else if(payUmoney.isChecked()) {
                    amount = mAmountTextView.getText().toString();

                    if (amount.isEmpty()) {
                        Toast.makeText(PaymentConfirmActivity.this, "Amount not found.", Toast.LENGTH_SHORT).show();

                    } else {
                        Map<String, String> params = new HashMap<>();
                        startPayment();
//                        transactionId = "SQRFAC" + com.hackerkernel.user.sqrfactor.Utils.Utils.generateRandomStr(8);
//
//                        orderId = "SQRFAC" + com.hackerkernel.user.sqrfactor.Utils.Utils.generateRandomStr(8);
//                        params.put(PayUMoney_Constants.KEY, "xrrZdj");
////        params.put(PayUMoney_Constants.KEY,"lESQjlQk"); //dev
//                        params.put(PayUMoney_Constants.UDF1,orderId);
//                        params.put(PayUMoney_Constants.TXN_ID, transactionId);
//                        params.put(PayUMoney_Constants.AMOUNT, amount);
//                        params.put(PayUMoney_Constants.PRODUCT_INFO, "Competition Participation");
//                        params.put(PayUMoney_Constants.FIRST_NAME, mUserName);
//                        params.put(PayUMoney_Constants.EMAIL, mUserEmail);
//                        params.put(PayUMoney_Constants.PHONE, mUserPhone);
//                        params.put(PayUMoney_Constants.SURL, "https://www.payumoney.com/mobileapp/payumoney/success.php");
//                        params.put(PayUMoney_Constants.FURL, "https://www.payumoney.com/mobileapp/payumoney/failure.php");
//
//                        String hash = Utils.generateHash((HashMap<String, String>) params, "FlVk5hXQ");
//
//                        params.put(PayUMoney_Constants.HASH, hash);
//                        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");
//
//                        Intent intent = new Intent(PaymentConfirmActivity.this, MakePaymentActivity.class);
//                        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_PRODUCTION);
//                        intent.putExtra(PayUMoney_Constants.PARAMS, (HashMap) params);
//
//                        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
                    }


                }
                else {
                    MDToast.makeText(PaymentConfirmActivity.this, "Select Payment Method", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "SqrFactor India Pvt. Ltd.");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.logofinal);
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(amount)*100);
//            options.put("amount", Integer.parseInt("1")*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mUserEmail);
            preFill.put("contact", mUserPhone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    private void getPayment() {
        //Getting the amount from editText
        String amount = mAmountTextView1.getText().toString();

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(mAmountTextView1.getText().toString())), "USD", "Competition Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    private void paymentConfirmApi() {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.PAYMENT_CONFIRM + mCompetitionId, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {

                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: payment confirm response = " + response);

                try {
//                    Toast.makeText(PaymentConfirmActivity.this, "Participation Successful", Toast.LENGTH_SHORT).show();
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject compIdObject = responseObject.getJSONObject("CompetitionID");
                    JSONObject userObject = compIdObject.getJSONObject("user");

                    mUserName = userObject.getString("name");

                    if (mUserName.equals("null")) {
                        mUserName = userObject.getString("first_name") + " " + userObject.getString("last_name");
                    }
                    mUserEmail = userObject.getString("email");
                    mUserPhone = userObject.getString("mobile_number");

                    String compTitle = compIdObject.getString("competition_title");
                    mCompTitleTV.setText(compTitle);

                    String organizer = responseObject.getString("competitionOrganizer");
                    mOrganizerTV.setText(organizer);

                    String orgnizerImage = responseObject.getString("competitionOrganizerImage");
                    Picasso.get().load(ServerConstants.IMAGE_BASE_URL + orgnizerImage).into(Organizer_image);

                    JSONArray mentorArray = responseObject.getJSONArray("mentor");
                    String mentor = mentorArray.getString(0);



                    if (!mentor.equals("null"))
                    {
                        mentorText.setVisibility(View.VISIBLE);
                        mMentorTV.setText(mentor);

                    }
                    else {
                        mentorText.setVisibility(View.GONE);
                        mMentorTV.setText("");
                    }

                    JSONArray amountArray = responseObject.getJSONArray("amount");
                    Log.d(TAG, "onResponse: amount array = " + amountArray.toString());
                    JSONArray amountarray1 = amountArray.getJSONArray(1);
                    if (amountarray1.length() > 0) {
                        JSONObject INRObj = amountarray1.getJSONObject(0);
                        JSONObject USDObj = amountarray1.getJSONObject(1);

                        String amount = INRObj.getString("amount");
                        String amountUSD = USDObj.getString("amount");

                        //amount=amount;
                        mAmountTextView.setText(amount);
                        mAmountTextView1.setText(amountUSD);

                    }

                    JSONArray participantsArray = responseObject.getJSONArray("users");

                    for (int i = 0; i < participantsArray.length(); i++) {
                        String participant = participantsArray.getString(i);

                        if (!participant.equals("null")) {
                            mParticipantsTV.append(participant + "\n");
                        }

                    }

                } catch (Error e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, PaymentConfirmActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == RESULT_OK && requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
//            Toast.makeText(this, "Payment Success,", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "payuResult" + data.toString());
//
//            // Log.d(TAG, "onActivityResult: payu response = " + data.getStringExtra("payu_response"));
//            String result = data.getStringExtra("result");
//            Log.d(TAG, "onActivityResult: payu result = " + data.getStringExtra("result")+" "+data.getStringExtra("paymentId") );
//
//            long orderId = Long.parseLong(result);
////            sendPayUmoneylData();
//
//
//        }
//        else if (resultCode == RESULT_CANCELED) {
//            Log.i(TAG, "failure");
//            Toast.makeText(this, "Payment Failed | Cancelled.", Toast.LENGTH_SHORT).show();
//        }

        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        String amount = mAmountTextView1.getText().toString();
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount",amount )
                                .putExtra(BundleConstants.COMPETITION_ID,mCompetitionId));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    public void sendPayUmoneylData(final String transactionId){
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.PAY_UMONEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(PaymentConfirmActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: payUmoney confirm response = " + response);

                try {
//                    Toast.makeText(PaymentConfirmActivity.this, "Participation Successful", Toast.LENGTH_SHORT).show();
                    JSONObject responseObject = new JSONObject(response);
                    Intent intent= new Intent(PaymentConfirmActivity.this,CompetitionDetailActivity.class);
                    startActivity(intent);
                    finish();

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
                        Log.v("payUmoney",res);
//                        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
//                        Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
//                        Toast.makeText(getApplicationContext(),e2.toString(),Toast.LENGTH_LONG).show();
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
                orderId = "SQRFAC" + com.hackerkernel.user.sqrfactor.Utils.Utils.generateRandomStr(8);
                params.put("txnid", transactionId +"");
                params.put("udf1", orderId +"");
                params.put("udf3", mCompetitionId +"");
                params.put("udf2",  "Competition");
                params.put("firstname", mUserName +"");
                params.put("email", mUserEmail +"");
                params.put("phone", mUserPhone +"");
                params.put("amount", amount +"");
                params.put("status", "success");

                return params;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sendPayUmoneylData(razorpayPaymentID);
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);

        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}

package com.hackerkernel.user.sqrfactor.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.hackerkernel.user.sqrfactor.Constants.ServerConstants.EVENT_2_ADD;

public class PostEvent2Activity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private Spinner mEventTypeSpinner;
    String eventType;

    private EditText mOrganizerET;

    private LinearLayout mPaidLayout; //paidLayout
    private RadioGroup mReg_from; //reg_from
    private RadioButton mRadio_reg1; //radio_reg1
    private TextView mConditionStatement; //condition_tv
    private RadioButton mRadio_reg2; //radio_reg2

    private LinearLayout mEnter_url_layout; //enter_url_layout
    private EditText mEnter_url; //enter_url
    private EditText mReg_early_start_date;//reg_early_start_date
    private EditText mReg_early_end_date;//reg_early_end_date
    private EditText mEarly_reg_type;//early_reg_type
    private EditText mEarly_amount;//early_amount
    private Spinner mEarly_currency_unit;//early_currency_unit
    private TextView mEarly_add_more;//early_add_more
    private LinearLayout mEarly_add_layout;//early_add_layout
    private EditText mAdv_reg_start_date;//reg_adv_start_date
    private EditText mAdv_reg_end_date;//reg_adv_end_date
    private EditText mAdv_reg_type;//adv_reg_type
    private EditText mAdv_amount;//adv_amount
    private Spinner mAdv_currency_unit;//adv_currency_unit
    private TextView mAdv_add_more;//adv_add_more
    private LinearLayout mAdv_add_layout;//adv_add_layout
    private EditText mLast_reg_start_date;//reg_late_start_date
    private EditText mLast_reg_end_date;//reg_late_end_date
    private EditText mLast_reg_type;//late_reg_type
    private EditText mLast_amount;//late_amount
    private Spinner mLast_currency_unit;//late_currency_unit
    private TextView mLast_add_more;//late_add_more
    private LinearLayout mLast_add_layout;//late_add_layout


    private EditText mStartDateET;
    private EditText mStartTimeET;
    private EditText mEndDateET;
    private EditText mEndTimeET;

    private String mNewSlug;
    private MySharedPreferences mSp;

    private Button mPostEventButton;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_event2);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        mPaidLayout = findViewById(R.id.paid_layout);

        mEventTypeSpinner = findViewById(R.id.event_add2_type);

        mEventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mPaidLayout.setVisibility(View.GONE);
                        Toast.makeText(PostEvent2Activity.this, "Please select event type first", Toast.LENGTH_SHORT).show();
                    case 1:
                        onFreeSelected();
                        eventType = "free";
                        break;

                    case 2:
                        onPaidSelected();
                        eventType = "paid";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mStartDateET = findViewById(R.id.event_add2_start_date);
        mStartTimeET = findViewById(R.id.event_add2_start_time);
        mEndDateET = findViewById(R.id.event_add2_end_date);
        mEndTimeET = findViewById(R.id.event_add2_end_time);

        mOrganizerET = findViewById(R.id.event_add2_organizer);

        String sl = getIntent().getStringExtra("slug");
//        Toast.makeText(PostEvent2Activity.this, "slug is"+sl, Toast.LENGTH_SHORT).show();
        mSp = MySharedPreferences.getInstance(this);

        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mNewSlug = EVENT_2_ADD + sl.toString();

        mPostEventButton = findViewById(R.id.event_add2_post_event);

        mStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostEvent2Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mStartDateET.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        mStartTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(PostEvent2Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                mStartTimeET.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        mEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostEvent2Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mEndDateET.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        mEndTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(PostEvent2Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                mEndTimeET.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }

    private void onFreeSelected(){

        mPaidLayout.setVisibility(View.GONE);

        mPostEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mStartDateET.getText().toString().isEmpty() &&
                        !mEndDateET.getText().toString().isEmpty() &&
                        !mStartTimeET.getText().toString().isEmpty() &&
                        !mEndTimeET.getText().toString().isEmpty() &&
                        !mOrganizerET.getText().toString().isEmpty()){
                    if (!NetworkUtil.isNetworkAvailable()) {
                        Toast.makeText(PostEvent2Activity.this, "No internet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mRequestQueue = MyVolley.getInstance().getRequestQueue();

                    final StringRequest requestPostEvent = new StringRequest(Request.Method.POST,
                            mNewSlug.replace("\"", ""), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(PostEvent2Activity.this, "Event posted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostEvent2Activity.this, EventsActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkUtil.handleSimpleVolleyRequestError(error, PostEvent2Activity.this);
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() {
                            final Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("datetimepicker_start_date",mStartDateET.getText().toString() );
                            params.put("datetimepicker_end_date",mEndDateET.getText().toString());
                            params.put("start_time",mStartTimeET.getText().toString());
                            params.put("end_time",mEndTimeET.getText().toString());
                            params.put("event_organizer",mOrganizerET.getText().toString());
                            params.put("event_type_name",eventType);

                            return params;
                        }
                    };
                    requestPostEvent.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mRequestQueue.add(requestPostEvent);
                }else{
                    Toast.makeText(getApplicationContext(),"Form Incomplete",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void onPaidSelected(){

        mPaidLayout.setVisibility(View.VISIBLE);

        final ArrayList<LinearLayout> mEarlyAddedMoreList;
        mEarly_add_layout = findViewById(R.id.early_add_layout);
        final ArrayList<LinearLayout> mAdvAddedMoreList;
        mAdv_add_layout = findViewById(R.id.adv_add_layout);
        final ArrayList<LinearLayout> mLastAddedMoreList;
        mLast_add_layout = findViewById(R.id.last_add_layout);


        mReg_from = findViewById(R.id.reg_from);
        mRadio_reg1 = findViewById(R.id.radio_reg1);
        mConditionStatement = findViewById(R.id.condition_tv);
        mRadio_reg2 = findViewById(R.id.radio_reg2);


        final String[] value = new String[1];

        mEnter_url_layout = findViewById(R.id.enter_url_layout);
        mEnter_url = findViewById(R.id.enter_url);


        //listening from the radio button
        mReg_from.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_reg1 :
                        mEnter_url_layout.setVisibility(View.GONE);
                        mConditionStatement.setVisibility(View.VISIBLE);
                        value[0] = "sqr";
                        break;
                    case R.id.radio_reg2 :
                        mConditionStatement.setVisibility(View.GONE);
                        mEnter_url_layout.setVisibility(View.VISIBLE);
                        value[0] = "oth";
                        break;
                }
            }
        });

        //date only textviews
        mReg_early_start_date = findViewById(R.id.reg_early_start_date);
        mReg_early_end_date = findViewById(R.id.reg_early_end_date);
        mAdv_reg_start_date = findViewById(R.id.adv_reg_start_date);
        mAdv_reg_end_date = findViewById(R.id.adv_reg_end_date);
        mLast_reg_start_date = findViewById(R.id.last_reg_start_date);
        mLast_reg_end_date = findViewById(R.id.last_reg_end_date);

        //layout, registration type, amount, spinner, add more -> textviews
        mEarly_add_layout = findViewById(R.id.early_add_layout);
        mAdv_add_layout = findViewById(R.id.adv_add_layout);
        mLast_add_layout = findViewById(R.id.last_add_layout);

        mEarly_reg_type = findViewById(R.id.early_reg_type);
        mAdv_reg_type = findViewById(R.id.adv_reg_type);
        mLast_reg_type = findViewById(R.id.last_reg_type);

        mEarly_amount = findViewById(R.id.early_amount);
        mAdv_amount = findViewById(R.id.adv_amount);
        mLast_amount = findViewById(R.id.last_amount);

        mEarly_currency_unit = findViewById(R.id.early_currency_unit);
        mAdv_currency_unit = findViewById(R.id.adv_currency_unit);
        mLast_currency_unit = findViewById(R.id.last_currency_unit);

        mEarly_add_more = findViewById(R.id.early_add_more);
        mAdv_add_more = findViewById(R.id.adv_add_more);
        mLast_add_more = findViewById(R.id.last_add_more);

        mEarlyAddedMoreList = new ArrayList<>();
        mAdvAddedMoreList = new ArrayList<>();
        mLastAddedMoreList = new ArrayList<>();

        //getting view from all the clicked textviews
        mReg_early_start_date.setOnClickListener(this);
        mReg_early_end_date.setOnClickListener(this);
        mAdv_reg_start_date.setOnClickListener(this);
        mAdv_reg_end_date.setOnClickListener(this);
        mLast_reg_start_date.setOnClickListener(this);
        mLast_reg_end_date.setOnClickListener(this);

        mEarly_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView remove = addMoreLayout.findViewById(R.id.add_more_remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEarly_add_layout.removeView(addMoreLayout);
                        mEarlyAddedMoreList.remove(addMoreLayout);
                    }
                });
                mEarly_add_layout.addView(addMoreLayout);
                mEarlyAddedMoreList.add(addMoreLayout);
                Log.d("SGFH", "onClick: ============================"+String.valueOf(mEarlyAddedMoreList.size()));
            }
        });
        mAdv_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView remove = addMoreLayout.findViewById(R.id.add_more_remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdv_add_layout.removeView(addMoreLayout);
                        mAdvAddedMoreList.remove(addMoreLayout);
                    }
                });
                mAdv_add_layout.addView(addMoreLayout);
                mAdvAddedMoreList.add(addMoreLayout);

            }
        });
        mLast_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView remove = addMoreLayout.findViewById(R.id.add_more_remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLast_add_layout.removeView(addMoreLayout);
                        mLastAddedMoreList.remove(addMoreLayout);
                    }
                });
                mLast_add_layout.addView(addMoreLayout);
                mLastAddedMoreList.add(addMoreLayout);

            }
        });

        mPostEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startdate = mStartDateET.getText().toString();
                String enddate = mEndDateET.getText().toString();
                String starttime = mStartTimeET.getText().toString();
                String endtime = mEndTimeET.getText().toString();
                String organizer = mOrganizerET.getText().toString();
                final String earlytype = mEarly_reg_type.getText().toString();
                final String advtype = mAdv_reg_type.getText().toString();
                final String lasttype = mLast_reg_type.getText().toString();
                String earlystartdate = mReg_early_start_date.getText().toString();
                String earlyenddate = mReg_early_end_date.getText().toString();
                String laststartdate = mLast_reg_start_date.getText().toString();
                String lastenddate = mLast_reg_end_date.getText().toString();
                String advstartdate = mAdv_reg_start_date.getText().toString();
                String advenddate = mAdv_reg_end_date.getText().toString();
                final String earlyamount = mEarly_amount.getText().toString();
                final String advamount = mAdv_amount.getText().toString();
                final String lastamount = mLast_amount.getText().toString();
                final String earlycurrency = mEarly_currency_unit.getSelectedItem().toString();
                final String advcurrency = mAdv_currency_unit.getSelectedItem().toString();
                final String lastcurrency = mLast_currency_unit.getSelectedItem().toString();
                final String mUrl = mEnter_url.getText().toString();


                if(!startdate.isEmpty() && !enddate.isEmpty() && !starttime.isEmpty() &&
                        !endtime.isEmpty() && !organizer.isEmpty() &&
                        //taking paid event details
                        !earlytype.isEmpty() && !advtype.isEmpty() && !lasttype.isEmpty() &&
                        !earlystartdate.isEmpty() && !earlyenddate.isEmpty() && !laststartdate.isEmpty() &&
                        !lastenddate.isEmpty() && !advstartdate.isEmpty() && !advenddate.isEmpty() &&
                        !earlyamount.isEmpty() && !advamount.isEmpty() && !lastamount.isEmpty()){
                    if (!NetworkUtil.isNetworkAvailable()) {
                        Toast.makeText(PostEvent2Activity.this, "No internet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mRequestQueue = MyVolley.getInstance().getRequestQueue();

                    final StringRequest requestPostEvent = new StringRequest(Request.Method.POST,
                            mNewSlug.replace("\"", ""), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject responseObject = new JSONObject(response);
//                                String sd = responseObject.getString("redirecturl");
                                Toast.makeText(PostEvent2Activity.this, "Event posted successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PostEvent2Activity.this, EventsActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkUtil.handleSimpleVolleyRequestError(error, PostEvent2Activity.this);
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() {
                            final Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("datetimepicker_start_date",mStartDateET.getText().toString() );
                            params.put("datetimepicker_end_date",mEndDateET.getText().toString());
                            params.put("start_time",mStartTimeET.getText().toString());
                            params.put("end_time",mEndTimeET.getText().toString());
                            params.put("event_organizer",mOrganizerET.getText().toString());
                            params.put("event_type_name",eventType);
                            params.put("early_bird_registration_type[0]",earlytype);
                            params.put("early_bird_registration_amount[0]",earlyamount);
                            params.put("early_bird_registration_currency[0]",earlycurrency);
                            params.put("advance_registration_type[0]",advtype);
                            params.put("advance_registration_amount[0]",advamount);
                            params.put("advance_registration_currency[0]",advcurrency);
                            params.put("last_minute_registration_type[0]",lasttype);
                            params.put("last_minute_registration_amount[0]",lastamount);
                            params.put("last_minute_registration_currency[0]",lastcurrency);
                            params.put("reg_form", value[0]);
                            if(value[0].equals("oth"))
                                params.put("url",mUrl);
                            Log.d("MESSAGEMESSAGE", "getParams: ==============="+value[0]+mUrl);


                            for(int i = 0; i < mEarlyAddedMoreList.size(); i++){
                                LinearLayout layout = mEarlyAddedMoreList.get(i);
                                EditText eregtype = layout.findViewById(R.id.add_more_reg_type);
                                EditText eregamount = layout.findViewById(R.id.add_more_award_amount);
                                Spinner currency = layout.findViewById(R.id.add_more_currency_unit);

                                String earlyRegType = eregtype.getText().toString();
                                String earlyRegAmount = eregamount.getText().toString();
                                String earlyCurrency = currency.getSelectedItem().toString();

                                params.put("early_bird_registration_type["+(i+1)+"]",earlyRegType);
                                params.put("early_bird_registration_amount["+(i+1)+"]",earlyRegAmount);
                                params.put("early_bird_registration_currency["+(i+1)+"]",earlyCurrency);
                            }
                            for(int i = 0; i < mAdvAddedMoreList.size(); i++){
                                LinearLayout layout = mAdvAddedMoreList.get(i);
                                EditText aregtype = layout.findViewById(R.id.add_more_reg_type);
                                EditText aregamount = layout.findViewById(R.id.add_more_award_amount);
                                Spinner currency = layout.findViewById(R.id.add_more_currency_unit);

                                String advRegType = aregtype.getText().toString();
                                String advRegAmount = aregamount.getText().toString();
                                String advCurrency = currency.getSelectedItem().toString();

                                params.put("advance_registration_type["+(i+1)+"]",advRegType);
                                params.put("advance_registration_amount["+(i+1)+"]",advRegAmount);
                                params.put("advance_registration_currency["+(i+1)+"]",advCurrency);
                            }
                            for(int i = 0; i < mLastAddedMoreList.size(); i++){
                                LinearLayout layout = mLastAddedMoreList.get(i);
                                EditText lregtype = layout.findViewById(R.id.add_more_reg_type);
                                EditText lregamount = layout.findViewById(R.id.add_more_award_amount);
                                Spinner currency = layout.findViewById(R.id.add_more_currency_unit);

                                String lastRegType = lregtype.getText().toString();
                                String lastRegAmount = lregamount.getText().toString();
                                String lastCurrency = currency.getSelectedItem().toString();

                                params.put("last_minute_registration_type["+(i+1)+"]",lastRegType);
                                params.put("last_minute_registration_amount["+(i+1)+"]",lastRegAmount);
                                params.put("last_minute_registration_currency["+(i+1)+"]",lastCurrency);
                            }

                            return params;
                        }
                    };
                    requestPostEvent.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mRequestQueue.add(requestPostEvent);
                }else{
                    Toast.makeText(getApplicationContext(),"Form Incomplete",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        //passing all the respective ids to a new method
        if(v == mReg_early_start_date)
            noteDate(R.id.reg_early_start_date);
        if(v == mReg_early_end_date)
            noteDate(R.id.reg_early_end_date);
        if(v == mAdv_reg_start_date)
            noteDate(R.id.adv_reg_start_date);
        if(v == mAdv_reg_end_date)
            noteDate(R.id.adv_reg_end_date);
        if(v == mLast_reg_start_date)
            noteDate(R.id.last_reg_start_date);
        if(v == mLast_reg_end_date)
            noteDate(R.id.last_reg_end_date);

    }

    private void noteDate(final int id){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(PostEvent2Activity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //set date according to the id selected
                        if(id == R.id.reg_early_start_date)
                            mReg_early_start_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);
                        if(id == R.id.reg_early_end_date)
                            mReg_early_end_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);
                        if(id == R.id.adv_reg_start_date)
                            mAdv_reg_start_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);
                        if(id == R.id.adv_reg_end_date)
                            mAdv_reg_end_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);
                        if(id == R.id.last_reg_start_date)
                            mLast_reg_start_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);
                        if(id == R.id.last_reg_end_date)
                            mLast_reg_end_date.setText(year+ "-" + (monthOfYear + 1) +"-"+dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }
}

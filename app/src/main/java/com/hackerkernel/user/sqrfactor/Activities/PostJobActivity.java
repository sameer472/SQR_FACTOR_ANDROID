package com.hackerkernel.user.sqrfactor.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.Skillsbean;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostJobActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    EditText job_description, min_salary, max_salary, job_skills, job_qual, job_firm, job_expirydate;
    TextView job_title;

    TextView add_more_s, add_more_q;
    Spinner job_category_spinner, job_position_spinner, job_work_exp_spinner, job_currency_unit;


    private int cyear, cmonth, cdate;
    private SearchableSpinner job_country_spinner;
    private SearchableSpinner job_state_spinner;
    private SearchableSpinner job_city_spinner;

    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<HashMap> countriesMapList = new ArrayList<>();
    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<HashMap> statesMapList = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<HashMap> citiesMapList = new ArrayList<>();

    String job_titleStr,
            descriptionStr,
            categoryStr,
            type_of_positionStr,
            work_experienceStr,
            minimumStr,
            maximumStr,
            salary_typeStr,
            skillsStr,
            educational_qualificationStr,
            firmStr,
            countryStr,
            stateStr,
            cityStr,
            datetimepickerStr;
    List<Skillsbean> group_feild1 = new ArrayList<>();
    List<Skillsbean> group_feild2 = new ArrayList<>();

    HashMap<Integer, List<View>> optionItemsMap = new HashMap<>();
    HashMap<Integer, List<View>> optionItemsMap2 = new HashMap<>();
    private List<View> mLayoutList = new ArrayList<>();
    private List<View> mLayoutList2 = new ArrayList<>();
    private LayoutInflater inflater;
    private LinearLayout mContainerView, mContainerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        mSp = MySharedPreferences.getInstance(this);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mContainerView = (LinearLayout) findViewById(R.id.parentView);
        mContainerView2 = (LinearLayout) findViewById(R.id.parentView2);
        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cdate = calendar.get(Calendar.DAY_OF_MONTH);

        add_more_s = findViewById(R.id.skills_add_more);
        add_more_q = findViewById(R.id.qual_add_more);
        add_more_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreSkillsRow(view);
            }
        });
        add_more_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreQulRow(view);
            }
        });

        job_category_spinner = findViewById(R.id.job_category_spinner);
        job_position_spinner = findViewById(R.id.job_position_spinner);
        job_work_exp_spinner = findViewById(R.id.job_work_exp_spinner);
        job_currency_unit = findViewById(R.id.job_currency_unit);


        job_title = findViewById(R.id.job_title);
        job_description = findViewById(R.id.job_description);
        min_salary = findViewById(R.id.min_salary);
        max_salary = findViewById(R.id.max_salary);
        job_skills = findViewById(R.id.job_skills);
        job_qual = findViewById(R.id.job_qual);
        job_firm = findViewById(R.id.job_firm);
        job_expirydate = findViewById(R.id.job_expirydate);

        job_expirydate = findViewById(R.id.job_expirydate);
        job_expirydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(PostJobActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        job_expirydate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, cyear, cmonth, cdate).show();

            }
        });

        job_country_spinner = findViewById(R.id.job_country_spinner);
        job_state_spinner = findViewById(R.id.job_state_spinner);
        job_city_spinner = findViewById(R.id.job_city_spinner);

        getCountryList();

        //when any country is selected
        job_country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idk = (String) countriesMapList.get(position).get("id");
                //for states
                getStatesList(idk);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //when any state is selected
        job_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) statesMapList.get(i).get("id");
                //for cities
                getCitiesList(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        findViewById(R.id.job_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id_country = countriesMapList.get((int) job_country_spinner.getSelectedItemId()).get("id").toString();
                String id_state = statesMapList.get((int) job_state_spinner.getSelectedItemId()).get("id").toString();
                String id_city = citiesMapList.get((int) job_city_spinner.getSelectedItemId()).get("id").toString();
                String job_cat = job_category_spinner.getSelectedItem().toString();
                String job_pos = job_position_spinner.getSelectedItem().toString();
                String job_exp = job_work_exp_spinner.getSelectedItem().toString();
                String job_unit = job_currency_unit.getSelectedItem().toString();


                job_titleStr = job_title.getText().toString();
                descriptionStr = job_description.getText().toString();
                categoryStr = job_cat;
                type_of_positionStr = job_pos;
                work_experienceStr = job_exp;
                minimumStr = min_salary.getText().toString();
                maximumStr = max_salary.getText().toString();
                salary_typeStr = job_unit;
                skillsStr = job_skills.getText().toString();
                educational_qualificationStr = job_qual.getText().toString();
                firmStr = job_firm.getText().toString();
                countryStr = id_country.replace("\"", "");
                stateStr = id_state.replace("\"", "");
                cityStr = id_city.replace("\"", "");
                datetimepickerStr = job_expirydate.getText().toString();


                if (mLayoutList.size() != 0) {
                    for (int i = 0; i < mLayoutList.size(); i++) {
                        Skillsbean groupFeildBean = new Skillsbean();
                        ViewGroup mContainerView = (LinearLayout) findViewById(R.id.parentView);
                        View v = mContainerView.getChildAt(i);
                        EditText et1 = v.findViewById(R.id.job_qual);
                        if (et1.getText().toString().equals("")) {
                            Toast.makeText(PostJobActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                        } else {
                            groupFeildBean.setIndex(i + 1);
                            groupFeildBean.setText(et1.getText().toString().trim());
                            group_feild1.add(groupFeildBean);
                        }

                    }
                }
                if (mLayoutList2.size() != 0) {
                    for (int i = 0; i < mLayoutList2.size(); i++) {
                        Skillsbean groupFeildBean = new Skillsbean();
                        ViewGroup mContainerView = (LinearLayout) findViewById(R.id.parentView2);
                        View v = mContainerView.getChildAt(i);
                        EditText et1 = v.findViewById(R.id.job_qual2);
                        if (et1.getText().toString().equals("")) {
                            Toast.makeText(PostJobActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                        } else {
                            groupFeildBean.setIndex(i + 1);
                            groupFeildBean.setText(et1.getText().toString().trim());
                            group_feild2.add(groupFeildBean);
                        }

                    }
                }


                if (job_titleStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter job title", Toast.LENGTH_SHORT).show();
                } else if (descriptionStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
                } else if (categoryStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select job category", Toast.LENGTH_SHORT).show();
                } else if (type_of_positionStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select position", Toast.LENGTH_SHORT).show();
                } else if (work_experienceStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select work experience", Toast.LENGTH_SHORT).show();
                } else if (minimumStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter minimum salary", Toast.LENGTH_SHORT).show();
                } else if (maximumStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter maximum salary", Toast.LENGTH_SHORT).show();
                } else if (salary_typeStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select salary type", Toast.LENGTH_SHORT).show();
                } else if (skillsStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter skills", Toast.LENGTH_SHORT).show();
                } else if (educational_qualificationStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please educational qualification", Toast.LENGTH_SHORT).show();
                } else if (firmStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please enter firm", Toast.LENGTH_SHORT).show();
                } else if (countryStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select country", Toast.LENGTH_SHORT).show();
                } else if (stateStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                } else if (cityStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
                } else if (datetimepickerStr.equals("")) {
                    Toast.makeText(PostJobActivity.this, "Please select job expire date", Toast.LENGTH_SHORT).show();
                } else {
                    postJob();
                }


            }
        });

    }

    public void addMoreSkillsRow(View v) {
        View view = inflater.inflate(R.layout.multiple_view_layout, null);
        final TextView remove = view.findViewById(R.id.remove);
        mLayoutList.add(view);
        int tagIndex = mLayoutList.size() - 1;
        remove.setTag(tagIndex);
        remove.setOnClickListener(this);
        mContainerView.addView(view);
    }

    public void addMoreQulRow(View v) {
        View view = inflater.inflate(R.layout.multiple_view_layout2, null);
        final TextView remove2 = view.findViewById(R.id.remove2);
        mLayoutList2.add(view);
        int tagIndex = mLayoutList2.size() - 1;
        remove2.setTag(tagIndex);
        remove2.setOnClickListener(this);
        mContainerView2.addView(view);
    }

    private void getCountryList() {
        StringRequest requestCountry = new StringRequest(Request.Method.GET, ServerConstants.EVENT_GET_COUNTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("yyyyyyyyyyyyyyy", "onResponse: Event list response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    final JSONArray countriesArray = responseObject.getJSONArray("countries");

                    for (int i = 0; i < countriesArray.length(); i++) {
                        final JSONObject singleObject = countriesArray.getJSONObject(i);
                        String cId = singleObject.getString("id");
                        String cName = singleObject.getString("name");
                        countries.add(cName);

                        //add to map list
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", cId);
                        map.put("name", cName);
                        countriesMapList.add(map);
                    }

                    ArrayAdapter<String> country_adapter = new ArrayAdapter<>(PostJobActivity.this, android.R.layout.simple_list_item_1, countries);
                    job_country_spinner.setAdapter(country_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                Log.d("Country List in Spinner", "getHeaders: api key = " + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }
        };
        requestCountry.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(requestCountry);
    }

    private void getStatesList(final String c) {
        StringRequest requestState = new StringRequest(Request.Method.POST, ServerConstants.EVENT_SET_STATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sssssssssssssssss", "onResponse: Event list response = " + response);

                states.clear();

                try {
                    JSONObject responseObject = new JSONObject(response);
                    final JSONArray statesArray = responseObject.getJSONArray("states");

                    statesMapList.clear();
                    for (int i = 0; i < statesArray.length(); i++) {
                        final JSONObject singleObject = statesArray.getJSONObject(i);
                        String sId = singleObject.getString("id");
                        String sName = singleObject.getString("name");
                        states.add(sName);

                        //add to map list
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", sId);
                        map.put("name", sName);
                        statesMapList.add(map);
                    }

                    ArrayAdapter<String> state_adapter = new ArrayAdapter<>(PostJobActivity.this, android.R.layout.simple_list_item_1, states);
                    job_state_spinner.setAdapter(state_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                Log.d("State List in Spinner", "getHeaders: api key = " + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("country", c);
                return params;
            }
        };
        requestState.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(requestState);
    }

    private void getCitiesList(final String s) {
        StringRequest requestCity = new StringRequest(Request.Method.POST, ServerConstants.EVENT_SET_CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cccccccccccccccccccc", "onResponse: Event list response = " + response);
                cities.clear();

                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray citiesArray = responseObject.getJSONArray("cities");

                    for (int i = 0; i < citiesArray.length(); i++) {
                        JSONObject singleObject = citiesArray.getJSONObject(i);
                        String cId = singleObject.getString("id");
                        String cName = singleObject.getString("name");
                        cities.add(cName);

                        //add to map list
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", cId);
                        map.put("name", cName);
                        citiesMapList.add(map);
                    }

                    ArrayAdapter<String> city_adapter = new ArrayAdapter<>(PostJobActivity.this, android.R.layout.simple_list_item_1, cities);
                    job_city_spinner.setAdapter(city_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                Log.d("State List in Spinner", "getHeaders: api key = " + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("state", s);
                return params;
            }
        };
        requestCity.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(requestCity);
    }

    private void postJob() {
        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.POST_JOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("postJobResponse: ", response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    Toast.makeText(PostJobActivity.this, responseObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                params.put("job_title", job_titleStr);
                params.put("description", descriptionStr);
                params.put("category", descriptionStr);
                params.put("type_of_position", type_of_positionStr);
                params.put("work_experience", work_experienceStr);
                params.put("minimum", minimumStr);
                params.put("maximum", maximumStr);
                params.put("salary_type", salary_typeStr);
                params.put("skills[0]", skillsStr);
                if (group_feild1.size() != 0) {
                    for (int i = 0; i < group_feild1.size(); i++) {
                        params.put("skills[" + group_feild1.get(i).getIndex() + "]", group_feild1.get(i).getText());
                    }
                }
                params.put("educational_qualification[0]", educational_qualificationStr);
                if (group_feild2.size() != 0) {
                    for (int i = 0; i < group_feild2.size(); i++) {
                        params.put("educational_qualification[" + group_feild2.get(i).getIndex() + "]", group_feild2.get(i).getText());
                    }
                }

                params.put("firm", firmStr);
                params.put("country", countryStr);
                params.put("state", stateStr);
                params.put("city", cityStr);
                params.put("datetimepicker", datetimepickerStr);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.remove) {
            int current = (int) view.getTag();
            View currentView = mLayoutList.get(current);
            mContainerView.removeView(currentView);
            mLayoutList.remove(current);
            for (int i = 0; i < mLayoutList.size(); i++) {
                TextView deletBtn = mLayoutList.get(i).findViewById(R.id.remove);
                deletBtn.setTag(i);
            }

            optionItemsMap.remove(current);
        } else if (view.getId() == R.id.remove2) {
            int current = (int) view.getTag();
            View currentView = mLayoutList2.get(current);
            mContainerView2.removeView(currentView);
            mLayoutList2.remove(current);
            for (int i = 0; i < mLayoutList2.size(); i++) {
                TextView deletBtn = mLayoutList2.get(i).findViewById(R.id.remove2);
                deletBtn.setTag(i);
            }

            optionItemsMap2.remove(current);
        }
    }
}

package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Pojo.CitiesClass;
import com.hackerkernel.user.sqrfactor.Pojo.CountryClass;
import com.hackerkernel.user.sqrfactor.Pojo.StateClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollegeBasicDetails extends AppCompatActivity {

    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList=new ArrayList<>();
    private ArrayList<String> statesName=new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList=new ArrayList<>();
    private ArrayList<String> citiesName=new ArrayList<>();
    private EditText nextEmail1,nextEmail2,nextEmail3;
    private int CountryID=0,StateID=0,CityID=0;
    private int actualCityID,actualStateID,actualCountryId;
    private String country_val=null,state_val=null,city_val=null,gender_val=null,country_name,state_name,city_name,compnayFirm_val = null;
    Toolbar toolbar;
    private int countryId=1;
    private EditText nameOfCollege,mobileNumber,email,shortBio,collegeName,registerNumber,facbook,linkedin,instagram,twitter;
    private Button save,addEmail;
    private boolean email2=false;
    private boolean email3=false;
    private int count=0;
    Spinner spin;
    private boolean firstTimeState=false,firstTimeCity=false;
    Spinner countrySpinner,stateSpinner,citySpinner;
    String spin_val=null;
    ArrayList<String> countries = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_basic_details);

        toolbar = findViewById(R.id.college_basic_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        countrySpinner=(Spinner)findViewById(R.id.college_Country);
        stateSpinner=(Spinner)findViewById(R.id.college_State);
        citySpinner=(Spinner)findViewById(R.id.college_City);

        LoadCountryFromServer();

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                if (countryName.size() > 0) {
                    country_name = countryName.get(position);
                    country_val = position + 1 + "";
                    LoadStateFromServer();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int  position, long id) {
                if(statesName.size()>0) {
                    state_name = statesName.get(position);
                    country_val=statesClassArrayList.get(position).getCountryId()+"";
                    state_val=actualStateID+position+"";
                    LoadCitiesFromServer();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int  position, long id) {
                if (citiesName.size()>0){
                    city_name = citiesName.get(position);
                    state_val=citiesClassArrayList.get(position).getStateId()+"";
                    country_val=citiesClassArrayList.get(position).getCountryId()+"";
                    city_val=actualCityID+position+"";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        nameOfCollege = (EditText) findViewById(R.id.college_name_text);
        mobileNumber = (EditText) findViewById(R.id.college_mobile_Text);
        email = (EditText) findViewById(R.id.college_email_text);
        shortBio = (EditText) findViewById(R.id.college_shortBioText);
        collegeName = (EditText) findViewById(R.id.college_university_name_text);
        registerNumber = (EditText) findViewById(R.id.college_registerNumber_text);
        facbook = (EditText) findViewById(R.id.college_facebookLinktext);
        instagram = (EditText) findViewById(R.id.college_InstagramLinktext);
        linkedin = (EditText) findViewById(R.id.college_LinkedinLinktext);
        twitter = (EditText) findViewById(R.id.college_TwitterLinktext);
        save = (Button) findViewById(R.id.college_save_next);
        addEmail = (Button) findViewById(R.id.college_AddEmail);

        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AddPopupEmail();
            }
        });

        ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(CollegeBasicDetails.this, android.R.layout.simple_list_item_1, countries);
        countrySpinner.setAdapter(spin_adapter1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDatToServer();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void LoadCountryFromServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Log.v("ResponseLike",s);
//                        Toast.makeText(BasicFirmDetails.this,"res"+s,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray countries=jsonObject.getJSONArray("countries");
                            countryName.clear();
                            countryClassArrayList.clear();
                            for (int i=0;i<countries.length();i++) {
                                CountryClass countryClass=new CountryClass(countries.getJSONObject(i));
                                countryClassArrayList.add(countryClass);
                                countryName.add(countryClass.getName());
                            }

                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(CollegeBasicDetails.this, android.R.layout.simple_list_item_1,countryName);
                            countrySpinner.setAdapter(spin_adapter1);
                            countrySpinner.setSelection(CountryID);
//                            LoadStateFromServer(countryID,stateID,cityID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }

        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);
    }

    public void LoadStateFromServer()
    {
        // Log.v("state ",countryID+stateID+cityID+"");
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray states=jsonObject.getJSONArray("states");
                            statesName.clear();
                            statesClassArrayList.clear();
                            for (int i=0;i< states.length();i++) {
                                StateClass stateClass=new StateClass(states.getJSONObject(i));
                                statesClassArrayList.add(stateClass);
                                statesName.add(stateClass.getName());
                            }

                            if(statesClassArrayList!=null && statesClassArrayList.size()>0) {
                                Log.v("cityId",statesClassArrayList.get(0).getId()+" "+CityID+"");
                                actualStateID=statesClassArrayList.get(0).getId();
                                StateID=StateID-statesClassArrayList.get(0).getId();
                            }

                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(CollegeBasicDetails.this, android.R.layout.simple_list_item_1,statesName);
                            stateSpinner.setAdapter(spin_adapter2);

                            if(firstTimeState) {
                                stateSpinner.setSelection(StateID);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country",country_val);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void LoadCitiesFromServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/city",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);
//                        Toast.makeText(BasicFirmDetails.this,s,Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray cities=jsonObject.getJSONArray("cities");
                            citiesName.clear();
                            citiesClassArrayList.clear();
                            for (int i=0;i< cities.length();i++) {
                                CitiesClass citiesClass=new CitiesClass(cities.getJSONObject(i));
                                citiesClassArrayList.add(citiesClass);
                                citiesName.add(citiesClass.getName());
                            }


                            if(citiesClassArrayList!=null&& citiesClassArrayList.size()>0) {
                                actualCityID=citiesClassArrayList.get(0).getId();
                                CityID=CityID-citiesClassArrayList.get(0).getId();
                            }
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(CollegeBasicDetails.this, android.R.layout.simple_list_item_1,citiesName);
                            citySpinner.setAdapter(spin_adapter3);

                            if(firstTimeCity) {
                                citySpinner.setSelection(CityID);
                                firstTimeCity=false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//              Log.v("state+country",state_val+" "+country_val);
                params.put("state",state_val);
                params.put("country",country_val);
                return params;
            }
        };
        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void AddPopupEmail() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(CollegeBasicDetails.this);
        final View promptsView = li.inflate(R.layout.addemailprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CollegeBasicDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText nextEmail1 = (EditText) promptsView
                .findViewById(R.id.nextEmailText1);
        final Button addPromptButton = (Button) promptsView
                .findViewById(R.id.AddPromptButton);

        final LinearLayout linearLayoutPrompt1 = (LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt1);
        linearLayoutPrompt1.setVisibility(View.GONE);

        final LinearLayout linearLayoutPrompt2 = (LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt2);
        linearLayoutPrompt2.setVisibility(View.GONE);

        final Button removePromptButton1 = (Button) promptsView
                .findViewById(R.id.removeEmailButton1);

        final Button removePromptButton2 = (Button) promptsView
                .findViewById(R.id.removeEmailButton2);

        //final TextInputLayout nextEmail=(TextInputLayout) promptsView.findViewById(R.id.nextEmail);

        addPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (!email2 && count == 1) {
                    linearLayoutPrompt1.setVisibility(View.VISIBLE);
                    email2 = true;
                }
                if (!email3 && count == 2) {
                    linearLayoutPrompt2.setVisibility(View.VISIBLE);
                    email3 = true;
                }
            }
        });

        removePromptButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email2) {
                    linearLayoutPrompt1.setVisibility(View.GONE);
                    email2 = false;
                    count--;
                }
            }
        });
        removePromptButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email3) {
                    linearLayoutPrompt2.setVisibility(View.GONE);
                    email3 = false;
                    count--;
                }
            }
        });




        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void LoadDatToServer()
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/hire-organization-basic-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//              // params.put("mobile",mobileNumber.getText().toString());
                // params.put("email",email.getText().toString());
                //params.put("email1",nextEmail1.getText().toString());
//                params.put("email2",nextEmail2.getText().toString());
//                Log.v("dataTosend",nameOfCollege.getText().toString()+" "+email.getText().toString()+""+country_val+""
//                +state_val+city_val+shortBio.getText().toString()+collegeName.getText().toString()+registerNumber.getText().toString()+facbook.getText().toString()
//                +twitter.getText().toString()+linkedin.getText().toString());

                if(!TextUtils.isEmpty(email.getText().toString())){
                    params.put("email[0]",email.getText().toString());
                }
                if(nextEmail1!=null){
                    params.put("email[1]",nextEmail1.getText().toString());
                }
                if(nextEmail2!=null){
                    params.put("email[2]",nextEmail2.getText().toString());
                }
                if(!TextUtils.isEmpty(nameOfCollege.getText().toString())){
                    params.put("name_of_the_company",nameOfCollege.getText().toString());
                } else{
                    params.put("name_of_the_company",null+"");
                }
                if(!TextUtils.isEmpty(shortBio.getText().toString())){
                    params.put("short_bio",shortBio.getText().toString());
                } else{
                    params.put("short_bio",null+"");
                }
                if(!TextUtils.isEmpty(collegeName.getText().toString())){
                    params.put("firm_or_company_name",collegeName.getText().toString());
                } else{
                    params.put("firm_or_company_name",null+"");
                }
                if(!TextUtils.isEmpty(registerNumber.getText().toString())){
                    params.put("firm_or_company_registration_number",registerNumber.getText().toString());
                } else{
                    params.put("firm_or_company_registration_number",null+"");
                }
                params.put("address",null+"");
                params.put("pin_code",null+"");
                params.put("business_description",null+"");
                params.put("webside",null+"");
                params.put("country",country_val);
                params.put("state",state_val);
                params.put("city",city_val);
                params.put("types_of_firm_company",null+"");
                params.put("facebook_link",facbook.getText().toString());
                params.put("twitter_link",twitter.getText().toString());
                params.put("linkedin_link",linkedin.getText().toString());
                params.put("instagram_link",instagram.getText().toString());
                //params.put("date_of_birth",instagram.getText().toString());
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }



}
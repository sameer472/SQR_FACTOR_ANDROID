package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.CitiesClass;
import com.hackerkernel.user.sqrfactor.Pojo.CountryClass;
import com.hackerkernel.user.sqrfactor.Pojo.StateClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicFirmDetails extends AppCompatActivity {
    Toolbar toolbar;
    private Button addEmail, AddOccupation, SaveandNext;
    private ArrayList<CountryClass> countryClassArrayList = new ArrayList<>();
    private ArrayList<String> countryName = new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList = new ArrayList<>();
    private ArrayList<String> statesName = new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList = new ArrayList<>();
    private ArrayList<String> citiesName = new ArrayList<>();
    private EditText nextEmail1, nextEmail2, nextEmail3;
    private boolean firsTimeLoading=false;
    private int actualCityID,actualStateID,actualCountryId;
    private String[] companyFirm;
    private String country_val=null,state_val=null,city_val=null,gender_val=null,country_name,state_name,city_name,compnayFirm_val = null;
    private int countryId = 1;
   // private String country_val = null, state_val = null, city_val = null, ;
    private EditText nameOfCompany, mobileNumber, email, shortBio, firmCompanyName, registerNumber, facbook, linkedin, instagram, twitter,emaileidttext1,emaileidttext2,emaileidttext3;;
    private Button save;
    private boolean firstTimeState=false,firstTimeCity=false;
    private boolean email2 = false;
    private boolean email3 = false;
    private int CountryID=1,StateID=1,CityID=1;
    private int count = 0;
    Spinner spin;
    UserClass userClass;
    UserData userData;
    Spinner countrySpinner, companySpinner, citySpinner, stateSpinner;
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> company = new ArrayList<String>();
    private TextInputLayout emailText1,emailText2,emailText3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_firm_details);

        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        userData= gson1.fromJson(json1, UserData.class);

        toolbar = (Toolbar) findViewById(R.id.basic_firm_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        countrySpinner = (Spinner) findViewById(R.id.company_Country);
        stateSpinner = (Spinner) findViewById(R.id.company_State);
        citySpinner = (Spinner) findViewById(R.id.company_City);
        companySpinner = (Spinner) findViewById(R.id.company_select);

        emailText1=findViewById(R.id.email1);
        emailText2=findViewById(R.id.email2);
        emailText3=findViewById(R.id.email3);

        emaileidttext1=findViewById(R.id.Emailtext1);
        emaileidttext2=findViewById(R.id.Emailtext2);
        emaileidttext3=findViewById(R.id.Emailtext3);



        nameOfCompany = (EditText) findViewById(R.id.company_name_text);
        mobileNumber = (EditText) findViewById(R.id.company_mobile_Text);
        email = (EditText) findViewById(R.id.company_email_text);
        shortBio = (EditText) findViewById(R.id.company_shortBioText);
        firmCompanyName = (EditText) findViewById(R.id.company_firm_name_text);
        registerNumber = (EditText) findViewById(R.id.company_registerNumber_text);
        facbook = (EditText) findViewById(R.id.company_facebookLinktext);
        instagram = (EditText) findViewById(R.id.company_InstagramLinktext);
        linkedin = (EditText) findViewById(R.id.company_LinkedinLinktext);
        twitter = (EditText) findViewById(R.id.company_TwitterLinktext);
        save = (Button) findViewById(R.id.company_SaveandNext);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UtilsClass.IsConnected(BasicFirmDetails.this)) {
                    saveDataToServer();
                }
                else {
                    MDToast.makeText(BasicFirmDetails.this, "Check Your Internet Connection", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
            }
        });

        addEmail = (Button) findViewById(R.id.company_AddEmail);
        addEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddEmailPopUp();
            }
        });

        companyFirm = getResources().getStringArray(R.array.Company);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                compnayFirm_val = companyFirm[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, companyFirm);
        companySpinner.setAdapter(spin_adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int  position, long id) {
                if(countryName.size()>0) {
                    country_name = countryName.get(position);
                    country_val=position+1+"";
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
                if (citiesName.size()>0) {
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
        BindDataTOviews();

    }

    public void LoadCountryFromServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
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

                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1,countryName);
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
                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1,statesName);
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
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1,citiesName);
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
//                Log.v("state+country",state_val+" "+country_val);
                params.put("state",state_val);
                params.put("country",country_val);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void AddEmailPopUp() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(BasicFirmDetails.this);
        final View promptsView = li.inflate(R.layout.addemailprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BasicFirmDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        nextEmail1 = (EditText) promptsView
                .findViewById(R.id.nextEmailText1);
        nextEmail2 = (EditText) promptsView
                .findViewById(R.id.nextEmailText2);
        nextEmail3 = (EditText) promptsView
                .findViewById(R.id.nextEmailText3);
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



        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        final Button saveButton= (Button) promptsView
                .findViewById(R.id.SaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BasicFirmDetails.this,"saved",Toast.LENGTH_LONG).show();
                if(!TextUtils.isEmpty(nextEmail1.getText().toString())) {
                    emailText1.setVisibility(View.VISIBLE);
                    emaileidttext1.setText(nextEmail1.getText().toString());
                    emaileidttext1.setEnabled(false);
                }

                if(email2 &&!TextUtils.isEmpty(nextEmail2.getText().toString())) {
                    emailText2.setVisibility(View.VISIBLE);
                    emaileidttext2.setText(nextEmail2.getText().toString());
                    emaileidttext2.setEnabled(false);
                }
                if(email3 && !TextUtils.isEmpty(nextEmail3.getText().toString())) {
                    emailText3.setVisibility(View.VISIBLE);
                    emaileidttext3.setText(nextEmail3.getText().toString());
                    emaileidttext3.setEnabled(false);
                }

                alertDialog.dismiss();

            }
        });
        final Button cancelButton= (Button) promptsView
                .findViewById(R.id.CancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BasicFirmDetails.this,"cancel",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void saveDataToServer() {
        //https://archsqr.in/api/profile/edit(POST Method)

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/hire-organization-basic-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Log.v("ResponseLike", s);
                      //  Toast.makeText(BasicFirmDetails.this, "Response" + s, Toast.LENGTH_LONG).show();
                        try {
                            //getting user info
                            SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
                            Gson gson = new Gson();
                            String json = mPrefs.getString("MyObject", "");
                            UserClass userClass = gson.fromJson(json, UserClass.class);


                           //getting userdata
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            Gson gson1 = new Gson();
                            String json1 = sharedPreferences.getString("UserData", "");
                            UserData userData= gson1.fromJson(json1, UserData.class);



                            userData.setName_of_the_company(nameOfCompany.getText().toString());
                            userData.setFirm_or_company_name(firmCompanyName.getText().toString());
                           // userClass.setLast_name(lNametext.getText().toString());
                            userClass.setShort_bio(shortBio.getText().toString());
                            userClass.setUser_address(city_name+", "+state_name+", "+country_name);



                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            json = gson.toJson(userClass);
                            prefsEditor.putString("MyObject", json);
                            prefsEditor.apply();

                            SharedPreferences mPrefs1 = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor1 = mPrefs1.edit();
                            json1 = gson1.toJson(userData);
                            prefsEditor1.putString("UserData", json1);
                            prefsEditor1.apply();


                            JSONObject jsonObject = new JSONObject(s);
                            nameOfCompany.setText("");
                            mobileNumber.setText("");
                            email.setText("");
                            shortBio.setText("");
                            firmCompanyName.setText("");
                            registerNumber.setText("");
                            facbook.setText("");
                            instagram.setText("");
                            linkedin.setText("");
                            twitter.setText("");
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

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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
               // Log.v("basicDetailsCountry",country_val+state_val+city_val);

                if(!TextUtils.isEmpty(nameOfCompany.getText().toString())) {
                    params.put("name_of_the_company", nameOfCompany.getText().toString());
                } else {
                    params.put("name_of_the_company", null+"");
                }
                if(!TextUtils.isEmpty(shortBio.getText().toString())) {
                    params.put("short_bio", shortBio.getText().toString());
                } else {
                    params.put("short_bio", null+"");
                }
                if(!TextUtils.isEmpty(firmCompanyName.getText().toString())) {
                    params.put("firm_or_company_name", firmCompanyName.getText().toString());
                } else {
                    params.put("firm_or_company_name", null+"");
                }
                if(!TextUtils.isEmpty(registerNumber.getText().toString())) {
                    params.put("firm_or_company_registration_number", registerNumber.getText().toString());
                } else {
                    params.put("firm_or_company_registration_number", null+"");
                }
                if(!TextUtils.isEmpty(facbook.getText().toString())) {
                    params.put("facebook_link", facbook.getText().toString());
                } else {
                    params.put("facebook_link", null+"");
                }
                if(!TextUtils.isEmpty(twitter.getText().toString())) {
                    params.put("twitter_link", twitter.getText().toString());
                } else {
                    params.put("twitter_link", null+"");
                }
                if(!TextUtils.isEmpty(linkedin.getText().toString())) {
                    params.put("linkedin_link", linkedin.getText().toString());
                } else {
                    params.put("linkedin_link",null+"");
                }

                params.put("types_of_firm_company", compnayFirm_val);
                params.put("address", null+"");
                params.put("pin_code", null+"");
                params.put("business_description", null+"");
                params.put("webside", null+"");
                params.put("country", country_val);
                params.put("state", state_val);
                params.put("city", city_val);
                // params.put("mobile",mobileNumber.getText().toString());
                // params.put("aadhar_id",email.getText().toString());
                //params.put("aadhar_id",nextEmail1.getText().toString());
                //params.put("aadhar_id",nextEmail2.getText().toString());
                //params.put("date_of_birth",instagram.getText().toString());
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);
    }


    private void BindDataTOviews() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        userData= gson1.fromJson(json1, UserData.class);
        if(userData!=null) {
            if (userData.getName_of_the_company()!=null && !userData.getName_of_the_company().equals("null")) {
                nameOfCompany.setText(userData.getName_of_the_company());
            }
            if (userClass.getMobile()!=null&&!userClass.getMobile().equals("null")) {
                mobileNumber.setText(userClass.getMobile());
            }
            if (userClass.getEmail()!=null&&!userClass.getEmail().equals("null")) {
                email.setText(userClass.getEmail());
            }
            if (userClass.getShort_bio()!=null && !userClass.getShort_bio().equals("null")) {
                shortBio.setText(userClass.getShort_bio());
            }
            if (userData.getFirm_or_company_name()!=null &&!userData.getFirm_or_company_name().equals("null")) {
                firmCompanyName.setText(userData.getFirm_or_company_name());
            }
            if (userData.getFirm_or_company_registration_number()!=null &&!userData.getFirm_or_company_registration_number().equals("null")) {
                registerNumber.setText(userData.getFirm_or_company_registration_number());
            }
            if (userData.getFacebook_link()!=null && !userData.getFacebook_link().equals("null")) {
                facbook.setText(userData.getFacebook_link());
            }
            if (userData.getLinkedin_link()!=null && !userData.getLinkedin_link().equals("null")) {
                linkedin.setText(userData.getLinkedin_link());
            }
            if (userData.getTwitter_link()!=null && !userData.getTwitter_link().equals("null")) {
                twitter.setText(userData.getTwitter_link());
            }
            if(userData.getInstagram_link()!=null &&!userData.getInstagram_link().equals("null")){
                instagram.setText(userData.getInstagram_link());
            }
            if(userData.getCountry_id()!=null&& !userData.getCountry_id().equals("null")) {
                CountryID=Integer.parseInt(userData.getCountry_id())-1;
                StateID=Integer.parseInt(userData.getState_id());
                CityID=Integer.parseInt(userData.getCity_id());
                firstTimeCity=true;
                firstTimeState=true;
                firsTimeLoading=true;
                LoadCountryFromServer();
                // LoadCountryFromServer(Integer.parseInt(userData.getCountry_id()),Integer.parseInt(userData.getState_id()),Integer.parseInt(userData.getCity_id()));
            } else {
                LoadCountryFromServer();
            }
        }
    }
}
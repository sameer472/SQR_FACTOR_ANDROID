package com.hackerkernel.user.sqrfactor;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.CitiesClass;
import com.hackerkernel.user.sqrfactor.Pojo.CountryClass;
import com.hackerkernel.user.sqrfactor.Pojo.StateClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.UserData;
import com.hackerkernel.user.sqrfactor.Pojo.User_basic_detail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasicDetails extends AppCompatActivity {

    private Button addEmail,AddOccupation,SaveandNext;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList=new ArrayList<>();
    private ArrayList<String> statesName=new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList=new ArrayList<>();
    private ArrayList<String> citiesName=new ArrayList<>();
    private ArrayList<String> emails=new ArrayList<>();
    private boolean firstTimeState=false,firstTimeCity=false;
    private EditText nextEmail1,nextEmail2,nextEmail3;
    private int CountryID=0,StateID=0,CityID=0,actualCityID,actualStateID,actualCountryId;
    private UserClass userClass;
    private UserData userData;
    private Spinner countrySpinner,gender,spin,stateSpinner,citySpinner;
    private String country_val=null,state_val=null,city_val=null,gender_val=null,country_name,state_name,city_name;
    private RadioGroup radioGroup;
    private EditText Occupation,fNametext,lNametext,Emailtext,mobileText,dateOfBirthText,UIDtext,shortBioTecxt,
            facebookLinktext,TwitterLinktext,LinkedinLinktext,InstagramLinktext,email,emaileidttext1,emaileidttext2,emaileidttext3;

    private LinearLayout linearLayout2;
    private boolean email2=false;
    private boolean firsTimeLoading=false;
    private boolean email3=false;
    private int countryId=1;
    private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6;
    private int count=0;
    private Toolbar toolbar;
    private SharedPreferences mPrefs;
    Gson gson;
    String json;
    private TextInputLayout emailText1,emailText2,emailText3;

    ArrayList<String> countries = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);

        toolbar = (Toolbar) findViewById(R.id.basic_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);


        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);




        emailText1=findViewById(R.id.email1);
        emailText2=findViewById(R.id.email2);
        emailText3=findViewById(R.id.email3);

        emaileidttext1=findViewById(R.id.Emailtext1);
        emaileidttext2=findViewById(R.id.Emailtext2);
        emaileidttext3=findViewById(R.id.Emailtext3);

        Occupation=(EditText)findViewById(R.id.otherOccupationtext);
        email=(EditText)findViewById(R.id.Emailtext);
        InstagramLinktext=(EditText)findViewById(R.id.InstagramLinktext);
        LinkedinLinktext=(EditText)findViewById(R.id.LinkedinLinktext);
        TwitterLinktext=(EditText)findViewById(R.id.TwitterLinktext);
        facebookLinktext=(EditText)findViewById(R.id.facebookLinktext);
        shortBioTecxt=(EditText)findViewById(R.id.shortBioTecxt);
        fNametext=(EditText)findViewById(R.id.fNametext);
        lNametext=(EditText)findViewById(R.id.lNametext);
        Emailtext=(EditText)findViewById(R.id.Emailtext);
        mobileText=(EditText)findViewById(R.id.mobileText);
        dateOfBirthText=(EditText)findViewById(R.id.dateOfBirthText);
        UIDtext=(EditText)findViewById(R.id.UIDtext);
        SaveandNext=(Button)findViewById(R.id.SaveandNext);
        AddOccupation=(Button)findViewById(R.id.AddOccupation);



        addEmail = (Button) findViewById(R.id.AddEmail);
        linearLayout2 =(LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout2.setVisibility(View.GONE);

        addEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AddEmailPopup();
            }
        });



        final String[] gender = { "Male", "Female" };
        spin = (Spinner) findViewById(R.id.gender);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                gender_val = gender[position];

            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1, gender);
        spin.setAdapter(spin_adapter);


        countrySpinner = (Spinner) findViewById(R.id.Country);
        stateSpinner = (Spinner) findViewById(R.id.State);
        citySpinner = (Spinner) findViewById(R.id.City);






        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {



                if(countryName.size()>0)
                {
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

            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(statesName.size()>0)
                {
                    state_name = statesName.get(position);
                    country_val=statesClassArrayList.get(position).getCountryId()+"";


                    if(firstTimeState)
                    {
                        state_val=actualStateID+position+"";
                        firstTimeState=false;
                       // Toast.makeText(getApplicationContext(),"first",Toast.LENGTH_LONG).show();
                    }
                    else {
                        state_val=actualStateID+position+"";
                       // Toast.makeText(getApplicationContext(),"second",Toast.LENGTH_LONG).show();
                        Log.v("statepostion",state_val+"");
                    }

                    LoadCitiesFromServer();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if (citiesName.size()>0)
                {

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







        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabe(myCalendar);
            }

        };

        dateOfBirthText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(BasicDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if(hasFocus)
                {

                    datePickerDialog.show();
                }
                else {
                    datePickerDialog.hide();
                }

            }
        });


        checkBox1= (CheckBox) findViewById(R.id.checkbox1);
        checkBox2= (CheckBox) findViewById(R.id.checkbox2);
        checkBox3= (CheckBox) findViewById(R.id.checkbox3);
        checkBox4= (CheckBox) findViewById(R.id.checkbox4);
        checkBox5= (CheckBox) findViewById(R.id.checkbox5);
        checkBox6= (CheckBox) findViewById(R.id.checkbox6);

        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    linearLayout2.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearLayout2.setVisibility(View.GONE);
                }
            }
        });

        SaveandNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Replace all toast with Mdtoast
                if(UtilsClass.IsConnected(BasicDetails.this))
                {
                    if(TextUtils.isEmpty(fNametext.getText().toString()))
                    {
                        MDToast.makeText(BasicDetails.this, "First name is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else if(TextUtils.isEmpty(lNametext.getText().toString()))
                    {
                        MDToast.makeText(BasicDetails.this, "Last name is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else if(country_val==null)
                    {
                        MDToast.makeText(BasicDetails.this, "Country field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                    }
                    else if(state_val==null)
                    {

                        MDToast.makeText(BasicDetails.this, "State field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else if(city_val==null)
                    {
                        MDToast.makeText(BasicDetails.this, "City field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else if(gender_val==null)
                    {
                        MDToast.makeText(BasicDetails.this, "Gender field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                    else {
                        saveDataToServer();
                    }
//

                }
                else {
                    MDToast.makeText(BasicDetails.this, "Check Your Internet Connection", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }



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
                        //Log.v("ResponseLike",s);
                        //Toast.makeText(BasicDetails.this,"res"+s,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray countries=jsonObject.getJSONArray("countries");
                            countryName.clear();
                            countryClassArrayList.clear();
                            for (int i=0;i<countries.length();i++)
                            {
                                CountryClass countryClass=new CountryClass(countries.getJSONObject(i));
                                countryClassArrayList.add(countryClass);
                                countryName.add(countryClass.getName());

                            }

                           // System.arraycopy(countryClassArrayList, 0, countryClassArrayList, 1, countryClassArrayList.size());
                            //System.arraycopy(countryName, 0, countryName, 1, countryName.size());

                            Log.v("base country",countryClassArrayList.get(0).getId()+" ");
                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,countryName);
                            countrySpinner.setAdapter(spin_adapter1);
                            countrySpinner.setSelection(CountryID);




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
                            for (int i=0;i< states.length();i++)
                            {
                                StateClass stateClass=new StateClass(states.getJSONObject(i));
                                statesClassArrayList.add(stateClass);
                                statesName.add(stateClass.getName());
                            }


                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,statesName);
                            stateSpinner.setAdapter(spin_adapter2);
                            if(statesClassArrayList!=null && statesClassArrayList.size()>0)
                            {

                                actualStateID=statesClassArrayList.get(0).getId();
                                StateID=StateID-statesClassArrayList.get(0).getId();
                                Log.v("statecoe",StateID+" ");

                            }
                            if(firstTimeState)
                            {
                                //Toast.makeText(getApplicationContext(),"firststate",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

    public void LoadCitiesFromServer()
    {
        //Toast.makeText(BasicDetails.this,stateId,Toast.LENGTH_LONG).show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/city",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray cities=jsonObject.getJSONArray("cities");
                            citiesName.clear();
                            citiesClassArrayList.clear();
                            for (int i=0;i< cities.length();i++)
                            {
                                CitiesClass citiesClass=new CitiesClass(cities.getJSONObject(i));
                                citiesClassArrayList.add(citiesClass);
                                citiesName.add(citiesClass.getName());
                            }

                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,citiesName);
                            citySpinner.setAdapter(spin_adapter3);

                            if(citiesClassArrayList!=null&& citiesClassArrayList.size()>0)
                            {

                                 actualCityID=citiesClassArrayList.get(0).getId();
                                 CityID=CityID-citiesClassArrayList.get(0).getId();

                            if(firstTimeCity)
                            {
                               // Toast.makeText(getApplicationContext(),"firstcity",Toast.LENGTH_LONG).show();
                                citySpinner.setSelection(CityID);
                                 firstTimeCity=false;
                            }
                            }



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
                Log.v("stateandcountrypostion",state_val+" "+country_val);
                params.put("state",state_val);
                params.put("country",country_val);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void AddEmailPopup()
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(BasicDetails.this);
        final View promptsView = li.inflate(R.layout.addemailprompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BasicDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        nextEmail1 = (EditText) promptsView
                .findViewById(R.id.nextEmailText1);
        nextEmail2 = (EditText) promptsView
                .findViewById(R.id.nextEmailText2);
        nextEmail3 = (EditText) promptsView
                .findViewById(R.id.nextEmailText3);

        final Button addPromptButton= (Button) promptsView
                .findViewById(R.id.AddPromptButton);

        final LinearLayout linearLayoutPrompt1=(LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt1);
        linearLayoutPrompt1.setVisibility(View.GONE);

        final LinearLayout linearLayoutPrompt2=(LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt2);
        linearLayoutPrompt2.setVisibility(View.GONE);

        final Button removePromptButton1= (Button) promptsView
                .findViewById(R.id.removeEmailButton1);

        final Button removePromptButton2= (Button) promptsView
                .findViewById(R.id.removeEmailButton2);






        addPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if(!email2 && count==1)
                {
                    linearLayoutPrompt1.setVisibility(View.VISIBLE);
                    email2=true;
                }
                if(!email3 && count==2)
                {
                    linearLayoutPrompt2.setVisibility(View.VISIBLE);
                    email3=true;
                }

            }
        });

        removePromptButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email2)
                {
                    linearLayoutPrompt1.setVisibility(View.GONE);
                    email2=false;
                    count--;

                }
            }
        });
        removePromptButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email3)
                {
                    linearLayoutPrompt2.setVisibility(View.GONE);
                    email3=false;
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
               // Toast.makeText(BasicDetails.this,"saved",Toast.LENGTH_LONG).show();

                if(count==0 && !TextUtils.isEmpty(nextEmail1.getText().toString()))
                {
                    emailText1.setVisibility(View.VISIBLE);
                    emaileidttext1.setText(nextEmail1.getText().toString());
                    emaileidttext1.setEnabled(false);
                }

                if(count==1 && email2 &&!TextUtils.isEmpty(nextEmail2.getText().toString()))
                {
                    emailText2.setVisibility(View.VISIBLE);
                    emaileidttext2.setText(nextEmail2.getText().toString());
                    emaileidttext2.setEnabled(false);
                }
                if(count==2 && email3 && !TextUtils.isEmpty(nextEmail3.getText().toString()))
                {
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
                //Toast.makeText(BasicDetails.this,"cancel",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });




    }


    public void saveDataToServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(BasicDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"parse/work-individual-basic",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);
                        MDToast.makeText(BasicDetails.this, "Profile updated successfully"+s, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
//

                        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = mPrefs.getString("MyObject", "");
                        UserClass userClass = gson.fromJson(json, UserClass.class);


                        userClass.setFirst_name(fNametext.getText().toString());
                        userClass.setLast_name(lNametext.getText().toString());
                        userClass.setShort_bio(shortBioTecxt.getText().toString());
                        userClass.setUser_address(city_name+", "+state_name+", "+country_name);



                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        json = gson.toJson(userClass);

                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();

                        fNametext.setText("");
                            lNametext.setText("");
                            mobileText.setText("");
                            dateOfBirthText.setText("");
                            email.setText("");
                            UIDtext.setText("");
                            shortBioTecxt.setText("");
                            facebookLinktext.setText("");
                            LinkedinLinktext.setText("");
                            TwitterLinktext.setText("");
                            InstagramLinktext.setText("");
                            Intent intent=new Intent(BasicDetails.this,ProfileActivity.class);
                            startActivity(intent);
                            finish();


//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        NetworkResponse response = volleyError.networkResponse;
                        if (volleyError instanceof ServerError && response != null) {
                            try {


                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                Log.v("chat",res);
//                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                               // Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                               // Toast.makeText(getApplicationContext(),e2.toString(),Toast.LENGTH_LONG).show();
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
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

//                Log.v("basicDetails",gender_val+fNametext.getText().toString()+lNametext.getText().toString()+
//                        country_val+state_val+city_val+"       "+checkBox1.getText().toString()+checkBox2.getText().toString()+checkBox3.getText().toString()+
//                        checkBox4.getText().toString());
//
                Log.v("basicDetailsCountry",country_val+state_val+city_val);

                params.put("first_name",fNametext.getText().toString());
                params.put("last_name",lNametext.getText().toString());
                params.put("country",country_val+"");
                params.put("state",state_val+"");
                params.put("city",city_val+"");
                params.put("gender",gender_val);
                params.put("aadhar_id",UIDtext.getText().toString());
                params.put("occupation",Occupation.getText().toString());
                params.put("mobile_number",mobileText.getText().toString());
                params.put("looking_for_an_architect","No");
                params.put("email[0]",email.getText().toString());
                if(nextEmail1!=null)
                params.put("email[1]",nextEmail1.getText().toString());
                if(nextEmail2!=null)
                params.put("email[2]",nextEmail2.getText().toString());
                if(nextEmail3!=null)
                params.put("email[3]",nextEmail2.getText().toString());



                if(checkBox1.isChecked())
                params.put("occupation[0]",checkBox1.getText().toString());

                if(checkBox2.isChecked())
                    params.put("occupation[1]",checkBox2.getText().toString());

                if(checkBox3.isChecked())
                    params.put("occupation[2]",checkBox3.getText().toString());

                if(checkBox4.isChecked())
                    params.put("occupation[3]",checkBox4.getText().toString());

                if(checkBox5.isChecked())
                    params.put("occupation[4]",checkBox5.getText().toString());

                if(checkBox6.isChecked())
                    params.put("occupation[5]",checkBox6.getText().toString());

                params.put("short_bio",shortBioTecxt.getText().toString());
                //params.put("")

                params.put("facebook_link",facebookLinktext.getText().toString());
                params.put("twitter_link",TwitterLinktext.getText().toString());
                params.put("linkedin_link",LinkedinLinktext.getText().toString());
                params.put("instagram_link",InstagramLinktext.getText().toString());
                params.put("date_of_birth",dateOfBirthText.getText().toString());


                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);




    }

    private void updateLabe(Calendar myCalendar) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirthText.setText(sdf.format(myCalendar.getTime()));
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



    private void BindDataTOviews() {

        RequestQueue requestQueue1 = Volley.newRequestQueue(BasicDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"profile/edit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.v("details123",response);
//                        MDToast.makeText(BasicDetails.this, response, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("User Data");
                            UserData  userData = new UserData(jsonArray.getJSONObject(0));

                            User_basic_detail user_basic_detail=new User_basic_detail(jsonObject.getJSONObject("User_basic_detail"));
                            userData.setUser_basic_detail(user_basic_detail);
                            JSONArray emailsObject=jsonObject.getJSONArray("emails");

                            for(int i=0;i<emailsObject.length();i++)
                            {

                                if(i==0)
                                {
                                    email.setText(emailsObject.getJSONObject(i).getString("email"));
                                }
                                if(i==1)
                                {
                                    emailText1.setVisibility(View.VISIBLE);
                                    emaileidttext1.setText(emailsObject.getJSONObject(i).getString("email"));
                                    emaileidttext1.setEnabled(false);

                                }
                                if(i==2)
                                {
                                    emailText2.setVisibility(View.VISIBLE);
                                    emaileidttext2.setText(emailsObject.getJSONObject(i).getString("email"));
                                    emaileidttext2.setEnabled(false);
                                }
                                if(i==3)
                                {
                                    emailText3.setVisibility(View.VISIBLE);
                                    emaileidttext3.setText(emailsObject.getJSONObject(i).getString("email"));
                                    emaileidttext3.setEnabled(false);
                                }
                            }


                            if(!userData.getOccupation().equals("null"))
                            {
                                String[] occupation=userData.getOccupation().split(",");
                                if(occupation.length>0)
                                {

                                     MarkOccupationCheckBox(occupation);
                                }
                            }

                            if (!userData.getUser_basic_detail().getFirst_name().equals("null")) {
                                fNametext.setText(userData.getUser_basic_detail().getFirst_name());
                            }
                            if (!userData.getUser_basic_detail().getLast_name().equals("null")) {
                                lNametext.setText(userData.getUser_basic_detail().getLast_name());
                            }
                            if (!userData.getShot_bio().equals("null")) {
                                shortBioTecxt.setText(userData.getShot_bio());
                            }

                            if (!userData.getDate_of_birth().equals("null")) {
                                dateOfBirthText.setText(userData.getDate_of_birth());
                            }
                            if (!userData.getFacebook_link().equals("null")) {
                                facebookLinktext.setText(userData.getFacebook_link());
                            }
                            if (!userData.getLinkedin_link().equals("null")) {
                                LinkedinLinktext.setText(userData.getLinkedin_link());
                            }
                            if (!userData.getTwitter_link().equals("null")) {
                                TwitterLinktext.setText(userData.getTwitter_link());
                            }
                            if (!userData.getInstagram_link().equals("null")) {
                                InstagramLinktext.setText(userData.getInstagram_link());
                            }
                            if (!userData.getAadhaar_id().equals("null")) {
                                UIDtext.setText(userData.getAadhaar_id());
                            }
                            if (!userData.getUser_basic_detail().getMobile_number().equals("null")) {
                                mobileText.setText(userData.getUser_basic_detail().getMobile_number());
                            }

                            if(!userData.getCountry_id().equals("null")&&!userData.getState_id().equals("null")&&!userData.getState_id().equals("null"))
                            {
                                CountryID=Integer.parseInt(userData.getCountry_id())-1;
                                StateID=Integer.parseInt(userData.getState_id());
                                CityID=Integer.parseInt(userData.getCity_id());

                                Log.v("responsecode_from",CountryID+" "+StateID+" "+CityID);
                                firstTimeCity=true;
                                firstTimeState=true;
                                firsTimeLoading=true;
                                LoadCountryFromServer();

                            }
                            else {
                                LoadCountryFromServer();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        NetworkResponse response = volleyError.networkResponse;
                        if (volleyError instanceof ServerError && response != null) {
                            try {


                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                Log.v("chat",res);
//                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                               // Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                               // Toast.makeText(getApplicationContext(),e2.toString(),Toast.LENGTH_LONG).show();
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
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


    private void MarkOccupationCheckBox(String[] occupation) {

        for(int i=0;i<occupation.length;i++)
        {

            if(occupation[i].equals("Architect")||occupation[i].equals(" Architect"))
             checkBox1.setChecked(true);

            else if(occupation[i].equals("Design Student")||occupation[i].equals(" Design Student") )
            checkBox2.setChecked(true);

            else if(occupation[i].equals("Academician")||occupation[i].equals(" Academician"))
                checkBox3.setChecked(true);

            else if(occupation[i].equals("Interior Designer")||occupation[i].equals(" Interior Designer"))
                checkBox4.setChecked(true);

            else if(occupation[i].equals("Landscape Architect")||occupation[i].equals(" Landscape Architect"))
                checkBox5.setChecked(true);

        }
    }
}
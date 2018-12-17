package com.hackerkernel.user.sqrfactor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Pojo.AllEducationDetailsClass;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EducationDetailsActivity extends AppCompatActivity {
    private LinearLayout newForm;
    private Button add_eduDetails,remove_eduother,save_eduDetails;
    private boolean adDate1=false,adDate2=false;
    ArrayList<AllEducationDetailsClass> allEducationDetailsClassArrayList=new ArrayList<>();
    //ArrayList<String> course=new ArrayList<>();

    private boolean gdDate1=false,gdDate2=false;
    private TextInputLayout admission;
    private EditText courseText,collegeText,admissionText,graduationText,courseText1,collegeText1,admissionText1,graduationText1;

    private Boolean isClicked= false;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        toolbar = (Toolbar) findViewById(R.id.edu_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);


        admission=(TextInputLayout)findViewById(R.id.admission);
        save_eduDetails=(Button)findViewById(R.id.save_eduDetails);
        courseText=(EditText)findViewById(R.id.courseText);
        collegeText=(EditText)findViewById(R.id.collegeText);
        admissionText= (EditText)findViewById(R.id.admissionText);
        graduationText=(EditText)findViewById(R.id.graduationText);

        courseText1=(EditText)findViewById(R.id.courseText1);
        collegeText1=(EditText)findViewById(R.id.collegeText1);
        admissionText1= (EditText)findViewById(R.id.admissionText1);
        graduationText1=(EditText)findViewById(R.id.graduationText1);

        add_eduDetails=(Button)findViewById(R.id.Add_eduDetails);
        remove_eduother=(Button)findViewById(R.id.Remove_eduother);
        newForm=(LinearLayout) findViewById(R.id.linear_edu);



        save_eduDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
                Intent intent=new Intent(EducationDetailsActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        newForm.setVisibility(View.GONE);
        add_eduDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked)
                {
                    newForm.setVisibility(View.VISIBLE);
                    isClicked=true;
                }

            }
        });

        remove_eduother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm!=null && isClicked)
                {
                    newForm.setVisibility(View.GONE);
                    isClicked=false;
                }
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
                updateLabel(myCalendar);
            }

        };


        admissionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(EducationDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if(hasFocus)
                {

                    adDate1=true;
                    adDate2=false;
                    gdDate1=false;
                    gdDate2=false;
                    datePickerDialog.show();
                }
                else {
                    datePickerDialog.hide();
                }

            }
        });

        admissionText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(EducationDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if(hasFocus)
                {

                    adDate1=false;
                    adDate2=true;
                    gdDate1=false;
                    gdDate2=false;
                    datePickerDialog.show();
                }
                else {
                    datePickerDialog.hide();
                }

            }
        });
        graduationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(EducationDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if(hasFocus)
                {

                    adDate1=false;
                    adDate2=false;
                    gdDate1=true;
                    gdDate2=false;
                    datePickerDialog.show();
                }
                else {
                    datePickerDialog.hide();
                }

            }
        });
        graduationText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(EducationDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if(hasFocus)
                {

                    adDate1=false;
                    adDate2=false;
                    gdDate1=false;
                    gdDate2=true;
                    datePickerDialog.show();
                }
                else {
                    datePickerDialog.hide();
                }

            }
        });

        GetDataFromServerAndBindToView();

    }
    public void GetDataFromServerAndBindToView()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"profile/edit/education-details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("ReponseFeed", response);
                        // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray allEducationDetails=jsonObject.getJSONArray("allEducationDetails");
//                            Toast.makeText(getApplicationContext(),allEducationDetails.length(),Toast.LENGTH_LONG).show();
                            for(int i=0;i<allEducationDetails.length();i++)
                            {
                                AllEducationDetailsClass allEducationDetailsClass=new AllEducationDetailsClass(allEducationDetails.getJSONObject(i));
                                allEducationDetailsClassArrayList.add(allEducationDetailsClass);

                            }

                            BindDataToView(allEducationDetailsClassArrayList);

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
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

        };


        requestQueue.add(myReq);
    }

    public void BindDataToView(ArrayList<AllEducationDetailsClass> allEducationDetailsClassArray)
    {

        for(int i=0;i<allEducationDetailsClassArray.size();i++)
        {
            if(i==0)
            {
                courseText.setText(allEducationDetailsClassArray.get(i).getCourse());
                collegeText.setText(allEducationDetailsClassArray.get(i).getCollege_university());
                admissionText.setText(allEducationDetailsClassArray.get(i).getYear_of_admission());
                graduationText.setText(allEducationDetailsClassArray.get(i).getYear_of_graduation());
            }
            if(i==1 && !isClicked)
            {

                newForm.setVisibility(View.VISIBLE);
                isClicked=true;
                courseText1.setText(allEducationDetailsClassArray.get(i).getCourse());
                collegeText1.setText(allEducationDetailsClassArray.get(i).getCollege_university());
                admissionText1.setText(allEducationDetailsClassArray.get(i).getYear_of_admission());
                graduationText1.setText(allEducationDetailsClassArray.get(i).getYear_of_graduation());
            }

        }
    }



    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(adDate1==true)
            admissionText.setText(sdf.format(myCalendar.getTime()));
        if(gdDate1==true)
            graduationText.setText(sdf.format(myCalendar.getTime()));
        if(adDate2==true)
            admissionText1.setText(sdf.format(myCalendar.getTime()));
        if(gdDate2==true)
            graduationText1.setText(sdf.format(myCalendar.getTime()));
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

    public void saveDataToServer()
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"parse/work-education-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(EducationDetailsActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            collegeText.setText("");
                            courseText.setText("");
                            admissionText.setText("");
                            graduationText.setText("");
                            collegeText1.setText("");
                            courseText1.setText("");
                            admissionText1.setText("");
                            graduationText1.setText("");

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
                if(!TextUtils.isEmpty(courseText.getText().toString()))
                {
                    params.put("course[0]",courseText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(courseText1.getText().toString()))
                {
                    //courseArray.add(courseText1.getText().toString());
                    params.put("course[1]",courseText1.getText().toString());
                }
//                JSONArray course = new JSONArray(courseArray);


                if(!TextUtils.isEmpty(collegeText.getText().toString()))
                {
                    //collegeArray.add(collegeText.getText().toString());
                    params.put("college_university[0]",collegeText.getText().toString());
                }
                if(!TextUtils.isEmpty(collegeText1.getText().toString()))
                {
                    params.put("college_university[1]",collegeText.getText().toString());
                }
//                JSONArray c = new JSONArray(collegeArray);


                if(!TextUtils.isEmpty(admissionText.getText().toString()))
                {
                    //admissionYearArray.add(admissionText.getText().toString());
                    params.put("year_of_admission[0]",admissionText.getText().toString());
                }
                if(!TextUtils.isEmpty(admissionText1.getText().toString()))
                {
                    params.put("year_of_admission[1]",admissionText1.getText().toString());
                }
                //JSONArray a = new JSONArray(admissionYearArray);


                if(!TextUtils.isEmpty(graduationText.getText().toString()))
                {
//                    graduationYearArray.add(graduationText.getText().toString());
                    params.put("year_of_graduation[0]",graduationText.getText().toString());
                }
                if(!TextUtils.isEmpty(graduationText1.getText().toString()))
                {
                    params.put("year_of_graduation[1]",graduationText1.getText().toString());
                }
                //JSONArray g = new JSONArray(graduationYearArray);


                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }
}

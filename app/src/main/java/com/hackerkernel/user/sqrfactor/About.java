package com.hackerkernel.user.sqrfactor;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.UserData;
import com.hackerkernel.user.sqrfactor.Pojo.Work_Individuals_User_Details_Class;
import com.hackerkernel.user.sqrfactor.Pojo.firmDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class About extends AppCompatActivity {

    TextView aboutName,aboutFollowers,aboutFollowing,about_city_text,about_shot_bio_text,about_company_type_text,about_company_name_text;
    TextView email,phoneNumber,about_nameofCompany_text,about_mobile_number_text,about_email_text,about_country_text,about_state_text,
            about_company_register_num_text;
    Toolbar toolbar;
    private int userId;
    private LinearLayout employee_member_details,ind_other_details,ind_professional_details,ind_basic_details,personal_info,basic_details,company_firm_details,basic_details_firm,ind_education;
    String usertype;
    TextView about_facebook_text,about_twitter_text,about_linkedin_text,about_instagram_text,employeemember_details,about_name_short_bio;


    //textview for ind_basic_details
    private TextView ind_basic_details_short_bio_text,ind_basic_details_dob_text,ind_basic_details_email_text,ind_basic_details_gender_text,
            ind_basic_details_phone_number_text,ind_basic_details_role_text,ind_basic_details_full_name_text;

    //textview for ind_proffestional details
    private TextView ind_professional_details_coa_reg_text,ind_professional_details_yearsr_since_service_text,ind_professional_details_role_text,
            ind_professional_details_company_firm_or_college_text,ind_professional_details_start_date_text,ind_professional_details_end_date_of_working_currently_text,
            ind_professional_details_salary_text;

    //textview for other details
    private TextView ind_other_details_text,ind_other_details_award_name_text,ind_other_details_project_name_text,ind_other_details_services_offered_text;

    //textview for ind_education

    private TextView ind_education_college_name_text,ind_education_startdate_enddate,ind_education_branch;

    private  UserClass userClass;

    //textview for firm_basic_details
    private TextView basic_details_firm_instagram_text,basic_details_firm_linkedin_text,basic_details_firm_twitter_text,basic_details_firm_facebook_text,basic_details_firm_company_register_num_text,basic_details_firm_company_name_text
            ,basic_details_firm_company_type_text,basic_details_firm_shot_bio_text,basic_details_firm_email_text,basic_details_firm_mobile_number_text,basic_details_firm_nameofCompany_text;

    //textview for firm_member_details
    private TextView employee_firm_full_name_text,employee_firm_role_text,employee_firm_phone_number_text,employee_firm_aadhaar_id_text,employee_firm_email_text,
    employee_firm_country_text,employee_firm_state_text,employee_firm_city_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if(getIntent()!=null)
        {
            userId=getIntent().getIntExtra("UserID",0);
            usertype=getIntent().getStringExtra("userType");
            //Toast.makeText(getApplicationContext(), "id"+userId+" "+usertype, Toast.LENGTH_LONG).show();
        }


        about_name_short_bio=findViewById(R.id.about_name_short_bio);
        //linear layour for firm


        employee_member_details=findViewById(R.id.employee_member_details);
        basic_details_firm=findViewById(R.id.basic_details_firm);

           //textview for firm_member_details
        employee_firm_full_name_text=findViewById(R.id.employee_firm_full_name_text);
        employee_firm_role_text=findViewById(R.id.employee_firm_role_text);
        employee_firm_phone_number_text=findViewById(R.id.employee_firm_phone_number_text);
        employee_firm_aadhaar_id_text=findViewById(R.id.employee_firm_aadhaar_id_text);
        employee_firm_email_text=findViewById(R.id.employee_firm_email_text);
        employee_firm_country_text=findViewById(R.id.employee_firm_country_text);
        employee_firm_state_text=findViewById(R.id.employee_firm_state_text);
        employee_firm_city_text=findViewById(R.id.employee_firm_city_text);


        //textview for basic_details_firm_instagram_text
        basic_details_firm_instagram_text=findViewById(R.id.basic_details_firm_instagram_text);
        basic_details_firm_linkedin_text=findViewById(R.id.basic_details_firm_linkedin_text);
        basic_details_firm_twitter_text=findViewById(R.id.basic_details_firm_twitter_text);
        basic_details_firm_facebook_text=findViewById(R.id.basic_details_firm_facebook_text);
        basic_details_firm_company_register_num_text=findViewById(R.id.basic_details_firm_company_register_num_text);
        basic_details_firm_company_name_text=findViewById(R.id.basic_details_firm_company_name_text);
        basic_details_firm_company_type_text=findViewById(R.id.basic_details_firm_instagram_text);
        basic_details_firm_shot_bio_text=findViewById(R.id.basic_details_firm_shot_bio_text);
        basic_details_firm_email_text=findViewById(R.id.basic_details_firm_email_text);
        basic_details_firm_mobile_number_text=findViewById(R.id.basic_details_firm_mobile_number_text);
        basic_details_firm_nameofCompany_text=findViewById(R.id.basic_details_firm_nameofCompany_text);

        //



         //linear layout for work_individuals
        ind_basic_details=findViewById(R.id.ind_basic_details);
        ind_professional_details=findViewById(R.id.ind_professional_details);
        ind_other_details=findViewById(R.id.ind_other_details);
        ind_education=findViewById(R.id.ind_education);

        //textview for ind_education
        ind_education_branch=findViewById(R.id.ind_education_branch);
        ind_education_startdate_enddate=findViewById(R.id.ind_education_startdate_enddate);
        ind_education_college_name_text=findViewById(R.id.ind_education_college_name_text);



      //textview for other details
        ind_other_details_text=findViewById(R.id.ind_other_details_award_text);
        ind_other_details_award_name_text=findViewById(R.id.ind_other_details_award_name_text);
        ind_other_details_project_name_text=findViewById(R.id.ind_other_details_project_name_text);
        ind_other_details_services_offered_text=findViewById(R.id.ind_other_details_services_offered_text);



        //textview for for ind_proffestional details
        ind_professional_details_coa_reg_text=findViewById(R.id.ind_professional_details_coa_reg_text);
        ind_professional_details_yearsr_since_service_text=findViewById(R.id.ind_professional_details_yearsr_since_service_text);
        ind_professional_details_role_text=findViewById(R.id.ind_professional_details_role_text);
        ind_professional_details_company_firm_or_college_text=findViewById(R.id.ind_professional_details_company_firm_or_college_text);
        ind_professional_details_start_date_text=findViewById(R.id.ind_professional_details_start_date_text);
        ind_professional_details_end_date_of_working_currently_text=findViewById(R.id.ind_professional_details_end_date_of_working_currently_text);
        ind_professional_details_salary_text=findViewById(R.id.ind_professional_details_salary_text);


        //textview for ind_basic_details
        ind_basic_details_short_bio_text=findViewById(R.id.ind_basic_details_short_bio_text);
        ind_basic_details_dob_text=findViewById(R.id.ind_basic_details_dob_text);
        ind_basic_details_email_text=findViewById(R.id.ind_basic_details_email_text);
        ind_basic_details_gender_text=findViewById(R.id.ind_basic_details_gender_text);
        ind_basic_details_phone_number_text=findViewById(R.id.ind_basic_details_phone_number_text);
        ind_basic_details_role_text=findViewById(R.id.ind_basic_details_role_text);
        ind_basic_details_full_name_text=findViewById(R.id.ind_basic_details_full_name_text);




        employeemember_details=findViewById(R.id.employeemember_details);
        aboutFollowers = findViewById(R.id.about_followers);
        aboutFollowing = findViewById(R.id.about_following);
       // email = findViewById(R.id.about_email_text);
       // phoneNumber = findViewById(R.id.about_phone_number_text);
        personal_info=findViewById(R.id.personal_info);
        basic_details_firm=findViewById(R.id.basic_details_firm);
       // company_firm_details=findViewById(R.id.company_firm_details);
        employee_member_details=findViewById(R.id.employee_member_details);


        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
         userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        UserData userData= gson1.fromJson(json1, UserData.class);

        aboutName = findViewById(R.id.about_name);
        if(usertype.equals("work_individual"))
        {
            getDataFromServer(UtilsClass.baseurl+"profile/about_individual_user/");
        }
        else if(usertype.equals("work_architecture_college"))
        {

        }
        else {
            getDataFromServer(UtilsClass.baseurl+"profile/about_firm_user/");
        }

    }

    private void getDataFromServer(String url) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, url+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("ReponseFeed3", response);
                     //   Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(usertype.equals("work_individual"))
                            {
                                Work_Individuals_User_Details_Class work_individuals_user_details_class=new Work_Individuals_User_Details_Class(jsonObject.getJSONObject("about_work_individual"));
                                BindIndividualUserData(work_individuals_user_details_class);
                            }
                             else if(usertype.equals("work_architecture_college"))
                            {
                                //bind data to college class
                            }
                            else {
                                firmDetails firmDetails1=new firmDetails(jsonObject.getJSONObject("firm_details"));
                                BindFirmUserData(firmDetails1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {


                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.v("chat",res);
//
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }

        };

        requestQueue.add(myReq);

    }

    private void BindFirmUserData(firmDetails firmdetails) {
        personal_info.setVisibility(View.VISIBLE);

        employee_member_details.setVisibility(View.VISIBLE);
        basic_details_firm.setVisibility(View.VISIBLE);


        if(firmdetails.getFollowers()!=0)
        {
            aboutFollowers.setText(firmdetails.getFollowers()+"");
        }
        if(firmdetails.getFollowing()!=0)
        {
            aboutFollowing.setText(firmdetails.getFollowing()+"");
        }

        aboutName.setText(firmdetails.getName());

        if(firmdetails.getShort_bio()!=null && !firmdetails.getShort_bio().equals("null"))
       {
           about_name_short_bio.setText(firmdetails.getShort_bio());
       }
       if(firmdetails.getInstagram_link()!=null && !firmdetails.getInstagram_link().equals("null"))
       {
           basic_details_firm_instagram_text.setText(firmdetails.getInstagram_link());
       }
       if(firmdetails.getTypes_of_firm_company()!=null && !firmdetails.getTypes_of_firm_company().equals("null"))
       {
           basic_details_firm_company_type_text.setText(firmdetails.getTypes_of_firm_company());
       }
       if(firmdetails.getName()!=null && !firmdetails.getName().equals("null"))
       {
           basic_details_firm_company_name_text.setText(firmdetails.getName());
       }
       if(firmdetails.getLinkedin_link()!=null && !firmdetails.getLinkedin_link().equals("null"))
       {
           basic_details_firm_linkedin_text.setText(firmdetails.getLinkedin_link());
       }
       if(firmdetails.getTwitter_link()!=null && !firmdetails.getTwitter_link().equals("null"))
       {
           basic_details_firm_twitter_text.setText(firmdetails.getTwitter_link());
       }
       if(firmdetails.getFacebook_link()!=null && !firmdetails.getFacebook_link().equals("null"))
       {
           basic_details_firm_facebook_text.setText(firmdetails.getFacebook_link());
       }
       if(firmdetails.getFirm_or_company_registration_number()!=null && !firmdetails.getFirm_or_company_registration_number().equals("null"))
       {
           basic_details_firm_company_register_num_text.setText(firmdetails.getFirm_or_company_registration_number());
       }
       if(firmdetails.getShort_bio()!=null && !firmdetails.getShort_bio().equals("null"))
       {
           basic_details_firm_shot_bio_text.setText(firmdetails.getShort_bio());
       }
       if(firmdetails.getEmail()!=null && !firmdetails.getEmail().equals("null"))
       {
           basic_details_firm_email_text.setText(firmdetails.getEmail());
       }
       if(firmdetails.getMobile_number()!=null && !firmdetails.getMobile_number().equals("null"))
       {
           basic_details_firm_mobile_number_text.setText(firmdetails.getMobile_number());
       }
       if(firmdetails.getFirm_or_company_name()!=null && !firmdetails.getFirm_or_company_name().equals("null"))
       {
           basic_details_firm_nameofCompany_text.setText(firmdetails.getFirm_or_company_name());
       }

        if(firmdetails.getFirst_name()!=null && !firmdetails.getFirst_name().equals("null")&& firmdetails.getLast_name()!=null && !firmdetails.getLast_name().equals("null"))
        {
            employee_firm_full_name_text.setText(firmdetails.getFirst_name()+" "+firmdetails.getLast_name());
        }
        if(firmdetails.getRole()!=null && !firmdetails.getRole().equals("null"))
        {
            employee_firm_role_text.setText(firmdetails.getRole());
        }
        if(firmdetails.getPhone_number()!=null && !firmdetails.getPhone_number().equals("null"))
        {
            employee_firm_phone_number_text.setText(firmdetails.getPhone_number());
        }
        if(firmdetails.getAadhar_id()!=null && !firmdetails.getAadhar_id().equals("null"))
        {
            employee_firm_aadhaar_id_text.setText(firmdetails.getAadhar_id());
        }
        if(firmdetails.getEmployee_email()!=null && !firmdetails.getEmployee_email().equals("null"))
        {
            employee_firm_email_text.setText(firmdetails.getEmployee_email());
        }
        if(firmdetails.getEmployee_city()!=null && !firmdetails.getEmployee_city().equals("null"));
        {
            employee_firm_city_text.setText(firmdetails.getEmployee_city()+"");
        }
        if(firmdetails.getEmployee_state()!=null && !firmdetails.getEmployee_state().equals("null"))
        {
            employee_firm_state_text.setText(firmdetails.getEmployee_state()+"");
        }
        if(firmdetails.getEmployee_country()!=null && !firmdetails.getEmployee_country().equals("null"))
        {
            employee_firm_country_text.setText(firmdetails.getEmployee_country()+"");
        }

    }

    private void BindIndividualUserData(Work_Individuals_User_Details_Class work_individuals_user_details_class) {
        ind_basic_details.setVisibility(View.VISIBLE);
        ind_professional_details.setVisibility(View.VISIBLE);
        ind_other_details.setVisibility(View.VISIBLE);
        ind_education.setVisibility(View.VISIBLE);
        personal_info.setVisibility(View.VISIBLE);


        if(!work_individuals_user_details_class.getFollowerCount().equals("null"))
        {
            aboutFollowers.setText(work_individuals_user_details_class.getFollowerCount());
        }
        if(!work_individuals_user_details_class.getFollowingCount().equals("null"))
        {
            aboutFollowing.setText(work_individuals_user_details_class.getFollowerCount());
        }
        aboutName.setText(work_individuals_user_details_class.getFull_name());
        if(work_individuals_user_details_class.getShort_bio()!=null && !work_individuals_user_details_class.getShort_bio().equals("null"))
        {
            about_name_short_bio.setText(work_individuals_user_details_class.getShort_bio());
        }
        if(work_individuals_user_details_class.getShort_bio()!=null && !work_individuals_user_details_class.getShort_bio().equals("null"))
        {
            ind_basic_details_short_bio_text.setText(work_individuals_user_details_class.getShort_bio());
        }

        if(work_individuals_user_details_class.getDate_of_birth()!=null && !work_individuals_user_details_class.getDate_of_birth().equals("null"))
        {
            ind_basic_details_dob_text.setText(work_individuals_user_details_class.getDate_of_birth());
        }
        if(work_individuals_user_details_class.getEmail()!=null && !work_individuals_user_details_class.getEmail().equals("null"))
        {
            ind_basic_details_email_text.setText(work_individuals_user_details_class.getEmail());
        }
        if(work_individuals_user_details_class.getGender()!=null && !work_individuals_user_details_class.getGender().equals("null"))
        {
            ind_basic_details_gender_text.setText(work_individuals_user_details_class.getGender());
        }
        if(work_individuals_user_details_class.getPhone_number()!=null && !work_individuals_user_details_class.getPhone_number().equals("null"))
        {
            ind_basic_details_phone_number_text.setText(work_individuals_user_details_class.getPhone_number());
        }
        if(work_individuals_user_details_class.getOccupation()!=null && !work_individuals_user_details_class.getOccupation().equals("null"))
        {
            ind_basic_details_role_text.setText(work_individuals_user_details_class.getOccupation());
        }
        if(work_individuals_user_details_class.getFull_name()!=null && !work_individuals_user_details_class.getFull_name().equals("null"))
        {
            ind_basic_details_full_name_text.setText(work_individuals_user_details_class.getFull_name());
        }


        if(work_individuals_user_details_class.getRole()!=null && !work_individuals_user_details_class.getRole().equals("null"))
        {
            ind_professional_details_role_text.setText(work_individuals_user_details_class.getRole());
        }
        if(work_individuals_user_details_class.getCompany_firm_or_college_university()!=null && !work_individuals_user_details_class.getCompany_firm_or_college_university().equals("null"))
        {
            ind_professional_details_company_firm_or_college_text.setText(work_individuals_user_details_class.getCompany_firm_or_college_university());
        }
        if(work_individuals_user_details_class.getStart_date()!=null && !work_individuals_user_details_class.getStart_date().equals("null"))
        {
            ind_professional_details_start_date_text.setText(work_individuals_user_details_class.getStart_date());
        }
        if(work_individuals_user_details_class.getEnd_date_of_working_currently()!=null && !work_individuals_user_details_class.getEnd_date_of_working_currently().equals("null"))
        {
            ind_professional_details_end_date_of_working_currently_text.setText(work_individuals_user_details_class.getEnd_date_of_working_currently());
        }
        if(work_individuals_user_details_class.getSalary_stripend()!=null && !work_individuals_user_details_class.getSalary_stripend().equals("null"))
        {
            ind_professional_details_salary_text.setText(work_individuals_user_details_class.getSalary_stripend());
        }
        if(work_individuals_user_details_class.getYears_since_service()!=null && !work_individuals_user_details_class.getYears_since_service().equals("null"))
        {
            ind_professional_details_yearsr_since_service_text.setText(work_individuals_user_details_class.getYears_since_service());
        }
       // private TextView ind_other_details_text,ind_other_details_award_name_text,ind_other_details_project_name_text,ind_other_details_services_offered_text;

        if(work_individuals_user_details_class.getAward()!=null && !work_individuals_user_details_class.getAward().equals("null"))
        {
            ind_other_details_text.setText(work_individuals_user_details_class.getAward());
        }
        if(work_individuals_user_details_class.getAward_name()!=null && !work_individuals_user_details_class.getAward_name().equals("null"))
        {
            ind_other_details_award_name_text.setText(work_individuals_user_details_class.getAward_name());
        }
        if(work_individuals_user_details_class.getProject_name()!=null && !work_individuals_user_details_class.getProject_name().equals("null"))
        {
            ind_other_details_project_name_text.setText(work_individuals_user_details_class.getProject_name());
        }
        if(work_individuals_user_details_class.getServices_offered()!=null && !work_individuals_user_details_class.getServices_offered().equals("null"))
        {
            ind_other_details_services_offered_text.setText(work_individuals_user_details_class.getServices_offered());
        }


        if(work_individuals_user_details_class.getCollege_university()!=null && !work_individuals_user_details_class.getCollege_university().equals("null"))
        {
            ind_education_college_name_text.setText(work_individuals_user_details_class.getCollege_university());
        }
        if(work_individuals_user_details_class.getYear_of_admission()!=null && !work_individuals_user_details_class.getYear_of_admission().equals("null"))
        {
            ind_education_startdate_enddate.setText(work_individuals_user_details_class.getYear_of_admission());
            if(!work_individuals_user_details_class.getYear_of_graduation().equals("null"))
            {
                ind_education_startdate_enddate.setText(work_individuals_user_details_class.getYear_of_admission()+"-"+work_individuals_user_details_class.getYear_of_graduation());
            }
        }
        if(work_individuals_user_details_class.getCourse()!=null && !work_individuals_user_details_class.getCourse().equals("null"))
        {
            ind_education_branch.setText(work_individuals_user_details_class.getCourse());
        }

        }
}

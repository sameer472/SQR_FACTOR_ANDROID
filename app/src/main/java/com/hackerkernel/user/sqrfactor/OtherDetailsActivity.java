package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;
import com.hackerkernel.user.sqrfactor.Pojo.allOtherDetailsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtherDetailsActivity extends AppCompatActivity {
    private LinearLayout newForm;
    private Button add,remove,save_otherDetails;
    private TextInputLayout startDate,endDate;
    private Boolean isClicked= false;
    private Toolbar toolbar;
    private EditText awardsText,awardsNameText,projectText,service_offered,awardsText1,awardsNameText1,projectText1,service_offered1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherdetails);

        toolbar = (Toolbar) findViewById(R.id.other_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        add=(Button)findViewById(R.id.Add_otherDetails);
        remove=(Button)findViewById(R.id.Remove_other);
        newForm=(LinearLayout) findViewById(R.id.linear_other);

        awardsText=(EditText)findViewById(R.id.awardsText);
        awardsText1=(EditText)findViewById(R.id.awardsText1);
        awardsNameText=(EditText)findViewById(R.id.awardsNameText);
        awardsNameText1=(EditText)findViewById(R.id.awardsNameText1);
        projectText=(EditText)findViewById(R.id.projectText);
        projectText1=(EditText)findViewById(R.id.projectText1);
        service_offered=(EditText)findViewById(R.id.other_service_offered);
        service_offered1=(EditText)findViewById(R.id.service_offered1);

        save_otherDetails=(Button)findViewById(R.id.save_otherDetails);
        save_otherDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(awardsText.getText().toString()))
                {
                    MDToast.makeText(OtherDetailsActivity.this, "Awards is Required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(awardsNameText.getText().toString()))
                {
                    MDToast.makeText(OtherDetailsActivity.this, "Awards Name is Required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(projectText.getText().toString()))
                {
                    MDToast.makeText(OtherDetailsActivity.this, "Project Name is Required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
                else if(TextUtils.isEmpty(service_offered.getText().toString()))
                {
                    MDToast.makeText(OtherDetailsActivity.this, "Services Offered is Required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
                else {
                    SaveDataTOServer();
                }
//


            }
        });

        newForm.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked)
                {
                    newForm.setVisibility(View.VISIBLE);
                    isClicked=true;
                }

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm!=null && isClicked)
                {
                    newForm.setVisibility(View.GONE);
                    isClicked=false;
                }
            }
        });

        BindDataToViewFromServer();
    }

    private void BindDataToViewFromServer() {


        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"profile/edit/other-details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);
//                        Toast.makeText(OtherDetailsActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray allOtherDetails=jsonObject.getJSONArray("allOtherDetail");

                            for(int i=0;i<allOtherDetails.length();i++)
                            {
                               allOtherDetailsClass allOtherDetailsClass1=new allOtherDetailsClass(allOtherDetails.getJSONObject(i));
                                if(i==0)
                                {
                                    awardsText.setText(allOtherDetailsClass1.getAward());
                                    awardsNameText.setText(allOtherDetailsClass1.getAward_name());
                                    projectText.setText(allOtherDetailsClass1.getProject_name());
                                    service_offered.setText(allOtherDetailsClass1.getServices_offered());
                                }
                                if (i==1)
                                {
                                    newForm.setVisibility(View.VISIBLE);
                                    awardsText1.setText(allOtherDetailsClass1.getAward());
                                    awardsNameText1.setText(allOtherDetailsClass1.getAward_name());
                                    projectText1.setText(allOtherDetailsClass1.getProject_name());
                                    service_offered1.setText(allOtherDetailsClass1.getServices_offered());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SaveDataTOServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"parse/work-other-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.v("ResponseLike",s);
//                        Toast.makeText(OtherDetailsActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            awardsNameText.setText("");
                            awardsNameText1.setText("");
                            awardsText.setText("");
                            awardsText1.setText("");
                            projectText.setText("");
                            projectText1.setText("");
                            service_offered.setText("");
                            service_offered1.setText("");
                            Intent intent=new Intent(OtherDetailsActivity.this,ProfileActivity.class);
                            startActivity(intent);

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


                params.put("award[0]",awardsText.getText().toString());
               // params.put("award[1]",awardsText1.getText().toString());
                params.put("award_name[0]",awardsNameText.getText().toString());
               // params.put("award_name[1]",awardsNameText1.getText().toString());
                params.put("project_name[0]",projectText.getText().toString());
               // params.put("project_name[1]",projectText1.getText().toString());
                params.put("services_offered[0]",service_offered.getText().toString());

                if(!TextUtils.isEmpty(awardsText1.getText().toString()))
                {
                    params.put("award[1]",awardsNameText1.getText().toString());
                }

                if(!TextUtils.isEmpty(awardsNameText1.getText().toString()))
                {
                    params.put("award_name[1]",awardsNameText1.getText().toString());
                }
//


                if(!TextUtils.isEmpty(projectText1.getText().toString()))
                {
                    params.put("project_name[1]",projectText1.getText().toString());
                }




                if(!TextUtils.isEmpty(service_offered1.getText().toString()))
                {
                    params.put("services_offered[1]",service_offered1.getText().toString());
                }



                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);
    }

}

package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.zxing.client.result.EmailAddressParsedResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeMemberDetails extends AppCompatActivity {
    Toolbar toolbar;
    private EditText firstName,lastName,role,phoneNumber,aadhaarId,email;
    private TextView employeeAttachment;
    private Button employeeAddbtn;
    private int countryId=1;
    private UserData userData;
    private String image;
    private ImageView attachedImage,attachment;
    private Spinner employee_CountrySpinner,employee_StateSpinner,employee_CitySpinner;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList=new ArrayList<>();
    private ArrayList<String> statesName=new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList=new ArrayList<>();
    private ArrayList<String> citiesName=new ArrayList<>();
    private int CountryID=1,StateID=1,CityID=1,actualCityID,actualStateID,actualCountryId;
    private String country_val=1+"",state_val=1+"",city_val=1+"",gender_val=null,country_name,state_name,city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_member_details);

        toolbar = (Toolbar) findViewById(R.id.employee_details_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        employee_CountrySpinner=(Spinner)findViewById(R.id.employee_Country);
        employee_StateSpinner=(Spinner)findViewById(R.id.employee_State);
        employee_CitySpinner=(Spinner)findViewById(R.id.employee_City);
        employeeAttachment=(TextView)findViewById(R.id.employee_attachment);
        attachedImage=(ImageView)findViewById(R.id.employee_attachment_image);
        attachment = (ImageView)findViewById(R.id.employee_attachment_icon);
        //attachmentImage = (ImageView)findViewById(R.id.employee_attachment_image);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        firstName =(EditText)findViewById(R.id.employee_firstName_text);
        lastName =(EditText)findViewById(R.id.employee_firstLast_text);
        role =(EditText)findViewById(R.id.employee_role_text);
        phoneNumber =(EditText)findViewById(R.id.employee_number_text);
        aadhaarId =(EditText)findViewById(R.id.employee_aadhaar_text);
        email =(EditText)findViewById(R.id.employee_email_text);
        employeeAddbtn =(Button)findViewById(R.id.employee_add_button);

        employeeAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(firstName.getText().toString()))
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "First name is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(lastName.getText().toString()))
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "Last name is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(role.getText().toString()))
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "role field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(phoneNumber.getText().toString()))
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "Mobile No. field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(TextUtils.isEmpty(email.getText().toString()))
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "Email field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(country_val==null)
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "Country field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();

                }
                else if(state_val==null)
                {

                    MDToast.makeText(EmployeeMemberDetails.this, "State field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                else if(city_val==null)
                {
                    MDToast.makeText(EmployeeMemberDetails.this, "City field is required", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }

                else {
                    SendDataToServer();

                         }
            }
        });

        employee_CountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {

                if(countryName.size()>0)
                {
                    country_name = countryName.get(position);
                    Log.v("countryPositon",position+"");
                    country_val=position+1+"";

//
                    LoadStateFromServer();

                }



            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        employee_StateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(statesName.size()>0)
                {
                    state_name = statesName.get(position);
                    state_val=actualStateID+position+"";
                    LoadCitiesFromServer();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        employee_CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if (citiesName.size()>0)
                {

                    city_name = citiesName.get(position);
                    city_val=actualCityID+position+"";


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });


   LoadCountryFromServer();
   LoadStateFromServer();
   LoadCitiesFromServer();



    }

    public void SendDataToServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        Toast.makeText(EmployeeMemberDetails.this,"calling",Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/add/member-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("employess",s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            if(jsonObject.has("return"))
                            {
                                MDToast.makeText(EmployeeMemberDetails.this, "Phone number or Email has already been taken", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                            }

                            else {
                                firstName.setText("");
                                lastName.setText("");
                                role.setText("");
                                phoneNumber.setText("");
                                aadhaarId.setText("");
                                email.setText("");
                                MDToast.makeText(EmployeeMemberDetails.this, "Member details added Successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
                                Intent intent =new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(intent);
                                finish();
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

                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
//
                                e1.printStackTrace();
                            } catch (JSONException e2) {
//
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

                Log.v("emp",image);
                Log.v("emp1",firstName.getText().toString());
                Log.v("emp2",lastName.getText().toString());
                Log.v("emp3",aadhaarId.getText().toString());
                Log.v("emp4",phoneNumber.getText().toString());
                Log.v("emp5",email.getText().toString());
                Log.v("emp6",role.getText().toString());
                Log.v("emp7",country_val+state_val+city_val);

//
                params.put("first_name",firstName.getText().toString());
                params.put("last_name",lastName.getText().toString());
                params.put("country",country_val+"");
                params.put("profile","data:image/jpeg;base64,"+image);
                params.put("state",state_val+"");
                params.put("city",city_val+"");
                params.put("role",role.getText().toString());
                params.put("phone_number",phoneNumber.getText().toString());
                params.put("aadhar_id",aadhaarId.getText().toString());
                params.put("email",email.getText().toString());

                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }


    public void LoadCountryFromServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
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
                            Log.v("base country",countryClassArrayList.get(0).getId()+" ");
                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,countryName);
                            employee_CountrySpinner.setAdapter(spin_adapter1);





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
                        Log.v("ResponseLike",s);
                        // Toast.makeText(BasicDetails.this,"res"+s,Toast.LENGTH_LONG).show();

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
                            if(statesClassArrayList!=null && statesClassArrayList.size()>0)
                            {
                                Log.v("base statecoe",statesClassArrayList.get(0).getId()+" ");
                                actualStateID=statesClassArrayList.get(0).getId();
                                StateID=StateID-statesClassArrayList.get(0).getId();

                            }



                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,statesName);
                            employee_StateSpinner.setAdapter(spin_adapter2);

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
                params.put("country",country_val);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

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
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,citiesName);
                            employee_CitySpinner.setAdapter(spin_adapter3);

                            if(citiesClassArrayList!=null&& citiesClassArrayList.size()>0)
                            {
                                Log.v("city base",citiesClassArrayList.get(0).getId()+" ");
                                actualCityID=citiesClassArrayList.get(0).getId();
                                CityID=CityID-citiesClassArrayList.get(0).getId();
                                Log.v("cityId",CityID+"");

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
                params.put("state",state_val);
                params.put("country",country_val);
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

    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }




    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);

                    image = getStringImage(bitmap);
                    //params.put("profile_image","data:image/jpeg;base64,"+image );


                    attachedImage.setImageBitmap(bitmap);



                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    employeeAttachment.setText(String.valueOf(System.currentTimeMillis()) + ".jpg");


                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();

                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {



                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                String[] fileName = picturePath.split("/");
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", fileName[fileName.length-1]+"");

                image=getStringImage(thumbnail);
                attachedImage.setImageBitmap(thumbnail);
                employeeAttachment.setText(fileName[fileName.length-1]+"");

            }

        }

    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }
}
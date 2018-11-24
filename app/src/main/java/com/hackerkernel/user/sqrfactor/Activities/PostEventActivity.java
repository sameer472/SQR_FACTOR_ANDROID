package com.hackerkernel.user.sqrfactor.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PostEventActivity extends AppCompatActivity {
    private static final String TAG = "PostEventActivity";

    private static final int REQUEST_COVER_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CHOSEN_IMAGE_CAPTURE = 3;

    private static final int RESULT_COVER_IMAGE = 888;
    private static final int RESULT_CHOSEN_IMAGE = 2;

    private static final int REQUEST_PERMISSION_CAMERA = 4;
    private static final int REQUEST_PERMISSION_CAMERA2 = 5;

    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 6;
    private static final int READ_EXTERNAL_STORAGE2 = 7;


    private Toolbar mToolbar;

    private ProgressBar mPb;
    private LinearLayout mContentLayout;

    private EditText mAttachEditText;
    private EditText mEventTitle;
    private EditText mDescEditText;
    private EditText mVenueEditText;

    private Button mNextButton;

//    private Spinner mCountrySpinner;
    private SearchableSpinner mCountrySpinner;
    private SearchableSpinner mStateSpinner;
    private SearchableSpinner mCitySpinner;

    private LinearLayout mPaths;

    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<HashMap> countriesMapList = new ArrayList<>();
    private ArrayList<String> states = new ArrayList<>();
    private ArrayList<HashMap> statesMapList = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<HashMap> citiesMapList = new ArrayList<>();


    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private String selectedCoverImagePath;
    private ArrayList<String> imagePaths;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        RelativeLayout mChooseImageButton = findViewById(R.id.event_add_choose_file);

        mCountrySpinner = findViewById(R.id.event_add_country);
        mStateSpinner = findViewById(R.id.event_add_state);
        mCitySpinner = findViewById(R.id.event_add_city);


        mAttachEditText = findViewById(R.id.event_add_attach);
        mDescEditText = findViewById(R.id.event_add_description);
        mVenueEditText = findViewById(R.id.event_add_venue);

        mPaths = findViewById(R.id.paths);

        mEventTitle = findViewById(R.id.event_add_title);

        mNextButton = findViewById(R.id.event_add_next);
        imagePaths = new ArrayList<>();

        mSp = MySharedPreferences.getInstance(this);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        isStoragePermissionGranted();

        //for countries
        getCountryList();

        mChooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(PostEventActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Take Photo")) {
//                                isStoragePermissionGranted();

                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                            startActivityForResult(takePictureIntent,REQUEST_CHOSEN_IMAGE_CAPTURE);

                                        } else {
                                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                                Toast.makeText(PostEventActivity.this, "This permission is needed to capture your cover image", Toast.LENGTH_SHORT).show();
                                            }
                                            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA2);
                                        }
                                    } else {
                                        startActivityForResult(takePictureIntent, REQUEST_CHOSEN_IMAGE_CAPTURE);
                                    }


                                } else {
                                    Toast.makeText(PostEventActivity.this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (items[item].equals("Choose from Gallery")) {

                                Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        chooseImageFromGallery(chooseImageIntent);

                                    } else {
                                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                            Toast.makeText(PostEventActivity.this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                                        }
                                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE2);
                                    }
                                } else {
                                    chooseImageFromGallery(chooseImageIntent);
                                }
                            }
                            else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
//                }
            }
        });

        mAttachEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseImageDialog();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mEventTitle.getText().toString().isEmpty() &&
                        !mDescEditText.getText().toString().isEmpty() &&
                        !mVenueEditText.getText().toString().isEmpty() &&
                        !mAttachEditText.getText().toString().matches("Attach image") &&
                        !imagePaths.isEmpty()){
                    sendPostRequestUsingMultipart(selectedCoverImagePath,imagePaths);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Form Incomplete !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //when any country is selected
        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idk = (String)countriesMapList.get(position).get("id");
                //for states
                getStatesList(idk);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //when any state is selected
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) statesMapList.get(i).get("id");
                //for cities
                getCitiesList(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void addPathView(String imagePath) {

        final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_more_layout,null);
        final TextView textView = addMoreLayout.findViewById(R.id.addedPath);
        textView.setText(displayName);
        final ImageButton ib = addMoreLayout.findViewById(R.id.removePath);
        mPaths.addView(addMoreLayout);
        ib.setTag(imagePath);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaths.removeView(addMoreLayout);

                Log.d(TAG, "imagepath: " + (ib.getTag()));
                imagePaths.remove(ib.getTag());
                Log.d(TAG, "onClick: image paths size = "+ imagePaths.size());
                Log.d(TAG, "onClick image path = " + imagePaths.toString());
            }
        });
    }


    private TextView createNewTextView(String text) {
        LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_more_layout,null);
        final TextView textView = addMoreLayout.findViewById(R.id.addedPath);

        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private void showChooseImageDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Log.d(TAG, "onClick: Take Photo clicked");
                    takePhotoIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    chooseImageIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImageIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                chooseCoverImageFromGallery(chooseImageIntent);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
        }
        else {
            chooseCoverImageFromGallery(chooseImageIntent);
        }
    }


    private void takePhotoIntent() {

//        isStoragePermissionGranted();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(takePictureIntent, REQUEST_COVER_IMAGE_CAPTURE);

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "This permission is needed to capture your cover image", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                }
            } else {
                startActivityForResult(takePictureIntent,REQUEST_COVER_IMAGE_CAPTURE);
            }

        } else {
            Toast.makeText(this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
        }

    }



    private void getCountryList(){
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
                        HashMap<String,String> map = new HashMap<>();
                        map.put("id",cId);
                        map.put("name",cName);
                        countriesMapList.add(map);
                    }

                    ArrayAdapter<String> country_adapter = new ArrayAdapter<>(PostEventActivity.this, android.R.layout.simple_list_item_1, countries);
                    mCountrySpinner.setAdapter(country_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }){
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

    private void getStatesList(final String c){
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
                        HashMap<String,String> map = new HashMap<>();
                        map.put("id",sId);
                        map.put("name",sName);
                        statesMapList.add(map);
                    }

                    ArrayAdapter<String> state_adapter = new ArrayAdapter<>(PostEventActivity.this, android.R.layout.simple_list_item_1, states);
                    mStateSpinner.setAdapter(state_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }){
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
                params.put("country",c);
                return params;
            }
        };
        requestState.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(requestState);
    }

    private void getCitiesList(final String s){
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
                        HashMap<String,String> map = new HashMap<>();
                        map.put("id",cId);
                        map.put("name",cName);
                        citiesMapList.add(map);
                    }

                    ArrayAdapter<String> city_adapter = new ArrayAdapter<>(PostEventActivity.this, android.R.layout.simple_list_item_1, cities);
                    mCitySpinner.setAdapter(city_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO:: handle
            }
        }){
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

    private void sendPostRequestUsingMultipart(String coverImagePath, List<String> imagePath) {

        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT);
            return;
        }


        try {
            final String uploadId = UUID.randomUUID().toString();

            //getting id from the hash maps
            String id_country = countriesMapList.get((int) mCountrySpinner.getSelectedItemId()).get("id").toString();
            String id_state = statesMapList.get((int) mStateSpinner.getSelectedItemId()).get("id").toString();
            String id_city = citiesMapList.get((int) mCitySpinner.getSelectedItemId()).get("id").toString();

            //initializeSSLContext(getCallingActivity());
            final MultipartUploadRequest request = new MultipartUploadRequest(this, uploadId, ServerConstants.EVENT_ADD);
            request.addParameter("event_title",mEventTitle.getText().toString())
                    .addParameter("description",mDescEditText.getText().toString())
                    .addParameter("venue",mVenueEditText.getText().toString())
                    .addParameter("country",id_country.replace("\"",""))
                    .addParameter("state",id_state.replace("\"",""))
                    .addParameter("city",id_city.replace("\"",""))
                    .addFileToUpload(coverImagePath,"cover_image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {

                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.i(TAG, "onProgress: ");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.d(TAG, "onError: called");
                            Log.d(TAG, "onError: exception = " + exception.toString());

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.d(TAG, "onCompleted: upload successful");
                            Log.d(TAG, "onCompleted: " + serverResponse.getBodyAsString());

                            Toast.makeText(getApplicationContext(), "Event added successfully. Moving to next step", Toast.LENGTH_SHORT).show();

                            String sl;
                            try {
                                JSONObject responseObject = new JSONObject(serverResponse.getBodyAsString());
                                sl = responseObject.getString("redirecturl");
                                startActivity(new Intent(context, PostEvent2Activity.class).putExtra("slug",sl));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.i(TAG, "onCancelled: ");
                        }
                    });
            for(int k = 0; k<imagePath.size();k++){
                request.addFileToUpload(imagePath.get(k),"image[" + k + "]");
            }

            request.addHeader("Authorization", Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY))
                    .addHeader("Accept", "application/json")
                    .startUpload();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_COVER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            selectedCoverImagePath = FileUtils.getPath(this, uri);

            File myFile = new File(uri.toString());
            String path = myFile.getAbsolutePath();
            displayName = null;
            if (uri.toString().startsWith("content://")) {
                try (Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("onResultpathIF: ", displayName);

                    }
                }
            } else if (uri.toString().startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("onResultpathELSEIF: ", path);
            }
            mAttachEditText.setText(displayName);
        }
        if (requestCode == RESULT_CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //String selectedImagePath = ImageUtils.getPath(this, uri);
            String selectedImagePath = FileUtils.getPath(PostEventActivity.this, uri);

            File myFile = new File(uri.toString());
            String path = myFile.getAbsolutePath();
            displayName = null;
            if (uri.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("onResultpathIF: ", displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uri.toString().startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("onResultpathELSEIF: ", path);
            }
            addPathView(selectedImagePath);
            imagePaths.add(selectedImagePath);
        }

        //for captured image from choose button
        if (requestCode == REQUEST_CHOSEN_IMAGE_CAPTURE && resultCode == RESULT_OK /* && data!=null && data.getData() != null */){

            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(PostEventActivity.this, photo);


            String capturedImagePath = FileUtils.getPath(PostEventActivity.this, tempUri);
            File myFile = new File(tempUri.toString());
            String path = myFile.getAbsolutePath();
            displayName = null;
            if (tempUri.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(tempUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("onResultpathIF: ", displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (tempUri.toString().startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("onResultpathELSEIF: ", path);
            }
            addPathView(capturedImagePath);
            imagePaths.add(capturedImagePath);
        }

        //for captured image from attach button
        if (requestCode == REQUEST_COVER_IMAGE_CAPTURE && resultCode == RESULT_OK /* && data!=null && data.getData() != null */){

            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(PostEventActivity.this, photo);

            selectedCoverImagePath = FileUtils.getPath(PostEventActivity.this, tempUri);
            File myFile = new File(tempUri.toString());
            String path = myFile.getAbsolutePath();
            displayName = null;
            if (tempUri.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(tempUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("onResultpathIF: ", displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (tempUri.toString().startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("onResultpathELSEIF: ", path);
            }
             mAttachEditText.setText(displayName);
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Toast.makeText(this, "getImageUri: ======"+ path , Toast.LENGTH_SHORT).show();
        return Uri.parse(path);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "onRequestPermissionsResult: permission " + i + "= " + permissions[i]);
        }

        if (requestCode == REQUEST_PERMISSION_CAMERA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_COVER_IMAGE_CAPTURE);

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == REQUEST_PERMISSION_CAMERA2) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQUEST_CHOSEN_IMAGE_CAPTURE);

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseImageFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

            } else {
                Toast.makeText(this, "Permission to choose image denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseCoverImageFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

            } else {
                Toast.makeText(this, "Permission to choose image denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void chooseImageFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent,RESULT_CHOSEN_IMAGE);
    }

    private void chooseCoverImageFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, RESULT_COVER_IMAGE);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}



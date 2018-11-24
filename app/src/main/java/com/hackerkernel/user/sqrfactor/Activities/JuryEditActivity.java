package com.hackerkernel.user.sqrfactor.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Adapters.UserArrayAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.Jury;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JuryEditActivity extends AppCompatActivity {
    private static final String TAG = "JuryEditActivity";

    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CHOOSE_JURY_IMAGE = 2;

    private Toolbar mToolbar;
    private LinearLayout mParentLayout;
    private List<LinearLayout> mAddMoreLayouts;
    private String mSelectedImagePath;
    private TextView mSelectedJuryImageTV;

    private RequestQueue mRequestQueue;
    private MySharedPreferences mSp;
    private ProgressBar mPb;
    private LinearLayout mContentLayout;

    private Button mAddMoreButton;
    private Button mSaveButton;
    private UserArrayAdapter mUserAddMoreAdapter;
    private String mSlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jury_edit);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mAddMoreButton = findViewById(R.id.jury_edit_add_more);
        mSaveButton = findViewById(R.id.jury_edit_save);

        mAddMoreLayouts = new ArrayList<>();

        mParentLayout = findViewById(R.id.jury_edit_parent_layout);

        mSp = MySharedPreferences.getInstance(this);
        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        mSlug = getIntent().getStringExtra(BundleConstants.SLUG);

        String juryArrayString = getIntent().getStringExtra(BundleConstants.JURY_ARRAY);

        try {
            JSONArray juryArray = new JSONArray(juryArrayString);

            for (int i = 0; i < juryArray.length(); i++) {
                JSONObject singleObject = juryArray.getJSONObject(i);
                if (singleObject.has("user")) {

                    if (!singleObject.getString("user").equals("null")) {
                        JSONObject userObject = singleObject.getJSONObject("user");
                        Log.d(TAG, "userObject: " + userObject.toString());

                        String fullName = singleObject.getString("jury_fullname");
                        String company = singleObject.getString("jury_firm_company");
                        String email = singleObject.getString("jury_email");
                        String phoneNumber = singleObject.getString("jury_contact");

                        Log.d(TAG, "onCreate: jury name = " + fullName);
                        Log.d(TAG, "onCreate: jury company = " + company);
                        Log.d(TAG, "onCreate: jury email = " + email);
                        Log.d(TAG, "onCreate: jury phone number = " + phoneNumber);

                        final LinearLayout juryLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.jury_layout, null);
                        EditText juryNameET = juryLayout.findViewById(R.id.jury_name);
                        EditText juryCompanyET = juryLayout.findViewById(R.id.jury_company);
                        EditText juryEmailET = juryLayout.findViewById(R.id.jury_email);
                        EditText juryContactET = juryLayout.findViewById(R.id.jury_contact);
                        Button chooseFileButton = juryLayout.findViewById(R.id.jury_image);
                        final TextView juryImagePathTV = juryLayout.findViewById(R.id.jury_image_path);
                        TextView juryRemoveTV = juryLayout.findViewById(R.id.jury_remove);

                        juryNameET.setText(fullName);
                        juryCompanyET.setText(company);
                        juryEmailET.setText(email);
                        juryContactET.setText(phoneNumber);

                        chooseFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSelectedJuryImageTV = juryImagePathTV;
                                chooseImageIntent();
                            }
                        });

                        juryRemoveTV.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                mParentLayout.removeView(juryLayout);
                                mAddMoreLayouts.remove(juryLayout);
                            }
                        });

                        mParentLayout.addView(juryLayout);
                        mAddMoreLayouts.add(juryLayout);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAddMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.jury_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.jury_remove);
                Button chooseFileBtn = addMoreLayout.findViewById(R.id.jury_image);
                final TextView imagePathTV = addMoreLayout.findViewById(R.id.jury_image_path);
                final EditText juryEmailET = addMoreLayout.findViewById(R.id.jury_email);
                final EditText juryContactET = addMoreLayout.findViewById(R.id.jury_contact);

                final AppCompatAutoCompleteTextView juryNameTV = addMoreLayout.findViewById(R.id.jury_name);
                juryNameTV.setThreshold(1);

                String str = juryNameTV.getText().toString().trim();
                mUserAddMoreAdapter = new UserArrayAdapter(JuryEditActivity.this, R.layout.simple_text_view, str);
                juryNameTV.setAdapter(mUserAddMoreAdapter);

                juryNameTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        UserClass user = mUserAddMoreAdapter.getItem(pos);
                        juryEmailET.setText(user.getEmail());
                        juryContactET.setText(user.getMobileNumber());
                        imagePathTV.setText(user.getProfilePicPath());

                        juryNameTV.setText(user.getName());
                    }
                });

                chooseFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectedJuryImageTV = imagePathTV;
                        chooseImageIntent();
                    }
                });

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mParentLayout.removeView(addMoreLayout);
                        mAddMoreLayouts.remove(addMoreLayout);
                    }
                });
                mParentLayout.addView(addMoreLayout);
                mAddMoreLayouts.add(addMoreLayout);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Jury> juryList = new ArrayList<>();

                for (int j = 0; j < mAddMoreLayouts.size(); j++) {
                    LinearLayout layout = mAddMoreLayouts.get(j);
                    AppCompatAutoCompleteTextView juryNameET = layout.findViewById(R.id.jury_name);
                    EditText juryCompanyET = layout.findViewById(R.id.jury_company);
                    EditText juryEmailET = layout.findViewById(R.id.jury_email);
                    EditText juryContactET = layout.findViewById(R.id.jury_contact);
                    TextView juryImagePathTV = layout.findViewById(R.id.jury_image_path);

                    String juryName = juryNameET.getText().toString();
                    String juryCompany = juryCompanyET.getText().toString();
                    String juryEmail = juryEmailET.getText().toString();
                    String juryContact = juryContactET.getText().toString();
                    String juryImagePath = juryImagePathTV.getText().toString();

                    Jury jury = new Jury(juryName, juryCompany, juryEmail, juryContact, juryImagePath);
                    juryList.add(jury);
                }

                juryUpdateApi(juryList);
            }
        });
    }

    private void juryUpdateApi(final List<Jury> juryList) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.JURY_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: jury update response = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, JuryEditActivity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                Log.d(TAG, "getHeaders: api key = " + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("users_competition_slug", mSlug);

                for (int i = 0; i < juryList.size(); i++) {
                    Jury jury = juryList.get(i);

                    params.put("jury_fullname[" + i + "]", jury.getName());
                    params.put("jury_firm_company[" + i + "]", jury.getCompany());
                    params.put("jury_email[" + i + "]", jury.getEmail());
                    params.put("jury_contact[" + i + "]", jury.getContact());
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);


    }

    private void chooseImageIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                chooseJuryFromGallery(chooseImageIntent);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
        } else {
            chooseJuryFromGallery(chooseImageIntent);
        }

    }

    private void chooseJuryFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, REQUEST_CHOOSE_JURY_IMAGE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseJuryFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

            } else {
                Toast.makeText(this, "Permission to choose image denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: called");
        Log.d(TAG, "onActivityResult: request code = " + requestCode);
        Log.d(TAG, "onActivityResult: result code = " + resultCode);

        if (requestCode == REQUEST_CHOOSE_JURY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedImagePath);

            mSelectedJuryImageTV.setText(mSelectedImagePath);

        }
    }
}

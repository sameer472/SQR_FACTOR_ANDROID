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
import com.hackerkernel.user.sqrfactor.Pojo.Partner;
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

public class PartnerEditActivity extends AppCompatActivity {
    private static final String TAG = "PartnerEditActivity";

    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CHOOSE_JURY_IMAGE = 2;

    private Toolbar mToolbar;
    private LinearLayout mParentLayout;
    private List<LinearLayout> mAddMoreLayouts;
    private String mSelectedImagePath;
    private TextView mSelectedPartnerImageTV;

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
        setContentView(R.layout.activity_partner_edit);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mAddMoreButton = findViewById(R.id.partner_edit_add_more);
        mSaveButton = findViewById(R.id.partner_edit_save);

        mAddMoreLayouts = new ArrayList<>();

        mParentLayout = findViewById(R.id.partner_edit_parent_layout);

        mSp = MySharedPreferences.getInstance(this);
        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        mSlug = getIntent().getStringExtra(BundleConstants.SLUG);

        String partnerArrayString = getIntent().getStringExtra(BundleConstants.PARTNERS_ARRAY);

        try {
            JSONArray partnerArray = new JSONArray(partnerArrayString);

            for (int i = 0; i < partnerArray.length(); i++) {
                JSONObject singleObject = partnerArray.getJSONObject(i);
                if (singleObject.has("user")) {

                    if (!singleObject.getString("user").equals("null")) {
                        JSONObject userObject = singleObject.getJSONObject("user");
                        Log.d(TAG, "userObject: " + userObject.toString());

                        String fullName = singleObject.getString("partner_name");
                        String website = singleObject.getString("partner_website");
                        String email = singleObject.getString("partner_email");
                        String phoneNumber = singleObject.getString("partner_contact");

                        Log.d(TAG, "onCreate: partner name = " + fullName);
                        Log.d(TAG, "onCreate: partner company = " + website);
                        Log.d(TAG, "onCreate: partner email = " + email);
                        Log.d(TAG, "onCreate: partner phone number = " + phoneNumber);

                        final LinearLayout partnerLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.partner_layout, null);
                        EditText partnerNameET = partnerLayout.findViewById(R.id.partner_name);
                        EditText partnerWebsiteET = partnerLayout.findViewById(R.id.partner_website);
                        EditText partnerEmailET = partnerLayout.findViewById(R.id.partner_email);
                        EditText partnerContactET = partnerLayout.findViewById(R.id.partner_contact);
                        Button chooseFileButton = partnerLayout.findViewById(R.id.partner_image);
                        final TextView partnerImagePathTV = partnerLayout.findViewById(R.id.partner_image_path);
                        TextView partnerRemoveTV = partnerLayout.findViewById(R.id.partner_remove);

                        partnerNameET.setText(fullName);
                        partnerWebsiteET.setText(website);
                        partnerEmailET.setText(email);
                        partnerContactET.setText(phoneNumber);

                        chooseFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSelectedPartnerImageTV = partnerImagePathTV;
                                chooseImageIntent();
                            }
                        });

                        partnerRemoveTV.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                mParentLayout.removeView(partnerLayout);
                                mAddMoreLayouts.remove(partnerLayout);
                            }
                        });

                        mParentLayout.addView(partnerLayout);
                        mAddMoreLayouts.add(partnerLayout);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mAddMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.partner_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.partner_remove);
                Button chooseFileBtn = addMoreLayout.findViewById(R.id.partner_image);
                final TextView imagePathTV = addMoreLayout.findViewById(R.id.partner_image_path);

                EditText partnerSiteET = addMoreLayout.findViewById(R.id.partner_website);
                final EditText partnerEmailET = addMoreLayout.findViewById(R.id.partner_email);
                final EditText partnerContactET = addMoreLayout.findViewById(R.id.partner_contact);

                final AppCompatAutoCompleteTextView partnerNameTV = addMoreLayout.findViewById(R.id.partner_name);
                partnerNameTV.setThreshold(1);

                String strAddMore = partnerNameTV.getText().toString().trim();
                mUserAddMoreAdapter = new UserArrayAdapter(PartnerEditActivity.this, R.layout.simple_text_view, strAddMore);
                partnerNameTV.setAdapter(mUserAddMoreAdapter);


                partnerNameTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        UserClass user = mUserAddMoreAdapter.getItem(pos);
                        partnerEmailET.setText(user.getEmail());
                        partnerContactET.setText(user.getMobileNumber());
                        imagePathTV.setText(user.getProfilePicPath());

                        partnerNameTV.setText(user.getName());
                    }
                });

                chooseFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectedPartnerImageTV = imagePathTV;
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
                List<Partner> partnerList = new ArrayList<>();

                for (int j = 0; j < mAddMoreLayouts.size(); j++) {
                    LinearLayout layout = mAddMoreLayouts.get(j);
                    AppCompatAutoCompleteTextView partnerNameET = layout.findViewById(R.id.partner_name);
                    EditText partnerSiteET = layout.findViewById(R.id.partner_website);
                    EditText partnerEmailET = layout.findViewById(R.id.partner_email);
                    EditText partnerContactET = layout.findViewById(R.id.partner_contact);
                    TextView partnerImagePathTV = layout.findViewById(R.id.partner_image_path);

                    String partnerName = partnerNameET.getText().toString();
                    String partnerSite = partnerSiteET.getText().toString();
                    String partnerEmail = partnerEmailET.getText().toString();
                    String partnerContact = partnerContactET.getText().toString();
                    String partnerImagePath = partnerImagePathTV.getText().toString();

                    Partner partner = new Partner(partnerName, partnerSite, partnerEmail, partnerContact, partnerImagePath);
                    partnerList.add(partner);
                }

                partnerUpdateApi(partnerList);
            }
        });
    }

    private void partnerUpdateApi(final List<Partner> partnerList) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.PARTNER_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Log.d(TAG, "onResponse: partner update response = " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, PartnerEditActivity.this);
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

                for (int i = 0; i < partnerList.size(); i++) {
                    Partner partner = partnerList.get(i);

//                    params.put("user_competition_jury_id[" + i + "]", jury.getId());
                    params.put("partner_name[" + i + "]", partner.getName());
                    params.put("partner_website[" + i + "]", partner.getWebsite());
                    params.put("partner_email[" + i + "]", partner.getEmail());
                    params.put("partner_contact[" + i + "]", partner.getContact());
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
                choosePartnerFromGallery(chooseImageIntent);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
        } else {
            choosePartnerFromGallery(chooseImageIntent);
        }

    }

    private void choosePartnerFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, REQUEST_CHOOSE_JURY_IMAGE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                choosePartnerFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

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

            mSelectedPartnerImageTV.setText(mSelectedImagePath);

        }
    }
}

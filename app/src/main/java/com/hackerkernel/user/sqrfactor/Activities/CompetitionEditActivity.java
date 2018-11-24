package com.hackerkernel.user.sqrfactor.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hackerkernel.user.sqrfactor.Adapters.JuryEditAdapter;
import com.hackerkernel.user.sqrfactor.Adapters.PartnersEditAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.JuryClass;
import com.hackerkernel.user.sqrfactor.Pojo.PartnerClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CompetitionEditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CompetitionEditActivity";
    private static final int REQUEST_PERMISSION_CAMERA = 1;
    private static final int REQUEST_COVER_IMAGE_CAPTURE = 2;
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_CHOOSE_COVER_IMAGE = 4;

    private Toolbar mToolbar;

    private RequestQueue mRequestQueue;
    private MySharedPreferences mSp;
    private LinearLayout mContentLayout;

    private TextView mTitleTextView;
    private WebView mBriefWebView;
    private TextView mEligibilityTV;
    private ImageView mBriefEditIV;
    private RecyclerView mJuryRecyclerView;
    private RecyclerView mPartnersRecyclerView;
    private ImageView mCoverImageView;
    private List<JuryClass> mJuryList;
    private JuryEditAdapter mJuryAdapter;
    private List<PartnerClass> mPartners;
    private PartnersEditAdapter mPartnersAdapter;

    private LinearLayout mJuryEditLayout;
    private LinearLayout mPartnerEditLayout;

    private String mCurrentTitle;
    private String mCurrentBrief;
    private String mCurrentEligibilityCriteria;
    private String mCurrentCoverUrl;
    private String mSelectedCoverImagePath;
    private ImageView mCoverDialogImageView;
    private Uri mPhotoURI;
    private String mSlug;
    private ProgressBar mPb;
    private JSONArray mPartnersArray;
    private JSONArray mJuryArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_edit);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Edit Competition");

        mSp = MySharedPreferences.getInstance(this);
        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        mTitleTextView = findViewById(R.id.edit_comp_title);
        mBriefWebView = findViewById(R.id.edit_comp_brief);
        mBriefEditIV = findViewById(R.id.edit_comp_brief_edit);
        mEligibilityTV = findViewById(R.id.edit_comp_eligibility_criteria);
        mCoverImageView = findViewById(R.id.edit_comp_cover);
        mJuryEditLayout = findViewById(R.id.edit_comp_jury_edit_layout);
        mPartnerEditLayout = findViewById(R.id.edit_comp_partner_edit_layout);

        mJuryRecyclerView = findViewById(R.id.edit_comp_jury_rv);
        mPartnersRecyclerView = findViewById(R.id.edit_comp_partners_rv);

        mJuryRecyclerView.setHasFixedSize(true);
        mJuryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJuryRecyclerView.setNestedScrollingEnabled(false);

        mJuryList = new ArrayList<>();
        mJuryAdapter = new JuryEditAdapter(this, mJuryList);
        mJuryRecyclerView.setAdapter(mJuryAdapter);

        mPartnersRecyclerView.setHasFixedSize(true);
        mPartnersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPartnersRecyclerView.setNestedScrollingEnabled(false);

        mPartners = new ArrayList<>();
        mPartnersAdapter = new PartnersEditAdapter(this, mPartners);
        mPartnersRecyclerView.setAdapter(mPartnersAdapter);

        mSlug = getIntent().getStringExtra(BundleConstants.SLUG);

        competitionDetailApi(mSlug);

        mTitleTextView.setOnClickListener(this);
        mBriefWebView.setOnClickListener(this);
        mEligibilityTV.setOnClickListener(this);
        mCoverImageView.setOnClickListener(this);
        mBriefEditIV.setOnClickListener(this);

        mJuryEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompetitionEditActivity.this, JuryEditActivity.class);
                String juryArrayString = mJuryArray.toString();
                i.putExtra(BundleConstants.JURY_ARRAY, juryArrayString);
                i.putExtra(BundleConstants.SLUG, mSlug);
                startActivity(i);
            }
        });

        mPartnerEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompetitionEditActivity.this, PartnerEditActivity.class);
                String partnerArrayString = mPartnersArray.toString();
                i.putExtra(BundleConstants.PARTNERS_ARRAY, partnerArrayString);
                i.putExtra(BundleConstants.SLUG, mSlug);
                startActivity(i);
            }
        });
    }

    private void competitionDetailApi(String slug) {
        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        Log.d(TAG, "SHIVANI: url = " + ServerConstants.COMPETITION_DETAIL + slug);
        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.COMPETITION_DETAIL + slug, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: competition detail response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject competitionObj = responseObject.getJSONObject("competition");

                    mCurrentTitle = competitionObj.getString("competition_title");
                    mTitleTextView.setText(mCurrentTitle);

                    mCurrentBrief = competitionObj.getString("brief");
                    mBriefWebView.loadData(mCurrentBrief, "text/html", null);

                    mCurrentEligibilityCriteria = competitionObj.getString("eligibility_criteria");
                    mEligibilityTV.setText(mCurrentEligibilityCriteria);

                    mCurrentCoverUrl = competitionObj.getString("cover_image");
                    Picasso.get().load(ServerConstants.IMAGE_BASE_URL + mCurrentCoverUrl).into(mCoverImageView);

                    mJuryArray = competitionObj.getJSONArray("user_competition_jury");
                    mPartnersArray = competitionObj.getJSONArray("user_competition_partner");

                    if (mJuryArray.length() != 0) {

                        for (int i = 0; i < mJuryArray.length(); i++) {
                            JSONObject singleObject = mJuryArray.getJSONObject(i);
                            if (singleObject.has("user")) {
                                Log.d(TAG, "singleObject.: " + singleObject.getString("user"));
                                if (!singleObject.getString("user").toString().equals("null")) {
                                    JSONObject userObject = singleObject.getJSONObject("user");
                                    Log.d(TAG, "userObject: " + userObject.toString());

                                    String id = singleObject.getString("id");
                                    String fullName = singleObject.getString("jury_fullname");
                                    String imageUrl = userObject.getString("profile");

                                    JuryClass jury = new JuryClass(id, fullName, imageUrl);
                                    mJuryList.add(jury);
                                    mJuryAdapter.notifyDataSetChanged();
                                }

                            }

                        }

                    } else {
                        Toast.makeText(CompetitionEditActivity.this, "No Jury", Toast.LENGTH_SHORT).show();
                    }

                    if (mPartnersArray.length() > 0) {

                        for (int i = 0; i < mPartnersArray.length(); i++) {
                            JSONObject singleObject = mPartnersArray.getJSONObject(i);

                            if (singleObject.has("user")) {
                                Log.d(TAG, "singleObject.: " + singleObject.getString("user"));
                                if (!singleObject.getString("user").toString().equals("null")) {

                                    JSONObject userObject = singleObject.getJSONObject("user");

                                    String id = singleObject.getString("id");
                                    String name = singleObject.getString("partner_name");
                                    String imageUrl = userObject.getString("profile");

                                    PartnerClass partner = new PartnerClass(id, name, imageUrl);
                                    mPartners.add(partner);
                                    mPartnersAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(CompetitionEditActivity.this , "No Report found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkUtil.handleSimpleVolleyRequestError(error, CompetitionEditActivity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));

                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.edit_comp_cover:
            case R.id.edit_comp_title: {
                showEditTitleNCoverDialog();
                break;
            }
            case R.id.edit_comp_brief_edit:
            case R.id.edit_comp_brief: {
                showEditBriefDialog();
                break;
            }
            case R.id.edit_comp_eligibility_criteria: {
                showEditECDialog();
                break;
            }
            default:
                break;
        }
    }

    private void showEditECDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setText(mCurrentEligibilityCriteria);

        alert.setTitle("Enter New Criteria");

        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newCriteria = edittext.getText().toString();
                eligibilityCriteriaUpdateApi(newCriteria);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                Log.d(TAG, "onClick: cancel pressed");
            }
        });
        alert.show();
    }

    private void eligibilityCriteriaUpdateApi(final String newCriteria) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.ELIGIBILITY_CRITERIA_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: eligibility criteria update response = " + response);

                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    String message = responseObject.getString("message");

                    Toast.makeText(CompetitionEditActivity.this, message, Toast.LENGTH_SHORT).show();
                    mEligibilityTV.setText(newCriteria);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, CompetitionEditActivity.this);
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

                params.put("eligibility_criteria", newCriteria);
                params.put("users_competition_slug", mSlug);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    private void showEditBriefDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setText(Html.fromHtml(mCurrentBrief));
        edittext.setHint("Enter new brief");
        alert.setTitle("Enter New Brief");

        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newBrief = edittext.getText().toString();
                briefUpdateApi(newBrief);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                Log.d(TAG, "onClick: cancel pressed");
            }
        });
        alert.show();
    }

    private void briefUpdateApi(final String newBrief) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.BRIEF_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: brief update response = " + response);

                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    String message = responseObject.getString("message");

                    Toast.makeText(CompetitionEditActivity.this, message, Toast.LENGTH_SHORT).show();
                    mBriefWebView.loadData(newBrief, "text/html", null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                NetworkUtil.handleSimpleVolleyRequestError(error, CompetitionEditActivity.this);
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

                params.put("berif", newBrief);
                params.put("users_competition_slug", mSlug);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    private void showEditTitleNCoverDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.title_cover_update_dialog, null);

        final EditText titleET = view.findViewById(R.id.title_update);
        mCoverDialogImageView = view.findViewById(R.id.cover_update);

        titleET.setText(mCurrentTitle);
        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + mCurrentCoverUrl).into(mCoverDialogImageView);

        mCoverDialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageTypeDialog();
            }
        });

        alert.setTitle("Update Title & Cover Image");

        alert.setView(view);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newTitle = titleET.getText().toString();
                titleNCoverUpdateApi(newTitle, mSelectedCoverImagePath);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                Log.d(TAG, "onClick: cancel pressed");
            }
        });
        alert.show();

    }

    private void titleNCoverUpdateApi(final String newTitle, String selectedCoverImagePath) {

        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT);
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        try {
            final String uploadId = UUID.randomUUID().toString();
            //initializeSSLContext(getCallingActivity());
            final MultipartUploadRequest request = new MultipartUploadRequest(this, uploadId, ServerConstants.  TITLE_N_COVER_UPDATE);

            if (selectedCoverImagePath != null) {
                request.addFileToUpload(selectedCoverImagePath, "cover_image");
            }

            request.addParameter("competition_title", newTitle)
                    .addParameter("users_competition_slug", mSlug)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {

                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.i(TAG, "onProgress: ");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            mPb.setVisibility(View.GONE);
                            mContentLayout.setVisibility(View.VISIBLE);

                            Log.d(TAG, "onError: called");
                            Log.d(TAG, "onError: server response = " + serverResponse.getBodyAsString());
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            mPb.setVisibility(View.GONE);
                            mContentLayout.setVisibility(View.VISIBLE);

                            Log.d(TAG, "onCompleted: upload successful");
                            Log.d(TAG, "onCompleted: " + serverResponse.getBodyAsString());

                            try {
                                JSONObject responseObject = new JSONObject(serverResponse.getBodyAsString());
                                String message = responseObject.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                mTitleTextView.setText(newTitle);
                                Picasso.get().load(mPhotoURI).into(mCoverImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            mPb.setVisibility(View.GONE);
                            mContentLayout.setVisibility(View.VISIBLE);
                            Log.i(TAG, "onCancelled: ");
                        }
                    });
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

    private void showImageTypeDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionEditActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraClickIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    chooseFromGalleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseFromGalleryIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                chooseImageFromGallery(chooseImageIntent);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
        } else {
            chooseImageFromGallery(chooseImageIntent);
        }

    }

    private void cameraClickIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    showCameraPreview(takePictureIntent);

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "This permission is needed to capture your cover image", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                }
            } else {
                showCameraPreview(takePictureIntent);
            }


        } else {
            Toast.makeText(this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCameraPreview(Intent takePictureIntent) {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            mPhotoURI = FileProvider.getUriForFile(this,
                    "com.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
            startActivityForResult(takePictureIntent, REQUEST_COVER_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mSelectedCoverImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CAMERA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraPreview(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseImageFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

            } else {
                Toast.makeText(this, "Permission to choose image denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseImageFromGallery(Intent intent) {
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_COVER_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: called");
        Log.d(TAG, "onActivityResult: request code = " + requestCode);
        Log.d(TAG, "onActivityResult: result code = " + resultCode);

        if (requestCode == REQUEST_COVER_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Picasso.get().load(mPhotoURI).into(mCoverDialogImageView);

        } else if (requestCode == REQUEST_CHOOSE_COVER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedCoverImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedCoverImagePath);

            Picasso.get().load(uri).into(mCoverDialogImageView);

        }
    }

}

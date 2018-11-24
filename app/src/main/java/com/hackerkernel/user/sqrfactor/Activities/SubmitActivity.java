package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubmitActivity extends AppCompatActivity {
    private static final String TAG = "SubmitActivity";
    private static final int RESULT_COVER_IMAGE = 1;
    private static final int RESULT_PDF = 2;

    private Toolbar mToolbar;
    private RelativeLayout mCoverImageButton;
    private TextView mCoverImagePathTV;
    private RelativeLayout mDesignPdfButton;
    private TextView mPdfPathTV;
    private Button mSubmitButton;

    private ProgressBar mPb;
    private MySharedPreferences mSp;
    private RelativeLayout mContentLayout;
    private RequestQueue mRequestQueue;
    private String mSelectedFilePath;
    private String mSelectedCoverImagePath;

    String slug;
    String title;
    String coverImagePath;
    String pdfPath;
    String competitionId;
    String participationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        mSp = MySharedPreferences.getInstance(this);

        final EditText mDesignTitleET = findViewById(R.id.submit_design_title);
        mCoverImageButton = findViewById(R.id.submit_design_cover_image);
        mCoverImagePathTV = findViewById(R.id.submit_design_cover_image_path);
        mDesignPdfButton = findViewById(R.id.submit_design_pdf);
        mPdfPathTV = findViewById(R.id.submit_design_pdf_path);
        mSubmitButton = findViewById(R.id.submit_design);

         slug = getIntent().getStringExtra(BundleConstants.SLUG);

        getIdDetails();

        mCoverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCoverImageIntent();
            }
        });

        mDesignPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePdfIntent();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = mDesignTitleET.getText().toString();
                if(!title.isEmpty() &&
                        !mCoverImagePathTV.getText().toString().matches("No file chosen") &&
                        !mPdfPathTV.getText().toString().matches("No file chosen")){
                    if(title.length() < 6){
                        mDesignTitleET.setError("Atleast 6 in length");
                    }
                    else
                        submissionDesignSaveApi();
                }
                else{
                    Log.d(TAG, "+++++++++++++++++"+title+" "+mCoverImagePathTV.getText().toString()+" "+mPdfPathTV.getText().toString());
                    Toast.makeText(SubmitActivity.this, "Enter Proper Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getIdDetails() {
//        /young-designers-award18/submit-design;
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        Log.d(TAG, "getIdDetails: =-=-=-=-=-"+slug+ServerConstants.SUBMISSION_ID_DETAILS);
        StringRequest idDetailRequest = new StringRequest(Request.Method.GET, ServerConstants.SUBMISSION_ID_DETAILS + "/" + slug + "/submit-design",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: ================="+response);
                        try {
                            JSONObject detailResponse = new JSONObject(response);
                            JSONObject compResponse = detailResponse.getJSONObject("competition");
                            JSONObject compPermResponse = detailResponse.getJSONObject("competition_participation");
                            competitionId = compResponse.getString("id");
                            participationId = compPermResponse.getString("id");

                            Log.d(TAG, "........;;;;;;;;;;;;"+competitionId+participationId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SubmitActivity.this, "Error getting details for submission", Toast.LENGTH_SHORT).show();
                        }
                    }){
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
//                headers.put("Content-Type", contentType);
                return headers;
            }
        };
        idDetailRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(idDetailRequest);
    }

    private void choosePdfIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf");
        startActivityForResult(i, RESULT_PDF );
    }

    private void chooseCoverImageIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, RESULT_COVER_IMAGE );
    }

    private void submissionDesignSaveApi() {
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
            final MultipartUploadRequest request = new MultipartUploadRequest(this, uploadId, ServerConstants.SUBMISSION_DESIGN_SAVE);
            request.addParameter("design_title",title)
                    .addParameter("competition_id",competitionId)
                    .addParameter("competition_participation_id", participationId)
                    .addFileToUpload(coverImagePath,"design_cover_image")
                    .addFileToUpload(pdfPath,"design_pdf")
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

                            Log.d(TAG, "onError: server response = " + serverResponse.getBodyAsString());
                            if(exception != null)
                            Log.d(TAG, "onError: exception = " + exception.toString());
                            else
                                Toast.makeText(context, "Error Uploading Design", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            mPb.setVisibility(View.GONE);
                            mContentLayout.setVisibility(View.VISIBLE);

                            Log.d(TAG, "onCompleted: upload successful");
                            Log.d(TAG, "onCompleted: " + serverResponse.getBodyAsString());

                            try {
                                JSONObject responseObject = new JSONObject(serverResponse.getBodyAsString());
                                String sl = responseObject.getString("message");
                                if(sl.equals("success"))
                                {
                                    Toast.makeText(SubmitActivity.this, "Design Upload Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SubmitActivity.this,CompetitionDetailActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .putExtra(BundleConstants.SLUG,slug));
                                    finish();
                                }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PDF && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            mSelectedFilePath = FileUtils.getPath(SubmitActivity.this, uri);
            pdfPath = mSelectedFilePath;
            String[] newOne = mSelectedFilePath.split("/");
            mPdfPathTV.setText((newOne[newOne.length - 1]));

        }
        else if (requestCode == RESULT_COVER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedCoverImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected cover image path = " + mSelectedCoverImagePath);
            coverImagePath = mSelectedCoverImagePath;
            String[] newOne = mSelectedCoverImagePath.split("/");
            mCoverImagePathTV.setText((newOne[newOne.length - 1]));
        }
    }
}

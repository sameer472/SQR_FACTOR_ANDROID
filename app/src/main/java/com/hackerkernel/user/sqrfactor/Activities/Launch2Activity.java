package com.hackerkernel.user.sqrfactor.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.hackerkernel.user.sqrfactor.Pojo.Award;
import com.hackerkernel.user.sqrfactor.Pojo.Jury;
import com.hackerkernel.user.sqrfactor.Pojo.Partner;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Launch2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "Launch2Activity";

    private static final int RESULT_DOC_FILE = 3;
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE = 4;
    private static final int REQUEST_CHOOSE_PARTNER_IMAGE = 5;
    private static final int REQUEST_PARTNER_IMAGE_CAPTURE = 6;

    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;

    private Toolbar mToolbar;
    private Spinner mCompetitionTypeSpinner;
    private AppCompatAutoCompleteTextView mPartnerNameET;
    private EditText mPartnerSiteET;
    private EditText mPartnerEmailET;
    private EditText mPartnerContactET;
    private Button mPartnerChooseFileButton;
    private TextView mPartnerFilePathTV;
    private TextView mPartnerAddMoreTV;
    private Button mAttachDocButton;
    private LinearLayout mDocsFilePathsLayout;
    private Button mSubmitButton;
    private LinearLayout mPartnerLayout;
    private LinearLayout mPaidCompetitionLayout;
    private RadioGroup mFromRadioGroup;
    private TextView mCommissionTV;

    private List<LinearLayout> mPartnerAddMoreList;
    private LinearLayout mUrlLayout;

    private List<LinearLayout> mEbrAddMoreList;
    private List<LinearLayout> mArAddMoreList;
    private List<LinearLayout> mLmrAddMoreList;

    private TextView mEbrAddMoreTV;
    private TextView mArAddMoreTV;
    private TextView mLmrAddMoreTV;

    private LinearLayout mEbrLayout;
    private LinearLayout mArLayout;
    private LinearLayout mLmrLayout;

    private List<String> mDocPaths;

    private EditText mEbrStartDateET;
    private EditText mEbrEndDateET;
    private EditText mArStartDateET;
    private EditText mArEndDateET;
    private EditText mLmrStartDateET;
    private EditText mLmrEndDateET;

    private EditText mCurrentlySelectedDateET;

    private List<UserClass> mUsers;
    private List<UserClass> mAddMoreUsers = new ArrayList<>();
    private UserArrayAdapter mUserArrayAdapter;
    private UserArrayAdapter mUserAddMoreAdapter;
    private Calendar mCalendar;

    private RadioGroup mSubRadioGroup;
    private EditText mUrlEditText;
    private String mSelectedPartnerImagePath;

    private TextView mChosenImageTV;
    private String mSelectedImagePath;

//    private TextView mRemoveAttachmentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch2);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDocPaths = new ArrayList<>();

        mSp = MySharedPreferences.getInstance(this);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        mCompetitionTypeSpinner = findViewById(R.id.launch_comp2_comp_type);
        mPartnerNameET = findViewById(R.id.launch_comp2_partner_name);
        mPartnerSiteET = findViewById(R.id.launch_comp2_partner_website);
        mPartnerEmailET = findViewById(R.id.launch_comp2_partner_email);
        mPartnerContactET = findViewById(R.id.launch_comp2_partner_contact);
        mPartnerChooseFileButton = findViewById(R.id.launch_comp2_partner_image);
        mPartnerFilePathTV = findViewById(R.id.launch_comp2_partner_image_path);
        mPartnerAddMoreTV = findViewById(R.id.launch_comp2_partner_add_more);
        mAttachDocButton = findViewById(R.id.launch_comp2_attach_choose_file);
        mDocsFilePathsLayout = findViewById(R.id.launch_comp2_doc_paths);
        mSubmitButton = findViewById(R.id.launch_comp2_submit);

        mPartnerLayout = findViewById(R.id.launch_comp2_partner_layout);
        mPaidCompetitionLayout = findViewById(R.id.launch_comp2_paid_comp_layout);
        mFromRadioGroup = findViewById(R.id.paid_comp_from);

        mCommissionTV = findViewById(R.id.paid_comp_commission);
        mUrlLayout = findViewById(R.id.paid_comp_url_layout);
        mUrlEditText = findViewById(R.id.paid_comp_url);

        mEbrAddMoreTV = findViewById(R.id.paid_comp_ebr_add_more);
        mArAddMoreTV = findViewById(R.id.paid_comp_ar_add_more);
        mLmrAddMoreTV = findViewById(R.id.paid_comp_lmr_add_more);

        mEbrLayout = findViewById(R.id.paid_comp_ebr_layout);
        mArLayout = findViewById(R.id.paid_comp_ar_layout);
        mLmrLayout = findViewById(R.id.paid_comp_lmr_layout);

        mEbrStartDateET = findViewById(R.id.paid_comp_ebr_start_date);
        mEbrEndDateET = findViewById(R.id.paid_comp_ebr_end_date);
        mArStartDateET = findViewById(R.id.paid_comp_ar_start_date);
        mArEndDateET = findViewById(R.id.paid_comp_ar_end_date);
        mLmrStartDateET = findViewById(R.id.paid_comp_lmr_start_date);
        mLmrEndDateET = findViewById(R.id.paid_comp_lmr_end_date);

        mSubRadioGroup = findViewById(R.id.paid_comp_from);

//        mRemoveAttachmentTV = findViewById(R.id.launch_2_remove_attachment);

        mPartnerAddMoreList = new ArrayList<>();
        mEbrAddMoreList = new ArrayList<>();
        mArAddMoreList = new ArrayList<>();
        mLmrAddMoreList = new ArrayList<>();

        mCalendar = Calendar.getInstance();

        mUsers = new ArrayList<>();

        mPartnerNameET.setThreshold(1);
        String str = mPartnerNameET.getText().toString().trim();
        mUserArrayAdapter = new UserArrayAdapter(this, R.layout.simple_text_view, str);
        mPartnerNameET.setAdapter(mUserArrayAdapter);

        mPartnerAddMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.partner_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.partner_remove);
                Button chooseFileBtn = addMoreLayout.findViewById(R.id.partner_image);
                final TextView imagePathTV = addMoreLayout.findViewById(R.id.partner_image_path);
                final EditText partnerEmailET = addMoreLayout.findViewById(R.id.partner_email);
                final EditText partnerContactET = addMoreLayout.findViewById(R.id.partner_contact);

                final AppCompatAutoCompleteTextView partnerNameTV = addMoreLayout.findViewById(R.id.partner_name);
                partnerNameTV.setThreshold(1);

                String strAddMore = partnerNameTV.getText().toString().trim();
                mUserAddMoreAdapter = new UserArrayAdapter(Launch2Activity.this, R.layout.simple_text_view, strAddMore);
                partnerNameTV.setAdapter(mUserAddMoreAdapter);

                partnerNameTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        UserClass user = mUserArrayAdapter.getItem(pos);
                        partnerEmailET.setText(user.getEmail());
                        partnerContactET.setText(user.getMobileNumber());
                        imagePathTV.setText(user.getProfilePicPath());

                        partnerNameTV.setText(user.getName());
                    }
                });

                chooseFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChosenImageTV = imagePathTV;
                        choosePartnerImageDialog();
                    }
                });

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPartnerLayout.removeView(addMoreLayout);
                        mPartnerAddMoreList.remove(addMoreLayout);
                    }
                });

                mPartnerLayout.addView(addMoreLayout);
                mPartnerAddMoreList.add(addMoreLayout);
            }
        });

        mEbrAddMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.add_more_remove);

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEbrLayout.removeView(addMoreLayout);
                        mEbrAddMoreList.remove(addMoreLayout);
                    }
                });

                mEbrLayout.addView(addMoreLayout);
                mEbrAddMoreList.add(addMoreLayout);
            }
        });

        mArAddMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.add_more_remove);

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mArLayout.removeView(addMoreLayout);
                        mArAddMoreList.remove(addMoreLayout);
                    }
                });

                mArLayout.addView(addMoreLayout);
                mArAddMoreList.add(addMoreLayout);
            }
        });

        mLmrAddMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.launch_comp2_add_more_layout,null);
                TextView removeTV = addMoreLayout.findViewById(R.id.add_more_remove);

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLmrLayout.removeView(addMoreLayout);
                        mLmrAddMoreList.remove(addMoreLayout);
                    }
                });

                mLmrLayout.addView(addMoreLayout);
                mLmrAddMoreList.add(addMoreLayout);
            }
        });

        mCompetitionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                    case 1:
                        mPaidCompetitionLayout.setVisibility(View.GONE);
                        break;

                    case 2:
                        mPaidCompetitionLayout.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFromRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.paid_comp_from_sqrFactor: {
                        mCommissionTV.setVisibility(View.VISIBLE);
                        mUrlLayout.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.paid_comp_from_own_site: {
                        mCommissionTV.setVisibility(View.GONE);
                        mUrlLayout.setVisibility(View.VISIBLE);
                        break;
                    }
                }

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                competitionSaveApi();
            }
        });

        mAttachDocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDocFileIntent();
            }
        });

//        mPartnerNameET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                userSearchApi(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mPartnerNameET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                UserClass user = mUserArrayAdapter.getItem(pos);
                mPartnerEmailET.setText(user.getEmail());
                mPartnerContactET.setText(user.getMobileNumber());
                mPartnerFilePathTV.setText(user.getProfilePicPath());

                mPartnerNameET.setText(user.getName());
            }
        });

        mPartnerChooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChosenImageTV = mPartnerFilePathTV;
                choosePartnerImageDialog();
            }
        });

        mEbrStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mEbrStartDateET;
                showDatePicker();
            }
        });

        mEbrEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mEbrEndDateET;
                showDatePicker();
            }
        });

        mArStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mArStartDateET;
                showDatePicker();
            }
        });

        mArEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mArEndDateET;
                showDatePicker();
            }
        });

        mLmrStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mLmrStartDateET;
                showDatePicker();
            }
        });

        mLmrEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentlySelectedDateET = mLmrEndDateET;
                showDatePicker();
            }
        });

//        mRemoveAttachmentTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Log.d(TAG, "onOptionsItemSelected: home pressed");
                onBackPressed();
                return true;
            }

            default:
                return false;
        }
    }

    private void showDatePicker() {
        Log.d(TAG, "showDatePicker: called");
        new DatePickerDialog(this, this, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "onDateSet: called");
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String dateString = sdf.format(mCalendar.getTime());

        mCurrentlySelectedDateET.setText(dateString);

    }

    private void chooseDocFileIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("application/pdf");
        startActivityForResult(i, RESULT_DOC_FILE);
    }

    private void competitionSaveApi() {
        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT);
            return;
        }

        Intent i = getIntent();
        String coverImagePath = i.getStringExtra(BundleConstants.COVER_IMAGE_PATH);
        Log.d(TAG, "competitionSaveApi: cover image path = " + coverImagePath);

        String compTitle = i.getStringExtra(BundleConstants.COMPETITION_TITLE);
        Log.d(TAG, "competitionSaveApi: competition title = " + compTitle);

        String brief = i.getStringExtra(BundleConstants.COMPETITION_BRIEF);
        Log.d(TAG, "competitionSaveApi: brief = " + brief);

        String eligibilityCriteria = i.getStringExtra(BundleConstants.ELIGIBILITY_CRITERIA);
        Log.d(TAG, "competitionSaveApi: eligibility criteria = " + eligibilityCriteria);

        String startDate = i.getStringExtra(BundleConstants.START_DATE);
        Log.d(TAG, "competitionSaveApi: start date = " + startDate);

        String closeRegDate = i.getStringExtra(BundleConstants.CLOSE_REG_DATE);
        Log.d(TAG, "competitionSaveApi: close registration date = " + closeRegDate);

        String closeSubDate = i.getStringExtra(BundleConstants.CLOSE_SUBMISSION_DATE);
        Log.d(TAG, "competitionSaveApi: close submission date = " + closeSubDate);

        String announceDate = i.getStringExtra(BundleConstants.ANNOUNCE_DATE);
        Log.d(TAG, "competitionSaveApi: announce date = " + announceDate);

        Bundle args = i.getBundleExtra(BundleConstants.BUNDLE);
        List<Jury> juryList = (List<Jury>) args.getSerializable(BundleConstants.JURY_LIST);
        List<Award> awardList = (List<Award>) args.getSerializable(BundleConstants.AWARD_LIST);

        Log.d(TAG, "competitionSaveApi: jury list = " + juryList.toString());
        Log.d(TAG, "competitionSaveApi: jury list size = " + juryList.size());

        Log.d(TAG, "competitionSaveApi: award list = " + awardList.toString());
        Log.d(TAG, "competitionSaveApi: award list size = " + awardList.size());

        String competitionType = mCompetitionTypeSpinner.getSelectedItem().toString();
        Log.d(TAG, "competitionSaveApi: competition type = " + competitionType);

        List<Partner> partnerList = new ArrayList<>();

        for (int j = 0; j < mPartnerAddMoreList.size(); j++) {
            LinearLayout layout = mPartnerAddMoreList.get(j);
            EditText partnerNameET = layout.findViewById(R.id.partner_name);
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

            Log.d(TAG, "competitionSaveApi: partner list = " + partnerList.toString());
            Log.d(TAG, "competitionSaveApi: partner list size = " + partnerList.size());
        }

        String partnerName = mPartnerNameET.getText().toString();
        String partnerSite = mPartnerSiteET.getText().toString();
        String partnerEmail = mPartnerEmailET.getText().toString();
        String partnerContact = mPartnerContactET.getText().toString();
        String partnerImagePath = mPartnerFilePathTV.getText().toString();

        Partner partner = new Partner(partnerName, partnerSite, partnerEmail, partnerContact, partnerImagePath);
        partnerList.add(partner);

        try {
            final String uploadId = UUID.randomUUID().toString();
            //initializeSSLContext(getCallingActivity());
            final MultipartUploadRequest request = new MultipartUploadRequest(this, uploadId, ServerConstants.COMPETITION_SAVE);

            for (int j = 0; j < juryList.size(); j++) {
                Jury jury = juryList.get(j);

                request.addParameter("jury_fullname[" + j + "]", jury.getName())
                        .addParameter("jury_firm_company[" + j + "]", jury.getCompany())
                        .addParameter("jury_email[" + j + "]", jury.getEmail())
                        .addParameter("jury_contact[" + j + "]", jury.getContact());
//                        .addFileToUpload(jury.getImagePath(), "jury_logo[" + j + "]");
            }

            for (int j = 0; j < awardList.size(); j++) {
                Award award = awardList.get(j);

                request.addParameter("award_type[" + j + "]", award.getType())
                        .addParameter("award_amount[" + j + "]", award.getAmount())
                        .addParameter("award_currency[" + j + "]", award.getCurrencyUnit())
                        .addParameter("award_extra[" + j + "]", award.getDetails());
            }

            for (int j = 0; j < partnerList.size(); j++) {
                Partner partner1 = partnerList.get(j);

                request.addParameter("partner_name[" + j + "]", partner1.getName())
//                        .addParameter("partner_id[" + j + "]", partner1.getId())
                        .addParameter("partner_website[" + j + "]", partner1.getWebsite())
                        .addParameter("partner_email[" + j + "]", partner1.getEmail())
                        .addParameter("partner_contact[" + j + "]", partner1.getContact());
            }

            request.addParameter("early_bird_registration_start_date", mEbrStartDateET.getText().toString())
                    .addParameter("early_bird_registration_end_date", mEbrEndDateET.getText().toString());

            for (int j = 0; j < mEbrAddMoreList.size(); j++) {
                LinearLayout layout = mEbrAddMoreList.get(j);

                EditText regTypeET = layout.findViewById(R.id.add_more_reg_type);
                EditText awardAmountET = layout.findViewById(R.id.add_more_award_amount);
                Spinner currencyUnitSpinner = layout.findViewById(R.id.add_more_currency_unit);

                String regType = regTypeET.getText().toString();
                String awardAmount = awardAmountET.getText().toString();
                String currencyUnit = currencyUnitSpinner.getSelectedItem().toString();

                request.addParameter("early_bird_registration_type[" + j + "]", regType)
                        .addParameter("early_bird_registration_currency[" + j + "]", currencyUnit)
                        .addParameter("early_bird_registration_amount[" + j + "]", awardAmount);
            }

            request.addParameter("advance_registration_start_date", mArStartDateET.getText().toString())
                    .addParameter("advance_registration_end_date", mArEndDateET.getText().toString());

            for (int j = 0; j < mArAddMoreList.size(); j++) {
                LinearLayout layout = mArAddMoreList.get(j);

                EditText regTypeET = layout.findViewById(R.id.add_more_reg_type);
                EditText awardAmountET = layout.findViewById(R.id.add_more_award_amount);
                Spinner currencyUnitSpinner = layout.findViewById(R.id.add_more_currency_unit);

                String regType = regTypeET.getText().toString();
                String awardAmount = awardAmountET.getText().toString();
                String currencyUnit = currencyUnitSpinner.getSelectedItem().toString();

                request.addParameter("advance_registration_type[" + j + "]", regType)
                        .addParameter("advance_registration_currency[" + j + "]", currencyUnit)
                        .addParameter("advance_registration_amount[" + j + "]", awardAmount);
            }

            request.addParameter("last_minute_registration_start_date", mLmrStartDateET.getText().toString())
                    .addParameter("last_minute_registration_end_date", mLmrEndDateET.getText().toString());

            for (int j = 0; j < mLmrAddMoreList.size(); j++) {
                LinearLayout layout = mLmrAddMoreList.get(j);

                EditText regTypeET = layout.findViewById(R.id.add_more_reg_type);
                EditText awardAmountET = layout.findViewById(R.id.add_more_award_amount);
                Spinner currencyUnitSpinner = layout.findViewById(R.id.add_more_currency_unit);

                String regType = regTypeET.getText().toString();
                String awardAmount = awardAmountET.getText().toString();
                String currencyUnit = currencyUnitSpinner.getSelectedItem().toString();

                request.addParameter("last_minute_registration_type[" + j + "]", regType)
                        .addParameter("last_minute_registration_currency[" + j + "]", currencyUnit)
                        .addParameter("last_minute_registration_amount[" + j + "]", awardAmount);
            }


            String regFrom = "sqr";
            request.addParameter("reg_form", regFrom);

            if (competitionType.equals("Paid Competition")) {

                if (mSubRadioGroup.getCheckedRadioButtonId() == R.id.paid_comp_from_sqrFactor) {
                    regFrom = "sqr";
                } else if (mSubRadioGroup.getCheckedRadioButtonId() == R.id.paid_comp_from_own_site) {
                    regFrom = "own";
                    request.addParameter("url", mUrlEditText.getText().toString());
                }
                Log.d(TAG, "competitionSaveApi: registration from = " + regFrom);

                request.addParameter("reg_form", regFrom)
                        .addParameter("early_bird_registration_start_date", mEbrStartDateET.getText().toString())
                        .addParameter("early_bird_registration_end_date", mEbrEndDateET.getText().toString())
                        .addParameter("advance_registration_start_date", mArStartDateET.getText().toString())
                        .addParameter("advance_registration_end_date", mArEndDateET.getText().toString())
                        .addParameter("last_minute_registration_start_date", mLmrStartDateET.getText().toString())
                        .addParameter("last_minute_registration_end_date", mLmrEndDateET.getText().toString());
            }

            request.addParameter("honourable_mentions", "None")
                    .addParameter("competition_title",compTitle)
                    .addParameter("competition_brief",brief)
                    .addParameter("eligibility_criteria",eligibilityCriteria)
                    .addParameter("schedule_start_date_of_registration", startDate)
                    .addParameter("schedule_close_date_of_registration",closeRegDate)
                    .addParameter("schedule_closing_date_of_project_submission",closeSubDate)
                    .addParameter("schedule_announcement_of_the_winner", announceDate)
                    .addParameter("competition_type", competitionType)
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
                            Log.d(TAG, "onError: server response = " + serverResponse.getBodyAsString());
//                            Log.d(TAG, "onError: exception = " + exception.toString());

//                            String responseEvent = serverResponse.getBodyAsString();
//                            UploadService.stopUpload(uploadId);
//
//                            try {
//
//                                JSONObject responseObject = new JSONObject(responseEvent);
//                                JSONArray errorKeys = responseObject.getJSONArray("error_keys");
//                                String errorKey = errorKeys.getString(0);
//                                JSONObject errorsObject = responseObject.getJSONObject("errors");
//                                JSONArray errorsArray = errorsObject.getJSONArray(errorKey);
//
//                                String errorMessage = errorsArray.getString(0);
//
//                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
//
//
//                            } catch (Throwable t) {
//                                Log.e("My App", "Could not parse malformed JSON: \"" + responseEvent + "\"");
//                            }
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.d(TAG, "onCompleted: upload successful");
                            Log.d(TAG, "onCompleted: " + serverResponse.getBodyAsString());

                            Toast.makeText(getApplicationContext(), "Event added successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(context, PostEvent2Activity.class));
                            finish();
                            try {
                                JSONObject responseObject = new JSONObject(serverResponse.getBodyAsString());
                                String url = responseObject.getString("Success");
                                Log.d(TAG, "onCompleted: url = " + url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
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

    private void choosePartnerImageDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Launch2Activity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takePartnerPhotoIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    choosePartnerImageIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void choosePartnerImageIntent() {
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

//        if (chooseImageIntent.resolveActivity(getPackageManager()) != null) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    chooseCoverFromGallery(chooseImageIntent);
//
//                } else {
//                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                        Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
//                    }
//                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
//                }
//            } else {
//                chooseCoverFromGallery(chooseImageIntent);
//            }
//
//        } else {
//            Toast.makeText(this, "Please install the image chooser app first", Toast.LENGTH_SHORT).show();
//        }
    }

    private void choosePartnerFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, REQUEST_CHOOSE_PARTNER_IMAGE);
    }

    private void takePartnerPhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    showPartnerCameraPreview(takePictureIntent);

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "This permission is needed to capture your cover image", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE);
                }
            } else {
                showPartnerCameraPreview(takePictureIntent);
            }

        } else {
            Toast.makeText(this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPartnerCameraPreview(Intent takePictureIntent) {
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
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_PARTNER_IMAGE_CAPTURE);
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
        mSelectedPartnerImagePath = image.getAbsolutePath();
        return image;
    }

    private void addPathView(String imagePath) {

        final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_more_layout,null);
        final TextView textView = addMoreLayout.findViewById(R.id.addedPath);
        textView.setText(imagePath);
        final ImageButton ib = addMoreLayout.findViewById(R.id.removePath);
        mDocsFilePathsLayout.addView(addMoreLayout);
        ib.setTag(imagePath);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDocsFilePathsLayout.removeView(addMoreLayout);

                Log.d(TAG, "imagepath: " + (ib.getTag()));
                mDocPaths.remove(ib.getTag());
                Log.d(TAG, "onClick: image paths size = "+ mDocPaths.size());
                Log.d(TAG, "onClick image path = " + mDocPaths.toString());
            }
        });
    }

    private void userSearchApi(final String searchQuery) {
        Log.d(TAG, "userSearchApi: called");
        Log.d(TAG, "userSearchApi: serach query = " + searchQuery);
//        mUsers.clear();
        mUsers = new ArrayList<>();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();


        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: user search response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    JSONArray usersArray = responseObject.getJSONArray("users");

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject singleObj = usersArray.getJSONObject(i);
                        String name;
                        if(singleObj.getString("first_name")!=null && singleObj.getString("first_name").equals("null") && singleObj.getString("first_name")!=null && singleObj.getString("last_name").equals("null") ){
                            name = singleObj.getString("name");
                        }else {
                            name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
                        }
                        String profileUrl = singleObj.getString("profile");
                        String email = singleObj.getString("email");
                        String mobileNumber = singleObj.getString("mobile_number");

                        UserClass user = new UserClass(name, profileUrl, email, mobileNumber);
                        mUsers.add(user);

//                        Log.d(TAG, "onResponse: user list size = " + mUsers.size());
//                        mUserArrayAdapter.notifyDataSetChanged();
                    }
                    //mUserArrayAdapter = new UserArrayAdapter(Launch2Activity.this, R.layout.simple_text_view, mUsers);
                    //mPartnerNameET.setAdapter(mUserArrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkUtil.handleSimpleVolleyRequestError(error, Launch2Activity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
//                headers.put(getString(R.string.authorization), "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjYyODI4YjM3MjJjMzcyN2ZiNThiOGU0YWRjMDg3ZDgzNzFhYzdhNGFlNzhlODc4NWY2NzI3Y2EyM2JmMTEyOGYzNGVkODA4OGU4MjQ5MjFmIn0.eyJhdWQiOiIzIiwianRpIjoiNjI4MjhiMzcyMmMzNzI3ZmI1OGI4ZTRhZGMwODdkODM3MWFjN2E0YWU3OGU4Nzg1ZjY3MjdjYTIzYmYxMTI4ZjM0ZWQ4MDg4ZTgyNDkyMWYiLCJpYXQiOjE1MzIwNzM0NjQsIm5iZiI6MTUzMjA3MzQ2NCwiZXhwIjoxNTYzNjA5NDY0LCJzdWIiOiIxMDYiLCJzY29wZXMiOltdfQ.kGgIH4IStNn0mkjj3pXs85YfpUNH37vKM8v3QUn3Vbljwds0eXQXacX76-r_uA7pbT_1b0HQmdOxxqdOGHPJ8ahweNe6wEbSgsSvWuhJ-niHb2Hf6y4KWlIK0AI6ktDvKfNFKKlAXbH-mML0N9Te6tge_MEkMtzhoy8RptlDrycJR1aoLr5UR0z3KUuynRqmh5hind79OD5vM6zq_4I2-VPvFe_WNRRsb63HDLZaGunZwTUPCE0SSW3Xo30zDPsgLBjZpwkt_8fIJzd9N1GPPEmjwFqHuggDlthh81zR3r-dGN9GdGpzWtIsLouyt-b0Rel2olgAjFkrR8kIjvShv1S4sgdFMlP-90gpdHBTbQ6rL4gsBkcPxC1veaxTGFxvGds81N3jQidjZh2_wnZ-OSBBYcxndDlxuQt5WceDQA7M4muX1y714gAJEL698qX9tXPFPXhlKBzP5KvPEfxRg_neIhEWf5KnuX40dnWAkD5EDdwJWi2n5BqBBMqjvt57EpPIlHovgSP2HT3bp8Y-rCbvdwGTfb-d2cItofndZfxzHTstRqzrgsKgNuI1ZsJudLIo_9fpulsoga9-88eJXzaOJMhHf0nmymuxOfaB3m0HHWw1wLpVbS7dOpN0H_z5a1HFqugBpDfXECsiPlJtZ8D8wJ5zhbKLYer_89OtYrc");
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
//                headers.put("Content-Type", contentType);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("search", searchQuery);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    private void userSearchAddMoreApi(final String searchQuery, final AppCompatAutoCompleteTextView juryNameTV) {
        Log.d(TAG, "userSearchApi: called");
        Log.d(TAG, "userSearchApi: serach query = " + searchQuery);
//        mAddMoreUsers.clear();
        mAddMoreUsers = new ArrayList<>();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: user search response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    JSONArray usersArray = responseObject.getJSONArray("users");

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject singleObj = usersArray.getJSONObject(i);
                        String name;
                        if(singleObj.getString("first_name")!=null && singleObj.getString("first_name").equals("null") && singleObj.getString("first_name")!=null && singleObj.getString("last_name").equals("null") ){
                            name = singleObj.getString("name");
                        }else {
                            name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
                        }
                        String profileUrl = singleObj.getString("profile");
                        String email = singleObj.getString("email");
                        String mobileNumber = singleObj.getString("mobile_number");

                        UserClass user = new UserClass(name, profileUrl, email, mobileNumber);
                        mAddMoreUsers.add(user);

//                        Log.d(TAG, "onResponse: user list size = " + mAddMoreUsers.size());
//                        mUserAddMoreAdapter.notifyDataSetChanged();
                    }
                    //mUserAddMoreAdapter = new UserArrayAdapter(Launch2Activity.this, R.layout.simple_text_view, mAddMoreUsers);
                    //juryNameTV.setAdapter(mUserAddMoreAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkUtil.handleSimpleVolleyRequestError(error, Launch2Activity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("search", searchQuery);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showPartnerCameraPreview(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

            } else {
                Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                choosePartnerFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

            } else {
                Toast.makeText(this, "Permission to choose image denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_DOC_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //String selectedImagePath = ImageUtils.getPath(this, uri);
            String selectedFilePath = FileUtils.getPath(Launch2Activity.this, uri);
//                File newFile = new File(uri.getPath());
//                selectedImagePath = newFile.getAbsolutePath();
            addPathView(selectedFilePath);
            mDocPaths.add(selectedFilePath);

        } else if (requestCode == REQUEST_PARTNER_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mChosenImageTV.setText(mSelectedImagePath);

            //mSelectedCoverImagePath = uri.getPath();
//            File newFile = new File(uri.getPath());
//            mSelectedCoverImagePath = newFile.getAbsolutePath();
//            File myFile = new File(uri.toString());
//            String path = myFile.getAbsolutePath();
//            mDisplayName = null;
//            if (uri.toString().startsWith("content://")) {
//                Cursor cursor = null;
//                try {
//                    cursor = getContentResolver().query(uri, null, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        mDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                        Log.d("onResultpathIF: ", mDisplayName);
//
//                    }
//                } finally {
//                    cursor.close();
//                }
//            } else if (uri.toString().startsWith("file://")) {
//                mDisplayName = myFile.getName();
//                Log.d("onResultpathELSEIF: ", path);
//            }
//            mAttachCoverImageET.setText(mDisplayName);
            Toast.makeText(Launch2Activity.this, mSelectedImagePath, Toast.LENGTH_SHORT).show();

        } else if (requestCode == REQUEST_CHOOSE_PARTNER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedImagePath);

            mChosenImageTV.setText(mSelectedImagePath);

        }
    }

}

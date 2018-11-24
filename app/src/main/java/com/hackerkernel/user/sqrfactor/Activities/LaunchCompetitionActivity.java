package com.hackerkernel.user.sqrfactor.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.FileUtils;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

enum PickerType {
    START_DATE,
    CLOSE_DATE_REGISTRATION,
    CLOSE_DATE_SUBMISSION,
    ANNOUNCEMENT_DATE
};

public class LaunchCompetitionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private static final String TAG = "LaunchCompetitionActivi";
    private static final int REQUEST_COVER_IMAGE_CAPTURE = 1;
    private static final int RESULT_CHOSEN_IMAGE = 2;
    private static final int REQUEST_CHOOSE_COVER_IMAGE = 3;
    private static final int REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE = 4;
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 5;
    private static final int REQUEST_CHOOSE_JURY_IMAGE = 6;
    private static final int REQUEST_JURY_IMAGE_CAPTURE = 7;

    private String mSelectedCoverImagePath;
    private String mSelectedChosenImagePath;

    private Toolbar mToolbar;
    private Button mNextButton;
    private EditText mAttachCoverImageET;
    private EditText mCompetitionTitleET;
    private EditText mBriefEditText;
    private EditText mEligibilityEditText;

    private AppCompatAutoCompleteTextView mJuryNameET;
    private EditText mJuryCompanyET;
    private EditText mJuryEmailET;
    private EditText mJuryContactET;
    private Button mJuryImageButton;
    private TextView mJuryImageTV;
    private TextView mJuryAddMoreTV;
    private LinearLayout mJuryLayout;

    private Spinner mAwardSpinner;
    private EditText mAwardAmountET;
    private Spinner mAwardCurrencyUnitSpinner;
    private EditText mAwardMoreDetailsET;
    private EditText mAwardOtherDetailsET;
    private TextView mAwardAddMoreTV;
    private LinearLayout mAwardLayout;

    private EditText mStartDateET;
    private EditText mCloseRegDateET;
    private EditText mCloseSubmissionDateET;
    private EditText mAnnouceDateET;

    private PickerType mPickerType;
    private Calendar mCalendar;

    private TextView mRemoveCoverTV;

    List<LinearLayout> mJuryAddMoreList;
    List<LinearLayout> mAwardAddMoreList;
//    private String mDisplayName;

    List<TextView> mFilePathTVs;
    private TextView mChosenImageTV;

    private List<UserClass> mUsers;
    private List<UserClass> mAddMoreUsers;

    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private UserArrayAdapter mUserArrayAdapter;
    private UserArrayAdapter mUserAddMoreAdapter;

    private RadioButton NoJury,willUpdateLater,AddJury;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_competition);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCalendar = Calendar.getInstance();

        mSp = MySharedPreferences.getInstance(this);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = findViewById(R.id.radio_group);
        NoJury = findViewById(R.id.rb1);
        willUpdateLater = findViewById(R.id.rb2);
        AddJury = findViewById(R.id.rb3);
        mAttachCoverImageET = findViewById(R.id.launch_comp_cover_attach);
        mCompetitionTitleET = findViewById(R.id.launch_comp_title);
        mBriefEditText = findViewById(R.id.launch_comp_brief);
        mEligibilityEditText = findViewById(R.id.launch_comp_eligibility);

        mJuryNameET = findViewById(R.id.launch_comp_jury_name);
        mJuryCompanyET = findViewById(R.id.launch_comp_jury_company);
        mJuryEmailET = findViewById(R.id.launch_comp_jury_email);
        mJuryContactET = findViewById(R.id.launch_comp_jury_contact);
        mJuryImageButton = findViewById(R.id.launch_comp_jury_image);
        mJuryImageTV = findViewById(R.id.launch_comp_jury_image_path);
        mJuryAddMoreTV = findViewById(R.id.launch_comp_jury_add_more);
        mJuryLayout = findViewById(R.id.launch_comp_jury_layout);

        mAwardSpinner = findViewById(R.id.launch_comp_awards);
        mAwardAmountET = findViewById(R.id.launch_comp_award_amount);
        mAwardCurrencyUnitSpinner = findViewById(R.id.launch_comp_currency_unit);
        mAwardMoreDetailsET = findViewById(R.id.launch_comp_award_more_details);
        mAwardOtherDetailsET = findViewById(R.id.launch_comp_award_other_details);
        mAwardLayout = findViewById(R.id.launch_comp_award_layout);
        mAwardAddMoreTV = findViewById(R.id.launch_comp_award_add_more);

        mStartDateET = findViewById(R.id.launch_comp_startdate);
        mCloseRegDateET = findViewById(R.id.launch_comp_close_date_reg);
        mCloseSubmissionDateET = findViewById(R.id.launch_comp_close_date_submission);
        mAnnouceDateET = findViewById(R.id.launch_comp_announcedate);

        mNextButton = findViewById(R.id.launch_comp_next);

        mRemoveCoverTV = findViewById(R.id.launch_comp_remove_cover);

        mJuryAddMoreList = new ArrayList<>();
        mAwardAddMoreList = new ArrayList<>();

        mUsers = new ArrayList<>();
        mAddMoreUsers = new ArrayList<>();

        mJuryNameET.setThreshold(1);
        String str = mJuryNameET.getText().toString().trim();
        mUserArrayAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view, str);
        mJuryNameET.setAdapter(mUserArrayAdapter);

        mStartDateET.setOnClickListener(this);
        mCloseRegDateET.setOnClickListener(this);
        mCloseSubmissionDateET.setOnClickListener(this);
        mAnnouceDateET.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        { @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            View radioButton = radioGroup.findViewById(checkedId);
            int index = radioGroup.indexOfChild(radioButton);
            // Add logic here
            switch (index)
            { case 0:
                // first button
//                Toast.makeText(getApplicationContext(), "Selected button number " + index,Toast.LENGTH_SHORT).show();
                mJuryLayout.setVisibility(View.GONE);
                AddJury.setVisibility(View.VISIBLE);
                break;
                case 1:
                    // secondbutton
//                    Toast.makeText(getApplicationContext(), "Selected button number " + index,Toast.LENGTH_SHORT).show();
                    mJuryLayout.setVisibility(View.GONE);
                    AddJury.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    // secondbutton
//                    Toast.makeText(getApplicationContext(), "Selected button number " + index,Toast.LENGTH_SHORT).show();
                    mJuryLayout.setVisibility(View.VISIBLE);
                    AddJury.setVisibility(View.GONE);

                    break;
            } }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coverImagePath = mAttachCoverImageET.getText().toString();
                String competitionTitle = mCompetitionTitleET.getText().toString();
                String brief = mBriefEditText.getText().toString();
                String eligibilityCriteria = mEligibilityEditText.getText().toString();
                String startDate = mStartDateET.getText().toString();
                String closeRegDate = mCloseRegDateET.getText().toString();
                String closeSubmissionDate = mCloseSubmissionDateET.getText().toString();
                String announceDate = mAnnouceDateET.getText().toString();

                final List<Jury> juryList = new ArrayList<>();
                List<Award> awardList = new ArrayList<>();
//                if (coverImagePath.equals("")) {
//                    Toast.makeText(LaunchCompetitionActivity.this, "Cover Image is required", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Intent i = new Intent(LaunchCompetitionActivity.this, Launch2Activity.class);
                i.putExtra(BundleConstants.COVER_IMAGE_PATH, coverImagePath);
                i.putExtra(BundleConstants.COMPETITION_TITLE, competitionTitle);
                i.putExtra(BundleConstants.COMPETITION_BRIEF, brief);
                i.putExtra(BundleConstants.ELIGIBILITY_CRITERIA, eligibilityCriteria);
                i.putExtra(BundleConstants.START_DATE, startDate);
                i.putExtra(BundleConstants.CLOSE_REG_DATE, closeRegDate);
                i.putExtra(BundleConstants.CLOSE_SUBMISSION_DATE, closeSubmissionDate);
                i.putExtra(BundleConstants.ANNOUNCE_DATE, announceDate);


                for (int j = 0; j < mJuryAddMoreList.size(); j++) {
                    LinearLayout layout = mJuryAddMoreList.get(j);
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


                for (int j = 0; j < mAwardAddMoreList.size(); j++) {
                    LinearLayout layout = mAwardAddMoreList.get(j);
                    Spinner awardTypeSpinner = layout.findViewById(R.id.award_type);
                    EditText awardAmountET = layout.findViewById(R.id.award_amount);
                    Spinner awardCurrencyUnitSpinner = layout.findViewById(R.id.award_currency_unit);
                    EditText awardDetailsET = layout.findViewById(R.id.award_details);

                    String awardType = awardTypeSpinner.getSelectedItem().toString();
                    String awardAmount = awardAmountET.getText().toString();
                    String awardCurrencyUnit = awardCurrencyUnitSpinner.getSelectedItem().toString();
                    String awardDetails = awardDetailsET.getText().toString();

                    Award award = new Award(awardType, awardAmount, awardCurrencyUnit, awardDetails);
                    awardList.add(award);
                }
                String juryName = mJuryNameET.getText().toString();
                String juryCompany = mJuryCompanyET.getText().toString();
                String juryEmail = mJuryEmailET.getText().toString();
                String juryContact = mJuryContactET.getText().toString();
                String juryImagePath = mJuryImageTV.getText().toString();

                Jury jury = new Jury(juryName, juryCompany, juryEmail, juryContact, juryImagePath);
                juryList.add(jury);

                String awardType = mAwardSpinner.getSelectedItem().toString();
                String awardAmount = mAwardAmountET.getText().toString();
                String awardCurrencyUnit = mAwardCurrencyUnitSpinner.getSelectedItem().toString();
                String awardDetails = mAwardMoreDetailsET.getText().toString();

                Award award = new Award(awardType, awardAmount, awardCurrencyUnit, awardDetails);
                awardList.add(award);

                Bundle args = new Bundle();
                args.putSerializable(BundleConstants.JURY_LIST, (Serializable) juryList);
                args.putSerializable(BundleConstants.AWARD_LIST, (Serializable) awardList);
                i.putExtra(BundleConstants.BUNDLE, args);
                startActivity(i);

//                if(coverImagePath.isEmpty() ||
//                        competitionTitle.isEmpty() ||
//                        brief.isEmpty() || eligibilityCriteria.isEmpty() ||
//                        startDate.isEmpty() || closeRegDate.isEmpty() ||
//                        closeSubmissionDate.isEmpty() || announceDate.isEmpty() ||
//                        args.isEmpty()){
//                    Toast.makeText(LaunchCompetitionActivity.this, "Form Incomplete !!", Toast.LENGTH_SHORT).show();
//                }
//                else
//                    startActivity(i);

            }
        });

        mJuryAddMoreTV.setOnClickListener(new View.OnClickListener() {
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
//                mUserAddMoreAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view, mAddMoreUsers);

                String strAddMore = juryNameTV.getText().toString().trim();
                mUserAddMoreAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view, strAddMore);
                juryNameTV.setAdapter(mUserAddMoreAdapter);

//                juryNameTV.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        userSearchAddMoreApi(charSequence.toString(), juryNameTV);
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });

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
                        mChosenImageTV = imagePathTV;
                        chooseJuryImageIntent();
                    }
                });

                removeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mJuryLayout.removeView(addMoreLayout);
                        mJuryAddMoreList.remove(addMoreLayout);
                    }
                });
                mJuryLayout.addView(addMoreLayout);
                mJuryAddMoreList.add(addMoreLayout);
            }
        });

        mAwardAddMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout addMoreLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.award_layout,null);
                TextView remove = addMoreLayout.findViewById(R.id.award_remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAwardLayout.removeView(addMoreLayout);
                        mAwardAddMoreList.remove(addMoreLayout);
                    }
                });
                mAwardLayout.addView(addMoreLayout);
                mAwardAddMoreList.add(addMoreLayout);
            }
        });

        mAttachCoverImageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCoverImage();
            }
        });

        mJuryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChosenImageTV = mJuryImageTV;
                chooseJuryImage();
            }
        });

//        mJuryNameET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                userSearchApi(charSequence.toString());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mJuryNameET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                UserClass user = mUserArrayAdapter.getItem(pos);
                mJuryEmailET.setText(user.getEmail());
                mJuryContactET.setText(user.getMobileNumber());
                mJuryImageTV.setText(user.getProfilePicPath());

                mJuryNameET.setText(user.getName());
            }
        });

        mRemoveCoverTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttachCoverImageET.setText("");
                mRemoveCoverTV.setVisibility(View.GONE);
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
//                    mUserArrayAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view, mUsers);
                    mUserArrayAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view);
                    mJuryNameET.setAdapter(mUserArrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkUtil.handleSimpleVolleyRequestError(error, LaunchCompetitionActivity.this);
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
//                    mUserAddMoreAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view, mAddMoreUsers);
                    mUserAddMoreAdapter = new UserArrayAdapter(LaunchCompetitionActivity.this, R.layout.simple_text_view);
                    juryNameTV.setAdapter(mUserAddMoreAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkUtil.handleSimpleVolleyRequestError(error, LaunchCompetitionActivity.this);
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

    @Override
    public void onClick(final View v) {
        Log.d(TAG, "onClick: called");
        switch (v.getId()) {
            case R.id.launch_comp_startdate: {
                mPickerType = PickerType.START_DATE;
                break;

            }
            case R.id.launch_comp_close_date_reg: {
                mPickerType = PickerType.CLOSE_DATE_REGISTRATION;
                break;

            }
            case R.id.launch_comp_close_date_submission: {
                mPickerType = PickerType.CLOSE_DATE_SUBMISSION;
                break;

            }
            case R.id.launch_comp_announcedate: {
                mPickerType = PickerType.ANNOUNCEMENT_DATE;
                break;
            }
            default:
                break;
        }
        showDatePicker();
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

        updateLabel();

    }

    private void updateLabel() {
        Log.d(TAG, "updateLabel: called");

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String dateString = sdf.format(mCalendar.getTime());

        if (mPickerType == PickerType.START_DATE) {
            mStartDateET.setText(dateString);

        } else if (mPickerType == PickerType.CLOSE_DATE_REGISTRATION) {
            mCloseRegDateET.setText(dateString);

        } else if (mPickerType == PickerType.CLOSE_DATE_SUBMISSION) {
            mCloseSubmissionDateET.setText(dateString);

        } else if (mPickerType == PickerType.ANNOUNCEMENT_DATE) {
            mAnnouceDateET.setText(dateString);
        }
    }

    private void chooseCoverImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchCompetitionActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takeCoverPhotoIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    chooseCoverImageIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(LaunchCompetitionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
//            //buttonRetry.setVisibility(View.VISIBLE);
//        } else {
//            final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(LaunchCompetitionActivity.this);
//            builder.setItems(items, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int item) {
//
//                    if (items[item].equals("Take Photo")) {
//                        takeCoverPhotoIntent();
//
//                    } else if (items[item].equals("Choose from Gallery")) {
//                        chooseCoverImageIntent();
//
//                    } else if (items[item].equals("Cancel")) {
//                        dialog.dismiss();
//                    }
//                }
//            });
//            builder.show();
//        }
    }

    private void chooseJuryImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchCompetitionActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takeJuryPhotoIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    chooseJuryImageIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(LaunchCompetitionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
//            //buttonRetry.setVisibility(View.VISIBLE);
//        } else {
//            final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(LaunchCompetitionActivity.this);
//            builder.setItems(items, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int item) {
//
//                    if (items[item].equals("Take Photo")) {
//                        takeCoverPhotoIntent();
//
//                    } else if (items[item].equals("Choose from Gallery")) {
//                        chooseCoverImageIntent();
//
//                    } else if (items[item].equals("Cancel")) {
//                        dialog.dismiss();
//                    }
//                }
//            });
//            builder.show();
//        }
    }
    private void takeCoverPhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    showCoverCameraPreview(takePictureIntent);

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "This permission is needed to capture and store your cover image", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE);
                }
            } else {
                showCoverCameraPreview(takePictureIntent);
            }

        } else {
            Toast.makeText(this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
        }
    }

    private void takeJuryPhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    showJuryCameraPreview(takePictureIntent);

                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "This permission is needed to capture your cover image", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE);
                }
            } else {
                showJuryCameraPreview(takePictureIntent);
            }

        } else {
            Toast.makeText(this, "Your phone's camera isn't working or it doesn't have one", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCoverCameraPreview(Intent takePictureIntent) {
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
            startActivityForResult(takePictureIntent, REQUEST_COVER_IMAGE_CAPTURE);
        }
    }

    private void showJuryCameraPreview(Intent takePictureIntent) {
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
            startActivityForResult(takePictureIntent, REQUEST_JURY_IMAGE_CAPTURE);
        }
    }

    private void chooseCoverImageIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                chooseCoverFromGallery(chooseImageIntent);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "This permission is needed to choose image from gallery", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
        } else {
            chooseCoverFromGallery(chooseImageIntent);
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

    private void chooseJuryImageIntent() {
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

    private void chooseCoverFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, REQUEST_CHOOSE_COVER_IMAGE);
    }

    private void chooseJuryFromGallery(Intent chooseImageIntent) {
        chooseImageIntent.setType("image/*");
        startActivityForResult(chooseImageIntent, REQUEST_CHOOSE_JURY_IMAGE);
    }

    private void chooseImageIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, RESULT_CHOSEN_IMAGE);
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

        if (requestCode == REQUEST_PERMISSION_CAMERA_N_EXT_STORAGE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showCoverCameraPreview(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

            } else {
                Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseCoverFromGallery(new Intent(Intent.ACTION_GET_CONTENT));

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

        if (requestCode == REQUEST_COVER_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: called");
            Log.d(TAG, "onActivityResult: selected cover image path = " + mSelectedCoverImagePath);

            mAttachCoverImageET.setText(mSelectedCoverImagePath);
            mRemoveCoverTV.setVisibility(View.VISIBLE);

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

        } else if (requestCode == RESULT_CHOSEN_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedChosenImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedChosenImagePath);

            mChosenImageTV.setText(mSelectedChosenImagePath);

        } else if (requestCode == REQUEST_CHOOSE_COVER_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedCoverImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedCoverImagePath);

            mAttachCoverImageET.setText(mSelectedCoverImagePath);
            mRemoveCoverTV.setVisibility(View.VISIBLE);

        } else if (requestCode == REQUEST_JURY_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mJuryImageTV.setText(mSelectedCoverImagePath);


        } else if (requestCode == REQUEST_CHOOSE_JURY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            mSelectedCoverImagePath = FileUtils.getPath(this, uri);
            Log.d(TAG, "onActivityResult: selected chosen image path = " + mSelectedCoverImagePath);

            mJuryImageTV.setText(mSelectedCoverImagePath);

        }
    }


}

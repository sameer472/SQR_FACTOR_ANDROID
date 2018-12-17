package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.hackerkernel.user.sqrfactor.Adapters.UserArrayAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.MDToast;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Parser.JsonParser;
import com.hackerkernel.user.sqrfactor.Pojo.AlreadyExistingParticipantsClass;
import com.hackerkernel.user.sqrfactor.Pojo.Participant;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.hackerkernel.user.sqrfactor.UtilsClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipateActivity extends AppCompatActivity {
    private static final String TAG = "ParticipateActivity";

    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private UserClass user1Tv,user2Tv,user3Tv;
    private List<UserClass> mParticipants1List;
    private List<UserClass> mParticipants2List;
    private List<UserClass> mParticipants3List;
    private TextView participant2NameTV ,participant2EmailTV;
    private ArrayList<AlreadyExistingParticipantsClass> alreadyParticipants=new ArrayList<>();
    private ImageView participant2ImageIV ;
    private static final String PREFS_TAG = "SharedPrefs";
    private static final String PRODUCT_TAG = "MyProduct";

    private TextView participant3NameTV;
    private TextView participant3EmailTV;
    private ImageView participant3ImageIV ;
    private TextView mentorNameTV ;
    private TextView mentorEmailTV ;
    private ImageView mentorImageIV ;

    private List<UserClass> mMentorsList;

    private UserArrayAdapter mParticipant1Adapter;
    private UserArrayAdapter mParticipant2Adapter;
    private UserArrayAdapter mParticipant3Adapter;

    private UserArrayAdapter mMentorAdapter;

    private EditText mParticipant1TV;
    private AppCompatAutoCompleteTextView mParticipant2TV;
    private AppCompatAutoCompleteTextView mParticipant3TV;
    private AppCompatAutoCompleteTextView mMentorsACTV;

    private ArrayList<UserClass> storeParticipantForDisplayAgain=new ArrayList<>();
    private HashMap<String, AlreadyExistingParticipantsClass> storedParticipantsMap = new HashMap<String, AlreadyExistingParticipantsClass>();
    private UserClass userClass;

    private LinearLayout mTeamMembersLayout;
    private LinearLayout mMentorLayout;
    private Button mParticipateButton;

    private TextView mTeamMembersTV;
    private TextView mMentorTV;

    private LinearLayout mParticipant1Layout;
    private LinearLayout mParticipant2Layout;
    private LinearLayout mParticipant3Layout;

    private ProgressBar mPb;
    private LinearLayout mContentLayout;

    private String mCompetitionId;
    private boolean index1=false,index2=false,index3=false;

    private ImageView mP2RemoveIV;
    private ImageView mP3RemoveIV;
    private ImageView mMentorRemoveIV;

    private android.support.v7.widget.Toolbar mToolbar;

    private List<Participant> mSelectedParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);

        mSp = MySharedPreferences.getInstance(this);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        mParticipants1List = new ArrayList<>();
        mParticipants2List = new ArrayList<>();
        mParticipants3List = new ArrayList<>();

        mMentorsList = new ArrayList<>();

        mSelectedParticipants = new ArrayList<>();



        mParticipant1TV = findViewById(R.id.participate_participant1);
        mParticipant2TV = findViewById(R.id.participate_participant2);
        mParticipant3TV = findViewById(R.id.participate_participant3);

        mMentorsACTV = findViewById(R.id.participate_mentor);

        mTeamMembersTV = findViewById(R.id.participate_team_members);
        mMentorTV = findViewById(R.id.participate_mentor_tv);

        mTeamMembersLayout = findViewById(R.id.participate_team_members_layout);

        mParticipant1Layout = findViewById(R.id.participant1_layout);
        mParticipant2Layout = findViewById(R.id.participant2_layout);
        mParticipant3Layout = findViewById(R.id.participant3_layout);
        mMentorLayout = findViewById(R.id.participate_mentor_layout);

        mP2RemoveIV = findViewById(R.id.participate_remove_p2);
        mP3RemoveIV = findViewById(R.id.participate_remove_p3);
        mMentorRemoveIV = findViewById(R.id.participate_remove_mentor);

        mParticipateButton = findViewById(R.id.participate_confirm);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPb = findViewById(R.id.pb);
        mContentLayout = findViewById(R.id.content_layout);

        Intent i = getIntent();
        mCompetitionId = i.getStringExtra(BundleConstants.COMPETITION_ID);


        String name = mSp.getKey(SPConstants.NAME);
        final String userId = mSp.getKey(SPConstants.USER_ID);
        String profileImagePath = mSp.getKey(SPConstants.PROFILE_URL);
        String email = mSp.getKey(SPConstants.EMAIL);

        Log.d(TAG, "onCreate: name = " + name);
        Log.d(TAG, "onCreate: user id = " + userId);
        Log.d(TAG, "onCreate: profile url = " + profileImagePath);
        Log.d(TAG, "onCreate: email = " + email);

        mParticipant1TV.setText(name);
        final TextView participant1NameTV = findViewById(R.id.participant1_name);
        final TextView participant1EmailTV = findViewById(R.id.participant1_email);
        final ImageView participant1ImageIV = findViewById(R.id.participant1_image);


        mParticipant1Layout.setVisibility(View.VISIBLE);

       // String profileUrlFull = ServerConstants.IMAGE_BASE_URL + profileImagePath;

        //Picasso.get().load(profileImagePath).into(participant1ImageIV);

        Glide.with(getApplicationContext()).load(UtilsClass.getParsedImageUrl(profileImagePath))
                .into(participant1ImageIV);


        participant1NameTV.setText(name);
        participant1EmailTV.setText(email);
//        userClass=new UserClass(userId,name,profileImagePath,email,"9999999999");
//        storeParticipantForDisplayAgain.add(userClass);



        participant2NameTV = findViewById(R.id.participant2_name);
        participant2EmailTV = findViewById(R.id.participant2_email);
        participant2ImageIV = findViewById(R.id.participant2_image);

        String str = mParticipant2TV.getText().toString().trim();
        mParticipant2Adapter = new UserArrayAdapter(ParticipateActivity.this,android.R.layout.simple_dropdown_item_1line,str);

        mParticipant2TV.setAdapter(mParticipant2Adapter);



        mParticipant2TV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                UserClass user2Tv = mParticipant2Adapter.getItem(pos);
                participant2NameTV.setText(user2Tv.getName());
                participant2EmailTV.setText(user2Tv.getEmail());
                index1=true;


                Log.d(TAG, "onItemClick:  url = " + user2Tv.getProfilePicPath());

               // Picasso.get().load(UtilsClass.getParsedImageUrl(user2Tv.getProfilePicPath())).into(participant2ImageIV);

                Glide.with(getApplicationContext()).load(UtilsClass.getParsedImageUrl(user2Tv.getProfilePicPath()))
                        .into(participant2ImageIV);

                mTeamMembersTV.setVisibility(View.VISIBLE);
                mParticipant2Layout.setVisibility(View.VISIBLE);

                mParticipant2TV.setTag(user2Tv.getId());

                mParticipant2TV.setText(user2Tv.getName());

                mP2RemoveIV.setVisibility(View.VISIBLE);
                //storedParticipantsMap.put("PARTICIPANT1",user2Tv);
                //storeParticipantForDisplayAgain.add(user2Tv);

            }
        });

       participant3NameTV = findViewById(R.id.participant3_name);
       participant3EmailTV = findViewById(R.id.participant3_email);
       participant3ImageIV = findViewById(R.id.participant3_image);


        String str2 = mParticipant3TV.getText().toString().trim();
        mParticipant3Adapter = new UserArrayAdapter(ParticipateActivity.this,android.R.layout.simple_dropdown_item_1line,str2);

        mParticipant3TV.setAdapter(mParticipant3Adapter);

        mParticipant3TV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                UserClass user = mParticipant3Adapter.getItem(pos);

                participant3NameTV.setText(user.getName());
                participant3EmailTV.setText(user.getEmail());
                index2=true;

                Log.d(TAG, "onItemClick:  url = " + user.getProfilePicPath());
               // Picasso.get().load(UtilsClass.getParsedImageUrl(user.getProfilePicPath())).into(participant3ImageIV);

                Glide.with(getApplicationContext()).load(UtilsClass.getParsedImageUrl(user.getProfilePicPath()))
                        .into(participant3ImageIV);

                mTeamMembersTV.setVisibility(View.VISIBLE);
                mParticipant3Layout.setVisibility(View.VISIBLE);

               // Log.d(TAG, "onItemClick: user id = " + user.getId());

                mParticipant3TV.setTag(user.getId());

                mParticipant3TV.setText(user.getName());

                mP3RemoveIV.setVisibility(View.VISIBLE);
                //storedParticipantsMap.put("PARTICIPANT2",user);
               // storeParticipantForDisplayAgain.add(user);

            }
        });

        mentorNameTV = findViewById(R.id.mentor_name);
        mentorEmailTV = findViewById(R.id.mentor_email);
        mentorImageIV = findViewById(R.id.mentor_image);




        String str3 = mMentorsACTV.getText().toString().trim();
        mMentorAdapter = new UserArrayAdapter(ParticipateActivity.this,android.R.layout.simple_dropdown_item_1line,str3);

        mMentorsACTV.setAdapter(mMentorAdapter);

        mMentorsACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                UserClass user = mMentorAdapter.getItem(pos);
                index3=true;

                Log.d(TAG, "onItemClick: user name = " + user.getName());
                Log.d(TAG, "onItemClick: user email = " + user.getEmail());
                Log.d(TAG, "onItemClick:  url = " + user.getProfilePicPath());

                mentorNameTV.setText(user.getName());
                mentorEmailTV.setText(user.getEmail());

               // Picasso.get().load(UtilsClass.getParsedImageUrl(user.getProfilePicPath())).into(mentorImageIV);

                Glide.with(getApplicationContext()).load(UtilsClass.getParsedImageUrl(user.getProfilePicPath()))
                        .into(mentorImageIV);

                mMentorTV.setVisibility(View.VISIBLE);
                mMentorLayout.setVisibility(View.VISIBLE);

//                Log.d(TAG, "onItemClick: user id = " + user.getId());

                mMentorsACTV.setTag(user.getId());

                mMentorsACTV.setText(user.getName());

                mMentorRemoveIV.setVisibility(View.VISIBLE);
               // storedParticipantsMap.put("PARTICIPANT3",user);
               // storeParticipantForDisplayAgain.add(user);
            }
        });

//
        if(getIntent()!=null && getIntent().hasExtra(BundleConstants.COMPETITION_EDIT_BTN))
        {
//            Toast.makeText(this, "No internet@123"+mCompetitionId, Toast.LENGTH_SHORT).show();
            mCompetitionId=getIntent().getStringExtra(BundleConstants.COMPETITION_ID);
            GetParticipantDetailsAndBindToView();

        }


        mParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String participant1Name = mSp.getKey(SPConstants.NAME);
                String participate1Id = mSp.getKey(SPConstants.USER_ID);

                if(mSelectedParticipants!=null)
                {
                    mSelectedParticipants.clear();
                }

                Participant participant1 = new Participant(participant1Name, participate1Id);
                mSelectedParticipants.add(participant1);

                Log.d(TAG, "getParams: participant 1 name = " + participant1Name);
                Log.d(TAG, "getParams: participant 1 id = " + participate1Id);

                if (mParticipant2TV.getTag() != null) {
                    String participant2Name = mParticipant2TV.getText().toString();
                    String participant2Id = mParticipant2TV.getTag().toString();

                    Participant participant2 = new Participant(participant2Name, participant2Id);
                    mSelectedParticipants.add(participant2);

                    Log.d(TAG, "getParams: participant 2 name = " + participant2Name);
                    Log.d(TAG, "getParams: participant 2 id = " + participant2Id);
                }

                if (mParticipant3TV.getTag() != null) {
                    String participant3Name = mParticipant3TV.getText().toString();
                    String participant3Id = mParticipant3TV.getTag().toString();

                    Participant participant3 = new Participant(participant3Name, participant3Id);
                    mSelectedParticipants.add(participant3);

                    Log.d(TAG, "getParams: participant 3 name = " + participant3Name);
                    Log.d(TAG, "getParams: participant 3 id = " + participant3Id);
                }




                participateDataApi();

            }
        });

        mP2RemoveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParticipant2TV.setText("");
                mParticipant2TV.setTag(null);
                mParticipant2Layout.setVisibility(View.GONE);
                mP2RemoveIV.setVisibility(View.GONE);
                index1=false;
                //storedParticipantsMap.remove("PARTICIPANT1");
            }
        });

        mP3RemoveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParticipant3TV.setText("");
                mParticipant3TV.setTag(null);
                index2=false;
                mParticipant3Layout.setVisibility(View.GONE);
                mP3RemoveIV.setVisibility(View.GONE);
                //storedParticipantsMap.remove("PARTICIPANT2");
            }
        });


        mMentorRemoveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMentorsACTV.setText("");
                mMentorsACTV.setTag(null);
                index3=false;
                mMentorLayout.setVisibility(View.GONE);
                mMentorRemoveIV.setVisibility(View.GONE);
               // storedParticipantsMap.remove("PARTICIPANT3");
               // setDataFromSharedPreferences(storedParticipantsMap);
            }
        });

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

    public void GetParticipantDetailsAndBindToView(){



//        if (!NetworkUtil.isNetworkAvailable()) {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//            mPb.setVisibility(View.GONE);
//            mContentLayout.setVisibility(View.VISIBLE);
//            return;
//        }
//
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
//        mRequestQueue=
        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.EXISTING_PARTICIPATE_DATA_FOR_EDIT+mCompetitionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);


//                Toast.makeText(getApplicationContext(),"calling edit",Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                Log.d(TAG, "onResponse: participate existingdata response = " + response);

                try {

                    JSONObject jsonObject=new JSONObject(response);

                    Object aObj = jsonObject.get("participate1");
                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
//                    if(jsonObject.getJSONObject("participate1")!=null)
                    else
                    {
//                        Toast.makeText(getApplicationContext(),"Toast1",Toast.LENGTH_LONG).show();
                        AlreadyExistingParticipantsClass alreadyExistingParticipantsClass=new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("participate1"));
                        //storedParticipantsMap.put("Key2",alreadyExistingParticipantsClass);
                    }
                    aObj = jsonObject.get("participate2");

                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else
                    {
//                        Toast.makeText(getApplicationContext(),"Toast2",Toast.LENGTH_LONG).show();
                        if(jsonObject.getJSONObject("participate2")!=null && jsonObject.getJSONObject("participate2").length()!=0) {
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("participate2"));
                            storedParticipantsMap.put("Key2", alreadyExistingParticipantsClass);
                        }
                    }

                    aObj = jsonObject.get("participate3");

                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else {
                        if (jsonObject.getJSONObject("participate3") != null && jsonObject.getJSONObject("participate3").length() != 0) {
//                        Toast.makeText(getApplicationContext(),"Toast3",Toast.LENGTH_LONG).show();
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("participate3"));
                            storedParticipantsMap.put("Key3", alreadyExistingParticipantsClass);
                        }
                    }
                    aObj = jsonObject.get("participate4");

                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else {

                        if (jsonObject.getJSONObject("participate4") != null && jsonObject.getJSONObject("participate4").length() != 0) {
//                        Toast.makeText(getApplicationContext(),"Toast4",Toast.LENGTH_LONG).show();
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("participate4"));
                            storedParticipantsMap.put("Key4", alreadyExistingParticipantsClass);
                        }
                    }

                    aObj = jsonObject.get("participate5");
                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else {

                        if (jsonObject.getJSONObject("participate5") != null && jsonObject.getJSONObject("participate5").length() != 0) {
//                        Toast.makeText(getApplicationContext(),"Toast5",Toast.LENGTH_LONG).show();
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("participate5"));
                            storedParticipantsMap.put("Key5", alreadyExistingParticipantsClass);
                        }
                    }

                    aObj = jsonObject.get("mentor1");
                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else {

                        if (jsonObject.getJSONObject("mentor1") != null && jsonObject.getJSONObject("mentor1").length() != 0) {
//                        Toast.makeText(getApplicationContext(),"mentor1",Toast.LENGTH_LONG).show();
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("mentor1"));
                            storedParticipantsMap.put("Key6", alreadyExistingParticipantsClass);
                        }
                    }
                    aObj = jsonObject.get("mentor2");
                    if(aObj instanceof String){
                        //System.out.println(aObj);
                    }
                    else {

                        if (jsonObject.getJSONObject("mentor2") != null && jsonObject.getJSONObject("mentor2").length() != 0) {
//                        Toast.makeText(getApplicationContext(),"mentor2",Toast.LENGTH_LONG).show();
                            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass = new AlreadyExistingParticipantsClass(jsonObject.getJSONObject("mentor2"));
                            storedParticipantsMap.put("Key7", alreadyExistingParticipantsClass);
                        }
                    }


                    BindAlreadyExistingParticipants(storedParticipantsMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    try {
                        String body = new String(error.networkResponse.data, "UTF-8");
                        Log.d("handleSimpleVolley: ", body);
                        if (statusCode == 400 || statusCode == 401) {
                            //server error
                            String errorMsg = JsonParser.SimpleParser(body);
//                            Toast.makeText(ParticipateActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                        } else if (statusCode == 422) {
                            JSONObject object = new JSONObject(body);
                            JSONObject responseObj = object.getJSONObject("Response");
                            String message = responseObj.getString("message");

//                            Toast.makeText(ParticipateActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorString = MyVolley.handleVolleyError(error);
//                            Toast.makeText(ParticipateActivity.this, errorString, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        NetworkUtil.showParsingErrorAlert(ParticipateActivity.this);
                    }
                } else {
                    String errorString = MyVolley.handleVolleyError(error);
//                    Toast.makeText(ParticipateActivity.this, errorString, Toast.LENGTH_SHORT).show();
                }
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

                params.put("user_id", mSp.getKey(SPConstants.USER_ID));
                Log.d(TAG, "getParams: competition id = " + mCompetitionId);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

}



public void BindAlreadyExistingParticipants(HashMap<String,AlreadyExistingParticipantsClass> storedParticipantsMap ) {


//        Toast.makeText(getApplicationContext(),"binding",Toast.LENGTH_LONG).show();


            if (storedParticipantsMap.get("Key2")!=null) {
//                Toast.makeText(getApplicationContext(),"key2@123",Toast.LENGTH_LONG).show();
                AlreadyExistingParticipantsClass alreadyExistingParticipantsClass =storedParticipantsMap.get("Key2");

                if(!alreadyExistingParticipantsClass.getName().equals("null"))
                {
                    participant2NameTV.setText(alreadyExistingParticipantsClass.getName());
                    mParticipant2TV.setText(alreadyExistingParticipantsClass.getName());
                }
                else {
                    participant2NameTV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());
                    mParticipant2TV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());
                }

                participant2EmailTV.setText(alreadyExistingParticipantsClass.getEmail());
                index1 = true;

                //Picasso.get().load(UtilsClass.getParsedImageUrl(alreadyExistingParticipantsClass.getProfile())).into(participant2ImageIV);
                Glide.with(this).load(UtilsClass.getParsedImageUrl(alreadyExistingParticipantsClass.getProfile()))
                        .into(participant2ImageIV);
                mTeamMembersTV.setVisibility(View.VISIBLE);
                mParticipant2Layout.setVisibility(View.VISIBLE);

                // Log.d(TAG, "onItemClick: user id = " + user.getId());

                mParticipant2TV.setTag(alreadyExistingParticipantsClass.getId());

//                mParticipant2TV.setText(alreadyExistingParticipantsClass.getName());
//                Log.v("p1data", mParticipant2TV.getText().toString() + " " + mParticipant2TV.getText().toString());

                mP2RemoveIV.setVisibility(View.VISIBLE);
            }
        if (storedParticipantsMap.get("Key3")!=null) {
//            Toast.makeText(getApplicationContext(),"key3@123",Toast.LENGTH_LONG).show();
            AlreadyExistingParticipantsClass alreadyExistingParticipantsClass =storedParticipantsMap.get("Key3");

            if(!alreadyExistingParticipantsClass.getName().equals("null"))
            {
                participant3NameTV.setText(alreadyExistingParticipantsClass.getName());
                mParticipant3TV.setText(alreadyExistingParticipantsClass.getName());
            }
            else {
                participant3NameTV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());
                mParticipant3TV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());

            }



                participant3EmailTV.setText(alreadyExistingParticipantsClass.getEmail());
                index2 = true;

                //Picasso.get().load(userClass.getProfilePicPath()).into(participant3ImageIV);
                Glide.with(this).load(UtilsClass.getParsedImageUrl(alreadyExistingParticipantsClass.getProfile()))
                        .into(participant3ImageIV);

                mTeamMembersTV.setVisibility(View.VISIBLE);
                mParticipant3Layout.setVisibility(View.VISIBLE);
                mParticipant3TV.setTag(alreadyExistingParticipantsClass.getId());


                Log.v("p2data", mParticipant3TV.getText().toString() + " " + mParticipant3TV.getText().toString());

                mP3RemoveIV.setVisibility(View.VISIBLE);
                // storeParticipantForDisplayAgain.add(user);
            }
        if (storedParticipantsMap.get("Key6")!=null) {

//            Toast.makeText(getApplicationContext(),"key6@123",Toast.LENGTH_LONG).show();
              AlreadyExistingParticipantsClass alreadyExistingParticipantsClass =storedParticipantsMap.get("Key6");

            if(!alreadyExistingParticipantsClass.getName().equals("null"))
            {
                mentorNameTV.setText(alreadyExistingParticipantsClass.getName());
                mMentorsACTV.setText(alreadyExistingParticipantsClass.getName());
            }
            else {
                mentorNameTV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());
                mMentorsACTV.setText(alreadyExistingParticipantsClass.getFirst_name()+" "+alreadyExistingParticipantsClass.getLast_name());
            }

                mentorEmailTV.setText(alreadyExistingParticipantsClass.getEmail());
                index3 = true;

                Glide.with(this).load(UtilsClass.getParsedImageUrl(alreadyExistingParticipantsClass.getProfile()))
                        .into(mentorImageIV);
                mMentorTV.setVisibility(View.VISIBLE);
                mMentorLayout.setVisibility(View.VISIBLE);


                mMentorsACTV.setTag(alreadyExistingParticipantsClass.getId());
              //  mMentorsACTV.setText(alreadyExistingParticipantsClass.getName());
                mMentorRemoveIV.setVisibility(View.VISIBLE);
                //storeParticipantForDisplayAgain.add(userClass);
            }


    }






    @Override
    protected void onResume() {
        super.onResume();

    }

    private void participateDataApi() {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);

        if((!mParticipant2TV.getText().toString().equals("") && index1==false))
        {

            MDToast.makeText(this, mParticipant2TV.getText().toString()+index1+" is not s registered user ,registerd first now", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
         }
        else if((!mParticipant3TV.getText().toString().equals("") && index2==false))
        {

            MDToast.makeText(this, mParticipant3TV.getText().toString()+index2+" is not s registered user ,registerd first now", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }
        else if((!mMentorsACTV.getText().toString().equals("") && index3==false))
        {

            MDToast.makeText(this, mMentorsACTV.getText().toString()+index3+" is not s registered user ,registerd first now", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

       else {

//            Toast.makeText(getApplicationContext(),"all data available "+ mSelectedParticipants.size() ,Toast.LENGTH_LONG).show();
            if (!NetworkUtil.isNetworkAvailable()) {
                Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                return;
            }
//
            mRequestQueue = MyVolley.getInstance().getRequestQueue();
//
           // mRequestQueue= Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.PARTICIPATE_DATA+mCompetitionId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    mPb.setVisibility(View.GONE);
                    mContentLayout.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: participate data response = " + response);

                    try {
                        //Toast.makeText(ParticipateActivity.this, "Participation Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ParticipateActivity.this, PaymentConfirmActivity.class);
                        i.putExtra(BundleConstants.COMPETITION_ID, mCompetitionId);
                        startActivity(i);
                        finish();

                    } catch (Error e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mPb.setVisibility(View.GONE);
                    mContentLayout.setVisibility(View.VISIBLE);

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        try {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            Log.d("handleSimpleVolley: ", body);
                            if (statusCode == 400 || statusCode == 401) {
                                //server error
                                String errorMsg = JsonParser.SimpleParser(body);
                                Toast.makeText(ParticipateActivity.this, statusCode+errorMsg, Toast.LENGTH_LONG).show();

                            } else if (statusCode == 422) {
                                JSONObject object = new JSONObject(body);
                                JSONObject responseObj = object.getJSONObject("Response");
                                String message = responseObj.getString("message");

                                Toast.makeText(ParticipateActivity.this, statusCode+message, Toast.LENGTH_SHORT).show();
                            } else {
                                String errorString = MyVolley.handleVolleyError(error);
                                Toast.makeText(ParticipateActivity.this, statusCode+errorString, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                            NetworkUtil.showParsingErrorAlert(ParticipateActivity.this);
                        }
                    } else {
                        String errorString = MyVolley.handleVolleyError(error);
                        Toast.makeText(ParticipateActivity.this, errorString, Toast.LENGTH_SHORT).show();
                    }
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

                    params.put("compition_id", mCompetitionId);
                    Log.d(TAG, "getParams: competition id = " + mCompetitionId);

                    for (int i = 0; i < 3; i++ ) {
                        if(i<mSelectedParticipants.size())
                        {
                            Participant participant = mSelectedParticipants.get(i);
                            Log.v("name@123", participant.getName()+" "+participant.getId());
                            params.put("participate[" + i + "]", participant.getName());
                            params.put("participate_id[" + i + "]", participant.getId());
                        }
                        else {
                            params.put("participate[" + i + "]", "");
                            params.put("participate_id[" + i + "]","");
                        }

                    }

                    if (mMentorsACTV.getTag() != null) {
                        //Toast.makeText(ParticipateActivity.this, "edititng", Toast.LENGTH_SHORT).show();
                        String mentorName = mMentorsACTV.getText().toString();
                        String mentorId = mMentorsACTV.getTag().toString();
                        params.put("mentor[0]", mentorName);
                        params.put("mentor_id[0]", mentorId);
                        Log.d(TAG, "getParams: mentor name = " + mentorName);
                        Log.d(TAG, "getParams: mentor id = " + mentorId);

                        }
                    else {
                        params.put("mentor[0]","");
                        params.put("mentor_id[0]","");
                    }

                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(request);

        }


    }
//    private void setDataFromSharedPreferences(HashMap<String, UserClass> storedParticipantsMap)
//    {
//        Gson gson = new Gson();
//        String jsonCurProduct = gson.toJson(storedParticipantsMap);
//
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(PRODUCT_TAG, jsonCurProduct);
//        editor.commit();
//    }
//
//    private HashMap<String,UserClass > getDataFromSharedPreferences(){
//        Gson gson = new Gson();
//
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
//        String jsonPreferences = sharedPref.getString(PRODUCT_TAG, "");
//        java.lang.reflect.Type type = new TypeToken<HashMap<String, UserClass>>(){}.getType();
//        HashMap<String,UserClass > testHashMap2 = gson.fromJson(jsonPreferences, type);
//        return testHashMap2;
//    }

//    private void userSearchApiP2(final String searchQuery) {
//        Log.d(TAG, "userSearchApiP1: called");
//        Log.d(TAG, "userSearchApiP1: serach query = " + searchQuery);
////        mUsers.clear();
//        mParticipants2List = new ArrayList<>();
//
//        if (!NetworkUtil.isNetworkAvailable()) {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mRequestQueue = MyVolley.getInstance().getRequestQueue();
//
//        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse: user search response = " + response);
//
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//
//                    JSONArray usersArray = responseObject.getJSONArray("users");
//
//                    for (int i = 0; i < usersArray.length(); i++) {
//                        JSONObject singleObj = usersArray.getJSONObject(i);
//
//                        String id = singleObj.getString("id");
//                        String name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
//                        String profileUrl = singleObj.getString("profile");
//                        String email = singleObj.getString("email");
//                        String mobileNumber = singleObj.getString("mobile_number");
//
//                        UserClass user = new UserClass(id, name, profileUrl, email, mobileNumber);
//                        mParticipants2List.add(user);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkUtil.handleSimpleVolleyRequestError(error, ParticipateActivity.this);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                final Map<String, String> headers = new HashMap<>();
//                headers.put(getString(R.string.accept), getString(R.string.application_json));
//                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("search", searchQuery);
//
//                return params;
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mRequestQueue.add(request);
//    }
//
//    private void userSearchApiP3(final String searchQuery) {
//        Log.d(TAG, "userSearchApiP1: called");
//        Log.d(TAG, "userSearchApiP1: serach query = " + searchQuery);
////        mUsers.clear();
//        mParticipants3List = new ArrayList<>();
//
//        if (!NetworkUtil.isNetworkAvailable()) {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mRequestQueue = MyVolley.getInstance().getRequestQueue();
//
//        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse: user search response = " + response);
//
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//
//                    JSONArray usersArray = responseObject.getJSONArray("users");
//
//                    for (int i = 0; i < usersArray.length(); i++) {
//                        JSONObject singleObj = usersArray.getJSONObject(i);
//
//                        String id = singleObj.getString("id");
//                        String name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
//                        String profileUrl = singleObj.getString("profile");
//                        String email = singleObj.getString("email");
//                        String mobileNumber = singleObj.getString("mobile_number");
//
//                        UserClass user = new UserClass(id, name, profileUrl, email, mobileNumber);
//                        mParticipants3List.add(user);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkUtil.handleSimpleVolleyRequestError(error, ParticipateActivity.this);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                final Map<String, String> headers = new HashMap<>();
//                headers.put(getString(R.string.accept), getString(R.string.application_json));
//                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("search", searchQuery);
//
//                return params;
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mRequestQueue.add(request);
//    }
//
//    private void userSearchApiMentor(final String searchQuery) {
//        Log.d(TAG, "userSearchApiP1: called");
//        Log.d(TAG, "userSearchApiP1: serach query = " + searchQuery);
////        mUsers.clear();
//        mMentorsList= new ArrayList<>();
//
//        if (!NetworkUtil.isNetworkAvailable()) {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mRequestQueue = MyVolley.getInstance().getRequestQueue();
//
//        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse: user search response = " + response);
//
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//
//                    JSONArray usersArray = responseObject.getJSONArray("users");
//
//                    for (int i = 0; i < usersArray.length(); i++) {
//                        JSONObject singleObj = usersArray.getJSONObject(i);
//
//                        String id = singleObj.getString("id");
//                        String name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
//                        String profileUrl = singleObj.getString("profile");
//                        String email = singleObj.getString("email");
//                        String mobileNumber = singleObj.getString("mobile_number");
//
//                        UserClass user = new UserClass(id, name, profileUrl, email, mobileNumber);
//                        mMentorsList.add(user);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkUtil.handleSimpleVolleyRequestError(error, ParticipateActivity.this);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                final Map<String, String> headers = new HashMap<>();
//                headers.put(getString(R.string.accept), getString(R.string.application_json));
//                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("search", searchQuery);
//
//                return params;
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mRequestQueue.add(request);
//    }
}
package com.hackerkernel.user.sqrfactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.hackerkernel.user.sqrfactor.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class ProfileActivity extends ToolbarActivity {
    private ArrayList<ProfileClass1> profileClassList = new ArrayList<>();
    private ImageView displayImage, camera;
    public ImageButton mRemoveButton;
    private ProfileAdapter profileAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    private ImageView morebtn, btn,coverImage,profileImage,profileStatusImage,profile_status_image;
    private TextView writePost,profileName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    Button btnSubmit,editProfile;
    Bitmap bitmap;
    private Uri uri;
    boolean flag = false;
    LinearLayoutManager layoutManager;
    TextView blueprint, portfolio, followers, following,profile_profile_address,profile_short_bio;
    private UserClass userClass;
    private static String nextPageUrl;
    private boolean isLoading=false;
    private static Context context;
    public  static final int RequestPermissionCode  = 1 ;
    final int PIC_CROP = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private Intent camIntent,gallIntent;

    private CoordinatorLayout mCLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCToolbarLayout;
    private TextView noData;

    FloatingActionButton fabView, fabStatus, fabDesign, fabArticle;
    private boolean fabExpanded = false;
    private LinearLayout layoutFabStatus;
    private LinearLayout layoutFabDesign;
    private LinearLayout layoutFabArticle;
    private ProgressBar progressBar;
    Animation rotate_forward, rotate_Backward, fab_open, fab_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);




        progressBar=findViewById(R.id.progress_bar_profile);
        noData= findViewById(R.id.noData);
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mCToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(mToolbar);

        mCToolbarLayout.setTitle("Profile");
        mCToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        mCToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.profile_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        profileAdapter = new ProfileAdapter(profileClassList,this);
        recyclerView.setAdapter(profileAdapter);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        profile_profile_address=findViewById(R.id.profile_profile_address);
        profile_short_bio=findViewById(R.id.profile_short_bio);

        editProfile = (Button)findViewById(R.id.profile_editprofile);
        coverImage = (ImageView) findViewById(R.id.profile_cover_image);
        profileImage = (ImageView) findViewById(R.id.profile_profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        android.app.AlertDialog.Builder imageDialog = new android.app.AlertDialog.Builder(ProfileActivity.this);
                        LayoutInflater inflater = (LayoutInflater) ProfileActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                                (ViewGroup) findViewById(R.id.layout_root));
                        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
                        image.setImageDrawable(profileImage.getDrawable());
                        imageDialog.setView(layout);
                        TextView edittext = (TextView)layout.findViewById(R.id.custom_fullimage_placename);
                        edittext.setVisibility(View.VISIBLE);
                        imageDialog.setPositiveButton(getResources().getString(R.string.Edit), new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(ProfileActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }

                                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                                        Manifest.permission.CAMERA))
                                {

//                    Toast.makeText(ProfileActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                                } else {

                                    ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{
                                            Manifest.permission.CAMERA}, RequestPermissionCode);

                                }
                                selectImage();

                            }

                        });

                        imageDialog.create();
                        imageDialog.show();
                    }
                });

        profileName =(TextView)findViewById(R.id.profile_profile_name);


        if(userClass.getUser_address()!=null && !userClass.getUser_address().equals("null"))
            profile_profile_address.setText(userClass.getUser_address());

        if(userClass.getShort_bio()!=null && !userClass.getShort_bio().equals("null"))
            profile_short_bio.setText(userClass.getShort_bio());

        Glide.with(this).load(UtilsClass.getParsedImageUrl(userClass.getProfile()))
                    .into(profileImage);

        profileName.setText(UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name()));


//        profileStatusImage = (ImageView) findViewById(R.id.profile_status_image);
        followCnt = (TextView) findViewById(R.id.profile_followerscnt);
        followingCnt = (TextView) findViewById(R.id.profile_followingcnt);
        portfolioCnt = (TextView) findViewById(R.id.profile_portfoliocnt);
        bluePrintCnt = (TextView) findViewById(R.id.profile_blueprintcnt);

        followCnt.setText(userClass.getFollowerCount());
        followingCnt.setText(userClass.getFollowingCount());
        bluePrintCnt.setText(userClass.getBlueprintCount());
        portfolioCnt.setText(userClass.getPortfolioCount());


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && fabView.isShown())
                {
                    fabView.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fabView.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fabView = findViewById(R.id.fab_view);
        fabStatus = findViewById(R.id.fab_status);
        fabDesign = findViewById(R.id.fab_design);
        fabArticle = findViewById(R.id.fab_article);

        layoutFabStatus = (LinearLayout)findViewById(R.id.layoutFabStatus);
        layoutFabDesign = (LinearLayout) findViewById(R.id.layoutFabDesign);
        layoutFabArticle = (LinearLayout) findViewById(R.id.layoutFabArticle);

        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_Backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        closeSubMenusFab();

        fabStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, StatusPostActivity.class);
                intent.putExtra("Fab",1);
                startActivity(intent);
            }
        });
        fabDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, DesignActivity.class);
                intent.putExtra("Fab",1);
               startActivity(intent);
            }
        });
        fabArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ArticleActivity.class);
                intent.putExtra("Fab",1);
                startActivity(intent);
            }
        });
        morebtn = (ImageView)findViewById(R.id.profile_about_morebtn);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenu().add(1,1,0,"About "+UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name()));
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case 1:
                                Intent i = new Intent(getApplicationContext(), About.class);
                                i.putExtra("UserID",userClass.getUserId());
                                i.putExtra("userType",userClass.getUserType());
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        blueprint = (TextView)findViewById(R.id.profile_blueprintClick);
        portfolio = (TextView)findViewById(R.id.profile_portfolioClick);
        followers = (TextView)findViewById(R.id.profile_followersClick);
        following = (TextView)findViewById(R.id.profile_followingClick);



        blueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ProfileActivity.this, BlueprintActivity.class);
//                startActivity(i);
                LoadData();
            }
        });

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PortfolioActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowersActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowingActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        LoadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isLoading=false;

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=layoutManager.findLastVisibleItemPosition();
                if(dy>0 && lastId + 2 > layoutManager.getItemCount() && !isLoading)
                {
                    isLoading=true;
                    LoadMoreDataFromServer();

                }
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void openSubMenusFab(){
        layoutFabStatus.setVisibility(View.VISIBLE);
        layoutFabDesign.setVisibility(View.VISIBLE);
        layoutFabArticle.setVisibility(View.VISIBLE);
        fabStatus.startAnimation(fab_open);
        fabDesign.setAnimation(fab_open);
        fabArticle.setAnimation(fab_open);
        fabView.startAnimation(rotate_forward);
        fabView.setImageResource(R.drawable.ic_add_black_24dp);
        fabExpanded = true;
    }
    private void closeSubMenusFab(){
        layoutFabStatus.setVisibility(View.GONE);
        layoutFabDesign.setVisibility(View.GONE);
        layoutFabArticle.setVisibility(View.GONE);
        fabStatus.startAnimation(fab_close);
        fabDesign.setAnimation(fab_close);
        fabArticle.setAnimation(fab_close);
        fabView.startAnimation(rotate_Backward);
        fabExpanded = false;
    }
    public void LoadData(){


        if(profileClassList!= null){
            profileClassList.clear();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"profile/detail/"+userClass.getUser_name(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        noData.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl = jsonObject.getString("nextPage");

                            JSONObject user=jsonObject.getJSONObject("user");

                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            if (jsonPost!=null)
                            {
                                JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                    profileClassList.add(profileClass1);
                                }
                                profileAdapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",userClass.getUser_name());
                return params;
            }

        };


        requestQueue.add(myReq);
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(profileClassList.size()==0)
                {
                    progressBar.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);

                }else {
                    progressBar.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                }
            }
        }, 2000);


    }
    public void LoadMoreDataFromServer(){

        final ArrayList<ProfileClass1> NewprofileClassList = new ArrayList<>();
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        if(nextPageUrl!=null) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest myReq = new StringRequest(Request.Method.POST, nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.v("ReponseFeed", response);
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                nextPageUrl = jsonObject.getString("nextPage");
                                JSONObject jsonPost = jsonObject.getJSONObject("posts");
                                if (jsonPost != null) {
                                    JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                        NewprofileClassList.add(profileClass1);
                                    }
                                    profileClassList.addAll(NewprofileClassList);
                                    profileAdapter.notifyDataSetChanged();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer " + TokenClass.Token);

                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userClass.getUser_name() );
                    return params;
                }
            };


            requestQueue.add(myReq);
        }


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

                    camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    uri=Uri.fromFile(f);
//                   uri = FileProvider.getUriForFile( ProfileActivity.this,ProfileActivity.this.getPackageName() + ".provider", f);
                    Log.v("uriCamara",uri+"");
                    camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    camIntent.putExtra("return-data", true);

                    startActivityForResult(camIntent, 1);

                }

                else if (options[item].equals("Choose from Gallery"))

                {


                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, 2);


                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                performCrop();

            }


            else if (requestCode == 2) {


                if(data!=null)
                {

                    uri = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    //uri=picturePath;
                    Log.v("uriGallary",picturePath+"   *****  "+uri);
                    cursor.close();
                    performCropFromGallary(picturePath);
                    //ImageView imageView = (ImageView) findViewById(R.id.imgView);
                    //profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }



            }
            else if(requestCode == 6)
            {

                if(data!=null)
                {
                    Bundle extras = data.getExtras();
                    bitmap  = extras.getParcelable("data");
                    profileImage.setImageBitmap(bitmap);
                    ChangeProfile();
                }

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
    private void performCrop(){
        try {

            Log.v("uri1",uri+"");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 640);
            cropIntent.putExtra("outputY", 640);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 6);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void performCropFromGallary(String picUri){
        try {

            Log.v("uri1",uri+"");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);
            cropIntent.setDataAndType(contentUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 640);
            cropIntent.putExtra("outputY", 640);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 6);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void ChangeProfile(){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest myReq = new StringRequest(Request.Method.POST,  UtilsClass.baseurl+"parse/change_profile",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // Log.v("ReponseFeed", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = mPrefs.getString("MyObject", "");
                                UserClass userClass = gson.fromJson(json, UserClass.class);


                                userClass.setProfile(jsonObject.getJSONObject("profilepic").getString("profile"));
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                json = gson.toJson(userClass);

                                prefsEditor.putString("MyObject", json);
                                prefsEditor.apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer " + TokenClass.Token);

                    return params;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    String image = getStringImage(bitmap);
                    params.put("profile_image","data:image/jpeg;base64,"+image );
                    return params;
                }

            };


            requestQueue.add(myReq);
        }



    }
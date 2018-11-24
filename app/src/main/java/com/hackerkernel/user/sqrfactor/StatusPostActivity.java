package com.hackerkernel.user.sqrfactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.hackerkernel.user.sqrfactor.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class StatusPostActivity extends AppCompatActivity {

    private  Uri uri;
    private ImageView displayImage;
    private ImageView camera;// button
    public ImageButton mRemoveButton;
    RecyclerView recyclerView;
    private NewsFeedStatus newsFeedStatus;
    Button like, comment, share, like2;
    String token;
    SharedPreferences sharedPreferences;
    ImageView user_profile_photo;
    String message, encodedImage;
    private boolean isScrolling;
    int currentItems,totalItems,scrolledItems;
    private ProgressBar progressBar;
    TextView btnSubmit;
    EditText writePost;
    private boolean isEdit=false;
    FrameLayout frameLayout;
    private String slug;
    private int postId;
    private ProgressDialog pDialog;
    public static String UPLOAD_URL = UtilsClass.baseurl+"post";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    LinearLayoutManager layoutManager;
    private NewsFeedAdapter newsFeedAdapter;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private Toolbar toolbar;
    public  static final int RequestPermissionCode  = 1 ;
    final int PIC_CROP = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Status");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);

        user_profile_photo = findViewById(R.id.statusProfileImage);
        Glide.with(this).load(UtilsClass.getParsedImageUrl(userClass.getProfile()))
                .into(user_profile_photo);

        camera = findViewById(R.id.news_camera);
        displayImage = findViewById(R.id.news_upload_image);
        btnSubmit = findViewById(R.id.news_postButton);
        writePost = findViewById(R.id.write_status);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        frameLayout = findViewById(R.id.rl);
        frameLayout.setVisibility(View.GONE);
        jsonObject = new JSONObject();

        Intent intent1=getIntent();
        if(intent1!=null && intent1.hasExtra("Post_Slug_ID"))
        {
//            Toast.makeText(this,intent.getStringExtra("Post_Slug_ID"),Toast.LENGTH_LONG).show();
            isEdit=true;
            postId=intent1.getIntExtra("Post_ID",0);
            slug=intent1.getStringExtra("Post_Slug_ID");
            FetchDataFromServerAndBindToViews(intent1.getStringExtra("Post_Slug_ID"));
        }

        mRemoveButton = findViewById(R.id.ib_remove);
        displayImage.setVisibility(View.GONE);
        mRemoveButton.setVisibility(View.GONE);

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                displayImage.setImageBitmap(null);
                displayImage.setVisibility(View.GONE);
                mRemoveButton.setVisibility(View.GONE);


            }

        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                displayImage.setVisibility(View.VISIBLE);
                mRemoveButton.setVisibility(View.VISIBLE);

                if (ContextCompat.checkSelfPermission(StatusPostActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(StatusPostActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(StatusPostActivity.this,
                        android.Manifest.permission.CAMERA))
                {

                    Toast.makeText(StatusPostActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(StatusPostActivity.this,new String[]{
                            Manifest.permission.CAMERA}, RequestPermissionCode);

                }
                selectImage();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(UtilsClass.IsConnected(StatusPostActivity.this))
                {

                    if (isEdit) {
                        uploadEditedStatusToServer();
                    } else {
                        uploadImage();
                    }
                }
                else {
                    MDToast.makeText(StatusPostActivity.this, "Check Your Internet Connection", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                }
                }
        });

    }

    private void uploadEditedStatusToServer() {
        Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"status/edit-status",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
                        writePost.setText("");
                        displayImage.setVisibility(View.GONE);
                        mRemoveButton.setVisibility(View.GONE);
                        Intent intent=new Intent(StatusPostActivity.this,HomeScreen.class);
                        startActivity(intent);

//                        //Showing toast message of the response
                        Log.v("response",s);
//                        Toast.makeText(getApplicationContext(), "response"+s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.v("response",volleyError.toString());
//                        Toast.makeText(getApplicationContext(), "response"+volleyError.toString() , Toast.LENGTH_LONG).show();
                        //Showing toast
                        // Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String image = getStringImage(bitmap);

                Log.v("data",slug+" "+writePost.getText().toString()+" "+image);
                params.put("image","data:image/jpeg;base64,"+image);
                params.put("post_slug",slug);
                params.put("description",writePost.getText().toString().trim());

                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
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

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    displayImage.setImageBitmap(bitmap);



                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";



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

                bitmap = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", fileName[fileName.length-1]+"");

                displayImage.setImageBitmap(bitmap);

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
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
//                        //Showing toast message of the response
                        Log.v("response",s);
                        writePost.setText("");
                        displayImage.setVisibility(View.GONE);
                        mRemoveButton.setVisibility(View.GONE);


//                        Toast.makeText(getApplicationContext(), "response"+s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.v("response",volleyError.toString());
//
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String image = getStringImage(bitmap);

                Log.v("ImageUrl","data:image/jpeg;base64,"+image+" "+writePost.getText().toString().trim());
                params.put("image_value","data:image/jpeg;base64,"+image);
                params.put("description",writePost.getText().toString().trim());

                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);

    }

    private void FetchDataFromServerAndBindToViews(String post_slug_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"status/edit/"+post_slug_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("StatusPost",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("statusPostEdit");
                            final ArticleEditClass statusPostEdit = new ArticleEditClass(jsonObjectFullPost);
                            writePost.setText(statusPostEdit.getDescription());
                            Log.v("url","https://archsqr.in/"+statusPostEdit.getImage());
                            displayImage.setVisibility(View.VISIBLE);
                            frameLayout.setVisibility(View.VISIBLE);
                            mRemoveButton.setVisibility(View.VISIBLE);
//                            Glide.with(getApplicationContext()).load("https://archsqr.in/"+statusPostEdit.getImage())
//                                    .into(displayImage);


                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(UtilsClass.baseurl1+statusPostEdit.getImage())
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                                    (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);
                                            // imageString=getStringImage(resource);
                                            displayImage.setImageBitmap(bitmapResized);
                                            bitmap=bitmapResized;
                                        }
                                    });

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

        };
        requestQueue.add(myReq);
    }
}

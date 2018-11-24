package com.hackerkernel.user.sqrfactor;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusPostFragment extends Fragment {
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
    private ProgressDialog pDialog;
    public static String UPLOAD_URL = UtilsClass.baseurl+"post";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    LinearLayoutManager layoutManager;
    private NewsFeedAdapter newsFeedAdapter;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;

    public StatusPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status_post, container, false);

        camera = v.findViewById(R.id.news_camera);
        displayImage = v.findViewById(R.id.news_upload_image);
        btnSubmit = v.findViewById(R.id.news_postButton);
        writePost = v.findViewById(R.id.write_status);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        final FrameLayout frameLayout = v.findViewById(R.id.rl);
        frameLayout.setVisibility(View.GONE);

        mRemoveButton = v.findViewById(R.id.ib_remove);
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
                showFileChooser();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        }

        return v;
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
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
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
//                        //Showing toast message of the response
                        Log.v("response",s);
                        Toast.makeText(getContext(), "response"+s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.v("response",volleyError.toString());
                        Toast.makeText(getContext(), "response"+volleyError.toString() , Toast.LENGTH_LONG).show();
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

                Log.v("ImageUrl","data:image/jpeg;base64,"+image);
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

    private void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data !=null){
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filepath);
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5),(int)(bitmap.getHeight()*0.5),false );
                displayImage.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void finish() {
        super.getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);

    }
}

package com.hackerkernel.user.sqrfactor;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    private Toolbar toolbar;
    private Editor editor;
    private Editor editor1;
    private Uri uri;
    private String finalHtml;
    //private String videoLink;
    String cropedImage;
    private WebView videoView;
    private String html;
    private String imageString;
    private Button saveArticleButton, video_post_close;
    private EditText articleTitle, articleShortDescription, articleTag;
    private TextView articleSelectBannerImage, articleUserName;
    // TextInputLayout articleSelectBannerImage;
    private ImageView articleCustomBaneerImage, cropFinalImage, profileImage, selectBanerImageIcon, banner_attachment_image;
    private FrameLayout videoFrameLayout;
    private ImageButton article_insert_video;
    Intent CamIntent, GalIntent, CropIntent;
    public static final int RequestPermissionCode = 1;
    private View v;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_article, container, false);
        ContinueAfterPermission();
        return v;
    }
    public void ContinueAfterPermission() {

        //adding text editor
        articleTitle = v.findViewById(R.id.articleTitle);
        articleTitle.setFocusable(true);

        //videoView=(WebView)findViewById(R.id.articleVideoView);
        articleShortDescription = v.findViewById(R.id.articleShortDescription);
        articleSelectBannerImage = v.findViewById(R.id.articleSelectBaneerImage);
        selectBanerImageIcon= v.findViewById(R.id.selectBanerImageIcon);

        article_insert_video=(ImageButton)v.findViewById(R.id.article_insert_video);
        article_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });
        banner_attachment_image=(ImageView)v.findViewById(R.id.banner_attachment_image);

        videoFrameLayout=(FrameLayout)v.findViewById(R.id.videoFrameLayout);
        video_post_close=(Button)v.findViewById(R.id.video_post_close);

        articleTag = v.findViewById(R.id.articleTags);
        saveArticleButton = v.findViewById(R.id.saveArticle);

        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        profileImage=v.findViewById(R.id.article_profile);
        articleUserName=v.findViewById(R.id.article_userName);


        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);


        if(userClass.getFirst_name().equals("null"))
        {
            articleUserName.setText(userClass.getUser_name());
        }

        else {
            articleUserName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());
        }




        cropFinalImage = v.findViewById(R.id.cropFinalImage);
//        cropFinalImage.setVisibility(View.GONE);


        articleSelectBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        CAMERA))
                {

                    Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                           CAMERA}, RequestPermissionCode);

                }
                selectImage();

            }


        });
        selectBanerImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                       CAMERA))
                {

                    Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            CAMERA}, RequestPermissionCode);

                }
                selectImage();

            }

        });


        saveArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the data and send it to server

                Log.v("dataTosend",editor.getContentAsHTML());

                cropFinalImage.invalidate();
                BitmapDrawable drawable = (BitmapDrawable)cropFinalImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String image=getStringImage(bitmap);
                Log.v("cropedImage",image);
                SendArticleDataToServer(image);
                // Toast.makeText(getApplicationContext(),editor.getContentAsHTML(),Toast.LENGTH_LONG).show();
//                if (!TextUtils.isEmpty(articleTitle.getText()) && !TextUtils.isEmpty(articleShortDescription.getText()) &&
//                        !TextUtils.isEmpty(articleTag.getText())) {
//
//                }
            }
        });


        editor = (Editor) v.findViewById(R.id.editor);
        editor.setFocusable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editor.setFocusable(View.NOT_FOCUSABLE);
        }

        v.findViewById(R.id.article_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        v.findViewById(R.id.article_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });

        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);
        //editor.StartEditor();
        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                //Toast.makeText(ArticleActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(getActivity(), uuid, Toast.LENGTH_LONG).show();
                uploadEditorImageToServer(uuid);

                // editor.onImageUploadFailed(uuid);
            }
        });
        editor.render();

    }

    public void uploadEditorImageToServer(final String uuid)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/upload-medium-image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Imgurl=jsonObject.getString("asset_image");
                            editor.onImageUploadComplete(Imgurl, uuid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.v("Reponse", response);
                        Toast.makeText(getActivity().getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();



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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image","data:image/jpeg;base64,"+imageString);
                return params;
            }
        };

        requestQueue.add(myReq);


    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }



    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 5);
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 4) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    cropFinalImage.setImageBitmap(bitmap);
                    cropFinalImage.setVisibility(View.VISIBLE);



                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    articleSelectBannerImage.setText(String.valueOf(System.currentTimeMillis()) + ".jpg");


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

            } else if (requestCode == 5) {



                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                String[] fileName = picturePath.split("/");
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", fileName[fileName.length-1]+"");

                cropFinalImage.setImageBitmap(thumbnail);
                cropFinalImage.setVisibility(View.VISIBLE);
                articleSelectBannerImage.setText(fileName[fileName.length-1]+"");

            }

        }


        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK&& data != null && data.getData() != null) {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap,
                        (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);
                imageString=getStringImage(bitmap);
                editor.insertImage(bitmapResized);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
        else if(requestCode== editor.MAP_MARKER_REQUEST){
            editor.insertMap(data.getStringExtra("cords"));
        }

        else if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();

        }
        else if (requestCode == 2) {
            if (data != null) {
                uri = data.getData();
                ImageCropFunction();

            }
        }
        else if (requestCode == 1) {
            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                cropedImage = getStringImage(bitmap);
                banner_attachment_image.setImageBitmap(bitmap);

            }
        }
    }

//    public void HandleCrop(int code,Intent result)
//        {
//            if(code==RESULT_OK)
//            {
//                cropFinalImage.setImageURI(Crop.getOutput(result));
//                cropFinalImage.setVisibility(View.VISIBLE);
//
//            }
//            else if(code==Crop.RESULT_ERROR)
//            {
//                Toast.makeText(this,"Error here",Toast.LENGTH_LONG).show();
//            }
//
//        }
//    public void picImage()
//    {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMAGE);
//    }


    public void SendArticleDataToServer(final String image)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/article-parse-post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Reponse", response);
                        Toast.makeText(getActivity().getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                        editor.clearAllContents();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            editor.setFocusable(View.NOT_FOCUSABLE);
//                        }
                        articleTag.setText("");
                        articleTitle.setText("");
                        articleShortDescription.setText("");
                        cropFinalImage.setVisibility(View.GONE);

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_type","article");
                params.put("title",articleTitle.getText().toString());
                params.put("tags",articleTag.getText().toString());
                params.put("description_short",articleShortDescription.getText().toString());
                params.put("banner_image","data:image/jpeg;base64,"+image);
                params.put("description",finalHtml);
                return params;
            }
        };

        requestQueue.add(myReq);
    }


    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 900);
            CropIntent.putExtra("outputY", 950);
            CropIntent.putExtra("aspectX", 0);
            CropIntent.putExtra("aspectY", 0);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {



        if(requestCode== RequestPermissionCode)

        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(),"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

            }
            else {

                Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

            }
        }

        else if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContinueAfterPermission();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showPopup(){
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.post_video_link_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.post_video_link);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String videoLink = userInput.getText().toString();
                                showVideo(videoLink);
                                //result.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        // AlertDialog alertDialog = alertDialogBuilder.create();


    }
    private void showVideo(String videoLink)
    {
        Toast.makeText(getActivity(),videoLink,Toast.LENGTH_LONG).show();
        final WebView myWebView = (WebView) v.findViewById(R.id.articleVideoView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());

        //String src ="src="+'"'+videoLink+'"';
        String[] stringId=videoLink.split("/");

        String id=stringId[stringId.length-1];
        Log.v("id",id);
        String src="src="+'"'+"https://www.youtube.com/embed/"+id+'"';



        html="<iframe width=\"100%\" height=\"300\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
        //String html1 = "<iframe width=\"100%\" height=\"600\" src=\"www.youtube.com/embed/cffcUX_aHe0\" frameborder=\"0\" allowfullscreen=\"\"></iframe>";

        myWebView.loadDataWithBaseURL("https://www.youtube.com/embed/"+id+'"', html, "text/html","UTF-8",null);
        //myWebView.loadUrl(videoLink);

        finalHtml="   <html>\n" +
                "  <head>\n" +
                "    <title>Combined</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"page1\">\n" +
                editor.getContentAsHTML() +
                "    </div>\n" +
                "    <div id=\"page2\">\n" +
                html +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        videoFrameLayout.setVisibility(View.VISIBLE);
//
        video_post_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFrameLayout.setVisibility(View.GONE);
            }
        });
    }

}
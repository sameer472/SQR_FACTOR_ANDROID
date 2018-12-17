package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.Pojo.ArticleEditClass;
import com.hackerkernel.user.sqrfactor.Pojo.SecondPageDesignData;
import com.hackerkernel.user.sqrfactor.Pojo.TokenClass;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesignActivity extends ToolbarActivity implements PlaceSelectionListener{

    private Uri uri;
    private int designId;
    Toolbar toolbar;
    private ImageView profileImage;
    private TextView profileName;
    private Editor editor;
    private ArticleEditClass designEditClass;
    private PlaceAutocompleteFragment autocompleteFragment;
    private SecondPageDesignData secondPageDesignData;
    private String imageString;
    private String slug;
    private String userLocation;
    private boolean isEdit=false;
    private int PLACE_PICKER_REQUEST = 1;
    private  String finalHtml,html;
    private FrameLayout videoFrameLayout;
    private Button nextButton,video_post_close;
    private ImageButton design_insert_video,design_insert_image,design_insert_link;
    private EditText designTitle,designShortDescription,designLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


        designTitle = findViewById(R.id.designTitle);
        designTitle.setFocusable(true);
        designShortDescription = findViewById(R.id.designShortDescription);
        designLocation = findViewById(R.id.design_location);
        design_insert_video=(ImageButton)findViewById(R.id.design_insert_video);
        design_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });

        videoFrameLayout=(FrameLayout)findViewById(R.id.videoFrameLayoutDesign);
        video_post_close=(Button)findViewById(R.id.video_post_design_close);
        nextButton = findViewById(R.id.next_design);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Enter your Location");
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);
        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setVisibility(View.GONE);

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent1=getIntent();
        if(intent1!=null && intent1.hasExtra("Post_Slug_ID"))
        {
//            Toast.makeText(this,intent1.getStringExtra("Post_Slug_ID"),Toast.LENGTH_LONG).show();
            isEdit=true;
            slug=intent1.getStringExtra("Post_Slug_ID");
            designId=intent1.getIntExtra("Post_ID",0);

            FetchDataFromServerAndBindToViews(intent1.getStringExtra("Post_Slug_ID"));
        }

        profileImage = findViewById(R.id.design_profile);
        Glide.with(this).load(UtilsClass.getParsedImageUrl(userClass.getProfile()))
                .into(profileImage);

        profileName = findViewById(R.id.design_profileName);
        profileName.setText(UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name()));



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Design",editor.getContentAsHTML());

                if(isEdit)
                {
                    Intent intent=new Intent(getApplicationContext(),Design2Activity.class);
                    intent.putExtra("ArticleEditClass",designEditClass);
                    intent.putExtra("SecondPageDesignData", secondPageDesignData);
                    //Intent intent=new Intent(getApplicationContext(),Design2Activity.class);
                    intent.putExtra("Title",designTitle.getText().toString());
                    intent.putExtra("slug",slug);
                    intent.putExtra("designId",designId);

                    if(userLocation!=null)
                    {
                        intent.putExtra("Location",userLocation);
                    }
                    else {
                        intent.putExtra("Location",secondPageDesignData.getLocation());
                    }
                    intent.putExtra("ShortDescription",designShortDescription.getText().toString());
                    if(finalHtml!=null)
                        intent.putExtra("Description",finalHtml);
                    else
                        intent.putExtra("Description",editor.getContentAsHTML());

                    startActivity(intent);

                }
                else if (!TextUtils.isEmpty(designTitle.getText()) && !TextUtils.isEmpty(designShortDescription.getText())) {
                    SendDesignDataToServer();
                }
                else {
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
            }
        });


        editor = (Editor) findViewById(R.id.design_editor);
        findViewById(R.id.design_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.design_insert_link).setOnClickListener(new View.OnClickListener() {
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
//                Toast.makeText(DesignActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

                uploadEditorImageToServer(uuid);

            }
        });
        editor.render();
    }

    public void uploadEditorImageToServer(final String uuid) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"upload-medium-image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Imgurl = jsonObject.getString("asset_image");
                            Log.v("imageUrl",Imgurl);
//                            Toast.makeText(getApplicationContext(),Imgurl+"",Toast.LENGTH_LONG).show();
                            editor.onImageUploadComplete(Imgurl, uuid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.v("Reponse", response);
//                        Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();


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
                params.put("image", "data:image/jpeg;base64," + imageString);
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
    public void showPopup()
    {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.post_video_link_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

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
    }

    private void showVideo(String videoLink)
    {


        final WebView myWebView = (WebView) findViewById(R.id.design_VideoView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient(){});
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);

        String[] stringId=videoLink.split("/");

        String id=stringId[stringId.length-1];
        Log.v("id",id);
        String src="src="+'"'+"https://www.youtube.com/embed/"+id+'"';



        html="<iframe width=\"100%\" height=\"250\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
       // String html1="<iframe width=\"100%\" height=\"200\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
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
        video_post_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myWebView.setVisibility(View.GONE);
                videoFrameLayout.setVisibility(View.GONE);
            }
        });


    }


    public void SendDesignDataToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"design-parse",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("Reponse", response);
                        // Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),Design2Activity.class);
                        intent.putExtra("Title",designTitle.getText().toString());
                        intent.putExtra("Location",userLocation);
                        intent.putExtra("ShortDescription",designShortDescription.getText().toString());
                        if(finalHtml!=null)
                            intent.putExtra("Description",finalHtml);
                        else
                            intent.putExtra("Description",editor.getContentAsHTML());
                        startActivity(intent);

                        editor.clearAllContents();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            editor.setFocusable(View.NOT_FOCUSABLE);
                        }
                        designTitle.setText("");
//                        designLocation.setText("");
                        designShortDescription.setText("");


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
                params.put("title",designTitle.getText().toString());
                params.put("formatted_address",userLocation);
                params.put("description_short", designShortDescription.getText().toString());
                params.put("description", editor.getContentAsHTML());
                params.put("post_type","design");
                return params;
            }
        };

        requestQueue.add(myReq);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK&& data != null && data.getData() != null) {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageString=getStringImage(bitmap);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap,
                        (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void FetchDataFromServerAndBindToViews(String post_slug_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.GET, UtilsClass.baseurl+"post/design/edit/"+post_slug_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("designPostEdit");
                            JSONObject jsonObjectSecondPageData = jsonObject.getJSONObject("secondpage_data");
                            designEditClass = new ArticleEditClass(jsonObjectFullPost);
                            secondPageDesignData = new SecondPageDesignData(jsonObjectSecondPageData);

                            designTitle.setText(designEditClass.getTitle());
                            designShortDescription.setText(designEditClass.getShort_description());
                            //designLocation.setText(secondPageDesignData.getLocation());
                            autocompleteFragment.setText(secondPageDesignData.getLocation());
                            setContentToView(designEditClass.getDescription());




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

    public void setContentToView(String content){
        List<String> p = new ArrayList<>();
        List<String> src = new ArrayList<>();
        Document doc = Jsoup.parse(content);

        Elements elements = doc.getAllElements();

        for(Element element :elements){
            Tag tag = element.tag();

            if(tag.getName().equalsIgnoreCase("a")){
                String name  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des1",name);
                if(name.contains("span")||name.contains("<i>")||name.contains("<b>"))
                {
                    continue;
                }
                else {
                    editor.getInputExtensions().insertEditText(0,"",name);
                }

            }

            else if(tag.getName().equalsIgnoreCase("b")){
                String title  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des2",title);
                if(title.contains("href")||title.equals("<br>"))
                {
                    continue;
                }

                else {
                    editor.getInputExtensions().insertEditText(1,"",title);
                    continue;
                }

            }

            else if(tag.getName().equalsIgnoreCase("p")){

                element.select("img").remove();
                String body= element.html();

                String[] parsedBody=body.split("\\.");
                StringBuilder builder = new StringBuilder();
                for(String s : parsedBody) {
                    Log.v("des3",s);
                    if(s.contains("<span")||s.contains("</span>")||s.contains("<br>"))
                    {
                        continue;
                    }
                    else
                        builder.append(s+".");
                }
                String str = builder.toString();
                if(body.contains("href")||body.equals("<br>")||body.contains("<b>"))
                {
                    continue;
                }
                else {
                    editor.getInputExtensions().insertEditText(0,"",str);
                    continue;
                }


            }
            else if (tag.getName().equalsIgnoreCase("img")){
                String url  = element.select("img").attr("src");
                Log.v("des4",url);

                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                        (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);
                                imageString=getStringImage(resource);
                                editor.insertImage(bitmapResized);
                            }
                        });
                continue;
            }
            else if (tag.getName().equalsIgnoreCase("iframe")){
                String url  = element.select("iframe").attr("src");
                Log.v("des5",url);
                final WebView myWebView = (WebView) findViewById(R.id.articleVideoView);
                myWebView.setWebViewClient(new WebViewClient());
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.setWebChromeClient(new WebChromeClient());
                String[] stringId=url.split("/");
                String id=stringId[stringId.length-1];
                String src1="src="+'"'+"https://www.youtube.com/embed/"+id+'"';
                String html="<iframe width=\"100%\" height=\"400\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";

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

                myWebView.loadDataWithBaseURL(url,html, "text/html", "UTF-8", "");
                videoFrameLayout.setVisibility(View.VISIBLE);
//
                video_post_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoFrameLayout.setVisibility(View.GONE);
                    }
                });
                continue;
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        userLocation=place.getAddress().toString();
        autocompleteFragment.setText(userLocation);
    }

    @Override
    public void onError(Status status) {

    }
}
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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class DesignFragment extends Fragment {
    private Uri uri;
    Toolbar toolbar;
    private ImageView profileImage;
    private TextView profileName;
    private Editor editor;
    private String imageString;
    private int PLACE_PICKER_REQUEST = 1;
    private  String finalHtml,html;
    private FrameLayout videoFrameLayout;
    private Button nextButton,video_post_close;
    private ImageButton design_insert_video,design_insert_image,design_insert_link;
    private EditText designTitle,designShortDescription,designLocation;

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v =  inflater.inflate(R.layout.fragment_design, container, false);
        designTitle = v.findViewById(R.id.designTitle);
        designTitle.setFocusable(true);
        designShortDescription = v.findViewById(R.id.designShortDescription);
        designLocation = v.findViewById(R.id.design_location);
        design_insert_video=(ImageButton)v.findViewById(R.id.design_insert_video);
        design_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });

        videoFrameLayout=(FrameLayout)v.findViewById(R.id.videoFrameLayoutDesign);
        video_post_close=(Button)v.findViewById(R.id.video_post_design_close);
        nextButton = v.findViewById(R.id.next_design);

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);

        profileImage = v.findViewById(R.id.design_profile);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);
        profileName = v.findViewById(R.id.design_profileName);
        if(userClass.getFirst_name().equals("null"))
        {
            profileName.setText(userClass.getUser_name());
        }

        else {
            profileName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());
        }

        designLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Design",editor.getContentAsHTML());

                if (!TextUtils.isEmpty(designTitle.getText()) && !TextUtils.isEmpty(designShortDescription.getText()) &&
                        !TextUtils.isEmpty(designLocation.getText())) {
                    SendDesignDataToServer();
                }
                else {
                    Toast.makeText(getContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
            }
        });

        editor = (Editor) v.findViewById(R.id.design_editor);
        v.findViewById(R.id.design_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        v.findViewById(R.id.design_insert_link).setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(getContext(), uuid, Toast.LENGTH_LONG).show();
                uploadEditorImageToServer(uuid);
                //editor.onImageUploadComplete("https://archsqr.in/img/medium/"+String.valueOf(System.currentTimeMillis()) + ".jpg", uuid);
                // uploadEditorImageToServer();
                // editor.onImageUploadFailed(uuid);
            }
        });
        editor.render();
        return  v;
    }
    public void uploadEditorImageToServer(final String uuid) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/upload-medium-image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Imgurl = jsonObject.getString("asset_image");
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
    }

    private void showVideo(String videoLink)
    {

        Toast.makeText(getActivity(),videoLink,Toast.LENGTH_LONG).show();
        final WebView myWebView = (WebView) v.findViewById(R.id.design_VideoView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());

        //String src ="src="+'"'+videoLink+'"';
        String[] stringId=videoLink.split("/");

        String id=stringId[stringId.length-1];
        Log.v("id",id);
        String src="src="+'"'+"https://www.youtube.com/embed/"+id+'"';



        html="<iframe width=\"100%\" height=\"400\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
        String html1="<iframe width=\"100%\" height=\"200\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
        //String html1 = "<iframe width=\"100%\" height=\"600\" src=\"www.youtube.com/embed/cffcUX_aHe0\" frameborder=\"0\" allowfullscreen=\"\"></iframe>";

        myWebView.loadDataWithBaseURL("https://www.youtube.com/embed/"+id+'"', html1, "text/html","UTF-8",null);
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
        myWebView.setVisibility(View.VISIBLE);
//
        video_post_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.setVisibility(View.GONE);
                videoFrameLayout.setVisibility(View.GONE);
            }
        });


    }


    public void SendDesignDataToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/design-parse",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("Reponse", response);
                        // Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getContext().getApplicationContext(),Design2Activity.class);
                        intent.putExtra("Title",designTitle.getText().toString());
                        intent.putExtra("Location",designLocation.getText().toString());
                        intent.putExtra("ShortDescription",designShortDescription.getText().toString());
                        intent.putExtra("Description",finalHtml);
                        startActivity(intent);

                        editor.clearAllContents();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            editor.setFocusable(View.NOT_FOCUSABLE);
                        }
                        designTitle.setText("");
                        designLocation.setText("");
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
                params.put("formatted_address",designLocation.getText().toString());
                params.put("description_short", designShortDescription.getText().toString());
                params.put("description", editor.getContentAsHTML());
                params.put("post_type","design");
                return params;
            }
        };

        requestQueue.add(myReq);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK&& data != null && data.getData() != null) {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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


}

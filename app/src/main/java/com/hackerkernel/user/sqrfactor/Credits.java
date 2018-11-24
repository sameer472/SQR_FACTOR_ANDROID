package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Credits extends AppCompatActivity {

    Toolbar toolbar;
    ImageView menu;
    String CurrentUrl=UtilsClass.baseurl+"profile/detail/sqrfactor/credits";
    String previousPageUrl=null;
    String nextPageUrl=null;
    CreditsAdapter creditsAdapter;
    private Button nextPage,prevPage;
    private RecyclerView recyclerView;
    private TextView profileName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    TextView bluePrint,portfolio,followers,following;
    private Button editProfile;
    ImageView userProfile,profileImage;
    private TextView Credits_shortBio,Credits_address;
    private boolean isScrolling=false;
    int currentItems, totalItems, scrolledItems;
    ArrayList<CreditsClass> creditsClassArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CreditsFragment()).commit();

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        profileName = (TextView) findViewById(R.id.profile_profile_name);
        profileImage = (ImageView) findViewById(R.id.profile_profile_image);
        followCnt = (TextView) findViewById(R.id.profile_followerscnt);
        followingCnt = (TextView) findViewById(R.id.profile_followingcnt);
        portfolioCnt = (TextView) findViewById(R.id.profile_portfoliocnt);
        bluePrintCnt = (TextView) findViewById(R.id.profile_blueprintcnt);


        followCnt.setText(userClass.getFollowerCount());
        followingCnt.setText(userClass.getFollowingCount());
        portfolioCnt.setText(userClass.getPortfolioCount());
        bluePrintCnt.setText(userClass.getBlueprintCount());

        Credits_address=findViewById(R.id.Credits_address);

        Credits_shortBio=findViewById(R.id.Credits_shortBio);

        profileName.setText(UtilsClass.getName(userClass.getFirst_name(),userClass.getLast_name(),userClass.getName(),userClass.getUser_name()));

        Glide.with(this).load(UtilsClass.getParsedImageUrl(userClass.getProfile()))
                .into(profileImage);


        if(userClass.getUser_address()!=null && !userClass.getUser_address().equals("null"))
            Credits_address.setText(userClass.getUser_address());

        if(userClass.getShort_bio()!=null && !userClass.getShort_bio().equals("null"))
            Credits_shortBio.setText(userClass.getShort_bio());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_credit);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(Credits.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        nextPage=(Button)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageRefersh(nextPageUrl);
                prevPage.setVisibility(View.VISIBLE);
            }
        });
        prevPage=(Button)findViewById(R.id.prevPage);
        prevPage.setVisibility(View.GONE);
        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageRefersh(previousPageUrl);
//                Toast.makeText(getApplicationContext(),previousPageUrl+" ",Toast.LENGTH_LONG).show();
                prevPage.setVisibility(View.GONE);
            }
        });
        //adding dummy data for testing
        creditsAdapter = new CreditsAdapter(this, creditsClassArrayList);
        recyclerView.setAdapter(creditsAdapter);


        toolbar = (Toolbar) findViewById(R.id.credits_toolbar);
        toolbar.setTitle("Credits");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        PageRefersh(CurrentUrl);

        menu = (ImageView) findViewById(R.id.profile_about_morebtn);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenu().add(1,1,0,"About "+userClass.getName());
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case 1:
                                Intent i = new Intent(getApplicationContext(), About.class);
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        editProfile =(Button)findViewById(R.id.profile_editprofile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });

        bluePrint = (TextView)findViewById(R.id.profile_blueprintClick);
        portfolio = (TextView)findViewById(R.id.profile_portfolioClick);
        followers = (TextView)findViewById(R.id.profile_followersClick);
        following = (TextView)findViewById(R.id.profile_followingClick);

        bluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits.this, PortfolioActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits.this, FollowersActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits.this, FollowingActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                currentItems = layoutManager.getChildCount();
//                totalItems = layoutManager.getItemCount();
//                scrolledItems = layoutManager.findFirstVisibleItemPosition();
//                if (isScrolling && (currentItems + scrolledItems == totalItems)) {
//                    isScrolling = false;
//                    fetchData();
//                }
//            }
//        });

    }
    public void PageRefersh( String currentUrl) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, currentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        creditsClassArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject post = jsonObject.getJSONObject("posts");
                            nextPageUrl=post.getString("next_page_url");
                            previousPageUrl=post.getString("prev_page_url");
                            JSONArray jsonArrayData = post.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                CreditsClass creditsClass = new CreditsClass(jsonArrayData.getJSONObject(i));
                                creditsClassArrayList.add(creditsClass);
                            }
                            creditsAdapter.notifyDataSetChanged();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
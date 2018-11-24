package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    private SharedPreferences sp;
    private SharedPreferences.Editor pref;

    @Override
    protected void onStart() {
        super.onStart();

        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (getIntent().getExtras() != null && getIntent().hasExtra("postSlug")) {
           // startFullPostActivity();
            if(sp.getString("isNotification","notification").equals(getIntent().getStringExtra("postSlug")) && sp.getBoolean("First",false))
            {
              //  Toast.makeText(getApplicationContext(),"activity1",Toast.LENGTH_LONG).show();
                pref=sp.edit();
                pref.putBoolean("First",false);
                pref.commit();
                Intent intent=new Intent(this,HomeScreen.class);
                intent.putExtra("FromNotification","True");
                startActivity(intent);
            }
            else {
               // Toast.makeText(getApplicationContext(),"activity2",Toast.LENGTH_LONG).show();
                pref=sp.edit();
                pref.putBoolean("First",true);
                pref.putString("isNotification",getIntent().getStringExtra("postSlug"));
                pref.commit();
                startFullPostActivity();
            }


        }

        else {

            if (sp.getBoolean("logged", false)) {
               // Toast.makeText(getApplicationContext(),"activity3",Toast.LENGTH_LONG).show();
                goToHomeScreen();

            } else {
               // Toast.makeText(getApplicationContext(),"activity4",Toast.LENGTH_LONG).show();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(i);
                        finish();
                    }
                }, 2000);

            }
        }
    }
    public void goToHomeScreen(){
        Intent i = new Intent(this,HomeScreen.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        }

    private void startFullPostActivity()
    {
        String slug,userName,userProfile;
        int userId;
        for (String key : getIntent().getExtras().keySet()) {
            if(key.equals("postSlug")) {
                slug = getIntent().getStringExtra("postSlug");
                userId=Integer.parseInt(getIntent().getStringExtra("senderID"));
                userName=getIntent().getStringExtra("username");
                userProfile=getIntent().getStringExtra("notificationSenderUrl");
                Log.d("NotificationTag" , key+"____" + slug);

                if(slug.equals(""))
                {
                    Toast.makeText(getApplicationContext(),slug+"1",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, UserProfileActivity.class);
                    intent.putExtra("User_id", userId);
                    intent.putExtra("ProfileUserName", userName);
                    intent.putExtra("ProfileUrl",userProfile);
                    startActivity(intent);

                }
                else if(slug.equals("chat"))
                {
                    Toast.makeText(getApplicationContext(),slug+"2",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, ChatWithAFriendActivity.class);
                    intent.putExtra("FriendId", userId);
                    intent.putExtra("FriendProfileUrl", userProfile);
                    intent.putExtra("FriendName",userName);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),slug+"3",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this,FullPostActivity.class);
                    intent.putExtra("Post_Slug_ID",slug);
                    startActivity(intent);
                }

                break;
                // value will represend your message body... Enjoy It

            }
        }
    }
}

package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class LoginScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //viewPager = (ViewPager)findViewById(R.id.pager);
        //pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(pagerAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().add(R.id.frag, new SignupFragment(), "stuff").commit();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.signup_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.login));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {

                    tab.setIcon(R.drawable.signup_selected);
                    getFragmentManager().beginTransaction().replace(R.id.frag, new SignupFragment(), "stuff").commit();
                }
                if (tab.getPosition() == 1) {

                    tab.setIcon(R.drawable.login_selected);
                    getFragmentManager().beginTransaction().replace(R.id.frag, new LoginFragment(), "stuff1").commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        tab.setIcon(R.drawable.signup);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.login);
                        break;

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
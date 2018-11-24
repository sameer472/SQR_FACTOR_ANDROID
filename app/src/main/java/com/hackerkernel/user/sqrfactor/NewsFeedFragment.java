package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class NewsFeedFragment extends Fragment {


    private EditText writePost;
    private ImageView profileImage;
    Button button1,button2;
    public int flag=0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View)inflater.inflate(R.layout.fragment_news_feed, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);


        button1 = view.findViewById(R.id.newsFeedbtn);
        button2 = view.findViewById(R.id.whatsRedbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();
            }


        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RedActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
        return view;
    }


}

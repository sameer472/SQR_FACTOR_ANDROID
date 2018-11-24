package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    TextView text1,text2,text3,text4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_settings, container, false);

        text1 = (TextView)view.findViewById(R.id.basic_details);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),BasicDetails.class);
                startActivity(i);
            }
        });
        text2 = (TextView)view.findViewById(R.id.edu_details);
        text2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getContext(),EducationDetailsActivity.class);
                startActivity(j);
            }
        });
        text3 = (TextView)view.findViewById(R.id.prof_details);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getContext(),ProfessionalActivity.class);
                startActivity(k);
            }
        });
        text4 = (TextView)view.findViewById(R.id.other_details);
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(getContext(),OtherDetailsActivity.class);
                startActivity(l);
            }
        });

        return view;
    }

}

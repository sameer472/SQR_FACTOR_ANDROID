package com.hackerkernel.user.sqrfactor.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hackerkernel.user.sqrfactor.Activities.CompetitionsActivity;
import com.hackerkernel.user.sqrfactor.Activities.EventsActivity;
import com.hackerkernel.user.sqrfactor.Activities.jobs.JobActivity;
import com.hackerkernel.user.sqrfactor.R;

public class ModalSheet extends BottomSheetDialogFragment {

    LinearLayout ll1, ll3, ll4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_modal_sheet, container, false);

        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll3 = (LinearLayout)view.findViewById(R.id.ll3);
        ll4 = (LinearLayout)view.findViewById(R.id.ll4);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CompetitionsActivity.class);
                startActivity(i);
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), JobActivity.class);
                startActivity(i);
            }
        });

        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EventsActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}

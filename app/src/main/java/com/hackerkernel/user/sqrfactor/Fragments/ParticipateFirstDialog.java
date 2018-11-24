package com.hackerkernel.user.sqrfactor.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hackerkernel.user.sqrfactor.Activities.ParticipateActivity;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.R;

public class ParticipateFirstDialog extends DialogFragment {
    private static final String TAG = "ParticipateFirstDialog";

    Button mGoButton;
    Button mCancelButton;
    private String mCompetitionId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.participate_first_dialog, null);

        builder.setView(view);

        mGoButton = view.findViewById(R.id.go_btn);
        mCancelButton = view.findViewById(R.id.cancel_btn);

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: competition id = " + mCompetitionId);

                Intent i = new Intent(getActivity(), ParticipateActivity.class);
                i.putExtra(BundleConstants.COMPETITION_ID, mCompetitionId);
                startActivity(i);

                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompetitionId = getArguments().getString(BundleConstants.COMPETITION_ID);

    }
}

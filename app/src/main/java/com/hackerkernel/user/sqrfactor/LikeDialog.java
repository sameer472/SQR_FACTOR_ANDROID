package com.hackerkernel.user.sqrfactor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class LikeDialog extends DialogFragment {

    ImageButton cross;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.like_dialog, null);

        cross = (ImageButton)view.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        recycler = (RecyclerView)view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.addItemDecoration(decoration);

        adapter = new LikeAdapter();
        recycler.setAdapter(adapter);

        builder.setView(view);

        return builder.create();

    }
}

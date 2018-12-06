package com.hackerkernel.user.sqrfactor.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Pojo.AttachmentClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;

import java.util.List;

public class AttachmentsAdapter extends RecyclerView.Adapter<AttachmentsAdapter.MyViewHolder> {
    private static final String TAG = "AttachmentsAdapter";
    private Context mContext;
    private List<AttachmentClass> mAttachments;

    private RequestQueue mRequestQueue;

    private MySharedPreferences mSp;
    AlertDialog dialog;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, download_link;
        ImageView imageIV;

        MyViewHolder(View view) {
            super(view);
            nameTV = view.findViewById(R.id.attachment_url);
            download_link = view.findViewById(R.id.download_link);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            AttachmentClass attachment = mAttachments.get(pos);
        }
    }



    public AttachmentsAdapter(Context context, List<AttachmentClass> attachments) {
        this.mContext = context;
        this.mAttachments = attachments;

        mSp = MySharedPreferences.getInstance(mContext);

    }

    @Override
    public AttachmentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_item, parent, false);

        return new AttachmentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AttachmentClass attachment = mAttachments.get(position);

        if(position==0)
        {
            holder.nameTV.setText("Download Site");
        }

        else {
            holder.nameTV.setText("Download Brief");
        }

        holder.nameTV.setTextColor(Color.RED);
        holder.download_link.setText(attachment.getAttachmentUrl());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ServerConstants.IMAGE_BASE_URL + holder.download_link.getText().toString()));
                Intent chooser = Intent.createChooser(intent, "Select Browser");
                mContext.startActivity(chooser);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }
}
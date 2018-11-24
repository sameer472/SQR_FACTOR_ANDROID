package com.hackerkernel.user.sqrfactor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Pojo.PartnerClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PartnersEditAdapter extends RecyclerView.Adapter<PartnersEditAdapter.MyViewHolder> {
    private static final String TAG = "PartnersEditAdapter";
    private Context mContext;
    private List<PartnerClass> mPartners;

    private RequestQueue mRequestQueue;

    private MySharedPreferences mSp;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV;
        ImageView imageIV;

        MyViewHolder(View view) {
            super(view);
            nameTV = view.findViewById(R.id.partner_name);
            imageIV = view.findViewById(R.id.partner_image);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            PartnerClass jury = mPartners.get(pos);

        }
    }


    public PartnersEditAdapter(Context context, List<PartnerClass> partners) {
        this.mContext = context;
        this.mPartners = partners;

        mSp = MySharedPreferences.getInstance(mContext);

    }

    @Override
    public PartnersEditAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_edit_item, parent, false);

        return new PartnersEditAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PartnerClass partner = mPartners.get(position);

        holder.nameTV.setText(partner.getName());
        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + partner.getImageUrl()).into(holder.imageIV);
    }

    @Override
    public int getItemCount() {
        return mPartners.size();
    }

}
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
import com.hackerkernel.user.sqrfactor.Pojo.JuryClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JuryAdapter extends RecyclerView.Adapter<JuryAdapter.MyViewHolder> {
    private static final String TAG = "JuryAdapter";
    private Context mContext;
    private List<JuryClass> mJuryList;

    private RequestQueue mRequestQueue;

    private MySharedPreferences mSp;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV;
        ImageView imageIV;

        MyViewHolder(View view) {
            super(view);
            nameTV = view.findViewById(R.id.jury_name);
            imageIV = view.findViewById(R.id.jury_image);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            JuryClass jury = mJuryList.get(pos);

        }
    }

    public JuryAdapter(Context context, List<JuryClass> juryList) {
        this.mContext = context;
        this.mJuryList = juryList;

        mSp = MySharedPreferences.getInstance(mContext);

    }

    @Override
    public JuryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jury_item, parent, false);

        return new JuryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        JuryClass jury = mJuryList.get(position);

        holder.nameTV.setText(jury.getFullName());
        Picasso.get().load(jury.getImageUrl()).into(holder.imageIV);
    }

    @Override
    public int getItemCount() {
        return mJuryList.size();
    }

}
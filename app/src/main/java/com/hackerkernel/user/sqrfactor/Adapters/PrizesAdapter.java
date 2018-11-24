package com.hackerkernel.user.sqrfactor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hackerkernel.user.sqrfactor.Pojo.PrizeClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;

import java.util.List;

public class PrizesAdapter extends RecyclerView.Adapter<PrizesAdapter.MyViewHolder> {
    private static final String TAG = "PrizesAdapter";
    private Context mContext;
    private List<PrizeClass> mPrizes;

    private RequestQueue mRequestQueue;

    private MySharedPreferences mSp;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTV;
        TextView descriptionTV;

        MyViewHolder(View view) {
            super(view);
            titleTV = view.findViewById(R.id.title);
            descriptionTV = view.findViewById(R.id.description);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            PrizeClass prize = mPrizes.get(pos);

        }
    }

    public PrizesAdapter(Context context, List<PrizeClass> prizes) {
        this.mContext = context;
        this.mPrizes = prizes;

        mSp = MySharedPreferences.getInstance(mContext);

    }

    @Override
    public PrizesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prize_item, parent, false);

        return new PrizesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PrizeClass prize = mPrizes.get(position);

        holder.titleTV.setText(prize.getType() + "\nAmount - " + prize.getAmount());
        holder.descriptionTV.setText(prize.getExtra());
    }

    @Override
    public int getItemCount() {
        return mPrizes.size();
    }

}
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
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    List<UserClass> jobBeanList;
    UserClass jobBean;
    private MySharedPreferences mSp;
    int iposition;
    private RequestQueue mRequestQueue;
    String status;


    public UserAdapter(List<UserClass> jobBeanList, Context context) {
        super();
        mSp = MySharedPreferences.getInstance(context);
        mRequestQueue = Volley.newRequestQueue(context);
        this.jobBeanList = jobBeanList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        jobBean = jobBeanList.get(position);
        Log.d("onBindViewHolder: ", ServerConstants.IMAGE_BASE_URL + jobBean.getProfilePicPath());
        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + jobBean.getProfilePicPath()).into(holder.userImage);
        holder.username.setText(jobBean.getName());


    }


    @Override
    public int getItemCount() {
        return jobBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);

        }
    }

}

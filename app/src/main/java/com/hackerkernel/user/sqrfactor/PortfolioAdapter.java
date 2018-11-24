package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.MyViewHolder> {
    private ArrayList<PortfolioClass> portfolioClassArrayList;
    private Context context;

    public PortfolioAdapter(ArrayList<PortfolioClass> portfolioClassArrayList, Context context) {
        this.portfolioClassArrayList = portfolioClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.portfolio_adapter_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final PortfolioClass portfolioClass=portfolioClassArrayList.get(position);
        holder.articleName.setText(portfolioClass.getTitle());
        holder.shortDescrip.setText(portfolioClass.getShort_description());

        Glide.with(context).load(UtilsClass.baseurl1+portfolioClass.getArticleImageUrl())
                .into(holder.articleImage);

        holder.articleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",portfolioClass.getSlug());
                context.startActivity(intent);
            }
        });

        holder.likes.setText(portfolioClass.getLikeCount());
        holder.comment.setText(portfolioClass.getCommentcount());
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.menu_portfolio, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.RemoveFromFeatures:
//                                Intent intent=new Intent(context,FullPostActivity.class);
//                                intent.putExtra("Post_Slug_ID",portfolioClass.getSlug());
//                                context.startActivity(intent);
                                break;
                            case R.id.EditPost:
                                if(portfolioClass.getType().equals("design"))
                                {
                                    Intent intent1=new Intent(context,DesignActivity.class);
                                    intent1.putExtra("Post_Slug_ID",portfolioClass.getSlug());
                                    context.startActivity(intent1);
                                }
                                else if(portfolioClass.getType().equals("article"))
                                {
                                    Intent intent1=new Intent(context,ArticleActivity.class);
                                    intent1.putExtra("Post_Slug_ID",portfolioClass.getSlug());
                                    context.startActivity(intent1);
                                }
                                else if(portfolioClass.getType().equals("status"))
                                {
                                    Intent intent1=new Intent(context,StatusPostActivity.class);
                                    intent1.putExtra("Post_Slug_ID",portfolioClass.getSlug());
                                    context.startActivity(intent1);
                                }
                                break;

                        }
                        return true;
                    }
                });
            }
        });
        //set profile image using glide libaray fromserver
    }

    @Override
    public int getItemCount() {
        return portfolioClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView articleName,shortDescrip,likes,comment;
        ImageView articleImage;
        ImageView menuButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            articleName=(TextView)itemView.findViewById(R.id.articleTitle_portfolio);
            shortDescrip=(TextView)itemView.findViewById(R.id.short_descrip_portfolio);
            likes=(TextView)itemView.findViewById(R.id.like_portfolio);
            comment=(TextView)itemView.findViewById(R.id.Comments_portfolio);
            articleImage=(ImageView)itemView.findViewById(R.id.imageProfile);
            menuButton=(ImageView)itemView.findViewById(R.id.more_portfolio);
        }
    }


    public void DeletePost(final String  user_post_id, final String  id, final String is_shared) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UtilsClass.baseurl+"delete_post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(context, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("users_post_id",user_post_id+"");
                params.put("id",id+"");
                params.put("is_shared",is_shared+"");
//
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
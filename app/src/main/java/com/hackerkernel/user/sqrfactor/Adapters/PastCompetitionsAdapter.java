package com.hackerkernel.user.sqrfactor.Adapters;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Activities.CompetitionDetailActivity;
import com.hackerkernel.user.sqrfactor.Activities.CompetitionEditActivity;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Interfaces.OnLoadMoreListener;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.CompetitionClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.MyMethods;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastCompetitionsAdapter extends RecyclerView.Adapter<PastCompetitionsAdapter.MyViewHolder> {
    private static final String TAG = "CompetitionsAdapter";
    private Context mContext;
    private List<CompetitionClass> mCompetitions;

    // infinite scroll
    private int lastVisibleItem;
    private int totalItemCount;
    public boolean isLoading;
    private static final int VISIBLE_THRESHOLD = 5;
    private OnLoadMoreListener onLoadMoreListener;

    private ProgressDialog mPd;
    private RequestQueue mRequestQueue;
    private MySharedPreferences mSp;

    //infinite scroll
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView competitionNameTV;
        TextView startTimeAgoTV;
        TextView lastSubmissionDateTV;
        TextView lastRegistrationDateTV;
        TextView prizeTV;
        TextView competitionTypeTV;
        ImageView competitionImageView;
        Button participateButton;
        ImageView compMenu;
        android.support.design.widget.FloatingActionButton shareFab;

        MyViewHolder(View view) {
            super(view);
            int pos = getAdapterPosition();

            competitionNameTV = view.findViewById(R.id.past_comp_name);
            lastSubmissionDateTV = view.findViewById(R.id.past_comp_last_submission_date);
            lastRegistrationDateTV = view.findViewById(R.id.past_comp_last_reg_date);
            startTimeAgoTV = view.findViewById(R.id.past_comp_start_time_ago);
            prizeTV = view.findViewById(R.id.past_comp_prize);
            competitionTypeTV = view.findViewById(R.id.past_comp_type);
            competitionImageView = view.findViewById(R.id.past_comp_image);
            compMenu = view.findViewById(R.id.past_comp_menu);
            participateButton = view.findViewById(R.id.past_participate);

            shareFab = view.findViewById(R.id.past_competition_share);


            Log.d(TAG, "MyViewHolder: pos = " + pos);

            participateButton.setOnClickListener(this);
            shareFab.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            Log.d(TAG, "onClick: pos = " + pos);
            CompetitionClass competition = mCompetitions.get(pos);
            String slug = competition.getSlug();

            final String link = ServerConstants.BASE_URL_COMPETITION_LINK + competition.getSlug();
            Log.d(TAG, "onClick: link = " + link);

            switch (view.getId()) {
                case R.id.participate: {
                    Intent i = new Intent(mContext, CompetitionDetailActivity.class);
                    i.putExtra(BundleConstants.SLUG, slug);
                    mContext.startActivity(i);
                    break;
                }

                case R.id.competition_share: {
                    Log.d(TAG, "onClick: share pressed");
                    share(link);
                    break;

                }
                default: {
                    Intent i = new Intent(mContext, CompetitionDetailActivity.class);
                    i.putExtra(BundleConstants.SLUG, slug);
                    mContext.startActivity(i);
                    break;
                }
            }
        }
    }

    private void share(String link) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, link);

        mContext.startActivity(Intent.createChooser(share, "Share"));
    }

    private void shareToSocialMedia(String pack, String link) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain*");
        intent.putExtra(Intent.EXTRA_TEXT, link);

        boolean installed = checkAppInstall(pack);
        if (installed) {
            intent.setPackage(pack);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext,
                    "Install the application first", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    public PastCompetitionsAdapter(Context context, List<CompetitionClass> competitions, RecyclerView recyclerView) {
        this.mContext = context;
        this.mCompetitions = competitions;

        mSp = MySharedPreferences.getInstance(mContext);

        mPd = new ProgressDialog(mContext);
        mPd.setMessage("Loading Please Wait..");
        mPd.setCancelable(false);

        //infinite scroll
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "onScrolled: called outside");
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "onScrolled: total item count = " + totalItemCount);
                Log.d(TAG, "onScrolled: last visible item =" + lastVisibleItem);
                Log.d(TAG, "onScrolled: competitions list size = " + mCompetitions.size());
                Log.d(TAG, "onScrolled: is loading = " + isLoading);

                if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)){
                    if (onLoadMoreListener != null && !mCompetitions.isEmpty()) {
                        Log.d(TAG, "onScrolled: called inside");
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    @Override
    public PastCompetitionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_competition_item, parent, false);

        return new PastCompetitionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CompetitionClass competition = mCompetitions.get(position);

        if (competition != null) {
            holder.competitionNameTV.setText(competition.getCompetitionName());
            try {
                holder.startTimeAgoTV.setText(MyMethods.convertDate(competition.getStartTimeAgo()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.lastSubmissionDateTV.setText("Last Date of Submission: " + competition.getLastSubmissionDate());
            holder.lastRegistrationDateTV.setText("Last Date of Registration: " + competition.getLastRegistrationDate());
            holder.prizeTV.setText("Prize: " + competition.getPrize());
            holder.competitionTypeTV.setText("Type: " + competition.getCompetitionType());
            Log.d(TAG, "onBindViewHolder: image url = " + ServerConstants.BASE_URL_COMPETITION + competition.getImageUrl());

            Picasso.get().load(ServerConstants.IMAGE_BASE_URL + competition.getImageUrl()).into(holder.competitionImageView);

            String compUserId = competition.getUserId();
            Log.d(TAG, "onBindViewHolder: competition user id = " + compUserId);

            String myUserId = mSp.getKey(SPConstants.USER_ID);
            Log.d(TAG, "onBindViewHolder: my user id = " + myUserId);

            if (compUserId.equals(myUserId)) {
                holder.compMenu.setVisibility(View.VISIBLE);

                holder.compMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(view, position);
                    }
                });
            } else {
                holder.compMenu.setVisibility(View.GONE);
            }

        } else {

        }

    }

    @Override
    public int getItemCount() {
        return mCompetitions.size();
    }

    private void showPopupMenu(View view, final int position) {

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.comp_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CompetitionClass competition = mCompetitions.get(position);

                switch (item.getItemId()) {
                    case R.id.comp_edit: {
                        String slug = competition.getSlug();
                        Toast.makeText(mContext, "Edit Pressed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, CompetitionEditActivity.class);
                        i.putExtra(BundleConstants.SLUG, slug);

                        mContext.startActivity(i);

                        return true;
                    }
                    case R.id.comp_delete: {
                        String slug = competition.getSlug();
                        String competitionId = competition.getId();

                        showDeleteDialog(position, competitionId, slug);
                        return true;
                    }

                    default:
                }
                return false;
            }
        });
        popup.show();
    }

    private void showDeleteDialog(final int position, final String competitionId, final String slug) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete this competition?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deletePostCompetitionApi(position, competitionId, slug);
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deletePostCompetitionApi(final int pos, final String competitionId, final String slug) {
        mPd.show();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            mPd.dismiss();
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.DELETE_POST_COMPETITION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.dismiss();

                Log.d(TAG, "onResponse: delete competition response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String message = responseObject.getString("message");
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    mCompetitions.remove(pos);
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPd.dismiss();
                NetworkUtil.handleSimpleVolleyRequestError(error, mContext);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(mContext.getString(R.string.accept), mContext.getString(R.string.application_json));
                headers.put(mContext.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("competition_slug", slug);
                params.put("competition_id", competitionId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public void setLoaded() {
        isLoading = false;
    }

}
package com.hackerkernel.user.sqrfactor.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.hackerkernel.user.sqrfactor.Activities.SubmissionDetailActivity;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.SubmissionClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionsAdapter extends RecyclerView.Adapter<SubmissionsAdapter.MyViewHolder> {
    private static final String TAG = "SubmissionsAdapter";
    private Context mContext;
    private List<SubmissionClass> mSubmissions;
    private MySharedPreferences mSp;
    private ProgressDialog mPd;
    private RequestQueue mRequestQueue;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTV;
        TextView codeTV;
        ImageView coverIV;
        Button likeButton;
        Button commentButton;

        MyViewHolder(View view) {
            super(view);
            titleTV = view.findViewById(R.id.submission_title);
            codeTV = view.findViewById(R.id.submission_code);
            coverIV = view.findViewById(R.id.submission_cover);
            likeButton = view.findViewById(R.id.submission_like);
            commentButton = view.findViewById(R.id.submission_comment);

            likeButton.setOnClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            SubmissionClass submission = mSubmissions.get(pos);
            String submissionId = submission.getId();

            switch (view.getId()) {
                case R.id.submission_like: {
                    submissionLikeApi(submissionId);
                    break;
                }
                default: {
                    String coverUrl = submission.getCoverUrl();
                    String pdfUrl = submission.getPdfUrl();
                    JSONArray commentsArray = submission.getCommentsArray();
                    String commentsArrayString = commentsArray.toString();

                    Log.d(TAG, "onClick: cover url = " + coverUrl);
                    Log.d(TAG, "onClick: pdf url = " + pdfUrl);

                    Intent i = new Intent(mContext, SubmissionDetailActivity.class);
                    i.putExtra(BundleConstants.COVER_IMAGE_PATH, coverUrl);
                    i.putExtra(BundleConstants.PDF_URL, pdfUrl);
                    i.putExtra(BundleConstants.COMMENTS_ARRAY, commentsArrayString);
                    i.putExtra(BundleConstants.SUBMISSION_ID, submissionId);
                    i.putExtra(BundleConstants.SUBMISSION_TITLE, submission.getTitle());

                    mContext.startActivity(i);
                    break;
                }
            }
        }
    }

    private void submissionLikeApi(final String submissionId) {
        mPd.show();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            mPd.dismiss();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.SUBMISSION_LIKE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.dismiss();

                Log.d(TAG, "onResponse: submission like response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject respObject = responseObject.getJSONObject("Response");

                    String message = respObject.getString("message");
//                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


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

                params.put("like_id", submissionId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }


    public SubmissionsAdapter(Context context, List<SubmissionClass> submissions) {
        this.mContext = context;
        this.mSubmissions = submissions;
        mSp = MySharedPreferences.getInstance(mContext);

        mPd = new ProgressDialog(mContext);
        mPd.setMessage("Uploading Please Wait..");
        mPd.setCancelable(false);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.submission_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SubmissionClass submission = mSubmissions.get(position);

        holder.titleTV.setText(submission.getTitle());
        holder.codeTV.setText(submission.getCode());
        String coverUrl = submission.getCoverUrl();

        JSONArray commentsArray = submission.getCommentsArray();
        if (commentsArray.length() > 0) {
            holder.commentButton.setText(commentsArray.length() + " comments");
        }


        if (coverUrl != null && !coverUrl.equals("null")) {
            Picasso.get().load(ServerConstants.IMAGE_BASE_URL + submission.getCoverUrl()).resize(350, 200).placeholder(R.drawable.no_image_placeholder).into(holder.coverIV);
        }


    }

    @Override
    public int getItemCount() {
        return mSubmissions.size();
    }

}
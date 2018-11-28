package com.hackerkernel.user.sqrfactor.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.QuestionCommentClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionCommentsAdapter extends RecyclerView.Adapter<SubmissionCommentsAdapter.MyViewHolder> {
    private static final String TAG = "SubmissionCommentsAdapt";
    private Context mContext;
    private List<QuestionCommentClass> mComments;
    private String mQuestionId;

    private RequestQueue mRequestQueue;
    private ProgressDialog mPd;

    private MySharedPreferences mSp;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView askedByTV;
        TextView descriptionTV;
        ImageView imageIV;
        ImageView menuThreeDotsIV;

        MyViewHolder(View view) {
            super(view);
            askedByTV = view.findViewById(R.id.comment_asked_by);
            descriptionTV = view.findViewById(R.id.comment_description);
            imageIV = view.findViewById(R.id.comment_image);
            menuThreeDotsIV = view.findViewById(R.id.comment_3_dots);

            menuThreeDotsIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    final QuestionCommentClass comment = mComments.get(pos);
                    final String commentId = comment.getId();

                    PopupMenu popup = new PopupMenu(mContext, menuThreeDotsIV);

                    popup.getMenuInflater().inflate(R.menu.comment_popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.comment_edit: {
                                    String commentDesc = comment.getDescription();
                                    showCommentEditDialog(pos,commentId, commentDesc);
                                    break;
                                }

                                case R.id.comment_delete: {
                                    showCommentDeleteDialog(pos, commentId);
                                    break;
                                }

                                default:
                                    return true;
                            }

                            return false;
                        }
                    });

                    popup.show();
                }
            });

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            QuestionCommentClass comment = mComments.get(pos);
        }
    }

    private void showCommentDeleteDialog(final int pos, final String commentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Are you sure you want to delete this comment?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        wallQuestionCommentDeleteApi(pos, commentId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void wallQuestionCommentDeleteApi(final int pos, final String commentId) {
        mPd.show();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            mPd.dismiss();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_COMMENT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.dismiss();

                Log.d(TAG, "onResponse: comment delete response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);

                    String message = responseObject.getString("message");
//                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    mComments.remove(pos);
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

                params.put("comment_id", commentId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    private void showCommentEditDialog(final int position, final String commentId, String prevComment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_comment_dialog,null);

        final EditText commentEditText = view.findViewById(R.id.comment_et);

        commentEditText.setText(prevComment);
        commentEditText.setSelection(commentEditText.getText().length());

        builder.setTitle("Edit your comment").setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = commentEditText.getText().toString();

                        if (comment.length() < 1) {
                            Toast.makeText(mContext, "The comment must not be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wallCommentUpdateApi(position,commentId, comment);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void wallCommentUpdateApi(final int position, final String commentId, final String comment) {
        Log.d(TAG, "wallQuestionAddApi: " + "OK");
        mPd.show();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            mPd.dismiss();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.WALL_QUESTION_COMMENT_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.dismiss();

                Log.d(TAG, "onResponse: comment update response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject respObject = responseObject.getJSONObject("Response");

                    String message = respObject.getString("message");
//                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    QuestionCommentClass commentObj = mComments.get(position);
                    commentObj.setDescription(comment);
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

                params.put("comment_id", commentId);
                params.put("comment_description", comment);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public SubmissionCommentsAdapter(Context context, List<QuestionCommentClass> comments, String questionId) {
        this.mContext = context;
        this.mComments = comments;
        this.mQuestionId = questionId;

        mSp = MySharedPreferences.getInstance(mContext);

        mPd = new ProgressDialog(mContext);
        mPd.setMessage("Loading Please Wait..");
        mPd.setCancelable(false);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_comment_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        QuestionCommentClass comment = mComments.get(position);

        String userId = comment.getUserId();

        holder.askedByTV.setText(comment.getAskedBy());
        holder.descriptionTV.setText(comment.getDescription());
        Log.d(TAG, "onBindViewHolder: image url = " + ServerConstants.IMAGE_BASE_URL + comment.getImageUrl());
        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + comment.getImageUrl()).into(holder.imageIV);

        String currentUserId = mSp.getKey(SPConstants.USER_ID);

        if (userId.equals(currentUserId)) {
            holder.menuThreeDotsIV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

}
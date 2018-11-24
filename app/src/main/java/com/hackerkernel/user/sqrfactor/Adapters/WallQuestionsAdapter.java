package com.hackerkernel.user.sqrfactor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hackerkernel.user.sqrfactor.Activities.QuestionDetailActivity;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Pojo.WallQuestionClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;

import org.json.JSONArray;

import java.util.List;

public class WallQuestionsAdapter extends RecyclerView.Adapter<WallQuestionsAdapter.MyViewHolder> {
    private static final String TAG = "WallQuestionsAdapter";
    private Context mContext;
    private List<WallQuestionClass> mWallQuestions;

    private RequestQueue mRequestQueue;

    private MySharedPreferences mSp;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView announcedByTV;
        TextView subjectTV;

        MyViewHolder(View view) {
            super(view);
            announcedByTV = view.findViewById(R.id.wall_announced_by);
            subjectTV = view.findViewById(R.id.wall_subject);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            WallQuestionClass wallQuestion = mWallQuestions.get(pos);

            JSONArray commentsArray = wallQuestion.getCommentsArray();
            String commentsArrayString = commentsArray.toString();
            Log.d(TAG, "onClick: comments array string = " + commentsArrayString);

            String questionTitle = wallQuestion.getSubject();
            String questionDesc = wallQuestion.getDescription();
            String questionId = wallQuestion.getId();
            String questionUserId = wallQuestion.getUserId();

            Intent i = new Intent(mContext, QuestionDetailActivity.class);
            i.putExtra(BundleConstants.QUESTION_ID, questionId);
            i.putExtra(BundleConstants.QUESTION_USER_ID, questionUserId);
            i.putExtra(BundleConstants.QUESTION_TITLE, questionTitle);
            i.putExtra(BundleConstants.QUESTION_DESCRIPTION, questionDesc);
            i.putExtra(BundleConstants.COMMENTS_ARRAY, commentsArrayString);
            i.putExtra("Asked By",wallQuestion.getAnnouncedBy());


            mContext.startActivity(i);

        }
    }

    public WallQuestionsAdapter(Context context, List<WallQuestionClass> wallQuestions) {
        this.mContext = context;
        this.mWallQuestions = wallQuestions;

        mSp = MySharedPreferences.getInstance(mContext);

    }

    @Override
    public WallQuestionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_item, parent, false);

        return new WallQuestionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        WallQuestionClass wallQuestion = mWallQuestions.get(position);

        holder.announcedByTV.setText("asked by " + wallQuestion.getAnnouncedBy());
        holder.subjectTV.setText(wallQuestion.getSubject());
    }

    @Override
    public int getItemCount() {
        return mWallQuestions.size();
    }

}
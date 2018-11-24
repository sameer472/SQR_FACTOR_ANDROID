package com.hackerkernel.user.sqrfactor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackerkernel.user.sqrfactor.Adapters.SubmissionCommentsAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Pojo.QuestionCommentClass;
import com.hackerkernel.user.sqrfactor.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubmissionDetailActivity extends AppCompatActivity {
    private static final String TAG = "SubmissionDetailActivit";

    private ImageView mSubImageView;
    private WebView mPdfWebView;

    private Toolbar mToolbar;

    private JSONArray mCommentsArray;

    private TextView mCommentsCountTV;

    private List<QuestionCommentClass> mComments;
    private SubmissionCommentsAdapter mCommentsAdapter;

    private RecyclerView mCommentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_detail);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent i = getIntent();

        String coverUrl = i.getStringExtra(BundleConstants.COVER_IMAGE_PATH);
        String pdfUrl = i.getStringExtra(BundleConstants.PDF_URL);
        String submissionId = i.getStringExtra(BundleConstants.SUBMISSION_ID);
        String commentsArrayString = i.getStringExtra(BundleConstants.COMMENTS_ARRAY);
        String submissionTitle = i.getStringExtra(BundleConstants.SUBMISSION_TITLE);

        getSupportActionBar().setTitle(submissionTitle);
        try {
            mCommentsArray = new JSONArray(commentsArrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: cover url = " + coverUrl);
        Log.d(TAG, "onCreate: 'pdf url = " + pdfUrl);
        Log.d(TAG, "onCreate: submission id = " + submissionId);
        Log.d(TAG, "onCreate: comments array = " + commentsArrayString);

        mSubImageView = findViewById(R.id.sub_detail_image);
        mPdfWebView = findViewById(R.id.sub_detail_pdf);
        mCommentsRecyclerView = findViewById(R.id.sub_detail_comments_rv);
        mCommentsCountTV = findViewById(R.id.sub_detail_comment_count);

        mCommentsRecyclerView.setHasFixedSize(true);
        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + coverUrl).placeholder(R.drawable.no_image_placeholder).into(mSubImageView);

        String pdfFullUrl = ServerConstants.PDF_BASE_URL + pdfUrl;

        mComments = new ArrayList<>();
        mCommentsAdapter = new SubmissionCommentsAdapter(this, mComments, submissionId);
        mCommentsRecyclerView.setAdapter(mCommentsAdapter);

//        mPdfWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfFullUrl);
        mPdfWebView.loadUrl("http://docs.google.com/gview?url=" + pdfFullUrl + "&embedded=true");

        mCommentsCountTV.setText(mCommentsArray.length() + " comments");

        loadComments();

    }

    private void loadComments() {
        for (int i = 0; i < mCommentsArray.length(); i++) {
            try {
                JSONObject singleObject = mCommentsArray.getJSONObject(i);
                JSONObject userObj = singleObject.getJSONObject("user");

                String id = singleObject.getString("id");
                String userId = singleObject.getString("user_id");
                String description = singleObject.getString("body");
                String askedBy = userObj.getString("name");
                String dateAsked = "";
                String imageUrl = userObj.getString("profile");

                if (askedBy.equals("null")) {
                    askedBy = userObj.getString("first_name") + " " + userObj.getString("last_name");
                }

                QuestionCommentClass comment = new QuestionCommentClass(id, userId, description, askedBy, dateAsked, imageUrl);
                mComments.add(comment);
                mCommentsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {

    private ArrayList<comments_limited> arrayList=new ArrayList<>();
    private CommentsLimitedAdapter commentsLimitedAdapter;
    private RecyclerView recyclerView;
    private NewsFeedStatus newsFeedStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comments, container, false);
        CommentsPage activity = (CommentsPage) getActivity();
        NewsFeedStatus newsFeedStatus = activity.getMyData();
        //NewsFeedStatus newsFeedStatus=(NewsFeedStatus)getArguments().getSerializable("DataCommentsLimited");





        recyclerView = view.findViewById(R.id.comments_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        arrayList=newsFeedStatus.getCommentsLimitedArrayList();
        Log.v("autname",arrayList.get(0).getUserClass().getEmail());
        //Log.v("length",arrayList.size()+"");
        // commentsLimitedAdapter=new CommentsLimitedAdapter(arrayList,this.getActivity());
        recyclerView.setAdapter(commentsLimitedAdapter);
        commentsLimitedAdapter.notifyDataSetChanged();



        return view;
//        Bundle extras = getArguments().getBundle("");
//        if (extras != null) {
//            newsFeedStatus = (NewsFeedStatus) getIntent().getSerializableExtra("PostDataClass");
//            //Log.v("PostDataOnCommetPage",newsFeedStatus.getFullDescription());
//            //Obtaining data
//        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = this.getArguments();
//        CommentsPage activity = (CommentsPage) getActivity();
//        NewsFeedStatus newsFeedStatus = activity.getMyData();
//        if (bundle != null) {
//            newsFeedStatus = (NewsFeedStatus) bundle.getSerializable("DataCommentsLimited");
//        }
        //newsFeedStatus=(NewsFeedStatus) savedInstanceState.getSerializable("DataCommentsLimited");
        //newsFeedStatus=(NewsFeedStatus)getArguments().getSerializable("DataCommentsLimited");
        // Log.v("postDatOncommentfrag",newsFeedStatus.getCommentDescription());
    }
}
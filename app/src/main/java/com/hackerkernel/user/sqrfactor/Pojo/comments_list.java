package com.hackerkernel.user.sqrfactor.Pojo;

import com.hackerkernel.user.sqrfactor.Pojo.commentLikeClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class comments_list implements Serializable {
    private int id,from_user_id,commentLikeCount;

    private String body,from_user_user_name,from_user_name,from_user_first_name,from_user_last_name,from_user_profile,comment_date;
    private ArrayList<commentLikeClass> commentLikeClassArrayList=new ArrayList<>();

    public ArrayList<commentLikeClass> getCommentLikeClassArrayList() {
        return commentLikeClassArrayList;
    }

    public void setCommentLikeClassArrayList(ArrayList<commentLikeClass> commentLikeClassArrayList) {
        this.commentLikeClassArrayList = commentLikeClassArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom_user_user_name() {
        return from_user_user_name;
    }

    public void setFrom_user_user_name(String from_user_user_name) {
        this.from_user_user_name = from_user_user_name;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getFrom_user_first_name() {
        return from_user_first_name;
    }

    public void setFrom_user_first_name(String from_user_first_name) {
        this.from_user_first_name = from_user_first_name;
    }

    public String getFrom_user_last_name() {
        return from_user_last_name;
    }

    public void setFrom_user_last_name(String from_user_last_name) {
        this.from_user_last_name = from_user_last_name;
    }

    public String getFrom_user_profile() {
        return from_user_profile;
    }

    public void setFrom_user_profile(String from_user_profile) {
        this.from_user_profile = from_user_profile;
    }

    public int getCommentLikeCount() {
        return commentLikeCount;
    }

    public void setCommentLikeCount(int commentLikeCount) {
        this.commentLikeCount = commentLikeCount;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public comments_list(int from_user_id, int commentLikeCount, String body, String from_user_user_name, String from_user_name, String from_user_first_name, String from_user_last_name, String from_user_profile, String comment_date) {
        this.from_user_id = from_user_id;
        this.commentLikeCount = commentLikeCount;
        this.body = body;
        this.from_user_user_name = from_user_user_name;
        this.from_user_name = from_user_name;
        this.from_user_first_name = from_user_first_name;
        this.from_user_last_name = from_user_last_name;
        this.from_user_profile = from_user_profile;
        this.comment_date = comment_date;
    }

    public comments_list(JSONObject jsonObject)
    {

        try {
            this.id=jsonObject.getInt("id");
            this.body=jsonObject.getString("body");
            this.commentLikeCount=jsonObject.getInt("like_counts");
            JSONObject user_from=jsonObject.getJSONObject("from_user");

            this.from_user_id=user_from.getInt("id");
            this.from_user_name=user_from.getString("name");
            this.from_user_user_name=user_from.getString("user_name");
            this.from_user_first_name=user_from.getString("first_name");
            this.from_user_last_name=user_from.getString("last_name");
            this.from_user_profile=user_from.getString("profile");

            JSONObject created_at=jsonObject.getJSONObject("created_at");
            this.comment_date=created_at.getString("date");


            JSONArray likes=jsonObject.getJSONArray("likes");
            for (int i=0;i<likes.length();i++)
            {
                commentLikeClass likeClass=new commentLikeClass(likes.getJSONObject(i));
                commentLikeClassArrayList.add(likeClass);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
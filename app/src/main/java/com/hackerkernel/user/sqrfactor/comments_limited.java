package com.hackerkernel.user.sqrfactor;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class comments_limited implements Serializable {

    private int comment_limited_id,comment_limited_user_id,comment_limited_commentable_id;
    private String comment_limited_commentable_type, comment_limited_body,comment_limited_created_at,comment_limited_updated_at;
    private String commentUserName,commentUserPrfile;
    private int likeCount;
    private int id,user_id,commentable_id;
    private String commentable_type,body,created_at,updated_at,deleted_at,commenterFirstName,commenterLastName;
    private transient UserClass userClass;

    public String getCommenterFirstName() {
        return commenterFirstName;
    }

    public void setCommenterFirstName(String commenterFirstName) {
        this.commenterFirstName = commenterFirstName;
    }

    public String getCommenterLastName() {
        return commenterLastName;
    }

    public void setCommenterLastName(String commenterLastName) {
        this.commenterLastName = commenterLastName;
    }





    public comments_limited(int likeCount, String commentUserName, String commentUserPrfile, int comment_limited_id,
                            int comment_limited_user_id, int comment_limited_commentable_id, String comment_limited_commentable_type,
                            String comment_limited_body, String comment_limited_created_at,
                            String comment_limited_updated_at) {
        this.likeCount=likeCount;
        this.commentUserName=commentUserName;

        this.commentUserPrfile=commentUserPrfile;
        this.comment_limited_id = comment_limited_id;
        this.comment_limited_user_id = comment_limited_user_id;

        this.comment_limited_commentable_id = comment_limited_commentable_id;
        this.comment_limited_commentable_type=comment_limited_commentable_type;
        this.comment_limited_body = comment_limited_body;
        this.comment_limited_created_at = comment_limited_created_at;
        this.comment_limited_updated_at = comment_limited_updated_at;
    }

    public comments_limited(int id,String commenterFirstName,String commenterLastName,String profileImageOfCommenter,String commenterName,String commentBody,String commmentTime,int likesCount)
    {
        this.user_id=id;
        this.body=commentBody;
        this.likeCount=likesCount;
        this.updated_at=commmentTime;
        this.commentUserName=commenterName;
        this.commentUserPrfile=profileImageOfCommenter;
        this.commenterFirstName=commenterFirstName;
        this.commenterLastName=commenterLastName;

    }

    public comments_limited(JSONObject comments)
    {
        try {
            this.id=comments.getInt("id");
            this.commentable_id=comments.getInt("commentable_id");
            this.user_id=comments.getInt("user_id");
            this.body=comments.getString("body");
            this.created_at=comments.getString("created_at");
            this.updated_at=comments.getString("updated_at");
            this.deleted_at=comments.getString("deleted_at");

            JSONObject user =comments.getJSONObject("user");
            this.commentUserName=user.getString("user_name");
            this.commentUserPrfile=user.getString("profile");
            this.commenterFirstName=user.getString("first_name");
            this.commenterLastName=user.getString("last_name");
            this.user_id=user.getInt("id");

            JSONArray likesArray=comments.getJSONArray("likes");
            //this.likeCount=likesArray.length();
            if(likesArray!=null)
            {
                this.likeCount=likesArray.length();
            }
            else
            {
                this.likeCount=0;
            }
            //this.userClass=new UserClass(comments);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCommentable_id() {
        return commentable_id;
    }

    public void setCommentable_id(int commentable_id) {
        this.commentable_id = commentable_id;
    }

    public String getCommentable_type() {
        return commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserPrfile() {
        return commentUserPrfile;
    }

    public void setCommentUserPrfile(String commentUserPrfile) {
        this.commentUserPrfile = commentUserPrfile;
    }

    public String getComment_limited_commentable_type() {
        return comment_limited_commentable_type;

    }

    public UserClass getUserClass() {
        return userClass;
    }

    public void setUserClass(UserClass userClass) {
        this.userClass = userClass;
    }

    public void setComment_limited_commentable_type(String comment_limited_commentable_type) {
        this.comment_limited_commentable_type = comment_limited_commentable_type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getComment_limited_id() {
        return comment_limited_id;
    }

    public void setComment_limited_id(int comment_limited_id) {
        this.comment_limited_id = comment_limited_id;
    }

    public int getComment_limited_user_id() {
        return comment_limited_user_id;
    }

    public void setComment_limited_user_id(int comment_limited_user_id) {
        this.comment_limited_user_id = comment_limited_user_id;
    }

    public int getComment_limited_commentable_id() {
        return comment_limited_commentable_id;
    }

    public void setComment_limited_commentable_id(int comment_limited_commentable_id) {
        this.comment_limited_commentable_id = comment_limited_commentable_id;
    }

    public String getComment_limited_body() {
        return comment_limited_body;
    }

    public void setComment_limited_body(String comment_limited_body) {
        this.comment_limited_body = comment_limited_body;
    }

    public String getComment_limited_created_at() {
        return comment_limited_created_at;
    }

    public void setComment_limited_created_at(String comment_limited_created_at) {
        this.comment_limited_created_at = comment_limited_created_at;
    }

    public String getComment_limited_updated_at() {
        return comment_limited_updated_at;
    }

    public void setComment_limited_updated_at(String comment_limited_updated_at) {
        this.comment_limited_updated_at = comment_limited_updated_at;
    }
}
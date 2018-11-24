package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotificationClass implements Serializable {
    public String type, time, user_name,name, profile, body,postType,title,description,shortDescription,slug,first_name,last_name;
    public int postId, userFromID,userId, commentID, commentType;
    public JSONObject jsonObject;


    public NotificationClass(String user_name,String type,String time, String name, String profile,String description,String shortDescription, String body,String postType,String title, int userId, int postId, int commentID, int commentType,String slug) {
        this.type = type;
        this.time = time;
        this.name = name;
        this.profile = profile;
        this.body = body;
        this.userId = userId;
        this.postId = postId;
        this.commentID = commentID;
        this.commentType = commentType;
        this.postType = postType;
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.slug = slug;
        this.user_name = user_name;
    }

    public NotificationClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        try {
            JSONObject type = jsonObject.getJSONObject("type");
            this.type = type.getString("type");

            JSONObject created = jsonObject.getJSONObject("created_at");
            this.time = created.getString("created_at");

            JSONObject userfrom = jsonObject.getJSONObject("user_from");
            this.name = userfrom.getString("name");
            this.userFromID=userfrom.getInt("id");
            this.first_name = userfrom.getString("first_name");
            this.last_name = userfrom.getString("last_name");
            this.user_name = userfrom.getString("user_name");


            this.profile = userfrom.getString("profile");


            JSONArray post = jsonObject.getJSONArray("post");
            for (int j = 0; j < post.length(); j++){
                JSONObject postObject = post.getJSONObject(j);
                if(postObject.has("type"))
                {
                    this.postType = postObject.getString("type");
                    this.slug = postObject.getString("slug");
                    this.title = postObject.getString("title");
                    this.postId = postObject.getInt("id");
                    this.userId = postObject.getInt("user_id");
                    this.description = postObject.getString("description");
                    this.shortDescription = postObject.getString("short_description");
                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getUserFromID() {
        return userFromID;
    }

    public void setUserFromID(int userFromID) {
        this.userFromID = userFromID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getPostType() {
        return postType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public JSONObject getJsonObject() {
        return jsonObject;

    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

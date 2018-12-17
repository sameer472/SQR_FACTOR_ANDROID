package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class ContributorsClass {
    private String views,numberOf_post,short_bio;
    private String id,name,first_name,last_name,user_name,profileImage;
    private JSONObject jsonObject;


    public ContributorsClass(String views, String numberOf_post, String short_bio, String id, String name, String first_name, String last_name, String user_name, String profileImage) {
        this.views = views;
        this.numberOf_post = numberOf_post;
        this.short_bio = short_bio;
        this.id = id;
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.profileImage = profileImage;
    }
    public ContributorsClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
try {
    this.views = jsonObject.getString("views");
    this.short_bio = jsonObject.getString("short_bio");
    this.numberOf_post = jsonObject.getString("no_of_posts");

    JSONObject user = jsonObject.getJSONObject("user");
    this.id = user.getString("id");
    this.name = user.getString("name");
    this.first_name = user.getString("first_name");
    this.last_name = user.getString("last_name");
    this.user_name = user.getString("user_name");
    this.profileImage = user.getString("profile");

    } catch (JSONException e) {
    e.printStackTrace();
}
    }

    public String getNumberOf_post() {
        return numberOf_post;
    }

    public void setNumberOf_post(String numberOf_post) {
        this.numberOf_post = numberOf_post;
    }

    public String getShort_bio() {
        return short_bio;
    }

    public void setShort_bio(String short_bio) {
        this.short_bio = short_bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}

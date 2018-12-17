package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class LikeClass {

    private String name,first_name,last_name,user_name,profile_url,isFollowing;
    private int id;
    private JSONObject jsonObject;

    public LikeClass(String name, String first_name, String last_name, String user_name, String profile_url, int id, JSONObject jsonObject) {
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.profile_url = profile_url;
        this.id = id;
        this.jsonObject = jsonObject;
    }


    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }

    public LikeClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;


        try {

            isFollowing=jsonObject.getString("isfollowing");
            JSONObject user= jsonObject.getJSONObject("user");
            this.id = user.getInt("id");
            this.name = user.getString("name");
            this.first_name = user.getString("first_name");
            this.last_name = user.getString("last_name");
            this.user_name = user.getString("user_name");
            this.profile_url = user.getString("profile");


        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
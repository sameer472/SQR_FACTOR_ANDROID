package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SearchResultClass implements Serializable {
    private int id;
    private String first_name,last_name,name,profile,profile_url,userName,userType;
    private JSONObject jsonObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfile_url() {
        return profile_url;
    }


    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public SearchResultClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        try {
            this.id=jsonObject.getInt("id");
            this.name=jsonObject.getString("name");
            this.first_name=jsonObject.getString("first_name");
            this.last_name=jsonObject.getString("last_name");
            this.userName=jsonObject.getString("user_name");
            this.profile=jsonObject.getString("profile");
            this.userType=jsonObject.getString("user_type");
            this.profile_url=jsonObject.getString("profile_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

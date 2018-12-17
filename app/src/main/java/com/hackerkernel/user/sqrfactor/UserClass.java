package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserClass implements Serializable{

    private JSONObject jsonObject;
    private String blueprintCount,portfolioCount,followerCount,followingCount;
    private String user_name;
    private String first_name;
    private String name;
    private String last_name;
    private String profile;
    private int userId;
    private String email;
    private String mobile;
    private String userType;
    private String short_bio,user_address;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getBlueprintCount() {
        return blueprintCount;
    }

    public void setBlueprintCount(String blueprintCount) {
        this.blueprintCount = blueprintCount;
    }

    public String getPortfolioCount() {
        return portfolioCount;
    }

    public void setPortfolioCount(String portfolioCount) {
        this.portfolioCount = portfolioCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }




    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



    public UserClass(String blueprintCount,String portfolioCount,String followerCount,String followingCount,String user_name, String first_name, String last_name, String profile, int userId, String email, String mobile, String userType) {
        this.blueprintCount=blueprintCount;
        this.portfolioCount=portfolioCount;
        this.followingCount=followingCount;
        this.followerCount=followerCount;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.userId = userId;
        this.email = email;
        this.mobile = mobile;
        this.userType = userType;
    }



    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_bio() {
        return short_bio;
    }

    public void setShort_bio(String short_bio) {
        this.short_bio = short_bio;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public UserClass(String user_name, String first_name, String last_name, String profile) {
        this.user_name = user_name;
        this.first_name = first_name;


        this.last_name = last_name;
        this.profile = profile;
    }


    public UserClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
//            JSONObject user = jsonObject.getJSONObject("user");
            this.followerCount=jsonObject.getString("followerCount");
            this.followingCount=jsonObject.getString("followingCount");
            this.portfolioCount=jsonObject.getString("portfolioCount");
            this.blueprintCount=jsonObject.getString("blueprintCount");
            this.userId = jsonObject.getInt("user_id");
            //this.name=jsonObject.getString("user_name");
            this.user_name = jsonObject.getString("user_username");
            this.first_name=jsonObject.getString("user_firstname");
            this.last_name=jsonObject.getString("user_lastname");
            this.profile=jsonObject.getString("user_profile");
            this.email=jsonObject.getString("user_email");
            this.mobile=jsonObject.getString("user_mobile");
            this.userType=jsonObject.getString("user_usertype");
            this.short_bio=jsonObject.getString("user_shortbio");
            this.user_address=jsonObject.getString("user_fullAddress");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
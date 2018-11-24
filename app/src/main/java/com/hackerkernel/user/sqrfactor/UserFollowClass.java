package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserFollowClass implements Serializable {
    private boolean returnType;
    private String message,follwoingCnt,followerCnt;
    private int userid;
    private String user_name,name,first_name,last_name,profile,email,mobile_number,user_type;
    private JSONObject jsonObject;

    public UserFollowClass(boolean returnType, String message, String follwoingCnt, String followerCnt, int userid, String user_name, String name, String first_name, String last_name, String profile, String email, String mobile_number, String user_type) {
        this.returnType = returnType;
        this.message = message;
        this.follwoingCnt = follwoingCnt;
        this.followerCnt = followerCnt;
        this.userid = userid;
        this.user_name = user_name;
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.email = email;
        this.mobile_number = mobile_number;
        this.user_type = user_type;
    }

    public UserFollowClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

      try {
          JSONObject message = jsonObject.getJSONObject("message");
          this.returnType = message.getBoolean("return");
          this.message = message.getString("message");
          this.follwoingCnt = message.getString("following_count");
          this.followerCnt = message.getString("follower_count");

          JSONObject fromuser = message.getJSONObject("from_user");
          this.userid = fromuser.getInt("id");
          this.user_name = fromuser.getString("user_name");
          this.first_name = fromuser.getString("first_name");
          this.last_name = fromuser.getString("last_name");
          this.profile = fromuser.getString("profile");
          this.email = fromuser.getString("email");
          this.mobile_number = fromuser.getString("mobile_number");
          this.name = fromuser.getString("name");
      } catch (JSONException e) {
          e.printStackTrace();
      }
    }

    public boolean isReturnType() {
        return returnType;
    }

    public void setReturnType(boolean returnType) {
        this.returnType = returnType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFollwoingCnt() {
        return follwoingCnt;
    }

    public void setFollwoingCnt(String follwoingCnt) {
        this.follwoingCnt = follwoingCnt;
    }

    public String getFollowerCnt() {
        return followerCnt;
    }

    public void setFollowerCnt(String followerCnt) {
        this.followerCnt = followerCnt;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}

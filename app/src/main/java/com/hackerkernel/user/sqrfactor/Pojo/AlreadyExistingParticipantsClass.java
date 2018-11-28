package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AlreadyExistingParticipantsClass implements Serializable {

    private String name,first_name,last_name,profile,email,profile_url;
    private int id;
    private JSONObject jsonObject;

    public AlreadyExistingParticipantsClass(String name, String first_name, String last_name, String profile, String email, String profile_url, int id) {
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.email = email;
        this.profile_url = profile_url;
        this.id = id;
    }
 public AlreadyExistingParticipantsClass(JSONObject jsonObject) {
        this.jsonObject=jsonObject;

     try {
         this.name = jsonObject.getString("name");
         this.first_name = jsonObject.getString("first_name");
         this.last_name = jsonObject.getString("last_name");
         this.profile = jsonObject.getString("profile");;
         this.email = jsonObject.getString("email");
         this.profile_url = jsonObject.getString("profile_url");
         this.id = jsonObject.getInt("id");
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

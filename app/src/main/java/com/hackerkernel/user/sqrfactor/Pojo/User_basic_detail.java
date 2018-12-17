package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User_basic_detail implements Serializable {
    private String terms_and_conditions,profile_flag,is_skip,msgCount,profile_url,
            active,mobile_verify,email_verify,type,user_type,email,
            is_approved,mobile_number,user_name,name,first_name,last_name,profile;
     private JSONObject jsonObject;


    public String getTerms_and_conditions() {
        return terms_and_conditions;
    }

    public void setTerms_and_conditions(String terms_and_conditions) {
        this.terms_and_conditions = terms_and_conditions;
    }

    public String getProfile_flag() {
        return profile_flag;
    }

    public void setProfile_flag(String profile_flag) {
        this.profile_flag = profile_flag;
    }

    public String getIs_skip() {
        return is_skip;
    }

    public void setIs_skip(String is_skip) {
        this.is_skip = is_skip;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMobile_verify() {
        return mobile_verify;
    }

    public void setMobile_verify(String mobile_verify) {
        this.mobile_verify = mobile_verify;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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

    public User_basic_detail(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        {
            try {
                this.user_name = jsonObject.getString("user_name");
                this.first_name = jsonObject.getString("first_name");
                this.name = jsonObject.getString("name");
                this.last_name = jsonObject.getString("last_name");
                this.active = jsonObject.getString("active");
                this.terms_and_conditions = jsonObject.getString("terms_and_conditions");
                this.type = jsonObject.getString("type");
                this.user_type = jsonObject.getString("user_type");
                this.mobile_number = jsonObject.getString("mobile_number");
                this.is_approved = jsonObject.getString("is_approved");
                this.is_skip = jsonObject.getString("is_skip");
                this.profile = jsonObject.getString("profile");
                this.email_verify = jsonObject.getString("email_verify");
                this.mobile_verify = jsonObject.getString("mobile_verify");
                this.profile_flag = jsonObject.getString("profile_flag");
                this.msgCount = jsonObject.getString("msgCount");
                this.email=jsonObject.getString("email");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

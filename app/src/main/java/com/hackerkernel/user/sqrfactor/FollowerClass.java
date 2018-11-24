package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FollowerClass implements Serializable {
    private String firstName,lastName,name,profile,email,profile_url,city,state,country,userName,postCount,portfolioCount;
    private  int id;
    private JSONObject jsonObject;




    public FollowerClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        try {
            this.city=jsonObject.getString("city");
            this.state=jsonObject.getString("state");
            this.country=jsonObject.getString("country");
            this.postCount=jsonObject.getString("post_count");
//            this.portfolioCount=jsonObject.getString("portfolio_count");

            JSONObject user=jsonObject.getJSONObject("user");

            if(user!=null)
            {
                this.firstName=user.getString("first_name");
                this.lastName=user.getString("last_name");
                this.name=user.getString("name");
                this.profile=user.getString("profile");
                this.email=user.getString("email");
                this.profile_url=user.getString("profile_url");
                this.id=user.getInt("id");
                this.userName=user.getString("user_name");
            }






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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getPortfolioCount() {
        return portfolioCount;
    }

    public void setPortfolioCount(String portfolioCount) {
        this.portfolioCount = portfolioCount;
    }



}

package com.hackerkernel.user.sqrfactor.Pojo;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class firmDetails implements Serializable {

    private String name, mobile_number,email,profile_url,short_bio,facebook_link,twitter_link,linkedin_link,instagram_link;
    private String types_of_firm_company,firm_or_company_name,firm_or_company_registration_number;
    private String first_name,last_name,profile,role,phone_number,aadhar_id,employee_email;
    private int country_id,state_id,city_id;
    private String employee_city,employee_state,employee_country;
    private JSONObject jsonObject;
    private int followers,following;

    public firmDetails(String name, String mobile_number, String email, String profile_url, String short_bio, String facebook_link, String twitter_link, String linkedin_link,String instagram_link, String types_of_firm_company, String firm_or_company_name, String firm_or_company_registration_number, String first_name, String last_name, String profile, String role, String phone_number, String aadhar_id, String employee_email, int country_id, int state_id, int city_id, String employee_city, String employee_state, String employee_country) {
        this.name = name;
        this.mobile_number = mobile_number;
        this.email = email;
        this.profile_url = profile_url;
        this.short_bio = short_bio;
        this.facebook_link = facebook_link;
        this.twitter_link = twitter_link;
        this.linkedin_link = linkedin_link;
        this.instagram_link = instagram_link;
        this.types_of_firm_company = types_of_firm_company;
        this.firm_or_company_name = firm_or_company_name;
        this.firm_or_company_registration_number = firm_or_company_registration_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.role = role;
        this.phone_number = phone_number;
        this.aadhar_id = aadhar_id;
        this.employee_email = employee_email;
        this.country_id = country_id;
        this.state_id = state_id;
        this.city_id = city_id;
        this.employee_city = employee_city;
        this.employee_state = employee_state;
        this.employee_country = employee_country;
    }

    public firmDetails(JSONObject jsonObject){
        this.jsonObject=jsonObject;
        try {
//            JSONObject firmDetails = jsonObject.getJSONObject("firm_details");
            this.name = jsonObject.getString("name");
            this.followers=jsonObject.getInt("no_of_followers");
            this.following=jsonObject.getInt("of_of_following");

            JSONObject basicDetails = jsonObject.getJSONObject("basic_details");
            this.mobile_number = basicDetails.getString("mobile_number");
            this.email = basicDetails.getString("email");
            this.profile_url = basicDetails.getString("profile_url");

            JSONObject company_details = jsonObject.getJSONObject("company_details");
            this.short_bio = company_details.getString("short_bio");
            this.facebook_link = company_details.getString("facebook_link");
            this.twitter_link = company_details.getString("twitter_link");
            this.linkedin_link = company_details.getString("linkedin_link");
            this.instagram_link = company_details.getString("instagram_link");
            this.types_of_firm_company = company_details.getString("types_of_firm_company");
            this.firm_or_company_name = company_details.getString("firm_or_company_name");
            this.firm_or_company_registration_number = company_details.getString("firm_or_company_registration_number");


            JSONObject employeeMemberDetails = jsonObject.getJSONObject("employee_member_details");
            this.first_name = employeeMemberDetails.getString("first_name");
            this.last_name = employeeMemberDetails.getString("last_name");
            this.profile = employeeMemberDetails.getString("profile");
            this.role = employeeMemberDetails.getString("role");
            this.phone_number = employeeMemberDetails.getString("phone_number");
            this.aadhar_id = employeeMemberDetails.getString("aadhar_id");
            this.employee_email = employeeMemberDetails.getString("email");
            this.country_id = employeeMemberDetails.getInt("country_id");
            this.state_id = employeeMemberDetails.getInt("state_id");
            this.city_id = employeeMemberDetails.getInt("city_id");

            JSONObject employeeCity = jsonObject.getJSONObject("employee_city");
            this.employee_city = employeeCity.getString("name");

            JSONObject employeeState = jsonObject.getJSONObject("employee_state");
            this.employee_state = employeeState.getString("name");

            JSONObject employeeCountry = jsonObject.getJSONObject("employee_country");
            this.employee_country = employeeCountry.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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

    public String getShort_bio() {
        return short_bio;
    }

    public void setShort_bio(String short_bio) {
        this.short_bio = short_bio;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getTypes_of_firm_company() {
        return types_of_firm_company;
    }

    public void setTypes_of_firm_company(String types_of_firm_company) {
        this.types_of_firm_company = types_of_firm_company;
    }

    public String getFirm_or_company_name() {
        return firm_or_company_name;
    }

    public void setFirm_or_company_name(String firm_or_company_name) {
        this.firm_or_company_name = firm_or_company_name;
    }

    public String getFirm_or_company_registration_number() {
        return firm_or_company_registration_number;
    }

    public void setFirm_or_company_registration_number(String firm_or_company_registration_number) {
        this.firm_or_company_registration_number = firm_or_company_registration_number;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAadhar_id() {
        return aadhar_id;
    }

    public void setAadhar_id(String aadhar_id) {
        this.aadhar_id = aadhar_id;
    }

    public String getEmployee_email() {
        return employee_email;
    }

    public void setEmployee_email(String employee_email) {
        this.employee_email = employee_email;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getLinkedin_link() {
        return linkedin_link;
    }

    public void setLinkedin_link(String linkedin_link) {
        this.linkedin_link = linkedin_link;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getState_id() {
        return state_id;

    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getEmployee_city() {
        return employee_city;
    }

    public void setEmployee_city(String employee_city) {
        this.employee_city = employee_city;
    }

    public String getEmployee_state() {
        return employee_state;
    }

    public void setEmployee_state(String employee_state) {
        this.employee_state = employee_state;
    }

    public String getEmployee_country() {
        return employee_country;
    }

    public void setEmployee_country(String employee_country) {
        this.employee_country = employee_country;
    }
}

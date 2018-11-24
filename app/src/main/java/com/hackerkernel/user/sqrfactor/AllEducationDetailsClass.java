package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AllEducationDetailsClass implements Serializable {
    private int id,user_id;
    private JSONObject jsonObject;
    private String slug,course,college_university_id,college_university,year_of_admission,year_of_graduation,created_at,updated_at,deleted_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCollege_university_id() {
        return college_university_id;
    }

    public void setCollege_university_id(String college_university_id) {
        this.college_university_id = college_university_id;
    }

    public String getCollege_university() {
        return college_university;
    }

    public void setCollege_university(String college_university) {
        this.college_university = college_university;
    }

    public String getYear_of_admission() {
        return year_of_admission;
    }

    public void setYear_of_admission(String year_of_admission) {
        this.year_of_admission = year_of_admission;
    }

    public String getYear_of_graduation() {
        return year_of_graduation;
    }

    public void setYear_of_graduation(String year_of_graduation) {
        this.year_of_graduation = year_of_graduation;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public AllEducationDetailsClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        try {
            this.id = jsonObject.getInt("id");
            this.user_id = jsonObject.getInt("user_id");
            this.slug = jsonObject.getString("slug");
            this.course = jsonObject.getString("course");
            this.college_university_id = jsonObject.getString("college_university_id");
            this.college_university = jsonObject.getString("college_university");
            this.year_of_admission = jsonObject.getString("year_of_admission");
            this.year_of_graduation = jsonObject.getString("year_of_graduation");
            this.created_at = jsonObject.getString("created_at");
            this.updated_at = jsonObject.getString("updated_at");
            this.deleted_at = jsonObject.getString("deleted_at");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
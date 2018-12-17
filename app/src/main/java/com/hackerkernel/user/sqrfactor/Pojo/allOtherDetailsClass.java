package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class allOtherDetailsClass implements Serializable {
    private int id,user_id;
    private String slug,award,award_name,project_name,services_offered,created_at,updated_at,deleted_at;
    private JSONObject jsonObject;

    public allOtherDetailsClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        try {
            this.id = jsonObject.getInt("id");
            this.user_id = jsonObject.getInt("user_id");
            this.slug = jsonObject.getString("slug");
            this.award = jsonObject.getString("award");
            this.award_name = jsonObject.getString("award_name");
            this.project_name = jsonObject.getString("project_name");
            this.services_offered = jsonObject.getString("services_offered");
            this.created_at = jsonObject.getString("created_at");
            this.updated_at = jsonObject.getString("updated_at");
            this.deleted_at = jsonObject.getString("deleted_at");


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

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getAward_name() {
        return award_name;
    }

    public void setAward_name(String award_name) {
        this.award_name = award_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getServices_offered() {
        return services_offered;
    }

    public void setServices_offered(String services_offered) {
        this.services_offered = services_offered;
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
}

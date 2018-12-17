package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SecondPageDesignData implements Serializable {
    private int id, user_id,user_post_id;
    private String status,building_program,select_design_type,start_year,end_year,total_budget,currency,location,lat,lng,project_part,
            competition_link,college_part,college_link,tags,created_at,updated_at,deleted_at;
    private transient JSONObject jsonObject;
    public SecondPageDesignData()
    {

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

    public int getUser_post_id() {
        return user_post_id;
    }

    public void setUser_post_id(int user_post_id) {
        this.user_post_id = user_post_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuilding_program() {
        return building_program;
    }

    public void setBuilding_program(String building_program) {
        this.building_program = building_program;
    }

    public String getSelect_design_type() {
        return select_design_type;
    }

    public void setSelect_design_type(String select_design_type) {
        this.select_design_type = select_design_type;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getEnd_year() {
        return end_year;
    }

    public void setEnd_year(String end_year) {
        this.end_year = end_year;
    }

    public String getTotal_budget() {
        return total_budget;
    }

    public void setTotal_budget(String total_budget) {
        this.total_budget = total_budget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getProject_part() {
        return project_part;
    }

    public void setProject_part(String project_part) {
        this.project_part = project_part;
    }

    public String getCompetition_link() {
        return competition_link;
    }

    public void setCompetition_link(String competition_link) {
        this.competition_link = competition_link;
    }

    public String getCollege_part() {
        return college_part;
    }

    public void setCollege_part(String college_part) {
        this.college_part = college_part;
    }

    public String getCollege_link() {
        return college_link;
    }

    public void setCollege_link(String college_link) {
        this.college_link = college_link;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public SecondPageDesignData(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {
            this.id=jsonObject.getInt("id");
            this.user_id=jsonObject.getInt("user_id");
            this.user_post_id=jsonObject.getInt("user_post_id");
            this.status=jsonObject.getString("status");
            this.building_program=jsonObject.getString("building_program");
            this.select_design_type=jsonObject.getString("select_design_type");
            this.start_year=jsonObject.getString("start_year");
            this.end_year=jsonObject.getString("end_year");
            this.total_budget=jsonObject.getString("total_budget");
            this.currency=jsonObject.getString("currency");
            this.location=jsonObject.getString("location");
            this.project_part=jsonObject.getString("project_part");
            this.competition_link=jsonObject.getString("competition_link");
            this.college_part=jsonObject.getString("college_part");
            this.college_link=jsonObject.getString("college_link");
            this.tags=jsonObject.getString("tags");
            this.lat=jsonObject.getString("lat");
            this.lng=jsonObject.getString("lng");
            this.created_at=jsonObject.getString("created_at");
            this.updated_at=jsonObject.getString("updated_at");
            this.deleted_at=jsonObject.getString("deleted_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

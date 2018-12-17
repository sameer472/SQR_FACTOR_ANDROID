package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ProfessionalDetailsClass implements Serializable {
    private int id,user_id;
    private String slug,role,company_firm_or_college_university_id,company_college_type,company_firm_or_college_university,start_date,end_date_of_working_currently,
            salary_stripend,created_at,updated_at,deleted_at;
    private JSONObject jsonObject;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany_firm_or_college_university_id() {
        return company_firm_or_college_university_id;
    }

    public void setCompany_firm_or_college_university_id(String company_firm_or_college_university_id) {
        this.company_firm_or_college_university_id = company_firm_or_college_university_id;
    }

    public String getCompany_college_type() {
        return company_college_type;
    }

    public void setCompany_college_type(String company_college_type) {
        this.company_college_type = company_college_type;
    }

    public String getCompany_firm_or_college_university() {
        return company_firm_or_college_university;
    }

    public void setCompany_firm_or_college_university(String company_firm_or_college_university) {
        this.company_firm_or_college_university = company_firm_or_college_university;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date_of_working_currently() {
        return end_date_of_working_currently;
    }

    public void setEnd_date_of_working_currently(String end_date_of_working_currently) {
        this.end_date_of_working_currently = end_date_of_working_currently;
    }

    public String getSalary_stripend() {
        return salary_stripend;
    }

    public void setSalary_stripend(String salary_stripend) {
        this.salary_stripend = salary_stripend;
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

    public ProfessionalDetailsClass (JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {
            this.id=jsonObject.getInt("id");
            this.user_id=jsonObject.getInt("user_id");
            this.slug=jsonObject.getString("slug");
            this.role=jsonObject.getString("role");
            this.company_firm_or_college_university=jsonObject.getString("company_firm_or_college_university");
            this.company_firm_or_college_university_id=jsonObject.getString("company_firm_or_college_university_id");
            this.company_college_type=jsonObject.getString("company_college_type");
            this.start_date=jsonObject.getString("start_date");
            this.end_date_of_working_currently=jsonObject.getString("end_date_of_working_currently");
            this.salary_stripend=jsonObject.getString("salary_stripend");
            this.created_at=jsonObject.getString("created_at");
            this.updated_at=jsonObject.getString("updated_at");
            this.deleted_at=jsonObject.getString("deleted_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
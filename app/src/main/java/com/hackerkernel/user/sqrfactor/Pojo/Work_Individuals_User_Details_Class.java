package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Work_Individuals_User_Details_Class implements Serializable {

    private String years_since_service,followerCount,followingCount,full_name,email,phone_number,date_of_birth,gender,short_bio,occupation,college_university,course,year_of_admission,year_of_graduation,
            role,company_firm_or_college_university,start_date,end_date_of_working_currently,salary_stripend,award,award_name,project_name,services_offered;
    private JSONObject jsonObject;

    public Work_Individuals_User_Details_Class()
    {}

    public Work_Individuals_User_Details_Class(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {

            this.followerCount=jsonObject.getString("no_of_followers");
            this.followingCount=jsonObject.getString("of_of_following");

            this.full_name=jsonObject.getString("full_name");
            this.email=jsonObject.getString("email");
            this.phone_number=jsonObject.getString("phone_number");

            JSONObject basic_details=jsonObject.getJSONObject("basic_details");
            this.years_since_service=basic_details.getString("years_since_service");
            this.date_of_birth=basic_details.getString("date_of_birth");
            this.gender=basic_details.getString("gender");
            this.short_bio=basic_details.getString("short_bio");
            this.occupation=basic_details.getString("occupation");

            JSONObject educational_details=jsonObject.getJSONObject("educational_details");
            this.college_university=educational_details.getString("college_university");
            this.course=educational_details.getString("course");
            this.year_of_admission=educational_details.getString("year_of_admission");
            this.year_of_graduation=educational_details.getString("year_of_graduation");

            JSONObject professional_details=jsonObject.getJSONObject("professional_details");
            this.role=professional_details.getString("role");
            this.company_firm_or_college_university=professional_details.getString("company_firm_or_college_university");
            this.start_date=professional_details.getString("start_date");
            this.end_date_of_working_currently=professional_details.getString("end_date_of_working_currently");
            this.salary_stripend=professional_details.getString("salary_stripend");

            JSONObject other_details=jsonObject.getJSONObject("other_details");
            this.award=other_details.getString("award");
            this.award_name=other_details.getString("award_name");
            this.project_name=other_details.getString("project_name");
            this.services_offered=other_details.getString("services_offered");

            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getYears_since_service() {
        return years_since_service;
    }

    public void setYears_since_service(String years_since_service) {
        this.years_since_service = years_since_service;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShort_bio() {
        return short_bio;
    }

    public void setShort_bio(String short_bio) {
        this.short_bio = short_bio;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCollege_university() {
        return college_university;
    }

    public void setCollege_university(String college_university) {
        this.college_university = college_university;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

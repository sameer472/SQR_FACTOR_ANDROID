package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

public class UserData {
    private int id, user_id;
    private String date_of_birth;
    private String phone_number;
    private String aadhaar_id;
    private String address;
    private String occupation;
    private String pin_code;
    private String country_id;
    private String state_id;
    private String city_id;
    private String shot_bio;
    private String facebook_link;
    private String twitter_link;
    private String linkedin_link;
    private String instagram_link;
    private String looking_for_an_architect;
    private String name_of_the_company;
    private String business_description;
    private String types_of_firm_company;
    private String firm_or_company_name;
    private String firm_or_company_registration_number;
    private String role;
    private String gender;
    private String describe_yourself;
    private String course;
    private String college_university;
    private String year_of_admission;
    private String year_of_graduation;
    private String years_in_service;
    private String see_buildtrail;
    private String bsiness_description;
    private String year_in_service;
    private String firm_size;
    private String asset_served;
    private String city_served;
    private String webside;
    private String salary_stripend_other_details;
    private String years_since_service;
    private String coa_registration;
    private String company_firm_or_college_university;
    private String services_offered;
    private String award_name;
    private String project_name;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String emails;
    private User_basic_detail user_basic_detail;


    private JSONObject jsonObject;

    public UserData(int id, int user_id, String date_of_birth, String phone_number, String aadhaar_id, String address, String occupation, String pin_code, String country_id, String state_id, String city_id, String shot_bio, String facebook_link, String twitter_link, String linkedin_link, String instagram_link,
                    String looking_for_an_architect, String name_of_the_company, String business_description, String types_of_firm_company, String firm_or_company_name, String firm_or_company_registration_number, String role, String gender, String describe_yourself, String course, String college_university,
                    String year_of_admission, String year_of_graduation, String years_in_service, String see_buildtrail, String bsiness_description, String year_in_service, String firm_size, String asset_served, String city_served, String webside, String salary_stripend_other_details, String years_since_service,
                    String coa_registration, String company_firm_or_college_university, String services_offered, String award_name, String project_name, String created_at, String updated_at, String deleted_at, String emails) {
        this.id = id;
        this.user_id = user_id;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.aadhaar_id = aadhaar_id;
        this.address = address;
        this.occupation = occupation;
        this.pin_code = pin_code;
        this.country_id = country_id;
        this.state_id = state_id;
        this.city_id = city_id;
        this.shot_bio = shot_bio;
        this.facebook_link = facebook_link;
        this.twitter_link = twitter_link;
        this.linkedin_link = linkedin_link;
        this.instagram_link = instagram_link;
        this.looking_for_an_architect = looking_for_an_architect;
        this.name_of_the_company = name_of_the_company;
        this.business_description = business_description;
        this.types_of_firm_company = types_of_firm_company;
        this.firm_or_company_name = firm_or_company_name;
        this.firm_or_company_registration_number = firm_or_company_registration_number;
        this.role = role;
        this.gender = gender;
        this.describe_yourself = describe_yourself;
        this.course = course;
        this.college_university = college_university;
        this.year_of_admission = year_of_admission;
        this.year_of_graduation = year_of_graduation;
        this.years_in_service = years_in_service;
        this.see_buildtrail = see_buildtrail;
        this.bsiness_description = bsiness_description;
        this.year_in_service = year_in_service;
        this.firm_size = firm_size;
        this.asset_served = asset_served;
        this.city_served = city_served;
        this.webside = webside;
        this.salary_stripend_other_details = salary_stripend_other_details;
        this.years_since_service = years_since_service;
        this.coa_registration = coa_registration;
        this.company_firm_or_college_university = company_firm_or_college_university;
        this.services_offered = services_offered;
        this.award_name = award_name;
        this.project_name = project_name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.emails = emails;
    }



    public UserData(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.id = jsonObject.getInt("id");
            this.user_id = jsonObject.getInt("user_id");
            this.date_of_birth = jsonObject.getString("date_of_birth");
            this.phone_number = jsonObject.getString("phone_number");
            this.aadhaar_id = jsonObject.getString("aadhar_id");
            this.address = jsonObject.getString("address");
            this.occupation = jsonObject.getString("occupation");
            this.pin_code = jsonObject.getString("pin_code");
            this.country_id = jsonObject.getString("country_id");
            this.state_id = jsonObject.getString("state_id");
            this.city_id = jsonObject.getString("city_id");
            this.shot_bio = jsonObject.getString("short_bio");
            this.facebook_link = jsonObject.getString("facebook_link");
            this.twitter_link = jsonObject.getString("twitter_link");
            this.linkedin_link = jsonObject.getString("linkedin_link");
            this.instagram_link = jsonObject.getString("instagram_link");
            this.looking_for_an_architect = jsonObject.getString("looking_for_an_architect");
            this.name_of_the_company = jsonObject.getString("name_of_the_company");
            this.business_description = jsonObject.getString("business_description");
            this.types_of_firm_company = jsonObject.getString("types_of_firm_company");
            this.firm_or_company_name = jsonObject.getString("firm_or_company_name");
            this.firm_or_company_registration_number = jsonObject.getString("firm_or_company_registration_number");
            this.role = jsonObject.getString("role");
            this.gender = jsonObject.getString("gender");
            this.describe_yourself = jsonObject.getString("describe_yourself");
            this.course = jsonObject.getString("course");
            this.college_university = jsonObject.getString("college_university");
            this.year_of_admission = jsonObject.getString("year_of_admission");
            this.year_of_graduation = jsonObject.getString("year_of_graduation");
            this.year_in_service = jsonObject.getString("years_in_service");
            this.see_buildtrail = jsonObject.getString("see_buildtrail");
            this.bsiness_description = jsonObject.getString("bsiness_description");
            this.year_in_service = jsonObject.getString("year_in_service");
            this.firm_size = jsonObject.getString("firm_size");
            this.asset_served = jsonObject.getString("asset_served");
            this.city_served = jsonObject.getString("city_served");
            this.webside = jsonObject.getString("webside");
            this.salary_stripend_other_details = jsonObject.getString("salary_stripend_other_details");
            this.years_since_service = jsonObject.getString("years_since_service");
            this.coa_registration = jsonObject.getString("coa_registration");
            this.company_firm_or_college_university = jsonObject.getString("company_firm_or_college_university");
            this.services_offered = jsonObject.getString("services_offered");
            this.award_name = jsonObject.getString("award_name");
            this.project_name = jsonObject.getString("project_name");
            this.created_at = jsonObject.getString("created_at");
            this.updated_at = jsonObject.getString("updated_at");
            this.deleted_at = jsonObject.getString("deleted_at");


            this.emails=null;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User_basic_detail getUser_basic_detail() {
        return user_basic_detail;
    }

    public void setUser_basic_detail(User_basic_detail user_basic_detail) {
        this.user_basic_detail = user_basic_detail;
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

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAadhaar_id() {
        return aadhaar_id;
    }

    public void setAadhaar_id(String aadhaar_id) {
        this.aadhaar_id = aadhaar_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getShot_bio() {
        return shot_bio;
    }

    public void setShot_bio(String shot_bio) {
        this.shot_bio = shot_bio;
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

    public String getLinkedin_link() {
        return linkedin_link;
    }

    public void setLinkedin_link(String linkedin_link) {
        this.linkedin_link = linkedin_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getLooking_for_an_architect() {
        return looking_for_an_architect;
    }

    public void setLooking_for_an_architect(String looking_for_an_architect) {
        this.looking_for_an_architect = looking_for_an_architect;
    }

    public String getName_of_the_company() {
        return name_of_the_company;
    }

    public void setName_of_the_company(String name_of_the_company) {
        this.name_of_the_company = name_of_the_company;
    }

    public String getBusiness_description() {
        return business_description;
    }

    public void setBusiness_description(String business_description) {
        this.business_description = business_description;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescribe_yourself() {
        return describe_yourself;
    }

    public void setDescribe_yourself(String describe_yourself) {
        this.describe_yourself = describe_yourself;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getYears_in_service() {
        return years_in_service;
    }

    public void setYears_in_service(String years_in_service) {
        this.years_in_service = years_in_service;
    }

    public String getSee_buildtrail() {
        return see_buildtrail;
    }

    public void setSee_buildtrail(String see_buildtrail) {
        this.see_buildtrail = see_buildtrail;
    }

    public String getBsiness_description() {
        return bsiness_description;
    }

    public void setBsiness_description(String bsiness_description) {
        this.bsiness_description = bsiness_description;
    }

    public String getYear_in_service() {
        return year_in_service;
    }

    public void setYear_in_service(String year_in_service) {
        this.year_in_service = year_in_service;
    }

    public String getFirm_size() {
        return firm_size;
    }

    public void setFirm_size(String firm_size) {
        this.firm_size = firm_size;
    }

    public String getAsset_served() {
        return asset_served;
    }

    public void setAsset_served(String asset_served) {
        this.asset_served = asset_served;
    }

    public String getCity_served() {
        return city_served;
    }

    public void setCity_served(String city_served) {
        this.city_served = city_served;
    }

    public String getWebside() {
        return webside;
    }

    public void setWebside(String webside) {
        this.webside = webside;
    }

    public String getSalary_stripend_other_details() {
        return salary_stripend_other_details;
    }

    public void setSalary_stripend_other_details(String salary_stripend_other_details) {
        this.salary_stripend_other_details = salary_stripend_other_details;
    }

    public String getYears_since_service() {
        return years_since_service;
    }

    public void setYears_since_service(String years_since_service) {
        this.years_since_service = years_since_service;
    }

    public String getCoa_registration() {
        return coa_registration;
    }

    public void setCoa_registration(String coa_registration) {
        this.coa_registration = coa_registration;
    }

    public String getCompany_firm_or_college_university() {
        return company_firm_or_college_university;
    }

    public void setCompany_firm_or_college_university(String company_firm_or_college_university) {
        this.company_firm_or_college_university = company_firm_or_college_university;
    }

    public String getServices_offered() {
        return services_offered;
    }

    public void setServices_offered(String services_offered) {
        this.services_offered = services_offered;
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

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getEmails() {
        return emails;

    }

    public void setEmails(String emails) {
        this.emails = emails;
    }
}
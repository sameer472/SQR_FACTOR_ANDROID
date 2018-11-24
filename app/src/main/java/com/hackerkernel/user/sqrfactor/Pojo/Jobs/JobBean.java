package com.hackerkernel.user.sqrfactor.Pojo.Jobs;

import java.util.List;

public class JobBean {
    String id,
            user_id,
            slug,
            job_title,
            description,
            category,
            type_of_position,
            work_experience,
            firm,
            country_id,
            state_id,
            city_id,
            salary_type,
            maximum_salary,
            minimum_salary,
            job_offer_expires_on,
            created_at,
            updated_at,
            deleted_at;
    List<SkillsBean> skillsBeanList;
    List<EduQulBean> eduQulBeanList;

    public JobBean() {
    }

    public JobBean(String id, String user_id, String slug, String job_title, String description, String category, String type_of_position, String work_experience, String firm, String country_id, String state_id, String city_id, String salary_type, String maximum_salary, String minimum_salary, String job_offer_expires_on, String created_at, String updated_at, String deleted_at, List<SkillsBean> skillsBeanList, List<EduQulBean> eduQulBeanList) {
        this.id = id;
        this.user_id = user_id;
        this.slug = slug;
        this.job_title = job_title;
        this.description = description;
        this.category = category;
        this.type_of_position = type_of_position;
        this.work_experience = work_experience;
        this.firm = firm;
        this.country_id = country_id;
        this.state_id = state_id;
        this.city_id = city_id;
        this.salary_type = salary_type;
        this.maximum_salary = maximum_salary;
        this.minimum_salary = minimum_salary;
        this.job_offer_expires_on = job_offer_expires_on;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.skillsBeanList = skillsBeanList;
        this.eduQulBeanList = eduQulBeanList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType_of_position() {
        return type_of_position;
    }

    public void setType_of_position(String type_of_position) {
        this.type_of_position = type_of_position;
    }

    public String getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(String work_experience) {
        this.work_experience = work_experience;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
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

    public String getSalary_type() {
        return salary_type;
    }

    public void setSalary_type(String salary_type) {
        this.salary_type = salary_type;
    }

    public String getMaximum_salary() {
        return maximum_salary;
    }

    public void setMaximum_salary(String maximum_salary) {
        this.maximum_salary = maximum_salary;
    }

    public String getMinimum_salary() {
        return minimum_salary;
    }

    public void setMinimum_salary(String minimum_salary) {
        this.minimum_salary = minimum_salary;
    }

    public String getJob_offer_expires_on() {
        return job_offer_expires_on;
    }

    public void setJob_offer_expires_on(String job_offer_expires_on) {
        this.job_offer_expires_on = job_offer_expires_on;
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

    public List<SkillsBean> getSkillsBeanList() {
        return skillsBeanList;
    }

    public void setSkillsBeanList(List<SkillsBean> skillsBeanList) {
        this.skillsBeanList = skillsBeanList;
    }

    public List<EduQulBean> getEduQulBeanList() {
        return eduQulBeanList;
    }

    public void setEduQulBeanList(List<EduQulBean> eduQulBeanList) {
        this.eduQulBeanList = eduQulBeanList;
    }
}

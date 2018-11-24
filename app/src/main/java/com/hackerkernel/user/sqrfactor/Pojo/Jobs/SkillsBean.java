package com.hackerkernel.user.sqrfactor.Pojo.Jobs;

public class SkillsBean {
    String id,
            users_job_id,
            slug,
            skills,
            created_at,
            updated_at,
            deleted_at;

    public SkillsBean() {
    }

    public SkillsBean(String id, String users_job_id, String slug, String skills, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.users_job_id = users_job_id;
        this.slug = slug;
        this.skills = skills;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsers_job_id() {
        return users_job_id;
    }

    public void setUsers_job_id(String users_job_id) {
        this.users_job_id = users_job_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
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

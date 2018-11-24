package com.hackerkernel.user.sqrfactor.Pojo.Jobs;

public class EduQulBean {
    String id,
            users_job_id,
            slug,
            educational_qualification,
            created_at,
            updated_at,
            deleted_at;

    public EduQulBean() {
    }

    public EduQulBean(String id, String users_job_id, String slug, String educational_qualification, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.users_job_id = users_job_id;
        this.slug = slug;
        this.educational_qualification = educational_qualification;
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

    public String getEducational_qualification() {
        return educational_qualification;
    }

    public void setEducational_qualification(String educational_qualification) {
        this.educational_qualification = educational_qualification;
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

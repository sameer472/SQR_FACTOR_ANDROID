package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONArray;

public class WallQuestionClass {
    private String subject;
    private String description;
    private String announcedBy;
    private String id;
    private String userId;
    private JSONArray commentsArray;

    public WallQuestionClass(String subject, String description, String announcedBy, String id, String userId, JSONArray commentsArray) {
        this.subject = subject;
        this.description = description;
        this.announcedBy = announcedBy;
        this.commentsArray = commentsArray;
        this.id = id;
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnouncedBy() {
        return announcedBy;
    }

    public void setAnnouncedBy(String announcedBy) {
        this.announcedBy = announcedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONArray getCommentsArray() {
        return commentsArray;
    }

    public void setCommentsArray(JSONArray commentsArray) {
        this.commentsArray = commentsArray;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

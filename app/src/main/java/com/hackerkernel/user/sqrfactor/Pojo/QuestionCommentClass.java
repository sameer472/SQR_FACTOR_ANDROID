package com.hackerkernel.user.sqrfactor.Pojo;

public class QuestionCommentClass {
    private String id;
    private String userId;
    private String description;
    private String askedBy;
    private String dateAsked;
    private String imageUrl;

    public QuestionCommentClass(String id, String userId, String description, String askedBy, String dateAsked, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.askedBy = askedBy;
        this.dateAsked = dateAsked;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(String askedBy) {
        this.askedBy = askedBy;
    }

    public String getDateAsked() {
        return dateAsked;
    }

    public void setDateAsked(String dateAsked) {
        this.dateAsked = dateAsked;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

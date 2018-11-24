package com.hackerkernel.user.sqrfactor.Pojo;

public class CompetitionClass {
    private String id;
    private String userId;
    private String competitionName;
    private String startTimeAgo;
    private String imageUrl;
    private String lastSubmissionDate;
    private String lastRegistrationDate;
    private String prize;
    private String competitionType;
    private String slug;

    public CompetitionClass(String id, String userId, String slug, String competitionName, String startTimeAgo, String imageUrl, String lastSubmissionDate, String lastRegistrationDate, String prize, String competitionType) {
        this.id = id;
        this.userId = userId;
        this.slug = slug;
        this.competitionName = competitionName;
        this.startTimeAgo = startTimeAgo;
        this.imageUrl = imageUrl;
        this.lastSubmissionDate = lastSubmissionDate;
        this.lastRegistrationDate = lastRegistrationDate;
        this.prize = prize;
        this.competitionType = competitionType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getStartTimeAgo() {
        return startTimeAgo;
    }

    public void setStartTimeAgo(String startTimeAgo) {
        this.startTimeAgo = startTimeAgo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastSubmissionDate() {
        return lastSubmissionDate;
    }

    public void setLastSubmissionDate(String lastSubmissionDate) {
        this.lastSubmissionDate = lastSubmissionDate;
    }

    public String getLastRegistrationDate() {
        return lastRegistrationDate;
    }

    public void setLastRegistrationDate(String lastRegistrationDate) {
        this.lastRegistrationDate = lastRegistrationDate;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getCompetitionType() {
        return competitionType;
    }

    public void setCompetitionType(String competitionType) {
        this.competitionType = competitionType;
    }
}

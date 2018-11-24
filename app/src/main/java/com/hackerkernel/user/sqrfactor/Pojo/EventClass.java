package com.hackerkernel.user.sqrfactor.Pojo;

public class EventClass {
    private String id;
    private String creatorId;
    private String slug;
    private String coverUrl;
    private String title;
    private String description;
    private String venue;
    private String event_type;
    private String startTimeAgo;


    public EventClass(String event_type, String id, String creatorId, String slug, String coverUrl, String title, String description, String venue, String startTimeAgo) {
        this.event_type = event_type;
        this.id = id;
        this.creatorId = creatorId;
        this.slug = slug;
        this.coverUrl = coverUrl;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.startTimeAgo = startTimeAgo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStartTimeAgo() {
        return startTimeAgo;
    }

    public void setStartTimeAgo(String startTimeAgo) {
        this.startTimeAgo = startTimeAgo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}

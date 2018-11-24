package com.hackerkernel.user.sqrfactor.Pojo;

import org.json.JSONArray;

public class SubmissionClass {
    private String id;
    private String title;
    private String code;
    private String coverUrl;
    private String pdfUrl;
    private JSONArray commentsArray;

    public SubmissionClass(String id, String title, String code, String coverUrl, String pdfUrl, JSONArray commentsArray) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.coverUrl = coverUrl;
        this.pdfUrl = pdfUrl;
        this.commentsArray = commentsArray;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public JSONArray getCommentsArray() {
        return commentsArray;
    }

    public void setCommentsArray(JSONArray commentsArray) {
        this.commentsArray = commentsArray;
    }
}

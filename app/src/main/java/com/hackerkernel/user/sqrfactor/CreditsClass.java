package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CreditsClass implements Serializable {
    private String article;
    private String totalViews,firstWeekViews;
    private String credits;
    private String credits_redeemed;
    private String title;
    private JSONObject jsonObject;

    public CreditsClass(String article, String totalViews, String firstWeekViews, String credits,String title,String credits_redeemed) {
        this.article = article;
        this.totalViews = totalViews;
        this.firstWeekViews = firstWeekViews;
        this.credits = credits;
        this.title = title;
        this.credits_redeemed = credits_redeemed;
    }
    public CreditsClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.title = jsonObject.getString("title");
            this.firstWeekViews = jsonObject.getString("week_views");
            this.credits = jsonObject.getString("credits_earned");
            this.totalViews = jsonObject.getString("views_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }


    public String getfirstWeekViews() {
        return firstWeekViews;
    }

    public String getCredits_redeemed() {
        return credits_redeemed;
    }

    public void setCredits_redeemed(String credits_redeemed) {
        this.credits_redeemed = credits_redeemed;
    }

    public void setfirstWeekViews(String firstWeekViews) {

        firstWeekViews = firstWeekViews;
    }

    public String getCredits() {
        return credits;
    }

    public String getFirstWeekViews() {
        return firstWeekViews;
    }

    public void setFirstWeekViews(String firstWeekViews) {
        this.firstWeekViews = firstWeekViews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}

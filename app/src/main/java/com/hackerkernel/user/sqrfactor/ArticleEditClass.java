package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ArticleEditClass implements Serializable {
    private int id,user_id,feature,report;
    private String slug,credits_earned,credits_paid,credits_redeemed,type,title,image,week_views,banner_image,short_description,description,paid_at,created_at,updated_at,deleted_at;
    private transient JSONObject jsonObject;

    public ArticleEditClass()
    {

    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String  getCredits_earned() {
        return credits_earned;
    }

    public void setCredits_earned(String credits_earned) {
        this.credits_earned = credits_earned;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getWeek_views() {
        return week_views;
    }

    public void setWeek_views(String week_views) {
        this.week_views = week_views;
    }

    public String getCredits_redeemed() {
        return credits_redeemed;
    }

    public void setCredits_redeemed(String credits_redeemed) {
        this.credits_redeemed = credits_redeemed;
    }

    public String getCredits_paid() {
        return credits_paid;
    }

    public void setCredits_paid(String credits_paid) {
        this.credits_paid = credits_paid;
    }

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
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

    public ArticleEditClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {
            this.id=jsonObject.getInt("id");
            this.user_id=jsonObject.getInt("user_id");
            this.week_views=jsonObject.getString("week_views");
            this.credits_earned=jsonObject.getString("credits_earned");
            this.credits_redeemed=jsonObject.getString("credits_redeemed");
            this.credits_paid=jsonObject.getString("credits_paid");
            this.short_description=jsonObject.getString("short_description");
            this.description=jsonObject.getString("description");
            this.created_at=jsonObject.getString("created_at");
            this.deleted_at=jsonObject.getString("deleted_at");
            this.image=jsonObject.getString("image");
            this.banner_image=jsonObject.getString("banner_image");
            this.title=jsonObject.getString("title");
            this.type=jsonObject.getString("type");
            this.updated_at=jsonObject.getString("updated_at");




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
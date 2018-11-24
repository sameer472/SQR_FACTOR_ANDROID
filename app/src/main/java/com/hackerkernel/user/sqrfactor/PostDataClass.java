package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PostDataClass implements Serializable {


    private int id,user_id,comments_count,shared_id,like_count;

    private String slug,type,title,image,banner_image,short_description,description,credits_earned,week_views,credits_redeemed,
            credits_paid,paid_at,created_at,updated_at,deleted_at,user_post_id,is_shared;

    private String userName,userProfile;
    private String firstName,lastName;




    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public PostDataClass()
    {

    }

    public PostDataClass(JSONObject post)
    {
        try {
            this.user_id = post.getInt("user_id");
            this.title = post.getString("title");
            this.banner_image = post.getString("banner_image");
            this.short_description = post.getString("short_description");
            this.description = post.getString("description");
            this.image = post.getString("image");
            this.updated_at = post.getString("updated_at");
            this.comments_count = post.getInt("comments_count");
            this.shared_id=post.getInt("shared_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public PostDataClass(int id, int user_id, int comments_count, int shared_id, String slug, String type, String title, String image, String banner_image, String short_description, String description, String credits_earned, String week_views, String credits_redeemed, String credits_paid, String paid_at, String created_at, String updated_at, String deleted_at, String user_post_id, String is_shared) {
        this.id = id;
        this.user_id = user_id;
        this.comments_count = comments_count;
        this.shared_id = shared_id;
        this.slug = slug;
        this.type = type;
        this.title = title;
        this.image = image;
        this.banner_image = banner_image;
        this.short_description = short_description;
        this.description = description;
        this.credits_earned = credits_earned;
        this.week_views = week_views;
        this.credits_redeemed = credits_redeemed;
        this.credits_paid = credits_paid;
        this.paid_at = paid_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.user_post_id = user_post_id;
        this.is_shared = is_shared;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getShared_id() {
        return shared_id;
    }

    public void setShared_id(int shared_id) {
        this.shared_id = shared_id;
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

    public String getCredits_earned() {
        return credits_earned;
    }

    public void setCredits_earned(String credits_earned) {
        this.credits_earned = credits_earned;
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

    public String getUser_post_id() {
        return user_post_id;
    }

    public void setUser_post_id(String user_post_id) {
        this.user_post_id = user_post_id;
    }

    public String getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(String is_shared) {
        this.is_shared = is_shared;
    }
}

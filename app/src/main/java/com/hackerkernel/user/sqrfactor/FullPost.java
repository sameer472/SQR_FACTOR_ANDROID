package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FullPost implements Serializable {
    public String authName,user_name_of_post,authImageUrl,first_name,last_name;
    int id, user_id,comments_count,commentId,shared_id,views_count;
    String slug,type,title,image,banner_image,short_description,description,credits_earned,week_views,credits_redeemed,credits_paid,paid_at,created_at
            ,updated_at,deleted_at,user_post_id,is_shared,like;
    public ArrayList<comments_limited>  commentsLimitedArrayList=new ArrayList<>();
    public ArrayList<Integer> AllLikesId=new ArrayList<>();
    public ArrayList<Integer> AllCommentId=new ArrayList<>();
    public transient JSONObject jsonObject;
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

    public int getShared_id() {
        return shared_id;
    }

    public void setShared_id(int shared_id) {
        this.shared_id = shared_id;
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

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getUser_name_of_post() {
        return user_name_of_post;
    }

    public void setUser_name_of_post(String user_name_of_post) {
        this.user_name_of_post = user_name_of_post;
    }

    public String getAuthImageUrl() {
        return authImageUrl;
    }

    public void setAuthImageUrl(String authImageUrl) {
        this.authImageUrl = authImageUrl;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public ArrayList<comments_limited> getCommentsLimitedArrayList() {
        return commentsLimitedArrayList;
    }

    public void setCommentsLimitedArrayList(ArrayList<comments_limited> commentsLimitedArrayList) {
        this.commentsLimitedArrayList = commentsLimitedArrayList;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ArrayList<Integer> getAllLikesId() {
        return AllLikesId;
    }

    public void setAllLikesId(ArrayList<Integer> allLikesId) {
        AllLikesId = allLikesId;
    }

    public ArrayList<Integer> getAllCommentId() {
        return AllCommentId;
    }

    public void setAllCommentId(ArrayList<Integer> allCommentId) {
        AllCommentId = allCommentId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public FullPost(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;

        try {
            this.id = jsonObject.getInt("id");
            this.slug = jsonObject.getString("slug");
            this.user_id=jsonObject.getInt("user_id");
            this.type = jsonObject.getString("type");
            this.title = jsonObject.getString("title");
            this.image= jsonObject.getString("image");
            this.banner_image = jsonObject.getString("banner_image");
            this.short_description = jsonObject.getString("short_description");
            this.description = jsonObject.getString("description");
            this.credits_earned = jsonObject.getString("credits_earned");
            this.shared_id=jsonObject.getInt("shared_id");
            this.week_views = jsonObject.getString("week_views");
            this.views_count=jsonObject.getInt("views_count");
            this.credits_redeemed = jsonObject.getString("credits_redeemed");
            this.credits_paid = jsonObject.getString("credits_paid");
            this.paid_at = jsonObject.getString("paid_at");
            this.created_at = jsonObject.getString("created_at");
            this.updated_at = jsonObject.getString("updated_at");
            this.deleted_at = jsonObject.getString("deleted_at");
            this.user_post_id = jsonObject.getString("user_post_id");
            this.is_shared = jsonObject.getString("is_shared");
            // this.comments_count = jsonObject.getInt("comments_count");

            JSONObject user = jsonObject.getJSONObject("user");


            this.first_name=user.getString("first_name");
            this.last_name=user.getString("last_name");
            this.authName = user.getString("name");
            this.authImageUrl = user.getString("profile");
            this.user_name_of_post = user.getString("user_name");

            JSONArray likes = jsonObject.getJSONArray("likes");

            for(int i=0;i<likes.length();i++)
            {
                this.AllLikesId.add(i,likes.getJSONObject(i).getInt("user_id"));
            }

            this.like = likes.length() + "";

            JSONArray commentsLimited = jsonObject.getJSONArray("comments_limited");

            this.comments_count = commentsLimited.length();

            for (int i = 0; i < commentsLimited.length(); i++) {
                try {

                    JSONObject jsonObject1 = commentsLimited.getJSONObject(i);
                    JSONObject user1 =jsonObject1.getJSONObject("user");
                    this.AllCommentId.add(i,user1.getInt("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
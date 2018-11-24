package com.hackerkernel.user.sqrfactor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PortfolioClass implements Serializable {
    private int id;
    private String likeCount,commentcount;
    private  String type,articleImageUrl,articleName,articleWirterName,likes,comments,short_description,title,slug;
    private JSONObject jsonObject;

    public PortfolioClass(int id,String likeCount,String commentcount,String articleImageUrl,String title, String short_description,String type,String articleName, String articleWirterName, String likes, String comments) {
        this.articleImageUrl = articleImageUrl;
        this.articleName = articleName;
        this.articleWirterName = articleWirterName;
        this.id = id;
        this.type = type;
        this.short_description = short_description;
        this.title = title;
        this.likeCount = likeCount;
        this.commentcount = commentcount;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public PortfolioClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {

            this.likeCount = jsonObject.getString("likecount");
            this.commentcount = jsonObject.getString("commentcount");
            JSONObject post = jsonObject.getJSONObject("post");
            this.id = post.getInt("id");
            this.type = post.getString("type");
            this.slug=post.getString("slug");
            this.short_description = post.getString("short_description");
            this.articleImageUrl = post.getString("banner_image");

            this.title = post.getString("title");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }


    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleWirterName() {
        return articleWirterName;
    }

    public void setArticleWirterName(String articleWirterName) {
        this.articleWirterName = articleWirterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getTitle() {
        return title;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
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




}
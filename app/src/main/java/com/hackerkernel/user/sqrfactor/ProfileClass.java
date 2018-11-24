package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileClass {
    private String type,credits_earned,is_Shared,deleted_at,paid_at,week_views,user_name_of_post;
    private String user_name,name,first_name,last_name,profile,email,mobile_number,user_type,post_time,postType,post_title,post_image,banner_image,short_description,description;
    private String like,comment,share;
    public String commentProfileImageUrl,commentUserName,commentTime,commentDescription,commentLike;
    private int user_id,post_id,post_user_id,sharedId, commentId;
    private JSONObject jsonObject;
    private String followerscnt,followingcnt,portfoliocnt,bluePrintcnt;
    public ArrayList<comments_limited> commentsLimitedArrayList=new ArrayList<>();

    public ProfileClass(String type, String credits_earned, String is_Shared, String deleted_at, String paid_at, String week_views, String user_name_of_post, String user_name, String name, String first_name, String last_name, String profile, String email, String mobile_number, String user_type, String post_time, String postType, String post_title, String post_image, String banner_image, String short_description, String description, String like, String comment, String share, String commentProfileImageUrl, String commentUserName, String commentTime, String commentDescription, String commentLike, int user_id, int post_id, int post_user_id, int sharedId, int commentId, JSONObject jsonObject, String followerscnt, String followingcnt, String portfoliocnt, String bluePrintcnt, ArrayList<comments_limited> commentsLimitedArrayList) {
        this.type = type;
        this.credits_earned = credits_earned;
        this.is_Shared = is_Shared;
        this.deleted_at = deleted_at;
        this.paid_at = paid_at;
        this.week_views = week_views;
        this.user_name_of_post = user_name_of_post;
        this.user_name = user_name;
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.email = email;
        this.mobile_number = mobile_number;
        this.user_type = user_type;
        this.post_time = post_time;
        this.postType = postType;
        this.post_title = post_title;
        this.post_image = post_image;
        this.banner_image = banner_image;
        this.short_description = short_description;
        this.description = description;
        this.like = like;
        this.comment = comment;
        this.share = share;
        this.commentProfileImageUrl = commentProfileImageUrl;
        this.commentUserName = commentUserName;
        this.commentTime = commentTime;
        this.commentDescription = commentDescription;
        this.commentLike = commentLike;
        this.user_id = user_id;
        this.post_id = post_id;
        this.post_user_id = post_user_id;
        this.sharedId = sharedId;
        this.commentId = commentId;
        this.jsonObject = jsonObject;
        this.followerscnt = followerscnt;
        this.followingcnt = followingcnt;
        this.portfoliocnt = portfoliocnt;
        this.bluePrintcnt = bluePrintcnt;
        this.commentsLimitedArrayList = commentsLimitedArrayList;
    }


    public ProfileClass(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.followingcnt = jsonObject.getString("followingCnt");
            this.followerscnt = jsonObject.getString("followCnt");
            this.portfoliocnt = jsonObject.getString("portfolioCnt");
            this.bluePrintcnt = jsonObject.getString("blueprintCnt");

            JSONObject user = jsonObject.getJSONObject("user");
            this.user_id = user.getInt("id");
            this.user_name = user.getString("user_name");
            this.first_name = user.getString("first_name");
            this.last_name = user.getString("last_name");
            this.profile = user.getString("profile");
            this.email = user.getString("email");
            this.mobile_number = user.getString("mobile_number");
            this.name = user.getString("name");

            JSONObject jsonPost = jsonObject.getJSONObject("posts");
            JSONArray jsonArrayData = jsonPost.getJSONArray("data");
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject post = jsonArrayData.getJSONObject(i);
                this.user_id = post.getInt("user_id");
                this.post_title = post.getString("title");
                this.banner_image = post.getString("banner_image");
                this.short_description = post.getString("short_description");
                this.description = post.getString("description");
                this.post_image = post.getString("image");
                this.post_time = post.getString("updated_at");
                this.comment = post.getString("comments_count");

                JSONArray likes = post.getJSONArray("likes");
                this.like = likes.length() + "";

                JSONArray commentsLimited = post.getJSONArray("comments_limited");

                for (int j = 0; j < commentsLimited.length(); j++) {
                    try {
                        JSONObject jsonObject1 = commentsLimited.getJSONObject(j);
                        this.commentId = jsonObject1.getInt("id");
                        comments_limited limited = new comments_limited(jsonObject1.getJSONArray("likes").length(), jsonObject1.getJSONObject("user").getString("first_name") + " " + jsonObject1.getJSONObject("user").getString("last_name"),
                                jsonObject1.getJSONObject("user").getString("profile"), jsonObject1.getInt("id"), jsonObject1.getInt("user_id"),
                                jsonObject1.getInt("commentable_id"), jsonObject1.getString("commentable_type"),
                                jsonObject1.getString("body"), jsonObject1.getString("created_at"), jsonObject1.getString("updated_at"));
                        this.commentsLimitedArrayList.add(limited);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getUser_name () {
        return user_name;
    }

    public void setUser_name (String user_name){
        this.user_name = user_name;
    }

    public String getName () {
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getFirst_name () {
        return first_name;
    }

    public void setFirst_name (String first_name){
        this.first_name = first_name;
    }

    public String getLast_name () {
        return last_name;
    }

    public void setLast_name (String last_name){
        this.last_name = last_name;
    }

    public String getProfile () {
        return profile;
    }

    public void setProfile (String profile){
        this.profile = profile;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getMobile_number () {
        return mobile_number;
    }

    public void setMobile_number (String mobile_number){
        this.mobile_number = mobile_number;
    }

    public String getUser_type () {
        return user_type;
    }

    public void setUser_type (String user_type){
        this.user_type = user_type;
    }

    public String getPost_time () {
        return post_time;
    }

    public void setPost_time (String post_time){
        this.post_time = post_time;
    }

    public String getPostType () {
        return postType;
    }

    public void setPostType (String postType){
        this.postType = postType;
    }

    public String getPost_title () {
        return post_title;
    }

    public void setPost_title (String post_title){
        this.post_title = post_title;
    }

    public String getPost_image () {
        return post_image;
    }

    public void setPost_image (String post_image){
        this.post_image = post_image;
    }

    public String getBanner_image () {
        return banner_image;
    }

    public void setBanner_image (String banner_image){
        this.banner_image = banner_image;
    }

    public String getShort_description () {
        return short_description;
    }

    public void setShort_description (String short_description){
        this.short_description = short_description;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description){
        this.description = description;
    }

    public String getLike () {
        return like;
    }

    public void setLike (String like){
        this.like = like;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredits_earned() {
        return credits_earned;
    }

    public void setCredits_earned(String credits_earned) {
        this.credits_earned = credits_earned;
    }

    public String getIs_Shared() {
        return is_Shared;
    }

    public void setIs_Shared(String is_Shared) {
        this.is_Shared = is_Shared;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public String getWeek_views() {
        return week_views;
    }

    public void setWeek_views(String week_views) {
        this.week_views = week_views;
    }

    public String getUser_name_of_post() {
        return user_name_of_post;
    }

    public void setUser_name_of_post(String user_name_of_post) {
        this.user_name_of_post = user_name_of_post;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment){
        this.comment = comment;
    }

    public String getShare () {
        return share;
    }

    public void setShare (String share){
        this.share = share;
    }

    public String getCommentProfileImageUrl () {
        return commentProfileImageUrl;
    }

    public void setCommentProfileImageUrl (String commentProfileImageUrl){
        this.commentProfileImageUrl = commentProfileImageUrl;
    }

    public String getCommentUserName () {
        return commentUserName;
    }

    public void setCommentUserName (String commentUserName){
        this.commentUserName = commentUserName;
    }

    public String getCommentTime () {
        return commentTime;
    }

    public void setCommentTime (String commentTime){
        this.commentTime = commentTime;
    }

    public String getCommentDescription () {
        return commentDescription;
    }

    public void setCommentDescription (String commentDescription){
        this.commentDescription = commentDescription;
    }

    public String getCommentLike () {
        return commentLike;
    }

    public void setCommentLike (String commentLike){
        this.commentLike = commentLike;
    }

    public int getUser_id () {
        return user_id;
    }

    public void setUser_id ( int user_id){
        this.user_id = user_id;
    }

    public int getPost_id () {
        return post_id;
    }

    public void setPost_id ( int post_id){
        this.post_id = post_id;
    }

    public int getPost_user_id () {
        return post_user_id;
    }

    public void setPost_user_id ( int post_user_id){
        this.post_user_id = post_user_id;
    }

    public int getSharedId () {
        return sharedId;
    }

    public void setSharedId ( int sharedId){
        this.sharedId = sharedId;
    }

    public int getCommentId () {
        return commentId;
    }

    public void setCommentId ( int commentId){
        this.commentId = commentId;
    }

    public JSONObject getJsonObject () {
        return jsonObject;
    }

    public void setJsonObject (JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public String getFollowerscnt () {
        return followerscnt;
    }

    public void setFollowerscnt (String followerscnt){
        this.followerscnt = followerscnt;
    }

    public String getFollowingcnt () {
        return followingcnt;
    }

    public void setFollowingcnt (String followingcnt){
        this.followingcnt = followingcnt;
    }

    public String getPortfoliocnt () {
        return portfoliocnt;
    }

    public void setPortfoliocnt (String portfoliocnt){
        this.portfoliocnt = portfoliocnt;
    }

    public String getBluePrintcnt () {
        return bluePrintcnt;
    }

    public void setBluePrintcnt (String bluePrintcnt){
        this.bluePrintcnt = bluePrintcnt;
    }

    public ArrayList<comments_limited> getCommentsLimitedArrayList () {
        return commentsLimitedArrayList;
    }

    public void setCommentsLimitedArrayList
            (ArrayList < comments_limited > commentsLimitedArrayList) {
        this.commentsLimitedArrayList = commentsLimitedArrayList;
    }
}
package com.hackerkernel.user.sqrfactor;

public class PostCommentClass {

    public PostCommentClass(String profileImage, String profileName, String commentTime, String commentMsg, String likeCount) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.commentTime = commentTime;
        this.commentMsg = commentMsg;
        this.likeCount = likeCount;
    }

    private String profileImage,profileName,commentTime,commentMsg,likeCount;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }


}

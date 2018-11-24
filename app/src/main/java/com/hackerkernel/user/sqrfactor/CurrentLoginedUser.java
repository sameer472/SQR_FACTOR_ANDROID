package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class CurrentLoginedUser implements Serializable {

    private int userId;
    private String userName,userProfileUrl;

    public CurrentLoginedUser(int userId, String userName, String userProfileUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userProfileUrl = userProfileUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }
}

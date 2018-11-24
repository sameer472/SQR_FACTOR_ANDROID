package com.hackerkernel.user.sqrfactor.Pojo;

import java.io.Serializable;

public class UserClass implements Serializable {
    private String id = String.valueOf(-1);  //TODO: Temporary arrangement, Remove it afterwards.
    private String name;
    private String profilePicPath;
    private String email;
    private String mobileNumber;

    public UserClass() {

    }

    public UserClass(String id, String name, String profilePicPath, String email, String mobileNumber) {
        this.id = id;
        this.name = name;
        this.profilePicPath = profilePicPath;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public UserClass(String name, String profilePicPath, String email, String mobileNumber) {
        this.name = name;
        this.profilePicPath = profilePicPath;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}

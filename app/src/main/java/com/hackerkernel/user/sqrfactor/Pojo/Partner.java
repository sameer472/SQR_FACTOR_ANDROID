package com.hackerkernel.user.sqrfactor.Pojo;

import java.io.Serializable;

public class Partner implements Serializable {
    private String name;
    private String website;
    private String email;
    private String contact;
    private String imagePath;

    public Partner(String name, String website, String email, String contact, String imagePath) {
        this.name = name;
        this.website = website;
        this.email = email;
        this.contact = contact;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

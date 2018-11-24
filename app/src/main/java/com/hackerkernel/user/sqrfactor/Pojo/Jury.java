package com.hackerkernel.user.sqrfactor.Pojo;

import java.io.Serializable;

public class Jury implements Serializable {
    private String name;
    private String company;
    private String email;
    private String contact;
    private String imagePath;

    public Jury(String name, String company, String email, String contact, String imagePath) {
        this.name = name;
        this.company = company;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

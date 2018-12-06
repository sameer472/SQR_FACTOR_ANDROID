package com.hackerkernel.user.sqrfactor.Pojo;

public class JuryClass {
    private int id;
    private String fullName;
    private String imageUrl;
    private String user_type;
    private String user_name;


    public JuryClass(int id, String fullName, String imageUrl,String user_type,String user_name) {
        this.id = id;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
        this.user_type=user_type;
        this.user_name=user_name;
    }
 public JuryClass(int id, String fullName, String imageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

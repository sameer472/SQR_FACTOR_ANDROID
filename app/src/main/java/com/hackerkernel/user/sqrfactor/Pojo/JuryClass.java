package com.hackerkernel.user.sqrfactor.Pojo;

public class JuryClass {
    private String id;
    private String fullName;
    private String imageUrl;

    public JuryClass(String id, String fullName, String imageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

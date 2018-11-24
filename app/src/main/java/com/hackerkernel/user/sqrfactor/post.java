package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class post implements Serializable {

    private String description,slug,title,type;
    private int id;

    public post(String description, String slug, String title, String type, int id) {
        this.description = description;
        this.slug = slug;
        this.title = title;
        this.type = type;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
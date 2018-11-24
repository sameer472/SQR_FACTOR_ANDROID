package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class commentLikeClass implements Serializable {
    private int id,user_id,likeable_id;
    private JSONObject jsonObject;
    private String likeable_type,created_at,updated_at,deleted_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLikeable_id() {
        return likeable_id;
    }

    public void setLikeable_id(int likeable_id) {
        this.likeable_id = likeable_id;
    }

    public String getLikeable_type() {
        return likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
    public commentLikeClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        {
            try {
                this.id=jsonObject.getInt("id");
                this.user_id=jsonObject.getInt("user_id");
                this.likeable_id=jsonObject.getInt("likeable_id");
                this.likeable_type=jsonObject.getString("likeable_type");
                this.created_at=jsonObject.getString("created_at");
                this.updated_at=jsonObject.getString("updated_at");
                this.deleted_at=jsonObject.getString("deleted_at");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


}
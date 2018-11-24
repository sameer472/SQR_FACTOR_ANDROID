package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class StateClass implements Serializable {

    private String name;
    private int id,countryId;
    private JSONObject jsonObject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public StateClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {
            this.countryId=jsonObject.getInt("country_id");
            this.name=jsonObject.getString("name");
            this.id=jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

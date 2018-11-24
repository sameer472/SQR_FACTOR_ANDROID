package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CitiesClass implements Serializable {

    private String name;
    private int id,countryId,stateId;
    private JSONObject jsonObject;

    public CitiesClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;


        try {
            this.name=jsonObject.getString("name");
            this.id=jsonObject.getInt("id");
            this.stateId=jsonObject.getInt("state_id");
            this.countryId=jsonObject.getInt("country_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
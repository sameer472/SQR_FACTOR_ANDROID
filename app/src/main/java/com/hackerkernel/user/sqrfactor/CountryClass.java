package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CountryClass implements Serializable {

    private int id,phoneCode;
    private String sortName,name;
    private JSONObject jsonObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryClass(JSONObject country)
    {

        this.jsonObject=country;
        try {
            this.id=jsonObject.getInt("id");
            this.sortName=jsonObject.getString("sortname");
            this.name=jsonObject.getString("name");
            this.phoneCode=jsonObject.getInt("phonecode");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

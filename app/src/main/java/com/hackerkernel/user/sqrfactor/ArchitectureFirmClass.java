package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

public class ArchitectureFirmClass {
        private int id;
        private String user_name, name, first_name, last_name, profile, email, mobile_number;
        private String user_type;
        private JSONObject jsonObject;

        public ArchitectureFirmClass(int id, String user_name, String name, String first_name, String last_name, String profile, String email, String mobile_number, String user_type, JSONObject jsonObject) {
            this.id = id;
            this.user_name = user_name;
            this.name = name;
            this.first_name = first_name;
            this.last_name = last_name;
            this.profile = profile;
            this.email = email;
            this.mobile_number = mobile_number;
            this.user_type = user_type;
            this.jsonObject = jsonObject;
        }
        public ArchitectureFirmClass(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
            try {
                this.id = jsonObject.getInt("id");
                this.profile = jsonObject.getString("profile");
                this.name = jsonObject.getString("name");
                this.user_name = jsonObject.getString("user_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public JSONObject getJsonObject() {
            return jsonObject;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }
    }


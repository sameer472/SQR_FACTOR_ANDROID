package com.hackerkernel.user.sqrfactor.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChatFriends implements Serializable {
    public String userName,userProfile,name,first_name,last_name,unread_count;
    public int userID;
    public transient JSONObject jsonObject=null;
    public String isOnline;
    public String lastSeen;
    public String chat,status,created_at;



    public ChatFriends() {

    }
    public ChatFriends(String userName, String userProfile, int userID) {
        this.userProfile = userProfile;
        this.userName = userName;
        this.userID = userID;

    }

    public ChatFriends(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {

            JSONObject friend_detail=jsonObject.getJSONObject("friend_detail");
            this.userID = friend_detail.getInt("id");
            this.first_name = friend_detail.getString("first_name");
            this.last_name=friend_detail.getString("last_name");
            this.userName=friend_detail.getString("user_name");
            this.name=friend_detail.getString("name");
            this.userProfile = friend_detail.getString("profile");

            JSONObject last_message=jsonObject.getJSONObject("last_message");
            this.chat=last_message.getString("chat");
            this.status=last_message.getString("status");
            this.created_at=last_message.getString("created_at");

            this.unread_count=jsonObject.getString("unread_count");




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getLastSeen() {
        return lastSeen;
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

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
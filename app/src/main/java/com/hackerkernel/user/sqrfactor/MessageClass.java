package com.hackerkernel.user.sqrfactor;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageClass {
      private int messageId,userFrom,userTo;
      private String chat,status,createdAt,updatedAt,senderProfile;

      private JSONObject jsonObject;

    public MessageClass(int messageId, int userFrom, int userTo, String chat, String status, String createdAt, String updatedAt) {
        this.messageId = messageId;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.chat = chat;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MessageClass(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;

        try {
            this.messageId = jsonObject.getInt("id");
            this.userFrom = jsonObject.getInt("user_from");
            this.userTo = jsonObject.getInt("user_to");
            this.chat = jsonObject.getString("chat");
            this.status=jsonObject.getString("status");
            this.createdAt=jsonObject.getString("created_at");
            this.updatedAt=jsonObject.getString("updated_at");




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
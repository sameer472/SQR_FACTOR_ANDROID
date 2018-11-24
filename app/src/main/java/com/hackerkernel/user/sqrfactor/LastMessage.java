package com.hackerkernel.user.sqrfactor;

public class LastMessage {

    private int chat_id;
    private int senderId;
    private String message;
    private String senderName;


    public LastMessage()
    {

    }


    public LastMessage(int senderId, String message,String senderName) {
        this.senderId = senderId;
        this.message = message;
        this.senderName=senderName;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
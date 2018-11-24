package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class IsOnline implements Serializable {
    private String isOnline,time;


    public IsOnline()
    {

    }

    public IsOnline(String isOnline, String time) {
        this.isOnline = isOnline;
        this.time = time;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.hackerkernel.user.sqrfactor.Pojo;

public class Skillsbean {
    int index;
    String text;

    public Skillsbean() {
    }

    public Skillsbean(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

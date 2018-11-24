package com.hackerkernel.user.sqrfactor.Pojo;


import java.io.Serializable;

public class Award implements Serializable {
    private String type;
    private String amount;
    private String currencyUnit;
    private String details;

    public Award(String type, String amount, String currencyUnit, String details) {
        this.type = type;
        this.amount = amount;
        this.currencyUnit = currencyUnit;
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

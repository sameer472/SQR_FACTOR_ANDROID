package com.hackerkernel.user.sqrfactor.Pojo;

public class PrizeClass {
    private String id;
    private String type;
    private String amount;
    private String currency;
    private String extra;

    public PrizeClass(String id, String type, String amount, String currency, String extra) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.extra = extra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}

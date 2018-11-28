package com.hackerkernel.user.sqrfactor.Pojo;

public class PaymentDetails {
    private String response_type;
    private String environment,paypal_sdk_version,platform,product_name;
    private String create_time,id,intent,state;

    public PaymentDetails(String response_type, String environment, String paypal_sdk_version, String platform, String product_name, String create_time, String id, String intent, String state) {
        this.response_type = response_type;
        this.environment = environment;
        this.paypal_sdk_version = paypal_sdk_version;
        this.platform = platform;
        this.product_name = product_name;
        this.create_time = create_time;
        this.id = id;
        this.intent = intent;
        this.state = state;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getPaypal_sdk_version() {
        return paypal_sdk_version;
    }

    public void setPaypal_sdk_version(String paypal_sdk_version) {
        this.paypal_sdk_version = paypal_sdk_version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

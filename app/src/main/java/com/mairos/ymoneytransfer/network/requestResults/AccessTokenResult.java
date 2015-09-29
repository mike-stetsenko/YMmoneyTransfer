package com.mairos.ymoneytransfer.network.requestResults;

public class AccessTokenResult {

    private String access_token = "";

    private String error = "";

    public String getError() {
        return error;
    }

    public String getAccessToken() {
        return access_token;
    }
}

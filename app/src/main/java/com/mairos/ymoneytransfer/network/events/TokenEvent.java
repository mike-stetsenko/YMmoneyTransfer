package com.mairos.ymoneytransfer.network.events;

public class TokenEvent {

    private boolean result = false;

    private String msg = "";

    private String accessToken = "";

    public TokenEvent(boolean result, String msg, String token) {
        this.msg = msg;
        this.result = result;
        this.accessToken = token;
    }

    public boolean isResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

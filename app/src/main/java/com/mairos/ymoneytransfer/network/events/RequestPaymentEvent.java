package com.mairos.ymoneytransfer.network.events;

import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResult;

public class RequestPaymentEvent {

    private RequestPaymentResult result;
    private String token;

    public RequestPaymentEvent(RequestPaymentResult result, String token) {
        this.result = result;
        this.token = token;
    }

    public RequestPaymentResult getResult() {
        return result;
    }

    public String getToken() {
        return token;
    }
}

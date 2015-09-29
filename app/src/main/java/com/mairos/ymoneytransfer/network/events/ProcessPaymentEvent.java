package com.mairos.ymoneytransfer.network.events;

import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResult;

public class ProcessPaymentEvent {

    private ProcessPaymentResult result;

    private String protectionCode;

    public ProcessPaymentEvent(ProcessPaymentResult result, String protectionCode) {
        this.result = result;
        this.protectionCode = protectionCode;
    }

    public ProcessPaymentResult getResult() {
        return result;
    }

    public String getProtectionCode() {
        return protectionCode;
    }
}

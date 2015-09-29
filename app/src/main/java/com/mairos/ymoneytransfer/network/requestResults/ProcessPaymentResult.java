package com.mairos.ymoneytransfer.network.requestResults;

public class ProcessPaymentResult {
    @ProcessPaymentResultCode
    private String status = "";
    private String error = "";
    private String payment_id = "";
    private String balance = "";
    private String invoice_id = "";
    private String payer = "";
    private String payee = "";
    private String credit_amount = "";
    private String account_unblock_uri = "";
    private String hold_for_pickup_link = "";
    private String acs_uri = "";
    private String acs_params = "";
    private String next_retry = "";
    private String digital_goods = "";

    @ProcessPaymentResultCode
    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPaymentId() {
        return payment_id;
    }

    public String getCreditAmount() {
        return credit_amount;
    }

    public String getPayee() {
        return payee;
    }
}

package com.mairos.ymoneytransfer.storage.models;

import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResultCode;

public class PaymentInfo {
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
    private String protection_code = "";
    private long time;

    public PaymentInfo(){
    }

    public PaymentInfo(String protection_code, String payment_id, String payee,
                       String credit_amount, long time) {
        this.protection_code = protection_code;
        this.payment_id = payment_id;
        this.payee = payee;
        this.credit_amount = credit_amount;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String getPaymentId() {
        return payment_id;
    }

    public String getPayee() {
        return payee;
    }

    public String getProtectionCode() {
        return protection_code;
    }

    public String getCreditAmount() {
        return credit_amount;
    }
}

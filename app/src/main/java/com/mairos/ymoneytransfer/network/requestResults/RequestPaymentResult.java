package com.mairos.ymoneytransfer.network.requestResults;

public class RequestPaymentResult {
    @RequestPaymentResultCode
    private String status = "";
    private String error = "";
    private String error_description = "";
    private Object money_source = "";
    private String request_id = "";
    private String contract_amount = "";
    private String balance = "";
    private String recipient_account_status = "";
    private String recipient_account_type = "";
    private String protection_code = "";
    private String account_unblock_uri = "";
    private String ext_action_uri = "";

    @RequestPaymentResultCode
    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getRequestId() {
        return request_id;
    }

    public String getContractAmount() {
        return contract_amount;
    }

    public String getProtectionCode() {
        return protection_code;
    }

    public String getErrorDescription() {
        return error_description;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setErrorDescription(String error_description) {
        this.error_description = error_description;
    }
}
